
      	<div>
			<button class="btn btn-primary" name="logoutBtn">Logout</button>
		</div>
		
		<script type="text/javascript">
		require(["sbt/dom", "sbt/config"],
			function(dom, config) {
				var endpoint = config.findEndpoint("connections");

				endpoint.isAuthenticationValid({	
					"forceAuthentication": true, 
					"actionUrl": "<?php echo plugins_url(PLUGIN_NAME); ?>/core/index.php?classpath=services&class=Proxy&method=route&isAuthenticated=true"}).then(
						function(response) {
							if (response.result) {
								logout(dom, config);
							} 
 					}, function(error) {
 						console.log(error);
 				});
			});

			function logout(dom, config) {
        		var logoutBtn = document.getElementsByName("logoutBtn");
        		for (var i = 0; i < logoutBtn.length; i++) {
        			logoutBtn[i].onclick = function(evt) {	
        				var endpoint = config.findEndpoint("connections");
        				endpoint.logout({"actionUrl": "<?php echo plugins_url(PLUGIN_NAME); ?>/core/index.php?classpath=services&class=Proxy&method=route&OAuthLogout=true"}).then(
        						function(response) {
        							window.location.reload();
        						},
        						function(error) {
            						console.log(error);
        						}
        					);
					};
				}
			}
		</script>