/*
 * © Copyright IBM Corp. 2011
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
package com.ibm.xsp.extlib.sbt.user;

import java.util.Set;

import com.ibm.commons.Platform;
import com.ibm.jscript.types.FBSValue;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.GenericService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.util.JsonNavigator;
import com.ibm.xsp.extlib.social.impl.AbstractPeopleDataProvider;
import com.ibm.xsp.extlib.social.impl.PersonImpl;

/**
 * PeopleDataProvider class to provide subscriber info for LotusLiveFiles.
 * 
 * @author doconnor
 *
 */
public class LotusLivePeopleDataProvider extends AbstractPeopleDataProvider {
    public static class PeopleData extends PersonImpl.Properties {
        boolean privateData;
        
        //LotusLiveFiles subscriber id
        String lotusLiveSubscriberId;
    }
    private static PeopleData EMPTY_DATA = new PeopleData(); 
    
    private final String SUBSCRIBER_ID = "lotusLiveSubscriberId";

    private PeopleData getPeopleData(PersonImpl person) {
        String id = person.getId();
        PeopleData data = (PeopleData)getProperties(id, PeopleData.class);
        
        // If the data is "private", then it is only available to the authenticated user
        if(data!=null && data.privateData && !person.isAuthenticatedUser()) {
            data = null;
        }
        
        if(data == null) {
            synchronized(getSyncObject()) {
                data = (PeopleData)getProperties(id, PeopleData.class);
                if(data == null) {
                    try {
						data = readPeopleData(person);
					} catch (ClientServicesException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if(data!=EMPTY_DATA) {
                        addProperties(id,data);
                    }
                }
            }
        }
        return data;
    }
    private PeopleData readPeopleData(PersonImpl person) throws ClientServicesException {
        if(!person.isAuthenticatedUser()){
            //should never happen as this should be guarded
            return null;
        }
        Endpoint ep = EndpointFactory.getEndpointUnchecked(EndpointFactory.SERVER_LOTUSLIVE);
        if(ep!=null) {
            if(ep.isAuthenticated()){
			    // Find the LotusLive id
			    GenericService service = new GenericService(ep);
//                    service.get("/manage/oauth/getUserIdentity",null, "json");
//                    
			    //TODO - Padraic
			 	HandlerJson json= new HandlerJson();
			 	Object result = service.get("/manage/oauth/getUserIdentity", json);
			    if(result instanceof FBSValue){
			        JsonNavigator jsonUtil = new JsonNavigator(result);
			        PeopleData data = new PeopleData();
			        data.lotusLiveSubscriberId = jsonUtil.stringValue("subscriberid");
			        return data;
			    }
			}
        }
        return EMPTY_DATA;
    }

    @Override
    public String getName() {
        return "LotusLive";
    }
    @Override
    protected String getDefaultCacheScope() {
        return "global"; // $NON-NLS-1$
    }
    @Override
    protected int getDefaultCacheSize() {
        return 300;
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.social.impl.AbstractPeopleDataProvider#getType(com.ibm.xsp.extlib.social.impl.PersonImpl, java.lang.Object)
     */
    @Override
    public Class<?> getType(PersonImpl person, Object key) {
        if(SUBSCRIBER_ID.equals(key)){
            return String.class;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.social.impl.AbstractPeopleDataProvider#getValue(com.ibm.xsp.extlib.social.impl.PersonImpl, java.lang.Object)
     */
    @Override
    public Object getValue(PersonImpl person, Object key) {
        if(SUBSCRIBER_ID.equals(key) && person.isAuthenticatedUser()){
            return getPeopleData(person).lotusLiveSubscriberId;
        }
        return null;
    }

    @Override
    public void enumerateProperties(Set<String> propNames) {
        super.enumerateProperties(propNames);
        propNames.add(SUBSCRIBER_ID);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.social.impl.AbstractPeopleDataProvider#readValues(com.ibm.xsp.extlib.social.impl.PersonImpl[])
     */
    @Override
    public void readValues(PersonImpl[] persons) {
    }
}
