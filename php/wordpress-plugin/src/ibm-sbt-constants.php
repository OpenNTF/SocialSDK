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
 * Collection of constant definitions needed by the plugin.
 * 
 * @author Benjamin Jakobus
 */
// Flag used to indicate to core that we are a Wordpress plugin
if (!defined('IBM_SBT_WORDPRESS_PLUGIN'))
define('IBM_SBT_WORDPRESS_PLUGIN', 'IBM SBT Wordpress Plugin');

// Flag used to prevent direct script access
if (!defined('SBT_SDK'))
define('SBT_SDK', 'IBM Social Business Toolkit');

// Base directory
if (!defined('BASE_PATH')) {
	define('BASE_PATH', dirname(__FILE__));
}

// Option name for endpoint configuration
if (!defined('ENDPOINTS')) {
	define('ENDPOINTS', 'ibm-sbtk-endpoints');
}

// Option name for the currently selected widget
if (!defined('SELECTED_WIDGET')) {
	define('SELECTED_WIDGET', 'ibm-sbtk-selected-widget');
}

// Option name for the currently selected widget
if (!defined('SDK_DEPLOY_URL')) {
	define('SDK_DEPLOY_URL', 'ibm-sbtk-sdk-deploy-url');
}

// Plugin name
if (!defined('PLUGIN_NAME')) {
	define('PLUGIN_NAME', 'ibm-sbtk');
}

// JavaScript library to use
if (!defined('JS_LIBRARY')) {
	define('JS_LIBRARY', 'ibm-sbtk-js-library');
}

// Key used to encrypt cookies
if (!defined('CRYPT')) {
	define('CRYPT', 'ibm-sbtk-crypt');
}

// User sessions
if (!defined('USER_SESSIONS')) {
	define('USER_SESSIONS', 'ibm-sbtk-user-sessions');
}

// Language 
if (!defined('LANG')) {
	define('LANG', 'ibm-sbtk-lang');
}

// Session indicator cookie name
if (!defined('WP_SESSION_INDICATOR')) {
	define('WP_SESSION_INDICATOR', 'ibm-sbtk-wp-session');
}

// SBTK session name
if (!defined('SESSION_NAME')) {
	define('SESSION_NAME', 'ibm_sbtk_session');
}