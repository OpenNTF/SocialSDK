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
 *The following resources are enabled for these applications:

 *Activities
 *You can follow any public activity. To follow a private activity, you must be a member of the activity.
 *Blogs
 *You can follow any stand-alone blog; you cannot follow community blogs.
 *Communities
 *You do not have to be a member to follow public or moderated communities. You must be a member to follow private communities.
 *Files
 *You can follow any public file or file folders. You can only follow private files and file folders that have been shared with you.
 *Forums
 *You can follow any public forum or forum topic. You can only follow private forums if you are a member or owner of the forum.
 *News repository
 *You can follow any tag.
 *Profiles
 *You can follow any profile.
 *Wikis
 *You can follow any public wiki or wiki page. You must be a reader, editor, or owner of a private wiki before you can follow it or one of its pages.
 * 
 * @module sbt.connections.FollowService
 */
define(
		[ "../declare", "../config", "../lang", "../stringUtil", "../Promise",
				"./FollowConstants", "../base/BaseService",
				"../base/BaseEntity", "../base/XmlDataHandler" ],
		function(declare, config, lang, stringUtil, Promise, consts,
				BaseService, BaseEntity, XmlDataHandler) {
			
			var followTmpl = "<entry><category term=\"${getSource}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/source\"></category><category term=\"${getResourceType}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/resource-type\"></category><category term=\"${getResourceId}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/resource-id\"></category></entry>";

			/**
			 * FollowedResource class represents an entry for a resource followed by logged in user.
			 * 
			 * @class FollowedResource
			 * @namespace sbt.connections
			 */
			var FollowedResource = declare(BaseEntity, {

				/**
				 * Construct a FollowedResource entity.
				 * 
				 * @constructor
				 * @param args
				 */
				constructor : function(args) {
				},

				/**
				 * Return the value of IBM Connections followed resource ID from
				 * resource ATOM entry document.
				 * 
				 * @method getFollowedResourceUuid
				 * @return {String} Resource ID
				 */
				getFollowedResourceUuid : function() {
					var followedResourceUuid = this.getAsString("followedResourceUuid");
					return extractFollowedResourceUuid(followedResourceUuid);
				},

				/**
				 * Sets id of IBM Connections followed resource.
				 * 
				 * @method setFollowedResourceUuid
				 * @param {String} followedResourceUuid Id of the followed resource
				 */
				setFollowedResourceUuid : function(followedResourceUuid) {
					return this.setAsString("followedResourced", followedResourceUuid);
				},

				/**
				 * Return the value of IBM Connections followed resource title.
				 * 
				 * @method getTitle
				 * @return {String} Followed resource title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},

				/**
				 * Sets title of IBM Connections followed resource.
				 * 
				 * @method setTitle
				 * @param {String}
				 *            title Title of the followed resource
				 */
				setTitle : function(title) {
					return this.setAsString("title", title);
				},

				/**
				 * Return the value of IBM Connections followed resource's service name
				 * 
				 * @method getSource
				 * @return {String} Followed resource's service name
				 */
				getSource : function() {
					return this.getAsString("source");
				},

				/**
				 * Sets IBM Connections followed resource's service name.
				 * 
				 * @method setSource
				 * @param {String}
				 *            Source service name of the followed resource
				 */
				setSource : function(source) {
					return this.setAsString("source", source);
				},

				/**
				 * Return the value of IBM Connections followed resource's resourceType
				 * 
				 * @method getResourceType
				 * @return {String} Followed resource's resource type
				 */
				getResourceType : function() {
					return this.getAsString("resourceType");
				},

				/**
				 * Sets IBM Connections followed resource's resource type.
				 * 
				 * @method setResourceType
				 * @param {String}
				 *            resourceType of the followed resource
				 */
				setResourceType : function(resourceType) {
					return this.setAsString("resourceType", resourceType);
				},

				/**
				 * Return the value of IBM Connections followed resource's resourceId
				 * 
				 * @method getResourceId
				 * @return {String} Followed resource's resource Id
				 */
				getResourceId : function() {
					return this.getAsString("resourceId");
				},

				/**
				 * Sets IBM Connections followed resource's resourceId.
				 * 
				 * @method setResourceType
				 * @param {String}
				 *            resource id of the followed resource
				 */
				setResourceId : function(resourceId) {
					return this.setAsString("resourceId", resourceId);
				},

				/**
				 * Return the value of IBM Connections followed resource's type
				 * 
				 * @method getType
				 * @return {String} Followed resource's resource type
				 */
				getType : function() {
					return this.getAsString("categoryType");
				},

				/**
				 * Sets IBM Connections followed resource's resource type.
				 * 
				 * @method setCategoryResourceType
				 * @param {String}
				 *            type of the followed resource 
				 */
				setCategoryType : function(categoryType) {
					return this.setAsString("categoryType", categoryType);
				}

			});
			
		    /*
		     * Callbacks used when reading a feed that contains forum recommendation entries.
		     */
		    var ConnectionsFollowFeedCallbacks = {
		        createEntities : function(service,data,response) {
		            return new XmlDataHandler({
		                service : service,
		                data : data,
		                namespaces : consts.Namespaces,
		                xpath : consts.FollowedResourceFeedXPath
		            });
		        },
		        createEntity : function(service,data,response) {
		        	 var entryHandler = new XmlDataHandler({
		                 service :  service,
		                 data : data,
		                 namespaces : consts.Namespaces,
		                 xpath : consts.FollowedResourceXPath
		             });
		             return new FollowedResource({
		                 service : service,
		                 dataHandler : entryHandler
		             });
		        }
		    };
			
			 /*
		     * Callbacks used when reading a feed that contains followed resource entries.
		     */
		    var ConnectionsFollowedResourceFeedCallbacks = {
		        createEntities : function(service,data,response) {
		            return new XmlDataHandler({
		                service :  service,
		                data : data,
		                namespaces : consts.Namespaces,
		                xpath : consts.FollowedResourceFeedXPath
		            });
		        },
		        createEntity : function(service,data,response) {
		            var entryHandler = new XmlDataHandler({
		                service :  service,
		                data : data,
		                namespaces : consts.Namespaces,
		                xpath : consts.FollowedResourceXPath
		            });
		            return new FollowedResource({
		                service : service,
		                dataHandler : entryHandler
		            });
		        }
		    };
		    
		    /*
		     * Method used to extract the community uuid for an id url.
		     */
		    var extractFollowedResourceUuid = function(followedResourceUuid) {
		    	var followedResourceUuidPrefix = "urn:lsid:ibm.com:follow:resource-";
	        	if(followedResourceUuid && followedResourceUuid.indexOf(followedResourceUuidPrefix) != -1){
	        		followedResourceUuid = followedResourceUuid.substring(followedResourceUuidPrefix.length, followedResourceUuid.length);
	        	}
	            return followedResourceUuid;
		    };

			/**
			 * FollowService class.
			 * 
			 * @class FollowService
			 * @namespace sbt.connections
			 */
			var FollowService = declare(
					BaseService,
					{
						contextRootMap : {
							activities : "activities",
							blogs : "blogs",
							communities : "communities",
							files : "files",
							forums : "forums",
							news : "news",
							profiles : "profiles",
							wikis : "wikis"
						},

						/**
						 * Constructor for FollowService
						 * 
						 * @constructor
						 * @param args
						 */
						constructor : function(args) {
							if (!this.endpoint) {
								this.endpoint = config.findEndpoint(this
										.getDefaultEndpointName());
							}
						},

						/**
						 * Return the default endpoint name if client did not
						 * specify one.
						 * 
						 * @returns {String}
						 */
						getDefaultEndpointName : function() {
							return "connections";
						},

						/**
						 * Get the followed resources feed
						 * 
						 * @method getFollowedResources
						 * @param {String} source String specifying the resource. Options are:
						 *
						 *	activities
						 *	blogs
						 *	communities
						 *	files
						 *	forums
						 *	profiles
						 *	wikis
						 *	tags
						 *
						 * @param {String} resourceType String representing the resource type. Options are:
						 * 
						 * If source=activities
						 * 	   activity
						 * 
						 * If source=blogs
						 *     blog
						 *     
						 * If source=communities
						 *     community
						 *     
						 * If source=files
						 *     file
						 *     file_folder
						 *      
						 * 
						 * If source=forums
						 *     forum
						 *     forum_topic
						 *      
						 * 
						 * If source=profiles
						 *     profile
						 *     
						 * If source=wikis
						 *     wiki
						 *     wiki_page
						 *      
						 * 
						 * If source=tags
						 *     tag
						 * 
						 * @param {Object} [args] Object representing various parameters
				         * that can be passed to get a feed of members of a
				         * community. The parameters must be exactly as they are
				         * supported by IBM Connections like ps, sortBy etc.
						 */
						getFollowedResources : function(source, resourceType, args) {
							var requestArgs = lang.mixin({
								source : source,
								type : resourceType
							}, args || {});
							
							var options = {
								method : "GET",
								handleAs : "text",
								query : requestArgs
							};
							
							var url = null;
				            url = this.constructUrl(consts.AtomFollowAPI, null, {
				            	service : this._getServiceName(source)
							});
							return this.getEntities(url, options, this.getFollowedResourceFeedCallbacks());
						},

				        /**
				         * Start Following a resource
				         * 
				         * @method startFollowing
				         * @param {String/Object} followedResource
				         * @param {Object} [args] Object representing various parameters if any
				         */
						startFollowing : function(followedResourceOrJson, args) {
							var followedResource = this._toFollowedResource(followedResourceOrJson);
				            var promise = this._validateFollowedResource(followedResource, false, args);
				            if (promise) {
				                return promise;
				            }
			                
			                var options = {
			                    method : "POST",
			                    headers : consts.AtomXmlHeaders,
			                    data : this._constructFollowResourcePostData(followedResource)
			                };
							
							var url = null;
				            url = this.constructUrl(consts.AtomFollowAPI, null, {
				            	service : this._getServiceName(followedResource.getSource())
							});
			                
			                return this.updateEntity(url, options, this.getFollowFeedCallbacks());
			            },

				        /**
				         * Stop Following a resource
				         * 
				         * @method stopFollowing
				         * @param {String/Object} followedResource
				         * @param {Object} [args] Object representing various parameters if any
				         */
						stopFollowing : function(followedResourceOrJson, args) {
							var followedResource = this._toFollowedResource(followedResourceOrJson);
				            var promise = this._validateFollowedResource(followedResource, false, args);
				            if (promise) {
				                return promise;
				            }

				            var requestArgs = lang.mixin({
								source : followedResource.getSource(),
								type : followedResource.getResourceType(),
								resource : followedResource.getResourceId()
							}, args || {});
				            
				            var options = {
			                    method : "DELETE",
			                    headers : consts.AtomXmlHeaders,
			                    query : requestArgs
			                };
			                var url = null;
			    			url = this.constructUrl(consts.AtomStopFollowAPI, null, {
				            	service : this._getServiceName(followedResource.getSource()),
				            	resourceId : followedResource.getFollowedResourceUuid()
			    			});
			                return this.deleteEntity(url, options, followedResource.getResourceId());
			            },
				        
				        /**
				         * Create a FollowedResource object with the specified data.
				         * 
				         * @method newFollowedResource
				         * @param {Object} args Object containing the fields for the 
				         * new blog 
				         */
				        newFollowedResource : function(args) {
				            return this._toFollowedResource(args);
				        },

						/*
						 * Callbacks used when reading a feed that contains
						 * followed resource entries.
						 */
						getFollowedResourceFeedCallbacks : function() {
							return ConnectionsFollowedResourceFeedCallbacks;
						},

						/*
						 * Callbacks used when reading a feed that contains
						 * followed resource entries.
						 */
						getFollowFeedCallbacks : function() {
							return ConnectionsFollowFeedCallbacks;
						},

				        /*
				         * Validate a blog, and return a Promise if invalid.
				         */
						_validateFollowedResource : function(followedResource) {
				            if (!followedResource || !followedResource.getSource()) {
				                return this.createBadRequestPromise("Invalid argument, resource with source must be specified.");
				            }
				            if (!followedResource.getResourceType()) {
				                return this.createBadRequestPromise("Invalid argument, resource with resourseType must be specified.");
				            }
				        },

				        /*
				         * Validate a followedResource UUID, and return a Promise if invalid.
				         */
				        _validateFollowedResourceUuid : function(followedResourceUuid) {
				            if (!followedResourceUuid || followedResourceUuid.length == 0) {
				                return this.createBadRequestPromise("Invalid argument, expected followedResourceUuid.");
				            }
				        },

						/*
						 * Construct a post data for a ollowed resource 
						 */
						
						_constructFollowResourcePostData : function(followedResource) {
							var transformer = function(value, key) {
								if (key == "getSource" && !value) {
									value = followedResource.getSource();
								} else if (key == "getResourceType" && !value) {
									value = followedResource.setCategoryResourceType();
								} else if (key == "getResourceId" && !value) {
									value = followedResource.getResourceId();
								}
								return value;
							};
							var postData = stringUtil.transform(followTmpl, followedResource, transformer, followedResource);
							return stringUtil.trim(postData);
						},
						/*
				         * Return a Followed Resource instance from FollowedResource or JSON or String. Throws
				         * an error if the argument was neither.
				         */
				        _toFollowedResource : function(followedResourceOrJsonOrString) {
				            if (followedResourceOrJsonOrString instanceof FollowedResource) {
				                return followedResourceOrJsonOrString;
				            } else {
				                if (lang.isString(followedResourceOrJsonOrString)) {
				                	followedResourceOrJsonOrString = {
			                			followedResourceUuid : followedResourceOrJsonOrString
				                    };
				                }
				                return new FollowedResource({
				                    service : this,
				                    _fields : lang.mixin({}, followedResourceOrJsonOrString)
				                });
				            }
				        },
						/*
				         * Return a Followed Resource instance from FollowedResource or JSON or String. Throws
				         * an error if the argument was neither.
				         */
				        _getServiceName : function(source) {
				        	var contextRoot = this.contextRootMap;
				        	for (var key in contextRoot) {
				        		if(source == key){
				        			return contextRoot[key];
				        		}
							}
							return "";
				        }
					});
			return FollowService;
		});