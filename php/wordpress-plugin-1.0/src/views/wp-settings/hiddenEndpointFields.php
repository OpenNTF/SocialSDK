<input type="hidden" id="access_token_url" name="access_token_url" value="" />
<input type="hidden" id="authorization_url" name="authorization_url" value="" />
<input type="hidden" id="basic_auth_method" name="basic_auth_method" value="<?php echo $viewData['basic_auth_method'];?>" />
<input type="hidden" id="basic_auth_password" name="basic_auth_password" value="" />
<input type="hidden" id="basic_auth_username" name="basic_auth_username" value="" />
<input type="hidden" id="consumer_key" name="consumer_key" value="" />
<input type="hidden" id="consumer_secret" name="consumer_secret" value="" />
<input type="hidden" id="endpoint_name" name="endpoint_name" value="<?php echo $viewData['endpointName']; ?>" />
<input type="hidden" id="endpoint_url" name="endpoint_url" value="" />
<input type="hidden" id="request_token_url" name="request_token_url" value="" />
<input type="hidden" id="server_type" name="server_type" value="<?php echo $viewData['server_type'];?>" />
<input type="hidden" id="callback_url" name="callback_url" value="<?php echo $viewData['callback_url'];?>" />
<input type="hidden" id="endpoint_version" name="endpoint_version" value="<?php echo $viewData['endpoint_version'];?>" />
<input type="hidden" id="authentication_method" name="authentication_method" value="<?php echo $viewData['authentication_method']; ?>" />
<input style="display: none;" type="checkbox" id="force_ssl_trust" name="force_ssl_trust" <?php echo (isset($viewData['force_ssl_trust']) && $viewData['force_ssl_trust'] == true ? 'checked="checked"' : "")?> value="force_ssl_trust" />
<input style="display: none;" type="checkbox" id="allow_client_access" name="allow_client_access" <?php echo (isset($viewData['allow_client_access']) && $viewData['allow_client_access'] == true ? 'checked="checked"' : "")?> value="allow_client_access" />