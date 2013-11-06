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
 * Implements a transport for when the SDK is used from a gadget.
 * @module
 */
define(['./declare','./lang'],function(declare,lang) {
    var MethodTypes = {
        'POST':   gadgets.io.MethodType.POST,
        'PUT':    gadgets.io.MethodType.PUT,
        'DELETE': gadgets.io.MethodType.DELETE,
        'GET':    gadgets.io.MethodType.GET
    };
    var ContentTypes = {
        'XML':                   gadgets.io.ContentType.DOM, 
        'JSON':                  gadgets.io.ContentType.JSON, 
        'TEXT':                  gadgets.io.ContentType.TEXT, 
        'JSON-COMMENT-OPTIONAL': gadgets.io.ContentType.JSON,
        'JSON-COMMENT-FILTERED': gadgets.io.ContentType.JSON,
        'JAVASCRIPT':            gadgets.io.ContentType.JSON
    };
    var AuthorizationTypes = {
            'NONE':    gadgets.io.AuthorizationType.NONE, 
            'OAUTH':   gadgets.io.AuthorizationType.OAUTH, 
            'OAUTH2':  gadgets.io.AuthorizationType.OAUTH2, 
            'SIGNED':  gadgets.io.AuthorizationType.SIGNED
    };
    return declare(null, {
        serviceName: null,
        constructor: function(args) {
            this.serviceName = args.serviceName || null;
        },
        /**
         * Performs an XHR request using the gadget APIs.
         * Warning this is not a final module.  Not everything is supported as of yet.
         * @param {String} method The HTTP method to use.
         * @param {Object} args The arguments for the XHR request.
         * @param {boolean} hasBody Indicates whether there is a request body.  
         */
        xhr: function(method,args,hasBody) {
            //TODO OAuth support
            if(args.sync) {
                throw new Error('Gadget transport does not support synchronous requests.');
            }
            
            var params = {};
            params[gadgets.io.RequestParameters.METHOD]        = MethodTypes[method.toUpperCase()] || gadgets.io.MethodType.GET;
            params[gadgets.io.RequestParameters.CONTENT_TYPE]  = ContentTypes[args.handleAs ? args.handleAs.toUpperCase() : 'TEXT'];
            params[gadgets.io.RequestParameters.HEADERS]       = args.headers || {};
            params[gadgets.io.RequestParameters.POST_DATA]     = this.getPostData(params[gadgets.io.RequestParameters.METHOD], args.postData || args.putData);
            if(args.preventCache) {
                params[gadgets.io.RequestParameters.REFRESH_INTERVAL] = 0;
            }
            if(this.serviceName || args.serviceName) {
                params[gadgets.io.RequestParameters.OAUTH_SERVICE_NAME] = this.serviceName || args.serviceName;
            }
            if(args.authType) {
                var authorization = args.authType.toUpperCase();
                params[gadgets.io.RequestParameters.AUTHORIZATION] = AuthorizationTypes[authorization] || AuthorizationTypes['NONE'];
            }
            
            var self = this;
            var callback = function(response) {
                self.handleResponse(args, response);
            };
            
            var url = this.buildUrl(args);
            gadgets.io.makeRequest(url, callback, params);
        },
        handleResponse: function(args, response) {
            if(response.errors && response.errors.length > 0) {
                if (args.error || args.handle) {
                    var error = this.createError(response);
                    this.notifyError(args, error);
                }
            } else {
                if (response.oauthApprovalUrl) {
                    this.handleApproval(args, response);
                } else {
                    this.notifyResponse(args, response);
                }
            }
        },
        handleApproval: function(args, response) {
            var error = new Error();
            error.code = 401;
            error.response = lang.mixin({}, response);
            this.notifyError(args, error);
        },
        notifyResponse: function(args, response) {
            if (args.load || args.handle) {
                var ioArgs = {
                    'args' : args,
                    'headers' : response.headers,
                    '_ioargs' : response
                };
                if (args.handle) {
                    args.handle(response.data || response.text, ioArgs);
                }
                if (args.load) {
                    args.load(response.data || response.text, ioArgs);
                }
            }
        },
        notifyError: function(args, error) {
            if (args.handle) {
                args.handle(error);
            }
            if (args.error) {
                args.error(error);
            }
        },
        createError: function(response) {
            var error = new Error();
            error.code = response.rc;
            error.message = response.errors[0];
            error.response = lang.mixin({}, response);
            return error;
        },
        getPostData: function(method, postData) {
            if (method == gadgets.io.MethodType.POST ||
                method == gadgets.io.MethodType.PUT) {
                return postData;
            }
            return '';
        },
        buildUrl: function(args) {
            var params = [];
            var url = args.url;
            if (args.content) {
                for (name in args.content) {
                    var param = encodeURIComponent(name) + "=" + encodeURIComponent(args.content[name]);
                    params.push(param);
                }
                if (params.length > 0) {
                    var query = params.join("&");
                    url += (~url.indexOf('?') ? '&' : '?') + query;
                }
            }
            return url;
        }
    });
});