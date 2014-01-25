***REMOVED*** 

if (isset($viewData['options'])) {
	
	// Print contents of all endpoints
	foreach ($viewData['options'] as $val) {
	
		$endpoint = (array)json_decode($val, true);
		$endpoint_name = $endpoint['name'];

		$id = str_replace(" ", "_", $endpoint_name);
		print '<input type="hidden" value="' . $endpoint_name . '" id="' . $id . '_name"/>';
		print '<input type="hidden" value="' . $endpoint['url'] . '" id="' . $id . '_endpoint_url"/>';
		print '<input type="hidden" value="' . $endpoint['consumer_key'] . '" id="' . $id . '_consumer_key"/>';
		print '<input type="hidden" value="' . $endpoint['consumer_secret'] . '" id="' . $id . '_consumer_secret"/>';
		print '<input type="hidden" value="' . $endpoint['authorization_url'] . '" id="' . $id . '_authorization_url"/>';
		print '<input type="hidden" value="' . $endpoint['access_token_url'] . '" id="' . $id . '_access_token_url"/>';
		print '<input type="hidden" value="' . $endpoint['request_token_url'] . '" id="' . $id . '_request_token_url"/>';
		print '<input type="hidden" value="' . $endpoint['authentication_method'] . '" id="' . $id . '_authentication_method"/>';
		print '<input type="hidden" value="' . $endpoint['basic_auth_username'] . '" id="' . $id . '_basic_auth_username"/>';
		print '<input type="hidden" value="' . $endpoint['basic_auth_password'] . '" id="' . $id . '_basic_auth_password"/>';
	}

	// Print default settings
	// Import default settings
	require_once BASE_PATH . '/config.php' ;
	print '<input type="hidden" value="' . $config['wp_endpoint_2_name'] . '" id="default_smartcloud_endpoint_name" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_2_url'] . '" id="default_smartcloud_endpoint_url" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_2_consumer_key'] . '" id="default_smartcloud_consumer_key" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_2_consumer_secret'] . '" id="default_smartcloud_consumer_secret" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_2_authorization_url'] . '" id="default_smartcloud_authorization_url" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_2_access_token_url'] . '" id="default_smartcloud_access_token_url" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_2_request_token_url'] . '" id="default_smartcloud_request_token_url" />';
	
	print '<input type="hidden" value="' . $config['wp_endpoint_1_name'] . '" id=""default_connections_name" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_1_url'] . '" id=""default_connections_endpoint_url" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_1_consumer_key'] . '" id=""default_connections_consumer_key" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_1_consumer_secret'] . '" id=""default_connections_consumer_secret" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_1_authorization_url'] . '" id=""default_connections_authorization_url" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_1_access_token_url'] . '" id=""default_connections_access_token_url" />';
	print '<input type="hidden" value="' . $config['wp_endpoint_1_request_token_url'] . '" id=""default_connections_request_token_url" />';
	

	print '<input type="hidden" value="' . plugins_url('sbtk-wp') . '/system/libs/js-sdk" id="default_sdk_deploy_url" />';

	print '<input type="hidden" value="no" name="delete_endpoint" id="delete_endpoint"/>';

	// Print Javascript
	print '<script type="text/javascript">';
	print '//<![CDATA[';
	require_once BASE_PATH . '/views/js/option.js';
	print '//]]>';
	print '</script>';
}
?>