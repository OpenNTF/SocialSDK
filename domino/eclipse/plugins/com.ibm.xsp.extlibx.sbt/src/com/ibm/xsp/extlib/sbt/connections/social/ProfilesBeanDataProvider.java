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

package com.ibm.xsp.extlib.sbt.connections.social;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.client.connections.ConnectionsService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.XmlNavigator;
import com.ibm.xsp.extlib.social.Person;
import com.ibm.xsp.extlib.social.impl.AbstractPeopleDataProvider;
import com.ibm.xsp.extlib.social.impl.IdentityMapper;
import com.ibm.xsp.extlib.social.impl.PersonImpl;


/**
 * Domino related user bean data provider.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public class ProfilesBeanDataProvider extends AbstractPeopleDataProvider {
	
    public static final String FIELD_LCERROR            = "lcError";
    
    public static final String FIELD_LCID	            = "lcId";   
    public static final String FIELD_LCKEY              = "lcKey";   
    public static final String FIELD_LCNAME             = "lcName";   
    public static final String FIELD_EMAIL              = "email";
    public static final String FIELD_GROUPWAREMAIL      = "groupwareMail";
    public static final String FIELD_ROLE               = "role";
    public static final String FIELD_TITLE              = "title";

	// Internal class that stores the user related data
	public static class ProfilesData extends PersonImpl.Properties {
		private static final long serialVersionUID = 1L;
		
		boolean privateData;
		String lcError;
		
        String lcid;
        String lckey;
        String lcname;
        String thumbnailUrl;
		String email;
        String groupwareMail;
        String role;
        String title;
	}
	private static ProfilesData EMPTY_DATA = new ProfilesData();
	
	public ProfilesBeanDataProvider() {
	}
	
	@Override
	public String getName() {
		return "Profiles";
	}
    @Override
    protected String getDefaultCacheScope() {
        return "global"; // $NON-NLS-1$
    }
    @Override
    protected int getDefaultCacheSize() {
        return 300;
    }

	@Override
    public void enumerateProperties(Set<String> propNames) {
		super.enumerateProperties(propNames);
		propNames.add(FIELD_LCERROR);
        propNames.add(FIELD_LCID);
        propNames.add(FIELD_LCKEY);
        propNames.add(FIELD_LCNAME);
        propNames.add(FIELD_EMAIL);
        propNames.add(FIELD_GROUPWAREMAIL);
        propNames.add(FIELD_ROLE);
        propNames.add(FIELD_TITLE);
        propNames.add(Person.FIELD_THUMBNAIL_URL);
	}

	@Override
	public int getWeight() {
		return WEIGHT_STANDARD;
	}

	@Override
    public Object getValue(PersonImpl person, Object key) {
	    switch(((String)key).charAt(0)) {
	        case 'e': {
	            if(key.equals(FIELD_EMAIL)) {
	                return getPeopleData(person).email;
	            }
	        } break;
            case 'g': {
                if(key.equals(FIELD_GROUPWAREMAIL)) {
                    return getPeopleData(person).groupwareMail;
                }
            } break;
            case 'l': {
                if(key.equals(FIELD_LCID)) {
                    return getPeopleData(person).lcid;
                }
                if(key.equals(FIELD_LCKEY)) {
                    return getPeopleData(person).lckey;
                }
                if(key.equals(FIELD_LCNAME)) {
                    return getPeopleData(person).lcname;
                }
                if(key.equals(FIELD_LCERROR)) {
                    return getPeopleData(person).lcError;
                }
            } break;
            case 'r': {
                if(key.equals(FIELD_ROLE)) {
                    return getPeopleData(person).role;
                }
            } break;
            case 't': {
                if(key.equals(FIELD_TITLE)) {
                    return getPeopleData(person).title;
                }
                if(key.equals(Person.FIELD_THUMBNAIL_URL)) {
                    return getPeopleData(person).thumbnailUrl;
                }
            } break;
	    }
		return null;
	}
    
    @Override
    public Class<?> getType(PersonImpl person, Object key) {
        switch(((String)key).charAt(0)) {
            case 'e': {
                if(key.equals(FIELD_EMAIL)) {
                    return String.class;
                }
            } break;
            case 'g': {
                if(key.equals(FIELD_GROUPWAREMAIL)) {
                    return String.class;
                }
            } break;
            case 'l': {
                if(key.equals(FIELD_LCID)) {
                    return String.class;
                }
                if(key.equals(FIELD_LCKEY)) {
                    return String.class;
                }
                if(key.equals(FIELD_LCNAME)) {
                    return String.class;
                }
                if(key.equals(FIELD_LCERROR)) {
                    return String.class;
                }
            } break;
            case 'r': {
                if(key.equals(FIELD_ROLE)) {
                    return String.class;
                }
            } break;
            case 't': {
                if(key.equals(FIELD_TITLE)) {
                    return String.class;
                }
                if(key.equals(Person.FIELD_THUMBNAIL_URL)) {
                    return String.class;
                }
            } break;
        }
        return null;
    }
	
    @Override
    public void readValues(PersonImpl[] persons) {
    	for(int i=0; i<persons.length; i++) {
    		getPeopleData(persons[i]);
    	}
    }
    
	private ProfilesData getPeopleData(PersonImpl person) {
		String id = person.getId();
		ProfilesData data = (ProfilesData)getProperties(id,ProfilesData.class);
		
		// If the data is "private", then it is only available to the authenticated user
		if(data!=null) {
		    if(!data.privateData || person.isAuthenticatedUser()) {
	            return data;
		    }
		}		
		
		// When no data is available, try to read it
		synchronized(getSyncObject()) {
		    ProfilesData pdata = (ProfilesData)getProperties(id,ProfilesData.class);
			if(pdata==null || data==pdata) {
				pdata = readPeopleData(person);
				if(pdata!=EMPTY_DATA) {
				    data = pdata;
				    addProperties(id,data);
				}
			}
		}
		return data;
	}
	
    private ProfilesData readPeopleData(PersonImpl person) {
        Endpoint ep = EndpointFactory.getEndpointUnchecked(EndpointFactory.SERVER_CONNECTIONS);
        if(ep!=null) {
            try {
                //String id = person.getId();
                boolean currentUser = person.isViewer();
                
                // Find the Connections id
                String lcid = person.getIdentity(IdentityMapper.TARGET_CONNECTIONS);
                
                // If the id is not available, then look at the end point to get the current id
                boolean privateData = false;

                // If it is the current user, then find the user uuis
                if(lcid==null && currentUser) {
                    // Be sure that we are authenticated for Connections
                    if(!ep.isAuthenticated()) {
                        ep.authenticate(true);
                    }
                    try {
//                        ConnectionsService svc = new ConnectionsService(ep,"/profiles/atom/profileService.do");
                    	ConnectionsService svc = new ConnectionsService(ep);
                    	HandlerXml xml = new HandlerXml();
                        Document doc = (Document)svc.get("/profiles/atom/profileService.do",xml);
                        XmlNavigator nav = new XmlNavigator(doc);
                        lcid = nav.get("service/workspace/collection/userid").stringValue(".");
                        privateData = true;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
//                // This is obsolete - the call above is better as it handle servers where email access is disabled
//                // Try from the current user identity (might be the email)
//                if(lcid==null) {
//                    lcid = ep.getUserIdentity();
//                    privateData = true;
//                }
                
                if(StringUtil.isNotEmpty(lcid)) {
                    ConnectionsService svc = new ConnectionsService(ep);
                    
                    String key = lcid.indexOf('@')>=0 ? "email" : "userid"; 
                    Map<String, String> p = Collections.singletonMap(key, lcid); 
                    HandlerXml xml = new HandlerXml();
                    Document doc = (Document)svc.get("/profiles/atom/profile.do",p,xml);
                    XmlNavigator nav = new XmlNavigator(doc);
                    ProfilesData data = new ProfilesData();
                    DataNavigator entry = nav.get("feed/entry/content/div/span/div");
                    data.privateData = privateData;
                    data.lcname = nav.get("feed/entry/contributor/name").stringValue(".");
                    data.lcid = entry.selectEq("@class","x-lconn-userid").stringValue(".");
                    data.lckey = entry.selectEq("@class","x-profile-key").stringValue(".");
                    data.thumbnailUrl = entry.get("img").selectEq("@class","photo").stringValue("@src");
                    data.email = entry.get("a").selectEq("@class","email").stringValue(".");                    
                    data.groupwareMail = entry.selectEq("@class","x-groupwareMail").stringValue(".");
                    data.role = entry.selectEq("@class","role").stringValue(".");
                    data.title = entry.selectEq("@class","TITLE").stringValue(".");
                    return data;
                }
            } catch(ClientServicesException ex) {
                // Ok, leave the profile empty...
                // But we return a profile withe an error in it
                Platform.getInstance().log(ex);
                ProfilesData data = new ProfilesData();
                data.lcError = ex.toString();
                return data;
            }
        }
        return EMPTY_DATA;
    }
}
