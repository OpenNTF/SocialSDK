<?php
/*
 * © Copyright IBM Corp. 2013
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
 * Logic for plugin activation and deactivation.
 *
 * @author Benjamin Jakobus
 */

/**
 * Called upon plugin deactivation. Removes all database entries.
 */
function ibm_sbtk_deactivate_plugin() {

	delete_option(ENDPOINTS);
	delete_option(SDK_DEPLOY_URL);

	delete_option(SELECTED_WIDGET);
	delete_option(JS_LIBRARY);
	
	delete_option(CRYPT);
	
	// Extract user session
	$sessions = get_option(USER_SESSIONS);
	foreach ($sessions as $session) {
		delete_option($session['id']);
	}
	
	delete_option(USER_SESSIONS);
	
	delete_option(PLUGIN_NAME . '*');
	
	return true;
}

/**
 * Called upon plugin activation. Creates database option entries.
 */
function ibm_sbtk_activate_plugin() {
	
	if (get_option(ENDPOINTS) === false) {
		add_option(ENDPOINTS,  array());
	}

	if (get_option(SELECTED_WIDGET) === false) {
		add_option(SELECTED_WIDGET,  '');
	}
	
	if (get_option(JS_LIBRARY) === false) {
		add_option(JS_LIBRARY,  '');
	}
	
	if (get_option(SDK_DEPLOY_URL) === false) {
		$settings = array();
		$settings['sdk_deploy_url'] = plugins_url(PLUGIN_NAME) . '/core/system/libs/js-sdk';
		add_option(SDK_DEPLOY_URL,  $settings);
	}
	
	if (get_option(CRYPT) === false) {
		$pivKey = ibm_sbtk_gen_string(32);
		$iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
    	$iv = mcrypt_create_iv($iv_size, MCRYPT_RAND);
    	
    	$iv = base64_encode($iv);

		add_option(CRYPT, array(
				'key' => $pivKey,
				'iv' => $iv
			)
		);
	}
	
	if (get_option(USER_SESSIONS) === false) {
		add_option(USER_SESSIONS, array());
	}
	
	return true;
}

/**
 * Generates a random string of given length.
 * 
 * @param int $length		Desired length of the string.
 * 
 * @return string			Random string.
 */
function ibm_sbtk_gen_string($length) {
	$str = "";
	for ($i = 0; $i < $length; $i++) {
		$str .= chr(mt_rand(33, 126));
	}
	return $str;
}

/**
 * Loads the language files.
 */
function ibm_sbtk_load_language_files() {
	// English language file
	require BASE_PATH . '/lang/en/lang.php';
	$GLOBALS[LANG] = $lang;
}

/**
 * Sets a flag to indicate to external non-wp code that the user is logged in (or isn't).
 */
function ibm_sbtk_user_login_check() {
	// Set a cookie to indicate whether the user is logged into wordpress (so that we
	// can determine if the user is logged in even if we are running in non-wordpress mode
	$timestamp = time();
	if(is_user_logged_in()) {
		setcookie(WP_SESSION_INDICATOR, get_current_user_id(), $timestamp + 604800);
		setcookie('IBMSBTKOAuthLogin', "yes", $timestamp + 604800);
	} else {
		setcookie(WP_SESSION_INDICATOR, "-1", $timestamp - 604800);
		setcookie('IBMSBTKOAuthLogin', "", $timestamp - 604800);
	}
}

/**
 * Logout hook for clearing session data
 */
function ibm_sbtk_logout() {
	setcookie(WP_SESSION_INDICATOR, "-1", $timestamp - 604800);
}