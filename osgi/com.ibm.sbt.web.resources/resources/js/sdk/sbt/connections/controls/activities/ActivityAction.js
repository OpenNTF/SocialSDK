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
 
dojo.provide("sbt.connections.controls.activities.ActivityAction");

define([ "../../../declare", "../../../controls/grid/GridAction"], 
        function(declare, GridAction) {

    /**
     * @class ActivityAction
     * @namespace sbt.connections.controls.activities
     * @module sbt.connections.controls.activities.ActivityAction
     */
    var ActivityGridAction = declare(GridAction, {
       
    	
        /**ForumAction Constructor function
         * @method constructor
         * @param args
         */
        constructor: function(args) {

        },
        
        /**
         * Handles displaying a tooltip for an item
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	 return item.getValue("title");
        },
        
        /**
         * The execute function is called from the handle click function
         * 
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, grid, event) {
        	document.location.href = item.getValue("historyUrl");
        }


    });

    return ActivityGridAction;
});