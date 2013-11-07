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

define(
		[ "../declare", "../lang", "../stringUtil", "../Promise", "./FileConstants", "../base/BaseService", "../base/BaseEntity", "../base/XmlDataHandler",
				"../config", "../util", "../xml" ],
		function(declare, lang, stringUtil, Promise, consts, BaseService, BaseEntity, XmlDataHandler, config, util, xml) {

			var FolderTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\">${getCategory}${getId}${getFolderLabel}${getTitle}${getSummary}${getVisibility}${getVisibilityShare}</entry>";
			var FolderLabelTmpl = "<label xmlns=\"urn:ibm.com/td\" makeUnique=\"true\">${label}</label>";
			var FileVisibilityShareTmpl = "<sharedWith xmlns=\"urn:ibm.com/td\"> <member xmlns=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0\" ca:type=\"${shareWithWhat}\" xmlns:ca=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0\" ca:id=\"${shareWithId}\" ca:role=\"${shareWithRole}\"/> </sharedWith>";
			var FileFeedTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed xmlns=\"http://www.w3.org/2005/Atom\">${getEntries}</feed>";
			var FileEntryTmpl = "<entry xmlns=\"http://www.w3.org/2005/Atom\">${getCategory}${getId}${getUuid}${getLabel}${getTitle}${getSummary}${getVisibility}${getItem}${getTags}${getNotification}</entry>";
			var FileItemEntryTmpl = "<entry>${getCategory}${getItem}</entry>";
			var FileCommentsTmpl = "<entry xmlns=\"http://www.w3.org/2005/Atom\">${getCategory}${getDeleteComment}${getContent}</entry>";
			var FileCategoryTmpl = "<category term=\"${category}\" label=\"${category}\" scheme=\"tag:ibm.com,2006:td/type\"/>";
			var FileContentTmpl = "<content type=\"text/plain\">${content}</content>";
			var FileDeleteCommentTmpl = "<deleteWithRecord xmlns=\"urn:ibm.com/td\">${deleteWithRecord}</deleteWithRecord>";
			var FileIdTmpl = "<id>${id}</id>";
			var FileUuidTmpl = "<uuid xmlns=\"urn:ibm.com/td\">${uuid}</uuid>";
			var FileLabelTmpl = "<label xmlns=\"urn:ibm.com/td\">${label}</label>";
			var FileTitleTmpl = "<title>${title}</title>";
			var FileSummaryTmpl = "<summary type=\"text\">${summary}</summary>";
			var FileVisibilityTmpl = "<visibility xmlns=\"urn:ibm.com/td\">${visibility}</visibility>";
			var FileItemIdTmpl = "<itemId xmlns=\"urn:ibm.com/td\">${fileId}</itemId>";
			var TagsTmpl = "<category term=\"${tag}\" /> ";
			var NotificationTmpl = "<td:notification>${notification}</td:notification>";

			/**
			 * Comment class associated with a file comment.
			 * 
			 * @class Comment
			 * @namespace sbt.connections
			 */
			var Comment = declare(BaseEntity, {

				/**
				 * Returned the Comment Id
				 * 
				 * @method getCommentId
				 * @rturns {String} File Id
				 */
				getCommentId : function() {
					return this.id || this.getAsString("uid");
				},
				/**
				 * Returns Comment Title
				 * 
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},
				/**
				 * Returns the Comment Content
				 * 
				 * @method getContent
				 * @returns {String} content
				 */
				getContent : function() {
					return this.getAsString("content");
				},
				/**
				 * Returns The create Date
				 * 
				 * @method getCreated
				 * @returns {Date} create Date
				 */
				getCreated : function() {
					return this.getAsDate("created");
				},
				/**
				 * Returns The modified Date
				 * 
				 * @method getModified
				 * @returns {Date} modified Date
				 */
				getModified : function() {
					return this.getAsDate("modified");
				},
				/**
				 * Returns the version label
				 * 
				 * @method getVersionLabel
				 * @returns {String} version label
				 */
				getVersionLabel : function() {
					return this.getAsString("versionLabel");
				},
				/**
				 * Returns the updated Date
				 * 
				 * @method getModified
				 * @returns {Date} modified Date
				 */
				getUpdated : function() {
					return this.getAsDate("updated");
				},
				/**
				 * Returns the published Date
				 * 
				 * @method getPublished
				 * @returns {Date} modified Date
				 */
				getPublished : function() {
					return this.getAsDate("published");
				},
				/**
				 * Returns the modifier
				 * 
				 * @method getModifier
				 * @returns {Object} modifier
				 */
				getModifier : function() {
					return this.getAsObject([ "modifierName", "modifierUserId", "modifierEmail", "modifierUserState" ]);
				},
				/**
				 * Returns the author
				 * 
				 * @method getAuthor
				 * @returns {Object} author
				 */
				getAuthor : function() {
					return this.getAsObject([ "authorName", "authorUserId", "authorEmail", "authorUserState" ]);
				},
				/**
				 * Returns the language
				 * 
				 * @method getLanguage
				 * @returns {String} language
				 */
				getLanguage : function() {
					return this.getAsString("language");
				},
				/**
				 * Returns the flag for delete with record
				 * 
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
				 * 
				 * @method getFileId
				 * @returns {String} file Id
				 */
				getFileId : function() {
					return this.id || this._fields.id || this.getAsString("uid");
				},
				/**
				 * Returns the label
				 * 
				 * @method getLabel
				 * @returns {String} label
				 */
				getLabel : function() {
					return this.getAsString("label");
				},
				/**
				 * Returns the self URL
				 * 
				 * @method getSelfUrl
				 * @returns {String} self URL
				 */
				getSelfUrl : function() {
					return this.getAsString("selfUrl");
				},
				/**
				 * Returns the alternate URL
				 * 
				 * @method getAlternateUrl
				 * @returns {String} alternate URL
				 */
				getAlternateUrl : function() {
					return this.getAsString("alternateUrl");
				},
				/**
				 * Returns the download URL
				 * 
				 * @method getDownloadUrl
				 * @returns {String} download URL
				 */
				getDownloadUrl : function() {
					return config.Properties.serviceUrl + "/files/" + this.service.endpoint.proxyPath + "/" + "connections" + "/" + "DownloadFile" + "/"
							+ this.getFileId() + "/" + this.getLibraryId();
					;
				},
				/**
				 * Returns the type
				 * 
				 * @method getType
				 * @returns {String} type
				 */
				getType : function() {
					return this.getAsString("type");
				},
				/**
				 * Returns the Category
				 * 
				 * @method getCategory
				 * @returns {String} category
				 */
				getCategory : function() {
					return this.getAsString("category");
				},
				/**
				 * Returns the size
				 * 
				 * @method getSize
				 * @returns {Number} length
				 */
				getSize : function() {
					return this.getAsNumber("length");
				},
				/**
				 * Returns the Edit Link
				 * 
				 * @method getEditLink
				 * @returns {String} edit link
				 */
				getEditLink : function() {
					return this.getAsString("editLink");
				},
				/**
				 * Returns the Edit Media Link
				 * 
				 * @method getEditMediaLink
				 * @returns {String} edit media link
				 */
				getEditMediaLink : function() {
					return this.getAsString("editMediaLink");
				},
				/**
				 * Returns the Thumbnail URL
				 * 
				 * @method getThumbnailUrl
				 * @returns {String} thumbnail URL
				 */
				getThumbnailUrl : function() {
					return this.getAsString("thumbnailUrl");
				},
				/**
				 * Returns the Comments URL
				 * 
				 * @method getCommentsUrl
				 * @returns {String} comments URL
				 */
				getCommentsUrl : function() {
					return this.getAsString("commentsUrl");
				},
				/**
				 * Returns the author
				 * 
				 * @method getAuthor
				 * @returns {Object} author
				 */
				getAuthor : function() {
					return this.getAsObject([ "authorName", "authorUserId", "authorEmail", "authorUserState" ]);
				},
				/**
				 * Returns the Title
				 * 
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},
				/**
				 * Returns the published date
				 * 
				 * @method getPublished
				 * @returns {Date} published date
				 */
				getPublished : function() {
					return this.getAsDate("published");
				},
				/**
				 * Returns the updated date
				 * 
				 * @method getUpdated
				 * @returns {Date} updated date
				 */
				getUpdated : function() {
					return this.getAsDate("updated");
				},
				/**
				 * Returns the created date
				 * 
				 * @method getCreated
				 * @returns {Date} created date
				 */
				getCreated : function() {
					return this.getAsDate("created");
				},
				/**
				 * Returns the modified date
				 * 
				 * @method getModified
				 * @returns {Date} modified date
				 */
				getModified : function() {
					return this.getAsDate("modified");
				},
				/**
				 * Returns the last accessed date
				 * 
				 * @method getLastAccessed
				 * @returns {Date} last accessed date
				 */
				getLastAccessed : function() {
					return this.getAsDate("lastAccessed");
				},
				/**
				 * Returns the modifier
				 * 
				 * @method getModifier
				 * @returns {Object} modifier
				 */
				getModifier : function() {
					return this.getAsObject([ "modifierName", "modifierUserId", "modifierEmail", "modifierUserState" ]);
				},
				/**
				 * Returns the visibility
				 * 
				 * @method getVisibility
				 * @returns {String} visibility
				 */
				getVisibility : function() {
					return this.getAsString("visibility");
				},
				/**
				 * Returns the library Id
				 * 
				 * @method getLibraryId
				 * @returns {String} library Id
				 */
				getLibraryId : function() {
					return this.getAsString("libraryId");
				},
				
				/**
				 * Sets the library Id
				 * 
				 * @method setLibraryId
				 * @param libaryId
				 */
				setLibraryId : function(libraryId) {
					return this.setAsString("libraryId", libraryId);
				},
				/**
				 * Returns the library Type
				 * 
				 * @method getLibraryType
				 * @returns {String} library Type
				 */
				getLibraryType : function() {
					return this.getAsString("libraryType");
				},
				/**
				 * Returns the version Id
				 * 
				 * @method getVersionUuid
				 * @returns {String} version Id
				 */
				getVersionUuid : function() {
					return this.getAsString("versionUuid");
				},
				/**
				 * Returns the version label
				 * 
				 * @method getVersionLabel
				 * @returns {String} version label
				 */
				getVersionLabel : function() {
					return this.getAsString("versionLabel");
				},
				/**
				 * Returns the propagation
				 * 
				 * @method getPropagation
				 * @returns {String} propagation
				 */
				getPropagation : function() {
					return this.getAsString("propagation");
				},
				/**
				 * Returns the recommendations Count
				 * 
				 * @method getRecommendationsCount
				 * @returns {Number} recommendations Count
				 */
				getRecommendationsCount : function() {
					return this.getAsNumber("recommendationsCount");
				},
				/**
				 * Returns the comments Count
				 * 
				 * @method getCommentsCount
				 * @returns {Number} comments Count
				 */
				getCommentsCount : function() {
					return this.getAsNumber("commentsCount");
				},
				/**
				 * Returns the shares Count
				 * 
				 * @method getSharesCount
				 * @returns {Number} shares Count
				 */
				getSharesCount : function() {
					return this.getAsNumber("sharesCount");
				},
				/**
				 * Returns the folders Count
				 * 
				 * @method getFoldersCount
				 * @returns {Number} folders Count
				 */
				getFoldersCount : function() {
					return this.getAsNumber("foldersCount");
				},
				/**
				 * Returns the attachments Count
				 * 
				 * @method getAttachmentsCount
				 * @returns {Number} attachments Count
				 */
				getAttachmentsCount : function() {
					return this.getAsNumber("attachmentsCount");
				},
				/**
				 * Returns the versions Count
				 * 
				 * @method getVersionsCount
				 * @returns {Number} versions Count
				 */
				getVersionsCount : function() {
					return this.getAsNumber("versionsCount");
				},
				/**
				 * Returns the references Count
				 * 
				 * @method getReferencesCount
				 * @returns {Number} references Count
				 */
				getReferencesCount : function() {
					return this.getAsNumber("referencesCount");
				},
				/**
				 * Returns the total Media Size
				 * 
				 * @method getTotalMediaSize
				 * @returns {Number} total Media Size
				 */
				getTotalMediaSize : function() {
					return this.getAsNumber("totalMediaSize");
				},
				/**
				 * Returns the Summary
				 * 
				 * @method getSummary
				 * @returns {String} Summary
				 */
				getSummary : function() {
					return this.getAsString("summary");
				},
				/**
				 * Returns the Content URL
				 * 
				 * @method getContentUrl
				 * @returns {String} Content URL
				 */
				getContentUrl : function() {
					return this.getAsString("contentUrl");
				},
				/**
				 * Returns the Content Type
				 * 
				 * @method getContentType
				 * @returns {String} Content Type
				 */
				getContentType : function() {
					return this.getAsString("contentType");
				},
				/**
				 * Returns the objectTypeId
				 * 
				 * @method getObjectTypeId
				 * @returns {String} objectTypeId
				 */
				getObjectTypeId : function() {
					return this.getAsString("objectTypeId");
				},
				/**
				 * Returns the lock state
				 * 
				 * @method getLockType
				 * @returns {String} lock state
				 */
				getLockType : function() {
					return this.getAsString("lock");
				},
				/**
				 * Returns the permission ACLs
				 * 
				 * @method getAcls
				 * @returns {String} ACLs
				 */
				getAcls : function() {
					return this.getAsString("acls");
				},
				/**
				 * Returns the hit count
				 * 
				 * @method getHitCount
				 * @returns {Number} hit count
				 */
				getHitCount : function() {
					return this.getAsNumber("hitCount");
				},
				/**
				 * Returns the anonymous hit count
				 * 
				 * @method getAnonymousHitCount
				 * @returns {Number} anonymous hit count
				 */
				getAnonymousHitCount : function() {
					return this.getAsNumber("anonymousHitCount");
				},
				/**
				 * Returns the tags
				 * 
				 * @method getTags
				 * @returns {Array} tags
				 */
				getTags : function() {
					return this.getAsArray("tags");
				},
				/**
				 * Returns the tags
				 * 
				 * @method setTags
				 * @param {Array} tags
				 */
				setTags : function(tags) {
					return this.setAsArray("tags", tags);
				},
				/**
				 * Sets the label
				 * 
				 * @method setLabel
				 * @param {String} label
				 */
				setLabel : function(label) {
					return this.setAsString("label", label);
				},
				/**
				 * Sets the summary
				 * 
				 * @method setSummary
				 * @param {String} summary
				 */
				setSummary : function(summary) {
					return this.setAsString("summary", summary);
				},
				/**
				 * Sets the visibility
				 * 
				 * @method setVisibility
				 * @param {String} visibility
				 */
				setVisibility : function(visibility) {
					return this.setAsString("visibility", visibility);
				},

				/**
				 * Sets Indicator whether the currently authenticated user wants to receive notifications as people edit the document. Options are on or off.
				 * @param {Boolean} notification
				 * @returns
				 */
				setNotification : function(notification) {
					return this.setAsBoolean("notification", notification ? "on" : "off");
				},
				/**
				 * Loads the file object with the atom entry associated with the file. By default, a network call is made to load the atom entry document in the
				 * file object.
				 * 
				 * @method load
				 * @param {Object} [args] Argument object
				 * @param {Boolean} [isPublic] Optinal flag to indicate whether to load public file which does not require authentication
				 */
				load : function(args, isPublic, url) {
					// detect a bad request by validating required arguments
					var fileUuid = this.getFileId();
					var promise = this.service.validateField("fileId", fileUuid);
					if (promise) {
						return promise;
					}
					if(isPublic) {
						promise = this.service.validateField("libraryId", this.getLibraryId());
						if (promise) {
							return promise;
						}
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
						// handleAs : "text",
						query : requestArgs
					};

					if (!url) {						
						if (isPublic) {
							url = this.service.constructUrl(consts.AtomFileInstancePublic, null, {
								"documentId" : fileUuid,
								"libraryId" : this.getLibraryId()
							});
						}
						else {
							url = this.service.constructUrl(consts.AtomFileInstance, null, {
								"documentId" : fileUuid							
							});
						}
					}
					return this.service.getEntity(url, options, fileUuid, callbacks);
				},
				/**
				 * Save this file
				 * 
				 * @method save
				 * @param {Object} [args] Argument object
				 */
				save : function(args) {
					if (this.getFileId()) {
						return this.service.updateFileMetadata(this, args);
					}
				},
				/**
				 * Adds a comment to the file.
				 * 
				 * @method addComment
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				addComment : function(comment, args) {
					return this.service.addCommentToFile(this.getAuthor().authorUserId, this.getFileId(), comment, args);
				},
				/**
				 * Pin th file, by sending a POST request to the myfavorites feed.
				 * 
				 * @method pin
				 * @param {Object} [args] Argument object.
				 */
				pin : function(args) {
					return this.service.pinFile(this.getFileId(), args);
				},
				/**
				 * Unpin the file, by sending a DELETE request to the myfavorites feed.
				 * 
				 * @method unPin
				 * @param {Object} [args] Argument object.
				 */
				unpin : function(args) {
					return this.service.unpinFile(this.getFileId(), args);
				},
				/**
				 * Lock the file
				 * 
				 * @method lock
				 * @param {Object} [args] Argument object
				 */
				lock : function(args) {
					return this.service.lockFile(this.getFileId(), args);
				},
				/**
				 * UnLock the file
				 * 
				 * @method unlock
				 * @param {Object} [args] Argument object
				 */
				unlock : function(args) {
					return this.service.unlockFile(this.getFileId(), args);
				},
				/**
				 * Deletes the file.
				 * 
				 * @method remove
				 * @param {Object} [args] Argument object
				 */
				remove : function(args) {
					return this.service.deleteFile(this.getFileId(), args);
				},
				/**
				 * Update the Atom document representation of the metadata for the file
				 * 
				 * @method update
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				update : function(args) {
					return this.service.updateFileMetadata(this, args);
				},
				/**
				 * Downloads the file
				 * 
				 * @method download
				 */
				download : function() {
					return this.service.downloadFile(this.getFileId(), this.getLibraryId());
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

				contextRootMap : {
					files : "files"
				},

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
				 * 
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
				 * 
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
				 * 
				 * @method getFile
				 * @param {String} fileId the Id of the file to be loaded
				 * @param {Object} [args] Argument object
				 */
				getFile : function(fileId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var file = this.newFile({
						id : fileId
					});
					return file.load(args);

				},

				/**
				 * Loads Community File 
				 * 
				 * @method getFile
				 * @param {String} fileId the Id of the file to be loaded
				 * @param {String} communityId the Id of the community to which it belongs
				 * @param {Object} [args] Argument object
				 */
				getCommunityFile : function(fileId, communityId, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("communityId", communityId);
					}
					if (promise) {
						return promise;
					}
					var file = this.newFile({
						id : fileId
					});
					var url = this.constructUrl(consts.AtomGetCommunityFile, null, {
						communityId : communityId,
						documentId : file.getFileId()
					});
					return file.load(args, null, url);

				},
				/**
				 * Get my files from IBM Connections
				 * 
				 * @method getMyFiles
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
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
				 * Get community files from IBM Connections (community files refer to
				 * files which the user uploaded to the community. Calling this function
				 * will not list files that have been shared with this community).
				 * 
				 * @method getCommunityFiles
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getCommunityFiles : function(communityId, args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					var url = this.constructUrl(consts.AtomGetAllFilesInCommunity, null, {
						communityId : communityId
					});

					return this.getEntities(url, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get files shared with logged in user from IBM Connections
				 * 
				 * @method getFilesSharedWithMe
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getFilesSharedWithMe : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",

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
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getFilesSharedByMe : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",

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
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getPublicFiles : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {},
						headers : {}
					};

					return this.getEntities(consts.AtomFilesPublic, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get my folders from IBM Connections
				 * 
				 * @method getMyFolders
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
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
				 * @method getMyFileComments
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getMyFileComments : function(userId, fileId, args) {

					return this.getFileComments(fileId, userId, false, null, args);
				},
				/**
				 * A feed of comments associated with all public files. Do not authenticate this request.
				 * 
				 * @method getPublicFileComments
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getPublicFileComments : function(userId, fileId, args) {

					return this.getFileComments(fileId, userId, true, null, args);
				},

				/**
				 * Adds a comment to the specified file.
				 * 
				 * @method addCommentToFile
				 * @param {String} [userId] the userId for the author
				 * @param {String} fileId the ID of the file
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				addCommentToFile : function(userId, fileId, comment, url, args) {
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

					if (!url) {
						if (!userId) {
							url = this.constructUrl(consts.AtomAddCommentToMyFile, null, {
								documentId : fileId
							});
						} else {
							url = this.constructUrl(consts.AtomAddCommentToFile, null, {
								userId : userId,
								documentId : fileId
							});
						}
					}
					return this.updateEntity(url, options, this.getCommentFeedCallbacks());
				},

				/**
				 * Adds a comment to the specified file of logged in user.
				 * 
				 * @method addCommentToMyFile
				 * @param {String} fileId the ID of the file
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				addCommentToMyFile : function(fileId, comment, args) {
					return this.addCommentToFile(null, fileId, comment, null, args);
				},

				/**
				 * Method to add comments to a Community file <p> Rest API used : /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
				 * 
				 * @method addCommentToCommunityFile
				 * @param {String} fileId
				 * @param {String} comment
				 * @param {String} communityId
				 * @param {Object} [args]
				 * @return {Comment} comment
				 */
				addCommentToCommunityFile : function(fileId, comment, communityId, args) {
					var url = this.constructUrl(consts.AtomAddCommentToCommunityFile, null, {
						communityId : communityId,
						documentId : fileId
					});
					return this.addCommentToFile(null, fileId, comment, url, args);
				},

				/**
				 * Update the Atom document representation of the metadata for a file from logged in user's library.
				 * 
				 * @method updateFileMetadata
				 * @param {Object} fileOrJson file or json representing the file to be updated
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				updateFileMetadata : function(fileOrJson, url, args) {

					var promise = this.validateField("fileOrJson", fileOrJson);
					if (promise) {
						return promise;
					}

					var file = this.newFile(fileOrJson);
					var options = {
						method : "PUT",
						query : args || {},
						headers : consts.AtomXmlHeaders,
						data : this._constructPayload(file._fields, file.getFileId())
					};

					if (!url) {
						url = this.constructUrl(consts.AtomUpdateFileMetadata, null, {
							documentId : file.getFileId()
						});
					}
					return this.updateEntity(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Method to update Community File's Metadata <p> Rest API used : /files/basic/api/library/<libraryId>/document/<fileId>/entry <p>
				 * @method updateCommunityFileMetadata
				 * @param {Object} fileOrJson
				 * @param {String} libraryId
				 * @param {Object} [args]
				 * @return {File}
				 */
				updateCommunityFileMetadata : function(fileOrJson, communityId, args) {
					var promise = this.validateField("fileOrJson", fileOrJson);
					if (promise) {
						return promise;
					}
					var file = this.newFile(fileOrJson);
					promise = new Promise();
					var _this = this;
					var update = function() {
						var url = _this.constructUrl(consts.AtomUpdateCommunityFileMetadata, null, {
							libraryId : file.getLibraryId(),
							documentId : file.getFileId()
						});
						_this.updateFileMetadata(file, url, args).then(function(file) {
							promise.fulfilled(file);
						}, function(error) {
							promise.rejected(error);
						});
					};
					if (file.isLoaded()) {
						update();
					} else {
						var url = _this.constructUrl(consts.AtomGetCommunityFile, null, {
							communityId : communityId,
							documentId : file.getFileId()
						});
						file.load(null, null, url).then(function() {
							update();
						}, function(error) {
							promise.rejected(error);
						});
					}
					return promise;
				},

				/**
				 * Pin a file, by sending a POST request to the myfavorites feed.
				 * 
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
						},
						query : parameters
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
				 * 
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
						},
						query : parameters
					};

					return this.deleteEntity(consts.AtomPinFile, options, fileId);

				},

				/**
				 * Add a file or files to a folder.
				 * 
				 * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file
				 * to a folder you must be an editor of the folder.
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

					var separatorChar = "?";
					for ( var counter in fileIds) {
						url += separatorChar + "itemId=" + fileIds[counter];
						separatorChar = "&";
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
				 * 
				 * @method getPinnedFiles
				 * @param {Object} [args] Argument object for the additional parameters like pageSize etc.
				 */
				getPinnedFiles : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomFilesMyPinned, options, this.getFileFeedCallbacks());
				},

				/**
				 * Delete a file.
				 * 
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
				 * 
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
				 * 
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
				 * 
				 * @method uploadFile
				 * @param {Object} fileControlOrId The Id of html control or the html control
				 * @param {Object} [args] The additional parameters for upload
				 */
				uploadFile : function(fileControlOrId, args) {

					var promise = this.validateField("File Control Or Id", fileControlOrId);
					if (promise) {
						return promise;
					}
					promise = this.validateHTML5FileSupport();
					if (promise) {
						return promise;
					}
					var files = null;
					if (typeof fileControlOrId == "string") {
						var fileControl = document.getElementById(fileControlOrId);
						filePath = fileControl.value;
						files = fileControl.files;
					} else if (typeof fileControlOrId == "object") {
						filePath = fileControlOrId.value;
						files = fileControlOrId.files;
					} else {
						return this.createBadRequestPromise("File Control or ID is required");
					}

					var file = files[0];
					var data = new FormData();
					data.append("file", file);

					return this.uploadFileBinary(data, file.name, args);
				},

				/**
				 * Uploads a new file for logged in user.
				 * 
				 * @method uploadFile
				 * @param {Object} binaryContent The binary content of the file
				 * @param {String} filename The name of the file
				 * @param {Object} [args] The additional parameters of metadata of file for upload like visibility, tag, etc.
				 */
				uploadFileBinary : function(binaryContent, fileName, args) {

					var promise = this.validateField("Binary Content", binaryContent);
					if (promise) {
						return promise;
					}
					promise = this.validateField("File Name", fileName);
					if (promise) {
						return promise;
					}
					if (util.getJavaScriptLibrary().indexOf("Dojo 1.4.3") != -1) {
						return this.createBadRequestPromise("Dojo 1.4.3 is not supported for File upload");
					}
					// /files/<<endpointName>>/<<serviceType>>/<<operation>>/fileName eg. /files/smartcloud/connections/UploadFile/fileName?args
					var url = this.constructUrl(config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/" + "UploadFile"
							+ "/" + encodeURIComponent(fileName), args && args.parameters ? args.parameters : {});
					var headers = {
						"Content-Type" : false,
						"Process-Data" : false // processData = false is reaquired by jquery
					};
					var options = {
						method : "POST",
						headers : headers,
						query : args || {},
						data : binaryContent
					};

					return this.updateEntity(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Upload new version of a file.
				 * 
				 * @method uploadNewVersion
				 * @param {Object} fileId The ID of the file
				 * @param {Object} fileControlOrId The Id of html control or the html control
				 * @param {Object} [args] The additional parameters ffor updating file metadata
				 */
				uploadNewVersion : function(fileId, fileControlOrId, args) {

					var promise = this.validateField("File Control Or Id", fileControlOrId);
					if (!promise) {
						promise = this.validateField("File ID", fileId);
					}
					if (promise) {
						return promise;
					}
					promise = this.validateHTML5FileSupport();
					if (promise) {
						return promise;
					}
					var files = null;
					if (typeof fileControlOrId == "string") {
						var fileControl = document.getElementById(fileControlOrId);
						filePath = fileControl.value;
						files = fileControl.files;
					} else if (typeof fileControlOrId == "object") {
						filePath = fileControlOrId.value;
						files = fileControlOrId.files;
					} else {
						return this.createBadRequestPromise("File Control or ID is required");
					}

					var file = files[0];
					var data = new FormData();
					data.append("file", file);

					return this.uploadNewVersionBinary(data, fileId, file.name, args);
				},

				/**
				 * Uploads new Version of a File.
				 * 
				 * @method uploadNewVersionBinary
				 * @param {Object} binaryContent The binary content of the file
				 * @param {String} fileId The ID of the file
				 * @param {Object} [args] The additional parameters for upding file metadata
				 */
				uploadNewVersionBinary : function(binaryContent, fileId, fileName, args) {

					var promise = this.validateField("Binary Content", binaryContent);
					if (promise) {
						return promise;
					}
					promise = this.validateField("File ID", fileId);
					if (promise) {
						return promise;
					}
					promise = this.validateField("File Name", fileName);
					if (promise) {
						return promise;
					}
					if (util.getJavaScriptLibrary().indexOf("Dojo 1.4.3") != -1) {
						return this.createBadRequestPromise("Dojo 1.4.3 is not supported for File Upload");
					}
					// /files/<<endpointName>>/<<serviceType>>/<<operation>>/fileId?args eg./files/smartcloud/connections/UpdateFile/fileId/fileName?args
					var url = this.constructUrl(config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/"
							+ "UploadNewVersion" + "/" + encodeURIComponent(fileId) + "/" + encodeURIComponent(fileName),
							args && args.parameters ? args.parameters : {});
					var headers = {
						"Content-Type" : false,
						"Process-Data" : false  // processData = false is reaquired by jquery
					};
					var options = {
						method : "PUT",
						headers : headers,
						data : binaryContent
					};
					var promise = new Promise();
					var _this = this;

					this.updateEntity(url, options, this.getFileFeedCallbacks()).then(function(file) {
						if (args) {
							_this.updateFile(file.getFileId(), args).then(function(updatedFile) {
								promise.fulfilled(updatedFile);
							});
						} else {
							promise.fulfilled(file);
						}
					}, function(error) {
						promise.rejected(error);
					});
					return promise;
				},

				/**
				 * Updates metadata of a file programatically using a PUT call
				 * @param [String] fileId the File ID
				 * @param [Object] args The parameters for update. Supported Input parameters are commentNotification, created, identifier, includePath,
				 * mediaNotification, modified, recommendation, removeTag, sendNotification, sharePermission, shareSummary, shareWith, tag and visibility
				 * @returns
				 */
				updateFile : function(fileId, args) {
					var promise = this.validateField("File ID", fileId);
					if (promise) {
						return promise;
					}
					var url = this.constructUrl(consts.AtomFileInstance, null, {
						documentId : fileId
					});
					var separatorChar = "?";
					if (args && args.tags) {
						var tags = args.tags.split(",");
						for ( var counter in tags) {
							url += separatorChar + "tag=" + stringUtil.trim(tags[counter]);
							separatorChar = "&";
						}
						delete args.tags;
					}
					if (args && args.removeTags) {
						var removeTags = args.removeTags.split(",");
						for ( var counter in removeTags) {
							url += separatorChar + "removeTag=" + stringUtil.trim(removeTags[counter]);
							separatorChar = "&";
						}
						delete args.removeTags;
					}

					var options = {
						method : "PUT",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : args || {}
					};

					return this.updateEntity(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Downloads a file.
				 * 
				 * @method downloadFile
				 * @param {String} fileId The ID of the file
				 * @param {String} libraryId The library ID of the file
				 */
				downloadFile : function(fileId, libraryId) {
					var url = config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/" + "DownloadFile" + "/" + fileId
							+ "/" + libraryId;
					window.open(url);
				},

				actOnCommentAwaitingApproval : function(commentId, action, actionReason) {

				},
				actOnFileAwaitingApproval : function(fileId, action, actionReason) {

				},
				/**
				 * Add a file to a folder or list of folders.
				 * 
				 * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file
				 * to a folder you must be an editor of the folder.
				 * 
				 * @method addFilesToFolder
				 * @param {String} fileId the Id of the file
				 * @param {List} folderIds list of folder Ids
				 * @param {String} [userId] the userId of the user in case of own file
				 * @param {Object} [args] Argument object.
				 */
				addFileToFolders : function(fileId, folderIds, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("folderIds", folderIds);
					}
					if (promise) {
						return promise;
					}

					var url = null;

					if (!userId) {
						url = this.constructUrl(consts.AtomAddMyFileToFolders, null, {
							documentId : fileId
						});
					} else {
						url = this.constructUrl(consts.AtomAddFileToFolders, null, {
							userId : userId,
							documentId : fileId
						});
					}

					var payload = this._constructPayloadForMultipleEntries(folderIds, "itemId", "collection");

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						data : payload
					};

					var callbacks = {
						createEntity : function(service, data, response) {
						}
					};

					return this.updateEntity(url, options, callbacks);
				},

				/**
				 * Create a new Folder
				 * 
				 * @method createFolder <p> Rest API used : /files/basic/api/collections/feed
				 * 
				 * @param {String} name name of the folder to be created
				 * @param {String} [description] description of the folder
				 * @param {String} [shareWith] If the folder needs to be shared, specify the details in this parameter. <br> Pass Coma separated List of id,
				 * (person/community/group) or role(reader/Contributor/owner) in order
				 * @return {Object} Folder
				 */
				createFolder : function(name, description, shareWith) {
					var promise = this.validateField("folderName", name);
					if (promise) {
						return promise;
					}
					var url = consts.AtomCreateFolder;
					var payload = this._constructPayloadFolder(name, description, shareWith, "create");

					var options = {
						method : "POST",
						headers : lang.mixin(consts.AtomXmlHeaders, {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}),
						data : payload
					};

					return this.updateEntity(url, options, this.getFileFeedCallbacks());

				},

				/**
				 * Delete Files From Recycle Bin
				 * 
				 * @param {String} userId The ID of user
				 */
				deleteAllFilesFromRecycleBin : function(userId) {

					var url = null;

					if (!userId) {
						url = consts.AtomDeleteMyFilesFromRecyclebBin;
					} else {
						url = this.constructUrl(consts.AtomDeleteAllFilesFromRecyclebBin, null, {
							userId : userId
						});
					}
					var options = {
						method : "DELETE"
					};
					return this.deleteEntity(url, options, "");
				},

				/**
				 * Delete all Versions of a File before the given version
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {String} [versionLabel] The version from which all will be deleted
				 * @param {Object} [args] additional arguments
				 */
				deleteAllVersionsOfFile : function(fileId, versionLabel, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("versionLabel", versionLabel);
					}
					if (promise) {
						return promise;
					}

					var requestArgs = lang.mixin({
						category : "version",
						deleteFrom : versionLabel
					}, args || {});

					var options = {
						method : "DELETE",
						query : requestArgs,
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomDeleteAllVersionsOfAFile, null, {
						documentId : fileId
					});

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * Delete a Comment for a file
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {String} commentId the ID of comment
				 * @param {String} [userId] the ID of the user, if not provided logged in user is assumed
				 * @param {Object} [args] the additional arguments
				 */
				deleteComment : function(fileId, commentId, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("commentId", commentId);
					}
					if (promise) {
						return promise;
					}

					var url = null;

					if (userId) {
						url = this.constructUrl(consts.AtomDeleteComment, null, {
							userId : userId,
							documentId : fileId,
							commentId : commentId
						});
					} else {
						url = this.constructUrl(consts.AtomDeleteMyComment, null, {
							documentId : fileId,
							commentId : commentId
						});
					}

					var options = {
						method : "DELETE",
						query : args || {},
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					return this.deleteEntity(url, options, commentId);
				},

				/**
				 * Delete File from RecycleBin of a user
				 * @param {String} fileId the Id of the file
				 * @param {String} [userId] the Id of the user
				 * @param {Object} args the additional arguments
				 * @returns
				 */
				deleteFileFromRecycleBin : function(fileId, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var url = null;

					if (userId) {
						url = this.constructUrl(consts.AtomDeleteFileFromRecycleBin, null, {
							userId : userId,
							documentId : fileId
						});
					} else {
						url = this.constructUrl(consts.AtomDeleteMyFileFromRecycleBin, null, {
							documentId : fileId
						});
					}

					var options = {
						method : "DELETE",
						query : args || {},
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * deletes a File Share
				 * @param {String} fileId the ID of the file
				 * @param {String} userId the ID of the user
				 * @param {Object} args the additional arguments
				 */
				deleteFileShare : function(fileId, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var requestArgs = lang.mixin({
						sharedWhat : fileId
					}, args || {});

					if (userId) {
						requestArgs.sharedWith = userId;
					}

					var url = consts.AtomDeleteFileShare;

					var options = {
						method : "DELETE",
						query : requestArgs,
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * Deletes a Folder
				 * @param {String} folderId the ID of the folder
				 */
				deleteFolder : function(folderId) {
					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomDeleteFFolder, null, {
						collectionId : folderId
					});

					return this.deleteEntity(url, options, folderId);
				},

				/**
				 * Get all user Files
				 * @param {String} userId the ID of the user
				 * @param {Object} args the addtional arguments
				 * @returns {Object} Files
				 */
				getAllUserFiles : function(userId, args) {

					var promise = this.validateField("userId", userId);
					if (promise) {
						return promise;
					}

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					var url = this.constructUrl(consts.AtomGetAllUsersFiles, null, {
						userId : userId
					});

					return this.getEntities(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get file Comments
				 * @param {String} fileId the ID of the file
				 * @param {String} [userId] the ID of the user
				 * @param {Boolean} [isAnnonymousAccess] flag to indicate annonymous access
				 * @param {String} [commentId] the ID of the comment
				 * @param {String} [communityId] required in case the file in a community file
				 * @param {Object} args the additional arguments
				 * @returns {Array} Comments List
				 */
				getFileComments : function(fileId, userId, isAnnonymousAccess, commentId, communityId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var url = null;
					if(communityId){
						url = this.constructUrl(consts.AtomAddCommentToCommunityFile, null, {
							communityId : communityId,
							documentId : fileId
						});
					}
					else if (commentId) {
						if (userId) {
							url = this.constructUrl(consts.AtomGetFileComment, null, {
								userId : userId,
								documentId : fileId,
								commentId : commentId
							});
						} else {
							url = this.constructUrl(consts.AtomGetMyFileComment, null, {
								documentId : fileId,
								commentId : commentId
							});
						}
					} else {
						var promise = this.validateField("userId", userId);
						if (promise) {
							return promise;
						}
						if (isAnnonymousAccess) {
							url = this.constructUrl(consts.AtomFileCommentsPublic, null, {
								userId : userId,
								documentId : fileId
							});
						} else {
							url = this.constructUrl(consts.AtomFileCommentsMy, null, {
								userId : userId,
								documentId : fileId
							});
						}
					}

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(url, options, this.getCommentFeedCallbacks());
				},
				
				 /**
			     * Method to get All comments of a Community File
			     * <p>
			     * Rest API Used : 
			     * /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
			     * <p>
			     * @method getAllCommunityFileComments
			     * @param {String} fileId
			     * @param {String} communityId
			     * @param {Object} [args]
			     * @returns {Array} comments
			     */
			    getAllCommunityFileComments : function(fileId, communityId, args) {
			    	
			    	var promise = this.validateField("fileId", fileId);
			    	if(!promise){
			    		promise = this.validateField("communityId", communityId);
			    	}
					if (promise) {
						return promise;
					}
					
					return this.getFileComments(fileId, null, null, null, communityId, args);			    	
			    },

				/**
				 * Get Files from recycle bin
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFileFromRecycleBin : function(fileId, userId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomGetFileFromRecycleBin, null, {
						userId : userId,
						documentId : fileId
					});

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

					return this.getEntity(url, options, callbacks);
				},

				/**
				 * Get Files awaiting approval
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFilesAwaitingApproval : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFilesAwaitingApproval, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get File Shares
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFileShares : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFileShares, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get Files in a folder
				 * @param {String} folderId the ID of the folder
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFilesInFolder : function(folderId, args) {

					var url = this.constructUrl(consts.AtomGetFilesInFolder, null, {
						collectionId : folderId
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(url, options, this.getFileFeedCallbacks());

				},

				/**
				 * Get Files in my recycle bin
				 * @param {Object} [args] the addtional arguments
				 * @returns
				 */
				getFilesInMyRecycleBin : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFilesInMyRecycleBin, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get a file with given version
				 * @param {String} fileId the ID of the file
				 * @param {String} versionId the ID of the version
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} File
				 */
				getFileWithGivenVersion : function(fileId, versionId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					if (!versionId) {
						return this.getFile(fileId, args);
					}
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomGetFileWithGivenVersion, null, {
						documentId : fileId,
						versionId : versionId
					});

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

					return this.getEntity(url, options, callbacks);
				},

				/**
				 * Get a folder
				 * @param {String} folderId the ID of the folder
				 * @returns
				 */
				getFolder : function(folderId) {
					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}
					var options = {
						method : "GET",
						handleAs : "text"
					};
					var url = this.constructUrl(consts.AtomGetFolder, null, {
						collectionId : folderId
					});

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

					return this.getEntity(url, options, callbacks);
				},

				/**
				 * Get Folders With Recently Added Files
				 * @param {Object} [args] the additional arguents
				 * @returns {Object} List of Files
				 */
				getFoldersWithRecentlyAddedFiles : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFoldersWithRecentlyAddedFiles, options, this.getFileFeedCallbacks());

				},

				/**
				 * Gets the folders pinned by the logged in user.
				 * 
				 * @method getPinnedFolders
				 * @param {Object} [args] Argument object for the additional parameters like pageSize etc.
				 */
				getPinnedFolders : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetPinnedFolders, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get public folders
				 * 
				 * @param {Object} [args] Additional arguments like ps, sort by, etc
				 */
				getPublicFolders : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetPublicFolders, options, this.getFileFeedCallbacks());
				},

				/**
				 * Pin a folder, by sending a POST request to the myfavorites feed.
				 * 
				 * @method pinFolder
				 * @param {String} folderId ID of folder which needs to be pinned
				 * @param {Object} [args] Argument object.
				 */
				pinFolder : function(folderId, args) {

					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = folderId;

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					var callbacks = {
						createEntity : function(service, data, response) {
						}
					};

					return this.updateEntity(consts.AtomPinFolder, options, callbacks);

				},

				/**
				 * Remove a File from a Folder
				 * 
				 * @param {String} folderId the ID of the folder
				 * @param {Stirng} fileId The ID of the File
				 */
				removeFileFromFolder : function(folderId, fileId) {

					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}
					promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = fileId;

					var url = this.constructUrl(consts.AtomRemoveFileFromFolder, null, {
						collectionId : folderId
					});
					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					return this.deleteEntity(url, options, fileId);

				},

				/**
				 * Restore a File from Recycle Bin (trash)
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {String} userId the ID of the user
				 */
				restoreFileFromRecycleBin : function(fileId, userId) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					promise = this.validateField("userId", userId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["undelete"] = "true";

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

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

					var url = this.constructUrl(consts.AtomRestoreFileFromRecycleBin, null, {
						userId : userId,
						documentId : fileId
					});

					return this.updateEntity(url, options, callbacks);
				},

				/**
				 * Share a File with community(ies)
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {Object} communityIds The list of community IDs
				 * @param {Object} args the additional arguments
				 */
				shareFileWithCommunities : function(fileId, communityIds, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("communityIds", communityIds);
					}
					if (promise) {
						return promise;
					}

					var url = this.constructUrl(consts.AtomShareFileWithCommunities, null, {
						documentId : fileId
					});

					var payload = this._constructPayloadForMultipleEntries(communityIds, "itemId", "community");

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						data : payload
					};

					var callbacks = {
						createEntity : function(service, data, response) {
							return response;
						}
					};

					return this.updateEntity(url, options, callbacks);
				},

				/**
				 * Unpin a folder, by sending a DELETE request to the myfavorites feed.
				 * 
				 * @method unpinFolder
				 * @param {String} folderId ID of folder which needs to be unpinned
				 * @param {Object} [args] Argument object.
				 */
				unpinFolder : function(folderId, args) {
					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}

					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = folderId;

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					return this.deleteEntity(consts.AtomPinFolder, options, folderId);

				},

				/**
				 * Update comment created by logged in user
				 * @param {String} fileId the ID of the file
				 * @param {String}commentId the ID of the comment
				 * @param {String} comment the updated comment
				 * @param {Object} args the additional arguments
				 * @returns
				 */
				updateMyComment : function(fileId, commentId, comment, args) {

					return updateComment(fileId, commentId, comment, null, args);
				},

				/**
				 * updates a comment
				 * @param {String} fileId the ID of the file
				 * @param {String} commentId the ID of the comment
				 * @param {String} comment the comment
				 * @param {String} [userId] the ID of the user
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} the updated Comment
				 */
				updateComment : function(fileId, commentId, comment, userId, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("comment", comment);
					}
					if (promise) {
						return promise;
					}
					if (!promise) {
						promise = this.validateField("commentId", commentId);
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
						url = this.constructUrl(consts.AtomUpdateMyComment, null, {
							documentId : fileId,
							commentId : commentId
						});
					} else {
						url = this.constructUrl(consts.AtomUpdateComment, null, {
							userId : userId,
							documentId : fileId,
							commentId : commentId
						});
					}
					return this.updateEntity(url, options, this.getCommentFeedCallbacks());
				},

				/**
				 * Add a file to a folder.
				 * 
				 * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file
				 * to a folder you must be an editor of the folder.
				 * 
				 * @method addFileToFolder
				 * @param {String} fileId the Id of the file
				 * @param {String} folderId the ID of the folder
				 * @param {String} [userId] the userId of the user in case of own file
				 * @param {Object} [args] Argument object.
				 */
				addFileToFolder : function(fileId, folderId, userId, args) {

					return this.addFileToFolders(fileId, [ folderId ], userId, args);
				},

				_constructPayloadFolder : function(name, description, shareWith, operation, entityId) {
					var _this = this;
					var shareWithId = null;
					var shareWithWhat = null;
					var shareWithRole = null;
					if (shareWith && stringUtil.trim(shareWith) != "") {
						var parts = shareWith.split(",");
						if (parts.length == 3) {
							shareWithId = parts[0];
							shareWithWhat = parts[1];
							shareWithRole = parts[2];
						}
					}
					var trans = function(value, key) {
						if (key == "category") {
							value = xml.encodeXmlEntry("collection");
						} else if (key == "id") {
							value = xml.encodeXmlEntry(entityId);
						} else if (key == "label") {
							value = xml.encodeXmlEntry(name);
						} else if (key == "title") {
							value = xml.encodeXmlEntry(name);
						} else if (key == "summary") {
							value = xml.encodeXmlEntry(description);
						} else if (key == "visibility") {
							value = xml.encodeXmlEntry("private");
						} else if (key == "shareWithId" && shareWithId) {
							value = xml.encodeXmlEntry(shareWithId);
						} else if (key == "shareWithWhat" && shareWithWhat) {
							value = xml.encodeXmlEntry(shareWithWhat);
						} else if (key == "shareWithRole" && shareWithRole) {
							value = xml.encodeXmlEntry(shareWithRole);
						}
						return value;
					};
					var transformer = function(value, key) {
						if (key == "getCategory") {
							value = stringUtil.transform(FileCategoryTmpl, _this, trans, _this);
						} else if (key == "getId" && entityId) {
							value = stringUtil.transform(FileIdTmpl, _this, trans, _this);
						} else if (key == "getFolderLabel") {
							value = stringUtil.transform(FolderLabelTmpl, _this, trans, _this);
						} else if (key == "getTitle") {
							value = stringUtil.transform(FileTitleTmpl, _this, trans, _this);
						} else if (key == "getSummary") {
							value = stringUtil.transform(FileSummaryTmpl, _this, trans, _this);
						} else if (key == "getVisibility") {
							value = stringUtil.transform(FileVisibilityTmpl, _this, trans, _this);
						} else if (key == "getVisibilityShare" && shareWithId) {
							value = stringUtil.transform(FileVisibilityShareTmpl, _this, trans, _this);
						}
						return value;
					};
					var postData = stringUtil.transform(FolderTmpl, this, transformer, this);					
					return stringUtil.trim(postData);
				},
				_constructPayloadForMultipleEntries : function(listOfIds, multipleEntryId, category) {
					var payload = FileFeedTmpl;
					var entriesXml = "";
					var categoryXml = "";
					var itemXml = "";
					var currentId = "";
					var transformer = function(value, key) {
						if (key == "category") {
							value = xml.encodeXmlEntry(category);
						} else if (key == "getCategory") {
							value = categoryXml;
						} else if (key == "fileId") {
							value = xml.encodeXmlEntry(currentId);
						} else if (key == "getItem") {
							value = itemXml;
						} else if (key == "getEntries") {
							value = entriesXml;
						}
						return value;
					};
					var _this = this;

					for ( var counter in listOfIds) {
						currentId = listOfIds[counter];
						var entryXml = FileItemEntryTmpl;
						if (category) {
							categoryXml = stringUtil.transform(FileCategoryTmpl, _this, transformer, _this);
						}
						itemXml = stringUtil.transform(FileItemIdTmpl, _this, transformer, _this);
						entryXml = stringUtil.transform(entryXml, _this, transformer, _this);
						entriesXml = entriesXml + entryXml;
					}

					if (entriesXml != "") {
						payload = stringUtil.transform(payload, _this, transformer, _this);
					}					
					return payload;
				},
				_constructPayloadForComment : function(isDelete, comment) {

					var payload = FileCommentsTmpl;
					var categoryXml = "";
					var contentXml = "";
					var deleteXml = "";
					var _this = this;

					var transformer = function(value, key) {
						if (key == "category") {
							value = xml.encodeXmlEntry("comment");
						} else if (key == "content") {
							value = xml.encodeXmlEntry(comment);
						} else if (key == "deleteWithRecord") {
							value = "true";
						} else if (key == "getCategory" && categoryXml != "") {
							value = categoryXml;
						} else if (key == "getContent" && contentXml != "") {
							value = contentXml;
						} else if (key == "getDeleteComment" && deleteXml != "") {
							value = deleteXml;
						}
						return value;
					};

					categoryXml = stringUtil.transform(FileCategoryTmpl, _this, transformer, _this);

					contentXml = stringUtil.transform(FileContentTmpl, _this, transformer, _this);
					if (isDelete) {
						deleteXml = stringUtil.transform(FileDeleteCommentTmpl, _this, transformer, _this);
					}

					payload = stringUtil.transform(payload, this, transformer, this);
					return payload;
				},
				_constructPayload : function(payloadMap, documentId) {

					var payload = FileEntryTmpl;
					var categoryXml = "";
					var idXml = "";
					var uuidXml = "";
					var labelXml = "";
					var titleXml = "";
					var summaryXml = "";
					var visibilityXml = "";
					var itemXml = "";
					var tagsXml = "";
					var notificationXml = "";
					var currentValue = null;
					var transformer = function(value, key) {
						if (currentValue) {
							value = xml.encodeXmlEntry(currentValue);
						} else if (key == "getCategory" && categoryXml != "") {
							value = categoryXml;
						} else if (key == "getId" && idXml != "") {
							value = idXml;
						} else if (key == "getUuid" && uuidXml != "") {
							value = uuidXml;
						} else if (key == "getLabel" && labelXml != "") {
							value = labelXml;
						} else if (key == "getTitle" && titleXml != "") {
							value = titleXml;
						} else if (key == "getSummary" && summaryXml != "") {
							value = summaryXml;
						} else if (key == "getVisibility" && visibilityXml != "") {
							value = visibilityXml;
						} else if (key == "getItem" && itemXml != "") {
							value = itemXml;
						} else if (key == "getTags" && tagsXml != "") {
							value = tagsXml;
						} else if (key == "getNotification" && notificationXml != "") {
							value = notificationXml;
						}
						return value;
					};

					for ( var currentElement in payloadMap) {
						currentValue = payloadMap[currentElement];
						if (currentElement.indexOf("category") != -1) {
							categoryXml = stringUtil.transform(FileCategoryTmpl, this, transformer, this);
						} else if (currentElement.indexOf("id") != -1) {
							idXml = stringUtil.transform(FileIdTmpl, this, transformer, this);
						} else if (currentElement.indexOf("uuid") != -1) {
							uuidXml = stringUtil.transform(FileUuidTmpl, this, transformer, this);
						} else if (currentElement.indexOf("label") != -1) {
							labelXml = stringUtil.transform(FileLabelTmpl, this, transformer, this);
							titleXml = stringUtil.transform(FileTitleTmpl, this, transformer, this);
						} else if (currentElement.indexOf("summary") != -1) {
							summaryXml = stringUtil.transform(FileSummaryTmpl, this, transformer, this);
						} else if (currentElement.indexOf("visibility") != -1) {
							visibilityXml = stringUtil.transform(FileVisibilityTmpl, this, transformer, this);
						} else if (currentElement.indexOf("itemId") != -1) {
							itemXml = stringUtil.transform(FileItemIdTmpl, this, transformer, this);
						} else if (currentElement.indexOf("tags") != -1) {
							var tags = currentValue;
							for ( var tag in tags) {
								tagsXml += stringUtil.transform(TagsTmpl, {
									"tag" : tags[tag]
								});
							}
						} else if (currentElement.contains("notification")) {
							notificationXml = stringUtil.transform(NotificationTmpl, this, transformer, this);
						}
					}
					currentValue = null;

					payload = stringUtil.transform(payload, this, transformer, this);
					return payload;
				}
			});
			return FileService;
		});
