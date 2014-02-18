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
 *  @module sbt.controls.grid.GridAction
 */
define(["../../declare"], function(declare) {

    /**
     * @class sbt.controls.grid.GridAction
     * @namespace sbt.controls.grid
     */
    var GridAction = declare(null, {
         
    	 /**
    	  * Grid Action Constructor function
    	  * @constructor
    	  */
         constructor: function() {
         },
         
         /**
          * Gets the string to be displayed as an elements tooltip
          * @method getTooltip
          * @param item the HTML element 
          * @returns {String} contains the text for the tooltip
          */
         getTooltip: function(item) {
            return "sbt.controls.GridAction No tooltip specified";
         },
         
         /** 
          * Default action for the grid
          * @method execute
          * @param item The element that fired the event
          * @param opts
          * @param event the Event, for example onClick
          */
         execute: function(item, opts, event) {
            dojo.stopEvent(event);
         },
         
         /**
          * Action to view the user's profile in connections. 
          * When the user clicks on a link to a person's profile
          * the HREF will have the URL and redirect the user to the person's
          * profile, if this action needs to be changed it can be overridden using this function
          *@method viewUserProfile 
          */
         viewUserProfile: function(){
        	 //do nothing by default
         }

    });
    
    return GridAction;
});