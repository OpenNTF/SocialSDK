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

define(["../../../declare",
        "../ConnectionsGridRenderer",
        "../../../localeUtil",
        "../../../text!./templates/ActivityRow.html",
        "../../../text!./templates/BootstrapActivityRow.html",
        "../../../i18n!./nls/ActivityGridRenderer"], 

    function(declare, ConnectionsGridRenderer, localeUtil, ActivityRow, BootstrapActivityRow, nls){
		
		/**
		 * @class ActivityGridRenderer
		 * @namespace sbt.connections.controls.activities
		 * @module sbt.connections.controls.forum.ActivityGridRenderer
		 */
	    var ActivityGridRenderer = declare(ConnectionsGridRenderer,{
	    	
	    	/**Strings used by the forum grid */
	    	_nls:nls,
  	
	    	/**
	    	 * The constructor function
	    	 * @method constructor
	    	 * @param args
	    	 */
	    	constructor: function(args,grid){
	    		
	    		if(grid.theme == "bootstrap"){
	    			this.template = BootstrapActivityRow;
	    		}else{
	    			this.template = ActivityRow;
	    		}
	    	},

	        /**
	          * Displays a tooltip by calling the getTooltip function in the ForumAction class
	          * @method tooltip
	          * @param grid The Grid element
	          * @param item the element to display the tooltip
	          * @param i the number of the current row
	          * @param items all of the items in the grid row
	          * @returns A String used as a tooltip
	          */
	         tooltip: function(grid, item, i, items) {
	             if (grid.activitiesAction) {
	                 return grid.activitiesAction.getTooltip(item);
	             }
	         },
	         
	         getUserProfileHref: function(grid,item,i,items){
	        	 return this.getProfileUrl(grid,item.getValue("authorUserId"));
	         },
	         
	         /**
	          * Gets the last updated date for an activity
	          * @param grid The Grid Element
	          * @param item An Object containing all of the data for the current row
	          * @param i the number of the current grid row
	          * @param items an object array containing the data for all of the grid rows
	          * @returns The date when the community was last updates
	          */
	         updatedDate: function(grid, item, i, items) {
	        	 	var updatedLabel = item.getValue("updated");
	        	 	if(updatedLabel.length > 0){
	        	 		 return localeUtil.getUpdatedLabel(updatedLabel);
	        	 	}else{
	        	 		return "";
	        	 	}  
	         },
	         
	         /**
	          * Gets the due date for an activity
	          * @param grid The Grid Element
	          * @param item An Object containing all of the data for the current row
	          * @param i the number of the current grid row
	          * @param items an object array containing the data for all of the grid rows
	          * @returns The date when the community was last updates
	          */
	         getDueDate: function(grid, item, i, items) {
	        	 var dueDate = item.getValue("dueDate");
	        	 	if(dueDate.length > 0){
	        	 		 return localeUtil.getUpdatedLabel(dueDate);
	        	 	}else{
	        	 		return "";
	        	 	}
	         }
	    	
	    });
	
	return ActivityGridRenderer;
});