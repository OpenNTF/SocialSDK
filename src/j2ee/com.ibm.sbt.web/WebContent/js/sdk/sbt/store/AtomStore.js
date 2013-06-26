/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.store.AtomStore
 */
define(["../declare","../config","../lang", "../base/core", "../xml", "../xpath", "../itemFactory",
        "dojo/_base/Deferred", "dojo/promise/Promise", "dojo/store/util/QueryResults", "dojox/html/entities"], 
        function(declare,config,lang, core, xml, xpath, itemFactory, Deferred, Promise, QueryResults, entities) {
  
    /**
     * @class sbt.store.AtomStore
     */
    var AtomStorePromise = declare(Promise, {
        // private
        _store : null,
        _isRejected : false,
        _isFulfilled : false,
        _isCancelled : false,
        _callbacks : [],
        _errbacks :  [],
        _endpoint : null,
        _xmlData : null,
        // read only
        totalResults : null,
        startIndex : 0,
        itemsPerPage : 5,
        items : null,
        // public
        url : "",
        sendQuery : true,
        unescapeHTML : false,
        atom : core.feedXPath,
        attributes : core.entryXPath,
        namespaces : core.namespaces,
        paramSchema: {},
        
        /**
         * Constructor for the AtomStore promise.
         * @param args requires
         * 	endpoint: the endpoint to be used
         */
        constructor: function(args, query, options) {
            this._endpoint = config.findEndpoint(args.endpoint || "connections");
            this._options = options;
            this._callbacks = [];
            this._errbacks = [];
            
            if (args) {
                this.url = args.url;
                this.attributes = args.attributes || this.attributes;
                this.atom = args.feedXPath || this.atom;
                this.namespaces = args.namespaces || this.namespaces;
                this.sendQuery = args.hasOwnProperty("sendQuery") ? args.sendQuery : this.sendQuery;
                this.unescapeHTML = args.unescapeHTML || this.unescapeHTML;
                this.paramSchema = args.paramSchema || this.paramSchema;
            }
            
            // add paging information to the query
            if (this.paramSchema.pageNumber) {
            	var page = Math.floor(options.start / options.count) + 1;
            	query.pageNumber = query.pageNumber || page;
            }
            if (this.paramSchema.startIndex) {
            	query.startIndex = query.startIndex || options.start;
            }
            if (this.paramSchema.pageSize) {
            	query.pageSize = query.pageSize || options.count;
            }
            
            // add the sorting information to the query
            if(options.sort && options.sort[0]) {
                if(options.sort[0].attribute) {
                    query.sortBy = options.sort[0].attribute;
                }

                if(options.sort[0].descending === true) {
                    query.sortOrder = "desc";
                }
                else if(options.sort[0].descending === false) {
                    query.sortOrder = "asc";
                }
            }

            var fetchArgs = {
                query : query
            };

            this._doFetch(fetchArgs);
        },

        /*
         * Add new callbacks to the promise.
         */
        then: function(callback, errback, progback) {
            if (this._isFulfilled) {
                callback(this.items);
                return;
            }
            
            if (callback) {
                this._callbacks.push(callback);
            }
            if (errback) {
                this._errbacks.push(errback);
            }
        },

        /*
         * Inform the deferred it may cancel its asynchronous operation.
         */
        cancel: function(reason, strict) {
            this._isCancelled = true;
        },

        /*
         * Checks whether the promise has been resolved.
         */
        isResolved: function() {
            return this._isRejected || this._isFulfilled;
        },

        /*
         * Checks whether the promise has been rejected.
         */
        isRejected: function() {
            return this._isRejected;
        },

        /*
         * Checks whether the promise has been resolved or rejected.
         */
        isFulfilled: function() {
            return this._isFulfilled;
        },

        /*
         * Checks whether the promise has been canceled.
         */
        isCanceled: function() {
            return this._isCancelled;
        },
        
        // Internals
        
        /*
         * Given a query and set of defined options, such as a start and count of items to return,
         * this method executes the query and makes the results available as data items.
         */
        _doFetch: function(args) {
            var self = this;
            var scope = args.scope || self;
            
            var serviceUrl = this._getServiceUrl(args.query);
            if (!serviceUrl) {
                if (args.onError) {
                    args.onError.call(new Error("sbt.store.AtomStore: No service URL specified."));
                }
                return;
            }
            
            this._endpoint.xhrGet({
                serviceUrl : serviceUrl,
                handleAs : "text",
                load : function(response) {
                    try {
                        // parse the data
                        self._xmlData = xml.parse(response);
                        self.totalResults = parseInt(xpath.selectText(self._xmlData, self.atom.totalResults, self.namespaces));
                        self.startIndex = parseInt(xpath.selectText(self._xmlData, self.atom.startIndex, self.namespaces));
                        self.itemsPerPage = parseInt(xpath.selectText(self._xmlData, self.atom.itemsPerPage, self.namespaces));
                        self.items = self._createItems(self._xmlData);
                        //self.items = itemFactory.createItems(self._xmlData, self.attributes, self, entities);
                        if (self._options.onComplete) {
                            self._options.onComplete.call(self._options.scope || self, self.items, self._options);
                        }
                        // invoke callbacks
                        self._fulfilled(self.totalResults);
                    } catch (error) {
                        self._rejected(error);
                    }
                },
                error : function(error) {
                    self._rejected(error);
                }
            });
        },
        
        /*
         * Create the service url and include query params
         */
        _getServiceUrl: function(query) {
            if (!this.sendQuery) {
                return this.url;
            }
            if (!query) {
                return this.url;
            }
            if (lang.isString(query)) {
            	return this.url + (~this.url.indexOf('?') ? '&' : '?') + query;
            }
            
            var pairs = [];
            var paramSchema = this.paramSchema;
            for(var key in query) {
                if (key in paramSchema) {
                    var val = paramSchema[key].format(query[key]);
                    if (val) {
                    	pairs.push(val);
                    }
                } else {
                	pairs.push(encodeURIComponent(key) + "=" + encodeURIComponent(query[key]));
                }
            }
            if (pairs.length == 0) {
                return this.url;
            }
            
            return this.url + (~this.url.indexOf('?') ? '&' : '?') + pairs.join("&");
        },
        
        /*
         * Create a query string from an object
         */
        _createQuery: function(queryMap) {
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

        _createItems: function(document) {
            var nodes = xpath.selectNodes(document, this.atom.entries, this.namespaces);
            var items = [];
            for (var i=0; i<nodes.length; i++) {
                items.push(this._createItem(nodes[i]));
            }
            return items;
        },
        
        _createItem: function(element) {
            var attribs = this._getAttributes();
            var xpathCountFunction = /^count\(.*\)$/;
            
            // TODO add item.index and item.attribs
            var item = { 
                element : element,
                getValue : function(attrib) { 
                	var result = [];
                	 if(typeof this[attrib] == "object"){
                     	for(var i=0;i<this[attrib].length; i++){
                     		result[i] = entities.encode(this[attrib][i]);
                     	}
                     }
                     else{
                    	 result = entities.encode(this[attrib]);
                     }
                	
                	return result; 
                }
            };
            for (var i=0; i<attribs.length; i++) {
                var attrib = attribs[i];
                var access = this.attributes[attrib];
                if (lang.isFunction(access)) {
                    item[attrib] = access(this, item);
                } else if (access.match(xpathCountFunction)){
                    item[attrib] = xpath.selectNumber(element, access, this.namespaces)+"";
                } else {
                    var nodes = xpath.selectNodes(element, access, this.namespaces);
                    if (nodes && nodes.length == 1) {
                        item[attrib] = nodes[0].text || nodes[0].textContent;
                    } else if (nodes) {
                        item[attrib] = [];
                        for (var j=0; j<nodes.length; j++) {
                            item[attrib].push(nodes[j].text || nodes[j].textContent);
                        }
                    } else {
                        item[attrib] = null;
                    }
                }
                
                //item[attrib] = (this.unescapeHTML) ? entities.decode(item[attrib]) : item[attrib];
            }
           
            return item;
        },
        
        _getAttributes: function() {
            var result = [];
            for (var name in this.attributes) {
                if (this.attributes.hasOwnProperty(name)) {
                    result.push(name);
                }
            }
            return result;
        },
        
        _fulfilled : function(totalCount) {
            if (this._isCancelled) {
                return;
            }
            this._isFulfilled = true;
            while (this._callbacks.length > 0) {
                var callback = this._callbacks.shift();
                callback(totalCount);
            }
        },
        
        _rejected : function(error) {
            if (this._isCancelled) {
                return;
            }
            this._isRejected = true;
            while (this._errbacks.length > 0) {
                var errback = this._errbacks.shift();
                errback(error);
            }
        }
        
    });
    
    /**
      * @module sbt.store.AtomStore
      */
    var AtomStore = declare(null, {
        
        // Indicates the property to use as the identity property. The values of this
        // property should be unique.
        idProperty: "id",
        
        _args : null,
        
        /**
         * Constructor for the Atom store.
         * 
         * @param args
         * An anonymous object to initialize properties. It expects the following values:
         *   url: The url to a service or an XML document that represents the store 
         *   unescapeHTML: A boolean to specify whether or not to unescape HTML text 
         *   sendQuery: A boolean indicate to add a query string to the service URL  
         */
        constructor: function(args) {
            this._args = args;

            //if(!args.url) {
            //    throw new Error("sbt.store.AtomStore: A service URL must be specified when creating the data store");
            //}
        },
        
        /**
         * @method getEndpoint
         * @returns
         */
        getEndpoint: function() {
        	return config.findEndpoint(this._args.endpoint || "connections");
        },
        
        /**
         * Retrieves an object by its identity
         * @method get
         * @param id
         */
        get: function(id) {
            throw new Error("sbt.store.AtomStore: Not implemented yet!");
        },

        /**
         * Returns an object's identity
         * @method getIdentity
         * @param object
         */
        getIdentity: function(object) {
            return object.id;
        },
        
        /**
         * Queries the store for objects. This does not alter the store, but returns a set of data from the store.
         * @method query
         * @param query
         * @param options
         */
        query: function(query, options) {
            var results = new AtomStorePromise(this._args, query, options);
            results.total = results;
            return QueryResults(results);
        }
    });
    return AtomStore;
    
});
