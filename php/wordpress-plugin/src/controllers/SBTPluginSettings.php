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
 * The settings page for configuring the SBTK plugin.
 * 
 * @author Benjamin Jakobus
 */

class SBTPluginSettings extends BasePluginController
{
	// The name of the settings page
	private $settingsPage = 'sbtk-sdk-settings-page';
	
	// Settings section ID
	private $settingSectionId = 'setting_section_id';
	
	// Name of the plugin settings section
	private $pluginSettingsSection = 'sbtk-plugin-settings';
	
	// Holds the values to be used in the fields callbacks. This array takes the form of
	// [key] ===> [value]
 	// whereby value is a json-encoded array holding endpoint information. Each value represents one endpoint.
	private $endpoints;
	
	// Associative array holding plugin configuration information
	private $pluginSettings;
	
	// Holds the values to be used in the fields callbacks. This array takes the form of
	// [key] ===> [value]
	// whereby value is a json-encoded array holding sdk setting information. Unlike with $options, a value is not
	// a serialized array.
	private $sdkSettings;
	
	// Tab definitions
	private $pluginSettingsTabs = array();
	
	// JavaScript library to use
	private $jsLibrary = "";

	/**
	 * Constructor.
	 * 
	 * @param string $showUpdateMessage		True if the "settings updated" message is to be displayed; false if not.
	 */
	public function __construct($showUpdateMessage = false) {
		$this->pluginSettingsTabs = array($this->settingsPage => 'Endpoint');
		
		$this->showUpdateMessage = $showUpdateMessage;
	
		// Add actions for creating the options page
		add_action('admin_menu', array($this, 'addPluginPage'));
		add_action('admin_init', array($this, 'pageInit'));
	}

	/**
	 * Creates the options page with the menu entry "SBTK Settings" (which will
	 * appear under the "Settings" tab in the wordpress admin panel).
	 */
	public function addPluginPage() {
		// This page will be under "Settings"
		add_options_page(
			'Settings Admin',
			'IBM Connections Toolkit',
			'manage_options',
			$this->settingsPage,
			array( $this, 'createSettingsPage' )
		);
	}

	/**
	 * Options page callback. Generates HTML for the settings page.
	 */
	public function createSettingsPage() {
		$tab = isset($_GET['tab']) ? $_GET['tab'] : $this->settingsPage;
		
		// Display success message if adequate 
		if ($this->showUpdateMessage) {
			print '<div id="setting-error-settings_updated" class="updated settings-error">';
			print '<p><strong>' . $GLOBALS[LANG]['settings_saved'] . '</strong></p></div>';
		}

		$this->endpoints = get_option(ENDPOINTS);		
		$this->sdkSettings = get_option(SDK_DEPLOY_URL);
		$this->jsLibrary = get_option(JS_LIBRARY);
		
		?>
        
        <div class="wrap">
        	***REMOVED*** $this->pluginOptionsTabs(); ?>          
            <form method="post" action="#">
            ***REMOVED***
                settings_fields($tab);
                do_settings_sections($tab);
                submit_button();
            ?>
            </form>
        </div>
        ***REMOVED***
    }
   
    /**
     * Register and add settings to the page.
     */
    public function pageInit() {        
   
    	// Register the settings group
        register_setting(
            'my_option_group', // Option group
            'my_endpoints', // Option name
            array( $this, 'sanitize' ) // Sanitize
        );
        
        // Register plugin settings
        register_setting(
        	'my_plugin_settings_group', // Option group
        	'my_plugin_settings', // Option name
        	array( $this, 'sanitize' ) // Sanitize
        );
        
        // Create SDK settings section
        add_settings_section(
        	'sdk_setting_section_id', // ID
        	'SDK definition', // Title
        	array( $this, 'printSdkSectionInfo' ), // Callback
        	$this->settingsPage // Page
        );
        
        add_settings_section(
        	$this->pluginSettingsSection . '_id', // ID
        	'Widget Settings', // Title
        	array( $this, 'printSdkSectionInfo' ), // Callback
       	 	$this->pluginSettingsSection // Page
        );

        // Create endpoint settings section
        add_settings_section(
        	$this->settingSectionId, // ID
        	'Environment definition', // Title
       	 	array( $this, 'printSectionInfo' ), // Callback
        	$this->settingsPage // Page
        );

        // Create fields for endpoint settings
        $this->_initEndpointFields();
        
        // Create fields for sdk settings
        $this->_initSdkFields();
    }
    
