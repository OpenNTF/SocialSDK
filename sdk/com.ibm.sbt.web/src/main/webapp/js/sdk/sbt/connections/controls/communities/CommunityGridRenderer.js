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
        "../../../text!../../../connections/controls/communities/templates/CommunityRow.html",
        "../../../text!../../../connections/controls/communities/templates/BootstrapCommunityRow.html",
        "../../../text!../../../connections/controls/communities/templates/TagAnchor.html",
        "../../../i18n!../../../connections/controls/communities/nls/CommunityGridRenderer"], 
        function(declare, stringUtil, i18n, lang, ConnectionsGridRenderer, CommunityRow,
        		BootstrapCommunityRow, TagAnchor, nls) {

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
          * The HTML template for tag anchors
          */
         tagAnchorTemplate: TagAnchor,
        
         /**
          * @param args
          */
         constructor: function(args,grid){
        	 if(grid.theme == "bootstrap"){
        		 this.template = BootstrapCommunityRow;
        	 }else{
        		 this.template = CommunityRow;
        	 }
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
          * Displays the restricted image for a community
          * @method displayRestricted
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current row
          * @param items  an object array containing the data for all of the grid rows
          * @returns {String}
          */
         displayRestricted: function(grid, item, i, items) {
                 var communityType = item.getValue("communityType");
             if (communityType == undefined || communityType != "publicInviteOnly") {
                 return "display: none;";
             } else {
                 return "display: inline-block;";
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
             if (tags == undefined) {
                 return "";
             } else {
                 var tagsStr = "";
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
                 return this.getProfileUrl(grid,item.getValue("authorUserid"));
         }

         
    });
    
    return CommunityGridRenderer;
});