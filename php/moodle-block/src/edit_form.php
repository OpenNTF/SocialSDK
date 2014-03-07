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
 * SBTK edit form
 *
 * @author Benjamin Jakobus
 */
if (!defined('BASE_LOCATION')) {
	$autoload = __DIR__ . '/core/autoload.php';
	include $autoload;
}
class block_ibmsbt_edit_form extends block_edit_form {
 
    protected function specific_definition($mform) {
 
        // Section header title according to language file.
        $mform->addElement('header', 'configheader', get_string('blocksettings', 'block')); 

        // List of available samples. Note: keys must reflect relative path from
        // core/samples and must omit the .php file extension
        $plugins = array('choose' => 'Choose one...');
        $path = str_replace('core', '', BASE_PATH) . '/user_widgets/';
        if ($handle = opendir($path)) {
        	while (false !== ($file = readdir($handle))) {
        		if ($file != "." && $file != "..") {
        			if (!strpos($file, '.php') && !strpos($file, '.html')) {
        				continue;
        			}
        			$widgetName = str_replace('.php', '', $file);
        			$widgetName = str_replace('-', ' ', $widgetName);
        			
        			$plugins[$path . $file] = $widgetName;
        		}
        	}
        	closedir($handle);
        }
        
        $mform->addElement('html', '<p>For documentation, tutorials and guides on how to use the Social Business SDK, go to 
        		<a href="https://www.ibmdw.net/social/">https://www.ibmdw.net/social/</a> or visit <a href="https://greenhousestage.lotus.com/sbt/sbtplayground.nsf">our playground</a>
        		 directly if you need JavaScript snippets.</p>');
        
        
        
        // Type dropdown
        $mform->addElement('select', 'config_plugin', 'Plugin:', $plugins);
        $mform->setDefault('config_plugin', 'choose');
        
        $endpoints = array('connections' => 'connections');
        $mform->addElement('select', 'config_endpoint', 'Endpoint:', $endpoints);
        $mform->setDefault('config_endpoint', 'connections');
        
        // Block title
        $mform->addElement('text', 'config_elementID', 'Element ID:');
        $mform->setDefault('config_elementID', 'ibm-sbt-element-' . time());
        $mform->setType('config_elementID', PARAM_MULTILANG);
        $mform->addElement('text', 'config_title', 'Title:');
        $mform->setDefault('config_title', 'default value');
        $mform->setType('config_title', PARAM_MULTILANG);
    
 
    }
}