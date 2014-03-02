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
 
dojo.provide("sbt.connections.controls.profiles.ProfilePanel");

/**
 * @module sbt.connections.controls.profiles.ProfilePanel
 */
define(["../../../declare", "../../../lang", "../../../config", 
        "../../../connections/ProfileService",
        "../../../controls/panel/_ProfilePanel",
        "../../../text!sbt/connections/controls/profiles/templates/ProfilePanel.html"], 
        function(declare, lang, config, ProfileService, _ProfilePanel, PanelTmpl) {

    var basicPeopleMe = "/connections/opensocial/basic/rest/people/@me/";
    var oauthPeopleMe = "/connections/opensocial/oauth/rest/people/@me/";
	
    /**
     * @module sbt.connections.controls.profiles.ProfilePanel
     */
    var ProfilePanel = declare([ _ProfilePanel ], {
    	
    	template: PanelTmpl,
    	profileService: null,
                
        constructor: function(args) {
        },
        
        getMyProfile: function() {
            var endpoint = this._getEndpoint();
            var path = basicPeopleMe;
            if (endpoint.authType == 'oauth') {
                path = oauthPeopleMe;
            }
            
            var self = this;
            endpoint.request(path, { handleAs : "json", preventCache : true }).then(
                function(response) {
                    var userid = response.entry.id.replace('urn:lsid:lconn.ibm.com:profiles.person:', '');
                    self.getProfile(userid);
                },
                function(error) {
                    self._displayError(error);
                }
            );
        },
        
        getProfile: function(id) {
        	var self = this;
            var promise = this._getProfileService().getProfile(id);
            promise.then(    
                function(profile) {
                	self.profile = profile;
                	self.showProfile();
                },
                function(error) {            	
                    self.profile = null;
                    self._displayError(error);
                }
            );
        },
        
        // Internals
        
        _getProfileService: function() {
        	if (!this.profileService) {
                this.profileService = new ProfileService(this._getEndpointName());
        	}
        	return this.profileService;
        },
        
        _getEndpoint: function() {
        	return config.findEndpoint(this._getEndpointName());
        },
        
        _getEndpointName: function() {
        	return this.endpoint || "connections";
        }        
                
    });
    
    return ProfilePanel;
});