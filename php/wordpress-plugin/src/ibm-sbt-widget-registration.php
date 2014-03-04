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
 * Handles the registration of widgets.
 *
 * @author Benjamin Jakobus
 */


// Register the header with Wordpress
add_action('wp_head', 'ibm_sbtk_header');

// Load callback functions
require 'ibm-sbt-widget-callbacks.php';

// Register files widget
add_action('widgets_init', function() {
	register_widget('SBTFilesWidget');
});

// Register communities widget
add_action('widgets_init', function() {
	register_widget('SBTCommunitiesWidget');
});

// Register communities widget
add_action('widgets_init', function() {
	register_widget('SBTFilesViewWidget');
});
	

add_action('widgets_init', function() {
	// Register user defined widgets
	if ($handle = opendir(BASE_PATH . '/user_widgets/controllers/')) {
		while (false !== ($file = readdir($handle))) {
			if ($file != "." && $file != "..") {
				require BASE_PATH . '/user_widgets/controllers/' . $file;
				$userWidget = str_replace('.php', '', $file);
				register_widget($userWidget);
			}
		}
		closedir($handle);
	}
});
