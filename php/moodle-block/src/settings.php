***REMOVED*** 
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
$settings->add(new admin_setting_configtext('basic_auth_username', 'Username:', '', '***REMOVED***', PARAM_RAW));
$settings->add(new admin_setting_configtext('basic_auth_password', 'Password:', '', '***REMOVED***', PARAM_RAW));
$basicAuthOptions = array('global' => 'Global user credentials', 'prompt' => 'Prompt for user credentials', 'profile' => 'Profile integration');
$settings->add(new admin_setting_configselect('basic_auth_method', 'Authentication method:', '', 'prompt', $basicAuthOptions));

$settings->add(new admin_setting_heading(
		'oauthHeader',
		'<span id="ibm-sbtk-oauth-admin-section">OAuth 1.0</span>',
		''
));
$settings->add(new admin_setting_configtext('o_auth_server_url', 'Server URL:', '', 'https://apps.na.collabserv.com', PARAM_RAW));
$settings->add(new admin_setting_configtext('consumer_key', 'Consumer Key:', '', '***REMOVED***', PARAM_RAW));
$settings->add(new admin_setting_configtext('consumer_secret', 'Consumer Secret:', '', '***REMOVED***', PARAM_RAW));
$settings->add(new admin_setting_configtext('request_token_url', 'Request Token URL:', '', 'https://apps.na.collabserv.com/manage/oauth/getRequestToken', PARAM_RAW));
$settings->add(new admin_setting_configtext('authorization_url', 'Authorization URL:', '', 'https://apps.na.collabserv.com/manage/oauth/authorizeToken', PARAM_RAW));
$settings->add(new admin_setting_configtext('access_token_url', 'Access Token URL:', '', 'https://apps.na.collabserv.com/manage/oauth/getAccessToken', PARAM_RAW));


ob_start();
require BASE_PATH . '/../views/js/globalPluginSettingsEditor.js'; 
echo '<script type="text/javascript">' . ob_get_clean() . '</script>';
?>