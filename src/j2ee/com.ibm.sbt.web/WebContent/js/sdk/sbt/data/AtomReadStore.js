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
 * @module sbt.data.AtomReadStore
 */
define(["../declare","../config", "../lang", "../base/core", "../xml", "../xpath","dojox/html/entities"], function(declare, config, lang, core, xml, xpath, entities) {
    
    /**
     * A data store for Atom XML based services or documents.   This store is still under development
     * and doesn't support filtering or paging yet.
     * 
     * @class AtomReadStore
     * @namespace sbt.data
     */
    var AtomReadStore = declare(null, {
        // private
        _endpoint : null,
        _xmlData : null,
        // read only
        totalResults : 0,
        startIndex : 0,
        itemsPerPage : 5,
        items : null,
        // public
        url : "",
        sendQuery : true,
        unescapeHTML : false,
        urlPreventCache : false,
        atom : core.feedXPath,
        attributes : core.entryXPath,
        namespaces : core.namespaces,
        paramSchema: {},
        
        /**
         * Constructor for the AtomRead store.
         * 
         * @param args
         * An anonymous object to initialize properties. It expects the following values:
         *   url: The url to a service or an XML document that represents the store 
         *   unescapeHTML: A boolean to specify whether or not to unescape HTML text 
         *   sendQuery: A boolean indicate to add a query string to the service URL  
         *   endpoint: the endpoint to be used
         */
        constructor: function(args) {
            this._endpoint = config.findEndpoint(args.endpoint || "connections");

            if (args) {
                this.url = args.url;
                this.attributes = args.attributes || this.attributes;
                this.atom = args.feedXPath || this.atom;
                this.namespaces = args.namespaces || this.namespaces;
                this.paramSchema = args.paramSchema || this.paramSchema;
                this.rewriteUrl = args.rewriteUrl;
                this.label = args.label || this.label;
                this.sendQuery = (args.sendQuery || args.sendquery || this.sendQuery);
                this.unescapeHTML = args.unescapeHTML;
                if ("urlPreventCache" in args) {
                    this.urlPreventCache = args.urlPreventCache ? true : false;
                }
            }
            if(!this.url) {
                throw new Error("sbt.data.AtomReadStore: A service URL must be specified when creating the data store");
            }
        },
        
        /**
         * @method getEndpoint
         * @returns
         */
        getEndpoint: function() {
        	return this._endpoint;
        },
        
        /*
         * Returns defaultValue if and only if *item* does not have a value for *attribute*.
         */
        getValue: function(item, attribute, defaultValue) {
            var xpathCountFunction = /^count\(.*\)$/;
            this._assertIsItem(item);
            this._assertIsAttribute(attribute);
            
            if (!item._attribs[attribute]) {
                var access = this.attributes[attribute];
                if (lang.isFunction(access)) {
                    item._attribs[attribute] = access(item, attribute);
                }else if (access.match(xpathCountFunction)){
                    item._attribs[attribute] = xpath.selectNumber(item.element, this.attributes[attribute], this.namespaces)+"";
                } else {
                    var nodes = xpath.selectNodes(item.element, this.attributes[attribute], this.namespaces);
                    if (nodes && nodes.length == 1) {
                        item._attribs[attribute] = nodes[0].text || nodes[0].textContent;
                    } else if (nodes) {
                        item._attribs[attribute] = [];
                        for (var j=0; j<nodes.length; j++) {
                            item._attribs[attribute].push(nodes[j].text || nodes[j].textContent);
                        }
                    } else {
                        item._attribs[attribute] = null;
                    }
                }
            }

            if (!item._attribs[attribute]) {
                return defaultValue;
            }
            
         
            if(typeof item._attribs[attribute] == "object"){
            	for(var i=0;i<item._attribs[attribute].length; i++){
            		item._attribs[attribute][i] = entities.encode(item._attribs[attribute][i]);
            	}
            }
            else{
            	item._attribs[attribute] = entities.encode(item._attribs[attribute]);
            }
            
            return item._attribs[attribute];
        },
        
        /*
         * This getValues() method works just like the getValue() method, but getValues()
         * always returns an array rather than a single attribute value.
         */
        getValues: function(item, attribute) {
            this._assertIsItem(item);
            this._assertIsAttribute(attribute);
            
            if (!item._attribs[attribute]) {
                var nodes = xpath.selectNodes(item.element, this.attributes[attribute], this.namespaces);
                var values = [];
                for (var i=0; i<nodes.length; i++) {
                    values[i] = nodes[i].text || nodes[i].textContent;
                }
                item._attribs[attribute] = values;
            }

            return item._attribs[attribute];
        },
        
        /*
         *  Returns an array with all the attributes that this item has.
         */
        getAttributes: function(item) {
            var result = [];
            for (var name in this.attributes) {
                if (this.attributes.hasOwnProperty(name)) {
                    result.push(name);
                }
            }
            return result;
        },
        
        /*
         * Returns true if the given *item* has a value for the given attribute*.
         */
        hasAttribute: function(item, attribute) {
            return (this.attributes[attribute] != undefined);
        },
        
        /*
         * Returns true if the given *value* is one of the values that getValues() would return.
         */
        containsValue: function(item, attribute, value) {
            throw new Error("sbt.data.AtomReadStore: Not implemented yet!");
        },

        /*
         * Returns true if *something* is an item and came from the store instance.
         */
        isItem: function(something) {
            if (something && something.element && something.store && something.store === this) {
                return true;
            }
            return false;
        },
        
        /*
         * Return true if *something* is loaded.
         */
        isItemLoaded: function(something) {
            return this.isItem(something);
        },
        
        /*
         * Given an item, this method loads the item so that a subsequent call
         * to store.isItemLoaded(item) will return true.
         */
        loadItem: function(keywordArgs) {
            throw new Error("sbt.data.AtomReadStore: Not implemented yet!");
        },
        
        /*
         * Given a query and set of defined options, such as a start and count of items to return,
         * this method executes the query and makes the results available as data items.
         */
        fetch: function(args) {
            var self = this;
            var scope = args.scope || self;
            
            var serviceUrl = this._getServiceUrl(this._getQuery(args));
            if (!serviceUrl) {
                if (args.onError) {
                    args.onError.call(new Error("sbt.data.AtomReadStore: No service URL specified."));
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
                        
                        
                        
                        // invoke callbacks
                        if (args.onBegin) {
                            args.onBegin.call(scope, self.totalResults, args);
                        }
                        if (args.onItem) {
                            for(var i=0; i<self._entries; i++){
                                args.onItem.call(scope, self.entries[i], args);
                            }
                        }
                        if (args.onComplete) {
                            args.onComplete.call(scope, args.onItem ? null : self.items, args);
                        }
                    } catch (e) {
                        if (args.onError) {
                            args.onError.call(e);
                        }
                    }
                },
                error : function(error) {
                    if (args.onError) {
                        args.onError.call(error);
                    }
                }
            });
        },
        
        /*
         * The getFeatures() method returns an simple keyword values object
         * that specifies what interface features the datastore implements.
         */
        getFeatures: function() {
            return { 'dojo.data.api.Read': true };
        },
        
        /*
         * The close() method is intended for instructing the store to 'close' out
         * any information associated with a particular request.
         */
        close: function(request) {
            throw new Error("sbt.data.AtomReadStore: Not implemented yet!");
        },
        
        /*
         * Method to inspect the item and return a user-readable 'label' for the item
         * that provides a general/adequate description of what the item is.
         */
        getLabel: function(item) {
            return this.getValue(item, this.label);
        },
        
        /*
         * Method to inspect the item and return an array of what attributes of the item were used
         * to generate its label, if any.
         */
        getLabelAttributes: function(item) {
            return [ this.label ];
        },
        
        // Internals
        
        _getQuery: function(args) {
            var query = args.query || {};
            query.pageSize = args.count || query.pageSize;
            var page = Math.floor(args.start / args.count) + 1; // needs to be a whole number
            query.pageNumber = page;
            
            if(args.sort && args.sort[0]) {
                var sort = args.sort[0];
                query.sortBy = sort.attribute;
                if(sort.descending === true) {
                    query.sortOrder = "desc";
                }
                else if(sort.descending === false) {
                    query.sortOrder = "asc";
                }
            }

            return query;
        },
        
        _getServiceUrl: function(query) {
            if (!this.sendQuery) {
                return this.url;
            }
            if (!query) {
                return this.url;
            }
            if (lang.isString(query)) {
                return this.url + query;
            }
            
            var queryString = "";
            var paramSchema = this.paramSchema;
            for(var key in query) {
                if(key in paramSchema) {
                    var val = paramSchema[key].format(query[key]);
                    if(val) {
                        queryString += val + "&";
                    }
                }
                else {
                    queryString += (key + "=" + query[key] + "&");
                }
            }

            if (!queryString) {
                return this.url;
            }
            var serviceUrl = this.url;
            if (serviceUrl.indexOf("?") < 0){
                serviceUrl += "?";
            } else {
                serviceUrl += "&";
            }
            return serviceUrl + queryString;
        },

        _createItems: function(document) {
            var nodes = xpath.selectNodes(document, this.atom.entries, this.namespaces);
            var items = [];
            for (var i=0; i<nodes.length; i++) {
                items.push(this._createItem(nodes[i]));
            }
            return items;
        },
        
        _createItem: function(element){
            var self = this;
            return {
                element : element,
                store : this,
                _attribs : {},              
                getValue : function(attribute) {
                    if (self.hasAttribute(this, attribute)) {
                        return self.getValue(this, attribute);
                    }
                }
            };
        },
        
        _assertIsItem: function(item) {
            if (!this.isItem(item)) {
                throw new Error("sbt.data.AtomReadStore: Invalid item argument.");
            }
        },
        
        _assertIsAttribute: function(attribute) {
            if (!this.attributes[attribute]) {
                throw new Error("sbt.data.AtomReadStore: Invalid attribute argument.");
            }
        }
        
        
    });
    return AtomReadStore;
    
});