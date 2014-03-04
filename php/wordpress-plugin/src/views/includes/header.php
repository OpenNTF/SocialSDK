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
 * @author Benjamin Jakobus
 */

$agnostic_deploy_url = str_replace('http://', '//', $deploy_url);
?>
<script type="text/javascript">
	var djConfig = {
	baseUrl: "***REMOVED*** echo $agnostic_deploy_url; ?>",
	parseOnLoad: true,
	locale: "en"
};

</script>


***REMOVED*** 
	if ($js_library != 'none') {
		switch ($js_library) {
			case 'Dojo Toolkit 1.4.3':
				echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js"></script>';
				echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/resources/dojo.css">';
				break;
			case 'Dojo Toolkit 1.5.2':
				echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dojo/dojo.js"></script>';
				echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dijit/themes/claro/claro.css">';
				break;
				
			case 'Dojo Toolkit 1.6.1':
				echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/dojo.js"></script>';
				echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dijit/themes/claro/claro.css">';
				break;
					
			case 'Dojo Toolkit 1.7.4':
				echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/dojo.js"></script>';
				echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dijit/themes/claro/claro.css">';
				break;
						
			case 'Dojo Toolkit 1.8.4':
				echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dojo/dojo.js"></script>';
				echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dijit/themes/claro/claro.css">';
				break;
							
			case 'Dojo Toolkit 1.9.0':
				echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dojo/dojo.js"></script>';
				echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dijit/themes/claro/claro.css">';
				break;
								
			case 'JQuery 1.8.3':
				echo '<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>';
				break;
				
				case 'Dojo Toolkit 1.4.3 uncompressed':
					echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js.uncompressed.js"></script>';
					echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/resources/dojo.css">';
					break;
				case 'Dojo Toolkit 1.5.2 uncompressed':
					echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dojo/dojo.js.uncompressed.js"></script>';
					echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.5.2/dijit/themes/claro/claro.css">';
					break;
				
				case 'Dojo Toolkit 1.6.1 uncompressed':
					echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/dojo.js.uncompressed.js"></script>';
					echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.6.1/dijit/themes/claro/claro.css">';
					break;
						
				case 'Dojo Toolkit 1.7.4 uncompressed':
					echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/dojo.js.uncompressed.js"></script>';
					echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.7.4/dijit/themes/claro/claro.css">';
					break;
				
				case 'Dojo Toolkit 1.8.4 uncompressed':
					echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dojo/dojo.js.uncompressed.js"></script>';
					echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.4/dijit/themes/claro/claro.css">';
					break;
						
				case 'Dojo Toolkit 1.9.0 uncompressed':
					echo '<script src="//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dojo/dojo.js.uncompressed.js"></script>';
					echo '<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dojo/resources/dojo.css">
					  <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/dojo/1.9.0/dijit/themes/claro/claro.css">';
					break;
				
				case 'JQuery 1.8.3 uncompressed':
					echo '<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js.uncompressed.js"></script>';
					break;
		}
	}
?>


<script>
	if (typeof _sbt == "undefined" || window._sbt_bridge_compat) {
		_sbt = 0;
		require({
			paths: {
				"sbt": "***REMOVED*** echo $agnostic_deploy_url; ?>/js/sdk/sbt"
			}
		});
		require({
			paths: {
				"sbt/_bridge": "***REMOVED*** echo $agnostic_deploy_url; ?>/js/sdk/_bridges/dojo-amd"
			}
		});
		require({
			paths: {
				"sbt/widget": "***REMOVED*** echo $agnostic_deploy_url; ?>/js/sdk/dojo2"
			}
		});

		define("sbt/config", ["sbt/Proxy", "sbt/_bridge/Transport", "sbt/authenticator/Basic", "sbt/Endpoint", "sbt/ErrorTransport", "sbt/authenticator/OAuth"], 
			function (Proxy, Transport, Basic, Endpoint, ErrorTransport, OAuth) {
				var sbt = {};
				sbt.Properties = {
					"libraryUrl": "",
					"serviceUrl": "***REMOVED*** echo plugins_url(PLUGIN_NAME); ?>/core/index.php?classpath=services&class=Proxy&method=fileOperations&_redirectUrl=",
					"sbtUrl": "***REMOVED*** echo $agnostic_deploy_url; ?>/js/sdk"
				};
				sbt.Endpoints = {
						***REMOVED*** 
							if ($allow_client_access) {
								echo generateEndpoint($type, $authentication_method, $url, $agnostic_deploy_url, $name, $api_version); 
							}
						?>
				};
				sbt.findEndpoint = function (endpointName) {
					return this.Endpoints[endpointName];
				};
			return sbt;
		});
	};
