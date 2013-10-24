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
define([ "../../../declare",
         "../../../controls/grid/Grid",
         "../../../store/parameter",
         "./ActivityGridRenderer",
         "./ActivityAction",
         "../../../connections/ActivityConstants"], 

function(declare, Grid, parameter, ActivityGridRenderer, ActivityAction, consts) {
	
	/**Sorting Values*/
	var sortVals = {
			modified: "lastmod",
			dueDate: "duedate",
			name: "title"
	};
	
	/**URL parameters */
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortfields",sortVals),
		sortOrder: parameter.sortOrder("sortorder")						
	};
	
	/**
     * @class ActivityGrid
     * @namespace  sbt.connections.controls.activities
     * @module sbt.connections.controls.activities.ActivityGrid
     */
	var ActivityGrid = declare(Grid,{

		options : {
	            "my" : {
	                storeArgs : {
	                    url : consts.AtomActivitiesMy,
	                    attributes : consts.ActivityNodeXPath,
	                    feedXPath : consts.ActivitiesFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "my"
	                }
	            }
		 },
		
		 /**The default type to use if none is specified */
		defaultOption: "my", 
		
		/**class to handle on click and tooltip actions  */
		activityAction : new ActivityAction(),
		
		/**
		 * ActivitiesGrid Constructor
		 * @method constructor
		 * @param args
		 */
		constructor: function(args){
			/**
             * Set the sorting information
             */
            this._sortInfo = {
                modified: { 
                    title: this.renderer._nls.modified, 
                    sortMethod: "sortBylastModified",
                    sortParameter: "date"
                },
                dueDate: {
                    title: this.renderer._nls.dueDate, 
                    sortMethod: "sortByDueDate",
                    sortParameter: "dueDate"
                },
                name: { 
                    title: this.renderer._nls.name, 
                    sortMethod: "sortByName",
                    sortParameter: "name"
                }
            };
            this._activeSortAnchor = this._sortInfo.name;
            this._activeSortIsDesc = true;
		},
		
		/**
		 * Returns Sorting info
		 * @method getSortInfo
		 * @returns {#an object containing sorting information}
		 */
		getSortInfo : function() {
			return {
				active : {
					anchor : this._activeSortAnchor,
					isDesc : this._activeSortIsDesc
				},
				list : [ this._sortInfo.modified, this._sortInfo.dueDate,
						this._sortInfo.name ]
			};
		},
		
		/**
		 * Sort the activities by last modification date
		 * @method sortByLastModified
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		sortBylastModified: function(el, data, ev){
			this._sort("modified", true, el, data, ev);
		},
		
		/**
		 * Sort the activities by Due Date
		 * @method sortByDueDate
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		sortByDueDate: function(el, data, ev){
			this._sort("dueDate", true, el, data, ev);
		},
		
		/**
		 * Sort the activities by name
		 * @method sortByName
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		sortByName: function(el, data, ev){
			this._sort("name", true, el, data, ev);
		},
		
		/**
		 * Event handler function for onClick events 
		 * @method handleClick
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		handleClick: function(el, data, ev) {
	        if (this.activityAction) {
	           this._stopEvent(ev);
	           this.activityAction.execute(data, this , ev);
	        }
	    },
		 
		 /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type
         * @returns an instance of an ActivitiesGridRenderer.
         */
        createDefaultRenderer : function(args) {
            return new ActivityGridRenderer(args);
        },
				
	});
	
	return ActivityGrid;
});