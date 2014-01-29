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
 * NewFolderAction
 */
define([ "../../../declare", "../../../dom", "../../../lang",
         "../../../i18n!./nls/files", "./NewFolderWidget", 
         "../../../controls/dialog/Dialog", "../../../controls/view/Action" ], 
	function(declare, dom, lang, nls, NewFolderWidget, Dialog, Action) {

	/**
	 * Action to create a new folder.
	 * 
	 * @class NewFolderAction
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.files.NewFolderAction
	 */
	var NewFolderAction = declare([ Action ], {
		
		name : nls.newFolder,
		
		style : "position: absolute; left: 686px; top: 98px; opacity: 1; z-index: 951;",

		/**
		 * Open dialog to create a new folder.
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
			var widget = new NewFolderWidget(widgetArgs);
			
			var dialog = new Dialog({ 
    			title: this.name,
    			style: this.style,
    			nls: { OK: nls.labelSave },
    			dialogContent: widget,
    			onExecute: lang.hitch(widget, widget.onExecute)
    		});
    		dialog.show();
		}
	});

	return NewFolderAction;
});