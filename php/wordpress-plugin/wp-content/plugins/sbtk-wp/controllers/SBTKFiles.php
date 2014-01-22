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
 * Plugin Controller Class
 *
 * This class object handles all logic related to plugin management.
 *
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class SBTKFiles extends BasePluginController {

	/**
	 * Creates a new files grid plugin.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createFilesGrid($args) {
		$this->loadModel('SBTKSettings');
		$settings = new SBTKSettings();
		$filesGridSettings = $settings->getFilesGridSettings();
		
		$viewData['hidePager'] = $filesGridSettings['hidePager'];
		$viewData['hideSorter'] = $filesGridSettings['hideSorter'];
		$viewData['containerType'] = $filesGridSettings['containerType'];
		
		$this->loadView('social/files/files-grid', $viewData);
	}
	
	/**
	 * Creates a new compact files grid plugin (lists file names only).
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createCompactFilesGrid($args) {
		$this->loadView('social/files/files-grid-compact', array());
	}

	
	/**
	 * Creates a new files grid displaying the files that have been shared
	 * by the given user.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createFilesSharedByMeGrid($args) {
		$this->loadView('social/files/files-shared-by-me-grid', array());
	}
	
	/**
	 * Creates a new files grid displaying the files that have been shared
	 * with the given user.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createFilesSharedWithMeGrid($args) {
		$this->loadView('social/files/files-shared-with-me-grid', array());
	}
	
	/**
	 * Creates a new files grid displaying the files and given comments.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createMyFileCommentsGrid($args) {
		$this->loadView('social/files/my-file-comments-grid', array());
	}
	
	/**
	 * Creates a new files grid displaying the folders of the given user.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createMyFoldersGrid($args) {
		$this->loadView('social/files/my-folders-grid', array());
	}
	
	/**
	 * Creates a new files grid displaying public files
	 * and their comments.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createPublicFileCommentsGrid($args) {
		$this->loadView('social/files/public-file-comments-grid', array());
	}
	
	/**
	 * Creates a public files grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createPublicFilesGrid($args) {
		$this->loadView('social/files/public-files-grid', array());
	}
}

