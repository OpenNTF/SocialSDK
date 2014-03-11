<?php 
/**
 * (C) Copyright IBM Corp. 2012
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
		'General Configuration',
		''
));

$settings->add(new admin_setting_configtext('sdk_deploy_url', 'SDK Deploy URL:', '', BASE_LOCATION . 'system/libs/js-sdk', PARAM_RAW));

$libraries = array(
		'//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js' => 'Dojo Toolkit 1.4.3', 
		'//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dojo/dojo.js' => 'Dojo Toolkit 1.5.2',
		'//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/dojo.js' => 'Dojo Toolkit 1.6.1',
		'//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/dojo.js' => 'Dojo Toolkit 1.7.4',
		'//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dojo/dojo.js' => 'Dojo Toolkit 1.8.4',
		'//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dojo/dojo.js' => 'Dojo Toolkit 1.9.0',
		'//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js' => 'JQuery 1.8.3',
		'//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js.uncompressed.js' => 'Dojo Toolkit 1.4.3 uncompressed',
		'//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dojo/dojo.js.uncompressed.js' => 'Dojo Toolkit 1.5.2 uncompressed',
		'//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/dojo.js.uncompressed.js' => 'Dojo Toolkit 1.6.1 uncompressed',
		'//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/dojo.js.uncompressed.js' => 'Dojo Toolkit 1.7.4 uncompressed',
		'//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dojo/dojo.js.uncompressed.js' => 'Dojo Toolkit 1.8.4 uncompressed',
		'//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dojo/dojo.js.uncompressed.js' => 'Dojo Toolkit 1.9.0 uncompressed',
		'//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js.uncompressed.js' => 'JQuery 1.8.3 uncompressed'
);
$settings->add(new admin_setting_configselect('js_library', 'JS Library:',
		'', '//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dojo/dojo.js', $libraries));
?>