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

/**
 * Social Business Toolkit SDK. 
 * Definition of a proxy re-writer.
 * 
 * @module sbt.Proxy
 */
define(['./declare','./lang','./pathUtil'],function(declare,lang,pathUtil) {

    /**
     * Definition of the proxy module
     * 
     * @class sbt.Proxy
     * 
     */
    var Proxy = declare(null, {
    	
    	proxyUrl		: null, 
    	
    	constructor: function(args){
    		lang.mixin(this, args);	
    	},
    	
    	rewriteUrl: function(baseUrl,serviceUrl,proxyPath) {
    		// When this proxy is being used, we don't add the base URL as it will be added on the server side
    		// A different implementation might use the full URL
    		var u = serviceUrl;
    		if(this.proxyUrl) {
    			if(u.indexOf("http://")==0) {
    				u = "/http/"+u.substring(7);
    			} else if(u.indexOf("https://")==0) {
    				u = "/https/"+u.substring(8);
    			}
    			if(proxyPath) {
    				u = pathUtil.concat(proxyPath,u);
    			}
    			return pathUtil.concat(this.proxyUrl,u);
    		}
    		return u;
    	}
    });
    
    return Proxy;

});