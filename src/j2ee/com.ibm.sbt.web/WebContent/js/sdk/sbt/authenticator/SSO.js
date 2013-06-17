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
define(["../declare", "../lang", "../i18n!sbt/authenticator/nls/SSO"],function(declare, lang, ssoMessage) {
/**
 * Proxy SSO authentication.
 * 
 * This class triggers the authentication for a service.
 */
return declare(null, {
	message:		"sbt/authenticator/templates/Message.html",
    url: "",
    
    constructor: function(args){
        lang.mixin(this, args || {});
    },
	
    authenticate: function(options) {
		globalSSOMessageStrings = ssoMessage;
		var popUpURL =  this.message;
		var url = this.url+popUpURL;
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
	}
}
);
});