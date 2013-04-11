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
 * 
 * Implementation of a transport using the Dojo XHR API.
 */
define([ 'sbt/_bridge/declare', 'dojo/_base/xhr', 'dojox/xml/parser', 'sbt/util' ], function(declare, xhr, parser, util) {
    return declare("sbt._bridge.Transport", null, {
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
            return null;
        },

        xhr: function(method, args, hasBody) {
            var _args = dojo.mixin({}, args);
            
            // override the handle callback to normalise the Error
            var self = this;
            _args.handle = function(data, ioArgs) {
                self.handleResponse(data, ioArgs, args);
            };

            dojo.xhr(method, _args, hasBody);
        },
        handleResponse: function(data, ioArgs, args) {
            var _data = data;
            
            if (data instanceof Error) {
                _data = this.createError(data, ioArgs);
            }
            
            try {
                var _ioArgs = {
                    'args' : args,
                    'headers' : util.getAllResponseHeaders(ioArgs.xhr),
                    '_ioargs' : ioArgs
                };
                args.handle(_data, _ioArgs); 
            } catch (ex) {
                console.log(ex);
            }
        },
        createError: function(error, ioArgs) {
            var _error = new Error();
            _error.code = error.status || (error.response&&error.response.status) || 400;
            _error.message = this.getErrorMessage(error);
            _error.cause = error;
            if (error.response) {
                _error.response = dojo.mixin({}, error.response);
            }
            return _error;
        },
        getErrorMessage: function(error) {
            var text = error.responseText || (error.response&&error.response.text);
            if (text) {
                try {
                    var dom = parser.parse(text);
                    var messages = dom.getElementsByTagName("message");
                    if (messages && messages.length != 0) {
                        text = messages[0].textContent;
                        text = dojo.trim(text);
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