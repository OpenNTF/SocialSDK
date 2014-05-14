<?php
/*
 * © Copyright IBM Corp. 2014
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
 * Base widget. All other widgets should extend this class.
 *
 * @author Benjamin Jakobus
 */
class SBTBaseWidget extends WP_Widget {
	
	// The path to the file containing the actual widget code
	private $widget_location;
	
	private $elID = "";
	
	private $endpoint = "connections";

	/**
	 * Constructor.
	 * 
	 * @param string $widget_id
	 * @param string $widget_name
	 * @param string $widget_description
	 */
	public function __construct($widget_id, $widget_name, $widget_description, $widget_location) {
		parent::__construct($widget_id, __($widget_name, 'text_domain'), 
			array('description' => __($widget_description, 'text_domain'),) 
		);
		$this->widget_location = $widget_location;
	}

	/**
	 * Outputs the content of the widget.
	 *
	 * @param array $args
	 * @param array $instance
	 */
	public function widget($args, $instance) {
		$settings = new SBTSettings();
		$store = SBTCredentialStore::getInstance();
 
		if (isset($instance['ibm-sbtk-endpoint'])) {
			$this->endpoint = $instance['ibm-sbtk-endpoint'];
		} else {
			$this->endpoint = "connections";
		}
		
		if (!$this->_isUserLoggedIn() && $settings->requireSignOn($this->endpoint)) {
			
			echo '<div class="widget-area" style="width: 100%;"><aside class="widget widget_recent_entries"><h3 class="widget-title">' . $this->widget_name . '</h3>';
			echo '' . $GLOBALS[LANG]['must_login'] . '</aside></div>';
			return;
		}
		
 		// If tokens exist, make sure that they are valid. Otherwise clear the store and force the
 		// user to re-log

 		if (($settings->getAuthenticationMethod($this->endpoint) == 'oauth1' || 
 				$settings->getAuthenticationMethod($this->endpoint) == 'oauth2') 
 				&& $store->getOAuthAccessToken($this->endpoint) != null) {
 			$endpoint = null;
 			if ($settings->getAuthenticationMethod($this->endpoint) == "oauth2") {
 				$endpoint = new SBTOAuth2Endpoint();
 			} else if ($settings->getAuthenticationMethod($this->endpoint) == "oauth1") {
 				$endpoint = new SBTOAuth1Endpoint();
 			}
 			
 			$service = '/files/basic/api/myuserlibrary/feed';
 			
 			$response = $endpoint->makeRequest($settings->getURL($this->endpoint), $service, 'GET', array(), null, null, $this->endpoint);
 			
 			if ($response->getStatusCode() == 401) {
 				$store->deleteOAuthCredentials($this->endpoint);
 				setcookie('IBMSBTKOAuthLogin', "", $timestamp - 604800);
 				require BASE_PATH . '/core/views/oauth-login-display.php';
 			}
 		}		
 	
		echo '<div name="ibm_sbtk_widget" class="widget-area" style="width:100%"><aside class="widget widget_recent_entries">';
		echo '<h3 class="widget-title">' . $this->widget_name . '</h3>';
		
		if (($settings->getAuthenticationMethod($this->endpoint) == 'oauth1' || $settings->getAuthenticationMethod($this->endpoint) == 'oauth2') 
			&& $store->getOAuthAccessToken($this->endpoint) == null &&
		(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes') && !$this->_isUserLoggedIn()) {
			require BASE_PATH . '/core/views/oauth-login-display.php';
			echo '</aside></div>';
			return;
		}
		
		$plugin = new SBTPlugin($this->endpoint);

		if (($settings->getAuthenticationMethod($this->endpoint) == 'basic' && $store->getBasicAuthUsername($this->endpoint) != null 
			&& $store->getBasicAuthPassword($this->endpoint) != null) || ($settings->getAuthenticationMethod($this->endpoint) == 'oauth1' && $store->getRequestToken($this->endpoint) != null)
			|| ($settings->getAuthenticationMethod($this->endpoint) == 'basic' && $settings->getBasicAuthMethod($this->endpoint) == 'global')
			|| ($settings->getAuthenticationMethod($this->endpoint) == 'oauth2' && $store->getOAuthAccessToken($this->endpoint) != null)) {
			require $this->widget_location;
		}

		if ($settings->getAuthenticationMethod($this->endpoint) == 'basic' && $settings->getBasicAuthMethod($this->endpoint) == 'prompt'
			&& $store->getBasicAuthPassword($this->endpoint) == null) {
			require_once BASE_PATH . '/views/basic-auth-login-display.php';
		} else if ($settings->getAuthenticationMethod($this->endpoint) == 'oauth1' || $settings->getAuthenticationMethod($this->endpoint) == 'oauth2') {
// 			require_once BASE_PATH . '/views/oauth-logout-display.php'; TODO: Uncomment when OAuth logout has been fixed
		}
		echo '</aside></div>';
	}
	
	/**
	 * Ouputs the options form on admin
	 *
	 * @param array $instance The widget options
	 */
	public function form($instance) {
		if (isset($instance['ibm-sbtk-element-id'])) {
			$this->elID = $instance['ibm-sbtk-element-id'];
		} else {
			$this->elID = "ibm-sbtk-element-" . time();
		}
		
		if (isset($instance['ibm-sbtk-template'])) {
			$template = $instance['ibm-sbtk-template'];
		} else {
			$template = "";
		}
		
		if (isset($instance['ibm-sbtk-endpoint'])) {
			$this->endpoint = $instance['ibm-sbtk-endpoint'];
		} else {
			$this->endpoint = "connections";
		}
	
		?>
				<p>
					<label for="<?php echo $this->get_field_id('ibm-sbtk-element-id'); ?>">ID:<br/><span style="font-size: 10px; color: red;">(For this widget to work, the ID must be unique)</label> 
					<input class="widefat" id="<?php echo $this->get_field_id('ibm-sbtk-element-id'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-element-id'); ?>" type="text" value="<?php echo esc_attr($this->elID); ?>"/>
				</p>
				<p>
					<label for="<?php echo $this->get_field_id('ibm-sbtk-template'); ?>">Template<br/><span style="font-size: 10px;">(path must be relative to <?php echo BASE_PATH; ?>)</span>:</label> 
					<input class="widefat" id="<?php echo $this->get_field_id('ibm-sbtk-template'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-template'); ?>" type="text" value="<?php echo esc_attr($template); ?>"/>
				</p>
				
				<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-endpoint'); ?>"><?php echo $GLOBALS[LANG]['endpoint']?>:</label> 
				<select id="<?php echo $this->get_field_id('ibm-sbtk-endpoint'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-endpoint'); ?>">
					<?php 
						$settings = new SBTSettings();
						$endpoints = $settings->getEndpoints();
						foreach ($endpoints as $ep) {
							echo '<option ' . ($ep['name'] == $this->endpoint ? 'selected="selected"' : '') . ' value="' . $ep['name'] . '">' . $ep['name'] . '</option>';
						}
					?>
				</select>
			</p>
			<?php 
		}
			
		/**
		 * Processing widget options on save
		 *
		 * @param array $new_instance The new options
	     * @param array $old_instance The previous options
		 */
		public function update($new_instance, $old_instance) {
			$instance = array();
			$instance['ibm-sbtk-element-id'] = (!empty($new_instance['ibm-sbtk-element-id'])) ? strip_tags($new_instance['ibm-sbtk-element-id'] ) : '';
			$instance['ibm-sbtk-template'] = (!empty($new_instance['ibm-sbtk-template'])) ? strip_tags($new_instance['ibm-sbtk-template'] ) : '';
			$instance['ibm-sbtk-endpoint'] = (!empty($new_instance['ibm-sbtk-endpoint'])) ? strip_tags($new_instance['ibm-sbtk-endpoint'] ) : '';
			return $instance;
		}
		
		private function _isUserLoggedIn() {
			$data = SBTCookie::get(WP_SESSION_INDICATOR);
			$this->userID  = $data;
		
			return $this->userID > 0 || is_user_logged_in();
		}

}
