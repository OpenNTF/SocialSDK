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
 * AddTagsWidget
 */
define([ "sbt/declare", "sbt/lang", "sbt/dom", "sbt/stringUtil", 
         "sbt/i18n!./nls/files", 
         "sbt/controls/view/BaseDialogContent", "sbt/connections/FileService", 
		 "sbt/text!./templates/AddTags.html" ],
		function(declare, lang, dom, stringUtil, nls, BaseDialogContent, FileService, AddTags) {

	/**
	 * Widget which can be used to add tags to a collection of files.
	 * 
	 * @class AddTagsWidget
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.forums.AddTagsWidget
	 */
	var AddTagsWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the  content.
		 */
		templateString : AddTags,
		
		/**
		 * Computed message which displays number of files selected.
		 */
		numberFilesSelected : "",
		
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
			this.selectionChanged(this.files);
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
		 * Selection changed function is called after the selection has changed.
		 *  
		 * @method selectionChanged
		 * @param selection
		 * @param context
		 */
		selectionChanged : function(selection, context) {
			this.files = selection;

			this._setNumberFilesSelected(this.files ? this.files.length : 0);
			this.setExecuteEnabled(this.files && this.files.length);
		},
		
		/**
		 * Called when the execute button is clicked.
		 * 
		 * @method onExecute
		 */
		onExecute : function() {
			var tags = this.tagsInput.value || "";
			var delim = tags.indexOf(",") ? "," : " ";
			tags = tags.split(delim);

			this._addTags(this.files, tags);
		},
		
		//
		// Internals
		//
			
		/*
		 * Add tags to the specified files
		 */
		_addTags : function(files, tags) {
			if (!files || !tags) {
				return;
			}
			var self = this;
			this.setExecuteEnabled(false);
			var fileService = this.getFileService();
			var taggedFiles = [];
			var errorCount = 0;
			for (var i = 0; i < files.length; i++) {
				files[i] = fileService.newFile(files[i]);
				var currentTags = files[i].getTags();
				files[i].setTags(currentTags.concat(tags));
				fileService.updateFileMetadata(files[i]).then(
					function(file) {
						taggedFiles.push(file);
						self._handleRequestComplete(taggedFiles, errorCount, files, tags);
					},
					function(error) {
						errorCount++;
						self._handleRequestComplete(taggedFiles, errorCount, files, tags);
					}
				);
			}
		},
		
		/*
		 * Called after a request has completed to check are we done yet
		 */
		_handleRequestComplete : function(taggedFiles, errorCount, files, tags) {
			if (taggedFiles.length + errorCount == files.length) {
				this.setExecuteEnabled(true);
				if (taggedFiles.length > 0) {
					this._setSuccessMessage(files, taggedFiles, tags);
					this.onSuccess();
				} else {
					this._setErrorMessage(files, tags);
					this.onError();
				}
			}
		},
		
		/*
		 * Set the number of files selected message
		 */
		_setNumberFilesSelected : function(count) {
			if (!count) {
				this.numberFilesSelected = stringUtil.replace(nls.labelFilesSelected, { length : 0 });
			} else if (count == 1) {
				this.numberFilesSelected = nls.labelFileSelected;					
			} else {
				this.numberFilesSelected = stringUtil.replace(nls.labelFilesSelected, { length : count });
			}
		},
		
		/*
		 * Set the successMessage for the specified add tags operation
		 */
		_setSuccessMessage : function(files, taggedFiles, tags) {
			tags = tags.join();
			if (files.length == 1) {
				this.successTemplate = stringUtil.transform(nls.addTagsSuccess, { label : files[0].getLabel(), tags : tags });
			} else {
				var notTaggedFiles = this._getNotTaggedFiles(files, taggedFiles);
				var msg = null;
				if (taggedFiles.length == 1) {
					msg = stringUtil.transform(nls.addTagsSuccess, { label : taggedFiles[0].getLabel(), tags : tags });
				} else {
					msg = stringUtil.transform(nls.addTagsSuccessMulti, { length : taggedFiles.length, tags : tags });
				}
				this.successTemplate = "<div>" + msg +
					this._getNotTaggedList(notTaggedFiles, tags) +
					"</div>";
			}
		},
		
		/*
		 * Set the errorMessage for the specified add tags operation
		 */
		_setErrorMessage : function(files, tags) {
			tags = tags.join();
			if (files.length == 1) {
				this.errorTemplate = stringUtil.transform(nls.addTagsError, { label : files[0].getLabel(), tags : tags });
			} else {
				this.errorTemplate = "<div>" + 
					this.errorTemplate, stringUtil.transform(nls.addTagsErrorMulti, { length : files.length, tags : tags }) +
					this._getNotTaggedList(files, tags) +
					"</div>";
			}
		},

		/*
		 * Return list contain names of files which could not be tagged.
		 */
		_getNotTaggedList : function(files, tags) {
			var ol = "<ol>";
			for (var i=0; i<files.length; i++) {
				var msg = stringUtil.transform(nls.addTagsError, { label : files[i].getLabel(), tags : tags });
				ol += "<li>" + msg + "</li>";
			}
			ol += "</ol>";
			return ol;
		},
		
		/*
		 * Return array of files objects which were not tagged
		 */
		_getNotTaggedFiles : function(files, taggedFiles) {
			var notTaggedFiles = [];
			for (var i=0; i<files.length; i++) {
				if (!this._isTaggedFile(taggedFiles, files[i])) {
					notTaggedFiles.push(files[i]);
				}
			}
			return notTaggedFiles;
		},
		
		/*
		 * Return true if the specified files was tagged
		 */
		_isTaggedFile: function(taggedFiles, file) {
			for (var i=0; i<taggedFiles.length; i++) {
				if (file.getFileId() == taggedFiles[i].getFileId()) {
					return true;
				}
			}
			return false;
		}
		
		
	});

	return AddTagsWidget;
});