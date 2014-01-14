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
 * ShareFileAction
 */
define([ "../../../declare", "../../../dom", "../../../lang",
         "../../../i18n!sbt/connections/controls/files/nls/files",
         "../../../controls/dialog/Dialog", "./ShareFileWidget", "../../../controls/view/Action"], 
	function(declare, dom, lang, nls, Dialog, ShareFileWidget, Action) {

	/**
	 * Action to share a file.
	 * 
	 * @class ShareFileAction
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.files.ShareFileAction
	 */
	var ShareFileAction = declare([ Action ], {
		
		name : nls.share,

		/**
		 * Only enabled when at least on file is selected.
		 */
		isEnabled : function(selection, context) {
			return (selection.length > 0);
		},
		
		/**
		 * Set files on the associated widget. 
		 */
		selectionChanged : function(state, selection, context) {
			this.inherited(arguments);
			
			if (this.widget) {
				this.widget.selectionChanged(selection, context);
			}
		},

		/**
		 * Open dialog to share a file.
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
			var widget = new ShareFileWidget(widgetArgs);
			
			var dialogArgs = lang.mixin({ 
    			title: this.name,
    			dialogStyle : "width: 600px;",
    			nls: { OK: nls.share },
    			dialogContent: widget,
    			onExecute: lang.hitch(widget, widget.onExecute)
    		},this.dialogArgs || {});
			var dialog = new Dialog(dialogArgs);
    		dialog.show();
		}
	});

	return ShareFileAction;
});