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
 * The settings page for configuring the SBTK plugin.
 * 
 * @author Benjamin Jakobus
 */

class SBTKPluginSettings extends BasePluginController
{
	// The name of the settings page
	private $settingsPage = 'sbtk-sdk-settings-page';
	
	// Settings section ID
	private $settingSectionId = 'setting_section_id';
	
	// Name of the plugin settings section
	private $pluginSettingsSection = 'sbtk-plugin-settings';
	
	// Name of the "about" section
	private $aboutSection = 'sbtk-plugin-settings';
	
	// Holds the values to be used in the fields callbacks. This array takes the form of
	// [key] ===> [value]
 	// whereby value is a json-encoded array holding endpoint information. Each value represents one endpoint.
	private $options;
	
	// Associative array holding plugin configuration information
	private $pluginSettings;
	
	// Holds the values to be used in the fields callbacks. This array takes the form of
	// [key] ===> [value]
	// whereby value is a json-encoded array holding sdk setting information. Unlike with $options, a value is not
	// a serialized array.
	private $sdkSettings;
	
	private $pluginAboutKey = 'sbtk_about';
	
	// Tab definitions
	private $pluginSettingsTabs = array();
	
	// Community grid settings
	private $communityGridSettings = array();
	
	// Custom plugins
	private $customPlugins = array();

	/**
	 * Constructor.
	 * 
	 * @param string $showUpdateMessage		True if the "settings updated" message is to be displayed; false if not.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function __construct($showUpdateMessage = false) {
		$this->pluginSettingsTabs = array($this->settingsPage => 'Endpoint', $this->pluginSettingsSection => 'Plugin');
		
		$this->showUpdateMessage = $showUpdateMessage;
		
		// Add actions for creating the options page
		add_action('admin_menu', array($this, 'addPluginPage'));
		add_action('admin_init', array($this, 'pageInit'));
	}

	/**
	 * Creates the options page with the menu entry "SBTK Settings" (which will
	 * appear under the "Settings" tab in the wordpress admin panel).
	 * 
	 * @author Benjamin Jakobus
	 */
	public function addPluginPage() {
		// This page will be under "Settings"
		add_options_page(
			'Settings Admin',
			'SBTK Settings',
			'manage_options',
			$this->settingsPage,
			array( $this, 'createSettingsPage' )
		);
	}

	/**
	 * Options page callback. Generates HTML for the settings page.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function createSettingsPage() {
		$tab = isset($_GET['tab']) ? $_GET['tab'] : $this->settingsPage;
		
		// Display success message if adequate 
		if ($this->showUpdateMessage) {
			print '<div id="setting-error-settings_updated" class="updated settings-error">';
			print '<p><strong>Settings saved.</strong></p></div>';
		}

		// Generate HTML
		// Set class property
		$this->options = get_option('my_endpoints');
		
		$this->communityGridSettings = get_option('community_grid_settings');
		
		$this->customPlugins = get_option('custom_plugins');
		
		$this->pluginSettings = get_option('my_plugin_settings');
		
		$this->sdkSettings = get_option('sbt_sdk_settings');
		?>
        
        <div class="wrap">
        	<?php $this->pluginOptionsTabs(); ?>          
            <form method="post" action="#">
            <?php
                settings_fields($tab);
                do_settings_sections($tab);
     
                if ($tab != 'sbtk_about') {
                	submit_button(); 
                }
            ?>
            </form>
        </div>
        <?php
    }
    
   
    
    /**
     * Register and add settings to the page.
     * 
     * @author Benjamin Jakobus
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
        
        // Register community grid settings
        register_setting(
        	'community_grid_settings_group', // Option group
        	'community_grid_settings', // Option name
        	array() // Sanitize
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
        	'Plugin Settings', // Title
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
        
        // Create fields for the plugin configuration
        $this->_initPluginOptionsFields();
    }
    
    
    /**
     * Registers the fields for configuring the plugin.
     *
     * @author Benjamin Jakobus
     */
    private function _initPluginOptionsFields() {
    	add_settings_field(
    		'sdk_grid_selection', // ID
    		'', // Title
    		array( $this, 'gridDisplayCallback' ), // Callback
    		$this->pluginSettingsSection, // Page
    		$this->pluginSettingsSection . '_id'// Section
    	);
    	
//     	add_settings_field(
//     		'sdk_pager_display', // ID
//     		', // Title
//     		array( $this, 'pagerDisplayCallback' ), // Callback
//     		$this->pluginSettingsSection, // Page
//     		$this->pluginSettingsSection . '_id'// Section
//     	);
    }
    
