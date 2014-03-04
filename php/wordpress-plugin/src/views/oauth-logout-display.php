
<div id="ibm_sbtk_login_template" style="display: none">
***REMOVED*** 
	require BASE_PATH . '/core/views/oauth-login-display.php';
?>
</div>
      	<div>
			<button class="btn btn-primary" name="logoutBtn">Logout</button>
		</div>
		
		<script type="text/javascript">
		require(["sbt/dom", "sbt/config"],
				function(dom, config) {
			var endpoint = config.findEndpoint("connections");

			endpoint.isAuthenticationValid({	
				"forceAuthentication": true, 
				"actionUrl": "***REMOVED*** echo plugins_url(PLUGIN_NAME); ?>/core/index.php?classpath=services&class=Proxy&method=route&isAuthenticated=true"}).then(
					function(response) {
						logout(dom, config);
					}, function(response) {
						logout(dom, config);
				});
		});
			function logout(dom, config) {
        		var logoutBtn = document.getElementsByName("logoutBtn");
        		for (var i = 0; i < logoutBtn.length; i++) {
        			logoutBtn[i].onclick = function(evt) {	
        				var endpoint = config.findEndpoint("connections");
        				endpoint.logout({"actionUrl": "***REMOVED*** echo plugins_url(PLUGIN_NAME); ?>/core/index.php?classpath=services&class=Proxy&method=route&OAuthLogout=true"}).then(
        						function(response) {
        							var widgets = document.getElementsByName("ibm_sbtk_widget");
        							var loginTemplate = document.getElementById("ibm_sbtk_login_template");
        							for (var i = 0; i < widgets.length; i++) {
        								widgets[i].innerHTML = loginTemplate.innerHTML;
        							}
        						},
        						function(error) {
            						console.log(error);
        						}
        					);
					};
				}
			}
		</script>