    /**
     * Registers the fields for configuring the SDK.
     */
    private function _initSdkFields() {
    	add_settings_field(
    		'sdk_deploy_dir', // ID
    		$GLOBALS[LANG]['sdk_location'], // Title
    		array( $this, 'sdkDeployCallback' ), // Callback
    		$this->settingsPage, // Page
    		'sdk_setting_section_id' // Section
    	);
    	
    	add_settings_field(
    		'js_library', // ID
    		$GLOBALS[LANG]['js_library'], // Title
    		array( $this, 'jsLibraryCallback' ), // Callback
    		$this->settingsPage, // Page
    		'sdk_setting_section_id' // Section
    	);
    }
    
    /**
     * Registers the fields for configuring endpoints.
     */
    private function _initEndpointFields() {
    	// Add fields
    	add_settings_field(
    		'endpoint_list', // ID
    		$GLOBALS[LANG]['server_connection'], // Title
    		array( $this, 'endpointListCallback' ), // Callback
    		$this->settingsPage, // Page
    		$this->settingSectionId // Section
    	);
    	
    	add_settings_field(
    		'hidden_endpoint_fields',
    		'',
    		array( $this, 'hiddenEndpointFieldsCallback' ),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
    		'hidden_settings',
    		'',
    		array($this, 'printHiddenSettings'),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    }


    /**
     * Sanitize each setting field as needed
     *
     * @param array $input Contains all settings fields as array keys.
     */
    public function sanitize($input) {
        $new_input = array();

        foreach ($input as $i) {
        	array_push($new_input, sanitize_text_field($i));
        }
        
        // TODO: check for ssl trust and url

        return $new_input;
    }

    /** 
     * Prints section description.
     */
    public function printSectionInfo() {
//         print 'Enter your settings below:';
    }
    
    /**
     * Prints SDK section description.
     */
    public function printSdkSectionInfo() {
// 		print 'SDK configuration info';
    }

  
    //======================================
    // Callback functions for generating the settings fields
    //======================================
    /**
     * Outputs HTML for the "New Endpoint" button.
     */
    private function _newEndpointButton() {
    	$this->loadView('wp-settings/newEndpointButton', array());
    }
    
    /**
     * Outputs HTML for the "Delete Endpoint" button.
     */
    private function _deleteEndpointButton() {
    	$this->loadView('wp-settings/deleteEndpointButton', array());
    }
    
    /**
     * Outputs HTML for the "Edit Endpoint" button.
     */
    private function _editEndpointButton() {
    	$this->loadView('wp-settings/editEndpointButton', array());
    }
   
    /**
     * Outputs HTML for the "Default Values" link needed to populate the SDK deploy URL entry with a default value.
     */
    private function _defaultSdkDeployUrlButton() {
    	$this->loadView('wp-settings/defaultSdkDeployUrlButton', array());
    }
    
    /**
     * Print HTML for hidden settings. These hidden fields are accessed by JavaScript to populate
     * settings fields when changing the endpoint selection.
     */
    public function printHiddenSettings() {
    	$viewData['options'] = $this->endpoints;
    	$this->loadView('wp-settings/hiddenSettings', $viewData);
    }
    
    /**
     * Prints and populates the hidden endpoint entries.
     */
    public function hiddenEndpointFieldsCallback() {
    	$viewData = array();
    	if ($this->endpoints) {
    		reset($this->endpoints);
    		$first_key = key($this->endpoints);
    		$endpoint = (array)json_decode($this->endpoints[$first_key], true);
    		$viewData['endpointName'] =  (isset($endpoint['name']) ? esc_attr($endpoint['name']) : "");
    		$viewData['authentication_method'] = $endpoint['authentication_method'];

    	}
    	
    	$ssl_trust = false;
    	$clientAccess = true;
    	$server_type = "";
    	$basic_auth_method = "";
    	$endpointVersion = "";
    	if ($this->endpoints) {
    		foreach ($this->endpoints as $val) {
    			$endpoint = (array)json_decode($val, true);
    			if ($endpoint['selected']) {
    				if (isset($endpoint['allow_client_access']) && $endpoint['allow_client_access'] == 'allow_client_access') {
    					$clientAccess = true;
    				} else {
						$clientAccess = false;
					}
    				if (isset($endpoint['force_ssl_trust']) && $endpoint['force_ssl_trust'] == 'force_ssl_trust') {
    					$ssl_trust = true;
    				}
    				if (isset($endpoint['server_type'])) {
    					$server_type = $endpoint['server_type'];
    				}
    				if (isset($endpoint['basic_auth_method'])) {
    					$basic_auth_method = $endpoint['basic_auth_method'];
    				}
    				$endpointVersion = (isset($endpoint['endpoint_version']) ? $endpoint['endpoint_version'] : "");
    			}
    		}
    	}
    	$callbackURL = BASE_LOCATION . '/core/index.php?classpath=endpoint&class=SBTOAuth2Endpoint&method=authenticationCallback';
    	$callbackURL = str_replace('http://', 'https://', $callbackURL);
    	$viewData['callback_url'] = $callbackURL;
    	$viewData['basic_auth_method'] = $basic_auth_method;
    	$viewData['server_type'] = $server_type;
    	$viewData['force_ssl_trust'] = $ssl_trust;
    	$viewData['allow_client_access'] = $clientAccess;
    	$viewData['endpoint_version'] = $endpointVersion; 
    	$this->loadView('wp-settings/hiddenEndpointFields', $viewData);
    }	
    
    /**
     * Prints and populates a list of available endpoints.
     */
    public function endpointListCallback() {
    	// Determine the selected endpoint by the user
    	$selected_endpoint = "";
    	$endpoints = array();

    	if ($this->endpoints) {
	    	foreach ($this->endpoints as $val) {
	    		$endpoint = (array)json_decode($val, true);
				if ($endpoint['selected']) {
					$selected_endpoint = $endpoint['name'];
				} 
				
				array_push($endpoints, $endpoint);
	    	}
    	}
    	$viewData['selected_endpoint'] = $selected_endpoint;
    	$viewData['endpoints'] = $endpoints;

    	$this->loadView('wp-settings/endpointList', $viewData);
    	
    	$this->_newEndpointButton();
    	$this->_editEndpointButton();
    	$this->_deleteEndpointButton();
    }

    /**
     * Prints the SDK deploy URL entry field and button for filling out entry fields with the default values.
     */
    public function sdkDeployCallback() {
		$this->_defaultSdkDeployUrlButton();
    	$viewData['sdk_deploy_url'] = (isset($this->sdkSettings['sdk_deploy_url']) ? $this->sdkSettings['sdk_deploy_url'] : plugins_url(PLUGIN_NAME) . '/core/system/libs/js-sdk');

		$this->loadView('wp-settings/sdkDeploy', $viewData);
    }
   
    /**
     * Prints select for the available JavaScript libraries.
     */
    public function jsLibraryCallback() {
		$viewData['js_library'] = $this->jsLibrary;
    	$this->loadView('wp-settings/librariesList', $viewData);
    }
    /**
     * Renders the tab.
     */
    function pluginOptionsTabs() {
		$viewData['settings_tabs'] = $this->pluginSettingsTabs;
    	$viewData['current_tab'] = isset( $_GET['tab'] ) ? $_GET['tab'] : $this->settingsPage;
    	$viewData['settings_page'] = $this->settingsPage;
    	$this->loadView('wp-settings/pluginOptionsTabs', $viewData);
    }
}