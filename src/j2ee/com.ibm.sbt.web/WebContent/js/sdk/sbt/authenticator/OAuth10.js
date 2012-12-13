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
define(['sbt/_bridge/declare','sbt/lang'], function(declare,lang) {

	/**
	 * OAuth 1.0 authentication.
	 * 
	 * This class triggers the authentication for a service.
	 */
	return declare("sbt.authenticator.OAuth10", null, {
		
		url:			"",
		loginUi:		"",	// mainWindow, dialog or popup
	
		constructor: function(args){
			lang.mixin(this, args || {});	
		},
	
		/**
		 * Method that authenticates the current user 
		 */
		authenticate: function(options) {
			var mode = options.loginUi || this.loginUi || (sbt.Properties ? sbt.Properties["loginUi"] : null);
			if(mode=="popup") {
				return this._authPopup(options);
			} else if(mode=="dialog") {
				return this._authDialog(options);
			} else {
				return this._authMainWindow(options);
			}
		},
		
		_authMainWindow: function(options) {
			var url = this.url + "?oaredirect="+encodeURIComponent(window.location.href);
			newwindow=window.location.href = url;
			return true;
			
		},
		
		_authPopup: function(options) {
			sbt.callback = options.callback;
			var url = this.url + "?loginUi=popup";
			newwindow=window.open(url,'Authentication','height=700,width=650');
			return true;
			
		},
		
		_authDialog: function(options) {
			var self=this;
			require(["sbt/_bridge/ui/OAuth10_Dialog"], function(dialog) {
				// TODO: should run the dance when done...
				dialog.show(self.url);
			});
			return true;
		}
	});
});