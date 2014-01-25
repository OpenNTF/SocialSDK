<?php ob_start();
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
 * Front controller. This allows for the calling of methods within classes from the URL.
 * 
 *  URLs take the form of XYZ and are re-written by the .htaccess file as:
 * 
 * @author Benjamin Jakobus
 */


//=============================
// Constants
//=============================

// Flag used to prevent direct script access
define('SBT_SDK', 'IBM Social Business Toolkit');

// Base directory
define('BASE_PATH', dirname( __FILE__));

include 'config.php';
if (!session_id()) {
	session_name($config['session_name']);
	session_start();
}


// Front controller blacklist - names of folders to containing classes that should be
// inaccessible through the frontpage controller
$blacklist = array('system', '../');

// Require the base controller
require_once 'system/core/BaseController.php';

// Require the base model
require_once 'system/core/BaseModel.php';

// Get class and method names
if (empty($_REQUEST) || empty($_REQUEST['class']) || empty($_REQUEST['method'])) {
	print('SBTK PHP Controller');
	return;
}
$class = $_REQUEST['class'];
$method = $_REQUEST['method'];
$classpath = (isset($_REQUEST['classpath']) ? $_REQUEST['classpath'] : '');
$plugin = null;

// See if the user is loading a plugin
if (!empty($_REQUEST['plugin'])) {
	$plugin = $_REQUEST['plugin'];
}

// Load plugin dependencies
if ($plugin != null) {
	switch ($plugin)
	{
		case "guzzle":
			// Load dependencies for Guzzle
			require_once "controllers/endpoint/OAuth1Endpoint.php";
			
			// Load properties
			require_once 'models/SBTKSettings.php';
			
			$settings = new SBTKSettings();
			
			//  Init the OAuth options
			$options = array(
					'consumer_key' => $settings->getConsumerKey(),
					'consumer_secret' => $settings->getConsumerSecret(),
					'server_uri' => $settings->getURL(),
					'request_token_uri' => $settings->getRequestTokenURL(),
					'authorize_uri' => $settings->getAuthorizationURL(),
					'access_token_uri' => $settings->getAccessTokenURL()
			);
			
			// Instantiate controller object
			$obj = new $class($options);

			// Call method on you controller object
			call_user_func_array(array($obj, $method), array());
			
			break;
	}
} else {	
	// Make sure that the classpath isn't blacklisted
	$blacklisted = false;
	foreach ($blacklist as $blacklistedItem) {
		if (startsWith($classpath, $blacklistedItem)) {
			$blacklisted = true;
			break;
		}
	}
	
	if ($blacklisted) {
		return;
	}

	$file = _include_file($classpath, $class);

	require_once $file;

	// Instantiate controller object
	$obj = new $class();

	// Call method on you controller object
	call_user_func_array(array($obj, $method), array());
}
 

/**
 * Checks whether a string stars with the given needle.
 * 
 * @param string $haystack		String to check.
 * @param string $needle		The substring for which to check.
 * @return boolean		True if the string starts with the specified substring; false if not.array
 * 
 * @author Benjamin Jakobus
 */
function startsWith($haystack, $needle) {
	return (strpos($haystack, $needle) === 0 || $needle === "");
}

/**
 * Returns the absolute path of the file to include. Since the project
 * can be deployed as part of another application (in which case the application folder
 * becomes the root) itself or as a standalone, we need to determine where to load
 * the file from.
 *
 * @param string $classpath
 * @param string $class			
 * @author Benjamin Jakobus
 */
function _include_file($classpath, $class) {
	if (file_exists('./controllers/' . $classpath . "/" . $class . '.php')) {
		return './controllers/' . $classpath . "/" . $class . '.php';
	} else if (file_exists(BASE_PATH . '/application/controllers/' . $classpath . "/" . $class . '.php')) {
		return BASE_PATH . '/application/controllers/' . $classpath . "/" . $class . '.php';
	} 
	return null;
}
