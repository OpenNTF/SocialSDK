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
define([ "../../../declare", "../../../stringUtil", "../../../controls/grid/GridAction" ], 
        function(declare, stringUtil, GridAction) {

    /**
     * @class FileAction
     * @namespace sbt.connections.controls.files
     * @module sbt.connections.controls.files.FileAction
     */
    var FileAction = declare(GridAction, {
        
    	/**Strings*/
        nls: {
            tooltip: "Click to download ${title}",
        },
       
        /**Constructor function
         * @method constructor
         *  */
        constructor: function() {
        },
        
        /**function to get the string to be displayed in an elements tooltip
         * @method getTooltip
         * @param item The element for which the tolltip will be displayed
         * @return A String, with the text to be displayed in the elements tooltip
         * */
        getTooltip: function(item) {
        	return stringUtil.replace(this.nls.tooltip, { title : item.getValue("title") });
        },
        
        /**
         * Execute function provides the default action for files
         * This function is called from the handle click function.
         * @method execute
         * @param item the element that fired the event
         * @param opts
         * @param event the event
         */
        execute: function(item, opts, event) {
        	window.open(item.getValue("fileUrl"));
        }

    });

    return FileAction;
});