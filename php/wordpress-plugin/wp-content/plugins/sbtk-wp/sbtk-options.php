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
 * The settings page for configuring the SBTK plugin.
 *
 * @author Benjamin Jakobus
 */



require_once 'EndpointUpdate.php';
require_once 'PluginUpdate.php';
require_once 'controllers/SBTKPluginSettings.php';


// If we are posting date to this page, then create an options update.
// Otherwise display the settings page as normal
if (isset($_POST['endpoint_name'])) {
	$optionsUpdate = new EndpointUpdate();
} else if (isset($_POST['update_plugins'])) {
		$optionsUpdate = new PluginUpdate();
} else {
	if(is_admin()) {
		$mySettingsPage = new SBTKPluginSettings();
	}
}