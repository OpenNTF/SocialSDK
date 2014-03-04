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
 
dojo.provide("sbt.connections.IdeationBlogService");

/**
 * The Ideation Blog is a new template for each community to generate ideas, gather feedback,
 * and come to consensus on the best ideas. This simplifies sharing of ideas in a community, 
 * voting on them, and moving the best ones forward as projects. The Ideation Blog is one type
 * of Blog, most common Blog APIs apply to Ideation Blog.
 * @module sbt.connections.FollowService
 */
define(
		[ "sbt/declare", "sbt/config", "sbt/lang", "sbt/stringUtil", "sbt/Promise",
				"sbt/connections/BlogConstants", "sbt/connections/ConnectionsService", "sbt/base/XmlDataHandler", "sbt/connections/BlogService", "sbt/connections/BlogPost" ],
		function(declare, config, lang, stringUtil, Promise, consts,
				ConnectionsService, XmlDataHandler, BlogService, BlogPost) {
		    /*
		     * Callbacks used when reading a feed that contains all ideas on which logged in user voted.
		     */
		    var VotedIdeasFeedCallbacks = {
		        createEntities : function(service,data,response) {
		            return new XmlDataHandler({
		                service : service,
		                data : data,
		                namespaces : consts.Namespaces,
		                xpath : consts.BlogFeedXPath
		            });
		        },
		        createEntity : function(service,data,response) {
		             return new BlogPost({
		                 service : service,
		                 data : data
		             });
		        }
		    };

			/**
			 * IdeationBlogService class.
			 * 
			 * @class IdeationBlogService
			 * @namespace sbt.connections
			 */
			var IdeationBlogService = declare(
					ConnectionsService,
					{
						contextRootMap : {
							blogs : "blogs"
						},
						
						serviceName : "blogs",
						
						blogService : null,

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
							this.blogService = new BlogService();
						},

						/**
						 * Get the Ideas logged in user voted for
						 * 
						 * @method getMyVotedIdeas
						 * @param {Object} [args] Addtional request arguments supported by Connections REST API.
						 */
						getMyVotedIdeas : function(args) {
							var requestArgs = lang.mixin({}, args || {});
							
							var options = {
								method : "GET",
								handleAs : "text",
								query : requestArgs
							};
							
							var url = null;
				            url = this.constructUrl(consts.AtomVotedIdeas, null, {
				            	blogHomepageHandle : this.blogService.handle
							});
							return this.getEntities(url, options, this._getVotedIdeasFeedCallbacks());
						},

				        /**
				         * Vote an idea
				         * 
				         * @method vote
				         * @param {String/Object} IdeationBlog object
				         */
				        voteIdea : function(idea) {
				            return this.blogService.recommendPost(idea);
				        },

				        /**
				         * Unvote an idea
				         * 
				         * @method unvote
				         * @param {String/Object} IdeationBlog object
				         * @param {String} blogHandle Id of post
				         */
				        unvoteIdea : function(idea) {
				        	return this.blogService.unrecommendPost(idea);
				        },

				        /**
				         * Contribute an idea
				         * 
				         * @method contributeIdea
				         * @param {String/Object} idea BlogPost object
				         */
				        contributeIdea : function(idea) {
				        	return this.blogService.createPost(idea);
				        },

						/*
						 * Callbacks used when reading a feed that contains
						 * followed resource entries.
						 */
						_getVotedIdeasFeedCallbacks : function() {
							return VotedIdeasFeedCallbacks;
						},
				        
				        _extractBlogHandle : function(source) {
				        	var urlSuffix = "/entry/";
				        	source = source.substring(0,source.indexOf(urlSuffix));
				        	var bloghandle = source.substring(source.lastIndexOf("/")+1,source.length);
				        	return bloghandle;
				        	
				        }
					});
			return IdeationBlogService;
		});