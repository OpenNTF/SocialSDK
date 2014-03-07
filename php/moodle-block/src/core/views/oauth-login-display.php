
<div id="grantAccessDiv">
	<div class="alert alert-error" id="grantAccessErrorDiv" style="display: none;"></div>
	<div id="desc">You have not yet granted access for this application to use your IBM account.</div>
	<button class="btn btn-primary" id="grantAccessBtn" onclick="grantAccess()">Grant Access</button>
</div>

<script type="text/javascript">
	function grantAccess() {
		<?php 
			if (!isset($_SERVER['HTTPS']) || !$_SERVER['HTTPS']) {
				echo "alert('The IBM Connect cookie policy requires that you use HTTPS to perform this action. Please try again by accessing this webpage using the HTTPS protocol.');";
				echo "return;";
			} else {
				echo "	var now = new Date();
						var time = now.getTime();
						time += 60000;
						now.setTime(time);
						document.cookie = 'IBMSBTKOAuthLogin=yes;expires=' + now.toGMTString();
					document.cookie = 'IBMSBTKOAuthOrigin=' + window.location + ';expires=' + now.toGMTString();
						location.reload();";
			}
		?>
	
	}
	
</script>


		
