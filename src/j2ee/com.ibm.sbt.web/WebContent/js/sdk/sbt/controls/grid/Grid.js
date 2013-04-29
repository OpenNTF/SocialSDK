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
define([ "sbt/_bridge/declare", "sbt/lang", "sbt/itemFactory", "sbt/widget/grid/_Grid"], 
        function(declare, lang, itemFactory, _Grid) {

    /**
     * @class grid
     * @namespace sbt.controls.grid
     * @module sbt.controls.grid.Grid
     */
    declare("sbt.controls.grid.Grid", [ _Grid ], {

        _strings: {},
        _storeArgs: null,
        
        data: null,
        
        /**The renderer associated with the grid*/
        renderer: null,
        /**Encode all of the data coming from the connections server 
         * As HTML entities, to prevent XSS attacks*/
        encodeHtml: true,
        /**The number of grid rows displayed per page*/
        pageSize: 5,
        /**Flag to hide the pager*/
        hidePager: false,
        /**Flag to hide the sorter*/
        hideSorter: false,
        /**FilterTag, is used for sorting and paging, as to only sort as filtered set of results*/
        filterTag: "",
        /** The name of the endpoint for the grid to use, IE connections,smartcloud */
        endpointName: null,
        
        /**
         * Constructor method for the grid.
         * Creates a default store and renderer, if none have been already created
         * @method constructor
         * @param args
         */
        constructor: function(args) {
        	
        	if(args && args.endpoint){
        		endpointName = args.endpoint;
        	}
        	
            lang.mixin(this, args);
            
            if (!this.store) {
                if (args && args.storeArgs) {
                    this._storeArgs = args.storeArgs;
                    this.store = this.createDefaultStore(args.storeArgs);
                } else if (this.options) {
                    this._storeArgs = this.options[this.defaultOption].storeArgs;
                    if (args && args.type && this.options.hasOwnProperty(args.type)) {
                        this._storeArgs = this.options[args.type].storeArgs;
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
            return this._createDefaultStore(args);
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
        
        /**Refresh the grid
         * @method - refresh
         * */
        refresh: function() {
           if (this.data) {
              if (this.data.fromUrl) {
                 this.update(null);
              } else {
                 this.update();
              }
           }
        },
        
        /**Update the grid
         * @method - update
         * */
        update: function(data) {
           if (arguments.length > 0) {
              this.data = data;
           }
           if (this.data) {
               this.renderer.render(this, this.gridNode, this.data.items, this.data);
               this.onUpdate(this.data);
           } else if (this.store) {
               this._doQuery(this.store, { start : 0, count : this.pageSize });
           } else {
              this.renderer.renderEmpty(this, this.gridNode, this.data);
              this.onUpdate(this.data);
           }
        },

        onUpdate: function(data) {
        },
        
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
            	}else{
            		var options = { 
	                		start : 0, count : this.pageSize,
	                };
            	}
            if (this.data) {
                options.start = Math.max(0, this.data.start - options.count);
            }
            
            if(this.filterTag != "" && this.filterTag != null){
           	 options.tag = this.filterTag;
            }
            
            //if sorting is being used
            if(this._activeSortIsDesc){
            	options.sort[0].descending = this._activeSortIsDesc;
            }
            this._doQuery(this.store, options);
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
            	}else {
            		var options = { 
	                    	start : 0, count : this.pageSize 
	                };
            	}
             if (this.data) {
                 options.start = this.data.start + options.count;
             }
             
             if(this.filterTag != "" && this.filterTag != null){
            	 options.tag = this.filterTag;
             }
             
             //if there is sorting
             if(this._activeSortAnchor){
            	 options.sort[0].descending = this._activeSortIsDesc;
             }
             this._doQuery(this.store, options);
            }
        },
        
        /**If the grid rows have checkboxes , get a list of the rows which are currently selected
         * (That have a checked checkbox)
         * @method - getSelected
         * */
        getSelected: function() {
            var items = [];
            var checkboxes = this.renderer._checkboxes;
            if (checkboxes) {
                for (var i=0; i<checkboxes.length; i++) {
                    if (checkboxes[i].element.checked) {
                        var item = {
                            index: i,
                            //data: checkboxes[i].getUserData("item") 
                            data: checkboxes[i].row
                        };
                        items.push(item);
                    }
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
        insertItem: function(document, index){
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
        
        // Internals
        
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
        
        _filter: function(args, data){
        	
        	var options = {
        			start: 0, count: this.pageSize,
                    sort: [{ attribute: this._activeSortAnchor.sortParameter  }],
                };        	
        	options.sort[0].descending = this._activeSortIsDesc;
        	
        	var query = {};
        	if(args.tag){
        		query.tag = args.tag;
        		this.filterTag = args.tag;
        	}

            this._doQuery(this.store, options,query);
        	
        },
        

        _updateWithError: function(e) {
           this.renderer.renderError(this, this.gridNode, e);
        }

    });
    
    return sbt.controls.grid.Grid;
});