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
 * 
 */
define([ "../../declare", "../../lang", "../../itemFactory", "../../stringUtil", "../../widget/grid/_Grid", "../../util"], 
        function(declare, lang, itemFactory, stringUtil, _Grid, util) {

    /**
     * @class grid
     * @namespace sbt.controls.grid
     * @module sbt.controls.grid.Grid
     */
    var Grid = declare([ _Grid ], {

    	/**
    	 * Data associated with this Grid
    	 */
        data: null,
        
        /**
         * The renderer associated with the grid
         */
        renderer: null,
        
        /**
         * Encode all of the data coming from the connections server 
         * as HTML entities, to prevent XSS attacks
         */
        encodeHtml: true,
        
        /**
         * The number of grid rows displayed per page
         */
        pageSize: 5,
        
        /**
         * Flag to hide the pager
         */
        hidePager: false,
        
        /**
         * Flag to hide the sorter
         */
        hideSorter: false,
        
        /**
         * FilterTag, is used for sorting and paging, as to only sort as filtered set of results
         */
        filterTag: "",
        
        /**
         * Selected rows are the rows of the grid that have been selected by checking a check box 
         */
        selectedRows: null,
        
        /*
         * TODO remove this?
         */
        _strings: {},

        /*
         * Arguments for the associated data store
         */
        _storeArgs: null,

        /*
         * Regular expression used to remove // from url's
         */
        _regExp : new RegExp("/{2}"),

        /**
         * Empty context root map, can be overridden by subclasses of Grid. Represents Connections context roots.
         */
        contextRootMap: {},
        
        /**
         * Constructor method for the grid.
         * Creates a default store and renderer, if none have been already created
         * @method constructor
         * @param args
         */
        constructor: function(args) {
            lang.mixin(this, args);
            
            this.selectedRows = [];
            
            if (!this.store) {
                if (args && args.storeArgs) {
                    this._storeArgs = lang.mixin({}, args.storeArgs);
                    this._storeArgs.endpoint = this.endpoint;
                    this.store = this.createDefaultStore(args.storeArgs);
                } else if (this.options) {
                    this._storeArgs = lang.mixin({}, this.options[this.defaultOption].storeArgs);
                    this._storeArgs.endpoint = this.endpoint;
                    if (args && args.type && this.options.hasOwnProperty(args.type)) {
                        lang.mixin(this._storeArgs, this.options[args.type].storeArgs); 
                    }   
                }
                this.store = this.createDefaultStore(this._storeArgs);
            }
            
            if (!this.renderer) {
                if (args && args.rendererArgs) {
                    this.renderer = this.createDefaultRenderer(args.rendererArgs);
                } else if (this.options) {
                    var rendererArgs = this.options[this.defaultOption].rendererArgs;
                    if (args && args.type && this.options.hasOwnProperty(args.type)) {
                        rendererArgs = this.options[args.type].rendererArgs;
                    }

                    this.renderer = this.createDefaultRenderer(rendererArgs);
                }
            }
        },
        
        /**
         * Create the store to be used with this Grid.
         * 
         * @method - createDefaultStore
         * @param args - the arguments to pass to the atom store, such as URL and attributes
         * @returns - an atom store instance
         */
        createDefaultStore: function(args) {
            var store = this._createDefaultStore(args);
            var url = store.getUrl();
            if (url) {
                url = this.buildUrl(url, args, store.getEndpoint());
            }
            store.setUrl(url);

            return store;
        },
        
        /**
         * Allow Grid to build the complete URL before it is passed to the store.
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint The endpoint, needed to verify if custom service mappings are present.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = {};
            if (this.query) {
                params = lang.mixin(params, this.query);
            }
            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * Create the renderer to be used with this Grid.
         * 
         * @method - createDefaultRenderer
         * @param args
         */
        createDefaultRenderer: function(args) {
        },
        
        /**
         * Post create function is called after grid has been created.
         * @method - postCreate
         */
        postCreate: function() {
           this.inherited(arguments);      
           
           if (this.renderer && this.store) {
               this.renderer.renderLoading(this, this.gridNode);
           }
           
           if (this.updateOnCreate) {
               this.created = true;
               this.update();
           }
        },
        
        /**
         * Refresh the grid
         * @method - refresh
         */
        refresh: function() {
           if (this.data) {
              if (this.store) {
                 this.update(null);
              } else {
                 this.update();
              }
           }
           
           this.selectedRows = [];
        },
        
        /**
         * Update the grid
         * @method - update
         */
        update: function(data) {
        	this.selectedRows = [];
        	
           if (arguments.length > 0) {
              this.data = data;
           }
           if (this.data) {
               this.renderer.render(this, this.gridNode, this.data.items, this.data);
               this.onUpdate(this.data);
           } else if (this.store) {
        	   if (this._activeSortAnchor && this._activeSortIsDesc !== undefined) {
        		   this._doQuery(this.store, { start : 0, count : this.pageSize, sort: [{ attribute : this._activeSortAnchor.sortParameter, descending : this._activeSortIsDesc }] });   
        	   } else {
        		   this._doQuery(this.store, { start : 0, count : this.pageSize });
        	   }
               
           } else {
              this.renderer.renderEmpty(this, this.gridNode, this.data);
              this.onUpdate(this.data);
           }
        },

        /**
         * @method onUpdate
         * @param data
         */
        onUpdate: function(data) {
        },
        
        /**
         * @method getSortInfo
         */
        getSortInfo: function() {
        },
        
        /**
         * Go back to the previous page
         * @method - prevPage
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         */
        prevPage: function(el, data, ev) {
            this._stopEvent(ev);
            
            if (this.store) {
            	
            	//if sorting
            	if(this._activeSortAnchor){
	                var options = { 
	                		start : 0, count : this.pageSize,
	                		sort: [{ attribute: this._activeSortAnchor.sortParameter }]
	                };
	               
	                if(this._activeSortIsDesc !== undefined){
	                	options.sort[0].descending = this._activeSortIsDesc;
	                }
            	}else{
            		var options = { 
	                		start : 0, count : this.pageSize
	                };
            	}
            if (this.data) {
                options.start = Math.max(0, this.data.start - options.count);
            }
            
            if(this.filterTag != "" && this.filterTag != null){
           	 options.tag = this.filterTag;
            }
            
            this._doQuery(this.store, options);
            }
        },
        
        show10ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 10);
        },
        
        show25ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 25);
        },
        
        show50ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 50);
        },
        
        show100ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 100);
        },
        
        /**
         * Displays the feed for the content that is currently shown.
         * @method - viewFeed
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         */
        viewFeed: function(el, data, ev) {
        	var endpoint = this.store.getEndpoint();
        	var proxy = endpoint.proxy;
        	var baseUrl = endpoint.baseUrl;
        	var proxyPath = endpoint.proxyPath;
        	var url = proxy.rewriteUrl(baseUrl, this.store.getUrl(), proxyPath);
        	window.open(url, "_new");
        },
        
        /**
         * Show "count" items per page
         * @method - showItemsPerPage
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         * @param count - the number of items to display per page
         */
        showItemsPerPage: function(el, data, ev, count) {
        	this._stopEvent(ev);
        	if (this.store) {
        		this.pageSize = count;
        		this.update(null);
        	}
        },
        
        
        /**
         * Move forward to the next page of grid rows
         * @method - nextPage
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         */
        nextPage: function(el, data, ev) {
            this._stopEvent(ev);
            
            if (this.store) {
            	//if there is sorting available
            	if(this._activeSortAnchor){
	            	var options = { 
	                    start : 0, count : this.pageSize ,
	                    sort: [{ attribute: this._activeSortAnchor.sortParameter }]
	                };
	            	if(this._activeSortAnchor !== undefined){
	               	   options.sort[0].descending = this._activeSortIsDesc;
	                }
            	} else {
            		var options = { 
	                    start : 0, count : this.pageSize 
	                };
            	}
                if (this.data) {
                    options.start = this.data.start + options.count;
                    options.count = this.pageSize;
                    options.total = this.data.totalCount;
                }
                if(this.filterTag != "" && this.filterTag != null){
            	    options.tag = this.filterTag;
                }
                this._doQuery(this.store, options);
            }
        },
        
        /**
         * Called when the user clicks a checkbox 
         * The row gets added or removed to an array, 
         * to retrieve the array call getSelected
         * @method handleCheckBox
         */
        handleCheckBox: function (el, data, ev){       	
        	if (el.checked) {
        		this.selectedRows.push(data);
        	} else if (!el.checked) {
        		var rows = this.getSelected();
        		for(var i=0;i<rows.length;i++){
        			if(rows[i].data == data){
        				//selected row
        				this.selectedRows.splice(i,1);
        			}
        		}
        	}
        },
        
        /**
         * If the grid rows have checkboxes , get a list of the rows which are currently selected
         * (That have a checked checkbox)
         * @method - getSelected
         * 
         */
        getSelected: function() {
            var items = [];
            if (this.selectedRows) {
                for (var i=0; i<this.selectedRows.length; i++) {
                        var item = {
                            data: this.selectedRows[i]
                        };
                        items.push(item);
                }
            }
            return items;
        },
        
        /**
         * Add an item using the specified Document
         * 
         * @method addItem
         * @param document
         */
        addItems: function(document) {
            if (!this.data) {
                this.data = { items: [], start: 0, end: 0, count: 0, totalCount: 0 };
            }
            var attributes = this._storeArgs.attributes;
            var items = itemFactory.createItems(document, attributes, this);
            this.data.items = this.data.items.concat(items); 
            this.data.totalCount += this.data.items.length;
            this.data.end = this.data.count = this.data.totalCount;
        },
        
        /**
         * Insert the specified item into the grid at the specified index.
         * 
         * TODO This is here so that we can insert the local user grid at the start of the items array, mainly.
         * The args like email are not available at the start. So try to see if the user can be inserted to the start of some asrray before it goes to get the different profiles.
         * @param e
         */
        insertItem: function(document, index) {
            if(!this.data){
                console.log("Data is not yet present, adding to beginning.");
                this.data = { items: [], start: 0, end: 0, count: 0, totalCount: 0 };
                index = 0;
            }
            var attributes = this._storeArgs.attributes;
            var items = itemFactory.createItems(document, attributes, this);
            this.data.items.splice(index, 0, items[0]); 
            this.data.totalCount = this.data.items.length;
            this.data.end = this.data.count = this.data.totalCount;
        },
        
        /**
         * @method encodeImageUrl
         * @param url
         */
        encodeImageUrl: function(url) {
        	var ep = this.store.getEndpoint();
        	return ep.proxy.rewriteUrl(ep.baseUrl, url, ep.proxyPath);
        },
        
        /**
         * Return the auth type to be  used
         * @returns {String}
         */
        getAuthType: function() {
        	return "";
        },
        
        /**
         * Return the url parameters to be used
         * @returns {Object}
         */
        getUrlParams: function() {
        	return { authType : this.getAuthType() };
        },
        
        /**
         * Construct a url using the specified parameters 
         * @method constructUrl
         * @param url
         * @param params
         * @param urlParams
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns
         */
        constructUrl : function(url,params,urlParams, endpoint) {
            if (!url) {
                throw new Error("Grid.constructUrl: Invalid argument, url is undefined or null.");
            }
            
            if(endpoint){
                lang.mixin(this.contextRootMap, endpoint.serviceMappings);
                
                if(this.contextRootMap){
                    url = stringUtil.transform(url, this.contextRootMap, function(value, key){
                        if(!value){
                            return key;
                        }
                        else{
                            return value;
                        }
                    }, this);
                }
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
                    if (params[param]) {
                        if (url.indexOf("?") == -1) {
                            url += "?";
                        } else if (url.indexOf("&") != (url.length - 1)) {
                            url += "&";
                        }
                        url += param + "=" + encodeURIComponent(params[param]);
                    }
                }
            }
            return url;
        },

        // Internals
        
        /*
         * Sort the contents
         */
        _sort: function(index, defaultOrder, el, data, ev) {
        	this._stopEvent(ev);
            var options = {
                start: data.start, count: this.pageSize, 
                sort: [{ attribute: index }]
            };
            
            if(this.filterTag != "" && this.filterTag != null){
           	 options.tag = this.filterTag;
            }
            
            if(this._activeSortAnchor === this._sortInfo[index]) {
                this._activeSortIsDesc = !this._activeSortIsDesc; // Flip sort order
            }
            else { // Change active sort to anchor clicked and its default order
                this._activeSortAnchor = this._sortInfo[index];
                this._activeSortIsDesc = defaultOrder;
            }
            options.sort[0].descending = this._activeSortIsDesc;

            this._doQuery(this.store, options);

        },
        
        /*
         * Filter the contents
         */
        _filter: function(args, data){
        	var options = {
        			start: 0, count: this.pageSize,
                    sort: [{ attribute: this._activeSortAnchor.sortParameter  }]
                };        	
        	options.sort[0].descending = this._activeSortIsDesc;
        	
        	var query = {};
        	if(args.tag){
        		query.tag = args.tag;
        		this.filterTag = args.tag;
        	}

            this._doQuery(this.store, options,query);
        },
        
        /*
         * Display the specified error message 
         */
        _updateWithError: function(e) {
        	console.error(e.message);
            this.renderer.renderError(this, this.gridNode, e.message);
        }

    });
    
    return Grid;
});
