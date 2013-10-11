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
					+ "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"${type}\" label=\"${type}\" /> "
					+ "<content type=\"html\">${content}</content><title type=\"text\">${title}</title>"
					+ "${getPosition}${getCommunity}${getTags}${getCompleted}${getCompleted}${getDueDate}${getInReplyTo}${getAssignedTo}${getIcon}"
					+ "${getFields}</entry>";
			var PositionTmpl = "<snx:position>${position}</snx:position>";
			var CommunityTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"community_activity\" label=\"Community Activity\"/><snx:communityUuid>${coummunityUuid}</communityUuid>"
					+ "<link rel=\"http://www.ibm.com/xmlns/prod/sn/container\" type=\"application/atom+xml\" href=\"${communityUrl}\"/>";
			var TagTmpl = "<category term=\"${tag}\" /> ";
			var CompletedTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"completed\"/>";
			var TemplateTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"template\"/>";
			var DueDateTmpl = "<snx:duedate>${dueDate}</duedate>";
			var InReplytoTmpl = "<thr:in-reply-to ref=\"${inReplyToId}\" type=\"application/atom+xml\" href=\"${inReplyToUrl}\" source=\"${activityUuid}\" />";
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

			var extractId = function(id, token) {
				if (id) {
					var index = id.indexOf(token);
					if (index != -1) {
						var len = token.length;
						id = id.substring(index + len);
					}
				}
				return id;
			};

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
				 * Returns the ID
				 * @method getUuid
				 * @returns {String} Uuid
				 */
				getUuid : function() {
					var _id = this.id || this._fields.id || this.getAsString("uid");
					this.id = _id;
					this._fields.id = _id;
					return _id;
				},

				/**
				 * Return the value of id from result ATOM entry document.
				 * 
				 * @method getActivityUuid
				 * @return {String} ID of the activity
				 */
				getActivityUuid : function() {
					return extractId(this.getUuid(), "urn:lsid:ibm.com:oa:");
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
				 * @method getType
				 * @returns {String} type
				 */
				getType : function() {
					return this.getAsString("type");
				},

				/**
				 * Sets Activity Node Type
				 * @method setType
				 * @param {String} type
				 */
				setType : function(type) {
					return this.setAsString("type", type);
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
				 * @method isCompleted
				 * @returns {Boolean} completed flag
				 */
				isCompleted : function() {
					return this.getAsBoolean("categoryFlagCompleted");
				},

				/**
				 * Set Completed Flag
				 * @param {Boolean} completed
				 * @returns
				 */
				setCompleted : function(completed) {
					return this.setAsBoolean("categoryFlagCompleted", completed);
				},

				/**
				 * Get Delete Flag
				 * 
				 * @returns {Boolean} isDelete
				 */
				isDeleted : function() {
					return this.getAsBoolean("categoryFlagDelete");
				},

				/**
				 * Gets Teplate Flag
				 * 
				 * @returns {Boolean} template
				 */
				isTemplate : function() {
					return this.getAsBoolean("categoryFlagTemplate");
				},

				/**
				 * Sets Template Flag
				 * 
				 * @param {Boolean} templateFlag
				 */
				setTemplate : function(templateFlag) {
					return this.setAsBoolean("categoryFlagTemplate", templateFlag);
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
				 * getCommunityUuid
				 * @method getCommunityUuid
				 * @returns {String} communityUuid
				 */
				getCommunityUuid : function() {
					return this.getAsString("communityUuid");
				},

				/**
				 * setCommunityUuid
				 * @method setCommunityUuid
				 * @param communityUuid
				 */
				setCommunityUuid : function(communityUuid) {
					return this.setAsString("communityUuid", communityUuid);
				},
				/**
				 * getCommunityUrl
				 * @method getCommunityUrl
				 * @returns {String} communityUrl
				 */
				getCommunityUrl : function() {
					return this.getAsString("communityUrl");
				},

				/**
				 * setCommunityUrl
				 * @method setCommunityUrl
				 * @param communityUrl
				 */
				setCommunityUrl : function(communityUrl) {
					return this.setAsString("communityUrl", communityUrl);
				},

				/**
				 * Loads the Activity object with the atom entry associated with the activity. By default, a network call is made to load the atom entry
				 * document in the activity object.
				 * 
				 * @method load
				 */
				load : function() {
					var promise = this.service.validateField("activityUuid", this.getActivityUuid());
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : this.getActivityUuid()
					};
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs
					};
					return this.service.getEntity(consts.AtomActivityNode, options, this.getActivityUuid(), ActivityFeedCallbacks);
				},

				/**
				 * Creates an activity, sends an Atom entry document containing the new activity to the user's My Activities feed.
				 * 
				 * @method create
				 * @returns {Object} Activity
				 */
				create : function() {
					return this.service.createActivity(this);
				},

				/**
				 * updates an activity, send a replacement Atom Entry document containing the modified activity to the existing activity's edit URL
				 * @method update
				 * @returns {Object} activity
				 */
				update : function() {
					return this.service.updateActivity(this);
				},

				/**
				 * Deletes an activity entry, sends an HTTP DELETE method to the edit web address specified for the node.
				 * 
				 * @method deleteActivity
				 */
				deleteActivity : function() {
					return this.service.deleteActivity(this.getActivityUuid());
				},

				/**
				 * Restores a deleted activity, use a HTTP PUT request. This moves the activity from the trash feed to the user's My Activities feed.
				 * 
				 * @method restore
				 */
				restore : function() {
					return this.service.restoreActivity(this.getActivityUuid());
				},

				/**
				 * Adds a member to the access control list of an activity, sends an Atom entry document containing the new member to the access control list
				 * feed. You can only add one member per post.
				 * @method addMember
				 * @param {Object} memberOrJson
				 */
				addMember : function(memberOrJson) {
					return this.service.addMember(this.getActivityUuid(), memberOrJson);
				},

				/**
				 * Removes a member from the acl list for an application, use the HTTP DELETE method.
				 * @method removeMember
				 * @param {String} memberId
				 */
				removeMember : function(memberId) {
					return this.service.deleteMember(this.getActivityUuid(), memberId);
				},

				/**
				 * Updates a member in the access control list for an application, sends a replacement member entry document in Atom format to the existing ACL
				 * node's edit web address.
				 * @method updateMember
				 * @param {Object} memberOrJson
				 */
				updateMember : function(memberOrJson) {
					return this.service.updateMember(this.getActivityUuid(), memberOrJson);
				},

				/**
				 * Retrieves a member from the access control list for a application, use the edit link found in the member entry in the ACL list feed.
				 * @method getMember
				 * @param {String} memberId
				 * @returns {Object} Member
				 */
				getMember : function(memberId) {
					return this.service.getMember(this.getActivityUuid(), memberId);
				},

				/**
				 * Retrieves activity members from the access control list for a application, use the edit link found in the member entry in the ACL list feed.
				 * @method getMembers
				 * @param {Object} [requestArgs] the optional arguments
				 * @returns {Array} members
				 */
				getMembers : function(requestArgs) {
					return this.service.getMembers(this.getActivityUuid(), requestArgs);
				},

				/**
				 * Creats an entry in an activity, such as a to-do item or to add a reply to another entry, send an Atom entry document containing the new
				 * activity node of the appropriate type to the parent activity's node list.
				 * 
				 * @mehtod createActivityNode
				 * @param {Object} activityNodeOrJson
				 * @returns {Object} ActivityNode
				 */
				createActivityNode : function(activityNodeOrJson) {
					return this.service.createActivityNode(this.getActivityUuid(), activityNodeOrJson);
				},

				/**
				 * Returns the tags for given actiivity
				 * @method getActivityTags
				 * @param {Object} [requestArgs] the optional arguments
				 * @returns {Array} tags
				 */
				getActivityTags : function(requestArgs) {
					return this.service.getActivityTags(this.getActivityUuid(), requestArgs);
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
				 * @method getActivityNodeUuid
				 * @return {String} ID of the result
				 */
				getActivityNodeUuid : function() {
					return extractId(this.getUuid(), "urn:lsid:ibm.com:oa:");
				},

				/**
				 * Return the value of activity uuid from result ATOM entry document.
				 * 
				 * @method getActivityUuid
				 * @return {String} cctivityUuid of the result
				 */
				getActivityUuid : function() {
					return this.getAsString("activityUuid");
				},

				/**
				 * 
				 * @param activityUuid
				 * @returns
				 */
				setActivityUuid : function(activityUuid) {
					return this.setAsString("activityUuid", activityUuid);
				},

				/**
				 * getInReplyToId
				 * @returns {String} getInReplyToId
				 */
				getInReplyToId : function() {
					return this.getAsString("inReplyToId");
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
				setInReplyTo : function(inReplyToId, inReplyToUrl) {
					var id = "urn:lsid:ibm.com:oa:" + inReplyToId;
					var inReplyTo = {
						"inReplyToId" : id,
						"inReplyToUrl" : inReplyToUrl
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
				 * Sets Assigned to in fields for creating playload
				 * @method setAssignedTo
				 * @param {String} assignedToUserId
				 * @param {String} assignedToName
				 * @param {String} assignedToEmail
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
				 * returns Text Fields in Activity node feed
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
				 * Adds a test field
				 * @param {Object} textField
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
				 * returns Date Fields in Activity node feed
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

				/**
				 * adds a DateField
				 * @param {Object} DateField
				 * @returns
				 */
				addDateField : function(dateField) {
					if (this._fields["fields"]) {
						this._fields["fields"].push(dateField);
						return this;
					} else {
						return this.setAsArray("fields", [ dateField ]);
					}
				},

				/**
				 * returns Link Fields in Activity node feed
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

				/**
				 * Adds a LinkField
				 * @param {Object} LinkField
				 * @returns
				 */
				addLinkField : function(linkField) {
					if (this._fields["fields"]) {
						this._fields["fields"].push(linkField);
						return this;
					} else {
						return this.setAsArray("fields", [ linkField ]);
					}
				},

				/**
				 * returns Person Fields in Activity node feed
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

				/**
				 * adds a person fields to activity node
				 * @param {Object} PersonField
				 * @returns
				 */
				addPersonField : function(personField) {
					if (this._fields["fields"]) {
						this._fields["fields"].push(personField);
						return this;
					} else {
						return this.setAsArray("fields", [ personField ]);
					}
				},

				/**
				 * returns File Fields in Activity node feed
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
				 * returns all fields in activity nodes feed
				 * @returns {Array} fields
				 */
				getFields : function() {
					if (this._fields.fields && this._fields.fields.length > 0) {
						return this._fields.fields;
					}
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

				/**
				 * Loads the ActivityNode object with the atom entry associated with the activity node. By default, a network call is made to load the atom
				 * entry document in the ActivityNode object.
				 * 
				 * @method load
				 */
				load : function() {
					var promise = this.service.validateField("activityNodeUuid", this.getActivityNodeUuid());
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : this.getActivityNodeUuid()
					};
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs
					};
					return this.service.getEntity(consts.AtomActivityNode, options, this.getActivityNodeUuid(), ActivityNodeFeedCallbacks);
				},
				/**
				 * Creats an entry in an activity, such as a to-do item or to add a reply to another entry, send an Atom entry document containing the new
				 * activity node of the appropriate type to the parent activity's node list.
				 * 
				 * @mehtod create
				 * @param {String} activityUuid
				 * @returns {Object} ActivityNode
				 */
				create : function(activityUuid) {
					return this.service.createActivityNode(activityUuid, this);
				},

				/**
				 * updates an activity node entry, sends a replacement Atom entry document containing the modified activity node to the existing activity's edit
				 * web address.
				 * @method update
				 * @returns {Object} activityNode
				 */
				update : function() {
					return this.service.updateActivityNode(this);
				},

				/**
				 * Deletes an activity node entry, sends an HTTP DELETE method to the edit web address specified for the node.
				 * 
				 * @method deleteActivityNode
				 */
				deleteActivityNode : function() {
					return this.service.deleteActivityNode(this.getActivityNodeUuid());
				},
				/**
				 * Restores a deleted entry to an activity, sends a HTTP PUT request to the edit web address for the node defined in the trash feed. This moves
				 * the entry from the trash feed to the user's activity node list.
				 * 
				 * @method restoreActivityNode
				 */
				restore : function() {
					return this.service.restoreActivityNode(this.getActivityNodeUuid());
				},
				/**
				 * Changes certain activity entries from one type to another.
				 * 
				 * <pre> 
				 * <b>The following types of entries can be changed to other types:</b>
				 * chat
				 * email
				 * entry
				 * reply
				 * todo<
				 * </pre>
				 * 
				 * @method changeType
				 * @param {String} newType
				 * @returns {Object} ActivityNode
				 */
				changeType : function(newType) {
					this.setType(newType);
					return this.service.changeType(this);
				},
				/**
				 * Moves a standard entry or a to-do entry to a section in an activity, send an updated Atom entry document to the parent activity's node list.
				 * 
				 * @method moveToSection
				 * @param {String} sectionId
				 * @param {String} [newTitle]
				 * @returns {Object} ActivityNode
				 */
				moveToSection : function(sectionId, newTitle) {
					return this.service.moveEntryToSection(this, sectionId);
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
				 * Get the value of ID from ATOM entry document
				 * @method getId
				 * @returns {String} ID
				 */
				getId : function() {
					return this.id || this._fields.id || this.getAsString("uid");
				},
				/**
				 * Return the member Id
				 * 
				 * @method getMemberId
				 * @return {String} ID of the result
				 */
				getMemberId : function() {
					return extractId(this.getId(), "&memberid=");
				},

				/**
				 * Returns member name
				 * @method getName
				 * @returns {String} name
				 */
				getName : function() {
					return this.getAsString("name");
				},

				/**
				 * Sets name in fields
				 * @method setName
				 * @param {String} name
				 */
				setName : function(name) {
					return this.setAsString("name", name);
				},

				/**
				 * Get Email
				 * @method getEmail
				 * @returns {String} email
				 */
				getEmail : function() {
					return this.getAsString("email");
				},

				/**
				 * Set Email
				 * @method setEmail
				 * @param {String} email
				 * @returns
				 */
				setEmail : function(email) {
					return this.setAsString("email", email);
				},

				/**
				 * get user ID
				 * @method getUserId
				 * @returns {String} userId
				 */
				getUserId : function() {
					return this.getAsString("userId");
				},

				/**
				 * Set user ID
				 * @method setUserId
				 * @param {String} userId
				 */
				setUserId : function(userId) {
					return this.setAsString("userId", userId);
				},

				/**
				 * Get role
				 * @method getRole
				 * @returns {String} role
				 */
				getRole : function() {
					return this.getAsString("role");
				},

				/**
				 * Set role
				 * @method setRole
				 * @param {String} role
				 * @returns
				 */
				setRole : function(role) {
					return this.setAsString("role", role);
				},

				/**
				 * Get UserState
				 * @method getUserState
				 * @returns {String} userState
				 */
				getUserState : function() {
					return this.getAsString("userState");
				},

				/**
				 * Get title
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},

				/**
				 * Get Updated
				 * @method getUpdated
				 * @returns {String} updated
				 */
				getUpdated : function() {
					return this.getAsDate("updated");
				},

				/**
				 * Get Summary
				 * @method getSummary
				 * @returns {String} summary
				 */
				getSummary : function() {
					return this.getAsString("summary");
				},

				/**
				 * Get EditUrl
				 * @method getEditUrl
				 * @returns {String} editUrl
				 */
				getEditUrl : function() {
					this.getAsString("editUrl");
				},

				/**
				 * getCategory
				 * @method getCategory
				 * @returns {String} category
				 */
				getCategory : function() {
					this.getAsString("category");
				},

				/**
				 * setCategory
				 * @method setCategory
				 * @param {String} category
				 */
				setCategory : function(category) {
					this.setAsString("category", category);
				},

				/**
				 * getPermissions
				 * @method getPermissions
				 * @returns {Array} permissions
				 */
				getPermissions : function() {
					var permissions = this.getAsString("permissions");
					if (permissions) {
						return permissions.split(", ");
					}
					return permissions;
				},

				/**
				 * Loads the Member object with the atom entry part with the activity. By default, a network call is made to load the atom entry document in the
				 * Member object.
				 * 
				 * @method load
				 * @param {Stirng} activityUuid The Activity ID
				 */
				load : function(activityUuid) {

					var promise = this.service.validateField("memberId", this.getMemberId());
					if (!promise) {
						promise = this.service.validateField("activityUuid", activityUuid);
					}
					if (promise) {
						return promise;
					}
					var options = {
						method : "GET",
						handleAs : "text"
					};

					var url = this.service.constructUrl(consts.AtomActivitiesMember, null, {
						"activityUuid" : activityUuid,
						"memberId" : this.getMemberId()
					});
					return this.service.getEntity(url, options, this.getMemberId(), MemberFeedCallbacks);
				},
				/**
				 * Adds a member to the access control list of an activity, sends an Atom entry document containing the new member to the access control list
				 * feed. You can only add one member per post.
				 * @method addToActivity
				 * @param {String} activityUuid
				 */
				addToActivity : function(actvitiyUuid) {
					return this.service.addMember(actvitiyUuid, this);
				},

				/**
				 * Removes a member from the acl list for an application, use the HTTP DELETE method.
				 * @method removeFromActivity
				 * @param {String} activityUuid
				 */
				removeFromActivity : function(activityUuid) {
					return this.service.deleteMember(activityUuid, this.getMemberId());
				},

				/**
				 * Updates a member in the access control list for an application, sends a replacement member entry document in Atom format to the existing ACL
				 * node's edit web address.
				 * @method updateInActivity
				 * @param {String} activityUuid
				 */
				updateInActivity : function(activityUuid) {
					return this.service.updateMember(activityUuid, this);
				},

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
			 * ActivityService class which provides wrapper APIs to the Activities application of IBM® Connections which enables a team to collect, organize,
			 * share, and reuse work related to a project goal. The Activities API allows application programs to create new activities, and to read and modify
			 * existing activities.
			 * 
			 * @class ActivityService
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
				 * @method getDefaultEndpointName
				 * @returns {String}
				 */
				getDefaultEndpointName : function() {
					return "connections";
				},

				/**
				 * Get a list of all active activities that match a specific criteria.
				 * 
				 * @method getMyActivitiesU
				 * @param {Object} [requestArgs] Optional arguments like ps, page, asc etc.
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
				 * Get a list of all active activity nodes that match a specific criteria in an activity.
				 * 
				 * @method getActivityNodes
				 * @param {String} activityUuid The Activity I
				 * @param {Object} [requestArgs] Optional arguments like ps, page, asc etc.
				 * @returns {Array} ActivityNode array
				 */
				getActivityNodes : function(activityUuid, requestArgs) {
					var args = lang.mixin(requestArgs || {}, {
						"nodeUuid" : activityUuid
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args
					};

					return this.getEntities(consts.AtomActivityNodes, options, ActivityNodeFeedCallbacks);
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
				 * @param {Object} [requestArgs] The optional arguments
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
				 * Retrieve an activity node entry, uses the edit link found in the corresponding activity node in the user's My Activities feed.
				 * 
				 * @method getActivityNode
				 * @param {String} activityNodeUuid the ID of Activity Node
				 * @returns {Object} ActivityNode
				 */
				getActivityNode : function(activityNodeUuid) {
					var promise = this.validateField("activityNodeUuid", activityNodeUuid);
					if (promise) {
						return promise;
					}
					var activityNode = this.newActivityNode(activityNodeUuid);
					return activityNode.load();
				},

				/**
				 * Retrieve an activity entry, uses the edit link found in the corresponding activity node in the user's My Activities feed.
				 * 
				 * @method getActivity
				 * @param {String} activityUuid the ID of Activity
				 * @returns {Object} Activity
				 */
				getActivity : function(activityUuid) {
					var promise = this.validateField("activityUuid", activityUuid);
					if (promise) {
						return promise;
					}
					var activity = this.newActivity(activityUuid);
					return activity.load();
				},

				/**
				 * Creats an entry in an activity, such as a to-do item or to add a reply to another entry, send an Atom entry document containing the new
				 * activity node of the appropriate type to the parent activity's node list.
				 * 
				 * @mehtod createActivityNode
				 * @param {String} activityUuid
				 * @param {Object} activityNodeOrJson
				 * @returns {Object} ActivityNode
				 */
				createActivityNode : function(activityUuid, activityNodeOrJson) {
					var promise = this.validateField("activityUuid", activityUuid);
					if (!promise) {
						promise = this.validateField("activityNodeOrJson", activityNodeOrJson);
					}
					if (promise) {
						return promise;
					}
					var activityNode = this.newActivityNode(activityNodeOrJson);
					activityNode.setActivityUuid(activityUuid);
					var payload = this._constructPayloadActivityNode(activityNode);
					var requestArgs = {
						"activityUuid" : activityUuid
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
				 * Creates an activity, sends an Atom entry document containing the new activity to the user's My Activities feed.
				 * 
				 * @method createActivity
				 * @param {Object} activityOrJson
				 * @returns {Object} Activity
				 */
				createActivity : function(activityOrJson) {
					var promise = this.validateField("activityOrJson", activityOrJson);
					if (promise) {
						return promise;
					}
					var activity = this.newActivity(activityOrJson);
					activity.setType(consts.ActivityNodeTypes.Activity);
					var payload = this._constructPayloadActivityNode(activity);

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						data : payload
					};

					return this.updateEntity(consts.AtomActivitiesMy, options, ActivityFeedCallbacks);
				},

				_validateActivityNode : function(activityNode, checkUuid, checkType) {
					if (checkUuid && !activityNode.getActivityNodeUuid()) {
						return this.createBadRequestPromise("Invalid argument, activity node with UUID must be specified.");
					}
					if (checkType && !activityNode.getType()) {
						return this.createBadRequestPromise("Invalid argument, activity node with Type must be specified.");
					}
				},

				_validateActivity : function(activity, checkUuid) {
					if (checkUuid && !activity.getActivityUuid()) {
						return this.createBadRequestPromise("Invalid argument, activity with UUID must be specified.");
					}
				},
				/**
				 * updates an activity node entry, sends a replacement Atom entry document containing the modified activity node to the existing activity's edit
				 * web address.
				 * @method updateActivityNode
				 * @param {Object} activityNodeOrJson ActivityNode or Json Object with Uuid populated
				 * @returns {Object} ActivityNode
				 */
				updateActivityNode : function(activityNodeOrJson) {
					var promise = this.validateField("activityNodeOrJson", activityNodeOrJson);
					if (promise) {
						return promise;
					}
					var newActivityNode = this.newActivityNode(activityNodeOrJson);
					promise = this._validateActivityNode(newActivityNode, true);
					if (promise) {
						return promise;
					}
					return this._update(newActivityNode, ActivityNodeFeedCallbacks);
				},

				/**
				 * Updates an activity, send a replacement Atom Entry document containing the modified activity to the existing activity's edit URL
				 * @method updateActivity
				 * @param {Object} activityOrJson Activity or Json Object
				 * @returns {Object} Activity
				 */
				updateActivity : function(activityOrJson) {
					var promise = this.validateField("activityOrJson", activityOrJson);
					if (promise) {
						return promise;
					}
					var newActivity = this.newActivity(activityOrJson);
					promise = this._validateActivity(newActivity, true);
					if (promise) {
						return promise;
					}
					return this._update(activityOrJson, ActivityFeedCallbacks);
				},

				_update : function(activityOrActivityNode, callbacks) {
					var promise = new Promise();
					var _this = this;
					var uuid = extractId(activityOrActivityNode.getUuid());
					var update = function() {
						var payload = _this._constructPayloadActivityNode(activityOrActivityNode);
						var requestArgs = {
							"activityNodeUuid" : uuid
						};
						var options = {
							method : "PUT",
							headers : consts.AtomXmlHeaders,
							query : requestArgs,
							data : payload
						};
						_this.updateEntity(consts.AtomActivityNode, options, callbacks).then(function(node) {
							promise.fulfilled(node);
						}, function(error) {
							promise.rejected(error);
						});
					};
					if (activityOrActivityNode.isLoaded()) {
						update();
					} else {
						activityOrActivityNode.load().then(function() {
							update();
						}, function(error) {
							promise.rejected(error);
						});
					}
					return promise;
				},
				/**
				 * Changes certain activity entries from one type to another.
				 * 
				 * <pre> 
				 * <b>The following types of entries can be changed to other types:</b>
				 * chat
				 * email
				 * entry
				 * reply
				 * todo<
				 * /pre>
				 * 
				 * @method changeEntryType
				 * @param {Object} activityNodeOrJson ActivityNode or Json object with Uuid and type populated
				 * @returns {Object} ActivityNode
				 */
				changeEntryType : function(activityNodeOrJson) {
					var promise = this.validateField("activityNodeOrJson", activityNodeOrJson);
					if (promise) {
						return promise;
					}
					var activityNode = this.newActivityNode(activityNodeOrJson);
					promise = this._validateActivityNode(activityNode, true, true);

					return this.updateActivityNode(activityNode);
				},

				/**
				 * Moves a standard entry or a to-do entry to a section in an activity, send an updated Atom entry document to the parent activity's node list.
				 * 
				 * @method moveEntryToSection
				 * @param {Object} activityNodeOrJson
				 * @param {Object} sectionNodeOrJsonOrId section activityNode or Json Object or Section ID
				 * @returns {Object} ActivityNode
				 */
				moveEntryToSection : function(activityNodeOrJson, sectionNodeOrJsonOrId) {
					var _this = this;
					var promise = this.validateField("activityNodeOrJson", activityNodeOrJson);
					if (!promise) {
						promise = this.validateField("sectionNodeOrJsonOrId", sectionNodeOrJsonOrId);
					}
					if (promise) {
						return promise;
					}
					var activityNode = this.newActivityNode(activityNodeOrJson);
					var sectionNode = this.newActivityNode(sectionNodeOrJsonOrId);
					promise = new Promise();
					var update = function() {
						activityNode.setInReplyTo(sectionNode.getActivityNodeUuid(), sectionNode.getSelfUrl());
						_this.updateActivityNode(activityNode).then(function(activityNode) {
							promise.fulfilled(activityNode);
						}, function(error) {
							promise.rejected(error);
						});
					};
					if (sectionNode.isLoaded()) {
						update();
					} else {
						sectionNode.load().then(function() {
							update();
						}, function(error) {
							promise.rejected(error);
						});
					}
					return promise;
				},

				/**
				 * Deletes an activity node entry, sends an HTTP DELETE method to the edit web address specified for the node.
				 * 
				 * @method deleteActivityNode
				 * @param {String} activityNodeUuid
				 */
				deleteActivityNode : function(activityNodeUuid) {
					var promise = this.validateField("activityNodeUuid", activityNodeUuid);
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : activityNodeUuid
					};
					var options = {
						method : "DELETE",
						headers : consts.AtomXmlHeaders,
						query : requestArgs
					};

					return this.deleteEntity(consts.AtomActivityNode, options, activityNodeUuid);
				},

				/**
				 * Deletes an activity entry, sends an HTTP DELETE method to the edit web address specified for the node.
				 * 
				 * @method deleteActivity
				 * @param {String} activityUuid
				 */
				deleteActivity : function(activityUuid) {
					var promise = this.validateField("activityUuid", activityUuid);
					if (promise) {
						return promise;
					}
					return this.deleteActivityNode(activityUuid);
				},

				/**
				 * Restores a deleted activity, use a HTTP PUT request. This moves the activity from the trash feed to the user's My Activities feed.
				 * 
				 * @method restoreActivity
				 * @param {String} activityUuid
				 */
				restoreActivity : function(activityUuid) {
					var promise = this.validateField("activityUuid", activityUuid);
					if (promise) {
						return promise;
					}
					var _this = this;
					promise = new Promise();
					this.getActivityNodeFromTrash(activityUuid).then(function(deleted) {
						return deleted;
					}).then(function(activity) {
						if (!activity.isDeleted()) {
							promise.rejected("Activity is not in Trash");
						} else {
							var requestArgs = {
								"activityNodeUuid" : activityUuid
							};
							var options = {
								method : "PUT",
								headers : consts.AtomXmlHeaders,
								query : requestArgs,
								data : _this._constructPayloadActivityNode(activity)
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
				 * Restores a deleted entry to an activity, sends a HTTP PUT request to the edit web address for the node defined in the trash feed. This moves
				 * the entry from the trash feed to the user's activity node list.
				 * 
				 * @method restoreActivityNode
				 * @param {String} activityNodeUuid
				 */
				restoreActivityNode : function(activityNodeUuid) {
					var promise = this.validateField("activityNodeUuid", activityNodeUuid);
					if (promise) {
						return promise;
					}
					var _this = this;
					var promise = new Promise();
					this.getActivityNodeFromTrash(activityNodeUuid).then(function(deletedNode) {
						return deletedNode;
					}).then(function(activityNode) {
						if (!activityNode.isDeleted()) {
							promise.rejected("Activity Node is not in Trash");
						} else {
							var requestArgs = {
								"activityNodeUuid" : activityNodeUuid
							};
							var options = {
								method : "PUT",
								headers : consts.AtomXmlHeaders,
								query : requestArgs,
								data : _this._constructPayloadActivityNode(activityNode)
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
				 * Retrieves and activity node from trash
				 * 
				 * @method getActivityNodeFromTrash
				 * @param {String} activityNodeUuid
				 * @returns {Object} ActivityNode
				 */
				getActivityNodeFromTrash : function(activityNodeUuid) {
					var promise = this.validateField("activityNodeUuid", activityNodeUuid);
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityNodeUuid" : activityNodeUuid
					};
					var options = {
						method : "GET",
						handleAs : "text",
						query : requestArgs
					};
					return this.getEntity(consts.AtomActivityNodeTrash, options, activityNodeUuid, ActivityNodeFeedCallbacks);
				},

				/**
				 * Returns a ActivityNode instance from ActivityNode or JSON or String. Throws an error if the argument was neither.
				 * 
				 * @method newActivityNode
				 * @param {Object} activityNodeOrJsonOrString The ActivityNode Object or json String for ActivityNode
				 */
				newActivityNode : function(activityNodeOrJsonOrString) {
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
				 * Gets All activity nodes in trash which match given criteria
				 * @method getActivityNodesInTrash
				 * @param {String} activityUuid
				 * @param {Object} [requestArgs] optional arguments
				 * @returns {Array} ActivityNode list
				 */
				getActivityNodesInTrash : function(activityUuid, requestArgs) {

					var promise = this.validateField("activityUuid", activityUuid);
					if (promise) {
						return promise;
					}
					var args = lang.mixin(requestArgs || {}, {
						"activityUuid" : activityUuid
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
				 * Search for a set of to-do items that match a specific criteria.
				 * 
				 * @method getAllToDos
				 * @param {Object} [requestArgs] the optional arguments
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
				 * Search for a set of tags that match a specific criteria.
				 * @method getAllTags
				 * @param {Object} [requestArgs] the optional arguments
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
				 * Search for sctivities in trash which math a specif criteria
				 * 
				 * @method getActivitiesInTrash
				 * @param {Object} [requestArgs] the optional arguments
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
				 * Returns the tags for given actiivity
				 * @method getActivityTags
				 * @param {String} activityUuid
				 * @param {Object} [requestArgs] the optional arguments
				 * @returns {Array} tags
				 */
				getActivityTags : function(activityUuid, requestArgs) {

					var promise = this.validateField("activityUuid", activityUuid);
					if (promise) {
						return promise;
					}

					var args = lang.mixin(requestArgs || {}, {
						"activityUuid" : activityUuid
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomActivitiesTags, options, TagFeedCallbacks);
				},

				/**
				 * Returns the tags for given actiivity node.
				 * @method getActivityNodeTags
				 * @param activityNodeUuid
				 * @param {Object} [requestArgs] the optional arguments
				 * @returns {Array} tags
				 */
				getActivityNodeTags : function(activityNodeUuid, requestArgs) {

					var promise = this.validateField("activityNodeUuid", activityNodeUuid);
					if (promise) {
						return promise;
					}
					var args = lang.mixin(requestArgs || {}, {
						"activityNodeUuid" : activityNodeUuid
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomActivitiesTags, options, TagFeedCallbacks);
				},

				/**
				 * Retrieves activity members from the access control list for a application, use the edit link found in the member entry in the ACL list feed.
				 * @method getMembers
				 * @param {String} activityUuid
				 * @param {Object} [requestArgs] the optional arguments
				 * @returns {Array} members
				 */
				getMembers : function(activityUuid, requestArgs) {
					var promise = this.validateField("activityUuid", activityUuid);
					if (promise) {
						return promise;
					}
					var args = lang.mixin(requestArgs || {}, {
						"activityUuid" : activityUuid
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomActivitiesMembers, options, MemberFeedCallbacks);
				},

				_validateMember : function(member, checkId, checkUserIdOrEmail) {
					if (checkId && !member.getMemberId()) {
						return this.createBadRequestPromise("Invalid argument, member with ID must be specified.");
					}
					if (checkUserIdOrEmail) {
						var id = member.getUserId() || member.getEmail();
						if (!id) {
							return this.createBadRequestPromise("Invalid argument, member with User ID or Email must be specified.");
						}
					}
				},

				/**
				 * Retrieves a member from the access control list for a application, use the edit link found in the member entry in the ACL list feed.
				 * @method getMember
				 * @param {String} activityUuid
				 * @param {String} memberId
				 * @returns {Object} Member
				 */
				getMember : function(activityUuid, memberId) {

					var promise = this.validateField("memberId", memberId);
					if (!promise) {
						promise = this.validateField("activityUuid", activityUuid);
					}
					if (promise) {
						return promise;
					}
					var member = this._toMember(memberId);
					return member.load(activityUuid);
				},

				/**
				 * Adds a member to the access control list of an activity, sends an Atom entry document containing the new member to the access control list
				 * feed. You can only add one member per post.
				 * @method addMember
				 * @param {String} activityUuid
				 * @param {Object} memberOrJson
				 */
				addMember : function(activityUuid, memberOrJson) {
					var promise = this.validateField("memberOrJson", memberOrJson);
					if (!promise) {
						promise = this.validateField("activityUuid", activityUuid);
					}
					if (promise) {
						return promise;
					}
					var member = this._toMember(memberOrJson);
					promise = this._validateMember(member, false, true);
					if (promise) {
						return promise;
					}
					if (!member.getRole()) {
						member.setRole("member");
					}
					var payload = this._constructPayloadMember(member);
					var requestArgs = {
						"activityUuid" : activityUuid
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
				 * Updates a member in the access control list for an application, sends a replacement member entry document in Atom format to the existing ACL
				 * node's edit web address.
				 * @method updateMember
				 * @param {String} activityUuid
				 * @param {Object} memberOrJson
				 */
				updateMember : function(activityUuid, memberOrJson) {
					var promise = this.validateField("memberOrJson", memberOrJson);
					if (!promise) {
						promise = this.validateField("activityUuid", activityUuid);
					}
					if (promise) {
						return promise;
					}
					var member = this._toMember(memberOrJson);
					promise = this._validateMember(member, true, true);
					if (promise) {
						return promise;
					}

					var payload = this._constructPayloadMember(member);
					var requestArgs = {
						"activityUuid" : activityUuid,
						"memberid" : member.getMemberId()
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
				 * Removes a member from the acl list for an application, use the HTTP DELETE method.
				 * @method deleteMember
				 * @param {String} activityUuid
				 * @param {String} memberId
				 */
				deleteMember : function(activityUuid, memberId) {
					var promise = this.validateField("activityUuid", activityUuid);
					if (!promise) {
						promise = this.validateField("memberId", memberId);
					}
					if (promise) {
						return promise;
					}
					var requestArgs = {
						"activityUuid" : activityUuid,
						"memberid" : memberId
					};
					var options = {
						method : "DELETE",
						headers : consts.AtomXmlHeaders,
						query : requestArgs
					};

					return this.deleteEntity(consts.AtomActivitiesMembers, options, memberId);
				},

				_toMember : function(memberOrJsonOrString) {
					if (memberOrJsonOrString) {
						if (memberOrJsonOrString instanceof Member) {
							return memberOrJsonOrString;
						}
						var member = new Member({
							service : this
						});
						if (lang.isString(memberOrJsonOrString)) {
							memberOrJsonOrString = {
								id : memberOrJsonOrString
							};
						}
						member._fields = lang.mixin({}, memberOrJsonOrString);
						return member;
					}
				},

				/**
				 * Returns a Member instance from Member or JSON or String. Throws an error if the argument was neither.
				 * @method newMember
				 * @param {Object} memberOrJsonOrString The Member Object or json String for Member
				 */
				newMember : function(memberOrJsonOrString) {
					return this._toMember(memberOrJsonOrString);
				},

				_constructPayloadMember : function(member) {
					var _this = this;

					var transformer = function(value, key) {
						var tmpl = null;
						var transformValue = null;
						var trans = function(value, key) {
							return xml.encodeXmlEntry(transformValue);
						};
						if (key == "getEmail" && member.getEmail()) {
							tmpl = EmailTmpl;
							transformValue = member.getEmail();
						} else if (key == "getUserid" && member.getUserId()) {
							tmpl = UseridTmpl;
							transformValue = member.getUserId();
						} else if (key == "getRole" && member.getRole()) {
							tmpl = RoleTmpl;
							transformValue = member.getRole();
						} else if (key == "getCategory" && member.getCategory()) {
							tmpl = CategoryTmpl;
							transformValue = member.getCategory();
						}
						if (tmpl) {
							value = stringUtil.transform(tmpl, _this, trans, _this);
						}
						return value;
					};
					return stringUtil.transform(MemberTmpl, this, transformer, this);
				},

				_constructPayloadActivityNode : function(activityNode) {

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
						if (key == "title" && activityNode.getTitle()) {
							value = xml.encodeXmlEntry(activityNode.getTitle());
						} else if (key == "content" && activityNode.getContent()) {
							value = xml.encodeXmlEntry(activityNode.getContent());
						} else if (key == "type" && activityNode.getType()) {
							value = xml.encodeXmlEntry(activityNode.getType());
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

					if (activityNode.getFields && activityNode.getFields().length > 0) {
						var fields = activityNode.getFields();
						for ( var counter in fields) {
							var field = fields[counter];
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

					if (activityNode.getPosition()) {
						var trans = function(value, key) {
							if (key == "position") {
								value = activityNode.getPosition();
							}
							return value;
						};
						positionXml = stringUtil.transform(PositionTmpl, this, trans, this);
					}

					if (activityNode.getCommunityUrl()) {
						var trans = function(value, key) {
							if (key == "communityUrl") {
								value = activityNode.getCommunityUrl();
							}
							if (key == "communityUuid") {
								value = activityNode.getCommunityUuid();
							}
							return value;
						};
						communityXml = stringUtil.transform(CommunityTmpl, this, trans, this);
					}

					if (activityNode.isCompleted()) {
						completedXml = CompletedTmpl;
					}

					if (activityNode.isTemplate()) {
						templateXml = TemplateTmpl;
					}

					if (activityNode.getDueDate()) {
						var trans = function(value, key) {
							if (key == "dueDate") {
								value = activityNode.getDueDate();
							}
							return value;
						};
						dueDateXml = stringUtil.transform(DueDateTmpl, this, trans, this);
					}

					if (activityNode.getIconUrl()) {
						var trans = function(value, key) {
							if (key == "iconUrl") {
								value = activityNode.getIconUrl();
							}
							return value;
						};
						iconXml = stringUtil.transform(IconTmpl, this, trans, this);
					}

					if (activityNode.getTags()) {
						var tags = activityNode.getTags();
						for ( var counter in tags) {
							var tag = tags[counter];
							var trans = function(value, key) {
								if (key == "tag") {
									value = tag;
								}
								return value;
							};
							tagsXml = tagsXml + stringUtil.transform(TagTmpl, this, trans, this);
						}
					}

					if (activityNode.getInReplyToId && activityNode.getInReplyToId()) {
						if (activityNode.getInReplyToId().indexOf(activityNode.getActivityUuid()) == -1) {
							var trans = function(value, key) {
								if (key == "inReplyToId") {
									value = activityNode.getInReplyToId();
								} else if (key == "inReplyToUrl") {
									value = activityNode.getInReplyToUrl();
								} else if (key == "activityUuid") {
									value = activityNode.getActivityUuid();
								}
								return value;
							};
							inReplyToXml = stringUtil.transform(InReplytoTmpl, this, trans, this);
						}
					}

					if (activityNode.getAssignedToUserId && activityNode.getAssignedToUserId()) {
						var trans = function(value, key) {
							if (key == "name") {
								value = activityNode.getAssignedToName();
							} else if (key == "userId") {
								value = activityNode.getAssignedToUserId();
							} else if (key == "email") {
								value = activityNode.getAssignedToEmail();
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
