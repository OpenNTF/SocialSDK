<script type="text/javascript">
function ibm_sbt_endpoint_logout() {
	require([ "sbt/dom", "sbt/config" ],	
		function(dom, config) {
			var endpoint = config.findEndpoint("<?php echo $this->config->endpoint; ?>");
			endpoint.logout({"actionUrl": "<?php echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=route&endpointName=<?php global $USER; echo $this->config->endpoint; ?>&uid=<?php echo $USER->id; ?>&basicAuthLogout=true&OAuthLogout=yes"}).then(
					function(response) {
						location.reload();
					},
					function(error) {
						console.log(error);
					}
				);
		}
	);
}
</script>