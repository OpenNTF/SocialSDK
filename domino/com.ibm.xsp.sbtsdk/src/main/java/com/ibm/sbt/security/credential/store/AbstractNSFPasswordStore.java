/*
 * © Copyright IBM Corp. 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.security.credential.store;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.SystemCache;
//import com.ibm.domino.password.PasswordException;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.security.authentication.password.consumer.UserPassword;
import com.ibm.sbt.security.credential.store.CredentialStore;
import com.ibm.xsp.model.domino.CredStoreUtil;
import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Domino OAuth application token store.
 * @author Philippe Riand
 * @author Padraic Edwards
 */
public abstract class AbstractNSFPasswordStore implements CredentialStore {
	
	public static final String PASSWORDSTORE_VIEW      = "PasswordStore";
	public static final String PASSWORDSTORE_FORM      = "PasswordStore";
	private static final String CLASSNAME = AbstractNSFPasswordStore.class.getName();
	private static final Logger logger = Logger.getLogger( CLASSNAME );

    protected class Context {
        private Session designerSession;
        private Session userSession;
        protected Context() {
        }
        protected void recyle() {
            if(designerSession!=null) {
                try {
                    AbstractNSFPasswordStore.this.releaseDesignerSession(designerSession);
                } catch(Throwable t) {Platform.getInstance().log(t);}
                designerSession = null;
            }
            if(userSession!=null) {
                try {
                    AbstractNSFPasswordStore.this.releaseUserSession(userSession);
                } catch(Throwable t) {Platform.getInstance().log(t);}
                userSession = null;
            }
        }
        public Session getDesignerSession() throws NotesException {
            if(designerSession==null) {
                designerSession = AbstractNSFPasswordStore.this.getDesignerSession();
            }
            return designerSession;
        }
        public Session getUserSession() throws NotesException {
            if(userSession==null) {
                userSession = AbstractNSFPasswordStore.this.getUserSession();
            }
            return userSession;
        }
    }
	
	private String database;

	//should be true?
	private boolean securityEnabled=true;
    private boolean checkConsumerInfoUpdatedBy = true;

	private boolean inMemoryCache = true;
	private int passwordCacheSize = 400;
	private SystemCache passwordCache;

	public AbstractNSFPasswordStore() {
	}

	//
	// Abstract methods to be overloaded by an actual implementation
	//
	protected abstract Session getDesignerSession() throws NotesException;
    protected abstract Session getUserSession() throws NotesException;

	protected void releaseDesignerSession( Session session ) {
	}
    protected void releaseUserSession( Session session ) {
    }
    
    protected String findDatabase() {
        String s = getDatabase();
        if(StringUtil.isEmpty(s)) {
            s = getDefaultDatabase();
        }
        return s;
    }
    protected String getDefaultDatabase() {
        return null;
    }
    
