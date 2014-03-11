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
         "../../../controls/dialog/Dialog", "../../../controls/view/Action",
         "../../ForumService"], 
	function(declare, dom, lang, nls, PinTopicWidget, Dialog, Action, ForumService) {

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
			
			if(selection.length > 0){
				var forumService = this.getForumService();
				var topic = forumService.newForumTopic(selection[0]);
				var isPinned = topic.isPinned();
				if(isPinned){
					this.actionNameNode.textContent = nls.unpinTopic;
					this.name = nls.unpinTopic;
				}else {
					this.actionNameNode.textContent = nls.pinTopic;
					this.name = nls.pinTopic;		
				}
			}
		},
		
		/**
		 * Return the ForumService.
		 */
		getForumService : function() {
			if (!this.forumService) {
				var args = this.endpoint ? { endpoint : this.endpoint } : {};
				this.forumService = new ForumService(args);
			}
			return this.forumService;
		},
		
		/**
		 * Only enabled when at least one topic is selected.
		 */
		isEnabled : function(selection, context) {
			return (selection.length > 0 && selection.length < 2);
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
    			nls: { OK: nls.ok },
    			dialogContent: widget,
    			onExecute: lang.hitch(widget, widget.onExecute)
    		});
    		dialog.show();
		}
	});

	return PinTopicAction;
});