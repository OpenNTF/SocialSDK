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
 * Javascript APIs for IBM Smartcloud File Service.
 * @module sbt.smartcloud.FileService
 */
define([ 'sbt/_bridge/declare', 'sbt/config', 'sbt/lang', 'sbt/smartcloud/core', 'sbt/xml', 'sbt/xpath', 'sbt/Cache', 'sbt/Endpoint',
		'sbt/smartcloud/FileConstants', 'sbt/smartcloud/Subscriber', 'sbt/base/BaseService', 'sbt/log', 'sbt/stringutil', 'sbt/validate' ], function(declare,
		cfg, lang, con, xml, xpath, Cache, Endpoint, FileConstants, Subscriber, BaseService, log, stringutil, validate) {

	function notifyCbNoCache(args, param) {

		if (args) {
			if (args.load)
				args.load(param);
			else if (args.handle)
				args.handle(param);
		} else {
			log.error("Callbacks not defined. Return Value={0}", param);
		}
	}

	/**
	 * File class associated with a file. 
	 * @namespace smartcloud
	 * @class File
	 * @constructor
	 * @param {Object} FileService  fileService object
	 * @param {String} id file id associated with the file.
	 */
	var FileEntry = declare("sbt.smartcloud.FileEntry", sbt.base.BaseEntity, {

		constructor : function(svc, id) {
			var args = {
				entityName : "File",
				Constants : FileConstants,
				con : con
			};
			this.inherited(arguments, [ svc, id, args ]);
		},

		validate : function(className, methodName, args, validateMap) {
			if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "File Id", this._id, "string", args))) {
				return false;
			}
			return true;
		},
		getId : function() {
			return this._id;
		},
		getName : function() {
			return this.get("title");
		},

		getVisibility : function() {

			return this.get("fileEntryVisibility");
		},

		isExternal : function() {

			return this.get("fileEntryIsExternal");
		},

		getFileId : function() {

			return this.get("fileEntryId");
		},

		getContentLink : function() {

			return this.get("contentLink");
		},

		getEditMediaLink : function() {

			return this.get("editMediaLink");
		},

		getLastStreamModifiedDate : function() {

			return this.getDate("fileEntryStreamLastModified");
		},

		getCreatedBy : function() {
			return this.extractProfileFromKey("fileEntryCreatedBy");
		},

		getCheckedOutBy : function() {
			return this.extractProfileFromKey("fileEntryCheckedOutBy");
		},

		getEntityURL : function() {
			return this.get("selfURL");
		},

		isEncrypted : function() {
			return this.get("fileEntryIsEncrypted");
		},

		getAuthorOrganizationName : function() {
			return this.get("authorOrgName");
		},

		getACLUrl : function() {
			return this.get("aclURL");
		},

		getUserState : function() {
			return this.get("userState");
		},

		getObjectTypeId : function() {
			return this.get("fileEntryObjectTypeId");
		},

		getLastEditDate : function() {
			return this.getDate("lastEditDate");
		},

		getLockedBy : function() {
			return this.extractProfileFromKey("fileEntryLockedBy");
		},

		getModifiedBy : function() {
			return this.extractProfileFromKey("fileEntryModifiedBy");
		},

		extractProfileFromKey : function(entryKey) {
			var entry = xpath.selectNodes(this._data, FileConstants.xpath_File[entryKey], con.namespaces);
			var node = entry[0];
			if (node) {
				var id = xpath.selectText(node, FileConstants.xpath_UserProfile["profileNode_userId"], con.namespaces);
				var userProfile = UserProfile.apply(null, [ this._service, id ]);
				userProfile.setData(node);
				return userProfile;
			}
		},

		getDownloadsAnonCount : function() {
			return this.get("fileEntryDownloadCountAnon");
		},

		getLastEntryModifiedDate : function() {
			return this.getDate("fileEntryLastModificationDate");
		},

		getDescrptionLink : function() {
			return this.get("descriptionLink");
		},

		getRepositoryTypeId : function() {
			return this.get("fileEntryRepositoryType");
		},

		getBaseTypeId : function() {
			return this.get("fileEntryBaseTypeId");
		},

		getCreationDate : function() {
			return this.getDate("fileEntryCreationDate");
		},

		getEditLink : function() {
			return this.get("editLink");
		},

		getDownloadLink : function() {
			return this.get("downloadLink");
		},

		isLastVersion : function() {

			return this.get("fileEntryIsLastVersion");
		},

		getReccomendationsCount : function() {

			return this.get("fileEntryRecommendationCount");
		},

		getSharingInformationURL : function() {
			return this.get("sharingInformationURL");
		},

		isMajorVersion : function() {
			return this.get("fileEntryIsMajorVersion");
		},

		getOranizationId : function() {
			return this.get("fileEntryOrgnizationId");
		},

		getContentStreamId : function() {
			return this.get("fileEntryContentStreamId");
		},

		getSummary : function() {

			return this.get("summary");
		},

		isPublic : function() {
			return this.get("fileEntryIsPublic");
		},

		/**
		 * An URL that points to the file page on the SmartCloud server, to be consumed by humans
		 * 
		 * @return a string representing the URL
		 */
		getPageURL : function() {
			return this.get("pageLink");
		},

		/**
		 * @return the total size for the entry
		 */
		getTotalSize : function() {
			return this.get("fileEntryTotalSize");
		},

		/**
		 * The identifier for this entry
		 * 
		 * @return a string
		 */
		getObjectId : function() {
			return this.get("fileEntryObjectId");
		},

		/**
		 * The number of downloads for the file represented by this entry
		 * 
		 * @return an Integer if the information is available or null
		 */
		getDownloadCount : function() {
			return this.get("fileEntryDownloadCount");
		},

		getVersionHistoryURL : function() {
			return this.get("versionHistoryURL");
		},

		getVersionSeriesId : function() {
			return this.get("fileEntryVersionSeriesId");
		},

		getRelationshipsURL : function() {
			return this.get("relationshipsURL");
		},

		getRecommendationsURL : function() {
			return this.get("recommendationsURL");
		},

		getAuthorName : function() {
			return this.get("authorName");
		},

		getDisplayName : function() {
			return this.get("fileEntryDisplayName");
		},

		getContentLanguage : function() {
			return this.get("fileEntryContentLanguage");
		},

		isImmutable : function() {
			return this.get("fileEntryIsImmutable");
		},

		isLatestMajorVersion : function() {
			return this.get("fileEntryIsLatestMajorVersion");
		},

		isVersionSeriesCheckedOut : function() {
			return this.get("fileEntryIsVersionSeriesCheckedOut");
		},

		getUserId : function() {
			return this.get("userId");
		},

		isViral : function() {
			return this.get("fileEntryIsViral");
		},

		getPermissionsURL : function() {
			return this.get("permissionsURL");
		},

		getACLRemoverURL : function() {
			return this.get("aclRemoverURL");
		},

		getPathSegment : function() {
			return this.get("pathSegment");
		},

		getContentStreamMimeType : function() {
			return this.get("fileEntryContentStreamMimeType");
		},

		getAuthorEmail : function() {
			return this.get("authorEmail");
		},

		getVersionHistoryAlternateURL : function() {
			return this.get("versionHistoryAlternateURL");
		},

		getOrganizationName : function() {
			return this.get("fileEntryOrganizationName");
		},

		getAuthorOrgId : function() {
			return this.get("authorOrgId");
		},

		getTitle : function() {
			return this.get("title");
		},

		getSharePermission : function() {
			return this.get("fileEntrySharePermission");
		},

		getRepositoryId : function() {
			return this.get("fileEntryrepositoryId");
		},

		getPublishDate : function() {
			return this.getDate("publishDate");
		},

		getLastUpdateDate : function() {
			return this.getDate("lastUpdateDate");
		},

		getLockedWhen : function() {
			return this.getDate("fileEntryLockedWhen");
		},

		getChangeTokenDate : function() {
			return this.getDate("fileEntryChangeToken");
		},

		getAuthorPrincipalId : function() {
			return this.get("authorPrincipalId");
		},

		getDownloadHistoryURL : function() {
			return this.get("downloadHistoryURL");
		},

		getACLHistoryURL : function() {
			return this.get("aclHistoryURL");
		},

		getDataURL : function() {
			return this.get("dataURL");
		},

		getCommentCount : function() {
			return this.get("fileEntryCommentCount");
		},

		getCheckinComment : function() {
			return this.get("fileEntryCheckinComment");
		},

		getContentStreamLength : function() {
			return this.get("fileEntryContentStreamLength");
		},

		getContentStreamFileName : function() {
			return this.get("fileEntryContentStreamFileName");
		},

		getOtherEmail : function() {
			return this.get("otherEmail");
		},

		getFileExtension : function() {
			return this.get("fileEntryFileExtension");
		},

		getVersionLabel : function() {
			return this.get("fileEntryVersionLabel");
		},

		getRecommendationURL : function() {
			return this.get("recommendationURL");
		},

		getServiceURL : function() {
			return this.get("serviceURL");
		},

		getLockType : function() {
			return this.get("fileEntryLockType");
		},

		getVersionSeriesCheckedOutId : function() {
			return this.get("fileEntryVersionSeriesCheckedOutId");
		},

		getPoliciesURL : function() {
			return this.get("policiesURL");
		},

		isRecommendedByCaller : function() {
			return this.get("fileEntryIsRecommendedByCaller");
		},

		getAuthorDisplayName : function() {
			return this.get("authorDisplayName");
		}

	});

	/**
	 * UserProfile class associated with a User associated with a File. 
	 * @namespace smartcloud
	 * @class UserProfile
	 * @constructor
	 * @param {Object} FileService  fileService object
	 * @param {String} id file id associated with the file.
	 */
	var UserProfile = declare("sbt.smartcloud.UserProfile", sbt.base.BaseEntity, {

		constructor : function(svc, id) {
			var args = {
				entityName : "UserProfile",
				Constants : FileConstants,
				con : con
			};
			this.inherited(arguments, [ svc, id, args ]);
		},

		getName : function() {
			return this.get("profileNode_name");
		},

		getValue : function() {
			return this.get("profileNode_value");
		},

		getOtherEmail : function() {
			return this.get("profileNode_otherEmail");
		},

		getDisplayName : function() {
			return this.get("profileNode_displayName");
		},

		getOrgId : function() {
			return this.get("profileNode_orgId");
		},

		getUserState : function() {
			return this.get("profileNode_userState");
		},

		getOrgName : function() {
			return this.get("profileNode_orgName");
		},

		getUserId : function() {
			return this.get("profileNode_userId");
		},

		getPrincipalId : function() {
			return this.get("profileNode_principalId");
		},

		getEmail : function() {
			return this.get("profileNode_email");
		}
	});

	/**
	 * 	File service class associated with the file service of IBM SmartCloud.
	 *
	 * @namespace smartcloud
	 * @class FileService
	 * @constructor
	 * @param {Object} options  Options object
	 * @param {String} [options.endpoint=smartcloud]  Endpoint to be used by FileService.
	 * 		
	 */
	var FileService = declare("sbt.smartcloud.FileService", sbt.base.BaseService, {

		_subscriberId : null,
		_endpointName : null,
		constructor : function(_options) {
			var options = _options || {};
			this._endpointName = options.endpoint || "smartcloud";
			options = lang.mixin({
				endpoint : this._endpointName,
				Constants : FileConstants,
				con : con,
				cachingEnabled : false
			});
			this.inherited(arguments, [ options ]);
		},

		_constructPayloadForUpdate : function(fileTitle, propertiesMap) {
			var payload = "<entry xmlns=\"http://www.w3.org/2005/Atom\"> <title type='text'>" + fileTitle + "</title><cmisra:object> <cmis:properties> ";
			for ( var property in propertiesMap) {
				var propertyType = FileConstants.fileUpdateProperties[property];
				var value = propertiesMap[property];
				payload += "<cmis:property" + propertyType + " propertyDefinitionId='snx:" + property + "'> <cmis:value>" + value
						+ "</cmis:value>  </cmis:property" + propertyType + ">";
			}
			payload += "</cmis:properties> </cmisra:object> </entry>";
			return payload;
		},

		_getForURLQuery : function(filterList) {
			// adding mandatory fields as per documentation
			filterList.push("cmis:objectId");
			filterList.push("cmis:objectTypeId");
			var first = true;
			var concatenation = "";
			for ( var e in filterList) {
				if (!first) {
					concatenation += ",";
				} else {
					first = false;
				}
				concatenation += filterList[e];
			}
			// NOTE: not encoded because it will be encoded after being passed to the base service
			return concatenation;

		},
		_createEntityObject : function(service, id, entityName) {
			if ("File" == entityName) {
				return new FileEntry(service, id);
			} else {
				return new UserProfile(service, id);
			}

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
		getMyFiles : function getMyFiles(args) {
			var _self = this;
			if (!this._subscriberId) {
				var subscriber = new Subscriber(this._endpoint);
				subscriber.load(function(subscriber, response) {
					if (subscriber) {
						_self._subscriberId = subscriber.getSubscriberId(response);
						_self._getEntities(args, {
							entityName : "File",
							serviceEntity : "FILE",
							entityType : "GET_MY_FILES",
							replaceArgs : {
								subscriberId : _self._subscriberId
							},
							entity : FileEntry
						});
					}
				});
			}
		},
		/**
		 * Gets the files of the logged in user.
		 * @method getMyFilesAlt
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
		getMyFilesAlt : function getMyFiles(args) {
			var _self = this;
			if (!this._subscriberId) {
				var subscriber = new Subscriber(this._endpoint);
				subscriber.load(function(subscriber, response) {
					if (subscriber) {
						_self._subscriberId = subscriber.getSubscriberId(response);
						_self._getEntities(args, {
							entityName : "File",
							serviceEntity : "FILE",
							entityType : "GET_MY_FILES_ALT",
							replaceArgs : {
								subscriberId : _self._subscriberId
							},
							entity : FileEntry
						});
					}
				});
			}
		},
		/**
		 * Gets the files of the logged in user with pagination.
		 * @method getMyFilesWithPagination
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
		 * @param {Integer} [args.maxItems = null] max items to be fetched.
		 * @param {Integer} [args.skipCount = null] the skip count.
		 * @param {Object} [args.filter = null] optional additional filters
		 */
		getMyFilesWithPagination : function getMyFiles(args) {
			if (!validate._validateInputTypeAndNotify("FileService", "getMyFilesWithPagination", "args", args, 'object', args)) {
				return;
			}
			var _self = this;
			if (!this._subscriberId) {
				var subscriber = new Subscriber(this._endpoint);
				subscriber.load(function(subscriber, response) {
					if (subscriber) {
						_self._subscriberId = subscriber.getSubscriberId(response);
						var parameters = {};
						if (args.filter) {
							parameters["filter"] = _self._getForURLQuery(args.filter);
						}
						if (args.includeACL) {
							parameters["includeACL"] = "true";
						}
						if (args.maxItems) {
							parameters["maxItems"] = args.maxItems;
						}
						if (args.skipCount) {
							parameters["skipCount"] = args.skipCount;
						}
						_self._getEntities(args, {
							entityName : "File",
							serviceEntity : "FILE",
							entityType : "GET_MY_FILES_WITH_FILTER",
							replaceArgs : {
								subscriberId : _self._subscriberId
							},
							entity : FileEntry,
							urlParams : parameters
						});
					}
				});
			}
		},
		/**
		 * Gets the files entry.
		 * @method getFilesSharedByMe
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
		getFileEntry : function readFileEntry(args) {
			if (!validate._validateInputTypesAndNotify("FileService", "getFileComments", [ "args", "fileId" ], [ args, args ? args.id : null ], [ "object",
					"string" ], args)) {
				return;
			}
			var _self = this;
			if (!this._subscriberId) {
				var subscriber = new Subscriber(this._endpoint);
				subscriber.load(function(subscriber, response) {
					if (subscriber) {
						_self._subscriberId = subscriber.getSubscriberId(response);
						var parameters = {};
						if (args.filter) {
							parameters["filter"] = _self._getForURLQuery(args.filter);
						}
						if (args.includeACL) {
							parameters["includeACL"] = "true";
						}
						_self._getOne(args, {
							entityName : "File",
							serviceEntity : "ENTRY",
							entityType : "GET_FILE_ENTRY",
							replaceArgs : {
								subscriberId : _self._subscriberId,
								fileId : args.id
							},
							entity : FileEntry,
							urlParams : parameters
						});
					}
				});
			}
		},
		/**
		 * Uploads a new file for logged in user.
		 * @method uploadFile
		 * @param {Object} fileControl FileEntry object with file path of file which needs to be uploaded, Use FileService.getFile() with loadIt
		 * false to create this object.
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
		 */
		uploadFile : function(args) {

			var _self = this;
			if (!this._subscriberId) {
				var subscriber = new Subscriber(this._endpoint);
				subscriber.load(function(subscriber, response) {
					if (subscriber) {
						_self._subscriberId = subscriber.getSubscriberId(response);
						var fileControl = document.getElementById(args.fileLocation);
						var files = fileControl.files;
						var filePath = fileControl.value;
						var reader = new FileReader();
						reader.onload = function(event) {
							var binaryContent = event.target.result;
							var url = _self._constructServiceUrl({
								entityName : "File",
								serviceEntity : "FILE",
								entityType : "POST_FILE_UPLOAD",
								replaceArgs : {
									subscriberId : _self._subscriberId
								}
							});
							url = cfg.Properties.serviceUrl + "/files/" + _self._endpointName + url;
							var headers = {};
							var index = filePath.lastIndexOf("\\");
							if (index == -1) {
								index = filePath.lastIndexOf("/");
							}
							headers["Slug"] = filePath.substring(index + 1);
							_self._endpoint.xhrPost({
								url : url,
								postData : binaryContent,
								headers : headers,
								load : function(data) {
									notifyCbNoCache(args, "SUCCESS");
								},
								error : function(error) {
									validate.notifyError(error, args);
								}
							});
						};
						reader.onerror = function(event) {
							validate.notifyError(error, args);
						};
						reader.readAsArrayBuffer(files[0]);
					}
				});
			}
		},
		updateFile : function(args) {
			var _self = this;
			if (!this._subscriberId) {
				var subscriber = new Subscriber(this._endpoint);
				subscriber.load(function(subscriber, response) {
					if (subscriber) {
						_self._subscriberId = subscriber.getSubscriberId(response);
						_self._getOne({
							load : function(file) {
								var updatePayload = _self._constructPayloadForUpdate(file.getName(), args.updateProperties);
								_self._updateEntity(args, {
									xmlPayload : updatePayload,
									entityName : "File",
									serviceEntity : "ENTRY",
									entityType : "UPDATE_FILE",
									replaceArgs : {
										subscriberId : _self._subscriberId,
										fileId : args.id
									},
									entity : FileEntry
								});

							},
							error : function(error) {
								validate.notifyError(error, args);
							}
						}, {
							entityName : "File",
							serviceEntity : "ENTRY",
							entityType : "GET_FILE_ENTRY",
							replaceArgs : {
								subscriberId : _self._subscriberId,
								fileId : args.id
							},
							entity : FileEntry,
						});
					}
				});
			}

		}
	});
	return FileService;
});
