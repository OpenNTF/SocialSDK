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
 * Social Business Toolkit SDK. Implementation of a transport using the
 * dojo/request API.
 */
define([ "dojo/_base/declare", "dojo/_base/lang", "dojo/request", "../util" ], function(declare,lang,request,util) {
    return declare(null, {

        /**
         * Provides an asynchronous request.
         * 
         * @method request
         * @param {String)
         *            url The URL the request should be made to.
         * @param {Object}
         *            [options] Optional A hash of any options for the provider.
         * @param {String|Object}
         *            [options.data=null] Data, if any, that should be sent with
         *            the request.
         * @param {String|Object}
         *            [options.query=null] The query string, if any, that should
         *            be sent with the request.
         * @param {Object}
         *            [options.headers=null] The headers, if any, that should
         *            be sent with the request.
         * @param {Boolean}
         *            [options.preventCache=false] If true will send an extra
         *            query parameter to ensure the the server won’t supply
         *            cached values.
         * @param {String}
         *            [options.method=GET] The HTTP method that should be used
         *            to send the request.
         * @param {Integer}
         *            [options.timeout=null] The number of milliseconds to wait
         *            for the response. If this time passes the request is
         *            canceled and the promise rejected.
         * @param {String}
         *            [options.handleAs=text] The content handler to process the
         *            response payload with.
         * 
         */
        request : function(url,options) {
            return request(url, options);
        },

        /**
         * @deprecated
         */
        xhr : function(method,args,hasBody) {
            // all options expected by dojo/request and the defaults
            args = lang.mixin({}, args);
            var options = {
                data : args.putData || args.postData || args.content || null,
                query : args.content || {},
                preventCache : args.preventCache || false,
                method : method,
                timeout : args.timeout || null,
                handleAs : args.handleAs || "text",
                headers : args.headers || null
            };

            var self = this;
            var promise = request(args.url, options);
            promise.response.then(function(response) {
                var _ioArgs = {
                    args : args,
                    headers : {},
                    _ioargs : response
                };
                return args.handle(response.data || response.text, _ioArgs);
            }, function(error) {
                return args.handle(self.createError(error));
            });
        },

        /**
         * @deprecated
         */
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

        /**
         * @deprecated
         */
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
