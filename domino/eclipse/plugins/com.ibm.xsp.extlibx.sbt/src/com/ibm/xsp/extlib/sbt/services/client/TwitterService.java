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

import sun.net.dns.ResolverConfiguration.Options;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 * @author doconnor
 *
 */
public class TwitterService extends ClientService {

//    /**
//     * @param endpoint
//     * @param serviceUrl
//     */
//    public TwitterService(Endpoint endpoint, String serviceUrl) {
//        super(endpoint, serviceUrl);
//    }

    public TwitterService(Endpoint endpoint) {
		super(endpoint);
	}
    
//    /* (non-Javadoc)
//     * @see com.ibm.xsp.extlib.sbt.services.client.ClientService#findUrl(com.ibm.xsp.extlib.sbt.services.client.ClientService.Options)
//     */
//    @Override
//    protected String findUrl(Options options) {
//        if(options != null && options.getParameters() != null){
//            if(options.getParameters().containsKey("q")){
//                //If we detect that we are trying to do a switter search then we need to modify the url
//                //In this case we ignore the URL defined in the endpoint (api.twitter.com) and go with search.twitter.com
//                return ExtLibUtil.concatPath("https://search.twitter.com", getServiceUrl(), '/');
//            }
//        }
//        return super.findUrl(options);
//    }
    
    @Override
    	protected void addUrlParameters(StringBuilder b, Args args)
    			throws ClientServicesException {
    		super.addUrlParameters(b, args);
    	}

	@Override
		protected String getUrlPath(Args args) {
		 if(args != null && args.getParameters() != null){
           if(args.getParameters().containsKey("q")){
               //If we detect that we are trying to do a switter search then we need to modify the url
               //In this case we ignore the URL defined in the endpoint (api.twitter.com) and go with search.twitter.com
              			return "https://search.twitter.com/search.atom";
           }
       }
		return super.getUrlPath(args);

		}
}
