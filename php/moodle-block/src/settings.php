***REMOVED*** 
/**
 * (C) Copyright IBM Corp. 2012
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

/**
 * @author Benjamin Jakobus
 */

require_once 'core/autoload.php';

$settings->add(new admin_setting_heading(
		'general',
		'General Configuration',
		''
));

$settings->add(new admin_setting_configtext('sdk_deploy_url', 'SDK Deploy URL:', '', BASE_LOCATION . 'system/libs/js-sdk', PARAM_RAW));

$options = array('basic' => 'Basic', 'oauth1' => 'OAuth 1.0');
$settings->add(new admin_setting_configselect('auth_type', 'Authentication Type:',
		'', 'basic', $options));

$settings->add(new admin_setting_heading(
		'basicAuthHeader',
		'<span id="ibm-sbtk-basic-auth-admin-section">Basic Authentication</span>',
		''
));

$settings->add(new admin_setting_configtext('server_url', 'Server URL:', '', 'https://qs.renovations.com:444', PARAM_RAW));
$settings->add(new admin_setting_configtext('basic_auth_username', 'Username:', '', 'fadams', PARAM_RAW));
$settings->add(new admin_setting_configtext('basic_auth_password', 'Password:', '', 'passw0rd', PARAM_RAW));
$basicAuthOptions = array('global' => 'Global user credentials', 'prompt' => 'Prompt for user credentials', 'profile' => 'Profile integration');
$settings->add(new admin_setting_configselect('basic_auth_method', 'Authentication method:', '', 'prompt', $basicAuthOptions));

$settings->add(new admin_setting_heading(
		'oauthHeader',
		'<span id="ibm-sbtk-oauth-admin-section">OAuth 1.0</span>',
		''
));
$settings->add(new admin_setting_configtext('o_auth_server_url', 'Server URL:', '', 'https://apps.na.collabserv.com', PARAM_RAW));
$settings->add(new admin_setting_configtext('consumer_key', 'Consumer Key:', '', '***REMOVED***', PARAM_RAW));
$settings->add(new admin_setting_configtext('consumer_secret', 'Consumer Secret:', '', 'd46e0237d5de41feb4b446089a0575f3', PARAM_RAW));
$settings->add(new admin_setting_configtext('request_token_url', 'Request Token URL:', '', 'https://apps.na.collabserv.com/manage/oauth/getRequestToken', PARAM_RAW));
$settings->add(new admin_setting_configtext('authorization_url', 'Authorization URL:', '', 'https://apps.na.collabserv.com/manage/oauth/authorizeToken', PARAM_RAW));
$settings->add(new admin_setting_configtext('access_token_url', 'Access Token URL:', '', 'https://apps.na.collabserv.com/manage/oauth/getAccessToken', PARAM_RAW));

?>