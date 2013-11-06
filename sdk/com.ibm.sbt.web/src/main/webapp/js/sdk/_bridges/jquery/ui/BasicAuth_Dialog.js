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
 * 
 * Definition of a jQuery UI based dialog for OAuth 1.0.
 */
define(["sbt/config", 'jquery/ui'], function(config) {
	
	return {
		submitOnClickHandle: function(contentForm, options) {
			var o = {};
			jQuery.each(contentForm, function() {
				if (o[this.name]) {
					if (!o[this.name].push) {
						o[this.name] = [o[this.name]];
					}
					o[this.name].push(this.value || '');
				} else {
					o[this.name] = this.value || '';
				}
			});
			contentForm = o;
			if(contentForm.username == "" || contentForm.password == ""){
				jQuery("#wrongCredsMessage").css("display","block");
				return;
			}else{
				var proxy = options.proxy.proxyUrl;
				var proxyServletURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
	    		var postToProxy = {
					url: proxyServletURL, 
					content: contentForm,
					headers: {
						"Content-Type": "application/x-www-form-urlencoded"
					},
					handle: function(data){
						if(data instanceof Error){
							var statusCode = data.code; // In the transport we already set the statusCode to error.code
							if(statusCode == 401 || statusCode == 403){
								jQuery("#wrongCredsMessage").css("display","block");
								return;
							};
						}
						config.dialog.dialog("close");
						config.dialog.dialog("destroy");
						options.callback();
				    }
	    		};
			    options.transport.xhr("POST",postToProxy,true);
			};
	    },
		show: function(options, dialogLoginPage) {
		  try{
			var self = this;
			require(['sbt/config', 'requirejs/text!sbt/'+dialogLoginPage],function(config, loginPage){
				var _title = "Authentication";
				var _isModal = false;
				var _width = 350;
				var _height = 220;
				var _autoOpen = true;
				var _resizeable = true;
				var _draggable = true;
				
	            var d = config.dialog = jQuery(loginPage).dialog({
					title : _title,
					modal: _isModal,
					width: _width,
					height: _height,
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
	            					self.submitOnClickHandle(jQuery("form").serializeArray(), options);
	            				}
	            			);
	            		}
	            		if (text == "Cancel"){
	            			thisButton.click(
	            				function() {
	            					config.dialog.dialog("close");
	        						config.dialog.dialog("destroy");
	            				}
	            			);
	            		}
	            		return thisButton;
	            });
			});
		  }catch(er){
			  console.log("error in BasicAuth_Dialog "+er);
		  }	
		}
	};
});