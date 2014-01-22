<?php

print '<div id="grantAccessDiv" style="display: none;">
        	<div class="alert alert-error" id="grantAccessErrorDiv" style="display: none;"></div>
			<div id="desc">You have not yet granted access for this application to use your IBM Connections account.</div>
	    	<button class="btn btn-primary" id="grantAccessBtn">Grant Access</button>
		</div>
		
		<div id="checkAccessDiv">
			<div id="desc">Checking if you have granted access for this application to use your IBM Connections account.</div>
		</div>
	
        <div id="accessGrantedDiv" style="display: none;">
			<button class="btn btn-primary" id="logoutBtn">Logout</button>
		</div>
		
		<script type="text/javascript">
		require([ "sbt/dom", "sbt/config" ],
			function(dom, config, FileService) {
				var endpoint = config.findEndpoint("connections");
				endpoint.isAuthenticationValid({"actionUrl": "' . plugins_url('sbtk-wp') . '/index.php?classpath=services&class=Proxy&method=route&isAuthenticated=true"}).then(function(response) {
					if (response.result) {
						setWidgetsDisplay("block");
						var checkAccessDiv = document.getElementById("checkAccessDiv");
						checkAccessDiv.style.display = "none";
						var grantAccessDiv = document.getElementById("grantAccessDiv");
						grantAccessDiv.style.display = "none";
						var grantAccessDiv = document.getElementById("accessGrantedDiv");
						grantAccessDiv.style.display = "";
						
						var logoutBtn = document.getElementById("logoutBtn");
						logoutBtn.onclick = function(evt) {
							endpoint.logout({"actionUrl": "' . plugins_url('sbtk-wp') . '/index.php?classpath=services&class=Proxy&method=route&basicAuthLogout=true"}).then(
								function(response) {
									displayGrantAccess(dom, config);
									logoutBtn.style.display = "none";
								}
							);
						};
					} else {
						displayGrantAccess(dom, config);
					}
				}, function() {
					displayGrantAccess(dom, config);
				});
			});
		
			function grantAccess(dom, config) {
				var endpoint = config.findEndpoint("connections");
				config.Properties["loginUi"] = "dialog";
				endpoint.authenticate().then(
					function(response) {
						displayAccessGranted(dom);
					},
					function(error) {
						setWidgetsDisplay("none");
						displayGrantAccess(dom, config, error);
					}
				);
			}
									
			function setWidgetsDisplay(display) {
				var els = document.getElementsByClassName("ibmsbtk-widget");
        		for (var i = 0; i < els.length; i++) {
					els[i].style.display = display;
				}
			}
		
			function displayGrantAccess(dom, config, error) {
				setWidgetsDisplay("none");
				var checkAccessDiv = document.getElementById("checkAccessDiv");
				checkAccessDiv.style.display = "none";
		
				var grantAccessDiv = document.getElementById("grantAccessDiv");
				grantAccessDiv.style.display = "";
		
				var grantAccessBtn = document.getElementById("grantAccessBtn");
				grantAccessBtn.onclick = function(evt) {
					grantAccess(dom, config);
				};
				
				var grantAccessErrorDiv = document.getElementById("grantAccessErrorDiv");
				if (error) {
					grantAccessErrorDiv.style.display = "";
					grantAccessErrorDiv.innerHTML = error.message;
				} else {
					grantAccessErrorDiv.style.display = "none";
				}
			}
		</script>';