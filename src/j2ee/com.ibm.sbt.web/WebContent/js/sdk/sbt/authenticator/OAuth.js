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
define(['../declare','../lang'], function(declare,lang) {

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
		    var popup = this._authPopup;
		    var authDialog = this._authDialog;
		    var authMainWindow = this._authMainWindow;
		    var sbtUrl = this.url;
			require(["sbt/config"], function(config){
			    var mode = options.loginUi || config.Properties["loginUi"] || this.loginUi;
	            if(mode=="popup") {
	                return popup(options, sbtUrl);
	            } else if(mode=="dialog") {
	                return authDialog(options, sbtUrl);
	            } else {
	                return authMainWindow(options, sbtUrl);
	            }
			});
		},
		
		_authMainWindow: function(options, sbtUrl) {
			var url = sbtUrl + "?oaredirect="+encodeURIComponent(window.location.href);
			newwindow=window.location.href = url;
			return true;
			
		},
		
		_authPopup: function(options, sbtUrl) {
		    require(["sbt/config"], function(config){
		        config.callback = options.callback;
	            var url = sbtUrl + "?loginUi=popup";
	            newwindow=window.open(url,'Authentication','height=700,width=650');
	            return true;
		    });
		},
		
		_authDialog: function(options, sbtUrl) {
			require(["sbt/_bridge/ui/OAuth10_Dialog"], function(dialog) {
				// TODO: should run the dance when done...
				dialog.show(sbtUrl);
			});
			return true;
		}
	});
});