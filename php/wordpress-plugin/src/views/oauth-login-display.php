
<div id="grantAccessDiv">
	<div class="alert alert-error" id="grantAccessErrorDiv" style="display: none;"></div>
	<div id="desc">You have not yet granted access for this application.</div>
	<button class="btn btn-primary" id="grantAccessBtn" onclick="grantAccess()">Grant Access</button>
</div>

<script type="text/javascript">
	function grantAccess() {
		var now = new Date();
		var time = now.getTime();
		time += 60000;
		now.setTime(time);
		document.cookie = 
		    'IBMSBTKOAuthLogin=yes;expires=' + now.toGMTString();
		location.reload();
	}
	
</script>


		
