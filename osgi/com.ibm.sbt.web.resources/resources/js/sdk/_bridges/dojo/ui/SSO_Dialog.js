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

dojo.provide("sbt._bridges.dojo.ui.SSO_Dialog");
/**
 * Social Business Toolkit SDK.
 * 
 * Definition of a dojo based dialog for Basic Auth
 */
define(["sbt/i18n!sbt/authenticator/nls/SSO", "sbt/config"], function(ssoStrings, config) {
	return {
		show: function(options, dialogLoginPage) {
		  try{
			  require(["dijit.Dialog", "dojo.cache"]);
					var d = new dijit.Dialog({
						title: ssoStrings.message_title,
			            content: dojo.cache("sbt", dialogLoginPage),
			            onRelogin : function () {
			            	d.hide();
			            	location.reload();
			            }
			        });
					
		  }catch(er){
			  console.log("error in SSO_Dialog "+er);
		  }	
		  config.dialog=d;
		  
		  dojo.connect(dojo.byId("ssoLoginFormOK"), "onclick", function(evt){
              config.dialog.onRelogin();
          });
	      d.show();
		}
	};
});