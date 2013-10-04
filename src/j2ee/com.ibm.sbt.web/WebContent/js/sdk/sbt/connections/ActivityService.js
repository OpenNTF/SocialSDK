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
 * The Activities application of IBM® Connections enables a team to collect, organize, share, and reuse work related to a project goal. The Activities API
 * allows application programs to create new activities, and to read and modify existing activities.
 * 
 * @module sbt.connections.ActivityService
 */
define(
		[ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./ActivityConstants", "../base/BaseService", "../base/BaseEntity",
				"../base/XmlDataHandler", "../xml" ],
		function(declare, config, lang, stringUtil, Promise, consts, BaseService, BaseEntity, XmlDataHandler, xml) {

			var ActivityNodeTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" "
					+ "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\">"
					+ "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"${entryType}\" label=\"${type}\" /> "
					+ "<content type=\"html\">${content}</content><title type=\"text\">${title}</title>"
					+ "${getPosition}${getCommunity}${getTags}${getCompleted}${getCompleted}${getDueDate}${getInReplyTo}${getAssignedTo}${getIcon}"
					+ "${getFields}</entry>";
			var PositionTmpl = "<snx:position>${position}</snx:position>";
			var CommunityTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"community_activity\" label=\"Community Activity\"/><snx:communityUuid>${coummunityId}</communityUuid>"
					+ "<link rel=\"http://www.ibm.com/xmlns/prod/sn/container\" type=\"application/atom+xml\" href=\"${communityLink}\"/>";
			var TagTmpl = "<category term=\"${tag}\" /> ";
			var CompletedTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"completed\"/>";
			var TemplateTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"template\"/>";
			var DueDateTmpl = "<snx:duedate>${dueDate}</duedate>";
			var InReplytoTmpl = "<thr:in-reply-to ref=\"${inReplyToId}\" type=\"application/atom+xml\" href=\"${inReplyToUrl}\" source=\"${activityId}\" />";
			var FieldTmpl = "<snx:field name=\"${name}\" fid=\"${fid}\" position=\"${position}\" type=\"${type}\">${getText}${getPerson}${getDate}${getLink}${getFile}</snx:field>";
			var TextFieldTmpl = "<summary type=\"text\">${summary}</summary>";
			var PersonFieldTmpl = "<name>${personName}</name> <email>{email}</email> <snx:userid>${userId}</snx:userid>";
			var LinkFieldTmpl = "<link href=\"${url\}\" title=\"${title}\" />";
			var DateFieldTmpl = "${date}";
			var IconTmpl = "<snx:icon>${iconUrl}</snx:icon>";
			var AssignedToTmpl = "<snx:assignedto name=\"${name}\" userid=\"${userId}\">${email}</snx:assignedto>";
			var MemberTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"> "
					+ "<contributor> ${getEmail} ${getUserid} </contributor> ${getRole} ${getCategory} </entry>";
			var RoleTmpl = "<snx:role xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" component=\"http://www.ibm.com/xmlns/prod/sn/activities\">${role}</snx:role>";
			var EmailTmpl = "<email>${email}</email>";
			var UseridTmpl = "<snx:userid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">${userid}</snx:userid>";
			var CategoryTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"${category}\" label=\"${category}\" />";

			/**
			 * Field class represents a Field in an Activity Node.
			 * 
			 * @class Field
			 * @namespace sbt.connections
			 */
			var Field = declare(null, {
				name : null,
				fid : null,
				position : null,
				type : null,

				/**
				 * Returns Field Name
				 * @method getName
				 * @returns {String} field name
				 */
				getName : function() {
					return this.name;
				},

				/**
				 * Sets Field Name
				 * @method setName
				 * @param {String} field name
				 */
				setName : function(newName) {
					this.name = newName;
				},

				/**
				 * Returns Field ID
				 * @method getFid
				 * @returns {String} field ID
				 */
				getFid : function() {
					return this.fid;
				},

				/**
				 * Returns Field Position
				 * @method getPosition
				 * @returns {String} field position
				 */
				getPosition : function() {
					return this.position;
				},

				/**
				 * Returns Field Type
				 * @method getType
				 * @returns {String} field type
				 */
				getType : function() {
					return this.type;
				}
			});

			/**
			 * TextField class represents a Text Field in an Activity Node.
			 * 
			 * @class TextField
			 * @namespace sbt.connections
			 */
			var TextField = declare(Field, {
				summary : null,

				/**
				 * Returns Field Summary
				 * @method getSummary
				 * @returns {String} field summary
				 */
				getSummary : function() {
					return this.summary;
				},

				/**
				 * Sets Field Summary
				 * @method setSummary
				 * @param {String} field summary
				 */
				setSummary : function(newSumamry) {
					this.summary = newSummary;
				}

			});

			/**
			 * DateField class represents a Date Field in an Activity Node.
			 * 
			 * @class DateField
			 * @namespace sbt.connections
			 */
			var DateField = declare(Field, {
				date : null,

				/**
				 * Returns Field Date
				 * @method getDate
				 * @returns {Date} field date
				 */
				getDate : function() {
					return this.date;
				},
				/**
				 * Sets Field Date
				 * @method setDate
				 * @param {Date} field date
				 */
				setDate : function(newDate) {
					this.date = newDate;
				}

			});

			/**
			 * LinkField class represents a Link Field in an Activity Node.
			 * 
			 * @class LinkField
			 * @namespace sbt.connections
			 */
			var LinkField = declare(Field, {
				url : null,
				title : null,
				/**
				 * Returns Link Field URL
				 * @method getUrl
				 * @returns {String} field Url
				 */
				getUrl : function() {
					return this.url;
				},
				/**
				 * Sets Link Field URL
				 * @method setUrl
				 * @param {String} field Url
				 */
				setUrl : function(newUrl) {
					this.url = newUrl;
				},
				/**
				 * Returns Link Field Title
				 * @method getTitle
				 * @returns {String} field Title
				 */
				getTitle : function() {
					return this.title;
				},
				/**
				 * Sets Link Field Title
				 * @method setTitle
				 * @param {String} field Title
				 */
				setTitle : function(title) {
					this.title = title;
				}

			});

			/**
			 * FileField class represents a File Field in an Activity Node.
			 * 
			 * @class FileField
			 * @namespace sbt.connections
			 */
			var FileField = declare(Field, {
				url : null,
				fileType : null,
				size : null,
				length : null,
				/**
				 * Returns File Field URL
				 * @method getUrl
				 * @returns {String} field URL
				 */
				getUrl : function() {
					return this.url;
				},
				/**
				 * Returns File Field File Type
				 * @method getFileType
				 * @returns {String} File Type
				 */
				getFileType : function() {
					return this.type;
				},

				/**
				 * Returns File Field File Size
				 * @method getSize
				 * @returns {String} File Size
				 */
				getSize : function() {
					return this.size;
				},
				/**
				 * Returns File Field File Length
				 * @method getLength
				 * @returns {String} File Length
				 */
				getLength : function() {
					return this.length;
				}
			});

			/**
			 * PersonField class represents a Person Field in an Activity Node.
			 * 
			 * @class PersonField
			 * @namespace sbt.connections
			 */
			var PersonField = declare(Field, {
				personName : null,
				userId : null,
				email : null,

				/**
				 * Returns Person Name
				 * @method getPersonName
				 * @returns {String} Person Name
				 */
				getPersonName : function() {
					return this.personName;
				},
				/**
				 * Sets Person Name
				 * @method setPersonName
				 * @param {String} Person Name
				 */
				setPersonName : function(newName) {
					this.personName = newName;
				},
				/**
				 * Returns Person User ID
				 * @method getUserId
				 * @returns {String} Person User ID
				 */
				getUserId : function() {
					return this.userId;
				},
				/**
				 * Sets Person User ID
				 * @method setUserId
				 * @param {String} Person User ID
				 */
				setUserId : function(newUserId) {
					this.userId = newUserId;
				},
				/**
				 * Returns Person Email
				 * @method getEmail
				 * @returns {String} Person Email
				 */
				getEmail : function() {
					return this.email;
				},
				/**
				 * Sets Person Email
				 * @method setEmail
				 * @param {String} Person Email
				 */
				setEmail : function(newEmail) {
					this.email = newEmail;
				}
			});

			/**
			 * Tag class represents an entry for a Tag feed returned by the Connections REST API.
			 * 
			 * @class Tag
			 * @namespace sbt.connections
			 */
			var Tag = declare(BaseEntity, {
				/**
				 * Construct a Tag entity.
				 * 
				 * @constructor
				 * @param args
				 */
				constructor : function(args) {
				},

				/**
				 * Returns tag term
				 * @method getTerm
				 * @returns {String} tag term
				 */
				getTerm : function() {
					return this.getAsString("term");
				},
				/**
				 * Returns tag frequency
				 * @method getFrequency
				 * @returns {String} tag frequency
				 */
				getFrequency : function() {
					return this.getAsString("frequency");
				},

				/**
				 * Returns tag bin
				 * @method getBin
				 * @returns {String} tag bin
				 */
				getBin : function() {
					return this.getAsString("bin");
				}
			});

			/**
			 * Activity class represents an entry for a activities feed returned by the Connections REST API.
			 * 
			 * @class Activity
			 * @namespace sbt.connections
			 */
			var Activity = declare(BaseEntity, {

				/**
				 * Construct an Activity entity.
				 * 
				 * @constructor
				 * @param args
				 */
				constructor : function(args) {
				},

				/**
				 * Return the value of id from result ATOM entry document.
				 * 
				 * @method getId
				 * @return {String} ID of the activity
				 */
				getActivityId : function() {
					var id = this.id || this._fields.id || this.getAsString("uid");
					if (id) {
						var index = id.indexOf("urn:lsid:ibm.com:oa:");
						if (index != -1) {
							var len = "urn:lsid:ibm.com:oa:".length;
							id = id.substring(index + len);
						}
					}
					return id;
				},

				/**
				 * Returns Activity Node Title
				 * 
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},

				/**
				 * Sets Activity Node Title
				 * 
				 * @method setTitle
				 * @param {String} title
				 */
				setTitle : function(title) {
					return this.setAsString("title", title);
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
				 * Returns the author
				 * 
				 * @method getAuthor
				 * @returns {Object} author
				 */
				getAuthor : function() {
					return this.getAsObject([ "authorName", "authorUserId", "authorEmail", "authorUserState", "authorLdapid" ]);
				},

				/**
				 * Returns the contributor
				 * 
				 * @method getContributor
				 * @returns {Object} contributor
				 */
				getContributor : function() {
					return this.getAsObject([ "contributorName", "contributorUserId", "contributorEmail", "contributorUserState", "contributorLdapid" ]);
				},

				/**
				 * Returns Activity Node Type
				 * 
				 * @method getEntryType
				 * @returns {String} type
				 */
				getEntryType : function() {
					return this.getAsString("entryType");
				},

				/**
				 * Sets Activity Node Type
				 * @method setEntryType
				 * @param {String} type
				 */
				setEntryType : function(type) {
					return this.setAsString("entryType", type);
				},
				/**
				 * Returns Activity Node Priority
				 * 
				 * @method getPriority
				 * @returns {String} priority
				 */
				getPriority : function() {
					return this.getAsString("priority");
				},

				/**
				 * Return tags of IBM Connections activity from activity ATOM entry document.
				 * 
				 * @method getTags
				 * @return {Object} Array of tags of the activity
				 */
				getTags : function() {
					return this.getAsArray("tags");
				},

				/**
				 * Set new tags to be associated with this IBM Connections activity.
				 * 
				 * @method setTags
				 * @param {Object} Array of tags to be added to the activity
				 */

				setTags : function(tags) {
					return this.setAsArray("tags", tags);
				},

				/**
				 * Returns the dueDate Date
				 * 
				 * @method getDueDate
				 * @returns {Date} DueDate
				 */
				getDueDate : function() {
					return this.getAsDate("dueDate");
				},

				/**
				 * Sets the Due Date of Activity
				 * @method setDueDate
				 * @param {Date} dueDate
				 */
				setDueDate : function(dueDate) {
					return this.setAsDate("dueDate", dueDate);
				},

				/**
				 * Returns Activity Node Members Url
				 * 
				 * @method getMembersUrl
				 * @returns {String} membersUrl
				 */
				getMembersUrl : function() {
					return this.getAsString("membersUrl");
				},

				/**
				 * Returns Activity Node History Url
				 * 
				 * @method getHistoryUrl
				 * @returns {String} historyUrl
				 */
				getHistoryUrl : function() {
					return this.getAsString("historyUrl");
				},

				/**
				 * Returns Activity Node Templates Url
				 * 
				 * @method getTemplatesUrl
				 * @returns {String} templatesUrl
				 */
				getTemplatesUrl : function() {
					return this.getAsString("templatesUrl");
				},

				/**
				 * Returns Activity Node Edit Url
				 * 
				 * @method getEditUrl
				 * @returns {String} editUrl
				 */
				getEditUrl : function() {
					return this.getAsString("editUrl");
				},

				/**
				 * Returns Activity Node Self Url
				 * 
				 * @method getSelfUrl
				 * @returns {String} selfUrl
				 */
				getSelfUrl : function() {
					return this.getAsString("selfUrl");
				},

				/**
				 * Returns Activity Node Alternate Url
				 * 
				 * @method getAlternateUrl
				 * @returns {String} alternateUrl
				 */
				getAlternateUrl : function() {
					return this.getAsString("alternateUrl");
				},

				/**
				 * Returns Activity Position
				 * 
				 * @method getPosition
				 * @returns {String} position
				 */
				getPosition : function() {
					return this.getAsString("position");
				},

				/**
				 * Sets Activity Position
				 * @method setPosition
				 * @param position
				 */
				setPosition : function(position) {
					return this.setAsString("position", position);
				},

				/**
				 * Returns Completed Flag for Activity
				 * @method getCompleted
				 * @returns {String} completed flag
				 */
				getCompleted : function() {
					return this.getAsString("categoryFlagCompleted");
				},

				/**
				 * 
				 * @param completed
				 * @returns
				 */
				setCompleted : function(completed) {
					return this.setAsString("completed", completed);
				},

				/**
				 * 
				 * @returns {Boolean} isDelete
				 */
				isDeleted : function() {
					var deleteFlag = this.getAsString("categoryFlagDelete");
					return deleteFlag != null && deleteFlag != "";
				},

				/**
				 * 
				 * @returns {String} template
				 */
				getTemplate : function() {
					return this.getAsString("categoryFlagTemplate");
				},

				/**
				 * 
				 * @param template
				 * @returns
				 */
				setTemplate : function(template) {
					return this.setAsString("template", template);
				},

				/**
				 * Returns Activity Node Depth
				 * 
				 * @method getDepth
				 * @returns {String} depth
				 */
				getDepth : function() {
					return this.getAsString("depth");
				},

				/**
				 * Returns Activity Node Permissions
				 * 
				 * @method getPermissions
				 * @returns {String} permissions
				 */
				getPermissions : function() {
					return this.getAsString("permissions");
				},

				/**
				 * Returns Activity Node IconUrl
				 * 
				 * @method getIconUrl
				 * @returns {String} iconUrl
				 */
				getIconUrl : function() {
					return this.getAsString("iconUrl");
				},

				/**
				 * setIconUrl
				 * @param iconUrl
				 */
				setIconUrl : function(iconUrl) {
					return this.setAsString("iconUrl", iconUrl);
				},

				/**
				 * Returns Activity Node Content
				 * 
				 * @method getContent
				 * @returns {String} content
				 */
				getContent : function() {
					return this.getAsString("content");
				},

				/**
				 * @method setContent
				 * @param content
				 */
				setContent : function(content) {
					return this.setAsString("content", content);
				},

				/**
				 * getCommunityId
				 * @method getCommunityId
				 * @returns {String} communityId
				 */
				getCommunityId : function() {
					return this.getAsString("communityId");
				},

				/**
				 * setCommunityId
				 * @method setCommunityId
				 * @param communityId
				 */
				setCommunityId : function(communityId) {
					return this.setAsString("communityId", communityId);
				},
				/**
				 * getCommunityLink
				 * @method getCommunityLink
				 * @returns {String} communityLink
				 */
				getCommunityLink : function() {
					return this.getAsString("communityLink");
				},

				/**
				 * setCommunityLink
				 * @method setCommunityLink
				 * @param communityLink
				 */
				setCommunityLink : function(communityLink) {
					return this.setAsString("communityLink", communityLink);
				},

				copyFields : function(activity) {
					this.setCommunityId(this._fields.communityId || activity.getCommunityId());
					this.setCommunityLink(this._fields.communityLink || activity.getCommunityLink());
					this.setCompleted(this._fields.completed || activity.getCompleted());
					this.setContent(this._fields.content || activity.getContent());
					this.setDueDate(this._fields.dueDate || activity.getDueDate());
					this.setIconUrl(this._fields.iconUrl || activity.getIconUrl());
					this.setPosition(this._fields.position || activity.getPosition());
					this.setTags(this._fields.tags || activity.getTags());
					this.setTemplate(this._fields.template || activity.getTemplate());
					this.setTitle(this._fields.title || activity.getTitle());
					this.setEntryType(this._fields.entryType || activity.getEntryType());
				}
			});

			/**
			 * Activity Node class represents an entry for a activities Node feed returned by the Connections REST API.
			 * 
			 * @class ActivityNode
			 * @namespace sbt.connections
			 */
			var ActivityNode = declare(Activity, {
				/**
				 * Construct a Result entity.
				 * 
				 * @constructor
				 * @param args
				 */
				constructor : function(args) {
				},

				/**
				 * Return the value of id from result ATOM entry document.
				 * 
				 * @method getId
				 * @return {String} ID of the result
				 */
				getActivityNodeId : function() {
					var id = this.id || this._fields.id || this.getAsString("uid");
					if (id) {
						var index = id.indexOf("urn:lsid:ibm.com:oa:");
						if (index != -1) {
							var len = "urn:lsid:ibm.com:oa:".length;
							id = id.substring(index + len);
						}
					}
					return id;
				},

				/**
				 * Return the value of activity id from result ATOM entry document.
				 * 
				 * @method getActivityId
				 * @return {String} getActivityId of the result
				 */
				getActivityId : function() {
					return this.getAsString("activityId");
				},

				/**
				 * 
				 * @param activityId
				 * @returns
				 */
				setActivityId : function(activityId) {
					return this.setAsString("activityId", activityId);
				},

				/**
				 * getInReplyToId
				 * @returns {String} getInReplyToId
				 */
				getInReplyToId : function() {
					var id = this.getAsString("inReplyToId");
					if (id) {
						var index = id.indexOf("urn:lsid:ibm.com:oa:");
						if (index != -1) {
							var len = "urn:lsid:ibm.com:oa:".length;
							id = id.substring(index + len);
						}
					}
					return id;
				},

				/**
				 * getInReplyToUrl
				 * @returns {String} getInReplyToUrl
				 */
				getInReplyToUrl : function() {
					return this.getAsString("inReplyToUrl");
				},
				/**
				 * 
				 * @param inReplytoId
				 * @param inReplyToUrl
				 * @param inReplyToActivity
				 */
				setInReplyTo : function(inReplyToId) {
					var id = "urn:lsid:ibm.com:oa:" + inReplyToId;
					var inReplyTo = {
						"inReplyToId" : id,
						"inReplyToUrl" : this.service.endpoint.baseUrl + stringUtil.replace(consts.ActivityNodeUrl, {
							"activityNodeId" : inReplyToId
						})
					};
					return this.setAsObject(inReplyTo);
				},

				/**
				 * getAssignedToUserId
				 * @returns {String} getAssignedToUserId
				 */
				getAssignedToUserId : function() {
					return this.getAsString("assignedToUserId");
				},

				/**
				 * getAssignedToName
				 * @returns {String} getAssignedToName
				 */
				getAssignedToName : function() {
					return this.getAsString("assignedToName");
				},

				/**
				 * getAssignedToEmail
				 * @returns {String} getAssignedToEmail
				 */
				getAssignedToEmail : function() {
					return this.getAsString("assignedToEmail");
				},

				/**
				 * 
				 * @param assignedToUserId
				 * @param assignedToName
				 * @param assignedToEmail
				 * @returns
				 */
				setAssignedTo : function(assignedToUserId, assignedToName, assignedToEmail) {
					var assignedTo = {
						"assignedToUserId" : assignedToUserId,
						"assignedToName" : assignedToName,
						"assignedToEmail" : assignedToEmail
					};
					return this.setAsObject(assignedTo);
				},

				/**
				 * 
				 * @returns {Array} textFields
				 */
				getTextFields : function() {
					var nodes = this.getAsNodesArray("textFields");
					var textFields = [];
					for ( var counter in nodes) {
						var node = nodes[counter];
						var textField = new TextField();
						for ( var i = 0; i < node.attributes.length; i++) {
							var attr = node.attributes[i];
							var attrName = attr.name;
							var attrValue = attr.value;
							textField[attrName] = attrValue;
						}
						var dataHandler = new XmlDataHandler({
							service : this.service,
							data : node,
							namespaces : consts.Namespaces,
							xpath : consts.TextFieldXPath
						});
						textField.summary = dataHandler.getAsString("summary");
						textFields.push(textField);
						this.addTextField(textField);
					}
					return textFields;
				},

				/**
				 * 
				 * @param {TextField} textField
				 * @returns
				 */
				addTextField : function(textField) {
					if (this._fields["fields"]) {
						this._fields["fields"].push(textField);
						return this;
					} else {
						return this.setAsArray("fields", [ textField ]);
					}
				},

				/**
				 * 
				 * @returns {Array} dateFields
				 */
				getDateFields : function() {
					var nodes = this.getAsNodesArray("dateFields");
					var dateFields = [];
					for ( var counter in nodes) {
						var node = nodes[counter];
						var dateField = new DateField;
						for ( var i = 0; i < node.attributes.length; i++) {
							var attr = node.attributes[i];
							var attrName = attr.name;
							var attrValue = attr.value;
							dateField[attrName] = attrValue;
						}
						dateField["date"] = new Date(stringUtil.trim(node.textContent));
						dateFields.push(dateField);
						this.addDateField(dateField);
					}
					return dateFields;
				},

				addDateField : function(dateField) {
					if (this._fields["fields"]) {
						this._fields["fields"].push(dateField);
						return this;
					} else {
						return this.setAsArray("fields", [ dateField ]);
					}
				},

				/**
				 * 
				 * @returns {Array} linkFields
				 */
				getLinkFields : function() {
					var nodes = this.getAsNodesArray("linkFields");
					var linkFields = [];
					for ( var counter in nodes) {
						var node = nodes[counter];
						var linkField = new LinkField;
						for ( var i = 0; i < node.attributes.length; i++) {
							var attr = node.attributes[i];
							var attrName = attr.name;
							var attrValue = attr.value;
							linkField[attrName] = attrValue;
						}
						var dataHandler = new XmlDataHandler({
							service : this.service,
							data : node,
							namespaces : consts.Namespaces,
							xpath : consts.LinkFieldXPath
						});
						linkField.url = dataHandler.getAsString("url");
						linkField.title = dataHandler.getAsString("title");
						linkFields.push(linkField);
						this.addLinkField(linkField);
					}
					return linkFields;
				},

				addLinkField : function(linkField) {
					if (this._fields["fields"]) {
						this._fields["fields"].push(linkField);
						return this;
					} else {
						return this.setAsArray("fields", [ linkField ]);
					}
				},

				/**
				 * 
				 * @returns {Array} personFields
				 */
				getPersonFields : function() {
					var nodes = this.getAsNodesArray("personFields");
					var personFields = [];
					for ( var counter in nodes) {
						var node = nodes[counter];
						var personField = new PersonField;
						for ( var i = 0; i < node.attributes.length; i++) {
							var attr = node.attributes[i];
							var attrName = attr.name;
							var attrValue = attr.value;
							personField[attrName] = attrValue;
						}
						var dataHandler = new XmlDataHandler({
							service : this.service,
							data : node,
							namespaces : consts.Namespaces,
							xpath : consts.PersonFieldXPath
						});
						personField.personName = dataHandler.getAsString("name");
						personField.userId = dataHandler.getAsString("userId");
						personField.email = dataHandler.getAsString("email");
						personFields.push(personField);
						this.addPersonField(personField);
					}
					return personFields;
				},

				addPersonField : function(personField) {
					if (this._fields["fields"]) {
						this._fields["fields"].push(personField);
						return this;
					} else {
						return this.setAsArray("fields", [ personField ]);
					}
				},

				/**
				 * 
				 * @returns {Array} fileFields
				 */
				getFileFields : function() {
					var nodes = this.getAsNodesArray("fileFields");
					var fileFields = [];
					for ( var counter in nodes) {
						var node = nodes[counter];
						var fileField = new FileField;
						for ( var i = 0; i < node.attributes.length; i++) {
							var attr = node.attributes[i];
							var attrName = attr.name;
							var attrValue = attr.value;
							fileField[attrName] = attrValue;
						}
						var dataHandler = new XmlDataHandler({
							service : this.service,
							data : node,
							namespaces : consts.Namespaces,
							xpath : consts.FileFieldXPath
						});
						fileField.url = dataHandler.getAsString("url");
						fileField.size = dataHandler.getAsString("size");
						fileField.type = dataHandler.getAsString("type");
						fileField.length = dataHandler.getAsString("length");
						fileFields.push(fileField);
					}
					return fileFields;
				},

				/**
				 * 
				 */
				getFields : function() {
					var fields = [];
					var textFields = this.getTextFields();
					var personFields = this.getPersonFields();
					var linkFields = this.getLinkFields();
					var fileFields = this.getFileFields();
					var dateFields = this.getDateFields();
					for ( var counter in textFields) {
						var field = textFields[counter];
						feilds.push(field);
					}
					for ( var counter in personFields) {
						var field = personFields[counter];
						feilds.push(field);
					}
					for ( var counter in linkFields) {
						var field = linkFields[counter];
						feilds.push(field);
					}
					for ( var counter in fileFields) {
						var field = fileFields[counter];
						feilds.push(field);
					}
					for ( var counter in dateFields) {
						var field = dateFields[counter];
						feilds.push(field);
					}
					return fields;
				},

				setFields : function(fields) {
					this._fields["fields"] = fields;
				},

				copyFields : function(activityNode) {
					Activity.prototype.copyFields.call(this, activityNode);
					this.setActivityId(this._fields.activityId || activityNode.getActivityId());
					if (!this._fields.assignedToUserId) {
						this.setAssignedTo(activityNode.getAssignedToUserId(), activityNode.getAssignedToName(), activityNode.getAssignedToEmail());
					}
					if (!this._fields.inReplyToId) {
						this.setInReplyTo(activityNode.getInReplyToId());
					}
					this.setFields(this._fields.fields || activityNode.getFields());
					this.setPosition(activityNode.getPosition());
				}

			});

			/**
			 * Member class represents an entry for a members feed returned by the Connections REST API.
			 * 
			 * @class Member
			 * @namespace sbt.connections
			 */
			var Member = declare(BaseEntity, {
				/**
				 * Return the value of id from result ATOM entry document.
				 * 
				 * @method getId
				 * @return {String} ID of the result
				 */
				getMemberId : function() {
					var id = this.id || this._fields.id || this.getAsString("uid");
					if (id) {
						var index = id.indexOf("&memberid=");
						if (index != -1) {
							var len = "&memberid=".length;
							id = id.substring(index + len);
						}
					}
					return id;
				},

				getName : function() {
					return this.getAsString("name");
				},

				setName : function(name) {
					return this.setAsString("name", name);
				},

				getEmail : function() {
					return this.getAsString("email");
				},

				setEmail : function(email) {
					return this.setAsString("email", email);
				},

				getUserId : function() {
					return this.getAsString("userId");
				},

				setUserId : function(userId) {
					return this.setAsString("userId", userId);
				},

				getRole : function() {
					return this.getAsString("role");
				},

				setRole : function(role) {
					return this.setAsString("role", role);
				},

				getUserState : function() {
					return this.getAsString("userState");
				},

				getTitle : function() {
					return this.getAsString("title");
				},

				getUpdated : function() {
					return this.getAsDate("updated");
				},

				getSummary : function() {
					return this.getAsString("summary");
				},

				getEditUrl : function() {
					this.getAsString("editUrl");
				},

				getCategory : function() {
					this.getAsString("category");
				},

				setCategory : function(category) {
					this.setAsString("category", category);
				},

				getPermissions : function() {
					var permissions = this.getAsString("permissions");
					if (permissions) {
						return permissions.split(", ");
					}
					return permissions;
				}

			});

			/*
			 * Callbacks used when reading a feed that contains Tag entries.
			 */
			var TagFeedCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						service : service,
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.TagXPath
					});
				},
				createEntity : function(service, data, response) {
					var entryHandler = new XmlDataHandler({
						service : service,
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.TagXPath
					});
					return new Tag({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/*
			 * Callbacks used when reading a feed that contains activities entries.
			 */
			var MemberFeedCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						service : service,
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.ActivitiesFeedXPath
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
							xpath : consts.MemberXPath
						});
						entry = feedHandler.data;
					}
					var entryHandler = new XmlDataHandler({
						data : entry,
						namespaces : consts.Namespaces,
						xpath : consts.MemberXPath
					});
					return new Member({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/*
			 * Callbacks used when reading a feed that contains activities entries.
			 */
			var ActivityFeedCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						service : service,
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.ActivitiesFeedXPath
					});
				},
				createEntity : function(service, data, response) {
					var entryHandler = new XmlDataHandler({
						service : service,
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.ActivityNodeXPath
					});
					return new Activity({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/*
			 * Callbacks used when reading a feed that contains activities nodes and activity entries.
			 */
			var ActivityNodeFeedCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						service : service,
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.ActivitiesFeedXPath
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
							xpath : consts.ActivityNodeXPath
						});
						entry = feedHandler.data;
					}
					var entryHandler = new XmlDataHandler({
						data : entry,
						namespaces : consts.Namespaces,
						xpath : consts.ActivityNodeXPath
					});
					return new ActivityNode({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/**
			 * ActivitiesService class.
			 * 
			 * @class ActivitiesService
			 * @namespace sbt.connections
			 */
			var ActivityService = declare(BaseService, {

				contextRootMap : {
					activities : "activities"
				},

				/**
				 * Constructor for ActivitiesService
				 * 
				 * @constructor
				 * @param args
				 */
				constructor : function(args) {
					if (!this.endpoint) {
						this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
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
				 * Get a list of all active activities that match a specific criteria.
				 * 
				 * @method getMyActivities
				 * @param requestArgs
				 * @returns {Array} Activity array
				 */
				getMyActivities : function(requestArgs) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs || {}
					};

					return this.getEntities(consts.AtomActivitiesMy, options, ActivityFeedCallbacks);
				},

				/**
				 * Search for content in all of the activities, both completed and active, that matches a specific criteria.
				 * 
				 * @method getAllActivities
				 * @param requestArgs
				 * @returns {Array} Activity array
				 */
				getAllActivities : function(requestArgs) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs || {}
					};

					return this.getEntities(consts.AtomActivitiesEverything, options, ActivityFeedCallbacks);
				},

				/**
				 * Search for a set of completed activities that match a specific criteria.
				 * 
				 * @method getCompletedActivities
				 * @param requestArgs
				 * @returns {Array} Activity array
				 */
				getCompletedActivities : function(requestArgs) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs || {}
					};

					return this.getEntities(consts.AtomActivitiesCompleted, options, ActivityFeedCallbacks);
				},

				/**
				 * Get an activity node.
				 * 
				 * @method getActivityNode
				 * @param {String} activityNodeId the ID of Activity Node
				 * @returns {Object} ActivityNode
				 */
				getActivityNode : function(activityNodeId) {
					var promise = this.validateField("activityNodeId", activityNodeId);															
					if(promise){
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : activityNodeId
					};
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs
					};
					return this.getEntity(consts.AtomActivityNode, options, activityNodeId, ActivityNodeFeedCallbacks);
				},

				/**
				 * Get an activity .
				 * 
				 * @method getActivity
				 * @param {String} activityId the ID of Activity
				 * @returns {Object} Activity
				 */
				getActivity : function(activityId) {
					var promise = this.validateField("activityId", activityId);															
					if(promise){
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : activityId
					};
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs
					};
					return this.getEntity(consts.AtomActivityNode, options, activityId, ActivityFeedCallbacks);
				},

				/**
				 * @mehtod createActivityNode
				 * @param activityId
				 * @param activityNodeOrJson
				 * @returns {Object} ActivityNode
				 */
				createActivityNode : function(activityId, activityNodeOrJson) {
					var promise = this.validateField("activityId", activityId);										
					if (promise) {
						promise = this.validateField("activityNodeOrJson", activityNodeOrJson);			
					}
					if(promise){
						return promise;
					}
					var activityNode = this.newActivityNode(activityNodeOrJson);
					activityNode.setActivityId(activityId);
					var payload = this._constructPayloadActivityNode(activityNode._fields);
					var requestArgs = {
						"activityUuid" : activityId
					};

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						query : requestArgs,
						data : payload
					};

					return this.updateEntity(consts.AtomCreateActivityNode, options, ActivityNodeFeedCallbacks);
				},

				/**
				 * @method createActivity
				 * @param activityOrJson
				 * @returns {Object} Activity
				 */
				createActivity : function(activityOrJson) {
					var promise = this.validateField("activityOrJson", activityOrJson);										
					if (promise) {
						return promise;
					}
					var activity = this.newActivity(activityOrJson);
					activity.setEntryType(consts.ActivityNodeTypes.Activity);
					var payload = this._constructPayloadActivityNode(activity._fields);

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						data : payload
					};

					return this.updateEntity(consts.AtomActivitiesMy, options, ActivityFeedCallbacks);
				},

				/**
				 * @method updateActivityNode
				 * @param activityNodeId
				 * @param activityNodeOrJson
				 * @returns {Object} activityNode
				 */
				updateActivityNode : function(activityNodeId, activityNodeOrJson) {
					var promise = this.validateField("activityNodeId", activityNodeId);					
					if (promise) {
						promise = this.validateField("activityNodeOrJson", activityNodeOrJson);		
					}
					if (promise) {
						return promise;
					}
					
					var newActivityNode = this.newActivityNode(activityNodeOrJson);
					var promise = new Promise();
					var _this = this;
					this.getActivityNode(activityNodeId).then(function(originalActivityNode) {
						return originalActivityNode;
					}, function(error) {
						promise.rejected(error);
					}).then(function(originalActivityNode) {
						newActivityNode.copyFields(originalActivityNode);
						var payload = _this._constructPayloadActivityNode(newActivityNode._fields);
						var requestArgs = {
							"activityNodeUuid" : activityNodeId
						};
						var options = {
							method : "PUT",
							headers : consts.AtomXmlHeaders,
							query : requestArgs,
							data : payload
						};

						_this.updateEntity(consts.AtomActivityNode, options, ActivityNodeFeedCallbacks).then(function(node) {
							promise.fulfilled(node);
						}, function(error) {
							promise.rejected(error);
						});

					}, function(error) {
						promise.rejected(error);
					});
					return promise;
				},
				/**
				 * @method changeEntryType
				 * @param activityNodeId
				 * @param newType
				 * @param activityNodeOrJson
				 * @returns {Object} ActivityNode
				 */
				changeEntryType : function(activityNodeId, newType, activityNodeOrJson) {
					var promise = this.validateField("activityNodeId", activityNodeId);					
					if (promise) {
						promise = this.validateField("newType", newType);		
					}
					if (promise) {
						promise = this.validateField("activityNodeOrJson", activityNodeOrJson);		
					}
					if(promise) {
						return promise;
					}
					var activityNode = null;
					if (activityNodeOrJson) {
						activityNode = this.newActivityNode(activityNodeOrJson);
					} else {
						activityNode = this.newActivityNode(activityNodeId);
					}
					activityNode.setEntryType(newType);
					return this.updateActivityNode(activityNodeId, activityNode);
				},

				/**
				 * @method moveEntryToSection
				 * @param activityNodeId				
				 * @param sectionId
				 * @param {String} [newTitle]
				 * @returns {Object} ActivityNode
				 */
				moveEntryToSection : function(activityNodeId, sectionId, newTitle) {
					var promise = this.validateField("activityNodeId", activityNodeId);					
					if (promise) {
						promise = this.validateField("sectionId", sectionId);		
					}
					if (promise) {
						return promise;
					}
					var newActivityNode = this.newActivityNode(activityNodeId);
					newActivityNode.setInReplyTo(sectionId);
					if (newTitle) {
						newActivityNode.setTitle(newTitle);
					}
					return this.updateActivityNode(activityNodeId, newActivityNode);
				},

				/**
				 * @method deleteActivityNode
				 * @param activityNodeId
				 */
				deleteActivityNode : function(activityNodeId) {
					var promise = this.validateField("activityNodeId", activityNodeId);					
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : activityNodeId
					};
					var options = {
						method : "DELETE",
						headers : consts.AtomXmlHeaders,
						query : requestArgs
					};

					return this.deleteEntity(consts.AtomActivityNode, options, activityNodeId);
				},

				/**
				 * @method deleteActivity
				 * @param activityId
				 */
				deleteActivity : function(activityId) {
					var promise = this.validateField("activityId", activityId);					
					if (promise) {
						return promise;
					}
					return this.deleteActivityNode(activityId);
				},

				/**
				 * @method restoreActivity
				 * @param activityId
				 */
				restoreActivity : function(activityId) {
					var promise = this.validateField("activityId", activityId);					
					if (promise) {
						return promise;
					}
					var _this = this;
					var promise = new Promise();
					this.getActivityNodeFromTrash(activityId).then(function(deleted) {
						return deleted;
					}, function(error) {
						promise.rejected(error);
					}).then(function(deleted) {
						if (deleted.isDeleted() == false) {
							promise.rejected("Activity is not in Trash");
						} else {
							var restored = _this.newActivity(activityId);
							restored.copyFields(deleted);

							var requestArgs = {
								"activityNodeUuid" : activityId
							};
							var options = {
								method : "PUT",
								headers : consts.AtomXmlHeaders,
								query : requestArgs,
								data : _this._constructPayloadActivityNode(restored._fields)
							};
							var callbacks = {
								createEntity : function(service, data, response) {
									return response;
								}
							};
							_this.updateEntity(consts.AtomActivityNodeTrash, options, callbacks).then(function(response) {
								promise.fulfilled(response);
							}, function(error) {
								promise.rejected(error);
							});
						}
					});
					return promise;
				},

				/**
				 * @method restoreActivityNode
				 * @param activityNodeId
				 */
				restoreActivityNode : function(activityNodeId) {
					var promise = this.validateField("activityNodeId", activityNodeId);					
					if (promise) {
						return promise;
					}
					var _this = this;
					var promise = new Promise();
					this.getActivityNodeFromTrash(activityNodeId).then(function(deletedNode) {
						return deletedNode;
					}, function(error) {
						promise.rejected(error);
					}).then(function(deletedNode) {
						if (deletedNode.isDeleted() == false) {
							promise.rejected("Activity Node is not in Trash");
						} else {
							var restoredNode = _this.newActivityNode(activityNodeId);
							restoredNode.copyFields(deletedNode);

							var requestArgs = {
								"activityNodeUuid" : activityNodeId
							};
							var options = {
								method : "PUT",
								headers : consts.AtomXmlHeaders,
								query : requestArgs,
								data : _this._constructPayloadActivityNode(restoredNode._fields)
							};
							var callbacks = {
								createEntity : function(service, data, response) {
									return response;
								}
							};
							_this.updateEntity(consts.AtomActivityNodeTrash, options, callbacks).then(function(response) {
								promise.fulfilled(response);
							}, function(error) {
								promise.rejected(error);
							});
						}
					});
					return promise;
				},

				/**
				 * @method getActivityNodeFromTrash
				 * @param activityNodeId
				 * @returns {Object} ActivityNode
				 */
				getActivityNodeFromTrash : function(activityNodeId) {
					var promise = this.validateField("activityNodeId", activityNodeId);					
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : activityNodeId
					};
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs
					};
					return this.getEntity(consts.AtomActivityNodeTrash, options, activityNodeId, ActivityNodeFeedCallbacks);
				},

				/**
				 * Returns a ActivityNode instance from ActivityNode or JSON or String. Throws an error if the argument was neither.
				 * @method newActivityNode
				 * @param {Object} activityNodeOrJsonOrString The ActivityNode Object or json String for ActivityNode
				 */
				newActivityNode : function(activityNodeOrJsonOrString) {
					var promise = this.validateField("activityNodeOrJsonOrString", activityNodeOrJsonOrString);					
					if (promise) {
						return promise;
					}
					if (activityNodeOrJsonOrString instanceof ActivityNode || activityNodeOrJsonOrString instanceof Activity) {
						return activityNodeOrJsonOrString;
					} else {
						if (lang.isString(activityNodeOrJsonOrString)) {
							activityNodeOrJsonOrString = {
								id : activityNodeOrJsonOrString
							};
						}
						return new ActivityNode({
							service : this,
							_fields : lang.mixin({}, activityNodeOrJsonOrString)
						});
					}
				},

				/**
				 * @method getActivityNodesInTrash
				 * @param activityId
				 * @param requestArgs
				 * @returns {Array} ActivityNode list
				 */
				getActivityNodesInTrash : function(activityId, requestArgs) {

					var promise = this.validateField("activityId", activityId);					
					if (promise) {
						return promise;
					}
					var args = lang.mixin(requestArgs || {}, {
						"activityUuid" : activityId
					});
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomActivitiesTrash, options, ActivityNodeFeedCallbacks);

				},

				/**
				 * Returns a Activity instance from Activity or JSON or String. Throws an error if the argument was neither.
				 * @method newActivity
				 * @param {Object} activityOrJsonOrString The Activity Object or json String for Activity
				 */
				newActivity : function(activityOrJsonOrString) {
					var promise = this.validateField("activityOrJsonOrString", activityOrJsonOrString);					
					if (promise) {
						return promise;
					}
					if (activityOrJsonOrString instanceof Activity) {
						return activityOrJsonOrString;
					} else {
						if (lang.isString(activityOrJsonOrString)) {
							activityOrJsonOrString = {
								id : activityOrJsonOrString
							};
						}
						return new Activity({
							service : this,
							_fields : lang.mixin({}, activityOrJsonOrString)
						});
					}
				},

				/**
				 * @method getAllToDos
				 * @param requestArgs
				 * @returns {Array} ActivityNode Array
				 */
				getAllToDos : function(requestArgs) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs || {}
					};

					return this.getEntities(consts.AtomActivitiesToDos, options, ActivityFeedCallbacks);
				},

				/**
				 * @method getAllTags
				 * @param requestArgs
				 * @returns {Array}
				 */
				getAllTags : function(requestArgs) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs || {}
					};

					return this.getEntities(consts.AtomActivitiesTags, options, TagFeedCallbacks);
				},

				/**
				 * @method getActivitiesInTrash
				 * @param requestArgs
				 * @returns {Array} activities
				 */
				getActivitiesInTrash : function(requestArgs) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs || {}
					};

					return this.getEntities(consts.AtomActivitiesTrash, options, ActivityFeedCallbacks);

				},

				/**
				 * @method updateActivity
				 * @param activityId
				 * @param activityOrJson
				 * @returns {Object} activity
				 */
				updateActivity : function(activityId, activityOrJson) {
					var promise = this.validateField("activityId", activityId);
					if (promise) {
						romise = this.validateField("activityOrJson", activityOrJson);
					}
					if (promise) {
						return promise;
					}
					return this.updateActivityNode(activityId, activityOrJson);
				},

				/**
				 * @method getActivityTags
				 * @param activityId
				 * @param requestArgs
				 * @returns {Array} tags
				 */
				getActivityTags : function(activityId, requestArgs) {

					var promise = this.validateField("activityId", activityId);
					if (promise) {
						return promise;
					}

					var args = lang.mixin(requestArgs || {}, {
						"activityUuid" : activityId
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomActivitiesTags, options, TagFeedCallbacks);
				},

				/**
				 * @method getActivityNodeTags
				 * @param activityNodeId
				 * @param requestArgs
				 * @returns {Array} tags
				 */
				getActivityNodeTags : function(activityNodeId, requestArgs) {

					var promise = this.validateField("activityNodeId", activityNodeId);
					if (promise) {
						return promise;
					}
					var args = lang.mixin(requestArgs || {}, {
						"activityNodeUuid" : activityNodeId
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomActivitiesTags, options, TagFeedCallbacks);
				},

				/**
				 * @method getMembers
				 * @param activityId
				 * @param requestArgs
				 * @returns {Array} members
				 */
				getMembers : function(activityId, requestArgs) {
					var promise = this.validateField("activityId", activityId);
					if (promise) {
						return promise;
					}
					var args = lang.mixin(requestArgs || {}, {
						"activityUuid" : activityId
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomActivitiesMembers, options, MemberFeedCallbacks);
				},

				/**
				 * @method getMember
				 * @param activityId
				 * @param memberId
				 * @returns {Object} member
				 */
				getMember : function(activityId, memberId) {

					var promise = this.validateField("memberId", memberId);
					if (promise) {
						promise = this.validateField("activityId", activityId);
					}
					if (promise) {
						return promise;
					}
					var options = {
						method : "GET",
						handleAs : "text"
					};

					var url = this.constructUrl(consts.AtomActivitiesMember, null, {
						"activityId" : activityId,
						"memberId" : memberId
					});
					return this.getEntity(url, options, memberId, MemberFeedCallbacks);
				},

				/**
				 * @method addMember
				 * @param activityId
				 * @param memberOrJson
				 */
				addMember : function(activityId, memberOrJson) {
					var promise = this.validateField("memberOrJson", memberOrJson);
					if (promise) {
						promise = this.validateField("activityId", activityId);
					}
					if (promise) {
						return promise;
					}
					var member = this.newMember(memberOrJson);
					if (!member._fields.userId && !member._fields.email) {
						return this.createBadRequestPromise("Member User ID or Email must be provided to add an Activity Member");
					}
					if (!member._fields.role) {
						member.setRole("member");
					}

					var payload = this._constructPayloadMember(member._fields);
					var requestArgs = {
						"activityUuid" : activityId
					};

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						query : requestArgs,
						data : payload
					};
					var callbacks = {
						createEntity : function(service, data, response) {
							return response;
						}
					};

					return this.updateEntity(consts.AtomActivitiesMembers, options, callbacks);

				},

				/**
				 * @method updateMember
				 * @param activityId
				 * @param memberOrJson
				 */
				updateMember : function(activityId, memberOrJson) {
					var promise = this.validateField("memberOrJson", memberOrJson);
					if (promise) {
						promise = this.validateField("activityId", activityId);
					}
					if (promise) {
						return promise;
					}
					var member = this.newMember(memberOrJson);
					var memberId = member.getMemberId();
					if (!memberId || memberId == "") {
						return this.createBadRequestPromise("Member ID must be provided to update Activity Member");

					}
					var id = member._fields.userId || member.getUserId() || member._fields.email || member.getEmail();

					if (!id) {
						return this.createBadRequestPromise("User ID or Email must be provided to update Activity Member");
					}
					if (!member._fields.role) {
						member.setRole(member.getRole());
					}
					if (!member._fields.category) {
						member.setCategory(member.getCategory());
					}

					var payload = this._constructPayloadMember(member._fields);
					var requestArgs = {
						"activityUuid" : activityId,
						"memberid" : memberId
					};

					var options = {
						method : "PUT",
						headers : consts.AtomXmlHeaders,
						query : requestArgs,
						data : payload
					};

					var callbacks = {
						createEntity : function(service, data, response) {
							return response;
						}
					};

					return this.updateEntity(consts.AtomActivitiesMembers, options, callbacks);

				},

				/**
				 * @method deleteMember
				 * @param activityId
				 * @param memberId
				 */
				deleteMember : function(activityId, memberId) {
					var promise = this.validateField("activityId", activityId);
					if (promise) {
						promise = this.validateField("memberId", memberId);
					}
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityUuid" : activityId,
						"memberid" : memberId
					};
					var options = {
						method : "DELETE",
						headers : consts.AtomXmlHeaders,
						query : requestArgs
					};

					return this.deleteEntity(consts.AtomActivitiesMembers, options, memberId);
				},

				/**
				 * Returns a Member instance from Member or JSON or String. Throws an error if the argument was neither.
				 * 
				 * @param {Object} memberOrJsonOrString The Member Object or json String for Member
				 */
				newMember : function(memberOrJsonOrString) {
					var promise = this.validateField("memberOrJsonOrString", memberOrJsonOrString);
					if (promise) {
						return promise;
					}
					if (memberOrJsonOrString instanceof Member) {
						return memberOrJsonOrString;
					} else {
						if (lang.isString(memberOrJsonOrString)) {
							memberOrJsonOrString = {
								id : memberOrJsonOrString
							};
						}
						return new Member({
							service : this,
							_fields : lang.mixin({}, memberOrJsonOrString)
						});
					}
				},

				_constructPayloadMember : function(payloadMap) {
					var _this = this;

					var transformer = function(value, key) {
						var tmpl = null;
						var transformValue = null;
						var trans = function(value, key) {
							return xml.encodeXmlEntry(transformValue);
						};
						if (key == "getEmail" && payloadMap.email) {
							tmpl = EmailTmpl;
							transformValue = payloadMap.email;
						} else if (key == "getUserid" && payloadMap.userId) {
							tmpl = UseridTmpl;
							transformValue = payloadMap.userId;
						} else if (key == "getRole" && payloadMap.role) {
							tmpl = RoleTmpl;
							transformValue = payloadMap.role;
						} else if (key == "getCategory" && payloadMap.category) {
							tmpl = CategoryTmpl;
							transformValue = payloadMap.category;
						}
						if (tmpl) {
							value = stringUtil.transform(tmpl, _this, trans, _this);
						}
						return value;
					};
					return stringUtil.transform(MemberTmpl, this, transformer, this);
				},

				_constructPayloadActivityNode : function(payloadMap) {

					var fieldsXml = "";
					var inReplyToXml = "";
					var positionXml = "";
					var communityXml = "";
					var tagsXml = "";
					var completedXml = "";
					var templateXml = "";
					var dueDateXml = "";
					var iconXml = "";
					var assignedToXml = "";

					var transformer = function(value, key) {
						if (key == "title" && payloadMap.title) {
							value = xml.encodeXmlEntry(payloadMap.title);
						} else if (key == "content" && payloadMap.content) {
							value = xml.encodeXmlEntry(payloadMap.content);
						} else if (key == "entryType" && payloadMap.entryType) {
							value = xml.encodeXmlEntry(payloadMap.entryType);
						} else if (key == "getFields" && fieldsXml != "") {
							value = fieldsXml;
						} else if (key == "getInReplyTo" && inReplyToXml != "") {
							value = inReplyToXml;
						} else if (key == "getPosition" && positionXml != "") {
							value = positionXml;
						} else if (key == "getCommunity" && communityXml != "") {
							value = communityXml;
						} else if (key == "getTags" && tagsXml != "") {
							value = tagsXml;
						} else if (key == "getTemplate" && templateXml != "") {
							value = templateXml;
						} else if (key == "getCompleted" && completedXml != "") {
							value = completedXml;
						} else if (key == "getDueDate" && dueDateXml != "") {
							value = dueDateXml;
						} else if (key == "getIcon" && iconXml != "") {
							value = iconXml;
						} else if (key == "getAssignedTo" && assignedToXml != "") {
							value = assignedToXml;
						}
						return value;
					};

					if (payloadMap.fields) {
						for ( var counter in payloadMap.fields) {
							var field = payloadMap.fields[counter];
							var innerXml = "";
							var trans = function(value, key) {
								if (field[key]) {
									value = xml.encodeXmlEntry(field[key]);
								} else if (innerXml != "") {
									value = innerXml;
								}
								return value;
							};
							var tmpl = TextFieldTmpl;
							if (field.type == "person") {
								tmpl = PersonFieldTmpl;
							} else if (field.type == "link") {
								tmpl = LinkFieldTmpl;
							} else if (field.type == "date") {
								tmpl = DateFieldTmpl;
							}
							innerXml = stringUtil.transform(tmpl, this, trans, this);
							fieldsXml = fieldsXml + stringUtil.transform(FieldTmpl, this, trans, this);
						}
					}

					if (payloadMap.position) {
						var trans = function(value, key) {
							if (key == "position") {
								value = payloadMap.position;
							}
							return value;
						};
						positionXml = stringUtil.transform(PositionTmpl, this, trans, this);
					}

					if (payloadMap.communityLink) {
						var trans = function(value, key) {
							if (key == "communityLink") {
								value = payloadMap.communityLink;
							}
							return value;
						};
						communityXml = stringUtil.transform(CommunityTmpl, this, trans, this);
					}

					if (payloadMap.completed) {
						completedXml = CompletedTmpl;
					}

					if (payloadMap.template) {
						templateXml = TemplateTmpl;
					}

					if (payloadMap.dueDate) {
						var trans = function(value, key) {
							if (key == "dueDate") {
								value = payloadMap.dueDate;
							}
							return value;
						};
						dueDateXml = stringUtil.transform(DueDateTmpl, this, trans, this);
					}

					if (payloadMap.iconUrl) {
						var trans = function(value, key) {
							if (key == "iconUrl") {
								value = payloadMap.iconUrl;
							}
							return value;
						};
						iconXml = stringUtil.transform(IconTmpl, this, trans, this);
					}

					if (payloadMap.tags) {
						for ( var counter in payloadMap.tags) {
							var tag = payloadMap.tags[counter];
							var trans = function(value, key) {
								if (key == "tag") {
									value = tag;
								}
								return value;
							};
							tagsXml = tagsXml + stringUtil.transform(TagTmpl, this, trans, this);
						}
					}

					if (payloadMap.inReplyToId) {
						var trans = function(value, key) {
							if (key == "inReplyToId") {
								value = payloadMap.inReplyToId;
							} else if (key == "inReplyToUrl") {
								value = payloadMap.inReplyToUrl;
							} else if (key == "activityId") {
								value = payloadMap.activityId;
							}
							return value;
						};
						inReplyToXml = stringUtil.transform(InReplytoTmpl, this, trans, this);
					}

					if (payloadMap.assignedToUserId) {
						var trans = function(value, key) {
							if (key == "name") {
								value = payloadMap.assignedToName;
							} else if (key == "userId") {
								value = payloadMap.assignedToUserId;
							} else if (key == "email") {
								value = payloadMap.assignedToEmail;
							}
							return value;
						};
						assignedToxml = stringUtil.transform(AssignedToTmpl, this, trans, this);
					}

					var payload = stringUtil.transform(ActivityNodeTmpl, this, transformer, this);
					return payload;
				}
			});
			return ActivityService;
		});
