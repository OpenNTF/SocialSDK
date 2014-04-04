***REMOVED***

/**
 * (C) Copyright IBM Corp. 2014
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
 * Handles AJAX requests for updating the endpoints.
 * 
 * @author Benjamin Jakobus
 */
global $CFG;
if (!isset($CFG) || !isset($CFG->wwwroot)) {
	$path = str_replace('blocks'.DIRECTORY_SEPARATOR.'ibmsbt', '', __DIR__);
	include_once $path . '/config.php';
}

if (!defined('ENDPOINTS')) {
	define('ENDPOINTS', 'ibm_sbt_endpoints');
}



// Make sure that the user is authorized to perform the action
if (!isloggedin()) {
	echo "You must be logged in to perform this action.";
	return;
}

$admins = get_admins();
$isadmin = false;
foreach ($admins as $admin) {
	if ($USER->id == $admin->id) {
		$isadmin = true;
		break;
	}
}
if (!$isadmin) {
	echo "Only admins can perform this action.";
	return;
} 




if (isset($_POST['type'])) {
	global $DB;
	global $USER;

	// Create a new endpoint
	if ($_POST['type'] == 'create') {
		$record = new stdClass();
		$record->created_by_user_id = intval($USER->id);
		
		if (isset($_POST['allow_client_access'])) {
			$record->allow_client_access = $DB->sql_like_escape($_POST['allow_client_access']);
		}
		
		if (isset($_POST['server_type'])) {
			$record->server_type = $DB->sql_like_escape($_POST['server_type']);
		}
		
		if (isset($_POST['api_version'])) {
			$record->api_version = $DB->sql_like_escape($_POST['api_version']);
		}
		
		if (isset($_POST['force_ssl_trust'])) {
			$record->force_ssl_trust = $DB->sql_like_escape($_POST['force_ssl_trust']);
		}
		
		if (isset($_POST['form_auth_page'])) {
			$record->form_auth_page = $DB->sql_like_escape($_POST['form_auth_page']);
		}
		
		if (isset($_POST['form_auth_login_page'])) {
			$record->form_auth_login_page = $DB->sql_like_escape($_POST['form_auth_login_page']);
		}
		
		if (isset($_POST['form_auth_cookie_cache'])) {
			$record->form_auth_cookie_cache = $DB->sql_like_escape($_POST['form_auth_cookie_cache']);
		}
		
		if (isset($_POST['basic_auth_method'])) {
			$record->basic_auth_method = $DB->sql_like_escape($_POST['basic_auth_method']);
		}
		
		if (isset($_POST['basic_auth_password'])) {
			$record->basic_auth_password = $DB->sql_like_escape($_POST['basic_auth_password']);
		}
		
		if (isset($_POST['basic_auth_username'])) {
			$record->basic_auth_username = $DB->sql_like_escape($_POST['basic_auth_username']);
		}
		
		if (isset($_POST['auth_type'])) {
			$record->auth_type = $DB->sql_like_escape($_POST['auth_type']);
		}
		
		if (isset($_POST['authorization_url'])) {
			$record->authorization_url = $DB->sql_like_escape($_POST['authorization_url']);
		}
		
		if (isset($_POST['oauth2_callback_url'])) {
			$record->oauth2_callback_url = $DB->sql_like_escape($_POST['oauth2_callback_url']);
		}
		
		if (isset($_POST['request_token_url'])) {
			$record->request_token_url = $DB->sql_like_escape($_POST['request_token_url']);
		}
		
		if (isset($_POST['client_secret'])) {
			$record->client_secret = $DB->sql_like_escape($_POST['client_secret']);
		}
		
		if (isset($_POST['client_id'])) {
			$record->client_id = $DB->sql_like_escape($_POST['client_id']);
		}
		
		if (isset($_POST['consumer_secret'])) {
			$record->consumer_secret = $DB->sql_like_escape($_POST['consumer_secret']);
		}
		
		if (isset($_POST['consumer_key'])) {
			$record->consumer_key = $DB->sql_like_escape($_POST['consumer_key']);
		}
		
		if (isset($_POST['access_token_url'])) {
			$record->access_token_url = $DB->sql_like_escape($_POST['access_token_url']);
		}
		
		if (isset($_POST['server_url'])) {
			$record->server_url = $DB->sql_like_escape($_POST['server_url']);
		}
		
		if (isset($_POST['name'])) {
			$record->name = $DB->sql_like_escape($_POST['name']);
		}
		
		$ret = $DB->insert_record(ENDPOINTS, $record);
		var_dump($ret);
		return $ret;
	} else if ($_POST['type'] == 'delete') {
		$DB->delete_records(ENDPOINTS, array('id' => $DB->sql_like_escape(intval($_POST['id']))));
	} else if ($_POST['type'] == 'update') {
		$record = $DB->get_record(ENDPOINTS, array('id' => $DB->sql_like_escape(intval($_POST['id']))));
		
		if ($record == null) {
			return;
		}
		
		if (isset($_POST['allow_client_access'])) {
			$record->allow_client_access = $DB->sql_like_escape($_POST['allow_client_access']);
		}
		
		if (isset($_POST['server_type'])) {
			$record->server_type = $DB->sql_like_escape($_POST['server_type']);
		}
		
		if (isset($_POST['api_version'])) {
			$record->api_version = $DB->sql_like_escape($_POST['api_version']);
		}
		
		if (isset($_POST['force_ssl_trust'])) {
			$record->force_ssl_trust = $DB->sql_like_escape($_POST['force_ssl_trust']);
		}
		
		if (isset($_POST['form_auth_page'])) {
			$record->form_auth_page = $DB->sql_like_escape($_POST['form_auth_page']);
		}
		
		if (isset($_POST['form_auth_login_page'])) {
			$record->form_auth_login_page = $DB->sql_like_escape($_POST['new_form_auth_login_page']);
		}
		
		if (isset($_POST['form_auth_cookie_cache'])) {
			$record->form_auth_cookie_cache = $DB->sql_like_escape($_POST['form_auth_cookie_cache']);
		}
		
		if (isset($_POST['basic_auth_method'])) {
			$record->basic_auth_method = $DB->sql_like_escape($_POST['basic_auth_method']);
		}
		
		if (isset($_POST['basic_auth_password'])) {
			$record->basic_auth_password = $DB->sql_like_escape($_POST['basic_auth_password']);
		}
		
		if (isset($_POST['basic_auth_username'])) {
			$record->basic_auth_username = $DB->sql_like_escape($_POST['basic_auth_username']);
		}
		
		if (isset($_POST['auth_type'])) {
			$record->auth_type = $DB->sql_like_escape($_POST['auth_type']);
		}
		
		if (isset($_POST['authorization_url'])) {
			$record->authorization_url = $DB->sql_like_escape($_POST['authorization_url']);
		}
		
		if (isset($_POST['oauth2_callback_url'])) {
			$record->oauth2_callback_url = $DB->sql_like_escape($_POST['oauth2_callback_url']);
		}
		
		if (isset($_POST['request_token_url'])) {
			$record->request_token_url = $DB->sql_like_escape($_POST['request_token_url']);
		}
		
		if (isset($_POST['client_secret'])) {
			$record->client_secret = $DB->sql_like_escape($_POST['client_secret']);
		}
		
		if (isset($_POST['client_id'])) {
			$record->client_id = $DB->sql_like_escape($_POST['client_id']);
		}
		
		if (isset($_POST['consumer_secret'])) {
			$record->consumer_secret = $DB->sql_like_escape($_POST['consumer_secret']);
		}
		
		if (isset($_POST['consumer_key'])) {
			$record->consumer_key = $DB->sql_like_escape($_POST['consumer_key']);
		}
		
		if (isset($_POST['access_token_url'])) {
			$record->access_token_url = $DB->sql_like_escape($_POST['access_token_url']);
		}
		
		if (isset($_POST['server_url'])) {
			$record->server_url = $DB->sql_like_escape($_POST['server_url']);
		}
		
		if (isset($_POST['name'])) {
			$record->name = $DB->sql_like_escape($_POST['name']);
		}
			
		$DB->update_record(ENDPOINTS, $record);
	} else if ($_POST['type'] == 'get') {
		$record = $DB->get_record(ENDPOINTS, array('id' => $DB->sql_like_escape(intval($_POST['id']))));
		
		if ($record == null) {
			return;
		}
		
		$endpoint = array();
		
		$endpoint['allow_client_access'] = $record->allow_client_access;
		$endpoint['server_type'] = $record->server_type;
		$endpoint['api_version'] = $record->api_version;
		$endpoint['force_ssl_trust'] = $record->force_ssl_trust;
		$endpoint['basic_auth_method'] = $record->basic_auth_method;
		$endpoint['basic_auth_password'] = $record->basic_auth_password;
		$endpoint['basic_auth_username'] = $record->basic_auth_username;
		$endpoint['auth_type'] = $record->auth_type;
		$endpoint['authorization_url'] = $record->authorization_url;
		$endpoint['oauth2_callback_url'] = $record->oauth2_callback_url;
		$endpoint['request_token_url'] = $record->request_token_url;
		$endpoint['client_secret'] = $record->client_secret;
		$endpoint['client_id'] = $record->client_id;
		$endpoint['consumer_secret'] = $record->consumer_secret;
		$endpoint['consumer_key'] = $record->consumer_key;
		$endpoint['access_token_url'] = $record->access_token_url;
		$endpoint['server_url'] = $record->server_url;
		$endpoint['name'] = $record->name;
		$endpoint['form_auth_page'] = $record->form_auth_page;
		$endpoint['form_auth_login_page'] = $record->form_auth_login_page;
		$endpoint['form_auth_cookie_cache'] = $record->form_auth_cookie_cache;
		
		echo json_encode($endpoint);
	}
}