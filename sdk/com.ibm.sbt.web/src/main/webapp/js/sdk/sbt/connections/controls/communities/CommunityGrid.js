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
 * @module sbt.connections.controls.communities.CommunityGrid
 */
define([ "../../../declare", 
         "../../../controls/grid/Grid", 
         "./CommunityGridRenderer", 
         "./CommunityAction", 
         "../../../connections/controls/vcard/SemanticTagService",
         "../../../config",
         "../../../connections/CommunityConstants",
         "../../../store/parameter"], 
        function(declare, Grid, CommunityGridRenderer, CommunityAction, SemanticTagService, sbt, consts, parameter) {
	
	var sortVals = {
			date: "modified",
            popularity: "count",
            name: "title"
	};
	
	var ParamSchema = {	
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortField",sortVals),
		sortOrder: parameter.booleanSortOrder("asc")			
	};
	
    /**
     * @class  CommunityGrid
     * @namespace  sbt.connections.controls.communities
     */
    var CommunityGrid = declare(Grid, {
        
        contextRootMap: {
            communities: "communities"
        },
    	
    	/**
    	 * Options are for which type of community grid is to be created
    	 * public will show public communities and my will show communities
    	 * the specified user is a member or owner of. 
    	 */
        options : {
            "public" : {
                storeArgs : {
                    url : consts.AtomCommunitiesAll,
                    attributes : consts.CommunityXPath,
                    feedXPath : consts.CommunityFeedXPath,
                    paramSchema : ParamSchema
                },
                rendererArgs : null
            },
            "my" : {
                storeArgs : {
                    url : consts.AtomCommunitiesMy,
                    attributes : consts.CommunityXPath,
                    feedXPath : consts.CommunityFeedXPath,
                    paramSchema : ParamSchema
                },
                rendererArgs : null
            },
            "invited" : {
                storeArgs : {
                    url : consts.AtomCommunityInvitesMy,
                    attributes : consts.CommunityXPath,
                    feedXPath : consts.CommunityFeedXPath,
                    paramSchema : ParamSchema
                },
                rendererArgs : null
            }
        },
        
        /**
         * set the grids action to be an instance of CommunityAction
         * This means an event will be provided for when the user moves the mouse over the 
         * community name or clicks on  a community.
         */
        communityAction: new CommunityAction(),
        
        /**
         * Default grid option is to display a list of public communities.
         */
        defaultOption: "public",
            
        /**
         * Constructor function
         * @method constructor
         */
        constructor: function(args) {
            var nls = this.renderer.nls;
            
            /**
             * Set the sorting information
             */
            this._sortInfo = {
                date: { 
                    title: nls.date, 
                    sortMethod: "sortByDate",
                    sortParameter: "date"
                },
                popularity: {
                    title: nls.popularity, 
                    sortMethod: "sortByPopularity",
                    sortParameter: "popularity"
                },
                name: { 
                    title: nls.name, 
                    sortMethod: "sortByName",
                    sortParameter: "name"
                }
            };
            this._activeSortAnchor = this._sortInfo.date;
            this._activeSortIsDesc = true;
        },
        
        /**
         * Create the CommunityGrid Renderer
         * @method createDefaultRenderer
         * @param args Arguments that can be passed to the renderer
         * @returns {CommunityGridRenderer}
         */
        createDefaultRenderer : function(args) {
            return new CommunityGridRenderer(args);
        },
        
        /**
         * Function is called after the grid is created,
         * Here we call the superclass postCreate and then load the 
         * Semantic tag service, which will handle business card functionality
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Event handler for onClick Events
         * Stops the default onClick event and calls the CommunityAction execute function
         * @method handleClick
         * @param el The element that fired the event
         * @param data the data associated with the row
         * @param ev the event
         */
        handleClick: function(el, data, ev) {
            if (this.communityAction) {
            	this._stopEvent(ev);
                
                this.communityAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        /**
         * Filter the community by specified tag
         * @param el
         * @param data
         * @param ev
         */
        filterByTag: function(el, data, ev){
        	this._stopEvent(ev);
            
        	var options = {
            	tag: el.text
            };

        	this._filter(options,data);
        },
        
        /**
         * Gets sorting information, such as
         * if the results are ascending or descending, and the sort anchors
         * @method getSortInfo
         * @returns An object containing sorting information
         */
        getSortInfo: function() {
            return {
                active: {
                    anchor: this._activeSortAnchor,
                    isDesc: this._activeSortIsDesc
                },
                list: [this._sortInfo.date, this._sortInfo.popularity, this._sortInfo.name]
            };
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
        
        /**
         * Sort the grid rows by popularity
         * @method sortByPopularity
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByPopularity: function(el, data, ev) {
            this._sort("popularity", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by title
         * @method sortByTitle
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByName: function(el, data, ev) {
            this._sort("name", false, el, data, ev);
        }
        
    });

    return CommunityGrid;
});
