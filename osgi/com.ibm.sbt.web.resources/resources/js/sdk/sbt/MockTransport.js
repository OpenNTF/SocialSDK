/*
 * © Copyright IBM Corp. 2013
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

dojo.provide('sbt.MockTransport');
/**
 * Social Business Toolkit SDK. 
 * 
 * Implementation of a transport which returns mock data.
 */
define('sbt/MockTransport',[ "sbt/declare", "sbt/lang", "sbt/dom", "sbt/xml", "sbt/json", "sbt/stringUtil", "sbt/Promise" ], 
	function(declare, lang, dom, xml, json, stringUtil, Promise) {
    return declare(null, {
    	
    	requestMap : {},
        
        /**
         * Provides mock data if available in the DOM.
         */
        request : function(url, options) {
            var query = this.createQuery(options.query);
            if(url && query){
                url += (~url.indexOf('?') ? '&' : '?') + query;
            }

            var promise = new Promise();
            promise.response = new Promise();

    		var id = url;
    		var hash = stringUtil.hashCode(id);
    		if (this.requestMap[hash]) {
    			this.requestMap[hash] = this.requestMap[hash] + 1;
        		id += "#" + this.requestMap[hash];
    		} else {
    			this.requestMap[hash] = 1;
    		}
    		
            var domNode = dom.byId(id);
	        if (domNode) {
	        	var response = domNode.text || domNode.textContent;
	        	var handleAs = domNode.getAttribute("handleAs");
	        	if (handleAs == "json") {
        			response = json.parse(response);
        		}
	        	
	        	var status = domNode.getAttribute("status");
	        	
	        	var error = domNode.getAttribute("error");
	        	if (error == "true") {
	        		var error = new Error();
	        		error.code = Number(status || 400);
	        		error.message = this.getErrorText(response);
	        		error.response = this.createResponse(url, options, response, Number(status || 400), {});
	                promise.rejected(error);
	                promise.response.rejected(error);
	        	} else {
		        	var location = domNode.getAttribute("location");
		        	var headers = {
		        		Location : location
		        	};
		        	
	                promise.fulfilled(response);
	                promise.response.fulfilled(this.createResponse(url, options, response, Number(status || 200), headers));
	        	}
	        }
	        else {
	        	var message = "Unable to find mock response for: "+url;
	        	var error = new Error(message);
	        	error.response = { status : 400 , message : message };
                promise.rejected(error);
                promise.response.rejected(error);
	        }

	        return promise;
        },
        
        /*
         * Create a response object
         */
        createResponse: function(url, options, response, status, headers) {
            var handleAs = options.handleAs || "text";
            return { 
                url : url,
                options : options,
                data : response,
                text : (handleAs == "text") ? response : null,
                status : status,
                getHeader : function(headerName) {
                    return headers[headerName];
                }
            };
        },

        /*
         * Create a query string from an object
         */
        createQuery: function(queryMap) {
            if (!queryMap) {
                return null;
            }
            var pairs = [];
            for(var name in queryMap){
                var value = queryMap[name];
                pairs.push(encodeURIComponent(name) + "=" + encodeURIComponent(value));
            }
            return pairs.join("&");
        },
        
        getErrorText: function(text) {    	
            if (text) {
                try {            	
                    var dom = xml.parse(text);
                    var messages = dom.getElementsByTagName("message");
                    if (messages && messages.length != 0) {                	
                        text = messages[0].text || messages[0].textContent;                	
                        text = lang.trim(text);
                    }
                } catch(ex) {}  
                return text.replace(/(\r\n|\n|\r)/g,"");
            } else {
                return text;
            }
        }
        
    });
});
