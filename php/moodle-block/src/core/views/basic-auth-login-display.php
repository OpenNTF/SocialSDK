	<style type="text/css">
	
		#ibmsbt\.loginActionForm {
			padding: 10px;
		}
  		#ibmsbt\.loginActionForm table {
    		z-index:5000; 
    		-webkit-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5); 
    		-webkit-border-radius: 6px; 
    		-moz-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5); 
    		background:#eee; 
    		-moz-border-radius: 5px; 
    		color: black;
    	}
    	
    	#ibmsbt\.loginActionForm table td {
    		padding: 10px;
    		border-style: none;
    	}
  	</style>
	
	<div name="grantAccessDiv" style="display: none;">
        	<div class="alert alert-error" id="grantAccessErrorDiv" style="display: none;"></div>
			<div id="desc">You have not yet granted access for this application to use your IBM Connections account.</div>
	    	<button class="btn btn-primary" name="grantAccessBtn">Grant Access</button>
		</div>
		
		<div name="checkAccessDiv">
			<div id="desc">Checking if you have granted access for this application to use your IBM Connections account.</div>
		</div>
	
        <div name="accessGrantedDiv" style="display: none;">
			<button class="btn btn-primary" name="logoutBtn">Logout</button>
		</div>
		
		<script type="text/javascript">
		require([ "sbt/dom", "sbt/config" ],
			
			function(dom, config) {
				var endpoint = config.findEndpoint("connections");

				endpoint.isAuthenticationValid({	
					"forceAuthentication": true, 
					"actionUrl": "***REMOVED*** echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=route&uid=***REMOVED*** global $USER; echo $USER->id?>&isAuthenticated=true"}).then(
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
				var endpoint = config.findEndpoint("connections");
				config.Properties["loginUi"] = "dialog";

				***REMOVED*** 
					if (!isset($_SERVER['HTTPS']) || !$_SERVER['HTTPS']) {
						echo "alert('The IBM Connect cookie policy requires that you use HTTPS to perform this action. Please try again by accessing this webpage using the HTTPS protocol.');";
						echo "return;";
					}
				?>

				endpoint.authenticate({"forceAuthentication": true, 
					"actionUrl": "***REMOVED*** echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=route&uid=***REMOVED*** global $USER; echo $USER->id?>&isAuthenticated=true"}).then(
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
        				var endpoint = config.findEndpoint("connections");
        				endpoint.logout({"actionUrl": "***REMOVED*** echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=route&uid=***REMOVED*** global $USER; echo $USER->id?>&basicAuthLogout=true"}).then(
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