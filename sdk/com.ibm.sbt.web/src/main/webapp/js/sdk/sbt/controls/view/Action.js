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
	 * @module sbt.controls.view.Action
	 */
	var Action = declare([], {
		
		href : "javascript: void(0)",

		/**
		 * Constructor method for the Action.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
		},

		/**
		 * 
		 * @method selectionChanged
		 * @param state
		 * @param selection
		 * @param context
		 * @returns {Boolean}
		 */
		selectionChanged : function(state, selection, context) {
			state.name = this.getName(selection, context);
			state.tooltip = this.getTooltip(selection, context);
			state.visible = !!this.isVisible(selection, context);
			state.enabled = !!this.isEnabled(selection, context);
		},
		
		/**
		 * 
		 * @method getName
		 * @param selection
		 * @param context
		 * @returns {String}
		 */
		getName : function(selection, context) {
			return this.name || "";
		},

		/**
		 * 
		 * @method getTooltip
		 * @param selection
		 * @param context
		 * @returns {String}
		 */
		getTooltip : function(selection, context) {
			return this.tooltip || "";
		},

		/**
		 * 
		 * @method isVisible
		 * @param selection
		 * @param context
		 * @returns {Boolean}
		 */
		isVisible : function(selection, context) {
			return true;
		},

		/**
		 * 
		 * @method isEnabled
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
		},
		
		/**
		 * @method displayMessage
		 */
		displayMessage : function(template, isError) {
			if (this.view) {
				this.view.displayMessage(template, isError);
			}
		}
		
	});

	return Action;
});