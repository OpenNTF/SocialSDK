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
define(["../../../declare",
        "../ConnectionsGridRenderer",
        "../../../i18n!sbt/connections/controls/profiles/nls/ProfileGridRenderer",
        "../../../text!sbt/connections/controls/profiles/templates/ProfileRow.html",
        "../../../text!sbt/connections/controls/profiles/templates/SharedConnectionsRow.html",
        "../../../text!sbt/connections/controls/profiles/templates/StatusUpdateRow.html"], 
        function(declare, ConnectionsGridRenderer, nls, profileTemplate, sharedConnTemplate, statusUpdateTemplate) {
		
    /**
     * @class ProfileGridRenderer
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileGridRenderer
     */
    var ProfileGridRenderer = declare(ConnectionsGridRenderer, {

          /**The strings used in the grid, these are stored in a separate file, in the nls folder*/
         _nls: nls, 
        
         /**
          * The constructor function
          * @method constructor
          * @param args
          */
         constructor: function(args) {
             if (args.type == "profile") {
                 this.template = profileTemplate;
             }else if(args.type == "statusUpdates"){
            	 this.template = statusUpdateTemplate;
             }
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
          * Displays a tooltip by calling the getTooltip function in the ProfileAction class
          * @method tooltip
          * @param grid The Grid Dijit
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