	protected SystemCache getUserPasswordCache() {
        if(passwordCache==null) {
            if(inMemoryCache) {
			    passwordCache = new SystemCache("Password store",passwordCacheSize);
			}
		}
        return passwordCache;
	}

	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}

	public boolean isInMemoryCache() {
		return inMemoryCache;
	}
	public void setInMemoryCache(boolean inMemoryCache) {
		this.inMemoryCache = inMemoryCache;
        if(!inMemoryCache) {
            passwordCache = null;
        }
	}

	public boolean isSecurityEnabled() {
		return securityEnabled;
	}
	public void setSecurityEnabled(boolean securityEnabled) {
		this.securityEnabled = securityEnabled;
	}

	public boolean isCheckConsumerInfoUpdatedBy() {
		return checkConsumerInfoUpdatedBy;
	}
	public void setCheckConsumerInfoUpdatedBy(boolean checkConsumerInfoUpdatedBy) {
		this.checkConsumerInfoUpdatedBy = checkConsumerInfoUpdatedBy;
	}

	public int getPasswordCacheSize() {
		return passwordCacheSize;
	}
	public void setPasswordCacheSize(int passwordCacheSize) {
		this.passwordCacheSize = passwordCacheSize;
	}


	//
	// Password management
	//
	public UserPassword loadUserPassword(String requestUri) throws PasswordException {
    	logger.entering(CLASSNAME, "loadUserPassword");
		Context context = new Context();
		try {
			SystemCache cache = getUserPasswordCache();
			
			String userId = context.getUserSession().getEffectiveUserName();
			String key = getPasswordViewKey(requestUri, userId);
			if(cache!=null) {
				UserPassword password = null;
				synchronized (cache) {
					password = (UserPassword)cache.get(key);
				}
				if(password!=null) {
					// Ok, verify that the token is correct					
				    String designerId=context.getUserSession().getEffectiveUserName();
					
				    //if(StringUtil.equals(password.verifierId, designerId)) {
				    if(StringUtil.equals(userId, designerId)) {
						return password;
					}
					// Else it is invalid
				}
			}

			Database db  = CredStoreUtil.getCredStoreDB(DominoUtils.getCurrentSession());
			View v = db.getView(PASSWORDSTORE_VIEW);
			Vector<Object> keys = new Vector<Object>();
			keys.add(requestUri);
			keys.add(userId);
			ViewEntryCollection col = v.getAllEntriesByKey(keys, true);

			ViewEntry entry = col.getFirstEntry();
			if (entry != null && entry.isValid()) {
				
			    Document doc = entry.getDocument();
			    try {
	                // Verify that the designer id is compatible
	                String userName= doc.getItemValueString("username");
	                String password = null;
	                	 
	                	byte[] passwordData = CredStoreUtil.decryptItemInDoc(db, requestUri,context.getUserSession().getEffectiveUserName(),doc.getUniversalID(), "Password");   
	                	
	                	if (passwordData != null) {
							InputStream is = new ByteArrayInputStream(passwordData);
							Properties _accessTokenProps = new Properties();
							try {
								_accessTokenProps.load(is);
								password = (String) _accessTokenProps.keys().nextElement();
							} catch (IOException e) {
								e.printStackTrace();
							}

						UserPassword token = new UserPassword( userName, password);
	                    if(cache!=null) {
	                        synchronized (cache) {
	                            cache.put(key, token);
	                        }
	                    }
	    
	                    // if we don't care about who updated, then return the first encountered
	                    // otherwise check before returning.
	                    if (!checkConsumerInfoUpdatedBy) {
	                        return token;
	                    } else {
	                        // Check that the updatedBy field matches the creator.  If it doesn't, then 
	                        // consider it not found
	                        Object updatedBy = doc.getItemValue("$UpdatedBy");
	                        if(checkUpdatedBy(updatedBy, context.getUserSession().getEffectiveUserName())) {
	                            return token;
	                        } 
	                    }
	                }
			    } finally {
			        doc.recycle();
			    }
			}
			
			return null;
		} catch(NotesException ex) {
			throw new PasswordException(ex);
		} finally {
			context.recyle();
		}
	}
    
    public void saveUserPassword(String requestUri, String userId, String password) throws PasswordException {
        Context context = new Context();
        try {
            saveAccessTokenToNSF(context, requestUri, userId,null);
        } finally {
            context.recyle();
        }
    }
    protected Document saveAccessTokenToNSF(Context context, String requestUri, String userName, UserPassword userPwd) throws PasswordException {
        try {
            String userId = context.getUserSession().getEffectiveUserName();
            
            // Create or load the token document
            Database db = CredStoreUtil.getCredStoreDB(DominoUtils.getCurrentSession());
            View v = db.getView(PASSWORDSTORE_VIEW);
            Vector<Object> keys = new Vector<Object>();
            keys.add(requestUri);
            keys.add(userName);

            Document doc = null;
        	ViewEntryCollection col = v.getAllEntriesByKey(keys, true);

			ViewEntry entry = col.getFirstEntry();
			if (entry != null && entry.isValid()) {
                Vector<Object> values = entry.getColumnValues();

                // Check that the updatedBy field matches the designer
                Object updatedBy = values.get(3);
                if(isCheckConsumerInfoUpdatedBy() && !checkUpdatedBy(updatedBy, userId)) {
                    // we found a matching document, but the updated by did not match
                    // this is a rogue document in the database and we should delete it, as there
                    // should not be more than one match per key
                    entry.getDocument().remove(true);
                }
                
                // found a match -- shouldn't be more than one, but allow any other matches to be cleaned up
                doc = entry.getDocument();
                
                return doc;
            }

            HashMap<String, String> itemsValues= new HashMap<String, String>();
	        HashMap<String, String> itemsValuesToEncrypt= new HashMap<String, String>();
	       
            if (doc == null) {            	          	
                itemsValues.put("Form", PASSWORDSTORE_FORM);
                itemsValues.put("ServerUri", requestUri);
                itemsValues.put("UserId", context.getUserSession().getEffectiveUserName());
                itemsValues.put("DesignerId", context.getDesignerSession().getEffectiveUserName());              
                itemsValues.put("UserName",userPwd.getUser());
            }

            itemsValuesToEncrypt.put("Password",userPwd.getPassword());
            
            String encryptedDocId=CredStoreUtil.createDocWithEncryption(db,requestUri, 
            		context.getUserSession().getEffectiveUserName(), itemsValues, itemsValuesToEncrypt);
            doc=db.getDocumentByUNID(encryptedDocId);
            
            // If security is enabled (default) then we should add the current user
            // as an Reader and Author
            if(isSecurityEnabled()) {
                // Calculate the list of readers/authors
                Vector<String> sec = getSecurityItems(context);
                
                // And set the fields value
                Item r = doc.replaceItemValue("Readers", sec);
                r.setReaders(true);
                Item w = doc.replaceItemValue("Authors", sec);
                w.setAuthors(true);
                
                doc.save();
            }

            // Make sure the view is refreshed for this session
            v.refresh();

            // Now, store it is the cache
            SystemCache cache = getUserPasswordCache();
            if(cache!=null) {
                String key = getPasswordViewKey(requestUri, userId);
                synchronized (cache) {
                    cache.put(key,new UserPassword(userPwd.getUser(), userPwd.getPassword()));
                }
            }
            
            return doc;
        } catch(NotesException ex) {
            throw new PasswordException(ex);
        }
    }
    
    protected Vector<String> getSecurityItems(Context context) throws NotesException {
        // Should it be the designer or the user session?
        Vector<String> v = new Vector<String>();
        v.add(context.getUserSession().getEffectiveUserName());
        return v;
    }
    
    public void deleteUserPassword(String requestUri) throws PasswordException {
        Context context = new Context();
        try {
            String userId = context.getUserSession().getEffectiveUserName();

            // Remove from in the in-memory cache
            SystemCache cache = getUserPasswordCache();
            if(cache!=null) {
                synchronized (cache) {
                    String key = getPasswordViewKey(requestUri, userId);
                    cache.remove(key);
                }
            }

            // And remove from the database
            Database db = CredStoreUtil.getCredStoreDB(DominoUtils.getCurrentSession());
            View v = db.getView(PASSWORDSTORE_VIEW);
            Vector<Object> keys = new Vector<Object>();
            keys.add(requestUri);
            keys.add(userId);

        	ViewEntryCollection col = v.getAllEntriesByKey(keys, true);

			ViewEntry entry = col.getFirstEntry();
			if (entry != null && entry.isValid()) {
                entry.getDocument().remove(true);
            }
        } catch(NotesException ex) {
            throw new PasswordException(ex);
        } finally {
            context.recyle();
        }
    }

	protected String getPasswordViewKey(String requestUri, String userId) throws PasswordException {
		StringBuilder sb = new StringBuilder( 200 );
		sb.append(requestUri).append('&').append(userId);
		return sb.toString();
	}

	protected boolean checkUpdatedBy(Object updatedBy, String userName) {
		if(updatedBy instanceof String) {
			return StringUtil.equals((String)updatedBy, userName);
		}
		if(updatedBy instanceof Vector) {
			Vector<Object> v = (Vector<Object>)updatedBy;
			if(!v.isEmpty()) {
				for(int i=0; i<v.size(); i++) {
					Object o = v.get(i);
					if(!(o instanceof String)) {
						return false;
					}
					if(!StringUtil.equals((String)o, userName)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
}
