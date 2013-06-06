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
 * @module sbt.connections.controls.communities.CommunityGridRenderer
 */
define(["../../../declare", "../../../stringUtil", "../../../i18n", "../../../lang",
        "../../../connections/controls/ConnectionsGridRenderer",
        "../../../text!sbt/connections/controls/communities/templates/CommunityRow.html",
        "../../../text!sbt/connections/controls/communities/templates/TagAnchor.html",
        "../../../i18n!sbt/connections/controls/communities/nls/CommunityGridRenderer"], 
        function(declare, stringUtil, i18n, lang, ConnectionsGridRenderer, CommunityRow, TagAnchor, nls) {

    /**
     * @class CommunityGridRenderer
     * @namespace sbt.connections.controls.communities
     */
    var CommunityGridRenderer = declare(ConnectionsGridRenderer, {
    	
    	 /**
    	  * Strings used in the grid
    	  */
         _nls: nls,
         
         /**
          * The HTML template to be used for this grid
          */
         template: CommunityRow,
         
         /**
          * The HTML template for tag anchors
          */
         tagAnchorTemplate: TagAnchor,
        
         /**
          * @param args
          */
         constructor: function(args) {
         },
         
         /**
          * Sets the class for the Current grid row
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns A CSS Class
          */
         rowClass: function(grid, item, i, items) {
             return (i === 0 ? "placeRow lotusFirst" : "placeRow");
         },
         
         /**
          * Handles Displaying a Tooltip for an item
          * @param grid The grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns A String, with the text to be displayed in the tooltip
          */
         tooltip: function(grid, item, i, items) {
             if (grid.communityAction) {
                 return grid.communityAction.getTooltip(item);
             }
         },
         
         /**
          * Handles encoding the image url
          * @param grid The grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns The image url
          */
         communityImage: function(grid, item, i, items) {
        	 var logoUrl = item.getValue("logoUrl");
             return grid.encodeImageUrl(logoUrl);
         },
         
         /**
          * Returns the number of members for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the Number of the current grid
          * @param items an object array containing the data for all of the grid rows
          * @returns A String with the number of members in a community
          */
         numOfMembers: function(grid, item, i, items) {
             var memberCount = item.getValue("memberCount");
             if (memberCount == 1) {
                 return this.nls.person;
             } else {
                 return stringUtil.replace(this.nls.people, { "memberCount" : memberCount });
             }
         },
         
         /**
          * Gets the last updated date for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns The date when the community was last updates
          */
         updatedDate: function(grid, item, i, items) {
        	 return i18n.getUpdatedLabel((item.getValue("updated")));
         },
         
         /**
          * Displays the tags for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current row
          * @param items  an object array containing the data for all of the grid rows
          * @returns {String}
          */
         displayTags: function(grid, item, i, items) {
        	 var tags = item.getValue("tags");
             if (tags == undefined || lang.isString(tags)) {
                 return "display: none";
             } else {
                 return "";
             }
         },
         
         /**
          * Get the tags for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current row
          * @param items  an object array containing the data for all of the grid rows
          * @returns an array of strings, that are tags for a community
          */
         tagsLabel: function(grid, item, i, items) {
        	 var tags = item.getValue("tags");
             if (tags == undefined || lang.isString(tags)) {
                 return "";
             } else {
                 return this.nls.tags;
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
             if (tags == undefined || lang.isString(tags)) {
                 return "";
             } else {
                 var tagsStr = "";
                 if (lang.isArray(tags)) {
                     for (var i=1; i<tags.length; i++) {
                         tagsStr += this._substitute(this.tagAnchorTemplate, { tagName : tags[i] });
                         if (i+1 < tags.length) {
                             tagsStr += ", ";
                         }
                     }
                 } 
                 return tagsStr;
             }
         },
         
         // Internals
         
         _substitute : function(template, item) {
             var self = this;
             return stringUtil.transform(template, item, function(value, key) {
                 if (typeof value == "undefined") {
                     // check the renderer for the property
                     value = this._getObject(key, false, self);
                 }

                 if (typeof value == "undefined") {
                     value = "ERROR:" + key;
                 }

                 if (value == null) {
                     return "";
                 }

                 return value;
             }, this);
         }

    });
    
    return CommunityGridRenderer;
});