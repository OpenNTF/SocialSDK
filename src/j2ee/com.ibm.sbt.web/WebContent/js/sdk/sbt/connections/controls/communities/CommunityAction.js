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
 * @module sbt.connections.controls.communities.CommunityAction
 */
define([ "../../../declare", "../../../controls/grid/GridAction", "../../../stringUtil" ], 
        function(declare, GridAction, stringUtil) {

    /**
     * 
     * @class CommunityAction
     * @namespace sbt.connections.controls.communities
     */
    var CommunityAction = declare(GridAction, {
        
        nls: {
            tooltip: "Go to ${title}",
        },
       
        /**
         * CommunityGridAction Constructor
         * @method  constructor
         */
        constructor: function() {
        },
        
        /**
         * @method getTooltip
         * @param item The element that the tooltip will display for
         * @returns A String to be displayed in an elements tooltip 
         */
        getTooltip: function(item) {
           return stringUtil.replace(this.nls.tooltip, { title : item.getValue("title") });
        },
        
        /**
         * When an event is fired, this is the action that will get called
         * from the hanleClick function
         * @method  execute
         * @param item  The element that fired the event and called this function 
         * @param opts
         * @param event  the Event
         */
        execute: function(item, opts, event) {
           window.open(item.getValue("communityUrl"));
        }

    });

    return CommunityAction;
});