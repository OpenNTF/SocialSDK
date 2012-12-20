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
 */
define(['sbt/_bridge/declare','sbt/lang'],function(declare,lang) {

/**
 * Defination of the proxy module
 * 
 * @module sbt.Proxy
 * 
 */
declare("sbt.Proxy", null, {
	
	proxyUrl		: null, 
	
	constructor: function(args){
		lang.mixin(this, args);	
	},
	
	rewriteUrl: function(u,proxyPath) {
		if(this.proxyUrl) {
			if(u.indexOf("http://")==0) {
				u = "/http/"+u.substring(7);
			} else if(u.indexOf("https://")==0) {
				u = "/https/"+u.substring(8);
			}
			if(proxyPath) {
				u = "/" + proxyPath + u;
			}
			return this.proxyUrl + u;
		}
		return u;
	}
});

return sbt.Proxy;

});