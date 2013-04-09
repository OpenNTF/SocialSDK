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
define([ "sbt/_bridge/declare",
         "sbt/controls/grid/Grid", 
         "sbt/controls/grid/connections/FileGridRenderer", 
         "sbt/controls/grid/connections/FileAction",
         "sbt/config",  
         "sbt/controls/vcard/connections/SemanticTagService", 
         "dojo/string", 
         "sbt/store/parameter",
         "sbt/connections/FileService",
         "sbt/connections/FileConstants"], 
        function(declare, Grid, FileGridRenderer, FileAction, sbt, SemanticTagService, string, parameter,FileService) {

    /**
     * @class FileGrid
     * @namespace sbt.controls.grid.connections
     * @module sbt.controls.grid.connections.FileGrid
     */
    declare("sbt.controls.grid.connections.FileGrid", Grid, {

    	gridSortType: "",
    	/**
    	 * Options determine which type of file grid will be created
    	 */
        options : {
            "library" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.library,
                    attributes : sbt.connections.fileConstants.xpath_files,
                    paramSchema: parameter.files.all
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "publicFiles" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.publicFiles,
                    attributes : sbt.connections.fileConstants.xpath_files,
                    paramSchema: parameter.files.all
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "pinnedFiles" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.pinnedFiles,
                    attributes : sbt.connections.fileConstants.xpath_files,
                    paramSchema: parameter.files.all
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "folders" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.folders,
                    attributes : sbt.connections.fileConstants.xpath_folders
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "publicFolders" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.publicFolders,
                    attributes : sbt.connections.fileConstants.xpath_folders,
                    paramSchema: parameter.files.all
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "pinnedFolders" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.pinnedFolders,
                    attributes : sbt.connections.fileConstants.xpath_folders
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "activeFolders" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.activeFolders,
                    attributes : sbt.connections.fileConstants.xpath_folders
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "shares" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.shares,
                    attributes : sbt.connections.fileConstants.xpath_files
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "recycledFiles" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.recycledFiles,
                    attributes : sbt.connections.fileConstants.xpath_files
                },
                rendererArgs : {
                    type : "recycledFile"
                }
            },
            "fileComments" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.fileComments,
                    attributes : sbt.connections.fileConstants.xpath_comments
                },
                rendererArgs : {
                    type : "comment"
                }
            },
            "fileShares" : {
                storeArgs : {
                    url : sbt.connections.fileUrls.fileShares,
                    attributes : sbt.connections.fileConstants.xpath_files
                },
                rendererArgs : {
                    type : "file"
                }
            }

        },
        
        /**Constructor function
         * @method - constructor
         * */
        constructor: function(args){
        	
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

            this._activeSortAnchor = this._sortInfo.name;
            this._activeSortIsDesc = true;
        },
        
        /**
         * The default grid, if no options are selected
         */
        defaultOption: "publicFiles",
        
        /**FileAction defines the default actions for files, which can be overridden */
        fileAction: new FileAction(),
        	
        /**
         * Creates a new AtomStore
         * @method - createDefaultStore
         * @param args -Store args, PArameters for the Atom store, such as URL, and Attributes
         * @returns
         */
        createDefaultStore : function(args) {
            args.url = this._buildUrl(args.url);
            
            return this.inherited(arguments);
        },
        
        /**
         * Instantiates a FileGridRenderer
         * @method - createDefaultRenderer
         * @param args
         * @returns {FileGridRenderer}
         */
        createDefaultRenderer : function(args) {
            return new FileGridRenderer(args);
        },
        
        /**
         * Called after the grid is created
         * @method - postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Event handler for onClick events
         * @method - handleClick
         * @param el - the element that fired the event
         * @param data -the data associated with the element
         * @param ev - the event
         */
        handleClick: function(el, data, ev) {
            if (this.fileAction) {
                dojo.stopEvent(ev);
                
                this.fileAction.execute(data, { grid : this.grid }, ev);
            }
        },
        /**
         * @method - getSortInfo
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
         * Sort the grid rows by last modified date
         * @method - sortByName
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByName: function(el, data, ev) {
            this._sort("name", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by last modified date
         * @method - sortByName
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByLastUpdated: function(el, data, ev) {
            this._sort("updated", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by last modified date
         * @method - sortByName
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByDownloads: function(el, data, ev) {
            this._sort("downloads", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by last modified date
         * @method - sortByName
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByComments: function(el, data, ev) {
            this._sort("comments", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by last modified date
         * @method - sortByName
         * @param el - The element that was clicked, typically a "sort by" button
         * @param data - the data associated with the element
         * @param ev - the event
         */
        sortByLikes: function(el, data, ev) {
            this._sort("likes", true, el, data, ev);
        },
        
        sortByCreatedDate:function(el, data, ev) {
            this._sort("created", true, el, data, ev);
        },
        
        sortByNumberOfFiles: function(el, data, ev) {
            this._sort("files", true, el, data, ev);
        },
        
        pinFile: function(el, data, ev){
        	
        	var uuid = "";
        	if(data.uuid){
        		uuid = data.uuid;
        	}else if(data.element.children[1].textContent){
        		uuid = data.element.children[1].textContent;
        	}
        		
        	var fileService = new FileService();
        	var file = fileService.getFile({
        		id : uuid,
        		loadIt : false
        	});
        	
        	 fileService.pinFile(file,{
        		load : function(response) {
        			/**TODO handle response */
        			//domClass.remove(el.firstElementChild, "lconnSprite-iconPinned16-off");
        			//domClass.add(el.firstElementChild, "lconnSprite-iconPinned16-on");

        			
        		},
        		error : function(response) {
        			//console.log("Error pinning file");
        		}
        	});

        },

        /**
         * Event handler to show and hide the more options in the files grid
         * @method - showMore
         * @param el - The "more" link that was clicked
         * @param data - the data for the row
         * @param ev - the event - onClick
         */
        showMore: function(el, data, ev){
        	/**TODO - to be implemented in iteration 9 */	
        },

        
        getPinnedFiles: function(){
        	/**TODO - To be implemented in iteration 8 */
        },
        
        // Internals
        _buildUrl: function(url) {
            url = string.substitute(url, this);
            
            if (this.direction) {
                url += "?direction=" + this.direction;
            }

            return url;
        }

    });

    return sbt.controls.grid.connections.FileGrid;
});