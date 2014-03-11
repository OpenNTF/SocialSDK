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
		 "../../../text!./templates/PinTopic.html"],
		function(declare, lang, dom, stringUtil, nls, BaseDialogContent, ForumService, PinTopic) {

	/**
	 * Widget which can be used to pin topics from a forum
	 * 
	 * @class PinTopicWidget
	 * @namespace sbt.connections.controls.forums
	 * @module sbt.connections.controls.forums.PinTopicWidget
	 */
	var PinTopicWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : PinTopic,
		

		
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
			this._pinTopics(this.topics[0]);
		},
		
		
		//
		// Internals
		//
		
		_pinTopics: function(topic){
			this.setExecuteEnabled(false);
			var forumService = this.getForumService();		
			topic = forumService.newForumTopic(topic);
			var isPinned = this._isTopicPinned(topic);
			if(!isPinned){
				topic.setPinned(true);
			}else{
				topic.setPinned(false);
			}
			
			var self = this;
			topic.update().then(               
			    function(topic){
			    	self._handleRequestComplete(isPinned);
			    },
			    function(error){
			    	self._handleError(error);
			    }
			);
			this.setExecuteEnabled(true);
		},
		
		_isTopicPinned: function(topic){
			var forumService = this.getForumService();
			var isPinned;
			
			topic = forumService.newForumTopic(topic);
			isPinned = topic.isPinned();
			
			return isPinned;
		},
		
		/*
		 * Called after a request has completed 
		 */
		_handleRequestComplete : function(isPinned) {	
			this._setSuccessMessage(isPinned);

			 this.onSuccess();
			//this.view.actionBar.selection.length = 0;
			//this.view.grid.refreshSelectionListeners();
		},
		
		/*
		 * Called If the request returns an error
		 */
		_handleError: function(error){
			this.setExecuteEnabled(true);
			this.errorTemplate = "<div>"+nls.pinTopicError+"</div>";	
			this.onError();
		},
		
		/*
		 * Set the successMessage for the specified add tags operation
		 */
		_setSuccessMessage : function(isPinned) {
			if(!isPinned){
				this.successTemplate = "<div>"+nls.pinTopicSuccess+"</div>";
			}else{
				this.successTemplate = "<div>"+nls.unpinTopicSuccess+"</div>";
			}
			
		}
	});

	return PinTopicWidget;
});