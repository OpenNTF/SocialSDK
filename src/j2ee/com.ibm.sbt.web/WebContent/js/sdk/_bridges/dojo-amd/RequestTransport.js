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
 * Implementation of a transport using the Dojo Request API.
 */
define([ 'dojo/_base/declare', 'dojo/request', 'dojo/_base/lang', 'dojox/xml/parser' ], function(declare, request, lang, parser){
	return declare("sbt._bridge.RequestTransport", null, {
		xhr: function(method, args, hasBody) { 
			var _self = this;
			var _args = lang.mixin({}, args);
			
			// Encapsulate the callers parameters making them suitable for request(URL, options)
			var _options = {
					method: method,
					query: _args.content,
					handleAs: _args.handleAs
			};
			
			var _promise = request(_args.url, _options);
			_promise.response.then(
				function(response) {
					return _args.handle(response.data);
				},
				function(error) {
					return _args.handle(_self.createError(error));
				}
			);    
		},
		
		createError: function(error) {
            var _error = new Error();
            _error.code = error.status || (error.response&&error.response.status) || 400;
            _error.message = this.getErrorMessage(error);
            _error.cause = error;
            if (error.response) {
                _error.response = lang.mixin({}, error.response);
            }
			return _error;	
		},
		
		getErrorMessage: function(error) {
            var text = error.responseText || (error.response&&error.response.text);
            if (text) {
                try {
                	//using dojo/xml/parser to parse the response
                	//this assumes the response is XML what if it is not ?
                    var dom = parser.parse(text);
                    var messages = dom.getElementsByTagName("message");
                    if (messages && messages.length != 0) {
                        text = messages[0].textContent;
                        text = lang.trim(text);
                    }
                } catch(ex) {
                    console.log(ex);
                }
                return text;
            } else {
                return error;
            }	
		}
	});
});
