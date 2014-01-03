/* © Copyright IBM Corp. 2013
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
 * UploadFileWidget
 */
define([ "sbt/declare", "sbt/lang", "sbt/dom", 
         "sbt/i18n!./nls/files", 
         "sbt/controls/view/BaseDialogContent", "sbt/connections/FileService", 
		 "sbt/text!./templates/ShareFile.html",
		 "sbt/controls/TypeAhead",
		 "sbt/stringUtil",
		 "sbt/connections/FileService"],
		function(declare, lang, dom, nls, BaseDialogContent, FileService, ShareFile,TypeAhead,stringUtil,FileService) {

	/**
	 * Widget which can be used to upload a file.
	 * 
	 * @class UploadFileWidget
	 * @namespace sbt.connections.controls.files
	 * @module sbt.connections.controls.forums.UploadFileWidget
	 */
	var ShareFileWidget = declare([ BaseDialogContent ], {
		
		/**
		 * Template used to display the upload file content.
		 */
		templateString : ShareFile,
		
		/**
		 * Share the file as public, or as private with a person or community
		 */
		visibility: "public",
		
		/**
		 * Computed message which displays number of files selected.
		 */
		numberFilesSelected : "",
		
		/**
		 * The permissions the person or community has for the shared file
		 */
		sharePermission: "View",
		
		/*
		 *The type ahead text field used to select a person or community 
		 */
		_typeAhead: "",
		
		/*
		 *To keep track of whether the message text area should be displayed after switching 
		 *between public and private views.
		 */
		_displayMessageTextArea: false,
		
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
		 * Called after properties have been set
		 */
		postMixInProperties: function() {
			this.selectionChanged(this.files);
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
		 * Post create function is called after section has been created.
		 * The input for searching for users or coomunities is created
		 * by creating a type ahead control.
		 * @method postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
			try {
			    this._typeAhead = new TypeAhead({
			    	placeholder: nls.labelPersonName,
			    	className: "lotusText",
			        storeArgs : {
			        	url: "/profiles/atom/search.do",
			            attributes : {
			    			"entry" : "/a:entry",
			    			"name" :"a:title",			
			    			"id" :"snx:communityUuid"
			    		}
			        }
			    });       
			    this.searchTypeAhead.appendChild(this._typeAhead.domNode);
			    
			} catch (err) {
				dom.setText("content", err);
			}
			
		},
		
		radioButtonSelected: function(event){
			var radioButton = event.target;
			if(radioButton.value === nls.labelPublic){
				this.shareWithRow.setAttribute("style", "display:none;");
				this.messageRow.setAttribute("style", "display:none;");
				this.visibility = "public";
				this.messageTextArea.setAttribute("style","display:none");
				this.messageRow.setAttribute("style", "display:none");
			}else if(radioButton.value === nls.labelPeople){
				this.shareWithRow.setAttribute("style", "");
				this.messageRow.setAttribute("style", "");
				this.visibility = "private";
				if(this._displayMessageTextArea){
					this.setMessage();
				}
			}
		},
		
		handleShareWithSelection: function(event){
			if(event.target.textContent === nls.labelAPerson){
				this._typeAhead.store.setUrl("/profiles/atom/search.do");
				this._typeAhead.domNode.placeholder = nls.labelPersonName;
			}else if (event.target.textContent === nls.labelACommunity){
				this._typeAhead.store.setUrl("/communities/service/atom/communities/all");
				this._typeAhead.domNode.placeholder = nls.labelCommunityName;
			}
		},
		
		handleSelectAccessType: function(event){
			if(event.target.textContent === nls.labelAsReader){
				this.sharePermission = "View";
			}else if (event.target.textContent === nls.labelAsEditor){
				this.sharePermission = "Edit";
			}
		},
		
		/**
		 * Displays a text area to allow the user to 
		 * type a message for sharing a file
		 * @param event the onClick event
		 */
		setMessage: function(event){
			this.messageTextArea.setAttribute("style","");
			this.messageRow.setAttribute("style", "display:none");
			this._displayMessageTextArea = true;
		},

		/**
		 * Called when the execute button is clicked.
		 * 
		 * @method onExecute
		 */
		onExecute : function() {
			
			//TODO add check for selected files
			//if files > 0 
			this._shareFiles();
			//else keep share files button disabled 
		},

		_shareFiles: function(){
			 
			this.fileService = this.getFileService();
			
			var params = {};
			
			if(this.visibility){
				params.visibility = this.visibility;
			}
			if (this._typeAhead.domNode.value.trim() != "") {
				params.shareWith = this._typeAhead.domNode.value;
			}
			if(this.messageTextArea.value.trim() != ""){
				params.summary = this.messageTextArea.value;
			}
			if(this.sharePermission){
				params.sharePermission = this.sharePermission;
			}
			
			//TODO get selectedFiles
			//fileservice.updateFile()
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
		}
		
	});

	return ShareFileWidget;
});