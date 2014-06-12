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
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.SystemCache;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.credential.store.BaseStore;
import com.ibm.xsp.model.domino.CredStoreUtil;
import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Domino OAuth application token store.
 * @author Philippe Riand
 * @author Padraic Edwards
 */
public abstract class AbstractNSFTokenStore<A, C> extends BaseStore
{
	private static final String CLASSNAME = AbstractNSFTokenStore.class.getName();
	private static final Logger logger = Logger.getLogger( CLASSNAME );

	private boolean securityEnabled = true;
	private String securityEntries;

	private boolean inMemoryCache = true;
	private int consumerCacheSize = 6;
	private SystemCache consumerCache;
	private int accessCacheSize = 400;
	private SystemCache accessCache;
	private boolean checkConsumerInfoUpdatedBy = true;
	
	private String consumerTokensViewName;
	private String accessTokensViewName;
	private String consumerTokensCacheName;
	private String accessTokensCacheName;
	
	protected static class Holder<T> {
		public T heldObject;
	}

	public AbstractNSFTokenStore(String consumerTokensViewName,
			                     String accessTokensViewName,
			                     String consumerTokensCacheName,
			                     String accessTokensCacheName) {
		this.consumerTokensViewName = consumerTokensViewName;
		this.accessTokensViewName = accessTokensViewName;
		this.consumerTokensCacheName = consumerTokensCacheName;
		this.accessTokensCacheName = accessTokensCacheName;
	}

	//
	// Abstract methods to be overloaded by an actual implementation
	//
	protected abstract Session getConsumerStoreSession() throws NotesException;
	protected abstract Session getAccessStoreSession() throws NotesException;

	protected void releaseConsumerStoreSession( Session session )
	{
	}
	
	protected void releaseAccessStoreSession( Session session )
	{
	}
    
	protected SystemCache getConsumerTokenCache() {
		if(inMemoryCache) {
			if(consumerCache==null) {
				consumerCache = new SystemCache(consumerTokensCacheName, getConsumerCacheSize());;
			}
			return consumerCache;
		}
		return null;
	}
    protected boolean shouldRefreshConsumerTokenCache() {
        return false;
    }
    protected void refreshConsumerTokenCache() {
        if(consumerCache!=null) {
            consumerCache.clear();
        }
    }

    protected SystemCache getAccessTokenCache() {
		if(inMemoryCache) {
			if(accessCache==null) {
				accessCache = new SystemCache(accessTokensCacheName, getAccessCacheSize());
			}
			return accessCache;
		}
		return null;
	}
    protected boolean shouldRefreshAccessTokenCache() {
        return false;
    }
    protected void refreshAccessTokenCache() {
        if(accessCache!=null) {
            accessCache.clear();
        }
    }

	public boolean isInMemoryCache() {
		return inMemoryCache;
	}
	public void setInMemoryCache(boolean inMemoryCache) {
		this.inMemoryCache = inMemoryCache;
	}

	public boolean isSecurityEnabled() {
		return securityEnabled;
	}
	public void setSecurityEnabled(boolean securityEnabled) {
		this.securityEnabled = securityEnabled;
	}

	public String getSecurityEntries() {
		return securityEntries;
	}
	public void setSecurityEntries(String securityEntries) {
		this.securityEntries = securityEntries;
	}

	public boolean isCheckConsumerInfoUpdatedBy() {
		return checkConsumerInfoUpdatedBy;
	}

	public void setCheckConsumerInfoUpdatedBy(boolean checkConsumerInfoUpdatedBy) {
		this.checkConsumerInfoUpdatedBy = checkConsumerInfoUpdatedBy;
	}


	public int getConsumerCacheSize() {
		return consumerCacheSize;
	}
	public void setConsumerCacheSize(int consumerCacheSize) {
		this.consumerCacheSize = consumerCacheSize;
	}

	public int getAccessCacheSize() {
		return accessCacheSize;
	}
	
	public void setAccessCacheSize(int accessCacheSize) {
		this.accessCacheSize = accessCacheSize;
	}

	//
	// Application tokens
	//

