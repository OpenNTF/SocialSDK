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
define([ "sbt/_bridge/declare", 
         "sbt/controls/grid/Grid", 
         "sbt/controls/grid/connections/CommunityGridRenderer", 
         "sbt/controls/grid/connections/CommunityAction", 
         "sbt/controls/vcard/connections/SemanticTagService",
         "sbt/config",
         "sbt/store/parameter",
         "sbt/connections/CommunityConstants"], 
        function(declare, Grid, CommunityGridRenderer, CommunityAction, SemanticTagService, sbt, parameter, communityConstants) {

    /**
     * @class  CommunityGrid
     * @namespace  sbt.controls.grid.connections
     * @module sbt.controls.grid.connections.CommunityGrid
     */
    declare("sbt.controls.grid.connections.CommunityGrid", Grid, {
    	
    	/**
    	 * Options are for which type of community grid is to be created
    	 */
        options : {
            "public" : {
                storeArgs : {
                    url : sbt.connections.communitiesUrls.allCommunities,
                    attributes : communityConstants.xpath_community,
                    feedXPath: communityConstants.xpath_feed_community,
                    paramSchema: parameter.communities.all
                },
                rendererArgs : null
            },
            "my" : {
                storeArgs : {
                   url : sbt.connections.communitiesUrls.myCommunities,
                    attributes : sbt.connections.communityConstants.xpath_community,
                    paramSchema: parameter.communities.all
                },
                rendererArgs : null
            }
        },
        
        /**Instantiate Actions for Community Grids*/
        communityAction: new CommunityAction(),
        
        /**The default grid to be created if an option is not specified*/
        defaultOption: "public",
            
        /**Constructor function
         * @method - constructor
         * */
        constructor: function(args){
        	
            var nls = this.renderer.nls;
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

            this._activeSortAnchor = this._sortInfo.name;
            this._activeSortIsDesc = true;
        },
        
        /**
         * Create the CommunityGrid Renderer
         * @method - createDefaultRenderer
         * @param args - Arguments that can be passed to the renderer
         * @returns {CommunityGridRenderer}
         */
        createDefaultRenderer : function(args) {
            return new CommunityGridRenderer(args);
        },
        
        /**
         * Function is called after the grid is created,
         * Here we call the superclass postCreate and then load the 
         * Semantic tag service, which if for business cared functionality
         * @method - postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Event handler for onClick Events
         * @method - handleClick
         * @param el - The element that fired the event
         * @param data - the data associated with the element
         * @param ev - the event
         */
        handleClick: function(el, data, ev) {
            if (this.communityAction) {
                dojo.stopEvent(ev);
                
                this.communityAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        
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
         * @method - getSortInfo
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
         * @method - sortByLastModified
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByDate: function(el, data, ev) {
            this._sort("date", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by popularity
         * @method - sortByPopularity
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByPopularity: function(el, data, ev) {
            this._sort("popularity", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by title
         * @method - sortByTitle
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByName: function(el, data, ev) {
            this._sort("name", false, el, data, ev);
        }
    });

    return sbt.controls.grid.connections.CommunityGrid;
});