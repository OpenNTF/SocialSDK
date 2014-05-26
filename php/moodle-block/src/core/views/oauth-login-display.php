
<div id="grantAccessDiv">
	<div class="alert alert-error" id="grantAccessErrorDiv" style="display: none;"></div>
	<div id="desc"><?php echo get_string('grant_access_message', 'block_ibmsbt'); ?></div>
	<button class="btn btn-primary" id="grantAccessBtn" onclick="grantAccess()"><?php echo get_string('grant_access', 'block_ibmsbt'); ?></button>
</div>

<script type="text/javascript">
	function grantAccess() {
		<?php 
			if (!isset($_SERVER['HTTPS']) || !$_SERVER['HTTPS']) {
				echo "alert('" . get_string('cookie_policy', 'block_ibmsbt') . "');";
				echo "return;";
			} else {
				echo "	var now = new Date();
						var time = now.getTime();
						time += 60000;
						now.setTime(time);
						document.cookie = 'IBMSBTKOAuthLogin=yes;expires=' + now.toGMTString();
						location.reload();";
			}
		?>
	
	}
	
</script>


		
