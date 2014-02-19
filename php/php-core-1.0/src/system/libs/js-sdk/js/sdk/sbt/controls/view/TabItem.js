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
define([ "../../declare", "../../lang", "../../stringUtil", "../../log" ], function(
		declare, lang, stringUtil, log) {

	/*
	 * @module sbt.controls.view.TabItem
	 */
	var TabItem = declare([], {
		
		href : "javascript: void(0)",
		
		selected : false,
		
		id : "",

		/**
		 * Constructor method for the TabItem.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
			
			if (args) {
				this.selected = args.selected;
				this.id = args.id;
			}
		},

		/**
		 * 
		 * @param selection
		 * @param context
		 * @returns {Boolean}
		 */
		getName : function(selection, context) {
			return this.name || "";
		},
		
		/**
		 * 
		 * @param isSelected true if the tab item is selected; false if not.
		 */
		setSelected : function(isSelected) {
			this.selected = isSelected;
		},
		
		/**
		 * 
		 * @returns {Boolean} true if the tab item is selected; false if not.
		 */
		isSelected : function() {
			return this.selected;
		},

		/**
		 * 
		 * @param selection
		 * @param context
		 * @returns {Boolean}
		 */
		getTooltip : function(selection, context) {
			return this.tooltip || "";
		},

		/**
		 * 
		 * @param selection
		 * @param context
		 * @returns {Boolean}
		 */
		isVisible : function(selection, context) {
			return true;
		},

		/**
		 * 
		 * @param selection
		 * @param context
		 * @returns {Boolean}
		 */
		isEnabled : function(selection, context) {
			return true;
		},

		/**
		 * Perform the action on the specific selection. An action may be
		 * invoked multiple times with different selections or context
		 * parameters in the same lifetime.
		 */
		execute : function(selection, context) {
		},
		
		/**
		 * Perform the action on the specific selection. An action may be
		 * invoked multiple times with different selections or context
		 * parameters in the same lifetime.
		 */
		execute : function(selection, context, element) {
		}
	});

	return TabItem;
});