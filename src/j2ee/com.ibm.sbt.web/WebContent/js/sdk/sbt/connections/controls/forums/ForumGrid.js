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
		 "./BackAction",
        "../../../connections/ForumConstants"], 
    function(declare, Grid, parameter, ForumGridRenderer, ForumAction, BackAction, consts){
	
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
	        backAction: new BackAction(),
	        hideBreadCrumb: true,
	        
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
	        
	        getForums: function(options){
	        	
	        	this.renderer.template = this.renderer.forumTemplate;
	        	this.renderer.headerTemplate = this.renderer.forumHeader;
	        	this.store.setAttributes(consts.ForumXPath);
	        	this.hideBreadCrumb = true;
	        	var endpoint = this.store.getEndpoint();
	        	
	        	if(this.params.type == "my"){
	        		var url = this.buildUrl(consts.AtomForumsMy, {},endpoint);
	        		this.store.setUrl(url);
	        	}else{
	        		var url = this.buildUrl(consts.AtomForumsPublic, {},endpoint);
	        		this.store.setUrl(url);
	        	}

	        	this.update(null);
	        },
	        
	        _forumID: "",
	        
	        /**
	         * 
	         * Show forum Topics
	         * @param forumId
	         * @param options
	         */
	        getTopics: function(forumId,options){
	        		        
	        	if(forumId != ""){
	        		this._forumID = forumId;
	        	}

	        	this.renderer.template = this.renderer.topicTemplate;
	        	this.renderer.headerTemplate = this.renderer.topicHeader;
	        	this.renderer.breadCrumb = this.renderer.topicBreadCrumb;
	        	this.store.setAttributes(consts.ForumTopicXPath);
	        	this.hideBreadCrumb = false;
	        	var endpoint = this.store.getEndpoint();
	        	        	
	        	if(this.params.type=="myTopics"){
	        		var url = this.buildUrl(consts.AtomTopicsMy, {});
	        		this.store.setUrl(url);
	        		this.hideBreadCrumb = true;
	        	}else{
	        		var url = this.buildUrl(consts.AtomTopics+"?forumUuid="+this._forumID, {}, endpoint);
	        		this.store.setUrl(url);
	        	}
	        	
	        	this.update(null);
	        },
	        
	        getTopicReplies: function(topicId,options){
	        	
	        	this.renderer.template = this.renderer.replyTemplate;
	        	this.renderer.headerTemplate = this.renderer.replyHeader;
	        	this.store.setAttributes(consts.ForumReplyXPath);
	        	this.hideBreadCrumb = false;
	        	var endpoint = this.store.getEndpoint();
	        	if(this.params.type=="myTopics"){
	        		this.renderer.breadCrumb = this.renderer.myTopicsBreadCrumb;
	        	}else{
	        		this.renderer.breadCrumb = this.renderer.replyBreadCrumb;
	        	}
	        	
	        	
	        	var url = this.buildUrl(consts.AtomReplies+"?topicUuid="+topicId,{},endpoint);
	        	this.store.setUrl(url);
	        	
	        	this.update(null);
	        },
	        
	        showTopics: function(el, data, ev){
	        	if (this.backAction) {
	                this._stopEvent(ev);
	                this.backAction.showTopics(data, this , ev);
	            }
	        },
	        
	        showForums: function(el, data, ev){
	        	if (this.backAction) {
	                this._stopEvent(ev);
	                this.backAction.showForums(data, this , ev);
	            }
	        },
	        
	        /**
	         * Add the since parameter to the URL, so that all forums will be 
	         * displayed and not just those that have been recently modified.
	         * The since parameter returns all entries last modified since a specified date. 
	         * Specify the date in the number of milliseconds since Unix EPOCH.  
	         * In this case 1 is used so all forums will be displayed.
	         * @param url The Rest API URL for the forum feed
	         * @param args
	         * @param endpoint An endpoint which may contain custom service mappings.
	         * @returns
	         */
	        buildUrl: function(url, args, endpoint) {	        	
	            var urlParams = { since: 1};
	            if (this.query) {
	            	params = lang.mixin(params, this.query);
	            }
	            if (this.direction) {
	            	params = lang.mixin(params, { direction : this.direction });
	            } 
	            return this.constructUrl(url, urlParams, {}, endpoint);
	        },
	        

		});
	
	    return ForumGrid;
});
