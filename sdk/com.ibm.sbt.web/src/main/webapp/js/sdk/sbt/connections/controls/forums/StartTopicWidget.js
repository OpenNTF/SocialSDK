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
 * StartTopicWidget
 */
define([ "../../../declare", "../../../lang", "../../../dom", "../../../stringUtil", 
         "../../../i18n!./nls/ForumView", 
         "../../../controls/view/BaseDialogContent", "../../ForumService", 
		 "../../../text!./templates/StartATopic.html"],
		function(declare, lang, dom, stringUtil, nls, BaseDialogContent, ForumService, StartATopic) {

	/**
	 * Widget which can be used to add tags to a collection of files.
	 * 
	 * @class StartTopicWidget
	 * @namespace sbt.connections.controls.forums
	 * @module sbt.connections.controls.forums.StartTopicWidget
	 */
	var StartTopicWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : StartATopic,
		
		/**
		 * Constructor method for the UploadFileWidget.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			this.nls = lang.mixin({}, nls, this.nls);
			lang.mixin(this, args);
		},

		/**
		 * Return the FileService.
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
				CKEDITOR.replace(this.editor);
			}
		},
		
		/**
		 * Called when the execute button is clicked.
		 * 
		 * @method onExecute
		 */
		onExecute : function() {
			
			this.setExecuteEnabled(false);
			var title = this.titleInput.value;
			if(!title || title.trim()==""){
				this._setErrorMessage(nls.titleError);
				this.onError();
				this.setExecuteEnabled(true);
				return;
			}
			var tags = this.tagsInput.value;
			var question = this.questionCheckBox.checked;
			
			var content ;
			if(window['CKEDITOR'] != undefined){
				content = CKEDITOR.instances.editor.getData();
			}else{
				content = "";
			}
			
			var forumService = this.getForumService();
			var topic = forumService.newForumTopic(); 
		    topic.setForumUuid(this.forumUuid);
		    topic.setTitle(title);
		    topic.setContent(content);
		    topic.setTags(tags);
		    topic.setQuestion(question);
		    var self = this;
		    forumService.createForumTopic(topic).then(  
		        function(success) { 
		            self._handleRequestComplete(success);
		        },
		        function(error) {
		        	self._handleError(error);
		        }
		    );
			
		},
		
		onCancel : function() {
			this.view.actionBar.showAction(this.action);
			this.view.setContent(this.view.grid);
			
		},
		
		//
		// Internals
		//
		
		/*
		 * Called after a request has completed 
		 */
		_handleRequestComplete : function(success) {
			
			this.setExecuteEnabled(true);
			this._setSuccessMessage(success);
			this.view.actionBar.showAction(this.action);
			this.view.grid.update(null);
			this.onSuccess();
			this.view.setContent(this.view.grid);
			
		},
		
		/*
		 * Called If the request returns an error
		 */
		_handleError: function(error){
			this.setExecuteEnabled(true);
			this._setErrorMessage(error);
			this.view.actionBar.showAction(this.action);
			this.onError();
			this.view.setContent(this.view.grid);
		},
		
		/*
		 * Set the successMessage for the specified add tags operation
		 */
		_setSuccessMessage : function(success) {
			this.successTemplate = "<div>"+nls.topicSuccess+"</div>";
		},
		
		/*
		 * Set the errorMessage for the specified add tags operation
		 */
		_setErrorMessage : function(error) {
			this.errorTemplate = "<div>"+nls.topicError+"</div>";	
		}

	});

	return StartTopicWidget;
});