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
							foreach($endpoints as $endpoint) {
								echo generateEndpoint($endpoint, $agnostic_deploy_url) . "\n";
							}
						?>
				};
				sbt.findEndpoint = function (endpointName) {
// 					console.log(arguments.callee.caller.toString());
					console.log(endpointName);
					console.log(this.Endpoints[endpointName]);
					console.log("====");
					return this.Endpoints[endpointName];
				};
			return sbt;
		});
	};
</script>

***REMOVED*** 

/**
 * Generates the JavaScript for an IBM Connections endpoint.
 *
 * @return string		The JavaScript representing an IBM Connections endpoint.
 * @author Benjamin Jakobus
 */

function generateEndpoint($endpoint, $agnostic_deploy_url) {
// $endpoint['auth_type'], $endpoint['server_url'], $agnostic_deploy_url, $endpoint['name'], $endpoint['api_version']
// $type, $authentication_method, $url, $deploy_url, $name, $api_version
	$endpoint_js = '"' . $endpoint['name'] . '": new Endpoint({';

	if ($endpoint['authentication_method'] == 'oauth1' || $endpoint['authentication_method'] == 'oauth2') {
		$endpoint_js .= '"authType": "oauth",';
	} else {
		$endpoint_js .= '"authType": "basic",';
	}
	
	if ($endpoint['api_version'] != "") {
		$endpoint_js .= '"apiVersion": "' . $endpoint['api_version'] . '",' . "\n";
	}
	
// 	$endpoint_js .= '"platform": "' . $endpoint['server_type'] . '",';
	$endpoint_js .= '"platform": "connections",' . "\n";
	
	if ($endpoint['authentication_method'] == 'oauth1') {
		$endpoint_js .= '"authenticator": new OAuth({"loginUi": null,
				"url": "' . $agnostic_deploy_url . '"}),';
	} else if ($endpoint['authentication_method'] == 'basic') {
		$endpoint_js .= '"authenticator": new Basic({';
		$endpoint_js .= '"url": "' . $agnostic_deploy_url . '", "actionUrl": "' . plugins_url(PLUGIN_NAME) . '/core/index.php?classpath=services&class=Proxy&method=route&endpointName=' . $endpoint['name'] . '&basicAuthRequest=true&_redirectUrl=' . getCurrentPage() . '"}),';
	}
// 	$endpoint_js .= '"proxyPath": "connections",';
	$endpoint_js .= '"proxyPath": "",' . "\n";
// 	$endpoint_js .= '"proxyPath": "' . $type . '",';
// 	if ($endpoint['server_type'] == 'smartcloud') {
// 		$endpoint_js .= '"isSmartCloud": true,';
// 	}
	$endpoint_js .= '"isAuthenticated": "false",' . "\n";
	$endpoint_js .= '"transport": new Transport({}),' . "\n";
	$endpoint_js .= '"serviceMappings": {},' . "\n";
	$endpoint_js .= '"name": "' . $endpoint['name'] . '",' . "\n";
	
	if ($type == 'smartcloud') {
		$endpoint_js .= '"authenticationErrorCode": "403",' . "\n";
	} else {
		$endpoint_js .= '"authenticationErrorCode": "401",' . "\n";
	}
	$endpoint_js .= '"baseUrl": "' . $endpoint['url'] . '",' . "\n";

	$endpoint_js .=	'"proxy": new Proxy({' . "\n";
	$endpoint_js .=	'"proxyUrl": "' . plugins_url(PLUGIN_NAME) . '/core/index.php?classpath=services&endpointName=' . $endpoint['name'] . '&class=Proxy&method=route&_redirectUrl="})}),';

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

