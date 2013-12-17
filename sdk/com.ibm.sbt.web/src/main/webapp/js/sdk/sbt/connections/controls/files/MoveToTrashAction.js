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
 * MoveToTrashAction
 */
define([ "sbt/declare", "sbt/dom", "sbt/lang",
         "sbt/i18n!./nls/files", "sbt/controls/dialog/Dialog", "./MoveToTrashWidget", "sbt/controls/view/Action" ], 
	function(declare, dom, lang, nls, Dialog, MoveToTrashWidget, Action) {

	/**
	 * Action to move a file to the trash
	 * 
	 * @class MoveToTrashAction
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.files.MoveToTrashAction
	 */
	var MoveToTrashAction = declare([ Action ], {
		
		name : nls.moveToTrash,
	
		/**
		 * Only enabled when at least on file is selected.
		 */
		isEnabled : function(selection, context) {
			return (selection.length > 0);
		},

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
			this.widget = new MoveToTrashWidget(widgetArgs);
			
			var dialog = new Dialog({ 
    			title: this.name,
    			nls: { OK: nls.moveToTrash },
    			dialogContent: this.widget,
    			onExecute: lang.hitch(this.widget, this.widget.onExecute)
    		});
    		dialog.show();
		}
	});

	return MoveToTrashAction;
});