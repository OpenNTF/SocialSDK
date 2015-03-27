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

define(["../../../declare", "../../../localeUtil", 
        "../../../connections/controls/ConnectionsGridRenderer",
        "../../../text!../../../connections/controls/bookmarks/templates/BookmarkRow.html",
        "../../../text!../../../connections/controls/bookmarks/templates/BootstrapBookmarkRow.html",
        "../../../text!../../../connections/controls/bookmarks/templates/TagAnchor.html",
        "../../../i18n!../../../connections/controls/bookmarks/nls/BookmarkGridRenderer",
        "../../../lang",
        "../../../text!../../../connections/controls/bookmarks/templates/BookmarkListItem.html"], 
        function(declare, localeUtil,  ConnectionsGridRenderer, BookmarkRow,BootstrapBookmarkRow,
        		TagAnchor, nls, lang, BookmarkListItem) {

                /**
                * @class BookmarkGridRenderer
            * @namespace sbt.connections.controls.bookmarks
            * @module sbt.connections.controls.bookmarks.BookmarkGridRenderer
            * */
            var BookmarkGridRenderer = declare(ConnectionsGridRenderer,{
                    
                _nls: nls,
                
                /**
                 * The Constructor function
                 * @method constructor
                 * @param args
                 */
                constructor: function(args,grid){
                	if(grid.theme == "bootstrap"){
                		this.template = BootstrapBookmarkRow;
                	}else{
                		if (args && args.containerType) {
	                        if(args.containerType == "ol" || args.containerType == "ul"){
	                            this.template = BookmarkListItem;
	                        }else if(args.containerType == "table"){
	                            this.template = BookmarkRow;
	                        }
                        }else{
                            this.template = BookmarkRow;
                        }
                        
                	}
                	
                	this.tagAnchorTemplate = TagAnchor;  
                },
                        
                         /**
                  * Displays a tooltip by calling the getTooltip function in the bookmarkAction.
                  * @method tooltip
                  * @param grid The Grid element
                  * @param item the element to display the tooltip
                  * @param i the number of the current row
                  * @param items all of the items in the grid row
                  * @returns A String used as a tooltip
                  */
                 tooltip: function(grid, item, i, items) {
                        return grid.bookmarkAction.getTooltip(item);

                 },
                 
                 /**
                  * Gets the last updated date in human readable format. 
                  * @param grid The Grid Element
                  * @param item An Object containing all of the data for the current row
                  * @param i the number of the current grid row
                  * @param items an object array containing the data for all of the grid rows
                  * @returns The date when the community was last updates
                  */
                 updatedDate: function(grid, item, i, items) {
                         return localeUtil.getSearchUpdatedLabel((item.getValue("updated")));
                 },
                 
                 /**
                  * Returns a CSS style based on if a community has tags. 
                  * @param grid The Grid Element
                  * @param item An Object containing all of the data for the current row
                  * @param i The number of the current row
                  * @param items  an object array containing the data for all of the grid rows
                  * @returns {String}
                  */
                 displayTags: function(grid, item, i, items) {
                     var tags = item.getValue("tags");
                     if (tags.length == 0) {
                         return "display: none";
                     } else {
                         return "";
                     }
                 },
                 
                 /**
                  * Get the tag label, if a community has no tags and empty string is returned
                  * @param grid The Grid Element
                  * @param item An Object containing all of the data for the current row
                  * @param i The number of the current row
                  * @param items  an object array containing the data for all of the grid rows
                  * @returns an array of strings, that are tags for a community
                  */
                 tagsLabel: function(grid, item, i, items) {
                         var tags = item.getValue("tags");
                     if (tags.length == 0) {
                         return "";
                     } else {
                         return this._nls.tags;
                     }
                 },
                        /**
                  * Substitutes tag labels(Strings) from the nls file into the tag
                  * Anchor template which creates a tag links, and displays these tags on the page 
                  * @param grid The Grid Element
                  * @param item An Object containing all of the data for the current row
                  * @param i The number of the current row
                  * @param items  an object array containing the data for all of the grid rows
                  * @returns {String}
                  */
                 tagsAnchors: function(grid, item, i, items) {
                     var tags = item.getValue("tags");
                     if (tags == undefined) {
                         return "";
                     } else {
                         var tagsStr="";
                         if (lang.isArray(tags)) {
                             for (var i=0; i<tags.length; i++) {
                                 tagsStr += grid._substitute(this.tagAnchorTemplate, { tagName : tags[i] });
                                 if (i+1 < tags.length) {
                                     tagsStr += ", ";
                                 }
                             }
                         } else if (lang.isString(tags)) {
                                 tagsStr = grid._substitute(this.tagAnchorTemplate, { tagName : tags });
                         }
                         return tagsStr;
                     }
                 },
                 
                 getUserProfileHref: function(grid,item,i,items){
                         return this.getProfileUrl(grid,item.getValue("authorId"));
                 }
                        
                });
                
                
        
                return BookmarkGridRenderer;
});