/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.profiles.ProfileGrid
 */
define([ "../../../declare", 
		 "../../../config",
		 "../../../lang",
		 "../../../controls/grid/Grid", 
		 "./ProfileGridRenderer", 
		 "./ProfileAction", 
		 "../../../connections/controls/vcard/SemanticTagService", 
		 "../../../store/parameter",
		 "../../../connections/ProfileConstants",
		 "../../../connections/CommunityConstants",
		 "../../../base/URLBuilder"], 
        function(declare, sbt, lang, Grid, ProfileGridRenderer, ProfileAction, SemanticTagService, parameter, consts, communities, URLBuilder) {

	var sortVals = {
			displayName: "displayName",
       		recent: "3" 
	};
	
	var communityMembersSortVals = {
			displayName: "displayName",
       		created: "created" 
	};
	
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")
	};
	
	var CommunityMembersParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy", communityMembersSortVals),
		sortOrder: parameter.sortOrder("sortOrder")
	};
	
    /**
     * @class ProfileGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileGrid
     */
    var ProfileGrid = declare(Grid, {
    	
    	/**
    	 * @param options, This is a list of all
    	 * the different types of profile grids available.
    	 * Depending on which one is selected specific arguments will be given to
    	 * the atom store and grid renderer.
    	 */
        options : {
            "profile" : {
                storeArgs : {
                    url : consts.AtomProfileDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "colleagues" : {
                storeArgs : {
                     url : consts.AtomConnectionsDo,
                     attributes : consts.ProfileXPath,
                     feedXPath : consts.ProfileFeedXPath,
                     paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "connectionsInCommon" : {
                storeArgs : {
                    url : consts.AtomConnectionsInCommonDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "peopleManaged" : {
                storeArgs : {
                    url : consts.AtomPeopleManagedDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "reportingChain" : {
                storeArgs : {
                    url : consts.AtomReportingChainDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "search" : {
                storeArgs : {
                    url : consts.AtomSearchDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "dynamic" : {
                storeArgs : {
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "communityMembers" : {
                storeArgs : {
                	url : communities.AtomCommunityMembers,
                    attributes : communities.MemberXPath,
                    feedXPath : communities.CommunityFeedXPath,
                    paramSchema: CommunityMembersParamSchema
                },
                rendererArgs : {
                    type : "communityMembers"
                }
            }
        },
        
        /**
         * A profile action, defines default behaviour for when 
         * items in the grid are clicked on or hovered on,
         * it is possible to override these actions
         */
        profileAction: new ProfileAction(),
        
        builder : new URLBuilder(),
        
        /**
         * This is the default grid that will be created if no 
         * arguments are given.
         */
        defaultOption: "colleagues",
        
        /**Constructor function
         * @method constructor
         * */
        constructor: function(args){
        	if(args.type == "peopleManaged" || args.type == "reportingChain" || args.type == "profile") {
        		this.hideSorter = true;
        	} 	
        	
            var nls = this.renderer.nls;

            if (args.type == "communityMembers") {
            	
            	this._sortInfo = {
            			displayName: { 
            				title: nls.displayName, 
       	                    sortMethod: "sortByTitle",
       	                    sortParameter: "title"   
            			},
       	                recent: {
       	                	title: nls.created, 
       	                    sortMethod: "sortByCreated",
       	                    sortParameter: "created"   
       	                }
            	};
       		 	this._activeSortAnchor = this._sortInfo.created;
       		 	this._activeSortIsDesc = true;
            } else {
            	this._sortInfo = {
            			displayName: { 
            				title: nls.displayName, 
            				sortMethod: "sortByDisplayName",
            				sortParameter: "displayName" 
            			},
            			recent: {
            				title: nls.recent, 
            				sortMethod: "sortByRecent",
            				sortParameter: "recent"   
            			}
               
            	};
            	this._activeSortAnchor = this._sortInfo.recent;
                this._activeSortIsDesc = true;
            }
            
        },
        
        contextRootMap: {
            profiles: "profiles"
        },
        
        /**
         * Override buildUrl to add outputType, format and email/userid's
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            url = this.builder.build(url, endpoint.apiVersion, {
            	authentication : endpoint.authType === "oauth" ? "oauth":""
			});
            var params = { 
            	outputType : "profile",
            	format : "full"
            };
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.type == "colleagues") {
            	params = lang.mixin(params, { connectionType : "colleague" });
            } else if (this.type == "communityMembers") {
            	params = lang.mixin(params, { communityUuid : this.communityUuid });
            }
         
            if (this.email) {
            	params = lang.mixin(params, { email : this.email });
            } 
            if (this.email1 && this.email2) {
            	params = lang.mixin(params, { email : this.email1 + "," + this.email2 });
            } 
            if (this.userid) {
            	params = lang.mixin(params, { userid : this.userid });
            } 
            if (this.userid1 && this.userid2) {
            	params = lang.mixin(params, { userid : this.userid1 + "," + this.userid2 });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * The post create function is called, after the grid has been created.
         * The function will call the super classes post create
         * then load the semantic tag service. The semantic tag service
         * is Javascript for creating business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	if(this.displayBusinessCard){
        		SemanticTagService.loadSemanticTagService();
        	}
        },
        
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ProfileGridRenderer(args,this);
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
            if (this.profileAction) {
            	console.log(data);
                this._stopEvent(ev);
                
                this.profileAction.execute(data, this, ev);
            }
        },
        
        /**
         * @method getSortInfo
         * @returns A list of strings that describe how the grid can be sorted
         * for profile grids these strings are "Display Name" and "Recent"
         */
        getSortInfo: function() {
        	return {
        		active: {
        			anchor: this._activeSortAnchor,
        			isDesc: this._activeSortIsDesc
        		},
        		list: [this._sortInfo.displayName, this._sortInfo.recent]
            };
        	
        },
                
        sortByDisplayName: function(el, data, ev){
        	this._sort("displayName", true, el, data, ev);
        },

        sortByRecent: function(el, data, ev){
        	this._sort("recent", true, el, data, ev);
        },
        
        sortByCreated: function(el, data, ev){
        	this._sort("created", true, el, data, ev);
        },
        
        sortByTitle: function(el, data, ev){
        	this._sort("title", true, el, data, ev);
        }

        // Internals
        
    });

    return ProfileGrid;
});