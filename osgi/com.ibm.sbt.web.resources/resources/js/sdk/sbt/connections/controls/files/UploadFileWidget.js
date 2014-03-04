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
 
dojo.provide("sbt.connections.controls.files.UploadFileWidget");

/**
 * UploadFileWidget
 */
define('sbt/connections/controls/files/UploadFileWidget',[ "../../../declare", "../../../lang", "../../../dom", "../../../stringUtil",
         "../../../i18n!./nls/files", "../../../controls/view/BaseDialogContent", 
         "../../FileService", "../../../text!./templates/UploadFile.html" ],
		function(declare, lang, dom, stringUtil, nls, BaseDialogContent, FileService, UploadFile) {

	/**
	 * Widget which can be used to upload a file.
	 * 
	 * @class UploadFileWidget
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.forums.UploadFileWidget
	 */
	var UploadFileWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the upload file content.
		 */
		templateString : UploadFile,
		
		/**
		 * Default visibility for uploaded file.
		 */
		defaultVisibility : "private",
		
		/**
		 * Default permission to allow others to share the file.
		 */
		defaultPropagate : true,
		
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
			this.errorTemplate = nls.uploadFileError;
			this.successTemplate = nls.uploadFileSuccess;
		},
		
		/**
		 * Post create function is called after section has been created.
		 * 
		 * @method postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
        	
        	if (this.privateInput && this.defaultVisibility == "private") {
        		this.privateInput.checked = true;
        	}
        	if (this.publicInput && this.defaultVisibility == "public") {
        		this.publicInput.checked = true;
        	}

        	if (this.propagateInput) {
        		this.propagateInput.checked = this.defaultPropagate;
        	}
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
			var tags = this.tagsInput.value || "";
			var delim = tags.indexOf(",") ? "," : " ";
			var requestArgs = {
				visibility : visibility,
				propagate : this.propagateInput.checked || false,
				tag : tags.split(delim)
			};

			this._uploadFile(this.fileInput, requestArgs);
		},
		
		//
		// Internals
		//
		
		/*
		 * Upload a single file
		 */
		_uploadFile : function(fileInput, requestArgs) {
			var self = this;
			this.setExecuteEnabled(false);
			var promise = this.getFileService().uploadFile(fileInput, requestArgs);
	        promise.then(
	        	function(file) {
	            	self.file = file;
	        		self.setExecuteEnabled(true);
	        		self._setSuccessMessage(file); 
            		self.onSuccess();
	        	},
	            function(error) {
	            	self.setExecuteEnabled(true);
	            	self._setErrorMessage(error);
	            	self.onError();
	            }
	        );
		},
		
		/*
		 * Set the successMessage for the specified upload operation
		 */
		_setSuccessMessage : function(file) {
			var file = file.getTitle() ;
			this.successTemplate =  stringUtil.transform(nls.uploadFileSuccess, { file : file });
		},
		
		/*
		 * Set the errorMessage for the specified upload operation
		 */
		_setErrorMessage : function(error) {
			this.errorTemplate = (error.response.status == 409) ? nls.uploadFileExists : nls.uploadFileError;
		}
		
	});

	return UploadFileWidget;
});
