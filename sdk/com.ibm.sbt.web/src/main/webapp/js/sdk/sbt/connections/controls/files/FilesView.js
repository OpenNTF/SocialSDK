/*
 * � Copyright IBM Corp. 2013
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
 * 
 */
define([ "../../../declare", "../../../lang", "../../../stringUtil", "../../../log",
         "./FileGrid", "../../../controls/view/BaseView", "./DownloadFileAction", "./ShareFileAction",
		 "./UploadFileAction", "./MoveToTrashAction", "./AddTagsAction" ],
		function(declare, lang, stringUtil, log, FileGrid, BaseView,DownloadFileAction,ShareFileAction, UploadFileAction, MoveToTrashAction,AddTagsAction) {

	/*
	 * @module sbt.connections.controls.FilesView
	 */
	var FilesView = declare([ BaseView ], {

		title : "Files", 
    
		iconClass : "lotusIcon iconsComponentsBlue24 iconsComponentsBlue24-FilesBlue24",
    	
		defaultActions : true,
		
		defaultGrid : true,
		
		grid : null,
		
		actionButtonTemplate: null,
		
		/**
		 * 
		 */
		postMixInProperties : function() {
			this.inherited(arguments);
			if(this.template){
				this.templateString = this.template;
			}
		},

		/**
		 * Post create function is called after widget has been created.
		 * 
		 * @method - postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
			
			if(this.disabledActionClass){
				this.actionBar.disabledClass = this.disabledActionClass;
			}
			
			if (!this.grid && this.defaultGrid) {
				var gridArgs = (this.type) ? {type : this.type} : {};
				gridArgs = lang.mixin(gridArgs, this.gridArgs || {});

				this.grid = new FileGrid(gridArgs);
				this.setContent(this.grid);
			}
			
			if(this.actionButtonTemplate){
				this.actionBar.actionTemplate = this.actionButtonTemplate;
			}
			
			if (this.grid && this.defaultActions) {
				var _dialogArgs = {};
				var _shareFileArgs = {};
				var _uploadFileArgs = {};
				var _addTagsArgs = {};
				var _moveToTrashArgs = {};
				
				if(this.dialogTemplate){
					_dialogArgs = {templateString: this.dialogTemplate};
				}
				if(this.shareFilesTemplate){
					_shareFileArgs = {templateString:this.shareFilesTemplate};	
				}
				if(this.uploadFileTemplate){
					_uploadFileArgs = {templateString:this.uploadFileTemplate};
				}
				if(this.addTagsTemplate){
					_addTagsArgs = {templateString:this.addTagsTemplate};
				}
				if(this.moveToTrashTemplate){
					_moveToTrashArgs = {templateString:this.moveToTrashTemplate};
				}	
				
				//this.addAction(new DownloadFileAction({grid : this.grid}));
				this.addAction(new ShareFileAction({
					grid : this.grid,
					dialogArgs:_dialogArgs,
					widgetArgs:_shareFileArgs
				}));
				this.addAction(new UploadFileAction({
					grid : this.grid,
					dialogArgs:_dialogArgs,
					widgetArgs:_uploadFileArgs
				}));
				this.addAction(new AddTagsAction({
					grid : this.grid,
					dialogArgs:_dialogArgs,
					widgetArgs:_addTagsArgs
				}));
				this.addAction(new MoveToTrashAction({
					grid : this.grid, 
					dialogArgs: _dialogArgs,
					widgetArgs:_moveToTrashArgs
				}));
			}
		}
	
		//
		// Internals
		//

	});

	return FilesView;
});