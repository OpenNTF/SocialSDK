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
define([ "../../../declare", "../../../lang", "../../../dom", 
         "../../../i18n!./nls/files", 
         "../../../controls/view/BaseDialogContent", "../../../connections/FileService", 
		 "../../../text!./templates/ShareFile.html",
		 "../../../controls/TypeAhead",
		 "../../../stringUtil",
		 "../../../connections/CommunityService"],
		function(declare, lang, dom, nls, BaseDialogContent, FileService, ShareFile,TypeAhead,stringUtil,CommunityService) {

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
		
		/*
		 * If sharing with person is selected use updateFile() from the FileService API
		 * if share with community is selected use shareFileWithCommunity()
		 * This variable is used to keep track of the user's selection
		 */
		_shareWithCommunityOrPerson: "person",
		
		
		
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
		
		getCommunityService: function(){
			if (!this.communityService) {
				var args = this.endpoint ? { endpoint : this.endpoint } : {};
				this.communityService = new CommunityService(args);
			}
			return this.communityService;
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
				var self = this;
			    this._typeAhead = new TypeAhead({
			    	placeholder: nls.labelPersonName,
			    	className: "lotusText",
			    	searchAttr: "name",
			    	queryExpr: "${0}*",
			    	autoComplete: false,
			        storeArgs : {
			        	url: "/profiles/atom/search.do",
			            attributes : {
			    			"entry" : "a:entry",
			    			"name" :"a:title",			
			    			"id" :"a:contributor/snx:userid"
			    		}
			        },
			        onChange: function(){
			        	if(self._shareWithCommunityOrPerson === "community"){
			        		if(this.item.visibility === "public"){
			        			self._checkIfFileIsPrivate();
			        		}
			        	}
			        	
			        },
			        onKeyDown:function(event){
			        	if(event.key === "Enter"){
			        		event.preventDefault();
			        	}
			        }
			    });       
			    this.searchTypeAhead.appendChild(this._typeAhead.domNode);
			    
			} catch (err) {
				dom.setText("content", err);
			}
			
			if(this.files.length == 1 && this.files[0].visibility == "public"){
				this.sharePublicRadioButton.setAttribute("style", "display:none;");
				this.sharePublicRadioButtonLabel.setAttribute("style", "display:none;");
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
				var attributes = {
		    			"entry" : "a:entry",
		    			"name" :"a:title",			
		    			"id" :"snx:userid"
		    		};
				this._typeAhead.searchAttr = "name";
				this._typeAhead.queryExpr = "${0}*";
				this._typeAhead.store.setAttributes(attributes);
				this._typeAhead.store.setUrl("/profiles/atom/search.do");
				this._typeAhead.domNode.placeholder = nls.labelPersonName;
				this._shareWithCommunityOrPerson = "person";
			}else if (event.target.textContent === nls.labelACommunity){
				var attributes = {
	    			"entry" : "a:entry",
	    			"name" :"a:title",			
	    			"id" :"snx:communityUuid",
	    			"visibility":"snx:communityType",
	    			"search":"a:title"
	    		};
				this._typeAhead.searchAttr = "search";
				this._typeAhead.queryExpr = "${0}*";
				this._typeAhead.store.setAttributes(attributes);
				this._typeAhead.store.setUrl("/communities/service/atom/communities/my");
				this._typeAhead.domNode.placeholder = nls.labelCommunityName;
				this._shareWithCommunityOrPerson = "community";
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
		onExecute : function(event) {
			this._shareFiles(this.files);
		},
		
		
		_getUrlParams: function(){
			var params = {};
			if(this.visibility){
				params.visibility = this.visibility;
			}
			if (this._typeAhead.item) {
				params.shareWith = this._typeAhead.item.id;
			}
			if(this.textArea.value.trim() != ""){
				params.summary = this.textArea.value;
			}
			if(this.sharePermission){
				params.sharePermission = this.sharePermission;
			}
			
			return params;
		},

		_shareFiles: function(files){ 
			this.fileService = this.getFileService();
			var params = this._getUrlParams();
			var itemId = params.shareWith;
			var sharedFiles = [];
			var self = this;
			var errorCount = 0;
			
			for(var i=0;i<files.length;i++){
				files[i] = this.fileService.newFile(files[i]);
				if(this._shareWithCommunityOrPerson === "person"){
					this.fileService.updateFile(files[i].getFileId(),params).then(
							function(file) {
								sharedFiles.push(file);
								self._handleRequestComplete(sharedFiles, errorCount, files);
							},
							function(error) {
								errorCount++;
								self._handleRequestComplete(sharedFiles, errorCount, files);
							}
					);	
				}else if(this._shareWithCommunityOrPerson === "community"){
					this._shareFileWithCommunity(files[i], itemId,this._typeAhead.item.visibility);				
				}
				
			}
			
		},
		
		_shareFileWithCommunity: function(file,communityId,communityType){
			var communityIds = [];
			communityIds.push(communityId);
			var sharedFiles = [];
			var self = this;
			var errorCount = 0;
			this.fileService = this.getFileService();
			
			var fileType = file.getVisibility();
			if(fileType === "private" || fileType==="shared" && communityType === "public"){
				var param = {
					visibility: "public"
				};
				this.fileService.updateFile(file.getFileId(),param).then(
						function(){
							return self.fileService.shareFileWithCommunities(file.getFileId(),communityIds);
						}
						
				).then(
						function(success){
							sharedFiles.push(file);
							self._handleRequestComplete(sharedFiles, errorCount, self.files);
						},
						function(error){
							errorCount++;
							self._handleRequestComplete(sharedFiles, errorCount, self.files);
						}
				);
			}else{
				this.fileService.shareFileWithCommunities(file.getFileId(),communityIds).then(
						function(file){
							sharedFiles.push(file);
							self._handleRequestComplete(sharedFiles, errorCount, self.files);
						},function(error){
							errorCount++;
							self._handleRequestComplete(sharedFiles, errorCount, self.files);
						}
				);
			}
			
		},
		
		_checkIfFileIsPrivate: function(){
			if(this._shareWithCommunityOrPerson==="community"){
        		for(var i=0;i<this.files.length;i++){
        			this.fileService = this.getFileService();
        			this.files[i] = this.fileService.newFile(this.files[i]);
        			if(this.files[i].getVisibility() === "private" || this.files[i].getVisibility() === "shared"){
        				this._displayShareWarning(true);
        			}
        		}
        	}
		},
		
		/*
		 * Called after a request has completed to check are we done yet
		 */
		_handleRequestComplete : function(sharedFiles, errorCount, files) {
			if (sharedFiles.length + errorCount == files.length) {
				this.setExecuteEnabled(true);
				if (sharedFiles.length > 0) {
					this._setSuccessMessage(files, sharedFiles);
					this.onSuccess();
				} else {
					this._setErrorMessage(files);
					this.onError();
				}
			}else {
				this.setExecuteEnabled(true);
				this._setErrorMessage(files);
				this.onError();
			}
		},
		
		/*
		 * Set the successMessage for the specified operation
		 */
		_setSuccessMessage : function(files) {
			if (files.length == 1) {
				this.successTemplate = stringUtil.replace(nls.shareSuccess, {fileName : files[0]._fields.title});
			} else {	
				this.successTemplate = stringUtil.replace(nls.shareSuccessMulti, {count : files.length});
			}
		},
		
		/*
		 * Set the errorMessage for the specified  operation
		 */
		_setErrorMessage : function(files) {
			if(files.length == 1){
				this.errorTemplate = stringUtil.replace(nls.shareError, {fileName : files[0]._fields.title});
			}else{
				this.errorTemplate = stringUtil.replace(nls.shareErrorMulti, {count : files.length});
			}
		},
		
		/*
		 * displays a warning if the user is about to share a private file
		 * with a public community that the file must be made public
		 */
		_displayShareWarning: function(showWarning){
			if(showWarning){
				this.makePublicWarning.setAttribute("style","");
			}else if(!showWarning){
				this.makePublicWarning.setAttribute("style","display:none");
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
		}
		
	});

	return ShareFileWidget;
});