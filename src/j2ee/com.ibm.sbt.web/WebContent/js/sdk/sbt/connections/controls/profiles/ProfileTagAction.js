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

/**
 * 
 */
define([ "../../../declare", "../../../controls/grid/GridAction" ], 
        function(declare, GridAction) {

    /**
     * @class ProfileTagAction
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileTagAction
     */
    var ProfileTagAction = declare(GridAction, {
        
    	/**Strings used in the actions */
        nls: {
            tooltip: "",
        },
        
        /**ProfileTagAction Constructor function
         * @method constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * For profiles, the tooltip by default will be a business card
         * So nothing is done in this function
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	return item.getTerm();
        },
        
        /**
         * The execute function is called from the handle click function
         * For Profiles by default the business card functionality is used
         * which works from the Semantic tag service so nothing is done here.
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, opts, event) {
        }

    });

    return ProfileTagAction;
});