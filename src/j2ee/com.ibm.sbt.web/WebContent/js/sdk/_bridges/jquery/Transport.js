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
 * Implementation of a XML HTTP Request using the JQuery API.
 */
define(['./declare', '../util', '../Promise', './jquery'], function(declare, util, Promise, jquery) {
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
		    var url = args.url;
		    var self = this;
		    var usedJQVersion = jQuery.fn.jquery;
		    var requiredJQVersion = "1.8";
		    var jQ_v_gte_18 = util.minVersion(requiredJQVersion, usedJQVersion);
		    
		    if (args.content && (method==="PUT"||method==="POST")) {
		    	var query = self.createQuery(args.content);
		    	if (query) {
		    		url += ((url.indexOf('?') != -1) ? "&" : "?") + query;	
		    	}
		    }
		    
		    var xhrData = args.putData || args.postData || args.content || null;
		    
		    if (!args.handleAs) {
		    	jQuery.extend(args, {handleAs: "text"});
		    }
		    var settings = {
		        type: method,
		        data: xhrData,
		        dataType: args.handleAs
		    };
		    
		    if (args.headers) {
		    	settings.headers = args.headers;	
		    }
		    
		    if (!jQ_v_gte_18) {
		    	settings = jQuery.extend(settings, {
		    		success: function(data, textStatus, jqXHR) {
		    			self.handleSuccess(args, data, textStatus, jqXHR);
		    		},
		    		error: function(jqXHR, textStatus, errorThrown)  {
		    			self.handleError(args, jqXHR, textStatus, errorThrown);
		    		}
		    	});
		    }
		    
		    var jqXHR = jQuery.ajax(url, settings);
		    
		    if (jQ_v_gte_18) {
		    	jqXHR.done(function(data, textStatus, jqXHR) {
		            self.handleSuccess(args, data, textStatus, jqXHR);
		        }).fail(function(jqXHR, textStatus, errorThrown)  {
		            self.handleError(args, jqXHR, textStatus, errorThrown);
		        });	
		    }
		},
		handleSuccess: function(args, data, textStatus, jqXHR) {
		    if (args.handle) {
                var _ioArgs = {
                    'args' : args,
                    'headers' : util.getAllResponseHeaders(jqXHR),
                    '_ioargs' : { xhr : jqXHR }
                };
		        args.handle(data, _ioArgs);
		    }
		},
		handleError: function(args, jqXHR, textStatus, errorThrown) {
			var error = this.createError(jqXHR, textStatus, errorThrown, args.handleAs);
            if (args.handle) {
                var _ioArgs = {
                    'args' : args,
                    'headers' : util.getAllResponseHeaders(jqXHR),
                    '_ioargs' : { xhr : jqXHR }
                };
		        args.handle(error, _ioArgs);
            }
		},
		createError: function(jqXHR, textStatus, errorThrown, type) {
            var _error = new Error();
            _error.responseText = jqXHR.responseText;
            _error.code = jqXHR.status || 400;
            _error.cause = {
            		responseText: jqXHR.responseText
            };
            _error.response = jqXHR;
            return _error;
        }
	});
});
