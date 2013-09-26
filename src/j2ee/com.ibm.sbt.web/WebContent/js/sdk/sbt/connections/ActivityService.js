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
			var PositionTmpl = "<snx:position>${position}</position>";
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

			var Field = declare(null, {
				name : null,
				fid : null,
				position : null,
				type : null,

				constructor : function(args) {
				},

				getName : function() {
					return this.name;
				},

				setName : function(newName) {
					this.name = newName;
				},

				getFid : function() {
					return this.fid;
				},

				getPosition : function() {
					return this.position;
				},

				getType : function() {
					return this.type;
				}
			});

			var TextField = declare(Field, {
				summary : null,
				getSummary : function() {
					return this.summary;
				},
				setSummary : function(newSumamry) {
					this.summary = newSummary;
				}

			});

			var DateField = declare(Field, {
				date : null,
				getDate : function() {
					return this.date;
				},
				setDate : function(newDate) {
					this.date = newDate;
				}

			});

			var LinkField = declare(Field, {
				url : null,
				title : null,
				getUrl : function() {
					return this.url;
				},
				setUrl : function(newUrl) {
					this.url = newUrl;
				},
				getTitle : function() {
					return this.title;
				},
				setTitle : function(title) {
					this.title = title;
				}

			});

			var FileField = declare(Field, {
				url : null,
				type : null,
				size : null,
				length : null,
				getUrl : function() {
					return this.url;
				},
				setUrl : function(newUrl) {
					this.url = newUrl;
				},
				getType : function() {
					return this.type;
				},
				setType : function(newType) {
					this.type = newType;
				},
				getSize : function() {
					return this.size;
				},
				setSize : function(newSize) {
					this.size = newSize;
				},
				getLength : function() {
					return this.length;
				},
				setLength : function(newLength) {
					this.length = newLength;
				}

			});

			var PersonField = declare(Field, {
				personName : null,
				userId : null,
				email : null,

				getPersonName : function() {
					return this.personName;
				},
				setPersonName : function(newName) {
					this.personName = newName;
				},
				getUserId : function() {
					return this.userId;
				},
				setUserId : function(newUserId) {
					this.userId = newUserId;
				},
				getEmail : function() {
					return this.email;
				},
				setEmail : function(newEmail) {
					this.email = newEmail;
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
				getActivityId : function() {
					return this.id || this._fields.id || this.getAsString("uid");
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
				 * @method getTYpe
				 * @returns {String} type
				 */
				getType : function() {
					return this.getAsString("type");
				},

				/**
				 * 
				 * @param type
				 * @returns
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
				 * 
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
				 * Returns Activity Node Position
				 * 
				 * @method getPosition
				 * @returns {String} position
				 */
				getPosition : function() {
					return this.getAsString("position");
				},

				/**
				 * setPosition
				 * @param position
				 * @returns
				 */
				setPosition : function(position) {
					return this.setAsString("position", position);
				},

				/**
				 * 
				 * @returns {String} completed
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
				 * @returns
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
				 * 
				 * @param content
				 * @returns
				 */
				setContent : function(content) {
					return this.setAsString("content", content);
				},				

				/**
				 * 
				 * @returns {String} communityId
				 */
				getCommunityId : function() {
					return this.getAsString("communityId");
				},

				/**
				 * 
				 * @param communityId
				 */
				setCommunityId : function(communityId) {
					return this.setAsString("communityId", communityId);
				},
				/**
				 * 
				 * @returns {String} communityLink
				 */
				getCommunityLink : function() {
					return this.getAsString("communityLink");
				},

				/**
				 * 
				 * @param communityLink
				 */
				setCommunityLink : function(communityLink) {
					return this.setAsString("communityLink", communityLink);
				},
				
				copyFrom : function(activityNode) {					
					this.setCommunityId(activityNode.getCommunityId());
					this.setCommunityLink(activityNode.getCommunityLink());
					this.setCompleted(activityNode.getCompleted());
					this.setContent(activityNode.getContent());
					this.setDueDate(activityNode.getDueDate());
					this.setIconUrl(activityNode.getIconUrl());
					this.setPosition(activityNode.getPosition());
					this.setTags(activityNode.getTags());
					this.setTemplate(activityNode.getTemplate());
					this.setTitle(activityNode.getTitle());
					this.setType(activityNode.getType());
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
					return this.id || this._fields.id || this.getAsString("uid");
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
				setInReplyTo : function(inReplyToId) {
					var index = inReplyToId.indexOf("urn:lsid:ibm.com:oa:") + 1;
					var id = inReplyToId.substring(index);
					var inReplyTo = {
						"inReplyToId" : inReplyToId,
						"inReplyToUrl" : this.service.endpoint.baseUrl + stringUtil.replace(consts.ActivityNodeUrl, id)
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
					var fields = getTextFields();
					fields.push(getPersonFields());
					fields.push(getLinkFields());
					fields.push(getFileFields());
					fields.push(getDateFields());
					return fields();
				},
				
				setFields : function(fields) {
					this._fields["fields"] = fields;
				},
				
				copyFrom : function(activityNode) {
					this.setActivityId(activityNode.getActivityId());
					this.setAssignedTo(activityNode.getAssignedToUserId(), activityNode.getAssignedToName(), activityNode.getAssignedToEmail());
					this.setInReplyTo(activityNode.getInReplyToId());
					this.setFields(activityNode.getFields());
					this.setCommunityId(activityNode.getCommunityId());
					this.setCommunityLink(activityNode.getCommunityLink());
					this.setCompleted(activityNode.getCompleted());
					this.setContent(activityNode.getContent());
					this.setDueDate(activityNode.getDueDate());
					this.setIconUrl(activityNode.getIconUrl());
					this.setPosition(activityNode.getPosition());
					this.setTags(activityNode.getTags());
					this.setTemplate(activityNode.getTemplate());
					this.setTitle(activityNode.getTitle());
					this.setType(activityNode.getType());
				}
				
			});
			
			/*
		     * Callbacks used when reading a feed that contains activities entries.
		     */
		    var ActivityFeedCallbacks = {
		        createEntities : function(service,data,response) {
		            return new XmlDataHandler({
		                service : service,
		                data : data,
		                namespaces : consts.Namespaces,
		                xpath : consts.ActivitiesFeedXPath
		            });
		        },
		        createEntity : function(service,data,response) {
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
				 */
				getActivityNode : function(activityNodeId) {
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
				 * @method getMyActivities
				 * @param {String} activityId the ID of Activity 
				 */
				getActivity : function(activityId) {
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
				 * 
				 * @param activityId
				 * @param activityNodeOrJson
				 * @returns
				 */
				createActivityNode : function(activityId, activityNodeOrJson) {
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
				 * 
				 * @param activityOrJson
				 * @returns
				 */
				createActivity : function(activityOrJson) {
					var activity = this.newActivity(activityOrJson);
					activity.setType(consts.ActivityNodeTypes.Activity);					
					var payload = this._constructPayloadActivityNode(activity._fields);
					
					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,						
						data : payload
					};

					return this.updateEntity(consts.AtomActivitiesMy, options, ActivityFeedCallbacks);
				},

				/**
				 * 
				 * @param activityNodeId
				 * @param activityNodeOrJson
				 * @returns
				 */
				updateActivityNode : function(activityNodeId, activityNodeOrJson) {
					var activityNode = this.newActivityNode(activityNodeOrJson);
					var payload = this._constructPayloadActivityNode(activityNode._fields);
					var requestArgs = {
						"activityNodeUuid" : activityNodeId
					};
					var options = {
						method : "PUT",
						headers : consts.AtomXmlHeaders,
						query : requestArgs,
						data : payload
					};

					return this.updateEntity(consts.AtomActivityNode, options, ActivityNodeFeedCallbacks);
				},
				/**
				 * 
				 * @param activityNodeId
				 * @param newType
				 * @param activityNodeOrJson
				 * @returns
				 */
				changeEntryType : function(activityNodeId, newType, activityNodeOrJson) {
					var activityNode = null;
					if (activityNodeOrJson) {
						activityNode = this.newActivityNode(activityNodeOrJson);
					} else {
						activityNode = this.newActivityNode(activityNodeId);
					}
					activityNode.setType(newType);
					return this.updateActivityNode(activityNodeId, activityNode);
				},

				/**
				 * 
				 * @param activityNodeId
				 * @param activityId
				 * @param sectionId
				 * @returns
				 */
				moveEntryToSection : function(activityNodeId, activityId, sectionId, title) {
					var activityNode = this.newActivityNode(activityNodeId);
					activityNode.setActivityId("urn:lsid:ibm.com:oa:" + activityId);
					activityNode.setInReplyTo(sectionId);
					activityNode.setTitle(title);
					return this.updateActivityNode(activityNodeId, activityNode);
				},

				/**
				 * 
				 * @param activityNodeId
				 * @returns
				 */
				deleteActivityNode : function(activityNodeId) {
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
				 * 
				 * @param activityNodeId
				 * @returns
				 */
				restoreActivityNode : function(activityNodeId) {
					
					var deletedNode = this.getActivityNodeFromTrash(activityNodeId);
					if(deletedNode.isDeleted() == false){
						return this.createBadRequestPromise("Activity Node is not in Trash");
					}
					var restoredNode = this.newActivityNode(deletedNode.getAcivityNodeId());
					restoredNode.copyFrom(deltedNode);
					
					var requestArgs = {
						"activityNodeUuid" : activityNodeId
					};
					var options = {
						method : "PUT",
						headers : consts.AtomXmlHeaders,
						query : requestArgs,
						data : this._constructPayloadActivityNode(restoredNode._fields)
					};

					var callbacks = {
						createEntity : function(service, data, response) {
						}
					};

					return this.updateEntity(consts.AtomActivityNodeTrash, options, callbacks);
				},
				
				getActivityNodeFromTrash : function(activityNodeId) {
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
				 * 
				 * @param {Object} activityNodeOrJsonOrString The ActivityNode Object or json String for ActivityNode
				 */
				newActivityNode : function(activityNodeOrJsonOrString) {
					if (activityNodeOrJsonOrString instanceof ActivityNode) {
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
				 * Returns a Activity instance from Activity or JSON or String. Throws an error if the argument was neither.
				 * 
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
						} else if (key == "type" && payloadMap.type) {
							value = xml.encodeXmlEntry(payloadMap.type);
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
							var field = payloadMap.textFields[counter];
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
