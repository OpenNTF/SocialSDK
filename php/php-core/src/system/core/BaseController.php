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
 * Base Controller Class
 *
 * This class object is the super class that every controller in the SDK will be assigned to.
 *
 * @author Benjamin Jakobus
 */
class BaseController {
	
	/**
	 * Model loader.
	 * 
	 * @param string $className		The name of the model to load.
	 */
	function loadModel($className) {
		$file = '/models/' . $className . '.php';
		$location = $this->_include_file($file);
		@include_once $location;
	}
	
	/**
	 * Controller loader.
	 *
	 * @param string $className		The name of the controller to load.
	 */
	function loadController($className) {
		$file = '/controllers/' . $className . '.php';
		$location = $this->_include_file($file);
		@include_once $location;
	}
	
	/**
	 * View loader.
	 *
	 * @param string $className		The name of the view to load.
	 * @param array  $viewData		Associative array containing the data to be displayed by the view.
	 */
	
	function loadView($className, $viewData) {
		$file = '/views/' . $className . '.php';
		$location = $this->_include_file($file);
		if ($viewData != null) {
			extract($viewData);
		}
		@include_once $location;
	}
	
	/**
	 * Returns the absolute path of the file to include. Since the project
	 * can be deployed as part of another application (in which case the application folder
	 * becomes the root) itself or as a standalone, we need to determine where to load
	 * the file from.
	 *
	 * @param string $file			The file to load, prefixed with /model, /controller or /view.
	 */
	private function _include_file($file) {
		if (file_exists(BASE_PATH . $file)) {
			return BASE_PATH . $file;
		} else if (file_exists(BASE_PATH . '/application' . $file)) {
			return BASE_PATH .'/application' . $file;
		}
		return null;
	}
	
	/**
	 * Simple class loader.
	 * 
	 * @param string $className		Name of the class to load.
	 */
	function load($className) {
		@include_once $className . '.php';
	}
}

