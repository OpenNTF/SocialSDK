/*
 * © Copyright IBM Corp. 2012
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
 * Javascript APIs for IBM Connections File Service.
 * @module sbt.connections.FileService
 */
define([ "sbt/_bridge/declare", "sbt/config", "sbt/lang", "sbt/connections/core", "sbt/xml", "sbt/xpath", "sbt/Endpoint", "sbt/connections/FileConstants",
				"sbt/validate", "sbt/log"],
		function(declare, cfg, lang, con, xml, xpath, endpoint, constants, validate, log) {

			// TODO revisit this
			function evaluateXpath(data, path) {
				return data && path ? xpath.selectText(data, path, con.namespaces) : null;
			}

/**
			 * FileEntry class associated with a file document returned in xml feed and also used to represent a file.
			 * @class FileEntry
			 * @constructor
			 * @param {Object} FileService fileService object
			 * @param {String} id id associated with the file.
			 */
			var FileEntry = declare("sbt.connections.FileEntry", null, {

				_service : null,
				_data : null,
				_id : null,
				fields : {},
				commentEntries : [],
				personEntry : null,

				constructor : function(svc, id) {
					this._id = id;
					this._service = svc;
					this.personEntry = new PersonEntry(svc);
					this.personEntry._data = this._data;
				},
				_setData : function(newData) {
					this._data = newData;
					this.personEntry._data = newData;
				},
				/**
				 * get method for getting any field in FileEntry
				 * @method get
				 * @param {String} fieldName
				 * @returns {Object} value of the field
				 */
				get : function(fieldName) {
					return this.fields[fieldName] || evaluateXpath(this._data, constants.xpathMapFile[fieldName]);
				},
				/**
				 * set method for setting any field in FileEntry
				 * @method set
				 * @param {String} fieldName
				 * @param {Object} value of the field
				 */
				set : function(fieldName, value) {
					this.fields[fieldName] = value;
				},
				/**
				 * gets the fieldId
				 * @method getId
				 * @returns {String} Id of the file
				 */
				getId : function() {
					return this._id || this.get("uuid");
				},
				/**
				 * gets the download link for file
				 * @method getDownloadLink
				 * @returns {String} DownloadLink of the file
				 */
				getDownloadLink : function() {
					return this.get("dwnLink");
				},
				/**
				 * gets the size file
				 * @method getSize
				 * @returns {String} size of the file
				 */
				getSize : function() {
					return this.get("size");
				},
				/**
				 * gets the created Date of file
				 * @method getCreatedDate
				 * @returns {String} CreatedDate of the file
				 */
				getCreatedDate : function() {
					return this.get("createdDate");
				},
				/**
				 * gets the Category of file
				 * @method getCategory
				 * @returns {String} Category of the file
				 */
				getCategory : function() {
					return this.get("category");
				},
				/**
				 * gets the Lock state of file
				 * @method getLock
				 * @returns {String} lock state of the file
				 */
				getLock : function() {
					return this.get("lock");
				},
				/**
				 * gets the Name of file
				 * @method getLock
				 * @returns {String} lock state of the file
				 */
				getName : function() {
					return this.get("name");
				},
				/**
				 * gets the modifed date of the file
				 * @method getModified
				 * @returns {String} modifed date of the file
				 */
				getModified : function() {
					return this.get("modified");
				},
				/**
				 * gets the visibility of the file
				 * @method getVisibility
				 * @returns {String} visibility of the file
				 */
				getVisibility : function() {
					return this.get("visibility");
				},
				/**
				 * gets the LibraryType of the file
				 * @method getLibraryType
				 * @returns {String} libraryType of the file
				 */
				getLibraryType : function() {
					return this.get("libraryType");
				},
				/**
				 * gets the version Id of the file
				 * @method getVersionUuid
				 * @returns {String} version Id of the file
				 */
				getVersionUuid : function() {
					return this.get("versionUuid");
				},
				/**
				 * gets the summary of the file
				 * @method getSummary
				 * @returns {String} summary of the file
				 */
				getSummary : function() {
					return this.get("summary");
				},
				/**
				 * gets the restrictedVisibility of the file
				 * @method getRestrictedVisibility
				 * @returns {String} restrictedVisibility of the file
				 */
				getRestrictedVisibility : function() {
					return this.get("restrictedVisibility");
				},
				/**
				 * gets the title of the file
				 * @method getTitle
				 * @returns {String} title of the file
				 */
				getTitle : function() {
					return this.get("title");
				},
				/**
				 * sets the label to be updated for the file
				 * @method setLabel
				 * @param {String} label to be updated
				 */
				setLabel : function(label) {
					this.set("label", label);
				},
				/**
				 * sets the summary to be updated for the file
				 * @method setSummary
				 * @param {String} summary to be updated
				 */
				setSummary : function(summary) {
					this.set("summary", summary);
				},
				/**
				 * sets the visibility to be updated for the file
				 * @method setVisibility
				 * @param {String} visibility to be updated
				 */
				setVisibility : function(visibility) {
					this.set("visibility", visibility);
				},
				/**
				 * updates metadata about a file like title, visibility, etc.
				 * @method update
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is updated. The function expects one parameter, the updated
				 * FileEntry object.
				 * @param {Function} [args.error] Sometimes the update calls fails due to bad request (400 error). The error parameter is a callback function
				 * that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the error function is a
				 * JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library error object, the
				 * status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to update the file completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				update : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.updateFile(this, args);
				},
				/**
				 * Gets the comments for a file.
				 * @method getFileComments
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the comments are retrieved successfully. The function expects one
				 * parameter, the list of CommentEntry objects.
				 * @param {Function} [args.error] Sometimes the call to get comments of a file fails due to bad request (400 error). The error parameter is a
				 * callback function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library
				 * error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to get the comments completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				getComments : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.getFileComments(this, args);
				},
				/**
				 * Uploads a new file for logged in user.
				 * @method upload
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is uploaded successfully. The function expects one parameter,
				 * the status of upload.
				 * @param {Function} [args.error] Sometimes the upload calls fails due to bad request (400 error). The error parameter is a callback function
				 * that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the error function is a
				 * JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library error object, the
				 * status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to upload the file completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				upload : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.uploadFile(this, args);
				},
				/**
				 * Adds comment to a file of any user.
				 * @method addComment
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the comment is added successfully to the file. The function expects one
				 * parameter, the newly created comment object.
				 * @param {Function} [args.error] Sometimes the update calls fails due to bad request (400 error). The error parameter is a callback function
				 * that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the error function is a
				 * JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library error object, the
				 * status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to update the profile completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				addComment : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.addCommentToFile(this, args);
				},
				/**
				 * Adds comment to a file of logged in user.
				 * @method addCommentToMyFile
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the comment is added successfully to the file. The function expects one
				 * parameter, the newly created comment object.
				 * @param {Function} [args.error] Sometimes the update calls fails due to bad request (400 error). The error parameter is a callback function
				 * that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the error function is a
				 * JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library error object, the
				 * status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to update the profile completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				addCommentToMyFile : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.addCommentToMyFile(this, args);
				},
				/**
				 * Lock a file.
				 * @method lockFile
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is locked successfully. The function expects one parameter, the
				 * status of uncock operation.
				 * @param {Function} [args.error] Sometimes the lock file calls fails due to bad request (400 error). The error parameter is a callback function
				 * that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the error function is a
				 * JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library error object, the
				 * status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to lock the file completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				lock : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.lockFile(this, args);
				},
				/**
				 * UnLock a file.
				 * @method unlockFile
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is unlocked successfully. The function expects one parameter,
				 * the status of uncock operation.
				 * @param {Function} [args.error] Sometimes the lock file calls fails due to bad request (400 error). The error parameter is a callback function
				 * that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the error function is a
				 * JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library error object, the
				 * status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to unlock the file completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				unLock : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.unlockFile(this, args);
				},
				/**
				 * Delete a file.
				 * @method deleteFile
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is deleted successfully. The function expects one parameter,
				 * the status of the delete openration.
				 * @param {Function} [args.error] Sometimes the delete calls fails due to bad request (400 error). The error parameter is a callback function
				 * that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the error function is a
				 * JavaScript Error object indicating what the failure was. From the error object. one can access the javascript library error object, the
				 * status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to delete the File completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 */
				deleteFile : function(args) {
					if (!this._service) {
						this._service = new FileService();
					}
					this._service.deleteFile(this, args);
				},
				/**
				 * Get the loaded comments for the File
				 * @returns {Object} commentEntries
				 */
				getComments : function() {
					return this.commentEntries;
				},
				/**
				 * Get the person entry for author and modifier for the File
				 * @returns {Object} PersonEntry
				 */
				getPersonEntry : function() {
					return this.personEntry;
				},
				validate : function(className, methodName, args, validateMap) {
					if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "File Id", this._id, "string", args))) {
						return false;
					}
					if (validateMap.isValidateUser
							&& (!(validate._validateInputTypeAndNotify(className, methodName, "File.personEntry", this.personEntry,
									"sbt.connections.PersonEntry", args)) || !(validate._validateInputTypeAndNotify(className, methodName, "File.UserId",
									this.personEntry ? this.personEntry.getAuthorId() : null, "string", args)))) {
						return false;
					}
					return true;
				}
			});

			/**
			 * CommentEntry class associated with a Coment document returned in xml feed and also used to represent a Comment.
			 * @class CommentEntry
			 * @constructor
			 * @param {Object} FileService fileService object
			 * @param {String} id id associated with the comment.
			 */
			var CommentEntry = declare("sbt.connections.CommentEntry", null, {

				_service : null,
				_data : null,
				_id : null,
				fields : {},

				constructor : function(svc, id) {
					this._id = id;
					this._service = svc;
				},
				_setData : function(newData) {
					this._data = newData;
				},
				get : function(fieldName) {
					return this.fields[fieldName] || evaluateXpath(this._data, constants.xpathMapComment[fieldName]);
				},
				set : function(fieldName, value) {
					this.fields[fieldName] = value;
				},
				/**
				 * gets the comment Id
				 * @method getId
				 * @returns {String} Id of the Comment
				 */
				getId : function() {
					return this._id || this.get("uuid");
				},
				/**
				 * gets the comment text
				 * @method getComment
				 * @returns {String} text of the Comment
				 */
				getComment : function() {
					return this.get("comment");
				}
			});

			/**
			 * PersonEntry class associated with a Person document returned in xml feed and also used to represent a file.
			 * @class PersonEntry
			 * @constructor
			 * @param {Object} FileService fileService object
			 * @param {String} id userId of the person.
			 */
			var PersonEntry = declare("sbt.connections.PersonEntry", null, {

				_service : null,
				_data : null,
				_id : null,
				fields : {},

				constructor : function(svc, id) {
					this._id = id;
					this._service = svc;
				},
				get : function(fieldName) {
					return this.fields[fieldName] || evaluateXpath(this._data, constants.xpathMapPerson[fieldName]);
				},
				set : function(fieldName, value) {
					this.fields[fieldName] = value;
				},
				/**
				 * gets the author's userId
				 * @method getAuthorId
				 * @returns {String} author Id of the file
				 */
				getAuthorId : function() {
					return this._id || this.get("userUuid");
				},
				/**
				 * gets the author's name
				 * @method getAuthorName
				 * @returns {String} author name of the file
				 */
				getAuthorName : function() {
					return this.get("author");
				},
				/**
				 * gets the author's email
				 * @method getAuthorEmail
				 * @returns {String} author email of the file
				 */
				getAuthorEmail : function() {
					return this.get("email");
				},
				/**
				 * gets the author's user state
				 * @method getAuthorUserState
				 * @returns {String} author's user state of the file
				 */
				getAuthorUserState : function() {
					return this.get("userState");
				},
				/**
				 * gets the modifier's user Id
				 * @method getModifierId
				 * @returns {String} modifier's user Id of the file
				 */
				getModifierId : function() {
					return this.get("userUuidModifier");
				},
				/**
				 * gets the modifier's Name
				 * @method getModifierName
				 * @returns {String} modifier's name of the file
				 */
				getModifierName : function() {
					return this.get("nameModifier");
				},
				/**
				 * gets the modifier's Email
				 * @method getModifierEmail
				 * @returns {String} modifier's email of the file
				 */
				getModifierEmail : function() {
					return this.get("emailModifier");
				},
				/**
				 * gets the modifier's user state
				 * @method getModifierUserState
				 * @returns {String} modifier's user state of the file
				 */
				getModifierUserState : function() {
					return this.get("userStateModifier");
				}
			});

			var _SubFilters = declare("sbt.connections.FileEntry._SubFilters", null, {
				DOCUMENT : "/document",
				COMMENT : "/comment",
				COLLECTION : "/collection",
				LIBRARY : "/userlibrary",
				userId : null,
				documentId : null,
				commentId : null,
				collectionId : null,

				getUserId : function() {
					return this.userId;
				},
				setUserId : function(id) {
					this.userId = id;
				},
				getDocumentId : function() {
					return this.documentId;
				},
				setDocumentId : function(docId) {
					this.documentId = docId;
				},
				getCommentId : function() {
					return this.commentId;
				},
				setCommentId : function(id) {
					this.commentId = id;
				},
				getCollectionId : function() {
					return this.collectionId;
				},
				setCollectionId : function(id) {
					this.collectionId = id;
				}
			});

			/**
			 * File service class associated with files API of IBM Connections.
			 * @class FileService
			 * @constructor
			 * @param {Object} parameters Parameter object
			 * @param {String} [parameters.endpoint=connections] Endpoint to be used by FileService.
			 */
			var FileService = declare(null,	{
				_endpoint : null,
				_file : null,
				_endpointName : null,
				_nonce : null,

				constructor : function(parameters) {
					parameters = parameters || {};
					this._endpointName = parameters.endpoint || "connections";
					this._endpoint = endpoint.find(this._endpointName);
				},
				_notifyCb : function(args, param) {

					if (args) {
						if (args.load)
							args.load(param);
						else if (args.handle)
							args.handle(param);
					} else {
						log.error("Callbacks not defined. Return Value={0}", param);
					}
				},

				/**
				 * Getter for FileEntry Object representing File Entry Document or used to do File API operations.
				 * @method getFile
				 * @param {Object} [args=null] Argument object
				 * @param {String} [args.id=null] FileId of the file, default null in case of new File created.
				 * @param {Boolean} [args.loadIt=true] Loads the FileEntry object with file entry document. If an empty FileEntry object associated with
				 * a FileEntry (with no file entry document), then the load method must be called with this parameter set to false. By default, this
				 * parameter is true.
				 * @param {Function} [args.load] The function invokes when the file is loaded successfully from the server. The function expects to
				 * receive one parameter, the loaded FileEntry object.
				 * @param {Function} [args.error] Sometimes the getFile call fails with bad request such as 400 or server errors such as 500. The error
				 * parameter is another callback function that is only invoked when an error occurs. This allows to control what happens when an error
				 * occurs without having to put a lot of logic into your load function to check for error conditions. The parameter passed to the error
				 * function is a JavaScript Error object indicating what the failure was. From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback is called regardless of whether the call to get the file completes or fails. The
				 * parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the
				 * javascript library error object, the status code and the error message.
				 */
				getFile : function(args) {
					if (!args) {
						args = {};
					}
					var file = new FileEntry(this, args.id);
					if (args.loadIt == null || args.loadIt == undefined || args.loadIt == true) {
						this._loadFile(file, args);
					}
					return file;
				},

				/**
				 * Getter for CommentEntry Object.
				 * @method getComment
				 * @param {String} [args.Id=null] CommentId of the Comment, default null in case of a new comment created.
				 */
				getComment : function(id) {
					return new CommentEntry(this, id);
				},

				_executeGet : function(args, url, file) {

					var _self = this;
					this._endpoint.xhrGet({
						serviceUrl : url,
						handleAs : "text",
						load : function(data) {
							if (args.responseFormat && args.responseFormat == constants.responseFormat.NON_XML_FORMAT) {
								_self._notifyCb(args, data);
							} else {
								var entries = _self._parseXmlData(data, file);
								if (args.responseFormat && args.responseFormat == constants.responseFormat.SINGLE && entries.length == 1) {
									_self._notifyCb(args, entries[0]);
								} else {
									_self._notifyCb(args, entries);
								}
							}
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});

				},

				_executePut : function(args, url, headers, payload) {

					var _self = this;

					this._endpoint.xhrPut({
						serviceUrl : url,
						putData : payload,
						headers : headers,
						load : function(data) {
							if (args.responseFormat && args.responseFormat == constants.responseFormat.NON_XML_FORMAT) {
								_self._notifyCb(args, data);
							} else {
								var entries = _self._parseXmlData(data);
								if (args.responseFormat && args.responseFormat == constants.responseFormat.SINGLE && entries.length == 1) {
									_self._notifyCb(args, entries[0]);
								} else {
									_self._notifyCb(args, entries);
								}
							}
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});

				},

				_executePost : function(args, url, headers, payload) {

					var _self = this;

					this._endpoint.xhrPost({
						serviceUrl : url,
						postData : payload,
						headers : headers,
						load : function(data) {
							if (args.responseFormat && args.responseFormat == constants.responseFormat.NON_XML_FORMAT) {
								_self._notifyCb(args, data);
							} else {
								var entries = _self._parseXmlData(data);
								if (args.responseFormat && args.responseFormat == constants.responseFormat.SINGLE && entries.length == 1) {
									_self._notifyCb(args, entries[0]);
								} else {
									_self._notifyCb(args, entries);
								}
							}
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});

				},

				_parseXmlData : function(data, file) {
					var entries = [];
					if (!data || data == "") {
						return entries;
					}
					var xmlData = xml.parse(data);

					var entryNodes = xpath.selectNodes(xmlData, constants.xPathEntry, con.namespaces);
					if (entryNodes.length == 0) {
						entryNodes = xpath.selectNodes(xmlData, constants.xpathMapFile["entry"], con.namespaces);
					}
					for ( var count = 0; count < entryNodes.length; count++) {
						var node = entryNodes[count];
						var entry = null;
						var category = evaluateXpath(node, constants.xpathMapFile["category"]);
						var id = evaluateXpath(node, constants.xpathMapFile["uuid"]);
						if (category == "comment") {
							entry = new CommentEntry(this, id);
							if (file) {
								file.commentEntries.push(entry);
							}
						} else {
							entry = new FileEntry(this, id);
						}
						entry._setData(node);
						entries.push(entry);
					}
					return entries;
				},

				_executeDelete : function(args, url, headers) {
					var _self = this;
					this._endpoint.xhrDelete({
						serviceUrl : url,
						headers : headers,
						load : function(data) {
							_self._notifyCb(args, "Success");
						},
						error : function(error, ioargs) {
							validate.notifyError(error, args);
						}
					});
				},

				_constructUrl : function(baseUrl, accessType, category, view, filter, subFilters, resultType, parameters) {

					var url = baseUrl + constants.SEPARATOR + this._endpoint.authType;

					if (!accessType && !category && !view && !filter && !subFilters) {
						accessType = constants.accessType.AUTHENTICATED;
						category = constants.categories.MYLIBRARY;
						view = constants.views.FILES;
						filter = constants.filters.NULL;
					}

					if (accessType)
						url = url + accessType;
					if (category)
						url = url + category;
					if (view)
						url = url + view;
					if (filter)
						url = url + filter;

					if (subFilters) {
						if (subFilters.getCollectionId())
							url = url + subFilters.COLLECTION + constants.SEPARATOR + subFilters.getCollectionId();
						if (subFilters.getUserId())
							url = url + subFilters.LIBRARY + constants.SEPARATOR + subFilters.getUserId();
						if (subFilters.getDocumentId())
							url = url + subFilters.DOCUMENT + constants.SEPARATOR + subFilters.getDocumentId();
						if (subFilters.getCommentId())
							url = url + subFilters.COMMENT + constants.SEPARATOR + subFilters.getCommentId();
					}

					if (resultType)
						url = url + resultType;

					if (parameters) {
						var c = "?";
						for ( var key in parameters) {
							url = url + c + encodeURIComponent(key) + "=" + encodeURIComponent(parameters[key]);
							c = "&";
						}
					}
					return url;
				},

				_constructPayload : function(payloadMap, documentId) {
					var payload = "<entry xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"document\" label=\"document\" scheme=\"tag:ibm.com,2006:td/type\"></category>";
					payload += "<id>urn:lsid:ibm.com:td:" + xml.encodeXmlEntry(documentId) + "</id>";
					if (payloadMap) {
						for (key in payloadMap) {
							var value = payloadMap[key];
							if (key == "label") {
								payload += "<label xmlns=\"" + con.namespaces.td + "\">" + xml.encodeXmlEntry(value) + "</label>";
								payload += "<title>" + xml.encodeXmlEntry(value) + "</title>";
							} else if (key == "summary") {
								payload += "<summary type=\"text\">" + xml.encodeXmlEntry(value) + "</summary>";
							} else if (key == "visibility") {
								payload += "<visibility xmlns=\"" + con.namespaces.td + "\">" + xml.encodeXmlEntry(value) + "</visibility>";
							}
						}
					}

					payload += "</entry>";
					log.debug("Payload for file update " + payload);
					return payload;
				},
				_constructPayloadForComment : function(isDelete, comment) {
					var payload = "<entry xmlns=\"http://www.w3.org/2005/Atom\">";
					payload += "<category term=\"comment\" label=\"comment\" scheme=\"tag:ibm.com,2006:td/type\"/>";

					if (isDelete == true)
						payload += "<deleteWithRecord xmlns=\"" + con.namespaces.td + "\">false</deleteWithRecord>";
					else
						payload += "<content type=\"text/plain\">" + xml.encodeXmlEntry(comment) + "</content>";
					payload += "</entry>";
					log.debug("Payload for Comment" + payload);
					return payload;
				},

				/**
				 * Gets the files of the logged in user.
				 * @method getMyFiles
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the files of the user are retrieved successfully. The function
				 * expects one parameter, a list of FileEntry objects.
				 * @param {Function} [args.error] Sometimes the call to get files of the user fails due to bad request (400 error). The error parameter
				 * is a callback function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter
				 * passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the
				 * javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to get files of the user completes or
				 * fails. The parameter passed to this callback is the list of FileEntry objects (or error object). From the error object. one can get
				 * access to the javascript library error object, the status code and the error message.
				 * @param {Object} [args.parameters] The additional parameters like pageSize etc.
				 */
				getMyFiles : function(args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var category = constants.categories.MYLIBRARY;
					var resultType = constants.resultType.FEED;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, category, null, null, null, resultType, args.parameters);
					this._executeGet(args, url);
				},

				/**
				 * Gets the files shared with the logged in user.
				 * @method getFilesSharedWithMe
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the files shared with the user are retrieved successfully. The
				 * function expects one parameter, a list of FileEntry objects.
				 * @param {Function} [args.error] Sometimes the call to get files shared with the user fails due to bad request (400 error). The error
				 * parameter is a callback function that is only invoked when an error occurs. This allows to write logic when an error occurs. The
				 * parameter passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
				 * access the javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to get files shared with the user
				 * completes or fails. The parameter passed to this callback is the list of FileEntry objects (or error object). From the error object.
				 * one can get access to the javascript library error object, the status code and the error message.
				 * @param {Object} [args.parameters] The additional parameters like pageSize etc.
				 */
				getFilesSharedWithMe : function(args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var view = constants.views.FILES;
					var filter = constants.filters.SHARED;
					var resultType = constants.resultType.FEED;
					var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
					parameters["direction"] = "inbound";
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, view, filter, null, resultType, parameters);
					this._executeGet(args, url);
				},

				/**
				 * Gets the files shared by the logged in user.
				 * @method getFilesSharedByMe
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the files shared by the user are retrieved successfully. The
				 * function expects one parameter, a list of FileEntry objects.
				 * @param {Function} [args.error] Sometimes the call to get files shared by the user fails due to bad request (400 error). The error
				 * parameter is a callback function that is only invoked when an error occurs. This allows to write logic when an error occurs. The
				 * parameter passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can
				 * access the javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to get files shared by the user
				 * completes or fails. The parameter passed to this callback is the list of FileEntry objects (or error object). From the error object.
				 * one can get access to the javascript library error object, the status code and the error message.
				 * @param {Object} [args.parameters] The additional parameters like pageSize etc.
				 */
				getFilesSharedByMe : function(args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var view = constants.views.FILES;
					var filter = constants.filters.SHARED;
					var resultType = constants.resultType.FEED;
					var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
					parameters["direction"] = "outbound";
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, view, filter, null, resultType, parameters);
					this._executeGet(args, url);
				},

				/**
				 * Gets the comments for a file.
				 * @method getFileComments
				 * @param {Object} file FileEntry object whose comments you want to get. Use FileService.getFile() with loadIt true to create this
				 * object.
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the comments are retrieved successfully. The function expects
				 * one parameter, the list of CommentEntry objects.
				 * @param {Function} [args.error] Sometimes the call to get comments of a file fails due to bad request (400 error). The error parameter
				 * is a callback function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter
				 * passed to the error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the
				 * javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to get the comments completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 * @param {Object} [args.parameters] The additional parameters like pageSize etc.
				 */
				getFileComments : function(file, args) {
					if (!validate._validateInputTypeAndNotify("FileService", "getFileComments", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "getFileComments", args, {
						isValidateId : true,
						isValidateUser : true
					})) {
						return;
					}

					var accessType = constants.accessType.AUTHENTICATED;
					var subFilters = new _SubFilters();
					subFilters.setUserId(file.getPersonEntry().getAuthorId());
					subFilters.setDocumentId(file.getId());
					var resultType = constants.resultType.FEED;
					var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
					parameters["category"] = "comment";
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, parameters);
					this._executeGet(args, url, file);

				},

				/**
				 * Uploads a new file for logged in user.
				 * @method uploadFile
				 * @param {Object} [args] Argument object
						 * @param {Objecr} [args.fileControlId] The Id of html control
						 * @param {Object} [args.fileControl] The html control
				 * @param {Function} [args.load] The callback function will invoke when the file is uploaded successfully. The function expects one
				 * parameter, the status of upload.
				 * @param {Function} [args.error] Sometimes the upload calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to upload the file completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
						 * @param {Object} [args.parameters] The additional parameters
				 */
				uploadFile : function(args) {

							if (!validate._validateInputTypeAndNotify("FileService", "uploadFile", "args", args, 'object', args)) {
						return;
					}
							var files = null;
							var filePath = null;

							if (args.fileControlId) {
								var fileControl = document.getElementById(args.fileControlId);
								filePath = fileControl.value;
								files = fileControl.files;
							} else if (args.fileControl) {
								filePath = args.fileControl.value;
								files = args.fileControl.files;
							} else {
								validate.notifyError("Either File Control of File Control ID is required for upload", args);
							}

							var index = filePath.lastIndexOf("\\");
							if (index == -1) {
								index = filePath.lastIndexOf("/");
							}

					var reader = new FileReader();
					var _self = this;
					reader.onload = function(event) {
						var binaryContent = event.target.result;
								var _args = lang.mixin({}, args);								
								var index = filePath.lastIndexOf("\\");
								if (index == -1) {
									index = filePath.lastIndexOf("/");
								}								
								_args["fileName"] = filePath.substring(index + 1);
								_self.uploadFileBinary(binaryContent, _args);
							};
							reader.onerror = function(error) {
								validate.notifyError(error, args);
							};
							reader.readAsBinaryString(files[0]);
						},

						/**
						 * Uploads a new file for logged in user.
						 * @method uploadFile
						 * @param {Object} [binaryContent] The binary content of the file						
						 * @param {Object} [args] Argument object						 
						 * @param {Function} [args.load] The callback function will invoke when the file is uploaded successfully. The function expects one
						 * parameter, the status of upload.
						 * @param {Function} [args.error] Sometimes the upload calls fails due to bad request (400 error). The error parameter is a callback
						 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
						 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
						 * library error object, the status code and the error message.
						 * @param {Function} [args.handle] This callback function is called regardless of whether the call to upload the file completes or
						 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
						 * the javascript library error object, the status code and the error message.
						 * @param {String} [args.fileName] The file name
						 * @param {Object} [args.parameters] The additional parameters
						 */
						uploadFileBinary : function(binaryContent, args) {
							if (!validate._validateInputTypesAndNotify("FileService", "uploadFile", [ "args", "binaryContent", "fileName" ], [ args,
									binaryContent, args ? args.fileName : null ], [ 'object', 'string', 'string' ], args)) {
								return;
							}

						var accessType = constants.accessType.AUTHENTICATED;
						var category = constants.categories.MYLIBRARY;
						var resultType = constants.resultType.FEED;
						var baseUrl = constants.baseUrl.FILES;
							var url = this._constructUrl(baseUrl, accessType, category, null, null, null, resultType, args.parameters);
							url = cfg.Properties.serviceUrl + "/files/" + this._endpointName + "/" + constants.FILE_TYPE_CONNECTIONS + "/" + url;
						var headers = {};
							headers["Slug"] = args.fileName;
							var _self = this;
							this._endpoint.xhrPost({
							url : url,
							postData : binaryContent,
							headers : headers,
							load : function(data) {
								_self._notifyCb(args, "Success");
							},
							error : function(error) {
								validate.notifyError(error, args);
							}
						});
				},

				/**
				 * updates metadata about a file like title, visibility, etc.
				 * @method updateFile
				 * @param {Object} file FileEntry object which needs to be updated with containing updated values. Use FileService.getFile() with loadIt
				 * false to create this object.
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is updated. The function expects one parameter, the
				 * updated FileEntry object.
				 * @param {Function} [args.error] Sometimes the update calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to update the file completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				updateFile : function(file, args) {

					if (!validate._validateInputTypeAndNotify("FileService", "updatefile", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "updateFile", args, {
						isValidateId : true
					})) {
						return;
					}

					var accessType = constants.accessType.AUTHENTICATED;
					var category = constants.categories.MYLIBRARY;
					var subFilters = new _SubFilters();
					subFilters.setDocumentId(file.getId());
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, category, null, null, subFilters, resultType, null);
					var headers = {};
					headers["Content-Type"] = constants.atom;
					var updateFilePayload = this._constructPayload(file.fields, file.getId());
					var _args = args ? lang.mixin({}, args) : {};
					_args["responseFormat"] = constants.responseFormat.SINGLE;
					this._executePut(_args, url, headers, updateFilePayload);
				},

				/**
				 * Adds comment to a file of any user.
				 * @method addCommentToFile
				 * @param {Object} file FileEntry object to which comment needs to be added. Id and Comment needs to be set on this object. Use
				 * FileService.getFile() with loadIt true to create this object.
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the comment is added successfully to the file. The function
				 * expects one parameter, the newly created comment object.
				 * @param {Function} [args.error] Sometimes the update calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to update the profile completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				addCommentToFile : function(file, args) {

					if (!validate._validateInputTypesAndNotify("FileService", "addCommentToFile", [ "File", "args", "comment" ], [ file, args,
							args ? args.comment : null ], [ "sbt.connections.FileEntry", 'object', 'string' ], args)) {
						return;
					}
					if (!file.validate("FileService", "addCommentToFile", args, {
						isValidateId : true,
						isValidateUser : true
					})) {

						return;
					}

					var accessType = constants.accessType.AUTHENTICATED;
					var subFilters = new _SubFilters();
					subFilters.setUserId(file.getPersonEntry().getAuthorId());
					subFilters.setDocumentId(file.getId());
					var resultType = constants.resultType.FEED;

					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, null);
					var headers = {};
					headers["Content-Type"] = constants.atom;
					var payload = this._constructPayloadForComment(false, args.comment);
					var _args = args ? lang.mixin({}, args) : {};
					_args["responseFormat"] = constants.responseFormat.SINGLE;
					this._executePost(_args, url, headers, payload);
				},

				/**
				 * Adds comment to a file of logged in user.
				 * @method addCommentToMyFile
				 * @param {Object} file FileEntry object to which comment needs to be added. Id and Comment needs to be set on this object. Use
				 * FileService.getFile() with loadIt false to create this object.
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the comment is added successfully to the file. The function
				 * expects one parameter, the newly created comment object.
				 * @param {Function} [args.error] Sometimes the update calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to update the profile completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				addCommentToMyFile : function(file, args) {

					if (!validate._validateInputTypesAndNotify("FileService", "addCommentToMyFile", [ "File", "args", "comment" ], [ file, args,
							args ? args.comment : null ], [ "sbt.connections.FileEntry", 'object', 'string' ], args)) {
						return;
					}
					if (!file.validate("FileService", "addCommentToMyFile", args, {
						isValidateId : true
					})) {

						return;
					}

					var accessType = constants.accessType.AUTHENTICATED;
					var category = constants.categories.MYLIBRARY;
					var subFilters = new _SubFilters();
					subFilters.setDocumentId(file.getId());
					var resultType = constants.resultType.FEED;

					var url = this._constructUrl(constants.baseUrl.FILES, accessType, category, null, null, subFilters, resultType, null);
					var headers = {};
					headers["Content-Type"] = constants.atom;
					var payload = this._constructPayloadForComment(false, args.comment);
					var _args = args ? lang.mixin({}, args) : {};
					_args["responseFormat"] = constants.responseFormat.SINGLE;
					this._executePost(_args, url, headers, payload);
				},

				/**
				 * Lock a file.
				 * @method lockFile
				 * @param {Object} file FileEntry object which needs to be unlocked
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is locked successfully. The function expects one
				 * parameter, the status of uncock operation.
				 * @param {Function} [args.error] Sometimes the lock file calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to lock the file completes or fails.
				 * The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the
				 * javascript library error object, the status code and the error message.
				 * @param {Object} [args.parameters] The additional parameters like pageSize etc.
				 */
				lockFile : function(file, args) {

					if (!validate._validateInputTypeAndNotify("FileService", "lockFile", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "lockFile", args, {
						isValidateId : true
					})) {

						return;
					}

					var _self = this;
					this._getNonce({
						load : function(nonceValue) {
							var accessType = constants.accessType.AUTHENTICATED;
							var subFilters = new _SubFilters();
							subFilters.setDocumentId(file.getId());
							var resultType = constants.resultType.LOCK;
							var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
							parameters["type"] = "HARD";
							var url = _self._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, parameters);
							var headers = {
								"X-Update-Nonce" : nonceValue
							};
							_self._executePost(args, url, headers, null);
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});
				},

				/**
				 * UnLock a file.
				 * @method unlockFile
				 * @param {Object} file FileEntry object which needs to be unlocked
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is unlocked successfully. The function expects one
				 * parameter, the status of uncock operation.
				 * @param {Function} [args.error] Sometimes the unlock file calls fails due to bad request (400 error). The error parameter is a
				 * callback function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to
				 * the error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to unlock the file completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				unlockFile : function(file, args) {

					if (!validate._validateInputTypeAndNotify("FileService", "unlockFile", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "unlockFile", args, {
						isValidateId : true
					})) {

						return;
					}
					var _self = this;
					this._getNonce({
						load : function(nonceValue) {
							var accessType = constants.accessType.AUTHENTICATED;
							var subFilters = new _SubFilters();
							subFilters.setDocumentId(file.getId());
							var resultType = constants.resultType.LOCK;
							var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
							parameters["type"] = "NONE";
							var url = _self._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, parameters);
							var headers = {
								"X-Update-Nonce" : nonceValue
							};
							_self._executePost(args, url, headers, null);
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});
				},

				/**
				 * Delete a file.
				 * @method deleteFile
				 * @param {Object} file FileEntry object which needs to be deleted
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is deleted successfully. The function expects one
				 * parameter, the status of the delete openration.
				 * @param {Function} [args.error] Sometimes the delete calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to delete the File completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				deleteFile : function(file, args) {

					if (!validate._validateInputTypeAndNotify("FileService", "deleteFile", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "deleteFile", args, {
						isValidateId : true
					})) {

						return;
					}
					var _self = this;
					this._getNonce({
						load : function(nonceValue) {
							var accessType = constants.accessType.AUTHENTICATED;
							var category = constants.categories.MYLIBRARY;
							var subFilters = new _SubFilters();
							subFilters.setDocumentId(file.getId());
							var resultType = constants.resultType.ENTRY;
							var url = _self._constructUrl(constants.baseUrl.FILES, accessType, category, null, null, subFilters, resultType, null);
							var headers = {
								"X-Update-Nonce" : nonceValue
							};
							_self._executeDelete(args, url, headers);
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});

				},

				_getNonce : function(args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var resultType = constants.resultType.NONCE;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, null, resultType);
					var _args = args ? lang.mixin({}, args) : {};
					_args["responseFormat"] = constants.responseFormat.NON_XML_FORMAT;
					this._executeGet(_args, url);
				},
				_loadFile : function(file, args) {
					if (!validate._validateInputTypeAndNotify("FileService", "_loadFile", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "_loadFile", args, {
						isValidateId : true
					})) {

						return;
					}
					var subFilters = new _SubFilters();
					subFilters.setDocumentId(file.getId());
					var url = this._constructUrl(constants.baseUrl.FILES, constants.accessType.AUTHENTICATED, constants.categories.MYLIBRARY, null,
							null, subFilters, constants.resultType.ENTRY);
					var _args = args ? lang.mixin({}, args) : {};
					_args["responseFormat"] = constants.responseFormat.SINGLE;
					this._executeGet(_args, url);
				},

				/**
				 * Retrieve a folder.
				 * @method getFolder						 
				 * @param {Object} [args] Argument object
				 * @param {String} [args.collectionId] the Id of the folder
				 * @param {Function} [args.load] The callback function will invoke when the folder is retrieved successfully. The function expects one
				 * parameter, the status of the retrieve openration.
				 * @param {Function} [args.error] Sometimes the delete calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to get files in foldercompletes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				getFolder : function(args) {
					if (!validate._validateInputTypesAndNotify("FileService", "getFilesInFolder", [ "args", "collectionId" ], [ args,
							args ? args.collectionId : null ], [ 'object', 'string' ], args)) {
						return;
					}
					var accessType = constants.accessType.AUTHENTICATED;
					var subFilters = new _SubFilters();
					subFilters.setCollectionId(args.collectionId);
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType);
					var _args = args ? lang.mixin({}, args) : {};
					_args["responseFormat"] = constants.responseFormat.SINGLE;
					this._executeGet(_args, url);
				},

				/**
				 * Add files a folder.
				 * @method addFilesToFolder						 
				 * @param {Object} [args] Argument object
				 * @param {String} [args.collectionId] the Id of the folder
				 * @param {String} [args.fileIds] comma seperated list of fileIds
				 * @param {Function} [args.load] The callback function will invoke when the file is added successfully. The function expects one
				 * parameter, the status of the add openration.
				 * @param {Function} [args.error] Sometimes the delete calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to Add files a folder completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				addFilesToFolder : function(args) {
					if (!validate._validateInputTypesAndNotify("FileService", "getFilesInFolder", [ "args", "collectionId", "fileIds" ], [ args,
							args ? args.collectionId : null, args ? args.fileIds : null ], [ 'object', 'string', 'string' ], args)) {
						return;
					}

					var _self = this;
					this._getNonce({
						load : function(nonceValue) {
							var accessType = constants.accessType.AUTHENTICATED;
							var subFilters = new _SubFilters();
							subFilters.setCollectionId(args.collectionId);
							var resultType = constants.resultType.FEED;
							var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
							parameters["itemId"] = args.fileIds;
							var url = _self._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, parameters);
							var headers = {
								"X-Update-Nonce" : nonceValue
							};
							var _args = args ? lang.mixin({}, args) : {};
							_args["responseFormat"] = constants.responseFormat.NON_XML_FORMAT;
							_self._executePost(_args, url, headers, null);
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});

				},

				/**
				 * Pin a file, by sending a POST request to the myfavorites feed.
				 * @method pinFile
				 * @param {Object} file FileEntry object which needs to be pinned
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is deleted successfully. The function expects one
				 * parameter, the status of the delete openration.
				 * @param {Function} [args.error] Sometimes the delete calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to pin the File completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				pinFile : function(file, args) {		
					if (!validate._validateInputTypeAndNotify("FileService", "pinFile", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "pinFile", args, {
						isValidateId : true
					})) {

						return;
					}
					
					var _self = this;
                    this._getNonce({
                        load : function(nonceValue) {
                            var accessType = constants.accessType.AUTHENTICATED;
                            var subFilters = new _SubFilters();
                            subFilters.setCollectionId(args.collectionId);
                            var resultType = constants.resultType.FEED;
                            var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
                            parameters["itemId"] = file.getId();
                            var category = constants.categories.PINNED;
                            var view = constants.views.FILES;
                            var url = _self._constructUrl(constants.baseUrl.FILES, accessType, category, view, null, subFilters, resultType, parameters);
                            var _args = args ? lang.mixin({}, args) : {};
                            _args["responseFormat"] = constants.responseFormat.NON_XML_FORMAT;
                            var headers = {
                                "X-Update-Nonce" : nonceValue
                            };
                            _self._executePost(_args, url, headers, null);
                        },
                        error : function(error) {
                            validate.notifyError(error, args);
                        }
                    });
				},
				
				/**
				 * Removes the file from the myfavorites feed.
				 * @method removePinFromFile
				 * @param {Object} file FileEntry object which needs to be pinned
				 * @param {Object} [args] Argument object
				 * @param {Function} [args.load] The callback function will invoke when the file is deleted successfully. The function expects one
				 * parameter, the status of the delete openration.
				 * @param {Function} [args.error] Sometimes the delete calls fails due to bad request (400 error). The error parameter is a callback
				 * function that is only invoked when an error occurs. This allows to write logic when an error occurs. The parameter passed to the
				 * error function is a JavaScript Error object indicating what the failure was. From the error object. one can access the javascript
				 * library error object, the status code and the error message.
				 * @param {Function} [args.handle] This callback function is called regardless of whether the call to remove pin for the File completes or
				 * fails. The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to
				 * the javascript library error object, the status code and the error message.
				 */
				removePinFromFile : function(file, args) {		
					if (!validate._validateInputTypeAndNotify("FileService", "removePinFromFile", "File", file, "sbt.connections.FileEntry", args)) {
						return;
					}
					if (!file.validate("FileService", "removePinFromFile", args, {
						isValidateId : true
					})) {

						return;
					}
					
					var _self = this;
					this._getNonce({
						load : function(nonceValue) {
							var accessType = constants.accessType.AUTHENTICATED;
							var subFilters = new _SubFilters();
							subFilters.setCollectionId(args.collectionId);
							var resultType = constants.resultType.FEED;
							var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
							parameters["itemId"] = file.getId();
							var category = constants.categories.PINNED;
							var view = constants.views.FILES;
							var url = _self._constructUrl(constants.baseUrl.FILES, accessType, category, view, null, subFilters, resultType, parameters);	
							var headers = {
									"X-Update-Nonce" : nonceValue
								};
							_self._executeDelete(args, url, headers, null);
						},
						error : function(error) {
							validate.notifyError(error, args);
						}
					});
				},

				retrieveFileComment : function(file, comment, args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var subFilters = new _SubFilters();
					subFilters.setUserId(file.getPersonEntry().getAuthorId());
					subFilters.setDocumentId(file.getId());
					subFilters.setCommentId(comment.getId());
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, args.parameters);
					this._executeGet(args, url);
				},
				_retrieveMyFileComment : function(file, comment, args) {
					var accessType = constaqnts.accessType.AUTHENTICATED;
					var category = consants.categories.MYLIBRARY;
					var subFilters = new _SubFilters();
					subFilters.setDocumentId(file.getId());
					subFilters.setCommentId(comment.getId());
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, category, null, null, subFilters, resultType, args.parameters);
					this._executeGet(args, url);
				},
				_deleteComment : function(file, comment, args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var subFilters = new _SubFilters();
					subFilters.setUserId(file.getPersonEntry().getAuthorId());
					subFilters.setDocumentId(file.getId());
					subFilters.setCommentId(comment.getId());
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, parameters);
					this._executeDelete(args, url, null);
				},
				_deleteMyComment : function(file, comment, args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var category = consants.categories.MYLIBRARY;
					var subFilters = new _SubFilters();
					subFilters.setDocumentId(file.getId());
					subFilters.setCommentId(comment.getId());
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, category, null, null, subFilters, resultType, parameters);
					this._executeDelete(args, url, null);
				},
				_updateComment : function(file, comment, args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var subFilters = new _SubFilters();
					subFilters.setUserId(file.getPersonEntry().getAuthorId());
					subFilters.setDocumentId(file.getId());
					subFilters.setCommentId(comment.getId());
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, null, null, subFilters, resultType, parameters);
					var headers = {};
					headers["Content-Type"] = constants.atom;
					var payload = this._constructPayloadForComment(false, comment.getComment());
					this._executePut(args, url, headers, payload);
				},
				_updateMyComment : function(file, comment, args) {
					var accessType = constants.accessType.AUTHENTICATED;
					var category = consants.categories.MYLIBRARY;
					var subFilters = new _SubFilters();
					subFilters.setDocumentId(file.getId());
					subFilters.setCommentId(comment.getId());
					var resultType = constants.resultType.ENTRY;
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, category, null, null, subFilters, resultType, parameters);
					var headers = {};
					headers["Content-Type"] = constants.atom;
					var payload = this._constructPayloadForComment(false, comment.getComment());
					this._executePut(args, url, headers, payload);
				},
				_getPublicFiles : function(args) {
					var accessType = constants.accessType.PUBLIC;
					var view = constants.views.FILES;
					var resultType = constants.resultType.FEED;
					var parameters = args.parameters ? lang.mixin({}, args.parameters) : {};
					parameters["visibility"] = "public";
					var url = this._constructUrl(constants.baseUrl.FILES, accessType, null, view, null, null, resultType, parameters);
					this._executeGet(args, url);
				}
			});

			return FileService;
		});
