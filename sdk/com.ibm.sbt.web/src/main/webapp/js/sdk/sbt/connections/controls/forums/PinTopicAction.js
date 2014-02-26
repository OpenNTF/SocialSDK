/*
 * Copyright IBM Corp. 2014
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
 * PinTopicAction
 */
define([ "../../../declare", "../../../dom", "../../../lang",
         "../../../i18n!./nls/ForumView", "./PinTopicWidget", 
         "../../../controls/dialog/Dialog", "../../../controls/view/Action"], 
	function(declare, dom, lang, nls, PinTopicWidget, Dialog, Action) {

	/**
	 * Action to pin a forum topic
	 * 
	 * @class PinTopicAction
	 * @namespace sbt.connections.controls.forums
	 * @module PinTopicAction
	 */
	var PinTopicAction = declare([ Action ], {
		
		name : nls.pinTopic,
	
		/**
		 * Set topics on the associated widget. 
		 */
		selectionChanged : function(state, selection, context) {
			this.inherited(arguments);
			
			if (this.widget) {
				this.widget.selectionChanged(selection, context);
			}
		},
		
		/**
		 * Only enabled when at least one topic is selected.
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
    			topics: selection,
    			view: this.view,
				displayMessage : function(template, isError) {
					self.displayMessage(template, isError);
				}
			}, this.widgetArgs || {});
			var widget = new PinTopicWidget(widgetArgs);
			
			//TODO Change this to use the new pattern when the latest code is pulled
			var dialog = new Dialog({ 
    			title: this.name,
    			nls: { OK: nls.pin },
    			dialogContent: widget,
    			onExecute: lang.hitch(widget, widget.onExecute)
    		});
    		dialog.show();
		}
	});

	return PinTopicAction;
});