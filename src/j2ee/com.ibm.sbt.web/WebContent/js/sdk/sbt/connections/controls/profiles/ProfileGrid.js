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
 * 
 */
define([ "../../../declare", 
		 "../../../controls/grid/Grid", 
		 "./ProfileGridRenderer", 
		 "./ProfileAction", 
		 "../../../connections/controls/vcard/SemanticTagService", 
		 "../../../config",
		 "../../../store/parameter",
		 "../../../connections/ProfileConstants"], 
        function(declare, Grid, ProfileGridRenderer, ProfileAction, SemanticTagService, sbt, parameter) {

	// TODO use values from constants and handle authType
	var profileUrls = {
	    profile : "/profiles/atom/profile.do", 
        reportingChain : "/profiles/atom/reportingChain.do?outputType=profile&format=full", 
        colleagues: "/profiles/atom/connections.do?connectionType=colleague&outputType=profile&format=full" , 
        peopleManaged: "/profiles/atom/peopleManaged.do",
        statusUpdates: "/profiles/atom/mv/theboard/entries.do?outputType=profile&format=full",
        connectionsInCommon: "/profiles/atom/connectionsInCommon.do?connectionType=colleague&outputType=profile&format=full"
	};
	var xpath_profile = {
        "id":               "a:id",
	 	"entry":			"/a:feed/a:entry",
		"uid":				"a:contributor/snx:userid",
		"name":				"a:contributor/a:name",
		"email":            "a:contributor/a:email",
		"title":            "a:title",
		"statusUpdate":     "a:title[@type='text']",
		"statusLastUpdate": "a:updated",
		"altEmail":         "a:content/h:div/h:span/h:div[@class='x-groupwareMail']",
		"photo":			"a:link[@rel='http://www.ibm.com/xmlns/prod/sn/image']/@href",			
		"jobTitle":			"a:content/h:div/h:span/h:div[@class='title']",
		"organizationUnit":	"a:content/h:div/h:span/h:div[@class='org']/h:span[@class='organization-unit']",
		"fnUrl":			"a:content/h:div/h:span/h:div/h:a[@class='fn url']/@href",			
		"telephoneNumber":	"a:content/h:div/h:span/h:div[@class='tel']/h:span[@class='value']",			
		"bldgId":			"a:content/h:div/h:span/h:div/h:span[@class='x-building']",			
		"floor":			"a:content/h:div/h:span/h:div/h:span[@class='x-floor']",
		"streetAddress":	"a:content/h:div/h:span/h:div/h:div[@class='street-address']",
		"extendedAddress":	"a:content/h:div/h:span/h:div/h:div[@class='extended-address x-streetAddress2']",
		"locality":			"a:content/h:div/h:span/h:div/h:span[@class='locality']",
		"postalCode":		"a:content/h:div/h:span/h:div/h:span[@class='postal-code']",
		"region":			"a:content/h:div/h:span/h:div/h:span[@class='region']",
		"countryName":		"a:content/h:div/h:span/h:div/h:div[@class='country-name']",			
		"soundUrl":			"a:content/h:div/h:span/h:div/h:a[@class='sound url']/@href",	
		"summary":			"a:summary",
		"groupwareMail":	"a:content/h:div/h:span/h:div[@class='x-groupwareMail']",				
		"networkProfileId":	"snx:connection/a:contributor[@snx:rel='http://www.ibm.com/xmlns/prod/sn/connection/target']/snx:userid",
		"networkProfileName":	"snx:connection/a:contributor[@snx:rel='http://www.ibm.com/xmlns/prod/sn/connection/target']/a:name",
		"networkProfileEmail":	"snx:connection/a:contributor[@snx:rel='http://www.ibm.com/xmlns/prod/sn/connection/target']/a:email",
        "totalResults"      :"/a:feed/opensearch:totalResults",
        "startIndex"        :"/a:feed/opensearch:startIndex",
        "itemsPerPage"      :"/a:feed/opensearch:itemsPerPage"
	};
	
	var sortVals = {
			displayName: "displayName",
       		recent: "3" 
	};
	
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
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
            "reportingChain" : {
                storeArgs : {
                    url : profileUrls.reportingChain,
                    attributes : xpath_profile,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "profile" : {
                storeArgs : {
                    url : profileUrls.profile,
                    attributes : xpath_profile,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "colleagues" : {
                storeArgs : {
                     url : profileUrls.colleagues,
                    attributes : xpath_profile,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "peopleManaged" : {
                storeArgs : {
                    url : profileUrls.peopleManaged,
                    attributes : xpath_profile,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "connectionsInCommon" : {
                storeArgs : {
                    url : profileUrls.connectionsInCommon,
                    attributes : xpath_profile,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "dynamic" : {
                storeArgs : {
                    attributes : xpath_profile
                },
                rendererArgs : {
                    type : "profile"
                }
            }
        },
        
        /**
         * A profile action, defines default behaviour for when 
         * items in the grid are clicked on or hovered on,
         * it is possible to override these actions
         */
        profileAction: new ProfileAction(),
        
        /**
         * This is the default grid that will be created if no 
         * arguments are given.
         */
        defaultOption: "colleagues",
        
        /**Constructor function
         * @method constructor
         * */
        constructor: function(args){
        	
        	if(args.type == "peopleManaged" || args.type == "reportingChain" || args.type == "profile"){
        		this.hideSorter = true;
        	}
        	
            var nls = this.renderer.nls;
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
            this._activeSortIsDesc = false;
        },
        
        
        /**
         * Creates an instance of an AtomStore
         * @method createDefaultStore
         * @param args will contain the url and attributes for the atom store.
         * @returns The createDefaultStore method of the superclass, which will return an
         * instance of the AtomStore.
         */
        createDefaultStore : function(args) {
            if (!args.url) {
                return null;
            }
            
            args.url = this._buildUrl(args.url);

            return this.inherited(arguments);
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
        	
        	SemanticTagService.loadSemanticTagService();
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
            return new ProfileGridRenderer(args);
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
                
                this.profileAction.execute(data, { grid : this.grid }, ev);
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

        // Internals
        _buildUrl: function(url) {
            if (url.indexOf("?") < 0) {
                url += "?";
            } else {
                url += "&";
            }
            
            if (this.email) {
                url += "email=" + this.email;
            } else if (this.email1 && this.email2) {
                url += "email=" + this.email1 + "," + this.email2;
            } else if (this.userid) {
                url += "userid=" + this.userid;
            } else if (this.userid1 && this.userid2) {
                url += "userid=" + this.userid1 + "," + this.userid2;
            }

            return url;
        }

    });

    return ProfileGrid;
});