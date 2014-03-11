/*
 * © Copyright IBM Corp. 2014
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
 * LockTopicWidget
 */
define([ "../../../declare", "../../../lang", "../../../dom", "../../../stringUtil", 
         "../../../i18n!./nls/ForumView", 
         "../../../controls/view/BaseDialogContent", "../../ForumService", 
		 "../../../text!./templates/LockTopic.html"],
		function(declare, lang, dom, stringUtil, nls, BaseDialogContent, ForumService, LockTopic) {

	/**
	 * Widget which can be used to lock topics from a forum
	 * 
	 * @class LockTopicWidget
	 * @namespace sbt.connections.controls.forums
	 * @module sbt.connections.controls.forums.LockTopicWidget
	 */
	var LockTopicWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : LockTopic,

		/**
		 * Constructor method for the StartTopicWidget.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			this.nls = lang.mixin({}, nls, this.nls);
			lang.mixin(this, args);
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
		 * Called when the execute button is clicked.
		 * 
		 * @method onExecute
		 */
		onExecute : function() {
			this._lockTopics(this.topics);
		},
		
		
		//
		// Internals
		//
		
		_lockTopics: function(topics){
			this.setExecuteEnabled(false);
			var forumService = this.getForumService();
				
			var isLocked = this._isTopicLocked(topics[0]);
			topics[0] = forumService.newForumTopic(topics[0]);
			if(!isLocked){
				topics[0].setLocked(true);
			}else {
				topics[0].setLocked(false);
			}
				var self = this;
				topics[0].update().then(               
				    function(topic){
				    	self._handleRequestComplete(isLocked);
				    },
				    function(error){
				    	self._handleError(error);
				    }
				);

			this.setExecuteEnabled(true);
		},
		
		_isTopicLocked: function(topic){
			var forumService = this.getForumService();
			var isLocked;
			topic = forumService.newForumTopic(topic);
			isLocked = topic.isLocked();
			return isLocked;
		},
		
		/*
		 * Called after a request has completed 
		 */
		_handleRequestComplete : function(isLocked) {
			this._setSuccessMessage(isLocked);
			this.onSuccess();			
		},
		
		/*
		 * Called If the request returns an error
		 */
		_handleError: function(error){
			this.setExecuteEnabled(true);
			this.errorTemplate = "<div>"+nls.LockTopicError+"</div>";	
			this.onError();
		},
		
		/*
		 * Set the successMessage for the specified add tags operation
		 */
		_setSuccessMessage : function(isLocked) {
			if(isLocked){
				this.successTemplate = "<div>"+nls.unlockTopicSuccess+"</div>";
			}else{
				this.successTemplate = "<div>"+nls.LockTopicSuccess+"</div>";
			}
			
		}
	});

	return LockTopicWidget;
});