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

define([ "../../../declare", "../../../controls/grid/GridAction","../../../i18n!./nls/ForumGridRenderer"], 
        function(declare, GridAction, nls) {

    /**
     * @class BackAction
     * @namespace sbt.connections.controls.forum
     * @module sbt.connections.controls.forum.BackAction
     */
    var BackAction = declare(GridAction, {
       
    	
        /**ForumAction Constructor function
         * @method constructor
         * */
        constructor: function(args) {

        },
        
        showForums: function(item, grid, event){
        	var options = {
                start: grid.data.start, count: grid.pageSize
            }; 
        	grid.getForums(options);
        },
        showTopics: function(item, grid, event){
        	var options = {
                start: grid.data.start, count: grid.pageSize
            }; 
        	grid.getTopics("",options);
        }

    });

    return BackAction;
});