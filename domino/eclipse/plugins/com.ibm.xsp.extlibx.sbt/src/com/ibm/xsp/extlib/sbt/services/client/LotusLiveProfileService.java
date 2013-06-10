/* ***************************************************************** */
/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.xsp.extlib.sbt.services.client;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.JsonNavigator;

/**
 * @author doconnor
 *
 */
public class LotusLiveProfileService extends ClientService {

    /**
     * @param endpoint
     * @param serviceUrl
     */
    public LotusLiveProfileService(Endpoint endpoint) {
        super(endpoint);
    }

//    public LotusLiveProfileService(Endpoint endpoint) {
//        super(endpoint, "/lotuslive-shindig-server/social/rest/people/@me/@self");
//    }
    
    //Utilities
    //TODO - Add utility methods for all profile attributes
    public static String getURL(Object json){
        JsonNavigator nav = new JsonNavigator(json);
        DataNavigator entry = nav.get("entry");
        entry = entry.get("photos");
        entry = entry.selectEq("type", "Photo");
        return entry.stringValue("value");
    }

    public static String getEMail(Object json){
        JsonNavigator nav = new JsonNavigator(json);
        DataNavigator entry = nav.get("entry");
        return entry.stringValue("emailAddress");
    }

    public static String getPrimaryAddress(Object json){
        JsonNavigator nav = new JsonNavigator(json);
        DataNavigator entry = nav.get("entry");
        entry = entry.get("addresses");
        entry = entry.selectEq("title", "Primary Address");
        return entry.stringValue("address");
    }

    public static String getPrimaryPhone(Object json){
        JsonNavigator nav = new JsonNavigator(json);
        DataNavigator entry = nav.get("entry");
        entry = entry.get("phoneNumbers");
        entry = entry.selectEq("title", "Primary Telephone");
        return entry.stringValue("phone");
    }
}
