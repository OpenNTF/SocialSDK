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
 * 
 */
define([ "../../../declare",
         "../../../lang",
         "../../../dom",
         "../../../stringUtil",
         "../../../config",  
         "../../../store/parameter",
         "../../../controls/grid/Grid", 
         "./FileGridRenderer", 
         "./FileAction",
         "../../../connections/controls/vcard/SemanticTagService", 
         "../../../connections/FileService",
         "../../../connections/FileConstants"], 
        function(declare, lang, dom, stringUtil, sbt, parameter, Grid, FileGridRenderer, FileAction, SemanticTagService, FileService, FileConstants) {

	// TODO use values from constants and handle authType
	var fileUrls = {
		publicFiles : "/files/basic/anonymous/api/documents/feed?visibility=public",
        pinnedFiles : "/files/basic/api/myfavorites/documents/feed",
        folders : "/files/basic/api/collections/feed",
        pinnedFolders : "/files/basic/api/myfavorites/collections/feed",
        activeFolders : "/files//basic/api/collections/addedto/feed", // Folders you recently added files too.
        publicFolders : "/files/basic/anonymous/api/collections/feed",
        library : "/files/basic/api/myuserlibrary/feed",
        shares : "/files/basic/api/documents/shared/feed", // only lists files shared with you.
        recycledFiles : "/files/basic/api/myuserlibrary/view/recyclebin/feed",
        fileComments : "/files/basic/api/userlibrary/{userId}/document/{fileId}/feed?category=comment",
        fileShares : "/files/basic/api/documents/shared/feed"
	};
	var xpath_files = {
		"id" : "id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"label" : "td:label",
		"fileSize" : "td:totalMediaSize",
		"fileUrl" : "a:link[@rel='alternate']/@href",
		"downloadUrl" : "a:link[@rel='edit-media']/@href",
		"tags" : "a:category/@term",
		"summary" : "a:summary[@type='text']",
		"content" : "a:content[@type='html']",
		"visibility" : "td:visibility",
		"notification" : "td:notification",
		"versionUuid" : "td:versionUuid",
		"versionLabel" : "td:versionLabel",
		"documentVersionUuid" : "td:documentVersionUuid",
		"documentVersionLabel" : "td:documentVersionLabel",
		"shareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/share']",
		"commentCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
		"hitCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
		"anonymousHitCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/anonymous_hit']",
		"attachmentCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/attachments']",
		"referenceCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/references']",
		"recommendationCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
		"collectionCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/collections']",
		"versionCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/versions']",
		"modified" : "td:modified",
		"created" : "td:created",
		"published" : "a:published",
		"updated" : "a:updated",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email",
		"modifierName" : "a:modifier/a:name",
		"modifierId" : "a:modifier/snx:userid",
		"modifierEmail" : "a:modifier/a:email"
	};
	var xpath_folders = {
		"id" : "id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"label" : "td:label",
		"folderUrl" : "a:link[@rel='alternate']/@href",
		"logoUrl" : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
		"tags" : "a:category/@term",
		"summary" : "a:summary[@type='text']",
		"content" : "a:content[@type='html']",
		"visibility" : "td:visibility",
		"notification" : "td:notification",
		"versionUuid" : "td:versionUuid",
		"versionLabel" : "td:versionLabel",
		"documentVersionUuid" : "td:documentVersionUuid",
		"documentVersionLabel" : "td:documentVersionLabel",
		"itemCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/item']",
		"shareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/user']",
		"groupShareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/group']",
		"modified" : "td:modified",
		"created" : "td:created",
		"updated" : "a:updated",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email",
		"content" : "a:content[@type='text']",
		"modifierName" : "a:modifier/a:name",
		"modifierId" : "a:modifier/snx:userid",
		"modifierEmail" : "a:modifier/a:email"
	};
	var xpath_comments = {
		"id" : "a:id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"content" : "a:content[@type='text']",
		"created" : "td:created",
		"modified" : "td:modified",
		"versionLabel" : "td:versionLabel",
		"updated" : "a:updated",
		"published" : "a:published",
		"modifierName" : "a:modifier/a:name",
		"modifierId" : "a:modifier/snx:userid",
		"modifierEmail" : "a:modifier/a:email",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email"
	};
	var xpath_shares = {
		"id" : "id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"summary" : "a:summary[@type='text']",
		"sharedResourceType" : "td:sharedResourceType", // always set to document, but will add the attribute anyway
		"sharePermission" : "td:sharePermission",
		"sharedWhat" : "td:sharedWhat",
		"sharedWithName" : "a:sharedWith/a:name",
		"sharedWithId" : "a:sharedWith/snx:userid",
		"sharedWithEmail" : "a:sharedWith/a:email",
		"documentOwner" : "td:documentOwner",
		"updated" : "a:updated",
		"published" : "a:published",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email"
	};
	var sortVals = {
			name: "title",
        	updated: "modified",
        	downloads: "downloaded",
        	comments: "commented",
        	likes: "recommended",
        	files: "itemCount",
        	created: "created",
        	modified: "modified"
	};
	
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")				
	};
	
    /**
     * @class FileGrid
     * @namespace sbt.connections.controls.files
     * @module sbt.connections.controls.files.FileGrid
     */
   var FileGrid =  declare(Grid, {

    	gridSortType: "",
    	fileService: null,
    	
    	/**
    	 * Options determine which type of file grid will be created
    	 */
        options : {
            "library" : {
                storeArgs : {
                    url : fileUrls.library,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "publicFiles" : {
                storeArgs : {
                    url : fileUrls.publicFiles,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "pinnedFiles" : {
                storeArgs : {
                    url : fileUrls.pinnedFiles,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "folders" : {
                storeArgs : {
                    url : fileUrls.folders,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "publicFolders" : {
                storeArgs : {
                    url : fileUrls.publicFolders,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "pinnedFolders" : {
                storeArgs : {
                    url : fileUrls.pinnedFolders,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "activeFolders" : {
                storeArgs : {
                    url : fileUrls.activeFolders,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "shares" : {
                storeArgs : {
                    url : fileUrls.shares,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "recycledFiles" : {
                storeArgs : {
                    url : fileUrls.recycledFiles,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "recycledFile"
                }
            },
            "fileComments" : {
                storeArgs : {
                    url : fileUrls.fileComments,
                    attributes : xpath_comments,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "comment"
                }
            },
            "fileShares" : {
                storeArgs : {
                    url : fileUrls.fileShares,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            }

        },
        
        /**
         * The default grid, if no options are selected
         */
        defaultOption: "publicFiles",
        
        /**
         * FileAction defines the default actions for files, which can be overridden 
         */
        fileAction: new FileAction(),
        	
        /**
         * Constructor function
         * @method constructor
         */
        constructor: function(args){

        	this.fileService = new FileService(this.endpointName || "connections");
        	
        	/**gridSortType is used to determine what sorting anchors should be used,
        	 * for example folders have different sort anchors than files, file comments have no anchors etc*/
        	if(args.type=="fileShares" || args.type == "library" || args.type == "pinnedFiles"){
        		gridSortType = "file";
        	}else if(args.type == "fileComments" || args.type == "recycledFiles"){
        		gridSortType= "";
        	}else if(args.type == "publicFiles"){
        		gridSortType="publicFiles";
        	}else{
        		gridSortType="folder";
        	}

            var nls = this.renderer.nls;
            this._sortInfo = {
                name: { 
                    title: nls.name, 
                    sortMethod: "sortByName",
                    sortParameter: "name"
                },
                updated: {
                    title: nls.updated, 
                    sortMethod: "sortByLastUpdated",
                    sortParameter: "updated"
                },
                downloads: { 
                    title: nls.downloads, 
                    sortMethod: "sortByDownloads",
                    sortParameter: "downloads"
                },
                comments: { 
                    title: nls.comments, 
                    sortMethod: "sortByComments",
                    sortParameter: "comments"
                },
                likes: { 
                    title: nls.likes, 
                    sortMethod: "sortByLikes",
                    sortParameter: "likes"
                },
                created:{
                	title: nls.created,
                	sortMethod: "sortByCreatedDate",
                    sortParameter: "created"
                },
                files:{
                	title: nls.files,
                	sortMethod: "sortByNumberOfFiles",
                    sortParameter: "files"
                }
            };

            if(args.type == "publicFiles"){
            	this._activeSortAnchor = this._sortInfo.created;
            	this._activeSortIsDesc = false;  
            }else if(args.type == "folders"){
            	this._activeSortAnchor = this._sortInfo.name;
            	this._activeSortIsDesc = true;  
            }else {
            	this._activeSortAnchor = this._sortInfo.updated; 
            	this._activeSortIsDesc = true;  
            }
            
            if(args && args.pinFile){
            	this.renderer.pinFiles = args.pinFile;
            }
              
        },
        
        /**
         * Creates a new AtomStore
         * @method createDefaultStore
         * @param args -Store args, PArameters for the Atom store, such as URL, and Attributes
         * @returns
         */
        createDefaultStore : function(args) {
            args.url = this._buildUrl(args.url);
            
            return this.inherited(arguments);
        },
        
        /**
         * Instantiates a FileGridRenderer
         * @method createDefaultRenderer
         * @param args
         * @returns {FileGridRenderer}
         */
        createDefaultRenderer : function(args) {
            return new FileGridRenderer(args);
        },
        
        /**
         * Called after the grid is created
         * The semanticTagService is loaded, which is responsible for displaying business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Event handler for onClick events
         * @method handleClick
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        handleClick: function(el, data, ev) {
            if (this.fileAction) {
                this._stopEvent(ev);
                
                this.fileAction.execute(data, { grid : this.grid }, ev);
            }
        },
        /**
         * @method getSortInfo
         * @returns A List of Strings,that describe how the grid can be sorted
         */        
        getSortInfo: function() {
            if(gridSortType == "file"){

	        	return {
	                active: {
	                    anchor: this._activeSortAnchor,
	                    isDesc: this._activeSortIsDesc
	                },
	                list: [this._sortInfo.name, this._sortInfo.updated, this._sortInfo.downloads, this._sortInfo.comments, this._sortInfo.likes]
	            };
            }else if (gridSortType == "folder"){
            	return {
	                active: {
	                    anchor: this._activeSortAnchor,
	                    isDesc: this._activeSortIsDesc
	                },
	                list: [this._sortInfo.name, this._sortInfo.updated, this._sortInfo.created,this._sortInfo.files]
	            };
            }else if(gridSortType == "publicFiles"){
            	return {
	                active: {
	                    anchor: this._activeSortAnchor,
	                    isDesc: this._activeSortIsDesc
	                },
	                list: [ this._sortInfo.created, this._sortInfo.downloads, this._sortInfo.comments, this._sortInfo.likes]
	            };
            }
        },
        
        /**
         * Sort the grid rows by name
         * @method  sortByName
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByName: function(el, data, ev) {
            this._sort("name", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by last modified date
         * @method  sortByLastUpdated
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByLastUpdated: function(el, data, ev) {
            this._sort("updated", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by the amount of times a file has been downloaded. 
         * @method  sortByDownloads
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByDownloads: function(el, data, ev) {
            this._sort("downloads", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by the amount of comments a file has.
         * @method sortByComments
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByComments: function(el, data, ev) {
            this._sort("comments", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by the number of "likes" that a file has. 
         * @method  sortByLikes
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByLikes: function(el, data, ev) {
            this._sort("likes", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by when the files were first created
         * @method  sortByCreatedDate
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByCreatedDate:function(el, data, ev) {
            this._sort("created", true, el, data, ev);
        },
        
        /**
         * Sorts the grid, based on the number of files contained in each folder.
         * This is for grids that display folders.
         * @method sortByNumberOfFiles
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByNumberOfFiles: function(el, data, ev) {
            this._sort("files", true, el, data, ev);
        },
       
        /**
         * Event handler to show and hide the more options in the files grid
         * @method showMore
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        showMore: function(el, data, ev){
        	/**TODO to be implemented in iteration 9 */	
        },
        
        /**
         * @method onUpdate This is called after the grid is updated
         * In this implementation, a list of pinned files is received from the server
         * then all of the pin file links, are retrieved by class name,
         * then if the file is a pinned file, its css class will be changed to reflect this 
         * NOTE: this function will only execute, if file pin functionality is passed as an argument to the grid
         */
        onUpdate: function(){
        	
	        if(this.renderer.pinFiles){
	        	
	        	//Get all of the pin file img tags, we do this by classname
	        	var pinElements = document.getElementsByClassName(this.renderer.unPinnedClass);
	        	//ids will hold the ID of each element, the id of the element is the uuid of the file.
	        	var ids = [];
	        	//set the Ids into the ids array
	        	for(var x =0;x <pinElements.length;x++){
	        		ids[x] = pinElements[x].id;
	        	}
	            
	        	//create an args object containing these three vars to hitch. 
	        	var pinClass = this.renderer.pinnedClass;
	            var unPinnedClass = this.renderer.unPinnedClass
	            var renderer = this.renderer;
	            var args = {pinClass:pinClass,unPinnedClass:unPinnedClass,ids:ids,renderer:renderer};
	            
	        	//we use the array of ids, and not the array of elements
	        	//because as we remove a class from an element, the array of elements will dynamically reduce
	        	this.renderer._hitch(args,this.fileService.getMyPinnedFiles().then(
	        		function(files) {
	        			for(var k=0;k<args.ids.length;k++){
	        				for(var i=0;i<files.length;i++){
	        					if(args.ids[k] == files[i].getId()){
	        						args.renderer._removeClass(args.ids[k],args.unPinnedClass);
	        						args.renderer._addClass(args.ids[k],args.pinClass);
	        					}
	        				}
	        			}
	        		},
	        		function(error) {
	        			console.log("error getting pinned files");
	        		}
	        	));
	        	
	          }
        },	
        /**This function pins(favourites) a file,
         * It will send a request to the server using the file service API,
         * And when the request returns successfully, the css clas
         * of the link will be change to reflect that the file is now pinned. 
         * If the file is already pinned, it will remove the pin from the file.
         * @method doPinFile
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        doPinFile: function(el, data, ev){
        	var uuid = "";
        	if(data.uuid){
        		uuid = data.uuid;
        	}else if(data._attribs.uuid){
        		uuid = data._attribs.uuid;
        	}
        	
        	//create an args object containing these three vars to hitch. 
        	var pinClass = this.renderer.pinnedClass;
            var unPinnedClass = this.renderer.unPinnedClass;
            var renderer = this.renderer;
            var args = {pinClass:pinClass,unPinnedClass:unPinnedClass,el:el,renderer:renderer};
        	
        	if(el.firstElementChild.className == this.renderer.unPinnedClass){
	        	 this.renderer._hitch(args,this.fileService.pinFile(uuid).then(
	        		function(response) {
	        			args.renderer._removeClass(args.el.firstElementChild, args.unPinnedClass);
	        			args.renderer._addClass(args.el.firstElementChild, args.pinClass);	
	
	        		},
	        		function(response) {
	        			console.log("Error pinning file");
	        		}
	        	));
        	} else if (el.firstElementChild.className == this.renderer.pinnedClass){
        		this.renderer._hitch(args,this.fileService.unpinFile(uuid).then(
        			function(data) {
        				args.renderer._removeClass(args.el.firstElementChild, args.pinClass);
        				args.renderer._addClass(args.el.firstElementChild, args.unPinnedClass);	
        			},
        			function(error) {
        				console.log("error removing pin from file");
        			}
        		));
        	}
        	
        },
        // Internals
        _buildUrl: function(url) {
            url = stringUtil.replace(url, this);
            
            if (this.direction) {
                url += "?direction=" + this.direction;
            }

            return url;
        }

    });

    return FileGrid;
});
