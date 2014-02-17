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
class SBTKBaseWidget extends WP_Widget {
	
	// The path to the file containing the actual widget code
	private $widget_location;

	/**
	 * Constructor.
	 * 
	 * @param string $widget_id
	 * @param string $widget_name
	 * @param string $widget_description
	 * 
	 * @author Benjamin Jakobus
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
	 * 
	 * @author Benjamin Jakobus
	 */
	public function widget($args, $instance) {
		$settings = new SBTKSettings();
		$store = new CredentialStore();

		if ($settings->getAuthenticationMethod() == 'oauth1' && $store->getOAuthAccessToken() == null &&
		(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
			require BASE_PATH . '/core/views/oauth-login-display.php';
			return;
		}

		if (($settings->getAuthenticationMethod() == 'basic' && $store->getBasicAuthUsername() != null 
			&& $store->getBasicAuthPassword() != null) || ($settings->getAuthenticationMethod() == 'oauth1' && $store->getOAuthAccessToken() != null)) {
			require $this->widget_location;
		}

		if ($settings->getAuthenticationMethod() == 'basic' && $settings->getBasicAuthMethod() == 'prompt') {
			require_once BASE_PATH . '/views/basic-auth-login-display.php';
		}
	}
	
	/**
	 * Ouputs the options form on admin
	 *
	 * @param array $instance The widget options
	 */
	public function form($instance) {
		if (isset($instance['ibm-sbtk-element-id'])) {
			$elID = $instance['ibm-sbtk-element-id'];
		} else {
			$elID = "myIBMElementID";
		}

		if (isset($instance['ibm-sbtk-integrate'])) {
			$integrate = $instance['ibm-sbtk-integrate'];
		} else {
			$integrate = true;
		}
	
		?>
				<p>
					<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-element-id'); ?>">ID:</label> 
					<input class="widefat" id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-element-id'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-element-id'); ?>" type="text" value="***REMOVED*** echo esc_attr($elID); ?>"/>
				</p>
				<p>
					<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-integrate'); ?>">***REMOVED*** echo $GLOBALS[LANG]['integrate-with-wp']?>:</label> 
					<input ***REMOVED*** echo ($integrate ? 'checked="checked"' : ''); ?> id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-integrate'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-integrate'); ?>" type="checkbox" value="integrate" />
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
			$instance['ibm-sbtk-integrate'] = (!empty($new_instance['ibm-sbtk-integrate'])) ? strip_tags($new_instance['ibm-sbtk-integrate'] ) : '';
			return $instance;
		}

}