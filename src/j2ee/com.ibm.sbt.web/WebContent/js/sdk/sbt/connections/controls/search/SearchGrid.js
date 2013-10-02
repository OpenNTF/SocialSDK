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
define([ "../../../declare", 
         "../../../lang", 
         "../../../config", 
         "../../../controls/grid/Grid", 
         "./SearchGridRenderer", 
         "../../../store/parameter",
         "../../../connections/SearchConstants"], 
        function(declare, lang, sbt, Grid, SearchGridRenderer, parameter, consts) {

    var sortVals = {
        relevance: "",
        date: "date"
    };
    
    var ParamSchema = {
        pageNumber: parameter.oneBasedInteger("page"),  
        pageSize: parameter.oneBasedInteger("ps"),
        sortBy: parameter.sortField("sortKey",sortVals),
        sortOrder: parameter.sortOrder("sortOrder") 
    };
	
    /**
     * @class SearchGrid
     * @namespace sbt.connections.controls.search
     * @module sbt.connections.controls.search
     */
    var searchGrid = declare(Grid, {

        options : {
            "all" : {
                storeArgs : {
                    url : consts.mySearch,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "all"
                }
            },
            
            "public" : {
                storeArgs : {
                    url : consts.publicSearch,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "all"
                }
            },
            
            "people" : {
                storeArgs : {
                    url : consts.searchPeople,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "people"
                }
            },
            
            "tags" : {
                storeArgs : {
                    url : consts.tagsSearch,
                    attributes : consts.SearchXPath
                },
                rendererArgs : {
                    type : "tags"
                }
            },
            
            "apps" : {
                storeArgs : {
                    url : consts.sourceSearch,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "apps"
                }
            },
        },

        defaultOption : "all",
        
        constructor: function() {
            var nls = this.renderer.nls;
            
            this._sortInfo = {
                relevance: { 
                    title: nls.sortByRelevance, 
                    sortMethod: "sortByRelevance",
                    sortParameter: "relevance" 
                },
                date: {
                    title: nls.sortByDate, 
                    sortMethod: "sortByDate",
                    sortParameter: "date"   
                }
               
            };

            this._activeSortAnchor = this._sortInfo.relevance;
            this._activeSortIsDesc = true;
        },
        
        contextRootMap: {
            search: "search"
        },

        /**
         * Override buildUrl to add format and component
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { format : this.format };
            
            if(url.indexOf("?") != -1){
            	url = url.substring(0, url.indexOf("?"));
            }
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.component) {
            	params = lang.mixin(params, { component : this.component });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * Return an instance of SearchGridRenderer.
         * 
         * @method createDefaultRenderer
         * @param args
         */
        createDefaultRenderer : function(args) {
            return new SearchGridRenderer(args);
        },
        
        /**
         * @method getSortInfo
         * @returns A list of strings that describe how the grid can be sorted
         * for profile grids these strings are "Display Name" and "Recent"
         */
        getSortInfo: function() {
            return {
                active: {
                    anchor: this._activeSortAnchor,
                    isDesc: this._activeSortIsDesc
                },
                list: [this._sortInfo.relevance, this._sortInfo.date]
            };
        },
        
        /**
         * @method sortByRelevance
         * @param el
         * @param data
         * @param ev
         */
        sortByRelevance: function(el, data, ev){
            this._sort("relevance", true, el, data, ev);
        },

        /**
         * Sort the grid rows by last modified date
         * @method sortByDate
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByDate: function(el, data, ev) {
            this._sort("date", true, el, data, ev);
        }
        
        // Internals

    });

    return searchGrid;
});