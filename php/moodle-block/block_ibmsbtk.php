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
class block_ibmsbtk extends block_base {
	
	/**
	 * Init.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function init() {
		$this->title = get_string('ibmsbtk', 'block_ibmsbtk');
	}
	
	/**
	 * Returns the block contents.
	 * 
	 * @return stdClass		Block content.
	 * @author Benjamin Jakobus
	 */
	public function get_content() {
		if ($this->content !== null) {
			return $this->content;
		}	

		$this->content = new stdClass();
	
		if (!isset($this->config->type) || $this->config->type == '') {
			return $this->content;
		}
		
		if (isset($this->config->type)) {
			ob_start();
			$sbtkPlugin = __DIR__ . '/core/samples/header-loader-sample.php';
			$oAuthLoginDisplay = __DIR__ . '/core/views/oauth-login-display.php';
			
			if (!defined('BASE_LOCATION')) {
				 $autoload = __DIR__ . '/core/autoload.php';
				 include $autoload;
			}
			
			$this->loadModel('SBTKSettings');
			$this->loadModel('CredentialStore');

			$store = new CredentialStore();
			$settings = new SBTKSettings();
			
			if ($settings->getAuthenticationMethod() != 'oauth1' || $store->getOAuthAccessToken() != null || 
				(isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] == 'yes')) {
				if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt') {
					$basicAuthLoginDisplay = __DIR__ . '/core/views/basic-auth-login-display.php';
					require $sbtkPlugin;
					echo '<div class="ibmsbtk-widget" style="display: none;">';
					echo $this->config->customHTML['text'];
					echo '<script type="text/javascript">' . $this->config->customCode['text'] . '</script>';
					echo '</div>';
					require $basicAuthLoginDisplay;
				} else {
					require $sbtkPlugin;
					echo $this->config->customHTML['text'];
					echo '<script type="text/javascript">' . $this->config->customCode['text'] . '</script>';
				}
			} else {
				require $oAuthLoginDisplay;  
			}	
			$this->content->text = ob_get_clean();
		}
		return $this->content;
	}

	/**
	 * Block specilization.
	 * 
	 * @author Benjamin Jakobus
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
	 * 
	 * @author Benjamin Jakobus
	 */
	public function instance_allow_multiple() {
		return true;
	}
	
	/**
	 * Allow global configuration.
	 *
	 * @return boolean		True - we want to allow multiple instances of this
	 * 						block to be used.
	 *
	 * @author Benjamin Jakobus
	 */
	function has_config() {
		return true;
	}
	
	/**
	 * Loads the specified model.
	 * 
	 * @param string $model			The model name.
	 * 
	 * @author Benjamin Jakobus
	 */
	function loadModel($model) {
		$file = __DIR__ . '/core/models/' . $className . '.php';
		@include_once $file;
	}
}   