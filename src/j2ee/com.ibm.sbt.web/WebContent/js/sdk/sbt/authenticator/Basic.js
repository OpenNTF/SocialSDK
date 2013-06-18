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
define(["../declare", "../lang"],function(declare, lang) {
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
	    var popup = this._authPopup;
	    var dialog = this._authDialog;
	    var mainWindow = this._authMainWindow;
	    var sbtUrl = this.url;
	    var defaultLoginPage = this.loginPage;
	    var defaultLoginUi = this.loginUi;
	    var defaultDialogLoginPage = this.dialogLoginPage;
	    require(['sbt/config'], function(config){
	        var mode =  options.loginUi || config.Properties["loginUi"] || defaultLoginUi;
	        var loginPage = options.loginPage || config.Properties["loginPage"] || defaultLoginPage;
	        var dialogLoginPage = options.dialogLoginPage || config.Properties["dialogLoginPage"] || defaultDialogLoginPage;
	        if(mode=="popup") {
	            popup(options, loginPage, config, sbtUrl, loginPage);
	        } else if(mode=="dialog") {
	            dialog(options, dialogLoginPage, config, dialogLoginPage);
	        } else {
	            mainWindow(options, loginPage, sbtUrl);
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
			dom.byId('wrongCredsMessage').innerHTML = loginForm.wrong_creds_message;
			dom.byId('basicLoginFormUsername').innerHTML = loginForm.username;
			dom.byId('basicLoginFormPassword').innerHTML = loginForm.password;
			dom.byId('basicLoginFormOK').value = loginForm.login_ok;
			dom.byId('basicLoginFormCancel').value = loginForm.login_cancel;
		});
		return true;
	},
	
	_authPopup: function(options, loginPage, sbtConfig, sbtUrl, loginPage) {
	    require(["sbt/i18n!sbt/nls/loginForm"], function(loginForm) {
            if(options.callback){
                sbtConfig.callback = options.callback;
            }
            if(options.cancel){
                sbtConfig.cancel = options.cancel;
            }
            globalLoginFormStrings = loginForm;
            
            var proxy = options.proxy.proxyUrl;
            var actionURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
            var url = sbtUrl+loginPage+'?actionURL='+encodeURIComponent(actionURL)
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
        });
        
        return true;
	},
	
	_authMainWindow: function(options, loginPage, sbtUrl) {
		var proxy = options.proxy.proxyUrl;
		var actionURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
		//var proxyServletURL='/sbt/proxy/basicAuth/'+options.proxyPath+"/JSApp";
		var url = sbtUrl+loginPage+'?actionURL='+encodeURIComponent(actionURL)
							 +'&redirectURL='+encodeURIComponent(document.URL)
							 +'&loginUi=mainWindow'
							 +'&showWrongCredsMessage=false';
		var newwindow=window.location.href = url;
		return true;
	}

}
);
});
