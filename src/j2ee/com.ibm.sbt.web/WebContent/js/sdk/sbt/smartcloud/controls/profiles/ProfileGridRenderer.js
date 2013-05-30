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
 * @module sbt.smartcloud.controls.profiles.ProfileGridRenderer
 */
define(["../../../declare", "../../../lang", "../../../stringUtil", "../BaseGridRenderer",
        "../../../i18n!sbt/smartcloud/controls/profiles/nls/ProfileGridRenderer",
        "../../../text!sbt/smartcloud/controls/profiles/templates/ProfileRow.html"], 
        function(declare, lang, stringUtil, BaseGridRenderer, nls, profileTemplate) {
		
    /**
     * @class ProfileGridRenderer
     * @namespace sbt.smartcloud.controls.profiles
     */
    var ProfileGridRenderer = declare(BaseGridRenderer, {

         /**
          * The strings used in the grid, these are stored in a separate file, in the nls folder
          */
         _nls: nls,
         
         /**
          * The template used to display a row in the grid
          */
         template: profileTemplate,
         
         /**
          * The template used to construct a photo url 
          */
         contactImageUrl: "{baseUrl}/contacts/img/photos/{photo}",
         
         /**
          * The template used to construct a no photo url
          */
         noContactImageUrl: "{baseUrl}/contacts/img/noContactImage.gif",
        
         /**
          * The constructor function
          * @method constructor
          * @param args
          */
         constructor: function(args) {
         },

         /**
          * Sets the css class for the row
          * @method rowClass
          * @param grid The Grid Dijit 
          * @param item the current row
          * @param i the number of the current row, ie 0, 1, 2 etc
          * @param items all of the rows in the grid
          */
         rowClass: function(grid, item, i, items) {
             item.rowClass = (i === 0 ? "lotusFirst" : (i % 2 === 1 ? "lotusAltRow" : null));
         },
         
         /**
          * 
          * @param grid
          * @param item
          * @param i
          * @param items
          * @returns {String}
          */
         photoUrl: function(grid, item, i, items) {
        	 var ep = grid.store.getEndpoint();
        	 if (!ep) return null;
        	 
        	 var photos = item.getValue("photos");
        	 if (photos && lang.isArray(photos) && photos.length > 1) {
        		 return stringUtil.replace(this.contactImageUrl, { baseUrl : ep.baseUrl , photo : photos[1] });
        	 } else {
        		 return stringUtil.replace(this.noContactImageUrl, { baseUrl : ep.baseUrl });
        	 }
         },
         
         /**
          * 
          * @param grid
          * @param item
          * @param i
          * @param items
          * @returns {String}
          */
         primaryTelephone: function(grid, item, i, items) {
        	 var phoneNumbers = item.getValue("phoneNumbers");
        	 if (phoneNumbers && lang.isArray(phoneNumbers) && phoneNumbers.length > 0) {
        		 return phoneNumbers[0];
        	 } else {
        		 return undefined;
        	 }
         },
         
         /**
          * Displays a tooltip by calling the getTooltip function in the ProfileAction class
          * @method tooltip
          * @param grid The Grid control
          * @param item the element to display the tooltip
          * @param i the number of the current row
          * @param items all of the items in the grid row
          * @returns A Tooltip the default for profiles is to display the vCard
          */
         tooltip: function(grid, item, i, items) {
             if (grid.profileAction) {
                 return grid.profileAction.getTooltip(item);
             }
         }
         
    });
    
    return ProfileGridRenderer;
});