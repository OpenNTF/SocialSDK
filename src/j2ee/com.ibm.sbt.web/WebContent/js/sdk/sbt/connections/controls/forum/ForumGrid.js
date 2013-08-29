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

define(["../../../declare",
        "../../../controls/grid/Grid",
        "../../../store/parameter",
        "./ForumGridRenderer", 
		 "./ForumAction",
		 "./ViewProfileAction",
		 "./BackAction",
        "../../../connections/ForumConstants"], 
    function(declare, Grid, parameter, ForumGridRenderer, ForumAction, ViewProfileAction, BackAction, consts){
	
		/**Values that forums Can be sorted By, NOTE Sotring is not enabled in Connections*/
		var sortVals = {
				created: "created",
	       		modified: "modified" 
		};
		
		/**URL parameters */
		var ParamSchema = {
			pageNumber: parameter.oneBasedInteger("page"),	
			pageSize: parameter.oneBasedInteger("ps"),
			sortBy: parameter.sortField("sortBy",sortVals),
			sortOrder: parameter.sortOrder("sortOrder")						
		};
		
		/**
		 * @class ForumGrid 
		 * @namespace sbt.connections.controls.forum
		 * @module sbt.connections.controls.forum.ForumGrid
		 */
	    var ForumGrid = declare(Grid,{
	    	
	    	/**Hide the table header */
	    	hideHeader: false,
	    	baseProfilesUrl: "/profiles",
	    	
	        options : {
	            "my" : {
	                storeArgs : {
	                    url : consts.AtomForumsMy,
	                    attributes : consts.ForumXPath,
	                    feedXPath : consts.ForumsFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "my"
	                }
	            },
	            "public" : {
	                storeArgs : {
	                    url : consts. AtomForumsPublic,
	                    attributes : consts.ForumXPath,
	                    feedXPath : consts.ForumsFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "public"
	                }
	            },
	            "myTopics" : {
	                storeArgs : {
	                    url : consts. AtomTopicsMy,
	                    attributes : consts.ForumTopicXPath,
	                    feedXPath : consts.ForumsFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "myTopics"
	                }
	            }	            
	        },
		    
	        /**The default Forum Grid that will be created, if another type is not specified */
	        defaultOption: "my",
	        
	        /**forumAction handles onClick and tooltip functions */
	        forumAction : new ForumAction(),
	        viewProfileAction: new ViewProfileAction(),
	        backAction: new BackAction(),
	        
	        /**
	         * The constructor function.
	         * @method constructor
	         * @param args
	         */
	        constructor: function(args){
	        	if(args.hideHeader){
	        		this.hideHeader = args.hideHeader;
	        	}
	        	if(args.baseProfilesUrl){
	        		this.baseProfilesUrl = args.baseProfilesUrl;
	        	}
	        	
	        },
	        
	        /**
	         * Creates a renderer for the grid.The renderer is responsible for 
	         * loading the grid's HTML content.
	         * @method createDefaultRenderer
	         * @param args sets the template the renderer will use, by checking args.type
	         * @returns an instance of a ForumGridRenderer.
	         */
	        createDefaultRenderer : function(args) {
	            return new ForumGridRenderer(args);
	        },
	        
	        /**
	         * In the grid HTML an element can have an event attached 
	         * using dojo-attach-event="onClick: handleClick".
	         * This method is the handler for the onclick event.
	         * @method handleClick
	         * @param el the element that fired the event
	         * @param data all of the items from the current row of the grid. 
	         * @param ev the event 
	         */
	        handleClick: function(el, data, ev) {
	            if (this.forumAction) {
	                this._stopEvent(ev);
	                this.forumAction.execute(data, this , ev);
	            }
	        },
	    	
	        /**
	         * In the grid HTML an element can have an event attached 
	         * using dojo-attach-event="onClick: handleClick".
	         * This method is the handler for the onclick event.
	         * This function is for viewing the profile of the forus author. 
	         * @method handleClick
	         * @param el the element that fired the event
	         * @param data all of the items from the current row of the grid. 
	         * @param ev the event 
	         */
	        viewAuthorProfile: function(el, data, ev){
	        	this._stopEvent(ev);
	        	this.viewProfileAction.openAuthorProfile(data, this.store,this.baseProfilesUrl);
	        },
	        
	        getForums: function(options){
	        	
	        	this.renderer.template = this.renderer.forumTemplate;
	        	this.renderer.headerTemplate = this.renderer.forumHeader;
	        	this._storeArgs.attributes = consts.ForumXPath;
	        	
	        	if(this.params.type == "my"){
	        		this.store._args.url = consts.AtomForumsMy;
	        	}else{
	        		this.store._args.url = consts.AtomForumTopics;
	        	}

	        	this._doQuery(this.store, options);
	        },
	        
	        _forumID: "",
	        
	        getTopics: function(forumId,options){
	        	
	        	if(forumId != ""){
	        		this._forumID = forumId;
	        	}

	        	this.renderer.template = this.renderer.topicTemplate;
	        	this.renderer.headerTemplate = this.renderer.topicHeader;
	        	this._storeArgs.attributes = consts.ForumTopicXPath;
	        	
	        	if(this.params.type=="myTopics"){
	        		this.store._args.url = consts.AtomTopicsMy;
	        	}else{
	        		this.store._args.url = consts.AtomForumTopics+"?forumUuid="+this._forumID;
	        	}

	        	this._doQuery(this.store, options);

	        },
	        
	        getTopicReplies: function(topicId,options){
	        	
	        	
	        	this.renderer.template = this.renderer.replyTemplate;
	        	this.renderer.headerTemplate = this.renderer.replyHeader;
	        	
	        	this._storeArgs.attributes = consts.ForumReplyXPath;
	        	
	        	this.store._args.url = consts.AtomReplies+"?topicUuid="+topicId;
	
	        	this._doQuery(this.store, options);
	        },
	        
	        previousPage: function(el, data, ev){
	        	if (this.backAction) {
	                this._stopEvent(ev);
	                this.backAction.previousPage(data, this , ev);
	            }
	        }
		
		});
	
	    return ForumGrid;
});
