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
 * Social Business Toolkit SDK. Implementation of a transport using the dojo/request API.
 */
define([ "dojo/_base/declare", "dojo/request", "dojo/_base/lang", "sbt/util" ], function(declare,request,lang,util) {
    return declare("sbt._bridge.RequestTransport", null, {
        xhr : function(method,args,hasBody) {
            // all options expected by dojo/request and the defaults
            args = lang.mixin({}, args);
            var options = {
                data : args.putData || args.postData,
                query : args.content || {},
                preventCache : args.preventCache || false,
                method : method,
                timeout : args.timeout || null,
                handleAs : args.handleAs || "text",
                headers : args.headers || null
            };

            var self = this;
            var promise = request(args.url, options);
            promise.response.then(
                function(response) {
                    var _ioArgs = {
                        args : args,
                        headers : {},
                        _ioargs : response
                    };
                    return args.handle(response.data || response.text, _ioArgs);
                }, 
                function(error) {
                    return args.handle(self.createError(error));
                }
            );
        },

        createError : function(error) {
            var _error = new Error();
            _error.code = error.status || (error.response && error.response.status) || 400;
            _error.message = this.getErrorMessage(error);
            _error.cause = error;
            if (error.response) {
                _error.response = lang.mixin({}, error.response);
            }
            return _error;
        },

        getErrorMessage : function(error) {
            var text = error.responseText || (error.response && error.response.text);
            if (text) {
                try {
                    // using dojo/xml/parser to parse the response
                    // this assumes the response is XML what if it is not ?
                    var dom = parser.parse(text);
                    var messages = dom.getElementsByTagName("message");
                    if (messages && messages.length != 0) {
                        text = messages[0].textContent;
                        text = lang.trim(text);
                    }
                } catch (ex) {
                    console.log(ex);
                }
                return text;
            } else {
                return error;
            }
        }
    });
});
