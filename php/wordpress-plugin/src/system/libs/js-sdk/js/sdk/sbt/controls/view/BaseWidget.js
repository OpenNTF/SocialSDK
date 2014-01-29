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
define([ "../../declare", "../../lang", "../../dom", "../../widget/_TemplatedWidget" ],
		function(declare, lang, dom, _TemplatedWidget) {

	/**
	 * Widget which can be used to upload files.
	 * 
	 * @class BaseWidget
	 * @namespace sbt.controls.view.filesx
	 * @module sbt.controls.view.forums.BaseWidget
	 */
	var BaseWidget = declare([ _TemplatedWidget ], {
		
		/**
		 * Class that will be added remove when enabling disabling save button.
		 */
		buttonDisabledClass : "lotusBtnDisabled",
		
		/**
		 * Constructor method for the ForumSection.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			this.nls = lang.mixin({}, nls, this.nls);

			lang.mixin(this, args);
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
		 * Called when an error occurs.
		 * 
		 * @method onError
		 * @param error
		 */
		onError : function(error) {
		},
		
		/**
		 * Called after successfully performing an operation.
		 * 
		 * @method onSuccess
		 * @param forum
		 */
		onSuccess : function(forum, followed) {
		},
		
		/**
		 * Called when the save button is clicked.
		 * 
		 * @method onSave
		 */
		onSave : function() {
		},
		
		/**
		 * Called when the cancel button is clicked.
		 * 
		 * @method onCancel
		 */
		onCancel : function() {
		}
		
	});

	return BaseWidget;
});