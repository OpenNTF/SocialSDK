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
 
dojo.provide("sbt.authenticator.SSO");
/**
 * Social Business Toolkit SDK.
 * Definition of an authentication mechanism.
 */
define('sbt/authenticator/SSO',["sbt/declare", "sbt/lang", "sbt/util", "sbt/i18n!sbt/nls/messageSSO"],function(declare, lang, util, ssoMessages) {
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
	    require(['sbt/config'], function(config){
	        var mode =  options.loginUi || config.Properties["loginUi"] || self.loginUi;
	        var messagePage = options.messageSSO || config.Properties["messageSSO"] || self.messageSSO;
	        var dialogMessagePage = options.dialogMessageSSO || config.Properties["dialogMessageSSO"] || self.dialogMessageSSO;
	        if(mode=="popup") {
	            self._authPopup(options, messagePage, self.url);
	        } else if(mode=="dialog") {
	            self._authDialog(options, dialogMessagePage);
	        } else {
	            self._authMainWindow(options, messagePage, self.url);
	        }
	    });
		return true;
    	
	},
	
	_authPopup: function(options, messagePage, sbtUrl) {
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
        loginWindow.globalSSOStrings = ssoMessages;
        loginWindow.globalEndpointAlias = options.name;
        loginWindow.focus();
        
        return true;
	},
	
	_authDialog: function(options, dialogLoginPage) {
		require(["sbt/_bridge/ui/SSO_Dialog", "sbt/dom"], function(dialog, dom) {	        
			dialog.show(options, dialogLoginPage, ssoMessages);
			dom.setText('reloginMessage', ssoMessages.message);
			dom.setAttr(dom.byId('ssoLoginFormOK'), "value", ssoMessages.relogin_button_text);
		});
		
		return true;
	},
	
	_authMainWindow: function(options, loginPage, sbtUrl) {
		var urlParamsMap = {
            redirectURL: document.URL,
            loginUi: 'mainWindow',
          	message_title: ssoMessages.message_title,
        	message: ssoMessages.message,
        	relogin_button_text: ssoMessages.relogin_button_text
        };
		
        var urlParams = util.createQuery(urlParamsMap, "&");
		var url = sbtUrl+loginPage + '?' + urlParams;
		var loginWindow = window.location.href = url;
		
		return true;
	}
}
);
});
