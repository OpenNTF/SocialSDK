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

/**
 * Social Business Toolkit SDK. 
 * 
 * Implementation of a transport which writes the response to the DOM.
 */
define([ "./declare", "./lang", "./dom", "./json", "./stringUtil", "sbt/_bridge/Transport" ], function(declare, lang, dom, json, stringUtil, Transport) {
    return declare(Transport, {
        
    	responseMap : {},
    	index : 0,
    	
        /*
         * Create a response object
         */
    	createResponse: function(url, options, response, ioargs) {
            var retval = this.inherited(arguments, [ url, options, response, ioargs ]);

            var mockNode = dom.byId("mockData");
        	if (mockNode) {
        		if (options.handleAs == "json") {
        			response = json.jsonBeanStringify(response);
        		}
        		
        		var pre = document.createElement("pre");
        		mockNode.appendChild(pre);
        		var status = retval.status || 0;
        		var handleAs = options.handleAs || "text";
        		var method = options.method || "GET";
        		var data = options.data || "";
        		var location = retval.getHeader("Location") || "";
        		var isError = (response instanceof Error);
        		if (isError) {
        			response = retval.data.responseText || retval.data.response.text || response;
        		}
        		var id = url;
        		var hash = stringUtil.hashCode(id);
        		if (this.responseMap[hash]) {
        			this.responseMap[hash] = this.responseMap[hash] + 1;
            		id += "#" + this.responseMap[hash];
        		} else {
        			this.responseMap[hash] = 1;
        		}
        		
        		var text = "<script type='text/template' status='"+status+
        					"' id='"+id+
        					"' index='"+this.index+
        		            "' handleAs='"+handleAs+
        		            "' method='"+method+
        		            "' location='"+location+
        		            "' error='"+isError+
        		            "'>\n"+response+"\n</script>";
        		pre.appendChild(dom.createTextNode(text));
        		
        		this.index++;
        	}
        	
        	return retval;
        }
        
    });
});