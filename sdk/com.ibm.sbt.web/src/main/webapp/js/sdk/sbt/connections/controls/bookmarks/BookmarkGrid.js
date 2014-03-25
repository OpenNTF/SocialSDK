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
define(["../../../declare",
        "../../../controls/grid/Grid",
        "../../../store/parameter",
        "../../../connections/BookmarkConstants",
        "../../../connections/CommunityConstants",
        "./BookmarkGridRenderer", "sbt/base/URLBuilder" ], 
    function(declare,Grid,parameter,consts,communityConstants,BookmarkGridRenderer, URLBuilder) {
	
	/**
	 * Sorting values
	 */
	var sortVals = {
			date: "created",
            popularity: "popularity"
	};
	
	/**
	 * Sorting and paging parameters
	 */
	var ParamSchema = {	
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")			
	};
	
	/**
	 * @class BookmarkGrid
     * @namespace sbt.connections.controls.bookmarks
     * @module sbt.connections.controls.bookmarks.BookmarkGrid
	 */
	var BookmarkGrid = declare(Grid,{
		
		/**
		 * Specifies how the bookmark should open, new tab, same window etc.
		 * Should match values for the name parameter from the window.open function
		 * _blank is default, 
		 * _parent - URL is loaded into the parent frame
		 * _self - URL replaces the current page
		 * _top - URL replaces any framesets that may be loaded
		 */
		targetName: "_blank",
		
        builder : new URLBuilder(),
        
		 options : {
	            "any" : {
	                storeArgs : {
	                    url : consts.AtomBookmarksAll,
	                    attributes : consts.BookmarkXPath,
	                    feedXPath : consts.BookmarkFeedXPath,
	                    paramSchema : ParamSchema
	                },
	                rendererArgs : null
	            },
	            "community": {
	                storeArgs : {
                        url : communityConstants.AtomCommunityBookmarks,
                        attributes : consts.BookmarkXPath,
                        feedXPath : consts.BookmarkFeedXPath,
                        paramSchema : ParamSchema
                    },
                    rendererArgs : null
	            }
	     },
	        
	     /**
	      * Default grid option.
	      */
	     defaultOption: "any", 
	     
	     /**
	      * The grid constructor function
	      * @method constructor
	      * @param args
	      */
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
                }
            },
            this._activeSortAnchor = this._sortInfo.date;
            this._activeSortIsDesc = true;
	     }, 
	     
	     /**
	      * Used to add parameters to the URL
	      * @method buildUrl
	      * @param url the url to add parameters to
	      * @param args
	      * @param endpoint An endpoint which may contain custom service mappings.
	      * @returns the url with parameters 
	      */
	     buildUrl: function(url, args, endpoint) {
	    	 var urlParams;
	         url = this.builder.build(url, this.endpoint.apiVersion, {
	           authentication : this.endpoint.authType === "oauth" ? "oauth":""
		     });
	    	 if(this.type == "private"){
	    		 urlParams = { access: "private"};
	    	 }else if(this.type == "public") {
	    		 urlParams = {access: "public"};
	    	 }else if(this.type == "community"){
	    	     urlParams = {communityUuid: this.communityUuid};
	    	 }else{
	    		 urlParams = {access: "any"};
	    	 }

	          return this.constructUrl(url, urlParams, {}, endpoint);
	        },
	     
        /**
         * Creates a Renderer for this grid.
         * @param args
         * @returns {BookmarkGridRenderer}
         */
	     createDefaultRenderer: function(args){
	    	 return new BookmarkGridRenderer(args,this);
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
                list: [this._sortInfo.date, this._sortInfo.popularity]
            };
        },
	     
	    /**
         * Filter the bookmarks by specified tag
         * @method filterByTag
         * @param el The Grid Dom node
         * @param data The Grid's data
         * @param ev The Event
         */
        filterByTag: function(el, data, ev){
        	this._stopEvent(ev);
            
        	var options = {
            	tag: el.text
            };

        	this._filter(options,data);
        },
        
        /**
         * Sorts the grid by date of creation.
         * @method sortByDate
         * @param el The Grid dom Element
         * @param data the Grid's data
         * @param ev the event
         */
        sortByDate: function(el, data, ev){
        	this._sort("date", true, el, data, ev);
        },
        
        /**
         * Sort the Grid by popularity
         * @method sortByPopularity
         * @param el The grid element
         * @param data the Grids data
         * @param ev The event
         */
        sortByPopularity: function(el, data, ev){
        	this._sort("popularity", true, el, data, ev);
        },
        
        /**
         * Event Handler for onClick events
         * @method handleClick
         * @param el the grid element
         * @param data the grids data
         * @param ev the event
         */
        handleClick: function(el, data, ev){
        	
        	this._stopEvent(ev);
        	this.bookmarkAction.execute(data,this);
        },

        /**
         * Default Action for handling click events and returning tooltip text.
         */
        bookmarkAction: {
        	
        	/**
        	 * Returns a string to be used in a tooltip or title. 
        	 * @method getTooltip 
        	 * @param item
        	 * @returns A String to be used in a tooltip. 
        	 */
        	getTooltip: function(item){
        		return item.getValue("title");
        	},
        	
        	/**
        	 * The code is executed from an onClick event
        	 * @method execute
        	 * @param data the Data associated with the grid
        	 */
        	execute: function(data,context){
        		var url = data.getValue("url");
        		window.open(url,context.targetName);
        	}
        }
	
	});
	
	
	return BookmarkGrid;
	
});