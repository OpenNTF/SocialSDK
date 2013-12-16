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
define([ "sbt/declare", "sbt/dom", "sbt/lang",
         "sbt/i18n!sbt/connections/controls/files/nls/files",
         "sbt/controls/dialog/Dialog", "./ShareFileWidget", "sbt/controls/view/Action"], 
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
			
			var dialog = new Dialog({ 
    			title: this.name,
    			grid: this.grid,
    			dialogStyle : "width: 600px;",
    			nls: { OK: nls.share },
    			dialogContent: widget,
    			onExecute: lang.hitch(widget, widget.onExecute)
    		});
    		dialog.show();
		}
	});

	return ShareFileAction;
});