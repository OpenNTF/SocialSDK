/*
 * © Copyright IBM Corp. 2013
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
 * NewFolderWidget
 */
define([ "../../../declare", "../../../lang", "../../../dom", 
         "../../../i18n!./nls/files", "../../../controls/view/BaseDialogContent", "../../FileService", 
		 "../../../text!./templates/NewFolder.html" ],
		function(declare, lang, dom, nls, BaseDialogContent, FileService, NewFolder) {

	/**
	 * Widget which can be used to create a new folder.
	 * 
	 * @class NewFolderWidget
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.forums.NewFolderWidget
	 */
	var NewFolderWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : NewFolder,
		
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
		getFileService : function() {
			if (!this.fileService) {
				var args = this.endpoint ? { endpoint : this.endpoint } : {};
				this.fileService = new FileService(args);
			}
			return this.fileService;
		},
		
		/**
		 * Called after properties have been set
		 */
		postMixInProperties: function() {
			this.errorTemplate = nls.newFolderError;
			this.successTemplate = nls.newFolderSuccess;
		},
		
		/**
		 * Post create function is called after section has been created.
		 * 
		 * @method postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
		},

		/**
		 * Called when the execute button is clicked.
		 * 
		 * @method onExecute
		 */
		onExecute : function() {
			var visibility = "private";
			if (this.publicInput.checked) {
				visibility = "public";
			}
			var name = this.nameInput.value || "";
			var description = this.descriptionInput.value || "";
			this._createFolder(name, description, visibility);
		},

		//
		// Internals
		//
			
		/*
		 * Create a new folder
		 */
		_createFolder : function(name, description, visibility) {
			var self = this;
			this.setExecuteEnabled(false);
			var promise = this.getFileService().createFolder(name, description);
			promise.then(
	        	function(folder) {
	            	self.folder = folder;
	        		self.setExecuteEnabled(true);
            		self.onSuccess(self.folder);
	        	},
	            function(error) {
	            	self.setExecuteEnabled(true);
	            	self.onError(error);
	            }
	        );
		}
		
	});

	return NewFolderWidget;
});