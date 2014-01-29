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
 * 
 * Definition of a dojo based dialog for OAuth 1.0.
 */
define(['dijit/Dialog', "dojo/cache", "sbt/config", "dojo/on", "sbt/dom"], function(Dialog, cache, config, on, dom) {
	return {
		show: function(options, dialogLoginPage, ssoStrings) {
		  try{
			var d = new Dialog({
				title: ssoStrings.message_title,
	            content: cache("sbt", dialogLoginPage),
	            onRelogin : function () {
	            	d.hide();
	            	location.reload();
	            }
	        });
		  }catch(er){
			  console.log("error in SSO_Dialog "+er);
		  }	
		  config.dialog=d;
		  
          on(dom.byId("ssoLoginFormOK"), "click", function(evt){
              config.dialog.onRelogin();
          });
	      d.show();
		}

	};
});