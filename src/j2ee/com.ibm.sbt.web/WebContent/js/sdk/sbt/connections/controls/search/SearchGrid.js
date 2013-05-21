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
define([ "sbt/declare", 
         "sbt/controls/grid/Grid", 
         "sbt/controls/grid/connections/SearchGridRenderer", 
         "sbt/store/AtomStore", 
         "sbt/config", 
         "dojo/string", 
         "sbt/connections/SearchConstants"], 
        function(declare, Grid, SearchGridRenderer, AtomStore, sbt, string) {

    /**
     * @class SearchGrid
     * @namespace sbt.controls.grid.connections
     * @module sbt.controls.connections.SearchGrid
     */
    declare("sbt.controls.grid.connections.SearchGrid", Grid, {

        options : {
            "all" : {
                storeArgs : {
                    url : sbt.connections.searchUrls.searchAll,
                    attributes : sbt.connections.searchConstants.xpath_search
                },
                rendererArgs : {
                    type : "all"
                }
            },
            
            "people" : {
                storeArgs : {
                    url : sbt.connections.searchUrls.searchPeople,
                    attributes : sbt.connections.searchConstants.xpath_search
                },
                rendererArgs : {
                    type : "people"
                }
            },
            
            "tags" : {
                storeArgs : {
                    url : sbt.connections.searchUrls.searchTags,
                    attributes : sbt.connections.searchConstants.xpath_search
                },
                rendererArgs : {
                    type : "tags"
                }
            },
            
            "apps" : {
                storeArgs : {
                    url : sbt.connections.searchUrls.searchApps,
                    attributes : sbt.connections.searchConstants.xpath_search
                },
                rendererArgs : {
                    type : "apps"
                }
            },
        },

        defaultOption : "all",

        createDefaultStore : function(args) {
            args.url = this._buildUrl(args.url);
            
            return new AtomStore(args);
        },

        createDefaultRenderer : function(args) {
            return new SearchGridRenderer(args);
        },
        
        getSortInfo: function() {
            return { 
                list: [{title: this.renderer.nls.name},
                       {title: this.renderer.nls.updated},
                       {title: this.renderer.nls.downloads},
                       {title: this.renderer.nls.comments},
                       {title: this.renderer.nls.likes}]
            };
        },
        
        // Internals
        _buildUrl: function(url) {
            if(this.query)
                url += "?query=" + this.query;
            return url;
        }

    });

    return sbt.controls.grid.connections.SearchGrid;
});