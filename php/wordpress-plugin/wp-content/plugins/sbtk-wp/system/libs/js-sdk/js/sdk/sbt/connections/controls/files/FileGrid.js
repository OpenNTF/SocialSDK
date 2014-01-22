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
define(
		[ "../../../declare", "../../../lang", "../../../dom",
				"../../../stringUtil", "../../../config",
				"../../../store/parameter", "../../../controls/grid/Grid",
				"./FileGridRenderer", "./FileAction",
				"../../../connections/controls/vcard/SemanticTagService",
				"../../../connections/FileService",
				"../../../connections/FileConstants" ],
		function(declare, lang, dom, stringUtil, sbt, parameter, Grid,
				FileGridRenderer, FileAction, SemanticTagService, FileService,
				FileConstants) {

			var sortVals = {
				name : "title",
				updated : "modified",
				downloads : "downloaded",
				comments : "commented",
				likes : "recommended",
				files : "itemCount",
				created : "created",
				modified : "modified"
			};

			var ParamSchema = {
				pageNumber : parameter.oneBasedInteger("page"),
				pageSize : parameter.oneBasedInteger("ps"),
				sortBy : parameter.sortField("sortBy", sortVals),
				sortOrder : parameter.sortOrder("sortOrder")
			};

			/**
			 * @class FileGrid
			 * @namespace sbt.connections.controls.files
			 * @module sbt.connections.controls.files.FileGrid
			 */
			var FileGrid = declare(
					Grid,
					{

						gridSortType : "",
						fileService : null,

						/**
						 * Options determine which type of file grid will be
						 * created
						 */
						options : {
							"myFiles" : {
								storeArgs : {
									url : FileConstants.AtomFilesMy,
									attributes : FileConstants.FileXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "file"
								}
							},
							"publicFiles" : {
								storeArgs : {
									url : FileConstants.AtomFilesPublic,
									attributes : FileConstants.FileXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "file"
								}
							},
							"myPinnedFiles" : {
								storeArgs : {
									url : FileConstants.AtomFilesMyPinned,
									attributes : FileConstants.FileXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "file"
								}
							},
							"myFolders" : {
								storeArgs : {
									url : FileConstants.AtomFoldersMy,
									attributes : FileConstants.FolderXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "folder"
								}
							},
							"publicFolders" : {
								storeArgs : {
									url : FileConstants.AtomFoldersPublic,
									attributes : FileConstants.FolderXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "folder"
								}
							},
							"myPinnedFolders" : {
								storeArgs : {
									url : FileConstants.AtomGetPinnedFolders,
									attributes : FileConstants.FolderXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "folder"
								}
							},
							"activeFolders" : {
								storeArgs : {
									url : FileConstants.AtomFoldersActive,
									attributes : FileConstants.FolderXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "folder"
								}
							},							
							"recycledFiles" : {
								storeArgs : {
									url : FileConstants.AtomFilesRecycled,
									attributes : FileConstants.FileXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "recycledFile"
								}
							},
							"myFileComments" : {
								storeArgs : {
									url : FileConstants.AtomFileCommentsMy,
									attributes : FileConstants.CommentXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "comment"
								}
							},
							"fileShares" : {
								storeArgs : {
									url : FileConstants.AtomFilesShared,
									attributes : FileConstants.FileXPath,
									paramSchema : ParamSchema
								},
								rendererArgs : {
									type : "file"
								}
							}

						},

						contextRootMap : {
							files : "files"
						},

						/**
						 * The default grid, if no options are selected
						 */
						defaultOption : "publicFiles",

						/**
						 * FileAction defines the default actions for files,
						 * which can be overridden
						 */
						fileAction : new FileAction(),

						/**
						 * Constructor function
						 * 
						 * @method constructor
						 */
						constructor : function(args) {

							this.fileService = new FileService(
									this.endpointName || "connections");

							/**
							 * gridSortType is used to determine what sorting
							 * anchors should be used, for example folders have
							 * different sort anchors than files, file comments
							 * have no anchors etc
							 */
							if (args.type == "fileShares"
									|| args.type == "library"
									|| args.type == "pinnedFiles") {
								gridSortType = "file";
							} else if (args.type == "fileComments"
									|| args.type == "recycledFiles") {
								gridSortType = "";
							} else if (args.type == "publicFiles") {
								gridSortType = "publicFiles";
							} else {
								gridSortType = "folder";
							}

							var nls = this.renderer.nls;
							this._sortInfo = {
								name : {
									title : nls.name,
									sortMethod : "sortByName",
									sortParameter : "name"
								},
								updated : {
									title : nls.updated,
									sortMethod : "sortByLastUpdated",
									sortParameter : "updated"
								},
								downloads : {
									title : nls.downloads,
									sortMethod : "sortByDownloads",
									sortParameter : "downloads"
								},
								comments : {
									title : nls.comments,
									sortMethod : "sortByComments",
									sortParameter : "comments"
								},
								likes : {
									title : nls.likes,
									sortMethod : "sortByLikes",
									sortParameter : "likes"
								},
								created : {
									title : nls.created,
									sortMethod : "sortByCreatedDate",
									sortParameter : "created"
								},
								files : {
									title : nls.files,
									sortMethod : "sortByNumberOfFiles",
									sortParameter : "files"
								}
							};

							if (args.type == "publicFiles") {
								this._activeSortAnchor = this._sortInfo.created;
								this._activeSortIsDesc = false;
							} else if (args.type == "folders") {
								this._activeSortAnchor = this._sortInfo.name;
								this._activeSortIsDesc = true;
							} else {
								this._activeSortAnchor = this._sortInfo.updated;
								this._activeSortIsDesc = true;
							}

							if (args && args.pinFile) {
								this.renderer.pinFiles = args.pinFile;
							}

						},

						/**
						 * Override buildUrl to add direction, userId and fileId
						 * 
						 * @method buildUrl
						 * @param url
						 *            base url
						 * @param args
						 *            arguments that will be passed to the store
						 * @param endpoint
						 *            An endpoint which may contain custom
						 *            service mappings.
						 * @returns Built url
						 */
						buildUrl : function(url, args, endpoint) {
							var params = {
								format : this.format
							};

							if (this.query) {
								params = lang.mixin(params, this.query);
							}
							if (this.direction) {
								params = lang.mixin(params, {
									direction : this.direction
								});
							}

							return this.constructUrl(url, params, this
									.getUrlParams(), endpoint);
						},

						/**
						 * Return the url parameters to be used
						 * 
						 * @returns {Object}
						 */
						getUrlParams : function() {
							var params = {
								authType : this.getAuthType()
							};

							if (this.userId) {
								params = lang.mixin(params, {
									userId : this.userId
								});
							}
							if (this.documentId) {
								params = lang.mixin(params, {
									documentId : this.documentId
								});
							}

							return params;
						},

						/**
						 * Instantiates a FileGridRenderer
						 * 
						 * @method createDefaultRenderer
						 * @param args
						 * @returns {FileGridRenderer}
						 */
						createDefaultRenderer : function(args) {
							return new FileGridRenderer(args);
						},

						/**
						 * Called after the grid is created The
						 * semanticTagService is loaded, which is responsible
						 * for displaying business card functionality.
						 * 
						 * @method postCreate
						 */
						postCreate : function() {
							this.inherited(arguments);
							SemanticTagService.loadSemanticTagService();
						},

						/**
						 * Event handler for onClick events
						 * 
						 * @method handleClick
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						handleClick : function(el, data, ev) {
							if (this.fileAction) {
								this._stopEvent(ev);

								this.fileAction.execute(data, {
									grid : this.grid
								}, ev);
							}
						},

						/**
						 * @method getSortInfo
						 * @returns A List of Strings,that describe how the grid
						 *          can be sorted
						 */
						getSortInfo : function() {
							if (gridSortType == "file") {

								return {
									active : {
										anchor : this._activeSortAnchor,
										isDesc : this._activeSortIsDesc
									},
									list : [ this._sortInfo.name,
											this._sortInfo.updated,
											this._sortInfo.downloads,
											this._sortInfo.comments,
											this._sortInfo.likes ]
								};
							} else if (gridSortType == "folder") {
								return {
									active : {
										anchor : this._activeSortAnchor,
										isDesc : this._activeSortIsDesc
									},
									list : [ this._sortInfo.name,
											this._sortInfo.updated,
											this._sortInfo.created,
											this._sortInfo.files ]
								};
							} else if (gridSortType == "publicFiles") {
								return {
									active : {
										anchor : this._activeSortAnchor,
										isDesc : this._activeSortIsDesc
									},
									list : [ this._sortInfo.created,
											this._sortInfo.downloads,
											this._sortInfo.comments,
											this._sortInfo.likes ]
								};
							}
						},

						/**
						 * Sort the grid rows by name
						 * 
						 * @method sortByName
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						sortByName : function(el, data, ev) {
							this._sort("name", true, el, data, ev);
						},

						/**
						 * Sort the grid rows by last modified date
						 * 
						 * @method sortByLastUpdated
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						sortByLastUpdated : function(el, data, ev) {
							this._sort("updated", true, el, data, ev);
						},

						/**
						 * Sort the grid rows by the amount of times a file has
						 * been downloaded.
						 * 
						 * @method sortByDownloads
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						sortByDownloads : function(el, data, ev) {
							this._sort("downloads", true, el, data, ev);
						},

						/**
						 * Sort the grid rows by the amount of comments a file
						 * has.
						 * 
						 * @method sortByComments
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						sortByComments : function(el, data, ev) {
							this._sort("comments", true, el, data, ev);
						},

						/**
						 * Sort the grid rows by the number of "likes" that a
						 * file has.
						 * 
						 * @method sortByLikes
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						sortByLikes : function(el, data, ev) {
							this._sort("likes", true, el, data, ev);
						},

						/**
						 * Sort the grid rows by when the files were first
						 * created
						 * 
						 * @method sortByCreatedDate
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						sortByCreatedDate : function(el, data, ev) {
							this._sort("created", true, el, data, ev);
						},

						/**
						 * Sorts the grid, based on the number of files
						 * contained in each folder. This is for grids that
						 * display folders.
						 * 
						 * @method sortByNumberOfFiles
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						sortByNumberOfFiles : function(el, data, ev) {
							this._sort("files", true, el, data, ev);
						},

						/**
						 * Event handler to show and hide the more options in
						 * the files grid
						 * 
						 * @method showMore
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						showMore : function(el, data, ev) {
							/** TODO to be implemented in iteration 9 */
						},

						/**
						 * @method onUpdate This is called after the grid is
						 *         updated In this implementation, a list of
						 *         pinned files is received from the server then
						 *         all of the pin file links, are retrieved by
						 *         class name, then if the file is a pinned
						 *         file, its css class will be changed to
						 *         reflect this NOTE: this function will only
						 *         execute, if file pin functionality is passed
						 *         as an argument to the grid
						 */
						onUpdate : function() {

							if (this.renderer.pinFiles) {

								// Get all of the pin file img tags, we do this
								// by classname
								var pinElements = document
										.getElementsByClassName(this.renderer.unPinnedClass);
								// ids will hold the ID of each element, the id
								// of the element is the uuid of the file.
								var ids = [];
								// set the Ids into the ids array
								for ( var x = 0; x < pinElements.length; x++) {
									ids[x] = pinElements[x].id;
								}

								// create an args object containing these three
								// vars to hitch.
								var pinClass = this.renderer.pinnedClass;
								var unPinnedClass = this.renderer.unPinnedClass
								var renderer = this.renderer;
								var args = {
									pinClass : pinClass,
									unPinnedClass : unPinnedClass,
									ids : ids,
									renderer : renderer
								};

								// we use the array of ids, and not the array of
								// elements
								// because as we remove a class from an element,
								// the array of elements will dynamically reduce
								this.renderer
										._hitch(
												args,
												this.fileService
														.getPinnedFiles()
														.then(
																function(files) {
																	for ( var k = 0; k < args.ids.length; k++) {
																		for ( var i = 0; i < files.length; i++) {
																			if (args.ids[k] == files[i]
																					.getId()) {
																				args.renderer
																						._removeClass(
																								args.ids[k],
																								args.unPinnedClass);
																				args.renderer
																						._addClass(
																								args.ids[k],
																								args.pinClass);
																			}
																		}
																	}
																},
																function(error) {
																	console
																			.log("error getting pinned files");
																}));

							}
						},

						/**
						 * This function pins(favourites) a file, It will send a
						 * request to the server using the file service API, And
						 * when the request returns successfully, the css clas
						 * of the link will be change to reflect that the file
						 * is now pinned. If the file is already pinned, it will
						 * remove the pin from the file.
						 * 
						 * @method doPinFile
						 * @param el
						 *            The element that fired the event
						 * @param data
						 *            The data associated with this table row
						 * @param ev
						 *            The event, onclick
						 */
						doPinFile : function(el, data, ev) {
							var uuid = "";
							if (data.getValue("uid")) {
								uuid = data.getValue("uid");
							}

							// create an args object containing these three vars
							// to hitch.
							var pinClass = this.renderer.pinnedClass;
							var unPinnedClass = this.renderer.unPinnedClass;
							var renderer = this.renderer;
							var args = {
								pinClass : pinClass,
								unPinnedClass : unPinnedClass,
								el : el,
								renderer : renderer
							};

							if (el.firstElementChild.className == this.renderer.unPinnedClass) {
								this.renderer
										._hitch(
												args,
												this.fileService
														.pinFile(uuid)
														.then(
																function(
																		response) {
																	args.renderer
																			._removeClass(
																					args.el.firstElementChild,
																					args.unPinnedClass);
																	args.renderer
																			._addClass(
																					args.el.firstElementChild,
																					args.pinClass);

																},
																function(
																		response) {
																	console
																			.log("Error pinning file");
																}));
							} else if (el.firstElementChild.className == this.renderer.pinnedClass) {
								this.renderer
										._hitch(
												args,
												this.fileService
														.unpinFile(uuid)
														.then(
																function(data) {
																	args.renderer
																			._removeClass(
																					args.el.firstElementChild,
																					args.pinClass);
																	args.renderer
																			._addClass(
																					args.el.firstElementChild,
																					args.unPinnedClass);
																},
																function(error) {
																	console
																			.log("error removing pin from file");
																}));
							}

						}

					// Internals

					});

			return FileGrid;
		});
