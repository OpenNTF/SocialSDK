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
define([ "../../declare", "../../lang", "../../stringUtil", "../../log",
        "./_ActionContainer",
		"../../text!../../controls/view/templates/ActionBar.html",
		"../../text!../../controls/view/templates/ActionBarButton.html" ],
		function(declare, lang, stringUtil, log, _ActionContainer, ActionBarTmpl, ActionTmpl) {

	/*
	 * @module sbt.controls.view.ActionBar
	 */
	var ActionBar = declare([ _ActionContainer ], {

		/**
		 * 
		 */
		templateString : ActionBarTmpl,

		/**
		 * 
		 */
		actionTemplate : ActionTmpl,
		
		/**
		 * 
		 */
		disabledClass : "lotusBtnDisabled",

		/**
		 * Constructor method for the ActionBar.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
		},
		
		/**
		 * 
		 * @param selection
		 * @param context
		 */
		selectionChanged : function(selection, context) {
			this.selection = selection || [];
			this.context = context || {};
			for (var i=0; i < this._actionElements.length; i++) {
				this._updateActionState(this._actionElements[i]);
			}
		},
		
		/**
		 * @method addAction
		 * @param action
		 * @param addToBeginning
		 */
		addAction : function(action, addToBeginning) {
			var element = this._renderElement(this.domNode, action, addToBeginning);
			if (addToBeginning) {
				this._place(element, this.domNode, "first");
			} else {
				this._place(element, this.domNode);
			}
		},

		//
		// Internals
		//

		_onKeyUp : function(event) {
			// if (event && event.keyCode == dojo.keys.SPACE) {
			// this._onClick(event);
			// }
		},

		_executeAction : function(action, element) {
			try {
				action.execute(this.selection, this.context);
			} catch(err) {
				log.error(err);
			}
		}

	});

	return ActionBar;
//	return lang.mixin(ActionBar, _ActionContainer);
});