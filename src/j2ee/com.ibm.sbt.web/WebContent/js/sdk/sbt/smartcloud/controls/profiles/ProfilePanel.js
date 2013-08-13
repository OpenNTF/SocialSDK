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
 * @module sbt.connections.controls.profiles.ProfilePanel
 */
define(["../../../declare", "../../../lang", "../../../config", 
        "../../../smartcloud/ProfileService",
        "../../../controls/panel/_ProfilePanel",
        "../../../text!sbt/smartcloud/controls/profiles/templates/ProfilePanel.html"], 
        function(declare, lang, config, ProfileService, _ProfilePanel, PanelTmpl) {

    var getUserIdentity = "/manage/oauth/getUserIdentity";
	
    /**
     * @module sbt.connections.controls.profiles.ProfilePanel
     */
    var ProfilePanel = declare([ _ProfilePanel ], {
    	
    	template: PanelTmpl,
    	profileService: null,
                
        constructor: function(args) {
        },
        
        getMyProfile: function() {
            var path = getUserIdentity;
            
            var self = this;
            var endpoint = this._getEndpoint();
            endpoint.request(path, { handleAs : "json", preventCache : true }).then(
                function(response) {
                	var userid = response.subscriberid;
                    self.getProfile(userid);
                },
                function(error) {
                    self._displayError(error);
                }
            );
        },
        
        getProfile: function(id) {
        	var self = this;
            var promise = this._getProfileService().getProfileByGUID(id);
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
        	return this.endpoint || "smartcloud";
        }
        
    });
    
    return ProfilePanel;
});