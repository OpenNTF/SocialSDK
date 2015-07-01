<?php 
/**
 * (C) Copyright IBM Corp. 2013
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
 * @author Benjamin Jakobus
 */

require_once 'core/autoload.php';

$settings->add(new admin_setting_heading(
		'general',
		get_string('general_config', 'block_ibmsbt'),
		''
));

$settings->add(new admin_setting_configtext('sdk_deploy_url', 'SDK Deploy URL:', '', BASE_LOCATION . 'system/libs/js-sdk', PARAM_RAW));

$libraries = array(
		'//ajax.googleapis.com/ajax/libs/dojo/1.8.10/dojo/dojo.js' => 'Dojo Toolkit 1.8.4',
);
$settings->add(new admin_setting_configselect('js_library', 'JS Library:',
		'', '//ajax.googleapis.com/ajax/libs/dojo/1.8.10/dojo/dojo.js', $libraries));
?>