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
    	
    	dialogStyle : "",
    	
    	templateString : DialogTemplate,

        /**
         * Constructor method for the grid.
         * Creates a default store and renderer, if none have been already created
         * @method constructor
         * @param args
         */
        constructor: function(args) {
        	this.nls = lang.mixin({}, nls, args.nls);
        	if (args.nls) {
        		delete args.nls;
        	}
        	
            lang.mixin(this, args);
        },
        
        /**
         * Construct the UI for this dialog from a template, setting this.domNode.
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
				
				this.widget = widget;
				this.contentNode.appendChild(widget.domNode);
			}
		}
    });
    
    return Dialog;
});
