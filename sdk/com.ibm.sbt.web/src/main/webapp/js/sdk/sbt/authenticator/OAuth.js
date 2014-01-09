/*
 * © Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK.
 * Definition of the authentication mechanism for OAuth 1.0.
 */
define(['../declare','../lang', '../util'], function(declare, lang, util) {

	/**
	 * OAuth 1.0 authentication.
	 * 
	 * This class triggers the authentication for a service.
	 */
	return declare(null, {
		
		url:			"",
		loginUi:		"",	// mainWindow, dialog or popup
	
		constructor: function(args){
			lang.mixin(this, args || {});	
		},
	
		/**
		 * Method that authenticates the current user 
		 */
		authenticate: function(options) {
		    var self = this;
			require(["sbt/config"], function(config) {
			    var mode = options.loginUi || config.Properties["loginUi"] || this.loginUi;
            	var width = config.Properties["login.oauth.width"] || 800;
            	var height = config.Properties["login.oauth.height"] || 450;
	            if(mode=="popup") {
	                return self._authPopup(options, self.url, width, height);
	            } else if(mode=="dialog") {
	                return self._authDialog(options, self.url, width, height);
	            } else {
	                return self._authMainWindow(options, self.url);
	            }
			});
		},
		
		_authMainWindow: function(options, sbtUrl) {
			var url = sbtUrl + "?oaredirect="+encodeURIComponent(window.location.href);
			newwindow=window.location.href = url;
			return true;
			
		},
		
		_authPopup: function(options, sbtUrl, width, height) {
		    require(["sbt/config"], function(config){
		        config.callback = options.callback;
	            var url = sbtUrl + "?loginUi=popup";
	            
	            var windowQueryMap = {
                    height: height,
                    width: width
	            };
	            var windowQuery = util.createQuery(windowQueryMap, ",");
	            newwindow = window.open(url,'Authentication',windowQuery);
	            return true;
		    });
		},
		
		_authDialog: function(options, sbtUrl, width, height) {
			require(["sbt/_bridge/ui/OAuthDialog"], function(dialog) {
				dialog.show(sbtUrl, widht, height);
			});
			return true;
		}
	});
});