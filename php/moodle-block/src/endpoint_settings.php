<?php

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

if (!defined('IBM_SBT_CRYPTO_ENABLED')) {
	require_once $CFG->dirroot . DIRECTORY_SEPARATOR . 'blocks' . DIRECTORY_SEPARATOR . 'ibmsbt' . DIRECTORY_SEPARATOR . 'security-config.php';
}



// Make sure that the user is authorized to perform the action
if (!isloggedin() && !defined('IBM_SBT_TEST')) {
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
if (!$isadmin && !defined('IBM_SBT_TEST')) {
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
		
		$record = populateRecord($record);
		$ret = $DB->insert_record(ENDPOINTS, $record);
		var_dump($ret);
		return $ret;
	} else if ($_POST['type'] == 'delete') {
		$DB->delete_records(ENDPOINTS, array('id' => (intval($_POST['id']))));
	} else if ($_POST['type'] == 'update') {
		$record = $DB->get_record(ENDPOINTS, array('id' => (intval($_POST['id']))));
		
		if ($record == null) {
			return;
		}
		
		$record = populateRecord($record);
			
		$DB->update_record(ENDPOINTS, $record);
	} else if ($_POST['type'] == 'get') {
		$record = $DB->get_record(ENDPOINTS, array('id' => (intval($_POST['id']))));
		
		if ($record == null) {
			return;
		}

		$endpoint = array();
		$endpoint['allow_client_access'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->allow_client_access, base64_decode($record->iv));
		$endpoint['server_type'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->server_type, base64_decode($record->iv));
		$endpoint['api_version'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->api_version, base64_decode($record->iv));
		$endpoint['force_ssl_trust'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->force_ssl_trust, base64_decode($record->iv));
		$endpoint['basic_auth_method'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->basic_auth_method, base64_decode($record->iv));
		$endpoint['basic_auth_password'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->basic_auth_password, base64_decode($record->iv));
		$endpoint['basic_auth_username'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->basic_auth_username, base64_decode($record->iv));
		$endpoint['auth_type'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->auth_type, base64_decode($record->iv));
		$endpoint['authorization_url'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->authorization_url, base64_decode($record->iv));
		$endpoint['oauth2_callback_url'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->oauth2_callback_url, base64_decode($record->iv));
		$endpoint['request_token_url'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->request_token_url, base64_decode($record->iv));
		$endpoint['client_secret'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->client_secret, base64_decode($record->iv));
		$endpoint['client_id'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->client_id, base64_decode($record->iv));
		$endpoint['consumer_secret'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->consumer_secret, base64_decode($record->iv));
		$endpoint['consumer_key'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->consumer_key, base64_decode($record->iv));
		$endpoint['access_token_url'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->access_token_url, base64_decode($record->iv));
		$endpoint['server_url'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->server_url, base64_decode($record->iv));
		$endpoint['name'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->name, base64_decode($record->iv));
		$endpoint['form_auth_page'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->form_auth_page, base64_decode($record->iv));
		$endpoint['form_auth_login_page'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->form_auth_login_page, base64_decode($record->iv));
		$endpoint['form_auth_cookie_cache'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->form_auth_cookie_cache, base64_decode($record->iv));
		$endpoint['oauth_origin'] = ibm_sbt_decrypt(IBM_SBT_SETTINGS_KEY, $record->oauth_origin, base64_decode($record->iv));
		
		echo json_encode($endpoint);
	}
}

/**
 * Populates and encrypts the database record object with the submitted $_POST values.
 * 
 * @param object $record
 * @return object
 */
function populateRecord($record) 
{
	$iv = null;
	
	if (!isset($record->iv) || $record->iv == null) {
		if (defined('IBM_SBT_CRYPTO_ENABLED') && IBM_SBT_CRYPTO_ENABLED) {
			$iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
			$iv = base64_encode(mcrypt_create_iv($iv_size, MCRYPT_RAND));
		}
		$record->iv = $iv;
	} else {
		$iv = $record->iv;
	}
	
	if (isset($_POST['allow_client_access'])) {
		$record->allow_client_access = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['allow_client_access']), base64_decode($iv));
	}
	
	if (isset($_POST['server_type'])) {
		$record->server_type = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['server_type']), base64_decode($iv));
	}
	
	if (isset($_POST['api_version'])) {
		$record->api_version = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['api_version']), base64_decode($iv));
	}
	
	if (isset($_POST['force_ssl_trust'])) {
		$record->force_ssl_trust = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['force_ssl_trust']), base64_decode($iv));
	}
	
	if (isset($_POST['basic_auth_method'])) {
		$record->basic_auth_method = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['basic_auth_method']), base64_decode($iv));
	}
	
	if (isset($_POST['basic_auth_password'])) {
		$record->basic_auth_password = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['basic_auth_password']), base64_decode($iv));
	}
	
	if (isset($_POST['basic_auth_username'])) {
		$record->basic_auth_username = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['basic_auth_username']), base64_decode($iv));
	}
	
	if (isset($_POST['auth_type'])) {
		$record->auth_type = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['auth_type']), base64_decode($iv));
	}
	
	if (isset($_POST['authorization_url'])) {
		$record->authorization_url = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['authorization_url']), base64_decode($iv));
	}
	
	if (isset($_POST['oauth2_callback_url'])) {
		$record->oauth2_callback_url = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['oauth2_callback_url']), base64_decode($iv));
	}
	
	if (isset($_POST['request_token_url'])) {
		$record->request_token_url = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['request_token_url']), base64_decode($iv));
	}
	
	if (isset($_POST['client_secret'])) {
		$record->client_secret = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['client_secret']), base64_decode($iv));
	}
	
	if (isset($_POST['client_id'])) {
		$record->client_id = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['client_id']), base64_decode($iv));
	}
	
	if (isset($_POST['consumer_secret'])) {
		$record->consumer_secret = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['consumer_secret']), base64_decode($iv));
	}
	
	if (isset($_POST['consumer_key'])) {
		$record->consumer_key = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['consumer_key']), base64_decode($iv));
	}
	
	if (isset($_POST['access_token_url'])) {
		$record->access_token_url = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['access_token_url']), base64_decode($iv));
	}
	
	if (isset($_POST['server_url'])) {
		$record->server_url = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['server_url']), base64_decode($iv));
	}
	
	if (isset($_POST['name'])) {
		$record->name = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($_POST['name']), base64_decode($iv));
	}
	
	global $CFG;
	$record->oauth_origin = ibm_sbt_encrypt(IBM_SBT_SETTINGS_KEY, mysql_escape_string($CFG->wwwroot), base64_decode($iv));

	return $record;
}