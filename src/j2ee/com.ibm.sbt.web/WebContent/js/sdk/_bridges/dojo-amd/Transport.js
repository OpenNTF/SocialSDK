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
define([ 'dojo/_base/declare', 'dojo/_base/xhr', 'dojo/_base/lang', 'dojox/xml/parser', '../util', '../Promise' ], function(declare, xhr, lang, parser, util, Promise) {
    return declare(null, {
        
        /**
         * Provides an asynchronous request using the associated Transport.
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
         * @return {sbt.Promise}
         */
        request : function(url, options) {
            var method = options.method || "GET";
            method = method.toUpperCase();
            var query = this.createQuery(options.query);
            if(url && query){
                url += (~url.indexOf('?') ? '&' : '?') + query;
            } 
            var args = {
                url : url,
                handleAs : options.handleAs || "text"
            };
            //if (options.query) {
            //    args.content = options.query;
            //}
            if (options.headers) {
                args.headers = options.headers;
            }
            var hasBody = false;
            if (method == "PUT") {
                args.putData = options.data || null;
                hasBody = true;
            } else if (method == "POST") {
                args.postData = options.data || null;
                hasBody = true;
            }
            
            var promise = new Promise();
            promise.response = new Promise();
            var self = this;
            args.handle = function(response, ioargs) {
                if (response instanceof Error) {
                    var error = response;
                    error.response = self.createResponse(url, options, response, ioargs);
                    promise.rejected(error);
                    promise.response.rejected(error);
                } else {
                    promise.fulfilled(response);
                    promise.response.fulfilled(self.createResponse(url, options, response, ioargs));
                }
            };
            
            this.xhr(method, args, hasBody);
            return promise;
        },
        
        /*
         * Create a response object
         */
        createResponse: function(url, options, response, ioargs) {
            var xhr = ioargs._ioargs.xhr;
            var handleAs = options.handleAs || "text";
            return { 
                url : url,
                options : options,
                data : response,
                text : (handleAs == "text") ? response : null,
                status : xhr.status,
                getHeader : function(headerName) {
                    return xhr.getResponseHeader(headerName);
                },
                xhr : xhr,
                _ioargs : ioargs._ioargs
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
        
        xhr: function(method, args, hasBody) {
            var _args = lang.mixin({}, args);
            
            // override the handle callback to normalise the Error
            var self = this;
            _args.handle = function(data, ioArgs) {
                self.handleResponse(data, ioArgs, args);
            };

           // dojo.xhr(method, _args, hasBody);
           xhr(method, _args, hasBody);
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
            _error.cause = error;
            if (error.response) {
                _error.response = lang.mixin({}, error.response);
            }
            return _error;
        }        
    });
});