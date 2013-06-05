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

/**
 * @author doconnor
 *
 */
public class LotusLiveCommunitiesService extends ClientService {
    
    public static enum CommunityType{
        MYCOMMUNITIES("/communities/service/atom/communities/my"),
        
        ALLCOMMUNITIES("/communities/service/atom/communities/all"),
        
        COMMUNITYMEMBERS("/communities/service/atom/community/members"),
        
        GETCOMMUNITY("/communities/service/atom/community/instance");
        
        
        private final String url;
        CommunityType(String url){
            this.url = url;
        }
        
        public String getUrl(){
            return url;
        }
        
        @Override
        public String toString(){
            return this.url;
        }
        
    }
//
//    /**
//     * @param endpoint
//     * @param serviceUrl
//     */
//    public LotusLiveCommunitiesService(Endpoint endpoint) {
//        super(endpoint, CommunityType.MYCOMMUNITIES.getUrl());
//    }
    
    public LotusLiveCommunitiesService(Endpoint endpoint, String url) {
        super(endpoint.getLabel());
    }
}
