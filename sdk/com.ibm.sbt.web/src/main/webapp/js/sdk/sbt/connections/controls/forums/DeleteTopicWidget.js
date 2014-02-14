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
 * DeleteTopicWidget
 */
define([ "../../../declare", "../../../lang", "../../../dom", "../../../stringUtil", 
         "../../../i18n!./nls/ForumView", 
         "../../../controls/view/BaseDialogContent", "../../ForumService", 
		 "../../../text!./templates/DeleteTopic.html"],
		function(declare, lang, dom, stringUtil, nls, BaseDialogContent, ForumService, DeleteTopic) {

	/**
	 * Widget which can be used to delete topics from a forum
	 * 
	 * @class DeleteTopicWidget
	 * @namespace sbt.connections.controls.forums
	 * @module sbt.connections.controls.forums.DeleteTopicWidget
	 */
	var DeleteTopicWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : DeleteTopic,
		
		/**
		 * String to check if the user is sure they want to delete a topic
		 * is set in postMixinProperties depending if more than one file is selected
		 */
		warningMessage: "",
		
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
		 * Called after properties have been set
		 */
		postMixInProperties: function(){
			this.setWarningMessage();
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
			this._deleteTopics(this.topics);
		},
		
		/**
		 * Sets the message to warn the user about deleting topics
		 * check if the user has selected one or more topics and sets the message based on that
		 */
		setWarningMessage: function(){
			if(this.topics.length > 1){
				this.warningMessage = nls.deleteMultipleCheck;
			}else{
				this.warningMessage = nls.deleteCheck;
			}
		},
		
		//
		// Internals
		//
		
		_deleteTopics: function(topics){
			this.setExecuteEnabled(false);
			var forumService = this.getForumService();
			var errorCount = 0;
			deletedCount = 0;
			var self = this;
			
			for(var i=0;i<topics.length;i++){
				topics[i] = forumService.newForumTopic(topics[i]);
				forumService.deleteForumTopic(topics[i].getTopicUuid()).then(
						function(deletedtopicId) {
							deletedCount ++;
							self._handleRequestComplete(errorCount,deletedCount,topics.length);
						}, 
						function(error) {
							errorCount++;
							self._handleRequestComplete(errorCount,deletedCount,topics.length);
						}
					);
			}
			this.setExecuteEnabled(true);
		},
		
		
		/*
		 * Called after a request has completed 
		 */
		_handleRequestComplete : function(errorCount, deletedCount, topicsLength) {
			
			//If all the requests have completed.
			if(deletedCount + errorCount == topicsLength){
				if(errorCount > 0){
					this._handleError(error);
				}else{
					this.setExecuteEnabled(true);
					this._setSuccessMessage(topicsLength);
					if(this.view){
						this.view.grid.update(null);
					}
					this.onSuccess();
				}
			}
			
		},
		
		/*
		 * Called If the request returns an error
		 */
		_handleError: function(error){
			this.setExecuteEnabled(true);
			this.errorTemplate = "<div>"+nls.deleteTopicError+"</div>";	
			this.onError();
		},
		
		/*
		 * Set the successMessage for the specified add tags operation
		 */
		_setSuccessMessage : function(topicsLength) {
			if(topicsLength > 1 ){
				this.successTemplate = "<div>"+nls.deleteTopicsSuccess+"</div>";
			}else{
				this.successTemplate = "<div>"+nls.deleteTopicSuccess+"</div>";
			}
			
		}
	});

	return DeleteTopicWidget;
});