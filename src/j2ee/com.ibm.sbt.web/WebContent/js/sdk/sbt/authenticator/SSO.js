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
 * Social Business Toolkit SDK.
 * Definition of an authentication mechanism.
 */
define(["../declare", "../lang", "../util"],function(declare, lang, util) {
/**
 * Proxy SSO authentication.
 * 
 * This class triggers the authentication for a service.
 */
return declare(null, {
	loginUi: "",
	messageSSO: "/sbt/authenticator/templates/MessageSSO.html",
	dialogMessageSSO: "authenticator/templates/MessageDialogSSO.html",
    url: "",
    
    constructor: function(args){
        lang.mixin(this, args || {});
    },
	
    authenticate: function(options) {
    	var self = this;
	    require(['sbt/config', 'sbt/i18n!sbt/nls/messageSSO'], function(config, ssoStr){
	        var mode =  options.loginUi || config.Properties["loginUi"] || self.loginUi;
	        var messagePage = options.messageSSO || config.Properties["messageSSO"] || self.messageSSO;
	        var dialogMessagePage = options.dialogMessageSSO || config.Properties["dialogMessageSSO"] || self.dialogMessageSSO;
	        if(mode=="popup") {
	            self._authPopup(options, messagePage, config, self.url, ssoStr);
	        } else if(mode=="dialog") {
	            self._authDialog(options, dialogMessagePage, config, ssoStr);
	        } else {
	            self._authMainWindow(options, messagePage, self.url, ssoStr);
	        }
	    });
		return true;
    	
	},
	
	_authPopup: function(options, messagePage, sbtConfig, sbtUrl, ssoStrings) {
        var urlParamsMap = {
            loginUi: 'popup'
        };
        var urlParams = util.createQuery(urlParamsMap, "&");
        var url = sbtUrl+messagePage + '?' + urlParams;
                                         
        var windowParamsMap = {
            width: window.screen.availWidth / 2,
            height: window.screen.availHeight / 2,
            left: window.screen.availWidth / 4,
            top: window.screen.availHeight / 4,
            menubar: 0,
            toolbar: 0,
            status: 0,
            location: 0,
            scrollbars: 1,
            resizable: 1
        };
        var windowParams = util.createQuery(windowParamsMap, ",");
        var loginWindow = window.open(url,'Authentication', windowParams);
        loginWindow.globalSSOStrings = ssoStrings;
        loginWindow.globalEndpointAlias = options.name;
        loginWindow.focus();
        
        return true;
	},
	
	_authDialog: function(options, dialogLoginPage, sbtConfig, ssoStr) {
		require(["sbt/_bridge/ui/SSO_Dialog", "sbt/dom"], function(dialog, dom) {	        
			dialog.show(options, dialogLoginPage, ssoStr);
			dom.setText('reloginMessage', ssoStr.message);
			dom.setAttr(dom.byId('ssoLoginFormOK'), "value", ssoStr.relogin_button_text);
		});
		return true;
	},
	
	_authMainWindow: function(options, loginPage, sbtUrl, ssoStrings) {
		var urlParamsMap = {
            redirectURL: document.URL,
            loginUi: 'mainWindow',
          	message_title: ssoStrings.message_title,
        	message: ssoStrings.message,
        	relogin_button_text: ssoStrings.relogin_button_text
        };
		
        var urlParams = util.createQuery(urlParamsMap, "&");
		var url = sbtUrl+loginPage + '?' + urlParams;
		var loginWindow = window.location.href = url;
		
		return true;
	}
}
);
});