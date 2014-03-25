/*
 * © Copyright IBM Corp. 2012, 2013
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
 * Javascript Base APIs for IBM Connections
 * 
 * @module sbt.base.BaseService
 * @author Carlos Manias
 */
define(["../config", "../declare", "../lang", "../log", "../stringUtil", "../Cache", "../Promise", "../util", "sbt/base/URLBuilder" ], 
    function(config, declare,lang,log,stringUtil,Cache,Promise, util, URLBuilder) {
	// TODO sbt/config is required here to solve module loading
	// issues with jquery until we remove the global sbt object
	
    var BadRequest = 400;
    
    var requests = {};

    /**
     * BaseService class.
     * 
     * @class BaseService
     * @namespace sbt.base
     */
    var BaseService = declare(null, {
    	
        /**
         * The Endpoint associated with the service.
         */
        endpoint : null,

        /*
         * The Cache associated with the service.
         */
        _cache : null,
        
        /*
         * Regular expression used to remove // from url's
         */
        _regExp : new RegExp("/{2}"),
        
        /**
         * A map of default context roots to custom, if any. This will be implemented in subClasses of BaseService.
         */
        contextRootMap: {},

        builder: new URLBuilder(),
        
        /**
         * Constructor for BaseService
         * 
         * An endpoint is required so subclasses must check if one
         * was created here and if not set the default endpoint.
         * 
         * @constructor
         * @param {Object} args Arguments for this service.
         */
        constructor : function(args) {
        	args = args || {};
        	
            // set endpoint if specified in args
            if (args.endpoint) {
            	if (lang.isString(args.endpoint)) {
            		this.endpoint = config.findEndpoint(args.endpoint);
            	} else {
            		this.endpoint = args.endpoint;
            	}
            }

            // optionally create a cache
            if (args.cacheSize) {
                this._cache = new Cache(args.cacheSize);
            }
        },

        /**
         * Construct a url using the specified parameters 
         * 
         * @method constructUrl
         * @param url Base part of the URL to construct
         * @param params Params to be encoded in the URL
         * @param urlParams Params to be encoded in the URL query
         * @returns The constructed URL
         */
        constructUrl : function(url,params,urlParams) {
            if (!url) {
                throw new Error("BaseService.constructUrl: Invalid argument, url is undefined or null.");
            }
           
            
            if(this.endpoint){
                lang.mixin(this.contextRootMap, this.endpoint.serviceMappings);
                //back compat with services reusing constants
                url = this.builder.build(url, this.endpoint.apiVersion,  {
                    authentication : this.endpoint.authType === "oauth" ? "oauth":""
                });
                url = stringUtil.transform(url, this.contextRootMap, function(value, key){
                    if(!value){
                        return key;
                    }
                    else{
                        return value;
                    }
                }, this);
            }else {
            	 //back compat with services reusing constants
                url = this.builder.build(url);
            }
            
            if (urlParams) {
                url = stringUtil.replace(url, urlParams);
                
                if (url.indexOf("//") != -1) {
                	// handle empty values
                	url = url.replace(this._regExp, "/");
                }
            }
            if (params) {
                for (param in params) {
                    if (url.indexOf("?") == -1) {
                        url += "?";
                    } else if (url.indexOf("&") != (url.length - 1)) {
                        url += "&";
                    }
                    var value = encodeURIComponent(params[param]);
                    if (value) {
                        url += param + "=" + value;
                    }
                }
            }
            return url;
        },
        
        /**
         * Get a collection of entities.
         * 
         * @method getEntities
         * @param url The URL to get the entities.
         * @param options Optional. Options for the request.
         * @param callbacks Callbacks used to parse the response and create the entities.
         * @returns {sbt/Promise}
         */
        getEntities : function(url,options,callbacks) {
            url = this.constructUrl(url);
            var self = this;
            var promise = new Promise();
            this.request(url,options,null,promise).response.then(
                function(response) {
                    promise.response = response;
                    try {
	                    var feedHandler = callbacks.createEntities.apply(self, [ self, response.data, response ]);
	                    var entitiesArray = feedHandler.getEntitiesDataArray();
	                    var entities = [];
	                    for ( var i = 0; i < entitiesArray.length; i++) {
	                        var entity = callbacks.createEntity.apply(self, [ self, entitiesArray[i], response ]);
	                        entities.push(entity);
	                    }
	                    promise.summary = feedHandler.getSummary();
	                    promise.fulfilled(entities);
                    } catch (cause) {
                    	var error = new Error("Error parsing response caused by: "+cause);
                    	error.cause = cause;
                    	promise.rejected(error);
                    }
                },
                function(error) {
                    promise.rejected(error);
                }
            );
            return promise;
        },

        /**
         * Get a single entity.
         * 
         * @method getEntity
         * @param url The URL to get the entity.
         * @param options Options for the request.
         * @param callbacks Callbacks used to parse the response and create the entity.
         * @returns {sbt/Promise}
         */
        getEntity : function(url,options,entityId,callbacks) {
            url = this.constructUrl(url);
            var promise = this._validateEntityId(entityId);
            if (promise) {
                return promise;
            }
            
            // check cache
            var promise = new Promise();
            var data = this.getFromCache(entityId);
            if (data) {
                promise.fulfilled(data);
                return promise;
            }

            var self = this;
            this.request(url,options,entityId,promise).response.then(
                function(response) {
                    promise.response = response;
                    try {
                        var entity = callbacks.createEntity.apply(self, [ self, response.data, response ]);
                        if (self._cache && entityId) {
                            self.fullFillOrRejectPromises.apply(self, [ entityId, entity, response ]);
                        } else {
                            promise.fulfilled(entity);
                        }
                    } catch (cause) {
                    	var error = new Error("Invalid response");
                    	error.cause = cause;
                        if (self._cache && entityId) {
                            self.fullFillOrRejectPromises.apply(self, [ entityId, error ]);
                        } else {
                            promise.rejected(error);
                        }
                    }
                },
                function(error) {
                    if (self._cache && entityId) {
                        self.fullFillOrRejectPromises.apply(self, [ entityId, error ]);
                    } else {
                        promise.rejected(error);
                    }
                }
            );
            return promise;
        },
        
        /**
         * Update the specified entity.
         * 
         * @method updateEntity
         * @param url The URL to update the entity.
         * @param options Options for the request.
         * @param callbacks Callbacks used to parse the response.
         * @param sbt/Promise
         */
        updateEntity : function(url, options, callbacks) {
            url = this.constructUrl(url);
            var self = this;
            var promise = new Promise();
            this.endpoint.request(url,options,null,promise).response.then(
                function(response) {
                    promise.response = response;
                    var entity = callbacks.createEntity.apply(self, [ self, response.data, response ]);
                    // callback can return a promise if an additional
                    // request is required to load the associated entity
                    if (entity instanceof Promise) {
                        entity.then(
                            function(response) {                                
                            	// it is the responsibility of the createEntity callback to clear the cache in this case.
                                promise.fulfilled(response);
                            },
                            function(error) {
                                promise.rejected(error);
                            }
                        );
                    } else {
                    	if(entity.id){
                    		self.removeFromCache(entity.id);
                    	}
                    	if(entity.id && entity.data){
                    		self.addToCache(entity.id, entity);
                    	}
                        promise.fulfilled(entity);
                    }
                },
                function(error) {
                    promise.rejected(error);
                }
            );
            return promise;
        },

        /**
         * Delete the specified entity.
         * 
         * @method deleteEntity
         * @param url The URL to delete the entity.
         * @param options Options for the request.
         * @param entityId Id of the entity to delete.
         * @param sbt/Promise
         */
        deleteEntity : function(url,options,entityId) {
            url = this.constructUrl(url);
            var promise = this._validateEntityId(entityId);
            if (promise) {
                return promise;
            }

            var self = this;
            var promise = new Promise();
            this.endpoint.request(url,options,entityId,promise).response.then(
                function(response) {
                    promise.response = response;
                    promise.fulfilled(entityId);
                    self.removeFromCache(entityId);
                },
                function(error) {
                    promise.rejected(error);
                }
            );
            return promise;
        },
        
        /**
         * Perform an XML HTTP Request with cache support.
         * 
         * @method request
         * @param url URL to request
         * @param options Options for the request.
         * @param entityId Id of the rntity associated with the request.
         * @param promise Promise being returned
         * @param sbt/Promise
         */
        request : function(url,options,entityId,promise) {
            url = this.constructUrl(url);
            if (this._cache && entityId) {
                this.pushPromise(entityId, promise);
            }
            return this.endpoint.request(url,options);
        },

        /**
         * Push set of promise onto stack for specified request id.
         * 
         * @method pushPromise
         * @param id Id of the request.
         * @param promise Promise to push.
         */
        pushPromise : function(id,promise) {
            log.debug("pushPromise, id : {0}, promise : {1}", id, promise);
            if (!requests[id]) {
                requests[id] = [];
            }
            requests[id].push(promise);
        },

        /**
         * Notify set of promises and pop from stack for specified request id.
         * 
         * @method fullFillOrRejectPromises
         * @param id
         * @param data
         * @param response
         */
        fullFillOrRejectPromises : function(id,data,response) {
            log.debug("fullFillOrRejectPromises, id : {0}, data : {1}, response : {2}", id, data, response);
            this.addToCache(id, data);
            var r = requests[id];
            if (r) {
                delete requests[id];
                for ( var i = 0; i < r.length; i++) {
                    var promise = r[i];
                    this.fullFillOrReject.apply(this, [ promise, data, response ]);
                }
            }
        },

        /**
         * Fullfill or reject specified promise.
         * 
         * @method fullFillOrReject
         * @param promise
         * @param data
         * @param response
         */
        fullFillOrReject : function(promise,data,response) {
            if (promise) {
                try {
                    promise.response = response;
                    if (data instanceof Error) {
                        promise.rejected(data);
                    } else {
                        promise.fulfilled(data);
                    }
                } catch (error) {
                    log.debug("fullFillOrReject: " + error.message);
                }
            }
        },

        /**
         * Add the specified data into the cache.
         * 
         * @method addToCache
         * @param id
         * @param data
         */
        addToCache : function(id, data) {
            if (this._cache && !(data instanceof Error)) {
                this._cache.put(id, data);
            }
        },
        
        /**
         * Remove the cached data for the specified id.
         * 
         * @method removeFromCache
         * @param id
         */
        removeFromCache : function(id) {
            if (this._cache) {
                this._cache.remove(id);
            }
        },
                
        /**
         * Get the cached data for the specified id.
         * 
         * @method getFromCache
         * @param id
         */
        getFromCache : function(id) {
            if (this._cache) {
                return this._cache.get(id);
            }
        },
                
        /**
         * Create a bad request Error.
         * 
         * @method createBadRequestError
         * @param message
         * @returns {Error}
         */
        createBadRequestError : function(message) {
            var error = new Error();
            error.code = BadRequest;
            error.message = message;
            return error;
        },
        
        /**
         * Create a bad request Promise.
         * 
         * @method createBadRequestPromise
         * @param message
         * @returns {sbt/Promise}
         */
        createBadRequestPromise : function(message) {
            return new Promise(this.createBadRequestError(message));
        },
        
        /**
         * Return true if the specified id is an email.
         * 
         * @method isEmail
         * @param id
         * @returns {Boolean}
         */
        isEmail : function(id) {
            return id && id.indexOf('@') >= 0;
        },
        
        /**
         * Extract the Location parameter from a URL.
         * 
         * @method getLocationParameter
         * @param ioArgs
         * @param name
         * @returns {String}
         */
        getLocationParameter: function (response, name) {
            var location = response.getHeader("Location") || undefined;
            if (location) {
                return this.getUrlParameter(location, name);
            }
        },
        
        /**
         * Extract the specified parameter from a URL.
         * 
         * @mehtod getUrlParameter
         * @param url
         * @param name
         * @returns {Boolean}
         */
        getUrlParameter : function (url, name) {
            return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(url)||[,""])[1].replace(/\+/g, '%20'))||null;
        },
        
        /**
         * Validate a string field, and return a Promise if invalid.
         * 
         * @param fieldName
         * @param fieldValue
         */
        validateField : function(fieldName, fieldValue) {
            if (!fieldValue) {
                var message = "Invalid value {0} for field {1}, the field must not be empty or undefined.";
                message = stringUtil.substitute(message, [ fieldValue || "'undefined'", fieldName ]);
                return this.createBadRequestPromise(message);
            }
        },
        
        /**
         * Validate a map of fields, and return a Promise for first invalid field found.
         * 
         * @param fieldMap
         */
        validateFields : function(fieldMap) {
            for(var name in fieldMap){
                var value = fieldMap[name];
                var promise = this.validateField(name, value);
                if (promise) {
                    return promise;
                }
            }
        },
        
        /**
         * Validate HTML5 File API Support for browser and JS Library	
         */
        validateHTML5FileSupport : function() {
        	if (!window.File || !window.FormData) {
				var message = "HTML 5 File API is not supported by the Browser.";
				return this.createBadRequestPromise(message);
			}
        	// Dojo 1.4.3 does not support HTML5 FormData
			if(util.getJavaScriptLibrary().indexOf("Dojo 1.4") != -1) {
				return this.createBadRequestPromise("Dojo 1.4.* is not supported for Update Profile Photo");
			}
        },
        
        /*
         * Validate the entityId and if invalid notify callbacks
         */
        _validateEntityId : function(entityId) {
            if (!entityId || !lang.isString(entityId)) {
                var message = "Invalid argument {0}, expected valid entity identifier.";
                message = stringUtil.substitute(message, [ entityId || "'undefined'" ]);
                return this.createBadRequestPromise(message);
            }
        },
        
        /**
         * Returns HTML5 File Control object 
         * @param {Object} fileControlOrId FileControl or ID of File Control
         * @returns {Object} FileControl
         */
        getFileControl : function(fileControlOrId) {
            var fileControl = null;
            if (typeof fileControlOrId == "string") {
                fileControl = document.getElementById(fileControlOrId);             
            } else if (typeof fileControlOrId == "object") {
                fileControl = fileControlOrId;
            }
            return fileControl;
        }

    });
    return BaseService;
});
