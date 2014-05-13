	<style type="text/css">
		#ibmsbt\.loginActionForm {
			padding: 10px;
		}
  		#ibmsbt\.loginActionForm table {
  			background:white;
    		z-index:6001; 
    		-moz-box-shadow: 1px 0px 10px rgba(0, 0, 0, 0.7); 
    		-moz-border-radius: 6px; 
    		-webkit-border-radius: 6px; 
    		-webkit-box-shadow: 1px 0px 10px rgba(0, 0, 0, 0.7); 
    		color: black;
    	}
    	
    	#ibmsbt\.loginActionForm table td {
    		padding: 10px;
    		border-style: none;
    	}
  	</style>
	
	<div name="grantAccessDiv" style="display: none;">
        	<div class="alert alert-error" id="grantAccessErrorDiv" style="display: none;"></div>
			<div id="desc"><?php echo get_string('grant_access_message', 'block_ibmsbt'); ?></div>
	    	<button class="btn btn-primary" name="grantAccessBtn"><?php echo get_string('grant_access', 'block_ibmsbt'); ?></button>
		</div>
		
		<div name="checkAccessDiv">
			<div id="desc"><?php echo get_string('check_grant_access_message', 'block_ibmsbt'); ?></div>
		</div>
	
        <div name="accessGrantedDiv" style="display: none;">
			<button class="btn btn-primary" name="logoutBtn"><?php echo get_string('logout', 'block_ibmsbt'); ?></button>
		</div>
		
		<script type="text/javascript">
		require([ "sbt/dom", "sbt/config" ],
			
			function(dom, config) {
				var endpoint = config.findEndpoint("<?php echo $this->config->endpoint; ?>");

				endpoint.isAuthenticationValid({	
					"forceAuthentication": true, 
					"actionUrl": "<?php echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=route&uid=<?php global $USER; echo $USER->id?>&isAuthenticated=true"}).then(
						function(response) {
							if (response.result) {
								setWidgetsDisplay("block");
								displayAccessGranted(dom, config);
							} else {
								displayGrantAccess(dom, config);
							}
 					}, function() {
 						displayGrantAccess(dom, config);
 				});
			});
		
			function grantAccess(dom, config) {
				var endpoint = config.findEndpoint("<?php echo $this->config->endpoint; ?>");
				config.Properties["loginUi"] = "dialog";

				<?php 
					if (!isset($_SERVER['HTTPS']) || !$_SERVER['HTTPS']) {
						echo "alert('" . get_string('cookie_policy', 'block_ibmsbt') . "');";
						echo "return;";
					}
				?>

				endpoint.authenticate({"forceAuthentication": true, 
					"actionUrl": "<?php echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=route&uid=<?php global $USER; echo $USER->id?>&isAuthenticated=true"}).then(
					function(response) {
						location.reload();
					},
					function(error) {
						setWidgetsDisplay("none");
						displayGrantAccess(dom, config, error);
						console.log(error);
					}
				);
			}

			function displayAccessGranted(dom, config) {

				var checkAccessDiv = document.getElementsByName("checkAccessDiv");
        		for (var i = 0; i < checkAccessDiv.length; i++) {
        			checkAccessDiv[i].style.display = "none";
				}
				
        		var grantAccessDiv = document.getElementsByName("grantAccessDiv");
        		for (var i = 0; i < grantAccessDiv.length; i++) {
        			grantAccessDiv[i].style.display = "none";
				}
				var accessGrantedDiv = document.getElementsByName("accessGrantedDiv");
        		for (var i = 0; i < accessGrantedDiv.length; i++) {
        			accessGrantedDiv[i].style.display = "";
				}

        		var logoutBtn = document.getElementsByName("logoutBtn");
     
        		for (var i = 0; i < logoutBtn.length; i++) {
  
        			logoutBtn[i].onclick = function(evt) {	
        				var endpoint = config.findEndpoint("<?php echo $this->config->endpoint; ?>");
        				endpoint.logout({"actionUrl": "<?php echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=route&uid=<?php global $USER; echo $USER->id?>&basicAuthLogout=true"}).then(
        						function(response) {
        							displayGrantAccess(dom, config);
        							var logoutBtn = document.getElementsByName("logoutBtn");
        							for (var j = 0; j < logoutBtn.length; j++) {
        								logoutBtn[j].style.display = "none";
        							}
        						},
        						function(error) {
            						console.log(error);
        						}
        					);
					};
				}
			}
									
			function setWidgetsDisplay(display) {
				var els = document.getElementsByClassName("ibmsbtk-widget");
        		for (var i = 0; i < els.length; i++) {
					els[i].style.display = display;
				}
			}
		
			function displayGrantAccess(dom, config, error) {
				setWidgetsDisplay("none");

				var checkAccessDiv = document.getElementsByName("checkAccessDiv");
        		for (var i = 0; i < checkAccessDiv.length; i++) {
        			checkAccessDiv[i].style.display = "none";
				}
				
        		var grantAccessDiv = document.getElementsByName("grantAccessDiv");
        		for (var i = 0; i < grantAccessDiv.length; i++) {
        			grantAccessDiv[i].style.display = "";
				}

        		var grantAccessBtn = document.getElementsByName("grantAccessBtn");
        		for (var i = 0; i < grantAccessBtn.length; i++) {
        			grantAccessBtn[i].onclick = function(evt) {
    					grantAccess(dom, config);
    				};
				}
				
        		var grantAccessErrorDiv = document.getElementsByName("grantAccessErrorDiv");
        		for (var i = 0; i < grantAccessErrorDiv.length; i++) {
        			if (error) {
    					grantAccessErrorDiv[i].style.display = "";
    					grantAccessErrorDiv[i].innerHTML = error.message;
    				} else {
    					grantAccessErrorDiv[i].style.display = "none";
    				}
				}
			}
		</script>