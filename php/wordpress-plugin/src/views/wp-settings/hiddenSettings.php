<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/themes/smoothness/jquery-ui.css" />
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/jquery-ui.min.js"></script>
<script type="text/javascript">
	var strConnectionsEndpointURL = '<?php echo $GLOBALS[LANG]['connections_server_url']?>';
	var strSmartcloudEndpointURL = '<?php echo $GLOBALS[LANG]['smartcloud_server_url']?>';
	var strSSLTrustError = '<?php echo $GLOBALS[LANG]['ssl_trust_error']?>';
	var strEndpointNameError = '<?php echo $GLOBALS[LANG]['endpoint_name_error']?>';

	function ibm_sbt_generate_callback_url() {
		var endpointName = document.getElementById('new_endpoint_name').value;
		if (endpointName == false) {
			alert('You must specify an endpoint name before the callback URL can be generated');
			return;
		}
		var url = "<?php echo BASE_LOCATION . '/core/index.php?classpath=endpoint&class=SBTOAuth2Endpoint&method=authenticationCallback&endpointName='?>";
		url += endpointName;
		document.getElementById('new_callback_url').value = url;
	}
</script>
<?php 
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
		print '<input type="hidden" value="' . $endpoint['endpoint_version'] . '" id="' . $id . '_endpoint_version"/>';
		print '<input type="hidden" value="' . $endpoint['allow_client_access'] . '" id="' . $id . 'allow_client_access"/>';
		print '<input type="hidden" value="' . $endpoint['force_ssl_trust'] . '" id="' . $id . 'force_ssl_trust"/>';
	}
}
print '<input type="hidden" value="' . plugins_url(PLUGIN_NAME) . '/system/libs/js-sdk" id="default_sdk_deploy_url" />';
print '<input type="hidden" value="no" name="delete_endpoint" id="delete_endpoint"/>';
print '<input type="hidden" value="no" name="new_endpoint" id="new_endpoint"/>';
print '<script type="text/javascript" src="' . plugins_url(PLUGIN_NAME) . '/views/js/option.js"></script>';
print '<script type="text/javascript" src="' . plugins_url(PLUGIN_NAME) . '/views/js/endpointCreationDialog.js"></script>';
?>
<div id="dialog" title="<?php echo $GLOBALS[LANG]['create_new_endpoint'];?>" style="column-width:300px;">
	<table>
		<tr>
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['server_type'];?>
			</td>
			<td>
				<select onchange="ibm_sbt_new_server_type_change();" id="new_server_type" name="new_server_type">
					<option value="choose"><?php echo $GLOBALS[LANG]['choose']; ?></option>
					<option value="connections"><?php echo $GLOBALS[LANG]['ibm_connections']; ?></option>
					<option value="smartcloud"><?php echo $GLOBALS[LANG]['ibm_smartcloud']; ?></option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['auth_method'];?>
			</td>
			<td>
				<select id="new_authentication_method" name="new_authentication_method" onchange="ibm_sbt_change_new_authentication_method();">
					<option value="choose"><?php echo $GLOBALS[LANG]['choose']; ?></option>
					<option id="new_oauth1" value="oauth1">OAuth 1.0</option>
					<option value="oauth2">OAuth 2.0</option>
					<option value="basic">Basic</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['endpoint_name'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_endpoint_name" name="new_endpoint_name" value="" />
			</td>
		</tr>
		<tr>
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['endpoint_version'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_endpoint_version" name="new_endpoint_version" value="" />(<?php echo $GLOBALS[LANG]['optional'];?>)
			</td>
		</tr>
		<tr style="display: none;" id="tr_force_ssl_trust">
			<td style="width: 200px;">
				<input type="checkbox" checked="checked" id="new_force_ssl_trust" name="new_force_ssl_trust" value="force_ssl_trust" />&nbsp;<?php echo $GLOBALS[LANG]['force_ssl_trust'];?>
			</td>
			<td style="width: 200px;">
				<input type="checkbox" checked="checked" id="new_allow_client_access" name="new_allow_client_access" value="allow_client_access" />&nbsp;<?php echo $GLOBALS[LANG]['allow_client_access'];?>
			</td>
		</tr>
		<tr>
			<td style="width: 200px;" id="lb_endpoint_url">
				<?php echo $GLOBALS[LANG]['endpoint_url'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_endpoint_url" name="new_endpoint_url" value="https://[my-server]" />
			</td>
		</tr>
	</table>
	<table>
		<tr style="display: none;" id="tr_new_consumer_key">
			<td style="width: 200px;" id="lb_new_consumer_key">
				<?php echo $GLOBALS[LANG]['consumer_key'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_consumer_key" name="new_consumer_key" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_consumer_secret">
			<td style="width: 200px;" id="lb_new_consumer_secret">
				<?php echo $GLOBALS[LANG]['consumer_secret'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_consumer_secret" name="new_consumer_secret" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_authorization_url">
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['authorization_url'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_authorization_url" name="new_authorization_url" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_request_token_url">
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['request_token_url'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_request_token_url" name="new_request_token_url" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_access_token_url">
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['access_token_url'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_access_token_url" name="new_access_token_url" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_basic_auth_method">
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['basic_auth_method'];?>
			</td>
			<td>
				<select id="new_basic_auth_method" name="new_basic_auth_method" onchange="ibm_sbt_change_new_basic_auth_method();">
					<option value="choose"><?php echo $GLOBALS[LANG]['choose']; ?></option>
					<option value="global"><?php echo $GLOBALS[LANG]['global_user_creds']; ?></option>
		    		<option value="prompt"><?php echo $GLOBALS[LANG]['prompt_user_creds']; ?></option>
				</select>
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_callback_url">
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['callback_url'];?>
			</td>
			<td>
				<input disabled="disabled" size="50" type="text" id="new_callback_url" name="new_callback_url" value="" />
				<button onclick="ibm_sbt_generate_callback_url();">Generate</button>
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_basic_auth_username">
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['username'];?>
			</td>
			<td>
				<input size="50" type="text" id="new_basic_auth_username" name="new_basic_auth_username" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_basic_auth_password">
			<td style="width: 200px;">
				<?php echo $GLOBALS[LANG]['password'];?>
			</td>
			<td>
				<input size="50" type="password" id="new_basic_auth_password" name="new_basic_auth_password" value="" />
			</td>
		</tr>
	</table>
	<button disabled="disabled" onclick="ibm_sbt_save_new_endpoint();" id="new_endpoint_save"><?php echo $GLOBALS[LANG]['save'];?></button> 
	<button onclick="ibm_sbt_cancel_new_endpoint();" id="new_endpoint_cancel"><?php echo $GLOBALS[LANG]['cancel'];?></button>
</div>
