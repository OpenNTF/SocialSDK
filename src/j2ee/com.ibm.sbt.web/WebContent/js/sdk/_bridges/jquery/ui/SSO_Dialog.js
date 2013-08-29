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
 * Definition of a jQuery UI based dialog for OAuth 1.0.
 */
define(["sbt/config", 'jquery/ui'], function(config) {
	
	return {
		show: function(options, dialogLoginPage, ssoStrings) {
		  try{
			var self = this;
			require(['sbt/config', 'requirejs/text!sbt/'+dialogLoginPage],function(config, loginPage){
				var _title = ssoStrings.message_title;
				var _isModal = false;
				var _autoOpen = true;
				var _resizeable = true;
				var _draggable = true;
				
	            var d = config.dialog = jQuery(loginPage).dialog({
					title : _title,
					modal: _isModal,
					resizeable: _resizeable,
					draggable: _draggable,
					autoOpen : _autoOpen
				});
	            var attrsArray = [];
	            jQuery("div[role=dialog]").find("input[type=button]").each(function(index){
	            	var attrs={};
	            	jQuery.each(jQuery(this)[0].attributes,function(idx, attr){
	            		attrs[attr.nodeName] = attr.nodeValue;
	            	});
	            	attrsArray[index] = attrs;
	            });
	            jQuery("div[role=dialog]").find("input[type=button]").replaceWith(function(idx){
	            		var text = attrsArray[idx].value;
	            		delete attrsArray[idx].value;
	            		delete attrsArray[idx].onclick;
	            		var thisButton = jQuery("<button />", attrsArray[idx]).append(jQuery(this).contents()).
            				attr("role","button").attr("aria-disabled", "false").
            				addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only").
            				append("<span class='ui-button-text'>"+text+"</span>");
	            		thisButton.hover(
	            			function() {
	            				jQuery(this).addClass("ui-state-hover");
	            			},
	            			function() {
	            				jQuery(this).removeClass("ui-state-hover");
	            			}
	            		);
	            		if (text == "OK"){
	            			thisButton.click(
	            				function() {
	            					config.dialog.dialog("close");
	        						config.dialog.dialog("destroy");
	        						location.reload();
	            				}
	            			);
	            		}
	            		return thisButton;
	            });
			});
		  }catch(er){
			  console.log("error in SSO_Dialog "+er);
		  }	
		}
	};
});