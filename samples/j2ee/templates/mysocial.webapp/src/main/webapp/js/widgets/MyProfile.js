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
define([ "sbt/declare", "sbt/config", "sbt/lang", "sbt/dom", 
         "sbt/widget/_TemplatedWidget", "sbt/Endpoint", 
         "sbt/connections/ProfileService", "sbt/smartcloud/ProfileService" ], 
	function(declare, config, lang, dom, _TemplatedWidget, Endpoint, ConnectionsService, SmartcloudService) {
	
	function getTemplateString(domId) {
        var domNode = dom.byId(domId);
        return domNode ? domNode.innerHTML : "<img/><h3/><span/><span/><span/>";
    };
    
    /**
     * @class MyProfile
     * @namespace widgets
     * @module widgets.MyProfile
     */
   var MyProfile =  declare(_TemplatedWidget, {
	   
	    templateId: "myapp.MyProfileTmpl",

	    templateString: null,
	   
	    listeners: null,
        	
        /**
         * Constructor function
         * @method constructor
         */
        constructor: function(args) {
        	lang.mixin(this, args);
        	
        	this.templateString = getTemplateString(this.templateId);
        	this.listeners = [];
        },
        
        addListener: function(listener) {
        	this.listeners.push(listener);

        	// notify the listener if profile are already loaded
        	if (this.profile) {
        		listener.apply(this, [ this.profile ]);
        	}        	
        },
        
        /**
         * Called after the grid is created
         * The semanticTagService is loaded, which is responsible for displaying business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	
        	var imgs = this.domNode.getElementsByTagName("img");
        	var spans = this.domNode.getElementsByTagName("span");
        	
        	this.imgNode = imgs[0];
        	this.nameNode = spans[0];
        	this.titleNode = spans[1];
        	this.deptNode = spans[2];
        	
        	this.retrieveProfile();
        },
        
        getEmail: function() {
        	if (this.profile) {
        		return this.profile.getEmail();
        	}
        },
        
        retrieveProfile: function() {
        	var endpoint = config.findEndpoint("connections");
        	var url = null;
        	if (endpoint.isSmartCloud) {
        		url = "/manage/oauth/getUserIdentity";
        		config.Properties["loginUi"] = "popup";
        	} else {
        		url = "/connections/opensocial/basic/rest/people/@me/";
            	config.Properties["loginUi"] = "popup";
        	}
        	var options = { handleAs : "json" };
        	var self = this;
        	endpoint.request(url, options).then(
        		function(response) {
        			var userid = null;
        			if (endpoint.isSmartCloud) {
        				userid = response.subscriberid;
        			} else {
        				userid = response.entry.id.replace('urn:lsid:lconn.ibm.com:profiles.person:', '');
        			}
        			self.handleUserId(userid, endpoint.isSmartCloud);
        		}, 
    			function (error) {
    				self.nameNode.innerHTML = error;
    			}
        	);
        },
       
    	handleUserId: function(userid, isSmartCloud) {
        	var self = this;
        	var profileService = null;
        	if (isSmartCloud) {
        		profileService = new SmartcloudService({ endpoint : "connections" });
        	} else {
	    		profileService = new ConnectionsService();
        	}
        	profileService.getProfile(userid).then(
    			function(profile) {
    				self.profile = profile;
    				self.imgNode.src = profile.getThumbnailUrl();
    				self.imgNode.style.display = "";
    				self.nameNode.innerHTML = profile.getName();
    				self.titleNode.innerHTML = profile.getJobTitle();
    				self.deptNode.innerHTML = profile.getDepartment();
    				
    				self.notifyListeners(profile);
    			}, 
    			function (error) {
    				self.nameNode.innerHTML = error;
    			}
    		);
    	},
    	
    	notifyListeners: function(profile) {
       		for (var i=0; i<this.listeners.length; i++) {
       			this.listeners[i].apply(this, [ profile ]);
        	}
    	}
    });
	
    return MyProfile;
});
