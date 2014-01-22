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
 * UploadFileAction
 */
define([ "../../../declare", "../../../dom", "../../../lang",
         "../../../i18n!./nls/ForumView", "./StartTopicWidget", 
         "../../../controls/dialog/Dialog", "../../../controls/view/Action"], 
	function(declare, dom, lang, nls, StartTopicWidget, Dialog, Action) {

	/**
	 * Action to start a new forum topic
	 * 
	 * @class StartTopicAction
	 * @namespace sbt.connections.controls.forums
	 * @module sbt.connections.controls.forums.StartTopicAction
	 */
	var StartTopicAction = declare([ Action ], {
		
		name : nls.startTopic,
	
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
		 * Open dialog to upload a file.
		 */
		execute : function(selection, context) {
			var self = this;
			var widgetArgs = lang.mixin({
				hideButtons: false,
				view:self.view,
				action: self,
				forumUuid: this.grid.forumUuid,
				displayMessage : function(template, isError) {
					self.displayMessage(template, isError);
				}
			}, this.widgetArgs || {});
			this.widget = new StartTopicWidget(widgetArgs);
			this.view.setContent(this.widget);
			this.view.actionBar.hideAction(this);		
		}
	});

	return StartTopicAction;
});