    /**
     * Registers the fields for configuring the SDK.
     * 
     * @author Benjamin Jakobus
     */
    private function _initSdkFields() {
    	add_settings_field(
    		'sdk_deploy_dir', // ID
    		'Where is your SDK located?', // Title
    		array( $this, 'sdkDeployCallback' ), // Callback
    		$this->settingsPage, // Page
    		'sdk_setting_section_id' // Section
    	);
    }
    
    /**
     * Registers the fields for configuring endpoints.
     *
     * @author Benjamin Jakobus
     */
    private function _initEndpointFields() {
    	// Add fields
    	add_settings_field(
    		'endpoint_list', // ID
    		'What server do you want to connect to?', // Title
    		array( $this, 'endpointListCallback' ), // Callback
    		$this->settingsPage, // Page
    		$this->settingSectionId // Section
    	);
    	
    	add_settings_field(
    		'authentication_method',
    		'What authentication method do you want to use?</span>',
    		array( $this, 'authenticationMethodCallback' ),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
    		'endpoint_name',
    		'<span style="display: none;" id="lb_endpoint_name">Endpoint name</span>',
    		array( $this, 'endpointNameCallback' ),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
    		'endpoint_url',
    		'<span style="display: none;" id="lb_endpoint_url">Endpoint URL</span>',
    		array( $this, 'endpointUrlCallback' ),
    		$this->settingsPage,
    		$this->settingSectionId
    	);

    	add_settings_field(
    		'consumer_key',
    		'<span style="display: none;" id="lb_consumer_key">Consumer Key</span>',
    		array( $this, 'consumerKeyCallback' ),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
    		'consumer_secret',
    		'<span style="display: none;" id="lb_consumer_secret">Consumer Secret</span>',
    		array( $this, 'consumerSecretCallback' ),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
    		'authorization_url',
    		'<span style="display: none;" id="lb_authorization_url">Authorization URL</span>',
    		array( $this, 'authorizationUrlCallback' ),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
    		'request_token_url',
    		'<span style="display: none;" id="lb_request_token_url">Request token URL</span>',
    		array($this, 'requestTokenUrlCallback'),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
    		'access_token_url',
    		'<span style="display: none;" id="lb_access_token_url">Access token URL</span>',
    		array($this, 'accessTokenUrlCallback'),
    		$this->settingsPage,
    		$this->settingSectionId
    	);
    	
    	add_settings_field(
	    	'basic_auth_username',
	    	'<span style="display: none;" id="lb_basic_auth_username">Username</span>',
	    	array( $this, 'basicAuthCallback' ),
	    	$this->settingsPage,
	    	$this->settingSectionId
    	);
    	
    	add_settings_field(
	    	'basic_auth_password',
	    	'<span style="display: none;" id="lb_basic_auth_password">Password</span>',
	    	array( $this, 'basicAuthPasswordCallback' ),
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
        
        // TODO: sanitize
//         if( isset( $input['id_number'] ) )
//             $new_input['id_number'] = absint( $input['id_number'] );

//         if( isset( $input['title'] ) )
//             $new_input['title'] = sanitize_text_field( $input['title'] );
		$new_input = $input;
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
     *
     * @author Benjamin Jakobus
     */
    private function _newEndpointButton() {
    	$this->loadView('wp-settings/newEndpointButton', array());
    }
    
    /**
     * Outputs HTML for the "Delete Endpoint" button.
     *
     * @author Benjamin Jakobus
     */
    private function _deleteEndpointButton() {
    	$this->loadView('wp-settings/deleteEndpointButton', array());
    }
    
    /**
     * Outputs HTML for the "Default Values" link needed to populate the options entries with the default values
     * for IBM SmartCloud.
     *
     * @author Benjamin Jakobus
     */
    private function _defaultSmartcloudValuesButton() {
    	$this->loadView('wp-settings/defaultSmartcloudValuesButton', array());
    }
    
    /**
     * Outputs HTML for the "Default Values" link needed to populate the SDK deploy URL entry with a default value.
     *
     * @author Benjamin Jakobus
     */
    private function _defaultSdkDeployUrlButton() {
    	$this->loadView('wp-settings/defaultSdkDeployUrlButton', array());
    }
    
    /**
     * Outputs HTML for the "Default Values" link needed to populate the options entries with the default values
     * for IBM Connections.
     *
     * @author Benjamin Jakobus
     */
    private function _defaultConnectionsValuesButton() {
    	$this->loadView('wp-settings/defaultConnectionsValuesButton', array());
    }
    
    
    /**
     * Print HTML for hidden settings. These hidden fields are accessed by JavaScript to populate
     * settings fields when changing the endpoint selection.
     *
     * @author Benjamin Jakobus
     */
    public function printHiddenSettings() {
    	$viewData['options'] = $this->options;
    	$viewData['default_sdk_deploy_url'] = plugins_url('sbtk-wp') . '/system/libs/js-sdk/';
    	$this->loadView('wp-settings/hiddenSettings', $viewData);
    }
    
    
    /**
     * Prints and populates (if possile) the endpoint name entry field.
     * 
     * @author Benjamin Jakobus
     */
    public function endpointNameCallback() {
    	// Display first option by default
    	if (sizeof($this->options) <= 0) {
    		print('<input style="display: none;" size="50" type="text" id="endpoint_name" name="endpoint_name" value="" />');
    	}
    	reset($this->options);
    	$first_key = key($this->options);
    	$endpoint = (array)json_decode($this->options[$first_key], true);
    	$viewData['endpointName'] =  (isset( $endpoint['name'] ) ? esc_attr( $endpoint['name']) : '');
    	$this->loadView('wp-settings/endpointName', $viewData);
    }
    
    /**
     * Prints and populates a list of available endpoints.
     *
     * @author Benjamin Jakobus
     */
    public function endpointListCallback() {
    	// Determine the selected endpoint by the user
    	$selected_endpoint = "";
    	foreach ($this->options as $val) {
    		$endpoint = (array)json_decode($val, true);
			
			if ($endpoint['selected']) {
				$selected_endpoint = $endpoint['name'];
				break;
			} 
    	}
    	$viewData['selected_endpoint'] = $selected_endpoint;
    	$this->loadView('wp-settings/endpointList', $viewData);
    	// Uncomment to enable custom endpoint creation
//     	if (isset($this->options)) {
// 			foreach ($this->options as $val) {
// 				$endpoint = (array)json_decode($val, true);
// 				$endpoint_name = $endpoint['name'];
// 				print '<option value="' . $endpoint_name . '">' . $endpoint_name . '</option>';
// 			}
// 		}
    	// Uncomment to enable custom endpoint creation
//     	$this->_newEndpointButton();
//     	$this->_deleteEndpointButton();
    }

    /**
     * Prints the endpoint URL entry field and buttons for filling out entry fields with the default values.
     * 
     * @author Benjamin Jakobus
     */
    public function endpointUrlCallback() {
    	$this->_defaultSmartcloudValuesButton();
    	$this->_defaultConnectionsValuesButton();
    	$this->loadView('wp-settings/endpointUrl', array());
    }
    
    /**
     * Prints the endpoint consumer key entry field for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function consumerKeyCallback() {
   		$this->loadView('wp-settings/consumerKey', array());
    }
    
    /**
     * Prints the SDK deploy URL entry field and button for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function sdkDeployCallback() {
		$this->_defaultSdkDeployUrlButton();
    	$viewData['sdk_deploy_url'] = (isset($this->sdkSettings['sdk_deploy_url']) ? $this->sdkSettings['sdk_deploy_url'] : '');

		$this->loadView('wp-settings/sdkDeploy', $viewData);
    }
    
    /**
     * Prints the consumer secret entry field and buttons for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function consumerSecretCallback() {
    	$this->loadView('wp-settings/consumerSecret', array());
    }
    
    /**
     * Prints the basic user authentication entry field and buttons for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function basicAuthCallback() {
    	$this->loadView('wp-settings/basicAuthUsername', array());
    }
    
    /**
     * Prints the basic user authentication entry field and buttons for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function basicAuthPasswordCallback() {
    	$this->loadView('wp-settings/basicAuthPassword', array());
    }
    
    /**
     * Prints the authorization URL entry field and buttons for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function authorizationUrlCallback() {
    	$this->loadView('wp-settings/authorizationUrl', array());
    }
    
    /**
     * Prints the access token URL entry field and buttons for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function accessTokenUrlCallback() {
    	$this->loadView('wp-settings/accessTokenUrl', array());
    }
    
    /**
     * Prints and populates a list for displaying the available autentication methods. 
     *
     * @author Benjamin Jakobus
     */
    public function authenticationMethodCallback() {
    	reset($this->options);
    	$first_key = key($this->options);
    	$endpoint = (array)json_decode($this->options[$first_key], true);
    	$viewData['authentication_method'] = $endpoint['authentication_method'];
    	$this->loadView('wp-settings/authenticationMethod', $viewData);
    }
    
    /**
     * Prints a radio button group allowing the user to select the type of grid he/she would
     * like to display.
     *
     * @author Benjamin Jakobus
     */
    public function gridDisplayCallback() {   
// 		$viewData['bookmark_grid_pager'] = $this->communityGridSettings['bookmark_grid_pager'];
// 		$viewData['bookmark_grid_sorter'] = $this->communityGridSettings['bookmark_grid_sorter'];
// 		$viewData['bookmark_grid_container_type'] = $this->communityGridSettings['bookmark_grid_container_type'];
// 		$this->loadView('wp-settings/plugin/bookmarkGridDisplay', $viewData);

// 		$viewData['community_grid_pager'] = $this->communityGridSettings['community_grid_pager'];
// 		$viewData['community_grid_sorter'] = $this->communityGridSettings['community_grid_sorter'];
// 		$viewData['community_grid_container_type'] = $this->communityGridSettings['community_grid_container_type'];
// 		$this->loadView('wp-settings/plugin/communityGridDisplay', $viewData);
		
		$viewData['plugins'] = $this->customPlugins;
		
		if (isset($this->customPlugins) && $this->customPlugins != null) {
			foreach ($this->customPlugins as $plugin) {
				$viewData[$plugin] = get_option($plugin);
			}
		}
		
		$this->loadView('wp-settings/plugin/customPluginEditor', $viewData);
		$this->loadView('wp-settings/plugin/hiddenPlugins', $viewData);
    }
    
    /**
     * Prints a radio button group allowing the user to indicate whether or not to display a pager.
     *
     * @author Benjamin Jakobus
     */
    public function pagerDisplayCallback() {    	
		$viewData['pager_group'] = $this->pluginSettings['pager_group'];
    	$this->loadView('wp-settings/pagerDisplay', $viewData);
    }
    
    /**
     * Prints the request token URL entry field and buttons for filling out entry fields with the default values.
     *
     * @author Benjamin Jakobus
     */
    public function requestTokenUrlCallback() {
    	$this->loadView('wp-settings/requestTokenUrl', array());
    }
    
    /**
     * Renders the tab.
     * 
     * @author Benjamin Jakobus
     */
    function pluginOptionsTabs() {
		$viewData['settings_tabs'] = $this->pluginSettingsTabs;
    	$viewData['current_tab'] = isset( $_GET['tab'] ) ? $_GET['tab'] : $this->settingsPage;
    	$viewData['settings_page'] = $this->settingsPage;
    	$this->loadView('wp-settings/pluginOptionsTabs', $viewData);
    }
}