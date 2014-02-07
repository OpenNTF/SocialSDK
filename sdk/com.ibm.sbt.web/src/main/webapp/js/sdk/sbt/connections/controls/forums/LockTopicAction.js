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
 * LockTopicAction
 */
define([ "../../../declare", "../../../dom", "../../../lang",
         "../../../i18n!./nls/ForumView", 
         "../../../controls/view/Action",
         "../../../connections/ForumService"], 
	function(declare, dom, lang, nls, Action,ForumService) {

	/**
	 * Action to lock a forum topic
	 * 
	 * @class LockTopicAction
	 * @namespace sbt.connections.controls.forums
	 * @module sbt.connections.controls.forums.LockTopicAction
	 */
	var LockTopicAction = declare([ Action ], {
		
		name : nls.lockTopic,
	
		/**
		 * Set forums on the associated widget. 
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
		 * Open the forum to create a new topic
		 */
		execute : function(selection, context) {
			
			var forumService = this.getForumService();
			
			for(var i=0;i<selection.length;i++){
				selection[i] = forumService.newForumTopic(selection[i]);
				
				var isLocked = selection[i].isLocked();
				console.log(isLocked);
				
				selection[i].setLocked(true);
		        var promise = forumService.updateForumTopic(selection[i]);
		        
		        promise.then(function(response) {
		           alert("successfully locked");
		        }, function(error) {
		            alert("error setting lock");
		        });
		 
			}
		}
	});

	return LockTopicAction;
});