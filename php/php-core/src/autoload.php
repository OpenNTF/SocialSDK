***REMOVED***
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
 * Handles the loading of dependencies.
 *
 * @author Benjamin Jakobus
 */
 
/**
 * Autoloader for application and system controllers.
 * 
 * @author Benjamin Jakobus
 */
// Flag used to prevent direct script access
if (!defined('SBT_SDK')) {
	define('SBT_SDK', 'IBM Social Business Toolkit');
}

if (!defined('BASE_PATH')) {
	define('BASE_PATH', dirname(__FILE__) );
}
// Define the base location
if (defined('IBM_SBT_MOODLE_BLOCK')) {
	define('BASE_LOCATION',  $CFG->wwwroot . '/blocks/ibmsbtk/core/');
} else if (defined('IBM_SBT_WORDPRESS_PLUGIN')) {
	define('BASE_LOCATION',  get_site_url() . '/wp-content/plugins/' . PLUGIN_NAME);
} else {
	define('BASE_LOCATION',  'http://localhost/core/src');
}

spl_autoload_register(function ($class) 
{
	$coreFile = BASE_PATH . '/system/core/' . $class . '.php';
	$applicationFile = BASE_PATH . '/controllers/' . $class . '.php';
	$applicationEndpointFile = BASE_PATH . '/controllers/endpoint/' . $class . '.php';
	$applicationServicesFile = BASE_PATH . '/controllers/services/' . $class . '.php';
	$model = BASE_PATH . '/models/' . $class . '.php';
	if (file_exists($coreFile)) {
		include $coreFile;
	} else if (file_exists($applicationFile)) {
		include $applicationFile;
	} else if (file_exists($model)) {
		include $model;
	} else if (file_exists($applicationEndpointFile)) {
		include $applicationEndpointFile;
	} else if (file_exists($applicationServicesFile)) {
		include $applicationServicesFile;
	}
});