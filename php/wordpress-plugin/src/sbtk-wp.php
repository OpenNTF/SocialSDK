***REMOVED***

/**
 * Plugin Name: Social Business Toolkit integration plugin
 * Plugin URI: http://example.com
 * Description: Plugin providing access to the SBTK javascript api and controls.
 * Version: 0.1
 * Author: Lorenzo Boccaccia
 * Author URI: https://github.com/LorenzoBoccaccia
 * License: Apache 2.0
 */


// Flag used to indicate to core that we are a Wordpress plugin
define('IBM_SBT_WORDPRESS_PLUGIN', 'IBM SBT Wordpress Plugin');

// Flag used to prevent direct script access
define('SBT_SDK', 'IBM Social Business Toolkit');
// Base directory
define('BASE_PATH', dirname(__FILE__));
// Samples directory
define('SAMPLES_PATH', BASE_PATH . '/views/social');
// Flag to indicate whether SBT JS header code has already been created or not
// (header code should only be created once)
$headerCreated = false;

// Require base controllers
require_once 'system/core/BaseController.php';
require_once 'system/core/BasePluginController.php';

// TODO: Create autoloader
require_once 'controllers/SBTKCommunities.php';
require_once 'controllers/SBTKBookmarks.php';
require_once 'controllers/SBTKFiles.php';
require_once 'controllers/SBTKForums.php';
require_once 'controllers/SBTKOptions.php';
require_once 'models/SBTKSettings.php';
require_once 'models/CredentialStore.php';
// TODO: Move functions into helper file

/**
 * Callback for creating a plugin using custom code.
 *
 * @param unknown $args
 *
 * @author Benjamin Jakobus
 */
function widget_sbtk_custom_plugin($args) {

	$settings = new SBTKSettings();
	$store = new CredentialStore();
// 	if (!$headerCreated) {
	if ($settings->getAuthenticationMethod() == 'oauth1' && $store->getOAuthAccessToken() == null &&
		(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
		require 'views/oauth-login-display.php';
		return;
		
	} 
// 		$headerCreated = true;
// 	}

		
	// Output code for checking whether a user is authenticated
	// TODO: Consolidate with OAuth
	if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt') {
		echo '<div class="ibmsbtk-widget" style="display: none;">';
	}
	
	$pluginData = null;

	if (strEndsWith($args['widget_id'], '.php')) {
		$contents = extractSampleContents($sample, $plugin);
		$pluginData['javascript'] = $contents['javascript'];
		$pluginData['html'] = $contents['html'];
	} else {
		$pluginData = get_option($args['widget_name']);
		if ($pluginData === FALSE) {
			$pluginData = get_option(createSampleName($args['widget_name']));
		}
	}
	
	echo $pluginData['html'];
	echo '<script type="text/javascript">' . $pluginData['javascript'] . '</script>';
	if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt') {
		echo '</div>';
		require_once 'views/basic-auth-login-display.php';
	}	
}

function create($widgetPath) {
	
	$settings = new SBTKSettings();
	$store = new CredentialStore();
	// 	if (!$headerCreated) {
	if ($settings->getAuthenticationMethod() == 'oauth1' && $store->getOAuthAccessToken() == null &&
		(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
		require 'views/oauth-login-display.php';
		return;
		
	} 
	// 		$headerCreated = true;
	// 	}
	
	// Output code for checking whether a user is authenticated
	// TODO: Consolidate with OAuth
	

	// Output code for checking whether a user is authenticated
	// TODO: Consolidate with OAuth
	if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt') {
		echo '<div class="ibmsbtk-widget" style="display: none;">';
	}
	
	$pluginData = null;
	
	require $widgetPath;
	
	
	if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt') {
		echo '</div>';
		require_once 'views/basic-auth-login-display.php';
	}
}

/**
 * Callback for creating the plugin header.
 *
 * @param unknown $args
 *
 * @author Benjamin Jakobus
 */
