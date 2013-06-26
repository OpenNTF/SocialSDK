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
 * JavaScript API for IBM Connections File Service.
 * 
 * @module sbt.connections.FileService
 */

define([ "../declare", "../lang", "../stringUtil", "../Promise", "./FileConstants", "../base/BaseService", "../base/BaseEntity", "../base/XmlDataHandler", "../config", "../util","../xml" ],
		function(declare, lang, stringUtil, Promise, consts, BaseService, BaseEntity, XmlDataHandler, config, util, xml) {

			/**
			 * Comment class associated with a file comment.
			 * 
			 * @class Comment
			 * @namespace sbt.connections
			 */
			var Comment = declare(BaseEntity, {

				/**
				 * Returned the Comment Id
				 * @method getId
				 * @rturns {String} File Id
				 */
				getId : function() {
					return this.id ? this.id : this.getAsString("uid");
				},
				/**
				 * Returns Comment Title
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},
				/**
				 * Returns the Comment Content
				 * @method getContent
				 * @returns {String} content
				 */
				getContent : function() {
					return this.getAsString("content");
				},
				/**
				 * Returns The create Date
				 * @method getCreated
				 * @returns {Date} create Date
				 */
				getCreated : function() {
					return this.getAsDate("created");
				},
				/**
				 * Returns The modified Date
				 * @method getModified
				 * @returns {Date} modified Date
				 */
				getModified : function() {
					return this.getAsDate("modified");
				},
				/**
				 * Returns the version label
				 * @method getVersionLabel
				 * @returns {String} version label
				 */
				getVersionLabel : function() {
					return this.getAsString("versionLabel");
				},
				/**
				 * Returns the updated Date
				 * @method getModified
				 * @returns {Date} modified Date
				 */
				getUpdated : function() {
					return this.getAsDate("updated");
				},
				/**
				 * Returns the published Date
				 * @method getPublished
				 * @returns {Date} modified Date
				 */
				getPublished : function() {
					return this.getAsDate("published");
				},
				/**
				 * Returns the modifier
				 * @method getModifier
				 * @returns {Object} modifier
				 */
				getModifier : function() {
					return this.getAsObject([ "modifierName", "modifierUserId", "modifierEmail", "modifierUserState" ]);
				},
				/**
				 * Returns the author
				 * @method getAuthor
				 * @returns {Object} author
				 */
				getAuthor : function() {
					return this.getAsObject([ "authorName", "authorUserId", "authorEmail", "authorUserState" ]);
				},
				/**
				 * Returns the language
				 * @method getLanguage
				 * @returns {String} language
				 */
				getLanguage : function() {
					return this.getAsString("language");
				},
				/**
				 * Returns the flag for delete with record
				 * @method getDeleteWithRecord
				 * @returns {Boolean} delete with record
				 */
				getDeleteWithRecord : function() {
					return this.getAsBoolean("deleteWithRecord");
				}
			});

			/**
			 * File class associated with a file.
			 * 
			 * @class File
			 * @namespace sbt.connections
			 */
			var File = declare(BaseEntity, {

				/**
				 * Returns the file Id
				 * @method getId
				 * @returns {String} file Id
				 */
				getId : function() {
					return this.id ? this.id : this._fields.id ? this._fields.id : this.getAsString("uid");
				},
				/**
				 * Returns the label
				 * @method getLabel
				 * @returns {String} label
				 */
				getLabel : function() {
					return this.getAsString("label");
				},
				/**
				 * Returns the self URL
				 * @method getSelfUrl
				 * @returns {String} self URL
				 */
				getSelfUrl : function() {
					return this.getAsString("selfUrl");
				},
				/**
				 * Returns the alternate URL
				 * @method getAlternateUrl
				 * @returns {String} alternate URL
				 */
				getAlternateUrl : function() {
					return this.getAsString("alternateUrl");
				},
				/**
				 * Returns the download URL
				 * @method getDownloadUrl
				 * @returns {String} download URL
				 */
				getDownloadUrl : function() {
					return this.getAsString("downloadUrl");
				},
				/**
				 * Returns the type
				 * @method getType
				 * @returns {String} type
				 */
				getType : function() {
					return this.getAsString("type");
				},
				/**
				 * Returns the length
				 * @method getLength
				 * @returns {Number} length
				 */
				getLength : function() {
					return this.getAsNumber("length");
				},
				/**
				 * Returns the Edit Link
				 * @method getEditLink
				 * @returns {String} edit link
				 */
				getEditLink : function() {
					return this.getAsString("editLink");
				},
				/**
				 * Returns the Edit Media Link
				 * @method getEditMediaLink
				 * @returns {String} edit media link
				 */
				getEditMediaLink : function() {
					return this.getAsString("editMediaLink");
				},
				/**
				 * Returns the Thumbnail URL
				 * @method getThumbnailUrl
				 * @returns {String} thumbnail URL
				 */
				getThumbnailUrl : function() {
					return this.getAsString("thumbnailUrl");
				},
				/**
				 * Returns the Comments URL
				 * @method getCommentsUrl
				 * @returns {String} comments URL
				 */
				getCommentsUrl : function() {
					return this.getAsString("commentsUrl");
				},
				/**
				 * Returns the author
				 * @method getAuthor
				 * @returns {Object} author
				 */
				getAuthor : function() {
					return this.getAsObject([ "authorName", "authorUserId", "authorEmail", "authorUserState" ]);
				},
				/**
				 * Returns the Title
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},
				/**
				 * Returns the published date
				 * @method getPublished
				 * @returns {Date} published date
				 */
				getPublished : function() {
					return this.getAsDate("published");
				},
				/**
				 * Returns the updated date
				 * @method getUpdated
				 * @returns {Date} updated date
				 */
				getUpdated : function() {
					return this.getAsDate("updated");
				},
				/**
				 * Returns the created date
				 * @method getCreated
				 * @returns {Date} created date
				 */
				getCreated : function() {
					return this.getAsDate("created");
				},
				/**
				 * Returns the modified date
				 * @method getModified
				 * @returns {Date} modified date
				 */
				getModified : function() {
					return this.getAsDate("modified");
				},
				/**
				 * Returns the last accessed date
				 * @method getLastAccessed
				 * @returns {Date} last accessed date
				 */
				getLastAccessed : function() {
					return this.getAsDate("lastAccessed");
				},
				/**
				 * Returns the modifier
				 * @method getModifier
				 * @returns {Object} modifier
				 */
				getModifier : function() {
					return this.getAsObject([ "modifierName", "modifierUserId", "modifierEmail", "modifierUserState" ]);
				},
				/**
				 * Returns the visibility
				 * @method getVisibility
				 * @returns {String} visibility
				 */
				getVisibility : function() {
					return this.getAsString("visibility");
				},
				/**
				 * Returns the library Id
				 * @method getLibraryId
				 * @returns {String} library Id
				 */
				getLibraryId : function() {
					return this.getAsString("libraryId");
				},
				/**
				 * Returns the library Type
				 * @method getLibraryType
				 * @returns {String} library Type
				 */
				getLibraryType : function() {
					return this.getAsString("libraryType");
				},
				/**
				 * Returns the version Id
				 * @method getVersionUuid
				 * @returns {String} version Id
				 */
				getVersionUuid : function() {
					return this.getAsString("versionUuid");
				},
				/**
				 * Returns the version label
				 * @method getVersionLabel
				 * @returns {String} version label
				 */
				getVersionLabel : function() {
					return this.getAsString("versionLabel");
				},
				/**
				 * Returns the propagation
				 * @method getPropagation
				 * @returns {String} propagation
				 */
				getPropagation : function() {
					return this.getAsString("propagation");
				},
				/**
				 * Returns the recommendations Count
				 * @method getRecommendationsCount
				 * @returns {Number} recommendations Count
				 */
				getRecommendationsCount : function() {
					return this.getAsNumber("recommendationsCount");
				},
				/**
				 * Returns the comments Count
				 * @method getCommentsCount
				 * @returns {Number} comments Count
				 */
				getCommentsCount : function() {
					return this.getAsNumber("commentsCount");
				},
				/**
				 * Returns the shares Count
				 * @method getSharesCount
				 * @returns {Number} shares Count
				 */
				getSharesCount : function() {
					return this.getAsNumber("sharesCount");
				},
				/**
				 * Returns the folders Count
				 * @method getFoldersCount
				 * @returns {Number} folders Count
				 */
				getFoldersCount : function() {
					return this.getAsNumber("foldersCount");
				},
				/**
				 * Returns the attachments Count
				 * @method getAttachmentsCount
				 * @returns {Number} attachments Count
				 */
				getAttachmentsCount : function() {
					return this.getAsNumber("attachmentsCount");
				},
				/**
				 * Returns the versions Count
				 * @method getVersionsCount
				 * @returns {Number} versions Count
				 */
				getVersionsCount : function() {
					return this.getAsNumber("versionsCount");
				},
				/**
				 * Returns the references Count
				 * @method getReferencesCount
				 * @returns {Number} references Count
				 */
				getReferencesCount : function() {
					return this.getAsNumber("referencesCount");
				},
				/**
				 * Returns the total Media Size
				 * @method getTotalMediaSize
				 * @returns {Number} total Media Size
				 */
				getTotalMediaSize : function() {
					return this.getAsNumber("totalMediaSize");
				},
				/**
				 * Returns the Summary
				 * @method getSummary
				 * @returns {String} Summary
				 */
				getSummary : function() {
					return this.getAsString("summary");
				},
				/**
				 * Returns the Content URL
				 * @method getContentUrl
				 * @returns {String} Content URL
				 */
				getContentUrl : function() {
					return this.getAsString("contentUrl");
				},
				/**
				 * Returns the Content Type
				 * @method getContentType
				 * @returns {String} Content Type
				 */
				getContentType : function() {
					return this.getAsString("contentType");
				},
				/**
				 * Returns the objectTypeId
				 * @method getObjectTypeId
				 * @returns {String} objectTypeId
				 */
				getObjectTypeId : function() {
					return this.getAsString("objectTypeId");
				},
				/**
				 * Returns the lock state
				 * @method getLock
				 * @returns {String} lock state
				 */
				getLock : function() {
					return this.getAsString("lock");
				},
				/**
				 * Returns the permission ACLs
				 * @method getAcls
				 * @returns {String} ACLs
				 */
				getAcls : function() {
					return this.getAsString("acls");
				},
				/**
				 * Returns the hit count
				 * @method getHitCount
				 * @returns {Number} hit count
				 */
				getHitCount : function() {
					return this.getAsNumber("hitCount");
				},
				/**
				 * Returns the anonymous hit count
				 * @method getAnonymousHitCount
				 * @returns {Number} anonymous hit count
				 */
				getAnonymousHitCount : function() {
					return this.getAsNumber("anonymousHitCount");
				},
				/**
				 * Returns the tags
				 * @method getTags
				 * @returns {String} tags
				 */
				getTags : function() {
					return this.getAsString("tags");
				},
				/**
				 * Sets the label
				 * @method setLabel
				 * @param {String} label
				 */
				setLabel : function(label) {
					return this.setAsString("label", label);
				},
				/**
				 * Sets the summary
				 * @method setSummary
				 * @param {String} summary
				 */
				setSummary : function(summary) {
					return this.setAsString("summary", summary);
				},
				/**
				 * Sets the visibility
				 * @method setVisibility
				 * @param {String} visibility
				 */
				setVisibility : function(visibility) {
					return this.setAsString("visibility", visibility);
				},
				/**
				 * Loads the file object with the atom entry associated with the file. By default, a network call is made to load the atom entry document in the file object.
				 * 
				 * @method load
				 * @param {Object} [args] Argument object
				 * @param {Boolean} [isPublic] Optinal flag to indicate whether to load public file which does not require authentication
				 */
				load : function(args, isPublic) {
					// detect a bad request by validating required arguments
					var fileUuid = this.getId();
					var promise = this.service.validateField("fileId", fileUuid);
					if (promise) {
						return promise;
					}

					var self = this;
					var callbacks = {
						createEntity : function(service, data, response) {
							self.dataHandler = new XmlDataHandler({
								data : data,
								namespaces : consts.Namespaces,
								xpath : consts.FileXPath
							});
							return self;
						}
					};

					var requestArgs = lang.mixin({
						fileUuid : fileUuid
					}, args || {});
					var options = {
						handleAs : "text",
						query : requestArgs
					};

					var url = consts.AtomFileInstance;

					if (isPublic) {
						url = consts.AtomFileInstancePublic;
					}

					url = this.service.constructUrl(url, null, {
						"documentId" : fileUuid
					});
					return this.service.getEntity(url, options, fileUuid, callbacks);
				},
				/**
				 * Save this file
				 * 
				 * @method save
				 * @param {Object} [args] Argument object
				 */
				save : function(args) {
					if (this.getId()) {
						return this.service.updateFileMetadata(this, args);
					}
				},
				/**
				 * Adds a comment to the file.
				 * 
				 * @method addComment
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				addComment : function(comment, args) {
					return this.service.addCommentToFile(this.getAuthor().authorUserId, this.getId(), comment, args);
				},
				/**
				 * Pin th file, by sending a POST request to the myfavorites feed.
				 * @method pin
				 * @param {Object} [args] Argument object.
				 */
				pin : function(args) {
					return this.service.pinFile(this.getId(), args);
				},
				/**
				 * Unpin the file, by sending a DELETE request to the myfavorites feed.
				 * @method unPin
				 * @param {Object} [args] Argument object.
				 */
				unpin : function(args) {
					return this.service.unpinFile(this.getId(), args);
				},
				/**
				 * Lock the file
				 * @method lock
				 * @param {Object} [args] Argument object
				 */
				lock : function(args) {
					return this.service.lockFile(this.getId(), args);
				},
				/**
				 * UnLock the file
				 * @method unlock
				 * @param {Object} [args] Argument object
				 */
				unlock : function(args) {
					return this.service.unlockFile(this.getId(), args);
				},
				/**
				 * Deletes the file.
				 * @method remove
				 * @param {Object} [args] Argument object
				 */
				remove : function(args) {
					return this.service.deleteFile(this.getId(), args);
				},
				/**
				 * Update the Atom document representation of the metadata for the file
				 * @method update
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				update : function(args) {
					return this.service.updateFileMetadata(this, args);
				}

			});

			/**
			 * Callbacks used when reading a feed that contains File entries.
			 */
			var FileFeedCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.FileFeedXPath
					});
				},
				createEntity : function(service, data, response) {
					var entry = null;
					if (typeof data == "object") {
						entry = data;
					} else {
						var feedHandler = new XmlDataHandler({
							data : data,
							namespaces : consts.Namespaces,
							xpath : consts.FileXPath
						});
						entry = feedHandler.data;
					}
					var entryHandler = new XmlDataHandler({
						data : entry,
						namespaces : consts.Namespaces,
						xpath : consts.FileXPath
					});
					return new File({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/**
			 * Callbacks used when reading a feed that contains File Comment entries.
			 */
			var CommentCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.CommentFeedXPath
					});
				},
				createEntity : function(service, data, response) {
					var entry = null;
					if (typeof data == "object") {
						entry = data;
					} else {
						var feedHandler = new XmlDataHandler({
							data : data,
							namespaces : consts.Namespaces,
							xpath : consts.CommentXPath
						});
						entry = feedHandler.data;
					}
					var entryHandler = new XmlDataHandler({
						data : entry,
						namespaces : consts.Namespaces,
						xpath : consts.CommentXPath
					});
					return new Comment({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/**
			 * FileService class.
			 * 
			 * @class FileService
			 * @namespace sbt.connections
			 */
			var FileService = declare(BaseService, {				

				/**
				 * Constructor for FileService
				 * 
				 * @constructor
				 * @param args
				 */
				constructor : function(args) {
					var endpointName = args ? (args.endpoint ? args.endpoint : this.getDefaultEndpointName()) : this.getDefaultEndpointName();
					if (!this.endpoint) {						
						this.endpoint = config.findEndpoint(endpointName);
					}
				},

				/**
				 * Return the default endpoint name if client did not specify one.
				 * @returns {String}
				 */
				getDefaultEndpointName : function() {
                    return "connections";
				},

				/**
				 * Callbacks used when reading a feed that contains File entries.
				 */
				getFileFeedCallbacks : function() {
					return FileFeedCallbacks;
				},

				/**
				 * Callbacks used when reading a feed that contains File Comment entries.
				 */
				getCommentFeedCallbacks : function() {
					return CommentCallbacks;
				},

				/**
				 * Returns a File instance from File or JSON or String. Throws an error if the argument was neither.
				 * @param {Object} fileOrJsonOrString The file Object or json String for File
				 */
				newFile : function(fileOrJsonOrString) {
					if (fileOrJsonOrString instanceof File) {
						return fileOrJsonOrString;
					} else {
						if (lang.isString(fileOrJsonOrString)) {
							fileOrJsonOrString = {
								id : fileOrJsonOrString
							};
						}
						return new File({
							service : this,
							_fields : lang.mixin({}, fileOrJsonOrString)
						});
					}
				},

				/**
				 * Loads File with the ID passed
				 * @method getFile
				 * @param {String} FileId the Id of the file to be loaded
				 * @param {Object} [args] Argument object
				 */
				getFile : function(fileId, args) {
					var file = this.newFile({
						id : fileId
					});
					return file.load(args);

				},

				/**
				 * Get my files from IBM Connections
				 * 
				 * @method getMyFiles
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				getMyFiles : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomFilesMy, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get files shared with logged in user from IBM Connections
				 * 
				 * @method getFilesSharedWithMe
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				getFilesSharedWithMe : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						request : args || {},
						query : lang.mixin({
							direction : "inbound"
						}, args ? args : {})
					};

					return this.getEntities(consts.AtomFilesShared, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get files shared by the logged in user from IBM Connections
				 * 
				 * @method getFilesSharedByMe
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				getFilesSharedByMe : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						request : args || {},
						query : lang.mixin({
							direction : "outbound"
						}, args ? args : {})
					};

					return this.getEntities(consts.AtomFilesShared, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get public files from IBM Connections
				 * 
				 * @method getPublicFiles
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				getPublicFiles : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						request : args || {},
						query : args || {}
					};

					return this.getEntities(consts.AtomFilesPublic, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get my folders from IBM Connections
				 * 
				 * @method getMyFolders
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				getMyFolders : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomFoldersMy, options, this.getFileFeedCallbacks());
				},
				/**
				 * A feed of comments associated with files to which you have access. You must authenticate this request.
				 * 
				 * @method getMyFolders
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				getMyFileComments : function(userId, fileId, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("userId", userId);
					}
					if (promise) {
						return promise;
					}
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					var url = this.constructUrl(consts.AtomFileCommentsMy, null, {
						userid : userId,
						documentId : fileId
					});
					return this.getEntities(url, options, this.getCommentFeedCallbacks());
				},
				/**
				 * A feed of comments associated with all public files. Do not authenticate this request.
				 * 
				 * @method getPublicFileComments
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				getPublicFileComments : function(userId, fileId, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("userId", userId);
					}
					if (promise) {
						return promise;
					}

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					var url = this.constructUrl(consts.AtomFileCommentsPublic, null, {
						userid : userId,
						documentId : fileId
					});
					return this.getEntities(url, options, this.getCommentFeedCallbacks());
				},

				/**
				 * Adds a comment to the specified file.
				 * 
				 * @method addCommentToFile
				 * @param {String} [userId] the userId for the author
				 * @param {String} fileId the ID of the file
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				addCommentToFile : function(userId, fileId, comment, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("comment", comment);
					}
					if (promise) {
						return promise;
					}
					var options = {
						method : "POST",
						query : args || {},
						headers : consts.AtomXmlHeaders,
						data : this._constructPayloadForComment(false, comment)
					};
					var url = null;

					if (!userId) {
						url = this.constructUrl(consts.AtomAddCommentToMyFile, null, {
							documentId : fileId
						});
					} else {
						url = this.constructUrl(consts.AtomAddCommentToFile, null, {
							userid : userId,
							documentId : fileId
						});
					}
					return this.updateEntity(url, options, this.getCommentFeedCallbacks());
				},

				/**
				 * Adds a comment to the specified file of logged in user.
				 * 
				 * @method addCommentToMyFile
				 * @param {String} fileId the ID of the file
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				addCommentToMyFile : function(fileId, comment, args) {
					return this.addCommentToFile(null, fileId, comment, args);
				},

				/**
				 * Update the Atom document representation of the metadata for a file from logged in user's library.
				 * @method updateFileMetadata
				 * @param {Object} fileOrJson file or json representing the file to be updated
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are supported by IBM Connections like ps,
				 * sortBy etc.
				 */
				updateFileMetadata : function(fileOrJson, args) {

					var promise = this.validateField("fileOrJson", fileOrJson);
					if (promise) {
						return promise;
					}

					var file = this.newFile(fileOrJson);
					var options = {
						method : "PUT",
						query : args || {},
						headers : consts.AtomXmlHeaders,
						data : this._constructPayload(file._fields, file.getId())
					};

					var url = this.constructUrl(consts.AtomUpdateFileMetadata, null, {
						documentId : file.getId()
					});
					return this.updateEntity(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Pin a file, by sending a POST request to the myfavorites feed.
				 * @method pinFile
				 * @param {String} fileId ID of file which needs to be pinned
				 * @param {Object} [args] Argument object.
				 */
				pinFile : function(fileId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = fileId;

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(consts.AtomPinFile, options, callbacks);

				},

				/**
				 * Unpin a file, by sending a DELETE request to the myfavorites feed.
				 * @method unpinFile
				 * @param {String} fileId ID of file which needs to be unpinned
				 * @param {Object} [args] Argument object.
				 */
				unpinFile : function(fileId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = fileId;

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					return this.deleteEntity(consts.AtomPinFile, options, fileId);

				},

				/**
				 * Add a file or files to a folder.
				 * 
				 * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file to a folder you must be an editor of
				 * the folder.
				 * 
				 * @method addFilesToFolder
				 * @param {String} folderId the Id of the folder
				 * @param {List} fileIds list of file Ids to be added to the folder
				 * @param {Object} [args] Argument object.
				 */
				addFilesToFolder : function(fileIds, folderId, args) {

					var promise = this.validateField("fileIds", fileIds);
					if (!promise) {
						promise = this.validateField("folderId", folderId);
					}

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomAddFilesToFolder, null, {
						collectionId : folderId
					});

					var char = "?";
					for ( var counter in fileIds) {
						url += char + "itemId=" + fileIds[counter];
						char = "&";
					}

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(url, options, callbacks);

				},

				/**
				 * Gets the files pinned by the logged in user.
				 * @method getMyPinnedFiles
				 * @param {Object} [args] Argument object for the additional parameters like pageSize etc.
				 */
				getMyPinnedFiles : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomFilesMyPinned, options, this.getFileFeedCallbacks());
				},

				/**
				 * Delete a file.
				 * @method deleteFile
				 * @param {String} fileId Id of the file which needs to be deleted
				 * @param {Object} [args] Argument object
				 */
				deleteFile : function(fileId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomDeleteFile, null, {
						documentId : fileId
					});

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * Lock a file
				 * @method lockFile
				 * @param {String} fileId Id of the file which needs to be locked
				 * @param {Object} [args] Argument object
				 */
				lockFile : function(fileId, args) {
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["type"] = "HARD";

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomLockUnlockFile, parameters, {
						documentId : fileId
					});

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(url, options, callbacks, args);
				},

				/**
				 * unlock a file
				 * @method lockFile
				 * @param {String} fileId Id of the file which needs to be unlocked
				 * @param {Object} [args] Argument object
				 */
				unlockFile : function(fileId, args) {
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["type"] = "NONE";

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomLockUnlockFile, parameters, {
						documentId : fileId
					});

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(url, options, callbacks, args);
				},

				/**
				 * Uploads a new file for logged in user.
				 * @method uploadFile
				 * @param {Object} [args] Argument object
				 * @param {Objecr} [args.fileControlId] The Id of html control
				 * @param {Object} [args.fileControl] The html control
				 * @param {Function} [args.load] The callback function will invoke when the file is uploaded successfully. The function expects one parameter, the status of upload.
				 * @param {Function} [args.error] Sometimes the upload calls fails due to bad request (400 error). The error parameter is a callback function that is only invoked when an error occurs. This
				 * allows to write logic when an error occurs. The parameter passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can access
				 * the javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to upload the file completes or fails. The parameter passed to this callback is the FileEntry
				 * object (or error object). From the error object. one can get access to the javascript library error object, the status code and the error message.
				 * @param {Object} [args.parameters] The additional parameters
				 */
				uploadFile : function(fileControlOrId, args) {
					if (!fileControlOrId) {
						util.notifyError({
							message : "fileControlOrId is not provided"
						}, args);
						return;
					}
					if (!window.FileReader) {
						util.notifyError({
							message : "HTML 5 File Control is not supported by the Browser."
						}, args);
						return;
					}
					var files = null;
					var filePath = null;
					if (typeof fileControlOrId == "string") {
						var fileControl = document.getElementById(fileControlOrId);
						filePath = fileControl.value;
						files = fileControl.files;
					} else if (typeof fileControlOrId == "object") {
						filePath = fileControlOrId.value;
						files = fileControlOrId.files;
					} else {
						util.notifyError("File Control or ID is required", args);
					}
					var index = filePath.lastIndexOf("\\");
					if (index == -1) {
						index = filePath.lastIndexOf("/");
					}

					var reader = new FileReader();
					var self = this;
					var fileObject = files[0];
					reader.onload = function(event) {
						var index = filePath.lastIndexOf("\\");
						if (index == -1) {
							index = filePath.lastIndexOf("/");
						}
						var fileName = filePath.substring(index + 1);

						var dashes = '--';
						var boundary = 'sdkfileupload' + (new Date()); 
						var crlf = "\r\n";

						var filetype;

						//Post with the correct MIME type (If the OS can identify one)
						if (fileObject.type == '') {
							filetype = 'application/octet-stream';
						} else {
							filetype = fileObject.type;
						}						
						var data = dashes + boundary + crlf + "Content-Disposition: form-data;" + "name=\"file\";" + "filename=\"" + unescape(encodeURIComponent(fileName)) + "\"" + crlf + "Content-Type: "
								+ filetype + crlf + crlf + event.target.result.replace("data:" + filetype + ";base64,", "") + crlf + dashes + boundary + dashes;

						self.uploadFileBinary(data, fileName, boundary, args);

					};
					reader.onerror = function(error) {
						util.notifyError(error, args);
					};
					reader.readAsDataURL(fileObject);
				},

				/**
				 * Uploads a new file for logged in user.
				 * @method uploadFile
				 * @param {Object} binaryContent The binary content of the file
				 * @param {String} filename The name of the file
				 * @param {String} boundary The boundary of multipart form data
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is uploaded successfully. The function expects one parameter, the status of upload.
				 * @param {Function} [args.error] Sometimes the upload calls fails due to bad request (400 error). The error parameter is a callback function that is only invoked when an error occurs. This
				 * allows to write logic when an error occurs. The parameter passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can access
				 * the javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to upload the file completes or fails. The parameter passed to this callback is the FileEntry
				 * object (or error object). From the error object. one can get access to the javascript library error object, the status code and the error message.
				 * @param {String} [args.fileName] The file name
				 * @param {Object} [args.parameters] The additional parameters
				 */
				uploadFileBinary : function(binaryContent, fileName, boundary, args) {

					if (!binaryContent) {
						util.notifyError({
							message : "Binary Content is not provided"
						}, args);
						return;
					}
					if (!fileName) {
						util.notifyError({
							message : "FileName is not provided"
						}, args);
						return;
					}
					// /files/<<endpointName>>/<<serviceType>>/fileName eg. /files/smartcloud/connections/fileName?args
					url = this.constructUrl(config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/" + encodeURIComponent(fileName), args && args.parameters ? args.parameters : {});
					var headers = {
						"Content-Type" : "multipart/form-data;boundary=" + boundary
					};
					var self = this;
					this.endpoint.xhrPost({
						url : url,
						postData : binaryContent,
						headers : headers,
						load : function(data) {
							var file = self.getFileFeedCallbacks().createEntity(this, data);
							if (args) {
								if (args.load)
									args.load(file);
								else if (args.handle)
									args.handle(file);
							} else {
								log.error("Callbacks not defined. Return Value={0}", param);
							}
						},
						error : function(error) {
							util.notifyError(error, args);
						}
					});
				},
				
				_constructPayloadForComment : function(isDelete, comment) {
					var payload = "<entry xmlns=\"http://www.w3.org/2005/Atom\">";
					payload += "<category term=\"comment\" label=\"comment\" scheme=\"tag:ibm.com,2006:td/type\"/>";

					if (isDelete == true)
						payload += "<deleteWithRecord xmlns=\"" + consts.Namespaces.td + "\">false</deleteWithRecord>";
					else
						payload += "<content type=\"text/plain\">" + xml.encodeXmlEntry(comment) + "</content>";
					payload += "</entry>";
					return payload;
				},

				_constructPayload : function(payloadMap, documentId) {
					var payload = "<entry xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"document\" label=\"document\" scheme=\"tag:ibm.com,2006:td/type\"></category>";
					payload += "<id>urn:lsid:ibm.com:td:" + xml.encodeXmlEntry(documentId) + "</id>";
					if (payloadMap) {
						for (key in payloadMap) {
							var value = payloadMap[key];
							if (key == "label") {
								payload += "<label xmlns=\"" + consts.Namespaces.td + "\">" + xml.encodeXmlEntry(value) + "</label>";
								payload += "<title>" + xml.encodeXmlEntry(value) + "</title>";
							} else if (key == "summary") {
								payload += "<summary type=\"text\">" + xml.encodeXmlEntry(value) + "</summary>";
							} else if (key == "visibility") {
								payload += "<visibility xmlns=\"" + consts.Namespaces.td + "\">" + xml.encodeXmlEntry(value) + "</visibility>";
							}
						}
					}

					payload += "</entry>";
					return payload;
				}				

			});
			return FileService;
		});
