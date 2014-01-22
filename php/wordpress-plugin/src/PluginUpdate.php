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
 * This class handles the updating of plugin settings.
 * 
 * @author Benjamin Jakobus
 */
class PluginUpdate {

	/**
	 * Constructor.
	 * 
	 * @author Benjamin Jakobus
	 */
	function __construct() {
		$this->_updatePlugins();
	}
	
	/**
	 * Updates the settings for the community grid widget.
	 * 
	 * @author Benjamin Jakobus
	 */
	private function _updatePlugins() {
		
		$customPlugins = get_option('custom_plugins');
		
		// If no plugins exist, then create a new array
		if ($customPlugins === FALSE) {
			$customPlugins = array();
		}
		
		if (!in_array($_POST['plugin_name'], $customPlugins) && $_POST['delete_widget'] != 'yes') {
			array_push($customPlugins, $_POST['plugin_name']);
			
			$plugin = array();
			$plugin['javascript'] = $_POST['javascript'];
			$plugin['html'] = $_POST['html'];
			add_option($_POST['plugin_name'],  $plugin);
		} else {
			if ($_POST['delete_widget'] == 'yes') {
				delete_option($_POST['plugin_name']);
				$updatedCustomPlugins = array();
				foreach ($customPlugins as $plugin) {
					if ($plugin != $_POST['plugin_name']) {
						array_push($updatedCustomPlugins, $plugin);
					}
				}
				update_option('custom_plugins', $updatedCustomPlugins);
			} else {
				$plugin = array();
				$plugin['javascript'] = $_POST['javascript'];
				$plugin['html'] = $_POST['html'];
				update_option($_POST['plugin_name'], $plugin);
			}
		}
// 	delete_option($plugin);
		// If no plugin settings exist, create a new entry. Otherwise just update
		// the existing entries
		if ($_POST['delete_widget'] != 'yes') {
			if(get_option('custom_plugins') === FALSE){
				add_option('custom_plugins',  $customPlugins);
			} else {
				update_option('custom_plugins', $customPlugins);
			}
		}
// 	delete_option('custom_plugins');
		// Direct the user to the settings page and display a success message
		if(is_admin()) {
			$mySettingsPage = new SBTKPluginSettings(true);
		}
	}
}