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

define([ "../../../declare", "../../../controls/grid/GridAction","../../../i18n!./nls/ForumGridRenderer","../../../stringUtil"], 
        function(declare, GridAction, nls, stringUtil) {

    /**
     * @class ForumAction
     * @namespace sbt.connections.controls.forum
     * @module sbt.connections.controls.forum.forumAction
     */
    var ForumAction = declare(GridAction, {
        
    	nls: {
            tooltip: "Go to {title}"
        },
       
    	
        /**ForumAction Constructor function
         * @method constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	 return stringUtil.replace(this.nls.tooltip, { title : item.getValue("title") });
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
        
        	var startOfId = item.getValue("id").lastIndexOf(":")+1;
        	var id = item.getValue("id").substring(startOfId,item.getValue("id").length);
        	
        	var options = {
                start: grid.data.start, count: grid.pageSize
            };
        	
        	if(grid.renderer.template == grid.renderer.topicTemplate){
        		grid.getTopicReplies(id,options);      		
        	}else{
            	grid.getTopics(id,options);
        	}
	
        }

    });

    return ForumAction;
});
