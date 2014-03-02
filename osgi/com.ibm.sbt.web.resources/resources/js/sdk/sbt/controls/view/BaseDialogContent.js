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

dojo.provide("sbt.controls.view.BaseDialogContent");

/**
 * 
 */
define([ "../../declare", "../../lang", "../../dom", "../../stringUtil", "../../widget/_TemplatedWidget" ],
		function(declare, lang, dom, stringUtil, _TemplatedWidget ) {

	/**
	 * Widget which can be embedded inside a dialog.
	 * 
	 * @class BaseDialogContent
	 * @namespace sbt.controls.view
	 * @module sbt.controls.view.forums.BaseDialogContent
	 */
	var BaseDialogContent = declare([ _TemplatedWidget ], {
		
		/**
		 * Template used to display the dialog content.
		 */
		templateString : null,
		
		/**
		 * Template for widget to be displayed when an error occurs.
		 */
		errorTemplate : null,
		
		/**
		 * Template for widget to be displayed for successful execution.
		 */
		successTemplate : null,
		
		/**
		 * Class that will be added remove when enabling disabling a button.
		 */
		buttonDisabledClass : "lotusBtnDisabled",
		
		/**
		 * Post create function is called after section has been created.
		 * 
		 * @method postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
        	
        	if (this.hideButtons && this.buttonsNode) {
        		this.buttonsNode.style.display = "none";
        	}         	
		},

		/**
		 * Selection changed function is called after the selection has changed.
		 *  
		 * @method selectionChanged
		 * @param selection
		 * @param context
		 */
		selectionChanged : function(selection, context) {
		},
		
		/**
		 * Used to show or hide the buttons used to create or cancel.
		 * 
		 * @method setButtonsVisible
		 * @param visible
		 */
		setButtonsVisible : function(visible) {
			if (this.buttonsNode) {
        		this.buttonsNode.style.display = visible ? "" : "none";
        	}
		},
		
		/**
		 * Used to enable or disable the button used to execute the dialog action.
		 * 
		 * @method setExecuteEnabled
		 * @param enabled
		 */
		setExecuteEnabled : function(enabled) {
			var executeButton = this.executeButton || null;
			if (this.dialog && this.dialog.executeButton) {
				executeButton = this.dialog.executeButton;
			}
			if (executeButton) {
				if (enabled) {
					dom.removeClass(executeButton, this.buttonDisabledClass);
					executeButton.disabled = false;
				} else {
					dom.addClass(executeButton, this.buttonDisabledClass);
					executeButton.disabled = true;
				}
			}
		},
		
		/**
		 * Called when an error occurs.
		 * 
		 * @method showError
		 * @param error
		 */
		onError : function(error) {
			var template = this.errorTemplate;
			if (error) {
				template = stringUtil.transform(template, error, function(v,k) { return v; }, error);
			}
			this.displayMessage(template, true);
		},
		
		/**
		 * Called after successfully saving or creating a forum.
		 * 
		 * @method showSuccess
		 * @param forum
		 */
		onSuccess : function(result) {
			var template = this.successTemplate;
			if (result) {
				template = stringUtil.transform(template, result, function(v,k) { return v; }, result);
			}
			this.displayMessage(template, false);
		},
		
		/**
		 * Called when the execute button is clicked.
		 * 
		 * @method onUpload
		 */
		onExecute : function() {
		},
		
		/**
		 * Called when the cancel button is clicked.
		 * 
		 * @method onCancel
		 */
		onCancel : function() {
		},
		
		/**
		 * Called to display a message.
		 * 
		 * @method displayMessage
		 */
		displayMessage : function(template, isError) {
		}
		
	});

	return BaseDialogContent;
});