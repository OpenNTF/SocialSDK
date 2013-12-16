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
 * UploadFileAction
 */
define([ "sbt/declare", "sbt/dom", "sbt/lang",
         "sbt/i18n!./nls/files", "sbt/controls/dialog/Dialog", "./UploadFileWidget", "sbt/controls/view/Action"], 
	function(declare, dom, lang, nls, Dialog, UploadFileWidget, Action) {

	/**
	 * Action to upload a file.
	 * 
	 * @class UploadFileAction
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.files.UploadFileAction
	 */
	var UploadFileAction = declare([ Action ], {
		
		name : nls.uploadFile,
		
		style : "position: absolute; left: 686px; top: 98px; opacity: 1; z-index: 951;",

		/**
		 * Open dialog to upload a file.
		 */
		execute : function(selection, context) {
			var self = this;
			var widgetArgs = lang.mixin({
				hideButtons: true,
    			files: selection,
				displayMessage : function(template, isError) {
					self.displayMessage(template, isError);
				}
			}, this.widgetArgs || {});
			var widget = new UploadFileWidget(widgetArgs);
			
			var dialog = new Dialog({ 
    			title: this.name,
    			style: this.style,
    			nls: { OK: nls.labelUpload },
    			dialogContent: widget,
    			onExecute: lang.hitch(widget, widget.onExecute)
    		});
    		dialog.show();
		}
	});

	return UploadFileAction;
});