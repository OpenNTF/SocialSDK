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
 * Plugin Controller Class
 *
 * This class object handles all logic related to plugin management.
 *
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class SBTKForums extends BasePluginController {
	/**
	 * Creates a new forums grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createForumsGrid($args) {
		$this->loadModel('SBTKSettings');
		$settings = new SBTKSettings();
		$forumsGridSettings = $settings->getForumsGridSettings();
		
		$viewData['hidePager'] = $forumsGridSettings['hidePager'];
		$viewData['hideSorter'] = $forumsGridSettings['hideSorter'];
		$viewData['containerType'] = $forumsGridSettings['containerType'];
		
		$this->loadView('social/forums/forums-grid', $viewData);
	}
	
	/**
	 * Creates a new forum topics grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createForumTopicsGrid($args) {
		$this->loadView('social/forums/topics-grid', array());
	}
	
	/**
	 * Creates a new forum entries grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createForumEntriesGrid($args) {
		$this->loadView('social/forums/entries-grid', array());
	}
	
	/**
	 * Creates a new forum answered topics grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createForumAnsweredTopicsGrid($args) {
		$this->loadView('social/forums/answered-topics-grid', array());
	}
}

