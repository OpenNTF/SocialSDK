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
define([ "../../declare", "../../lang",
         "../../i18n!./nls/dialog",
         "../../widget/_TemplatedDialog",
         "../../text!./templates/Dialog.html" ], 
        function(declare, lang, nls, _TemplatedDialog, DialogTemplate) {

    /**
     * @class Dialog
     * @namespace sbt.controls.dialog
     * @module sbt.controls.dialog.Dialog
     */
    var Dialog = declare([ _TemplatedDialog ], {
    	
    	dialogStyle : "width: 525px;",
    	
    	templateString : DialogTemplate,
    	
    	buttonClass : "lotusFormButton",
    	
        /**
         * Constructor method for the grid.
         * Creates a default store and renderer, if none have been already created
         * @method constructor
         * @param args
         */
        constructor: function(args) {
			this.nls = lang.mixin({}, nls, args.nls || {});
			if (args.nls) {
				delete args.nls;
			}
			
            lang.mixin(this, args || {});
        },
        
        /**
         * Construct the UI for this dialog from a template, setting this.domNode.
         * 
         * @method buildRendering
         */
		buildRendering: function() {
			this.inherited(arguments);
			
			// create the dialog content
			if (this.dialogContent) {
				var widget = this.dialogContent;
				if (lang.isString(widget)) {
					widget = new _TemplatedWidget({
						templateString : widget
					});
				}
				widget.dialog = this;
				
				this.widget = widget;
				this.contentNode.appendChild(widget.domNode);
			}
			
			// optionally hide the execute and Cancel buttons
			if (this.hideExecute && this.executeButton) {
				this.executeButton.style.display = "none";
			}
			if (this.hideCancel && this.cancelButton) {
				this.cancelButton.style.display = "none";
			}
		},
		
		/**
		 * Add a new button to the dialog
		 * 
		 * @method addButton
		 * @param label
		 * @param style
		 * @param callback
		 */
		addButton: function(label, style, callback) {
            var button = dom.create("input", {
                type: "button",
            	"class": this.buttonClass,
            	"style": style || "",
                role: "button",
                value: label || ""
            }, this.buttonsNode);
            if (callback) {
            	button.onclick = callback;
            }
            this.buttonsNode.appendChild(button);
            return button;
		}
    });
    
    return Dialog;
});
