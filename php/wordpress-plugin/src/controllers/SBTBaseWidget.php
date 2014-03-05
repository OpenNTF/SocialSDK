***REMOVED***
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
 
 		// If tokens exist, make sure that they are valid. Otherwise clear the store and force the
 		// user to re-log
 		
 		if (($settings->getAuthenticationMethod() == 'oauth1' || $settings->getAuthenticationMethod() == 'oauth2') && $store->getOAuthAccessToken() != null) {
 			$endpoint = null;
 			if ($settings->getAuthenticationMethod() == "oauth2") {
 				$endpoint = new SBTOAuth2Endpoint();
 			} else if ($settings->getAuthenticationMethod() == "oauth1") {
 				$endpoint = new SBTOAuth1Endpoint();
 			}
 			
 			$service = '/files/basic/api/myuserlibrary/feed';
 			
 			$response = $endpoint->makeRequest($settings->getURL(), $service, 'GET', null, null);
 			if ($response->getStatusCode() == 401) {
 				$store->deleteOAuthCredentials();
 				setcookie('IBMSBTKOAuthLogin', "", $timestamp - 86400);
 			}
 		}		
		echo '<div name="ibm_sbtk_widget">';
		if (($settings->getAuthenticationMethod() == 'oauth1' || $settings->getAuthenticationMethod() == 'oauth2') && $store->getOAuthAccessToken() == null &&
		(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
			require BASE_PATH . '/core/views/oauth-login-display.php';
			return;
		}

		if (($settings->getAuthenticationMethod() == 'basic' && $store->getBasicAuthUsername() != null 
			&& $store->getBasicAuthPassword() != null) || ($settings->getAuthenticationMethod() == 'oauth1' && $store->getRequestToken() != null)
			|| ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'global')
			|| ($settings->getAuthenticationMethod() == 'oauth2' && $store->getOAuthAccessToken() != null)) {
			require $this->widget_location;
		}

		if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt') {
			require_once BASE_PATH . '/views/basic-auth-login-display.php';
		} else if ($settings->getAuthenticationMethod() == 'oauth1' || $settings->getAuthenticationMethod() == 'oauth2') {
// 			require_once BASE_PATH . '/views/oauth-logout-display.php'; TODO: Uncomment when OAuth logout has been fixed
		}
		echo '</div>';
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
	
		?>
				<p>
					<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-element-id'); ?>">ID:<br/><span style="font-size: 10px; color: red;">(For this widget to work, the ID must be unique)</label> 
					<input class="widefat" id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-element-id'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-element-id'); ?>" type="text" value="***REMOVED*** echo esc_attr($this->elID); ?>"/>
				</p>
				<p>
					<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-template'); ?>">Template<br/><span style="font-size: 10px;">(path must be relative to ***REMOVED*** echo BASE_PATH; ?>)</span>:</label> 
					<input class="widefat" id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-template'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-template'); ?>" type="text" value="***REMOVED*** echo esc_attr($template); ?>"/>
				</p>
			***REMOVED*** 
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
			return $instance;
		}

}
