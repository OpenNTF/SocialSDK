<?php ob_start();

/**
 * Plugin Name: Social Business Toolkit integration plugin
 * Plugin URI: https://github.com/OpenNTF/SocialSDK/
 * Description: This plugin provides access to the IBM Social Business Toolkit.
 * Version: 1.0
 * Text Domain: ibm-sbtk
 * Author: Benjamin Jakobus
 * Author URI: https://github.com/OpenNTF/SocialSDK/
 * License: Apache 2.0
 */

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
spl_autoload_register(function ($class)
{
	$coreFile = BASE_PATH . '/core/system/core/' . $class . '.php';
	$applicationFile = BASE_PATH . '/controllers/' . $class . '.php';
	$coreController = BASE_PATH . '/core/controllers/' . $class . '.php';
	$applicationEndpointFile = BASE_PATH . '/core/controllers/endpoint/' . $class . '.php';
	$applicationServicesFile = BASE_PATH . '/core/controllers/services/' . $class . '.php';
	$model = BASE_PATH . '/models/' . $class . '.php';
	$coreModel = BASE_PATH . '/core/models/' . $class . '.php';
	if (file_exists($coreFile)) {
		@include $coreFile;
	} else if (file_exists($applicationFile)) {
		@include $applicationFile;
	} else if (file_exists($model)) {
		@include $model;
	} else if (file_exists($applicationEndpointFile)) {
		@include $applicationEndpointFile;
	} else if (file_exists($applicationServicesFile)) {
		@include $applicationServicesFile;
	} else if (file_exists($coreModel)) {
		@include $coreModel;
	} else if (file_exists($coreController)) {
		@include $coreController;
	}

});

// IBM SBTK Wordpress plugin specific constants
require_once 'ibm-sbtk-constants.php';

// Add activation and deactivation hooks
require_once 'ibm-sbtk-plugin-setup.php';

// Activation hook
register_activation_hook(__FILE__, 'ibm_sbtk_activate_plugin');

// Deactivation hook
register_deactivation_hook(__FILE__, 'ibm_sbtk_deactivate_plugin');

// Uninstall hook
register_uninstall_hook(__FILE__, 'ibm_sbtk_deactivate_plugin');

// Add language file initialization listener
ibm_sbtk_load_language_files();

// Autoloader for loading dependencies
require_once 'core/autoload.php';

// Widget registration
require_once 'ibm-sbtk-widget-registration.php';

// Load base controllers
require_once 'core/system/core/BaseController.php';
require_once 'core/system/core/BasePluginController.php';

// Check database for expired sessions. Delete them
$sessions = get_option(USER_SESSIONS);
if ($sessions !== false) {
	$now = time();
	for ($i = 0; $i < sizeof($sessions); $i++) {
		$session = $sessions[$i];
		// Delete sessions that are older than one day 86400
		if ($now - $session['created'] >= 86400) {
			delete_option($session['id']);
			unset($sessions[$i]);
			update_option(USER_SESSIONS, $sessions); 
		}
	}
}

// If we are posting date to this page, then create an options update.
// Otherwise display the settings page as normal
if (isset($_POST['endpoint_name'])) {
	$optionsUpdate = new SBTKEndpointUpdate();
} else {
	if(is_admin()) {
		$mySettingsPage = new SBTKPluginSettings();
	}
}
?>