	protected abstract C createConsumerToken( Document doc ) throws NotesException;
	protected abstract boolean verifyConsumerToken( C token, Session session ) throws NotesException;
	
	protected C loadConsumerTokenFromNSF(String appId, String serviceName) throws OAuthException {
		final String _method = "loadConsumerTokenFromNSF";
		logger.entering( CLASSNAME, _method, new Object[] { appId, serviceName} );
		
		Session session = null;
		try {
			session = getConsumerStoreSession();
			
			// Get the cache and refresh it if necessary 
			SystemCache cache = getConsumerTokenCache();
			if(shouldRefreshConsumerTokenCache()) {
			    refreshConsumerTokenCache();
			}
			
			String key = getConsumerViewKey(appId, serviceName);
			if(cache!=null) {
				C token = null;
				synchronized (cache) {
					token = (C)cache.get(key);
				}
				if(token!=null) {
					// Ok, verify that the token is correct
					logger.logp( Level.FINEST, CLASSNAME, _method, "Token found in caches" );
					
					try {
						if(verifyConsumerToken( token, session )) {
							logger.exiting( CLASSNAME, _method, token );
							return token;
						}
						// Else it is invalid
					} catch(NotesException ex) {
						throw new OAuthException(ex);
					}
				}
			}
			
			Database db = CredStoreUtil.getCredStoreDB(DominoUtils.getCurrentSession());
			View v = db.getView(consumerTokensViewName);
			Vector<Object> keys = new Vector<Object>();
			keys.add(appId);
			keys.add(serviceName);
			
            ViewEntryCollection col = v.getAllEntriesByKey(keys,true);
            // This entry either exists or it doesn't (Exact Match - SPR# RJBR9EYLC2)
            ViewEntry entry = col.getFirstEntry();
            if(entry != null && entry.isValid()) {

				
			    Document doc = entry.getDocument();
			    if (doc != null) {
				    try {
				    	C token = createConsumerToken( doc );
		                if(cache!=null) {
		                    synchronized (cache) {
		                        cache.put(key, token);
		                    }
		                }
	
		                // if we don't care about who updated, then return the first encountered
		                // otherwise check before returning.
		                if (!isCheckConsumerInfoUpdatedBy()) {
		                	logger.exiting( CLASSNAME, _method, token );
		                    return token;
		                } else {
		                    // Check that the updatedBy field matches the creator.  If it doesn't, then 
		                    // consider it not found
		                    Object updatedBy = doc.getItemValue("$UpdatedBy");
;
		                    if(checkUpdatedBy(updatedBy, DominoUtils.getCurrentSession().getEffectiveUserName())) {
		                    	logger.exiting( CLASSNAME, _method, token );
		                        return token;
		                    } 
		                }
				    } finally {
				        doc.recycle();
				    }
			    }
			}
			
			throw new OAuthException(null,"No application is registered with id {0} and provider {1}",appId,serviceName);
		} catch (OAuthException oae) {
			
			logger.throwing( CLASSNAME, _method, oae );			
			throw oae;
			
		} catch(NotesException ex) {
			OAuthException oae = new OAuthException(ex);
			logger.throwing( CLASSNAME, _method, oae );
			throw oae;
		} finally {
			releaseConsumerStoreSession( session );
		}
	}

	
	protected String getConsumerViewKey(String appId, String serviceName) throws OAuthException {
		StringBuilder sb = new StringBuilder( 200 );
		sb.append( appId ).append( '&' ).append( serviceName );
		return sb.toString();
	}

	//
	// User tokens
	//
	
	protected abstract Document writeAccessTokenToDocument( Database db, Document doc, A oToken ) throws NotesException;
	protected abstract A createAccessToken( Document doc, String consumerKey, String accessKey, String accessSecret ) throws NotesException;
	protected abstract int getUpdatedByIndex();
	protected abstract boolean verifyAccessToken( A token, Session session ) throws NotesException;
	
