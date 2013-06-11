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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.JsonNavigator;

/**
 * @author doconnor
 *
 */
public class LotusLiveContactsService extends ClientService {
    
    public static class LLContact implements Serializable{
        /**
         * 
         */
        private static final long serialVersionUID = 2500126733627058535L;
        private String photo;
        private String name;
        private String phone;
        private String email;
        private String url;
        /**
         * @return the photo
         */
        public String getPhoto() {
            return photo;
        }
        /**
         * @param photo the photo to set
         */
        public void setPhoto(String photo) {
            if(StringUtil.isNotEmpty(photo)){
                this.photo = "https://apps.lotuslive.com/contacts/img/photos/" + photo;
            }
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the phone
         */
        public String getPhone() {
            return phone;
        }
        /**
         * @param phone the phone to set
         */
        public void setPhone(String phone) {
            this.phone = phone;
        }
        /**
         * @return the email
         */
        public String getEmail() {
            return email;
        }
        /**
         * @param email the email to set
         */
        public void setEmail(String email) {
            this.email = email;
        }
        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }
        /**
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }
    /**
     * @param endpoint
     * @param serviceUrl
     */
    public LotusLiveContactsService(Endpoint endpoint, String serviceUrl) {
        super(endpoint.getLabel());
    }
    //TODO - padraic
//    /**
//     * Returns all of the contacts for the current user
//     * 
//     * @param endpoint
//     */
//    public LotusLiveContactsService(Endpoint endpoint) {
//        super(endpoint, "lotuslive-shindig-server/social/rest/people/@me/@all");
//    }
    
    public static LLContact[] parseContacts(Object json){
        DataNavigator navigator  = new JsonNavigator(json).get("entry");
        List<LLContact> contacts = new ArrayList<LLContact>();
        if(navigator != null){
            for(int i = 0; i < navigator.getCount(); i++){
                DataNavigator nav = navigator.get(i);
                DataNavigator photo = nav.get("photos").selectEq("type", "Photo");
                String val = photo.stringValue("value");
                LLContact contact = new LLContact();
                contact.setPhoto(val);
                val = nav.stringValue("displayName");
                contact.setName(val);
                val = nav.stringValue("emailAddress");
                contact.setEmail(val); 
                val = nav.stringValue("profileUrl");
                contact.setUrl(val);
                contacts.add(contact);
            }
        }
        return contacts.toArray(new LLContact[0]);
    }
}
