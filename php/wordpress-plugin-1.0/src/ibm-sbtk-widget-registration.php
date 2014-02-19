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
 * Handles the registration of widgets.
 *
 * @author Benjamin Jakobus
 */


// Register the header with Wordpress
add_action('wp_head', 'ibm_sbtk_header');

// Load callback functions
require 'ibm-sbtk-widget-callbacks.php';

// Register files widget
add_action('widgets_init', function() {
	register_widget('SBTKFilesWidget');
});

// Register communities widget
add_action('widgets_init', function() {
	register_widget('SBTKCommunitiesWidget');
});

// Register communities widget
add_action('widgets_init', function() {
	register_widget('SBTKFilesViewWidget');
});
