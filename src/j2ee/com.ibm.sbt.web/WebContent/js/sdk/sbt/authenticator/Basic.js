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
define(["../declare", "../lang", "../util"],function(declare, lang, util) {
/**
 * Proxy basic authentication.
 * 
 * This class triggers the authentication for a service.
 */
return declare(null, {
	loginUi:		"",
	loginPage:		"/sbt/authenticator/templates/login.html",
	dialogLoginPage:"authenticator/templates/loginDialog.html",
	url: "",
	
	/**
	 * Constructor, necessary so that this.url is not empty. 
	 * It may also mixin loginUi, loginPage or dialogLoginPage if they are present in sbt.properties.
	 */
	constructor: function(args){
	    lang.mixin(this, args || {});
	},
	
	/**
	 * Method that authenticate the current user . 
	 * 
	 * This is a working version. But this is work in progress with following todos
	 * 
	 * todos:
	 *  Internationalization
	 */
	authenticate: function(options) {
	    var self = this;
	    require(['sbt/config'], function(config){
	        var mode =  options.loginUi || config.Properties["loginUi"] || self.loginUi;
	        var loginPage = options.loginPage || config.Properties["loginPage"] || self.loginPage;
	        var dialogLoginPage = options.dialogLoginPage || config.Properties["dialogLoginPage"] || self.dialogLoginPage;
	        if(mode=="popup") {
	            self._authPopup(options, loginPage, config, self.url);
	        } else if(mode=="dialog") {
	            self._authDialog(options, dialogLoginPage, config);
	        } else {
	            self._authMainWindow(options, loginPage, self.url);
	        }
	    });

	    return true;
	},	
	
	_authDialog: function(options, dialogLoginPage, sbtConfig) {
		require(["sbt/_bridge/ui/BasicAuth_Dialog", "sbt/i18n!sbt/nls/loginForm", "sbt/dom"], function(dialog, loginForm, dom) {
		    if(options.cancel){
	            sbtConfig.cancel = options.cancel;
	        }
	        
			dialog.show(options, dialogLoginPage);
			dom.setText('wrongCredsMessage', loginForm.wrong_creds_message);
			dom.setText('basicLoginFormUsername', loginForm.username);
			dom.setText('basicLoginFormPassword', loginForm.password);
			dom.setAttr(dom.byId('basicLoginFormOK'), "value", loginForm.login_ok);
			dom.setAttr(dom.byId('basicLoginFormCancel'), "value", loginForm.login_cancel);
		});
		return true;
	},
	
	_authPopup: function(options, loginPage, sbtConfig, sbtUrl) {
	    require(["sbt/i18n!sbt/nls/loginForm"], function(loginForm) {
            var proxy = options.proxy.proxyUrl;
            var actionURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
            var urlParamsMap = {
                actionURL: actionURL,
                redirectURL: 'empty',
                loginUi: 'popup',
                showWrongCredsMessage: 'false'
            };
            var urlParams = util.createQuery(urlParamsMap, "&");
            var url = sbtUrl+loginPage + '?' + urlParams;
                                             
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
            if(options.callback){
                sbtConfig.callback = options.callback;
                loginWindow.callback = options.callback;
            }
            if(options.cancel){
                sbtConfig.cancel = options.cancel;
                loginWindow.cancel = options.cancel;
            }
            loginWindow.globalLoginFormStrings = loginForm;
            loginWindow.globalEndpointAlias = options.name;
            loginWindow.focus();
        });
        
        return true;
	},
	
	_authMainWindow: function(options, loginPage, sbtUrl) {
		var proxy = options.proxy.proxyUrl;
		var actionURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
		var urlParamsMap = {
            actionURL: actionURL,
            redirectURL: document.URL,
            loginUi: 'mainWindow',
            showWrongCredsMessage: 'false'
        };
		
        var urlParams = util.createQuery(urlParamsMap, "&");
		var url = sbtUrl+loginPage + '?' + urlParams;
		window.location.href = url;
		
		return true;
	}
	
}
);
});
