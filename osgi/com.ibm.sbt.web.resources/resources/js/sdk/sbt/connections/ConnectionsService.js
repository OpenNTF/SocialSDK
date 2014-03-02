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
 
dojo.provide("sbt.connections.ConnectionsService");

/**
 * Connections common APIs allow application programs to discover information about IBM� Connections as a whole.
 * 
 * @module sbt.connections.ConnectionsService
 */
define('sbt/connections/ConnectionsService',[ "sbt/declare", "sbt/config", "sbt/Promise", "sbt/connections/ConnectionsConstants", "sbt/base/BaseService",
         "sbt/base/AtomEntity", "sbt/base/XmlDataHandler", "sbt/xpath" ], 
    function(declare,config,Promise,consts,BaseService,AtomEntity,XmlDataHandler,xpath) {
	
	var CategoryConnectionsBookmark = "<category term=\"bookmark\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>"; 
	var CategoryMember = "<category term=\"person\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
	var CategoryServiceConfig = "<category term=\"service-config\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>"
	
    var ServiceConfigsDataHandler = declare(XmlDataHandler, {
        /**
         * @method getSummary
         * @returns
         */
    	getSummary : function() {
    		var languageTermsArray = this._selectArray("language");
    		var languageLabelsArray = this._selectArray("languageLabels");
    		var displayLanguagesMap = [];
    		for(var i=0;i<languageTermsArray.length;i++){
    			displayLanguagesMap.push({label: languageLabelsArray[i], term: languageTermsArray[i]});
    		}
    		if (!this._summary) {
                this._summary = {
                		isEmailExposed : (xpath.selectText(this.data, this._getXPath("emailConfig"), this.namespaces)=="email-exposed")?"true":"false",
                		displayLanguages : displayLanguagesMap
                };
            }
            return this._summary;
        }
    });
		
		
	 /**
     * ServiceConfig class represents an entry for a service config of a Connections server
     * 
     * @class ServiceConfig
     * @namespace sbt.connections
     */
    var ServiceConfig = declare(AtomEntity, {

    	xpath : consts.ServiceConfigXPath,
    	namespaces : consts.Namespaces,
    	categoryScheme : CategoryServiceConfig,
    	    	
        /**
         * Construct a ServiceConfig entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return the value of IBM Connections service config alternate SSL URL from service config ATOM
         * entry document.
         * 
         * @method getAlternateSSLUrl
         * @return {String} Alternate SSL URL
         */
        getAlternateSSLUrl : function() {
            return this.getAsString("alternateSSLUrl");
        }
    });
    
    /**
     * Member class represents an entry for a member of a Connections Activity, Community or Forum
     * 
     * @class Member
     * @namespace sbt.connections
     */
    var Member = declare(AtomEntity, {

    	xpath : consts.MemberXPath,
    	namespaces : consts.Namespaces,
    	categoryScheme : CategoryMember,
    	    	
        /**
         * Construct a Member entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return the value of IBM Connections bookmark URL from bookmark ATOM
         * entry document.
         * 
         * @method getUrl
         * @return {String} Bookmark URL of the bookmark
         */
        getRole : function() {
            return this.getAsString("role");
        }
    });
    
    /**
     * ReportEntry class represents the elements in a report created to flag inappropriate content. 
     * 
     * @class ReportEntry
     * @namespace sbt.connections
     */
    var ReportEntry = declare(AtomEntity, {

    	xpath : consts.ReportEntryXPath,
    	namespaces : consts.Namespaces,
    	    	
        /**
         * Construct a ReportEntry entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Returns the type of issue reported
         * The default values are:
		 *	001
		 *	Legal issue
		 *  002
		 *	Human resource issue
         * 
         * @method getCategoryIssue
         * @return {String} Issue type
         */
        getCategoryIssue : function() {
            return this.getAsString("categoryIssue");
        },
        
        /**
         * Return the related link url entry that you want to flag
         * 
         * @method getReportedItemLink
         * @return {String} Reported item link url
         */
        getReportedItemLink : function() {
            return this.getAsString("reportItemLink");
        },
        
        /**
         * Return the related link url of the ATOM entry document .Required when flagging blog posts or blog comnments
         * 
         * @method getRelatedLink
         * @return {String} Related link url
         */
        getRelatedLink : function() {
            return this.getAsString("relatedLink");
        },
        
        /**
         * Return the id of the concerned entry
         * 
         * @method getReferredEntryId
         * @return {String} Reported item id
         */
        getReferredEntryId : function() {
            return this.getAsString("inRefTo");
        }
    });
    
    /**
     * ReportEntry class represents the elements in a report created to flag inappropriate content. 
     * 
     * @class ReportEntry
     * @namespace sbt.connections
     */
    var ModerationActionEntry = declare(AtomEntity, {

    	xpath : consts.ModerationActionEntryXPath,
    	namespaces : consts.Namespaces,
    	    	
        /**
         * Construct a ModerationActionEntry entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Returns the type of moderation action to be taken
         * The options for premoderation are:
		 *	    approve
		 *	    reject
		 * The options for post-moderation are:
		 *	    dismiss
		 *	    quarantine
		 *	    restore
		 *	For Blogs, the option return is also supported for post-moderation of posts, but not comments.
         * 
         * @method getModerationAction
         * @return {String} Issue type
         */
        getModerationAction : function() {
            return this.getAsString("moderationAction");
        },
        
        /**
         * Return the related link url of the entry on which you want to take action using a link element.
         * This element is used in the Blogs API.
         * 
         * @method getRelatedLink
         * @return {String} Related link url
         */
        getRelatedLink : function() {
            return this.getAsString("relatedLink");
        },
        
        /**
         * Return the resource-id of the concerned entry
         * 
         * @method getReferredEntryId
         * @return {String} Moderated item id
         */
        getReferredEntryId : function() {
            return this.getAsString("inRefTo");
        }
    });
    
    /*
     * Callbacks used when reading a connections service configs feed.
     */
    var ConnectionsServiceConfigsCallbacks = {
        createEntities : function(service,data,response) {
            return new ServiceConfigsDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ConnectionsServiceDocsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new ServiceConfig({
            	service : service,
                data : data,
                response : response
            });
        }
    };

    /**
     * ConnectionService class.
     * 
     * @class ConnectionService
     * @namespace sbt.connections
     */
    var ConnectionsService = declare(BaseService, {

        /**
         * Constructor for ConnectionsService
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
         * Retrieve configuration information for the server.
         * 
         * @method getServiceConfigs
         * 
         */
        getServiceConfigEntries : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            url = this.constructUrl(consts.ServiceConfigs, null, {
            	service : this.serviceName
			});
            return this.getEntities(url, options, ConnectionsServiceConfigsCallbacks);
        }

    });
    return ConnectionsService;
});