</script>
</head>
***REMOVED*** 


/**
 * Decides on an endpoint to use and generates the JavaScript for it.
 *
 * @return string			The JavaScript representing an endpoint.
 * @author Benjamin Jakobus
*/
function generateEndpoint($type,$authentication_method, $url, $deploy_url, $name, $api_version) {
	//TODO support list of endpoint and aliases
	return generateConnectionsEndpoint($type,$authentication_method, $url, $deploy_url, $api_version);
}

/**
 * Generates the JavaScript for an IBM Connections endpoint.
 *
 * @return string		The JavaScript representing an IBM Connections endpoint.
 * @author Benjamin Jakobus
 */

function generateConnectionsEndpoint($type, $authentication_method, $url, $deploy_url, $api_version) {
	$endpoint_js = '"connections": new Endpoint({';
	
	if ($authentication_method == 'oauth1' || $authentication_method == 'oauth2') {
		$endpoint_js .= '"authType": "oauth",';
	} else {
		$endpoint_js .= '"authType": "basic",';
	}
	
	if ($api_version != "") {
		$endpoint_js .= '"apiVersion": "' . $api_version . '",';
	}
	
	$endpoint_js .= '"platform": "' . $type . '",';
	
	if ($authentication_method == 'oauth1') {
		$endpoint_js .= '"authenticator": new OAuth({"loginUi": null,
				"url": "' . $deploy_url . '"}),';
	} else if ($authentication_method == 'basic') {
		$endpoint_js .= '"authenticator": new Basic({';
		$endpoint_js .= '"url": "' . $deploy_url . '", "actionUrl": "' . plugins_url(PLUGIN_NAME) . '/core/index.php?classpath=services&class=Proxy&method=route&basicAuthRequest=true&_redirectUrl=' . getCurrentPage() . '"}),';
	}
	$endpoint_js .= '"proxyPath": "connections",';
// 	$endpoint_js .= '"proxyPath": "' . $type . '",';
	if ($type == 'smartcloud') {
		$endpoint_js .= '"isSmartCloud": true,';
	}
	$endpoint_js .= '"isAuthenticated": "false",';
	$endpoint_js .= '"transport": new Transport({}),';
	$endpoint_js .= '"serviceMappings": {},';
	$endpoint_js .= '"name": "' . $type . '",';
	
	if ($type == 'smartcloud') {
		$endpoint_js .= '"authenticationErrorCode": "403",';
	} else {
		$endpoint_js .= '"authenticationErrorCode": "401",';
	}
	$endpoint_js .= '"baseUrl": "' . $url . '",';

	$endpoint_js .=	'"proxy": new Proxy({';
	$endpoint_js .=	'"proxyUrl": "' . plugins_url(PLUGIN_NAME) . '/core/index.php?classpath=services&class=Proxy&method=route&_redirectUrl="})}),';

	return $endpoint_js;
}

/**
 * Returns the URL of the page that the user is currently on.
 *
 * @return string	The URL of the page that the user is currently on.
 * @author Benjamin Jakobus
 */
function getCurrentPage() {
	$protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";
	$currentPage = $protocol . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'];
	return $currentPage;
}

