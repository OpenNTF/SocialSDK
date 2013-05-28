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
define([ "../../declare", "../../dom", "../../lang", "dojo/string", "../../widget/grid/_GridRenderer" ], 
         function(declare, dom, lang, string, _GridRenderer) {

    /**
     * @module sbt.controls.grid.GridRenderer
     * @class  GridRenderer
     * @namespace  sbt.controls.grid
     */
    var GridRenderer = declare([ _GridRenderer ], {
        /**Strings used in the grid*/
    	nls: null,
    	/**CSS class to be used for tables - see ConnectionsGridRenderer*/
        tableClass: null,
        emptyClass: null,
        errorClass: null,
        loadingClass: null,
        loadingImgClass: null,
        
        /**
         * Constructor function
         * @method - constructor
         */
        constructor: function(args) {
           lang.mixin(this, args);
        },
        
        /**
         * Function to render the Paging , sorting and the table
         * @method - render
         * @param grid - the grid
         * @param el - the grid DOM node
         * @param items - the items in the grid row, for example icon, displayName,email etc.
         * @param data - the data associated with the grid row
         */
        render: function(grid, el, items, data) {
           while (el.childNodes[0]) {
               this._destroy(el.childNodes[0]);
           }
           var size = items.length;
           if (size === 0) {
              this.renderEmpty(grid, el);
           }
           else {
              this.renderPager(grid, el, items, data);
              this.renderSorter(grid, el, data);
              var tbody = this.renderTable(grid, el, items, data);
              for (var i=0; i<items.length; i++) {
                  this.renderItem(grid, tbody, data, items[i], i, items);
              }
             
           }
        },
        
        /**
         * Checks if a pagerTemplate exists ,if so, the HTML template is converted to
         * a DOM node, and attached to the body.
         * @method - renderPager
         * @param grid - the grid
         * @param el - the current element
         * @param items - the items in the current element
         * @param data - the data associated with the current element
         */
        renderPager : function(grid,el,items,data) {
            if (this.pagerTemplate && !grid.hidePager) {
                var node;
                if (this._isString(this.pagerTemplate)) {
                    var domStr = this._substituteItems(this.pagerTemplate, grid, this, items, data);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.pagerTemplate.cloneNode(true);
                }
                el.appendChild(node);
                
                this._doAttachEvents(grid, el, data);
            }
        },
        
        /**
         * Converts an HTML sortTemplate to a DOM node and attaches it 
         * @method - renderSorter
         * @param grid - the Grid
         * @param el - the current element
         * @param data - the data associated with the current element
         */
        renderSorter : function(grid,el,data) {
            if (this.sortTemplate && !grid.hideSorter) {
                var sortInfo = grid.getSortInfo();
                if (sortInfo) {
                    var node;
                    if (this._isString(this.sortTemplate)) {
                        var domStr = this._substituteItems(this.sortTemplate, grid, this, sortInfo);
                        node = this._toDom(domStr, el.ownerDocument);
                    } else {
                        node = this.sortTemplate.cloneNode(true);
                    }
                    el.appendChild(node);
                    
                    this._doAttachEvents(grid, el, data);
                }
            }
        },
        
        /***
         * Creates a table and table body, Attaches the table body to the 
         * table, and returns the table body
         * @method - renderTable
         * @param grid - the grid
         * @param el - the current element
         * @param items - all of the items in the current row
         * @param data - the data associated with the current row
         * @returns - A table body element, that is attached to a table
         */
        renderTable: function(grid, el, items, data) {       	
            var table = this._create("table", {
                "class": this.tableClass,
                border:"0",
                cellspacing:"0",
                cellpadding:"0",
               // summary:this.nls.summary
                role:"presentation"
            }, el);
            var tbody = this._create("tbody", null, table);
            return tbody;
        },
        
        /**
         * Creates a DIV and attaches it to the current element
         * Then creates the loading image and attaches it to the DIV 
         * @method - renderLoading
         * @param grid - The grid
         * @param el - The Current Element
         */
        renderLoading: function(grid, el) {
           var div = this._create("div", {
              "class": this.loadingClass,
              innerHTML: this.nls.loading
           }, el, "only");
           var img = this._create("img", {
              "class": this.loadingImgClass,
              src: this._blankGif,
              role: "presentation"
           }, div, "first");
        },
        
        /**
         * Creates a Div, with a different CSS class, to display a grid that has no results
         * @method - renderEmpty
         * @param - grid - The Grid
         * @param - el - The Current Element
         */
        renderEmpty: function(grid, el) {
           while (el.childNodes[0]) {
               this._destroy(el.childNodes[0]);
           }
           var ediv = this._create("div", {
             "class": this.emptyClass,
             innerHTML: this.nls.empty,
             role: "document",
             tabIndex: 0
           }, el, "only");
           //dijit.setWaiState(ediv, "label", this.nls.empty);
        },
        
        /**Creates a div to display a grid that has returned an error
         * @method - renderError
         * @param - grid - The Grid
         * @param - el - The Current Element
         * @param - error - The error message to be displayed*/
        renderError: function(grid, el, error) {
            while (el.childNodes[0]) {
                this._destroy(el.childNodes[0]);
            }
           var ediv = this._create("div", {
              "class": this.errorClass,
              innerHTML: error,
              role: "alert",
              tabIndex: 0
            }, el, "only");
        },
        
        /**
         * @method getSortInfo
         */
        getSortInfo: function() {
        },       
        
        /**
         * Returns the paging results,  - How many pages of results there are
         * @method - pagingResults
         * @return - A String for paging - for example "0 - 5 Of 5"
         */
        pagingResults : function(grid,renderer,items,data) {
            return string.substitute(renderer.nls.pagingResults, data);
        },
        
        /**If the user is on the first page of results, they cannot click
         * to go back a page, this function hides the back link
         * @method - hidePreviousLink
         * @return - A String used as a CSS style 
         */
        hidePreviousLink : function(grid,renderer,items,data) {
            return (data.start > 1) ? "" : "display: none;";
        },
        
        /**Hides the back page label
         * @method - hidePreviousLabel
         * @return - A String used as a CSS style
         */
        hidePreviousLabel : function(grid,renderer,items,data) {
            return (data.start == 0) ? "" : "display: none;";
        },
        
        /**If there is only one page of results the user cannot move forward 
         * to the next page, this function hides the next link
         * @method - hideNextLink
         * @return - A String used as CSS style
         */
        hideNextLink : function(grid,renderer,items,data) {
            return (data.start + data.count < data.totalCount) ? "" : "display: none;";
        },
        
        /**
         * If there is only one page of results the user cannot move forward 
         * to the next page, this function hides the next label
         * @method - hideNextLabel
         * @return - A String used as CSS style
         */
        hideNextLabel : function(grid,renderer,items,data) {
            return (data.start + data.count >= data.totalCount) ? "" : "display: none;";
        },

        /**
         * @method updateSort
         * @param el
         */
        updateSort : function(el) {
        },

        /**
         * @method renderSort
         * @param el
         * @param sort
         * @param i
         * @param sorts
         */
        renderSort : function(el,sort,i,sorts) {
        },
        
        /**
         * A sort anchor is what the grid is sorted against.
         * For example sort by name, date etc. This function returns a string of HTML links
         * @method - sortAnchors
         * @param grid - the grid
         * @param renderer - the associated renderer
         * @param items - items contains a list of sort anchors, which will be substituted into the html
         * @returns {String} - a HTML string consisting of the various sort anchors
         */
        sortAnchors : function(grid,renderer,items) {
            if (items.list == undefined || items.list.length == 0) {
                return "";
            } else {
                var sortStr = "";
                for ( var i = 0; i < items.list.length; i++) {
                    sortStr += this._substituteItem(this.sortAnchor, grid, items.list[i], i, items);
                }
                return sortStr;
            }
        },

        /**
         * @method sortItemClass
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        sortItemClass : function(grid,item,i,items) {
            return (i === 0 ? this.firstClass : "");
        },
        
        /**
         * Sets the CSS class for the sort anchor which is currently being used
         * Also sets CSS classes for whether the results are ascending or descending
         * @method - sortAnchorClass
         * @param grid - The Grid
         * @param item - the current sort anchor - for example "name"
         * @param i - the number of the grid row
         * @param items - all of the sort anchors
         * @returns - A CSS class
         */
        sortAnchorClass : function(grid,item,i,items) {
            if(item !== items.active.anchor) {
                return "";
            }
            
            if(items.active.isDesc) {
                return this.descendingSortClass;
            }
            else {
                return this.ascendingSortClass;
            }
        },
        
        /**
         * Converts the HTML template into a dom node and attaches it.
         * @method - rendeItem
         * @param grid - the grid
         * @param el - the current element
         * @param data - the data for the current row, ie name, last updated etc
         * @param item - the current item and its associated data
         * @param i - the number of the grid row
         * @param items - All of the elements & data for each row
         */
        renderItem : function(grid,el,data,item,i,items) {
            if (this.template) {
                var node;
                if (this._isString(this.template)) {
                    var domStr = this._substituteItem(this.template, grid, item, i, items);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.template.cloneNode(true);
                }
                el.appendChild(node);
                
                this._doAttachEvents(grid, el, item);
            }
        },

        // Internals

        /*
         * Does substitution of ${foo} type properties in template string
         */
        _substituteItem : function(template,grid,item,i,items) {
            var self = this;
            return string.substitute(template, item, function(value,key) {
                if (typeof value == "undefined") {
                    // check the renderer for the property
                    value = this._getObject(key, false, self);
                }

                if (typeof value == 'function') {
                    // invoke function to return the value
                    try {
                        value = value.apply(self, [grid, item, i, items]);
                    } catch (ex) {
                        value = "ERROR:" + key + " " + ex;
                    }
                }
                
                if (typeof value == "undefined" && typeof item.getValue == 'function') {
                    // invoke function to return the value
                    try {
                        value = item.getValue(key);
                    } catch (ex) {
                        value = "ERROR:" + key + " " + ex;
                    }
                }

                if (typeof value == "undefined" || value == null) {
                    return "";
                }

                return value;
            }, this);
        },

        _substituteItems : function(template,grid,renderer,items,data) {
            var self = this;
            return string.substitute(template, renderer, function(value,key) {
                if (typeof value == "undefined") {
                    // check the renderer for the property
                    value = this._getObject(key, false, self);
                }

                if (typeof value == 'function') {
                    // invoke function to return the value
                    try {
                        value = value.apply(self, [grid, renderer, items, data]);
                    } catch (ex) {
                        value = "ERROR:" + key + " " + ex;
                    }
                }

                if (typeof value == "undefined" || value == null) {
                    return "";
                }

                return value;
            }, this);
        },
        
        _doAttachEvents: function(grid, el, data) {
            var nodes = (el.all || el.getElementsByTagName("*"));
            for (var i in nodes) {
                var attachEvent = (nodes[i].getAttribute) ? nodes[i].getAttribute("data-dojo-attach-event") : null;
                if (attachEvent) {
                    nodes[i].removeAttribute("data-dojo-attach-event");
                    var event, events = attachEvent.split(/\s*,\s*/);
                    while((event = events.shift())) {
                        if (event) {
                            var func = null;
                            if (event.indexOf(":") != -1) {
                                var eventFunc = event.split(":");
                                event = this._trim(eventFunc[0]);
                                func = this._trim(eventFunc[1]);
                            } else {
                                event = this._trim(event);
                            }
                            if (!func) {
                                func = event;
                            }
                            var callback = this._hitch(grid, grid[func], nodes[i], data);
                            grid._connect(nodes[i], event, callback);
                        }
                    }
                }
            }
        }
        
    });
    
    return GridRenderer;
});
