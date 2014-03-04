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
 
dojo.provide("sbt.connections.controls.files.MoveToTrashWidget");

/**
 * MoveToTrashWidget
 */
define('sbt/connections/controls/files/MoveToTrashWidget',[ "../../../declare", "../../../lang", "../../../dom", 
         "../../../i18n!./nls/files", "../../../controls/view/BaseDialogContent", "../../FileService", 
		 "../../../text!./templates/MoveToTrash.html",
		 "../../../stringUtil"],
		function(declare, lang, dom, nls, BaseDialogContent, FileService, MoveToTrash,stringUtil) {

	/**
	 * Widget which can be used to move a collection of files to trash.
	 * 
	 * @class MoveToTrashWidget
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.forums.MoveToTrashWidget
	 */
	var MoveToTrashWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : MoveToTrash,
		
		/**
		 * Computed message displayed in the move to trash dialog.
		 */
		moveToTrashMessage : "",
		
		/**
		 * name of file(s) to be displayed
		 */
		fileName: "fileName",
		
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
			this.errorMessage = nls.moveToTrashError;
			this.successMessage = nls.moveToTrashSuccess;
			this._setMoveToTrashMessage(this.files);
			this._getSelectedFileNames(this.files);
		},
				
		/**
		 * Post create function is called after section has been created.
		 * 
		 * @method postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
			
			if (!this.files || !this.files.length) {
				this.setExecuteEnabled(false);
			} 
		},

		/**
		 * Called when the execute button is clicked.
		 * 
		 * @method onExecute
		 */
		onExecute : function() {
			this._moveToTrash(this.files);	
		},
		
	
		//
		// Internals
		//
			
		/*
		 * Create a new folder
		 */
		_moveToTrash : function(files) {
			var self = this;
			this.setExecuteEnabled(false);
			var fileService = this.getFileService();
			var deletedFiles = [];
			var errorCount = 0;
			
			for(var i=0;i<files.length;i++){
				files[i] = fileService.newFile(files[i]);
				fileService.deleteFile(files[i].getFileId()).then(
						function(deletedFileId) {
							deletedFiles.push(deletedFileId);
							self._handleRequestComplete(deletedFiles, errorCount,files);								
						}, 
						function(error) {
							errorCount++;
							self._handleRequestComplete(deletedFiles, errorCount,files);		
						}
					);
			}
			this.setExecuteEnabled(true);
		},
		
		/*
		 * Called after a request has completed to check are we done yet
		 */
		_handleRequestComplete : function(deletedFiles, errorCount, files) {
			if (deletedFiles.length + errorCount == files.length) {
				this.setExecuteEnabled(true);
				if (deletedFiles.length > 0) {
					this._setSuccessMessage(files, deletedFiles);
					this.onSuccess();
				} else {
					this._setErrorMessage(files);
					this.onError();
				}
			}
		},
		
		/*
		 * Set the successMessage for the specified operation
		 */
		_setSuccessMessage : function(files) {
			if (files.length == 1) {
				this.successTemplate = stringUtil.replace(nls.moveToTrashSuccess, {fileName : files[0]._fields.title});
			} else {	
				this.successTemplate = stringUtil.replace(nls.moveToTrashSuccessMulti, {count : files.length});
			}
		},
		
		/*
		 * Set the errorMessage for the specified  operation
		 */
		_setErrorMessage : function(files) {

			this.errorTemplate = stringUtil.replace(nls.moveToTrashErrorMulti, {fileName : files[0]._fields.title});

		},
		
		/*
		 * Set the move to trash message
		 */
		_setMoveToTrashMessage : function(files) {
			if (!files) {
				this.moveToTrashMessage = "";
			} else if (files.length == 1) {
				this.moveToTrashMessage = nls.labelMoveFile;					
			} else {
				this.moveToTrashMessage = nls.labelMoveFiles;
			}
		},
		
		_getSelectedFileNames: function(files){
			var fileNames="";
			for(var i=0;i<files.length;i++){
				fileNames = fileNames+(files[i].title+"<br />");
			}
			this.fileName = fileNames;
		}
		
	});

	return MoveToTrashWidget;
});
