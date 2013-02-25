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
 * Definition of an authentication mechanism.
 */
define(["sbt/_bridge/declare"],function(declare) {
/**
 * Proxy basic authentication.
 * 
 * This class triggers the authentication for a service.
 */
return declare("sbt.authenticator.Basic", null, {
	loginUi:		"",
	loginPage:		"/sbt/authenticator/templates/login.html",
	dialogLoginPage:"authenticator/templates/loginDialog.html",
	/**
	 * Method that authenticate the current user . 
	 * 
	 * This is a working version. But this is work in progress with following todos
	 * 
	 * todos:
	 *  Internationalization
	 */
	authenticate: function(options) {
		var mode =  options.loginUi || sbt.Properties["loginUi"] || this.loginUi ;
		var loginPage = options.loginPage || sbt.Properties["loginPage"] || this.loginPage;
		var dialogLoginPage = options.dialogLoginPage || sbt.Properties["dialogLoginPage"] || this.dialogLoginPage;
		if(mode=="popup") {
			return this._authPopup(options, loginPage);
		} else if(mode=="dialog") {
			return this._authDialog(options, dialogLoginPage);
		} else {
			return this._authMainWindow(options, loginPage);
		}
	},	
	
	_authDialog: function(options, dialogLoginPage) {
		require(["sbt/_bridge/ui/BasicAuth_Dialog"], function(dialog) {
			dialog.show(options, dialogLoginPage);
		});
		return true;
	},
	
	_authPopup: function(options, loginPage) {
		sbt.callback = options.callback;
		var proxy = options.proxy.proxyUrl;
		var actionURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
		var url = sbt.Properties["sbtUrl"]+loginPage+'?actionURL='+encodeURIComponent(actionURL)
										 +'&redirectURL=empty'
										 +'&loginUi=popup'
										 +'&showWrongCredsMessage=false';
										 //+'&endPointName='+endPointName;
		var newWindowWidth = 300;
		var newWindowLeftAttr = document.body.offsetWidth/2 - newWindowWidth/2;
		var loginWindow = window.open(url,'Authentication',
				   'width='+newWindowWidth
				   +',height=200'
				   +',left='+newWindowLeftAttr
				   +',menubar=0'
				   +',toolbar=0'
				   +',status=0'
				   +',location=0'
				   +',scrollbars=1'
				   +',resizable=1');
		return true;
	},
	
	_authMainWindow: function(options, loginPage) {
		var proxy = options.proxy.proxyUrl;
		var actionURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
		//var proxyServletURL='/sbt/proxy/basicAuth/'+options.proxyPath+"/JSApp";
		var url = sbt.Properties["sbtUrl"]+loginPage+'?actionURL='+encodeURIComponent(actionURL)
							 +'&redirectURL='+encodeURIComponent(document.URL)
							 +'&loginUi=mainWindow'
							 +'&showWrongCredsMessage=false';
		var newwindow=window.location.href = url;
		return true;
	}

}
);
});