	protected A loadAccessTokenFromNSF( String accessViewKey, Vector<Object> dbKeys ) throws OAuthException, NotesException
	{
		final String _method = "loadAccessTokenFromNSF";
		logger.entering( CLASSNAME, _method, new Object[] { accessViewKey, dbKeys } );
		
		Session session = null;
		try {
			session = getAccessStoreSession();

			// Get the cache and refresh it if necessary
			SystemCache cache = getAccessTokenCache();
			if (shouldRefreshAccessTokenCache()) {
				refreshAccessTokenCache();
			}

			// Look in the in-memory cache
			if (cache != null) {
				A token = null;
				synchronized (cache) {
					token = (A) cache.get(accessViewKey);
				}
				if (token != null) {
					logger.logp(Level.FINEST, CLASSNAME, _method,
							"Token found in cache");

					// Ok, verify that the token is correct
					try {
						if (verifyAccessToken(token, session)) {
							logger.exiting(CLASSNAME, _method, token);
							return token;
						}
						// Else it is invalid
					} catch (NotesException ex) {
						throw new OAuthException(ex);
					}
				}
			}

			Database db = CredStoreUtil.getCredStoreDB(session);
			View v = db.getView(accessTokensViewName);

			A result = null;
			ViewEntryCollection col = v.getAllEntriesByKey(dbKeys, true);

			ViewEntry entry = col.getFirstEntry();
			if (entry != null && entry.isValid()) {

				Document doc = entry.getDocument();

				if (doc != null) {
					boolean docRemoved = false;

					String consumerKey = doc.getItemValueString(NSFTokenStore10.TF_CONSUMERKEY);
					String applicationId = (doc.getItemValueString(NSFTokenStore10.TF_APPID));
					String serviceId = doc.getItemValueString(NSFTokenStore10.TF_SERVICE);

						byte[] accessSecretData = CredStoreUtil
								.decryptItemInDoc(db, applicationId, serviceId,
										doc.getUniversalID(),
										NSFTokenStore10.TF_ACCESSSECRET);
						byte[] accessKeyData = CredStoreUtil.decryptItemInDoc(
								db, applicationId, serviceId,
								doc.getUniversalID(),
								NSFTokenStore10.TF_ACCESSTOKEN);

						String accessKey = null;
						String accessSecret = null;

						if (accessSecretData != null) {
							InputStream is = new ByteArrayInputStream(
									accessSecretData);
							Properties _accessTokenProps = new Properties();
							try {
								_accessTokenProps.load(is);
								accessSecret = (String) _accessTokenProps
										.keys().nextElement();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (accessKeyData != null) {
							InputStream is = new ByteArrayInputStream(
									accessKeyData);
							Properties _accessTokenProps = new Properties();
							try {
								_accessTokenProps.load(is);
								accessKey = (String) _accessTokenProps.keys()
										.nextElement();
							} catch (IOException e) {
								e.printStackTrace();
							}
						
						try {
							if (isCheckConsumerInfoUpdatedBy()) {
								// Check that the updatedBy field matches the
								// designer
								Object updatedBy = doc.getItemValue("$UpdatedBy");
								if (!checkUpdatedBy(updatedBy,
										session.getEffectiveUserName())) {
									// we found a matching document, but the
									// updated by did not match
									// this is a rogue document in the database
									// and we should delete it, as there
									// should not be more than one match per key
									doc.remove(true);
									docRemoved = true;
									String msg = StringUtil
											.format("Access token document removed: document for {0} has been updated by another user {1}",
													session.getEffectiveUserName(),
													updatedBy); // $NLW-AbstractNSFTokenStore.Accesstokendocumentremoveddocumen-1$
									
								}

								if (!docRemoved) {
									A token = createAccessToken(doc,consumerKey, accessKey,	accessSecret);

									if (cache != null) {
										synchronized (cache) {
											cache.put(accessViewKey, token);
										}
									}

									// set up the return value
									result = token;
								}
							}
						} finally {
							doc.recycle();
						}
					}
				}
			}
			
			logger.exiting( CLASSNAME, _method, result );
			return result;
            } 
		finally {
			releaseAccessStoreSession( session );
		}
	}

	
	protected void saveAccessTokenToNSF(Session session, A token, String accessViewKey, Vector<Object> dbKeys) throws OAuthException 
	{
		final String _method = "saveAccessTokenToNSF";
		logger.entering( CLASSNAME, _method, new Object[] { token, accessViewKey, dbKeys } ); 
		try {
			Database db = CredStoreUtil.getCredStoreDB(DominoUtils.getCurrentSession());;
			
			View v = db.getView(accessTokensViewName);

			Document doc = null;
			ViewEntryCollection col = v.getAllEntriesByKey(dbKeys, true);

			ViewEntry entry = col.getFirstEntry();
			if (entry != null && entry.isValid()) {

				Document tempDoc = entry.getDocument();
				
				Vector<Object> values = (Vector<Object>)entry.getColumnValues();

				// Check that the updatedBy field matches the designer
				Object updatedBy = values.get( getUpdatedByIndex() );
				if(!checkUpdatedBy(updatedBy, session.getEffectiveUserName())) {
					// we found a matching document, but the updated by did not match
					// this is a rogue document in the database and we should delete it, as there
					// should not be more than one match per key
					tempDoc.remove(true);
				}
				
				doc = tempDoc;
			}

			doc = writeAccessTokenToDocument( db, doc, token );

			// If security is enabled (default) then we should add the current user
			// as an Reader and Author
			if(isSecurityEnabled()) {
				// Calculate the list of readers/authors
				Vector<Object> sec = new Vector<Object>();
				sec.add(session.getEffectiveUserName());
				String s = getSecurityEntries();
				if(StringUtil.isNotEmpty(s)) {
					String[] ss = StringUtil.splitString(s, ';');
					for(int i=0; i<ss.length; i++) {
						if(StringUtil.isNotEmpty(ss[i])) {
							sec.add(ss[i]);
						}
					}
				}
				// And set the fields value
				Item r = doc.replaceItemValue("Readers", sec);
				r.setReaders(true);
				Item w = doc.replaceItemValue("Authors", sec);
				w.setAuthors(true);
			}
			doc.save();

			// Make sure the view is refreshed for this session
			v.refresh();

			// Now, store it is the cache
			SystemCache cache = getAccessTokenCache();
			if(cache!=null) {
				synchronized (cache) {
					cache.put(accessViewKey,token);
				}
			}
		} catch(NotesException ex) {
			OAuthException oae = new OAuthException(ex);
			logger.throwing( CLASSNAME, _method, oae );
			throw oae;
		}
		logger.exiting( CLASSNAME, _method );
	}


   	protected void deleteAccessTokenFromNSF( String accessViewKey, Vector<Object> dbKeys ) throws OAuthException
   	{
   		final String _method = "deleteAccessTokenFromNSF";
   		logger.entering( CLASSNAME, _method, new Object[] { accessViewKey, dbKeys } );
   		
		Session session = null;
		try {
			session = getAccessStoreSession();

			SystemCache cache = getAccessTokenCache();
			
			// Remove from in the in-memory cache
			if(cache!=null) {
				cache.remove(accessViewKey);
			}

			// And remove from the database
			Database db = CredStoreUtil.getCredStoreDB(DominoUtils.getCurrentSession());;
			
			View v = db.getView(accessTokensViewName);

			ViewEntryCollection col = v.getAllEntriesByKey(dbKeys, true);

			ViewEntry entry = col.getFirstEntry();
			if (entry != null && entry.isValid()) {
			
				
				Document doc = entry.getDocument();
				if (doc != null) {
					doc.remove( true );
				}
			}
		} catch(NotesException ex) {
			OAuthException oae = new OAuthException(ex);
			logger.throwing( CLASSNAME, _method, oae );
			throw oae;
		} finally {
			releaseAccessStoreSession( session );
		}
		logger.exiting( CLASSNAME, _method );
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

	protected Date convertToDate( Object itemValue ) throws NotesException
	{
		if (itemValue == null) {
			return null;
		}
		
		if (itemValue instanceof DateTime) {
            return ((DateTime)itemValue).toJavaDate();
        }
		
		if (itemValue instanceof Vector) {
			Vector<Object> v = (Vector<Object>)itemValue;
			if (v.size() > 0) {
				Object o = v.get(0);
				if (o instanceof DateTime) {
					return ((DateTime)o).toJavaDate();
				}
			}
		}

		return null;
	}
}
