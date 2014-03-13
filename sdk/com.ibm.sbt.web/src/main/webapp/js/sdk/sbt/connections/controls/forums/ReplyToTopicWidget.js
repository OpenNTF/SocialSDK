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
 * ReplyToTopicWidget
 */
define([ "../../../declare", "../../../lang", "../../../dom", "../../../stringUtil", 
         "../../../i18n!./nls/ForumView", 
         "../../../controls/view/BaseDialogContent", "../../ForumService", 
		 "../../../text!./templates/ReplyToTopic.html"],
		function(declare, lang, dom, stringUtil, nls, BaseDialogContent, ForumService, ReplyToTopic) {

	/**
	 * Widget to reply to a forum topic.
	 * 
	 * @class ReplyToTopicWidget
	 * @namespace sbt.connections.controls.forums
	 * @module sbt.connections.controls.forums.ReplyToTopicWidget
	 */
	var ReplyToTopicWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : ReplyToTopic,
		
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
		 * Post create function is called after section has been created.
		 * 
		 * @method postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
			if(window['CKEDITOR'] != undefined){
				CKEDITOR.replace(this.replyEditor);
			}
		},
		
		/**
		 * Called when the execute button is clicked.
		 * 
		 * @method onExecute
		 */
		onExecute : function() {
		
			this.setExecuteEnabled(false);	
			var content="";
			if(window['CKEDITOR'] != undefined){
				content = CKEDITOR.instances.editor.getData();
			}			
		    
			this._replyToTopic( content);
		    this.setExecuteEnabled(true);	
		},

		//
		// Internals
		//
		
		_replyToTopic: function(content){
			var forumService = this.getForumService();
			
			var errorCount = 0;			
			for(var i=0; i<this.topics.length;i++){
				var reply = forumService.newForumReply(); 
			    reply.setTopicUuid(this.topics[i].getValue("topicUuid"));
			    reply.setTitle(this.topics[i].getValue("title"));
			    reply.setContent(content);
			    var self = this;
			    forumService.createForumReply(reply).then(  
			        function(reply) { 
			        	self._handleRequestComplete(reply,errorCount,i);
			        },
			        function(error) {
			        	errorCount ++;
			        	self._handleError(error);
			        }
			    );
			}
		},
		
		/*
		 * Called after a request has completed 
		 */
		_handleRequestComplete : function(success,errorCount,count) {
			//if for loop is finished and no errors
			if(count == this.topics.length && errorCount == 0){
				this.setExecuteEnabled(true);
				this._setSuccessMessage(success);
				this.onSuccess();
			}

		},
		
		/*
		 * Called If the request returns an error
		 */
		_handleError: function(error){
			this.setExecuteEnabled(true);
			this._setErrorMessage(error);
			this.onError();			
		},
		
		/*
		 * Set the successMessage for the specified add tags operation
		 */
		_setSuccessMessage : function(success) {
			if(this.topics>1){
				this.successTemplate = "<div>"+nls.replySuccess+"</div>";
			}else{
				this.successTemplate = "<div>"+nls.replyMultipleSuccess+"</div>";
			}
			
		},
		
		/*
		 * Set the errorMessage for the specified add tags operation
		 */
		_setErrorMessage : function(error) {
			this.errorTemplate = "<div>"+nls.replyError+"</div>";	
		}

	});

	return ReplyToTopicWidget;
});