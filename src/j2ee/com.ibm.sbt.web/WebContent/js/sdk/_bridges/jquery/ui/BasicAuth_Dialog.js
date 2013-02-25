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
define(['jquery/ui', 'jquery/serialize'], function() {
	
	return {
		submitOnClickHandle: function(contentForm) {
			var o = {};
			$.each(contentForm, function() {
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
				$("#wrongCredsMessage").css("display","block");
				return;
			}else{
				var proxy = options.proxy.proxyUrl;
				var proxyServletURL = proxy.substring(0,proxy.lastIndexOf("/"))+"/basicAuth/"+options.proxyPath+"/JSApp";
	    		var postToProxy = {
					url: proxyServletURL, 
					content: contentForm,
					handle: function(data){
						if(data instanceof Error){
							var statusCode = data.code; // In the transport we already set the statusCode to error.code
							if(statusCode == 401 || statusCode == 403){
								$("#wrongCredsMessage").css("display","block");
								return;
							};
						}
						sbt.dialog.dialog("close");
						sbt.dialog.dialog("destroy");
						options.callback();
				    }
	    		};
			    options.transport.xhr("POST",postToProxy,true);
			};
	    },
		show: function(options, dialogLoginPage) {
		  try{
			var self = this;
			require(['requirejs/text!sbt/'+dialogLoginPage],function(loginPage){
				var _title = "Authentication";
				var _isModal = false;
				var _width = 350;
				var _height = 220;
				var _autoOpen = true;
				var _resizeable = true;
				var _draggable = true;
				
	            var d = sbt.dialog = $(loginPage).dialog({
					title : _title,
					modal: _isModal,
					width: _width,
					height: _height,
					resizeable: _resizeable,
					draggable: _draggable,
					autoOpen : _autoOpen
				});
	            var attrsArray = [];
	            $("div[role=dialog]").find("input[type=button]").each(function(index){
	            	var attrs={};
	            	$.each($(this)[0].attributes,function(idx, attr){
	            		attrs[attr.nodeName] = attr.nodeValue;
	            	});
	            	attrsArray[index] = attrs;
	            });
	            $("div[role=dialog]").find("input[type=button]").replaceWith(function(idx){
	            		var text = attrsArray[idx].value;
	            		delete attrsArray[idx].value;
	            		delete attrsArray[idx].onclick;
	            		var thisButton = $("<button />", attrsArray[idx]).append($(this).contents()).
            				attr("role","button").attr("aria-disabled", "false").
            				addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only").
            				append("<span class='ui-button-text'>"+text+"</span>");
	            		thisButton.hover(
	            			function() {
	            				$(this).addClass("ui-state-hover");
	            			},
	            			function() {
	            				$(this).removeClass("ui-state-hover");
	            			}
	            		);
	            		if (text == "OK"){
	            			thisButton.click(
	            				function() {
	            					self.submitOnClickHandle($("form").serializeArray());
	            				}
	            			);
	            		}
	            		if (text == "Cancel"){
	            			thisButton.click(
	            				function() {
	            					sbt.dialog.dialog("close");
	        						sbt.dialog.dialog("destroy");
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