function widget_sbtk_header($args) {
	$settings = new SBTKSettings();
	$store = new CredentialStore();

	if ($settings->getAuthenticationMethod() == 'oauth1' && $store->getOAuthAccessToken() == null &&
		(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
		
		return;
	}
	
	$plugin = new SBTKForums();
	$plugin->createHeader();
}

/**
 * Creates a sample filename from a widget name.
 * 
 * @param unknown $name
 * @return string
 * 
 * @author Benjamin Jakobus
 */
function createSampleName($name) {
	$sample = str_replace(' ', '-', $name);
	$sample = trim(strtolower($sample)) . '.php';
	return $sample;
}

/**
 * Extracts the plugin name given a PHP sample filename.
 * 
 * @param unknown $pluginFile
 * @return string
 * 
 * @author Benjamin Jakobus
 */
function extractSampleName($pluginFile) {
	$plugin = str_replace('.php', '', $pluginFile);
	$plugin = str_replace('-', ' ', $plugin);
	return ucwords($plugin);
}

function extractSampleContents($type, $plugin) {
 	$sbtkPlugin = BASE_PATH . '/views/social/' . $type . '/' . $plugin;

 	$file = file_get_contents($sbtkPlugin);
	
 	// Extract HTML
 	$html = preg_replace('#<script.*</script>#is', '', $file);
	
 	// Extract JavaScript
 	preg_match('#<script.*</script>#is', $file, $javascript);
 	$javascript = str_replace('</script>', '', $javascript);
 	$javascript = str_replace('<script type="text/javascript">', '', $javascript);
 	$javascript = preg_replace("/\<\?php.*?>/is", "true", $javascript);
 	$javascript = str_replace('rendererArgs : { containerType : "true" }', '', $javascript);
	
 	$contents = array();
 	$contents['html'] = $html;
 	$contents['javascript'] = $javascript;
	return $contents;
}

function strEndsWith($haystack, $needle) {
	return $needle === "" || substr($haystack, -strlen($needle)) === $needle;
}


// Register the header with Wordpress
add_action('wp_head', 'widget_sbtk_header');

// Register different widgets with Wordpress
$plugins = get_option('custom_plugins');

if (isset($plugins) && $plugins != null) {
	foreach ($plugins as $plugin) {
		$plugin_id = str_replace(' ', '_', $plugin);
		if (strEndsWith($plugin, '.php')) {
			continue;
		}
		wp_register_sidebar_widget('custom_plugin_' . $plugin_id, $plugin, 'widget_sbtk_custom_plugin', array('description' => 'Displays a custom SBT plugin.'));
	}
}	

function widget_sbtk_my_files($args) {
	create('views/my-files.php');
}

function widget_sbtk_my_communities($args) {
	create('views/my-communities.php');
}

function widget_sbtk_my_communities_grid($args) {
	create('views/my-communities-grid.php');
}

// REGISTRATION GOES HERE
wp_register_sidebar_widget('my_files_widget_id', 'My Files', 'widget_sbtk_my_files', array('description' => 'Displays files owned by the current user.'));
wp_register_sidebar_widget('my_communities_widget_id', 'My Communities', 'widget_sbtk_my_communities', array('description' => 'Displays communities owned by the current user.'));
wp_register_sidebar_widget('my_communities_grid_widget_id', 'My Communities Grid', 'widget_sbtk_my_communities_grid', array('description' => 'Displays communities owned by the current user.'));


// Register options page
require_once 'sbtk-options.php';

// Make sure that we have connection information for at least one endpoint;
// if not, then populate the DB with a default.
$options = new SBTKOptions();

add_action('wp_head', 'myStartSession', 1);
add_action('wp_logout', 'myEndSession');
add_action('wp_login', 'myEndSession');

function widget_sbtk_deactivate() {
	delete_option('my_endpoints');
	
	delete_option('sbt_sdk_settings');
	
	$customPlugins = get_option('custom_plugins');
	
	if ($customPlugins !== FALSE) {
		foreach ($customPlugins as $plugin) {
			delete_option($plugin);
		}
	}
	
	delete_option('custom_plugins');
	delete_option('selected_custom_plugin');
}
// Deactivation hook
register_deactivation_hook( __FILE__, 'widget_sbtk_deactivate' );

function myStartSession() {
	if (!session_id()) {
		require BASE_PATH . '/config.php';
		session_name($config['session_name']);
		session_start();
	}
}

function myEndSession() {
	session_destroy ();
}
?>