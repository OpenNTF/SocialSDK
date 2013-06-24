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
         "../../../config", 
         "../../../controls/grid/Grid", 
         "./SearchGridRenderer", 
         "../../../store/AtomStore",
         "../../../store/parameter"], 
        function(declare, sbt, Grid, SearchGridRenderer, AtomStore, parameter) {

	// TODO use values from constants and handle authType
	var searchUrls = {
	    searchPeople: "/search/atom/search/facets/people",
        searchTags: "/search/atom/search/facets/tags",
        searchApps: "/search/atom/search/facets/source",
        searchAll: "/search/atom/mysearch/results"
	};
	
    var xpathMap = {
        "id" : "a:id",
        "title" : "a:title",
        "summary" : "a:summary",
        "updated" : "a:updated",
        "relevance" : "relevance:score",
        "authorName" : "a:author/a:name",
        "authorUid" : "a:author/snx:userid",
        "authorEmail" : "a:author/a:email",
        "authorState" : "a:author/snx:userState",
        "type" : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
        "application" : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term",
        "applicationCount" : "count(a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term)",
        "primaryComponent" : "a:category[ibmsc:field[@id='primaryComponent']]/@term",
        "tags" : "a:category[not(@scheme)]/@term",
        "commentCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
        "resultLink" : "a:link[not(@rel)]/@href",
        "bookmarkLink" : "ibmsc:field[@id='dogearURL']",
        "eventStartDate" : "ibmsc:field[@id='eventStartDate']",
        "authorJobTitle" : "a:content/xhtml:div/xhtml:span/xhtml:div[@class='title']",
        "authorJobLocation" : "a:content/xhtml:div/xhtml:span/xhtml:div[@class='location']",
        "authorCount" : "count(a:contributor)",
        "contributorCount" : "count(a:author)",
        "tagCount" : "count(a:category[not(@scheme)])",
        "highlightField" : "ibmsc:field[@id='highlight']",
        "fileExtension" : "ibmsc:field[@id='fileExtension']",
        "memberCount" : "snx:membercount",
        "communityUuid" : "snx:communityUuid",
        "containerType" : "ibmsc:field[@id='container_type']",
        "communityParentLink" : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container' and @type='text/html']/@href",
        "parentageMetaID" : "ibmsc:field[contains(@id, 'ID')]/@id",
        "parentageMetaURL" : "ibmsc:field[contains(@id, 'URL')]",
        "parentageMetaURLID" : "ibmsc:field[contains(@id, 'URL')]/@id",
        "objectRefDisplayName" : "ibmsc:field[@id='FIELD_OBJECT_REF_DISPLAY_NAME']",
        "objectRefUrl" : "ibmsc:field[@id='FIELD_OBJECT_REF_URL']",
        "accessControl" : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/accesscontrolled']/@term",
        "commentsSummary" : "ibmsc:field[@id='commentsSummary']"
    };
    
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
                    url : searchUrls.searchAll,
                    attributes : xpathMap,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "all"
                }
            },
            
            "people" : {
                storeArgs : {
                    url : searchUrls.searchPeople,
                    attributes : xpathMap,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "people"
                }
            },
            
            "tags" : {
                storeArgs : {
                    url : searchUrls.searchTags,
                    attributes : xpathMap
                },
                rendererArgs : {
                    type : "tags"
                }
            },
            
            "apps" : {
                storeArgs : {
                    url : searchUrls.searchApps,
                    attributes : xpathMap,
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

        createDefaultStore : function(args) {
            args.url = this._buildUrl(args.url);
            
            return new AtomStore(args);
        },

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
        
        sortByRelevance: function(el, data, ev){
            this._sort("relevance", true, el, data, ev);
        },

        /**
         * Sort the grid rows by last modified date
         * @method sortByLastModified
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByDate: function(el, data, ev) {
            this._sort("date", true, el, data, ev);
        },
        
        // Internals
        _buildUrl: function(url) {
            if(this.query){
                url += "?query=" + this.query;
                var type = this.type;
                if(type !== undefined){
                    if(type.indexOf("bookmark") === 0 || type.indexOf("dogear") == 0)
                        url += "&component=dogear";
                    else if(type.indexOf("status") === 0)
                        url += "&component=status_updates";
                    else if(type !== "all")
                        url += "&component=" + this.type;
                }

            }
            return url;
        }

    });

    return searchGrid;
});