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
 * SBTK Moodle block
 *
 * @author Benjamin Jakobus
 */

define('IBM_SBT_MOODLE_BLOCK', 'IBM SBT MOODLE BLOCK');
class block_ibmsbt extends block_base {
	
	/**
	 * Init.
	 */
	public function init() {
		$this->title = get_string('ibmsbt', 'block_ibmsbt');
	}
	
	/**
	 * Returns the block contents.
	 * 
	 * @return stdClass		Block content.
	 */
	public function get_content() {
		if ($this->content !== null) {
			return $this->content;
		}	

		$this->content = new stdClass();
	
		if (!isset($this->config->plugin) || $this->config->plugin == '') {
			return $this->content;
		}
		
		if (isset($this->config->plugin)) {
			ob_start();
			
			if (!defined('BASE_LOCATION')) {
				$autoload = __DIR__ . '/core/autoload.php';
				include $autoload;
			}
			
			$this->loadModel('SBTSettings');
			$this->loadModel('SBTCredentialStore');

			$settings = new SBTSettings();
			$store = SBTCredentialStore::getInstance();	
			
			global $CFG;
			
			$blockPath = $CFG->dirroot . '/blocks/ibmsbt/';

			echo '<div name="ibm_sbtk_widget">';
			if (($settings->getAuthenticationMethod() == 'oauth1' || $settings->getAuthenticationMethod() == 'oauth2') && $store->getOAuthAccessToken() == null &&
			(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
				require $blockPath . '/core/views/oauth-login-display.php';
				$this->content->text = ob_get_clean();
				return;
		
			}
			$plugin = new SBTPlugin();
			$plugin->createHeader();
			if (($settings->getAuthenticationMethod() == 'basic' && $store->getBasicAuthUsername() != null 
				&& $store->getBasicAuthPassword() != null) || ($settings->getAuthenticationMethod() == 'oauth1' && $store->getRequestToken() != null)
				|| ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'global')
				|| ($settings->getAuthenticationMethod() == 'oauth2' && $store->getOAuthAccessToken() != null)) {
				require $this->config->plugin;
				
			}
	
			if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt' && $store->getBasicAuthUsername() == null ) {
				require_once $blockPath . '/core/views/basic-auth-login-display.php';
			} else if ($settings->getAuthenticationMethod() == 'oauth1' || $settings->getAuthenticationMethod() == 'oauth2') {
	// 			require_once BASE_PATH . '/views/oauth-logout-display.php'; TODO: Uncomment when OAuth logout has been fixed
			}
			echo '</div>';

			$this->content->text = ob_get_clean();
		}
		return $this->content;
	}

	/**
	 * Block specilization.
	 */
	public function specialization() {
		if (! empty ( $this->config->title )) {
			$this->title = $this->config->title;
		} else {
			$this->config->title = 'Default title ...';
		}
		
		if (empty ( $this->config->text )) {
			$this->config->text = 'Default text ...';
		}
	}
	
	/**
	 * Allow multiple instances of this block.
	 * 
	 * @return boolean		True - we want to allow multiple instances of this
	 * 						block to be used.
	 */
	public function instance_allow_multiple() {
		return true;
	}
	
	/**
	 * Allow global configuration.
	 *
	 * @return boolean		True - we want to allow multiple instances of this
	 * 						block to be used.
	 */
	function has_config() {
		return true;
	}
	
	/**
	 * Loads the specified model.
	 * 
	 * @param string $model			The model name.
	 */
	function loadModel($model) {
		global $CFG;
		$file = $CFG->dirroot . '/blocks/ibmsbt/core/models/' . $model . '.php';
		include_once $file;
	}
}   