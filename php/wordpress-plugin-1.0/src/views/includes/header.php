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
 * @author Benjamin Jakobus
 */

$agnostic_deploy_url = str_replace('http://', '//', $deploy_url);
?>
<script type="text/javascript">
	var djConfig = {
	baseUrl: "<?php echo $agnostic_deploy_url; ?>",
	parseOnLoad: true,
	locale: "en"
};

</script>


<?php 
// <link rel="stylesheet" type="text/css" title="Style" href="//infolib.lotus.com/resources/oneui/3.0/css/base/core.css">
// <link rel="stylesheet" type="text/css" title="Style" href="//infolib.lotus.com/resources/oneui/3.0/css/defaultTheme/defaultTheme.css">
// <link rel="stylesheet" type="text/css" title="Style" href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojox/grid/enhanced/resources/claro/EnhancedGrid.css">
// <link rel="stylesheet" type="text/css" title="Style" href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css">
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
		}
	}
?>


<script>
	if (typeof _sbt == "undefined" || window._sbt_bridge_compat) {
		_sbt = 0;
		require({
			paths: {
				"sbt": "<?php echo $agnostic_deploy_url; ?>/js/sdk/sbt"
			}
		});
		require({
			paths: {
				"sbt/_bridge": "<?php echo $agnostic_deploy_url; ?>/js/sdk/_bridges/dojo-amd"
			}
		});
		require({
			paths: {
				"sbt/widget": "<?php echo $agnostic_deploy_url; ?>/js/sdk/dojo2"
			}
		});

		define("sbt/config", ["sbt/Proxy", "sbt/_bridge/Transport", "sbt/authenticator/Basic", "sbt/Endpoint", "sbt/ErrorTransport", "sbt/authenticator/OAuth"], 
			function (Proxy, Transport, Basic, Endpoint, ErrorTransport, OAuth) {
				var sbt = {};
				sbt.Properties = {
					"libraryUrl": "",
					"serviceUrl": "<?php echo plugins_url(PLUGIN_NAME); ?>/core/index.php?classpath=services&class=Proxy&method=fileOperations&_redirectUrl=",
					"sbtUrl": "<?php echo $agnostic_deploy_url; ?>/js/sdk"
				};
				sbt.Endpoints = {
						<?php echo generateEndpoint($authentication_method, $url, $agnostic_deploy_url, $name); ?>
				};
				sbt.findEndpoint = function (endpointName) {
					return this.Endpoints[endpointName];
				};
			return sbt;
		});
	};
</script>
</head>
<?php 


/**
 * Decides on an endpoint to use and generates the JavaScript for it.
 *
 * @return string			The JavaScript representing an endpoint.
 * @author Benjamin Jakobus
*/
function generateEndpoint($authentication_method, $url, $deploy_url, $name) {
	//TODO support list of endpoint and aliases
	return generateConnectionsEndpoint($authentication_method, $url, $deploy_url);
}

/**
 * Generates the JavaScript for an IBM Connections endpoint.
 *
 * @return string		The JavaScript representing an IBM Connections endpoint.
 * @author Benjamin Jakobus
 */

function generateConnectionsEndpoint($authentication_method, $url, $deploy_url) {
	$endpoint_js = '"connections": new Endpoint({';
	$endpoint_js .= '"authType": "' . $authentication_method . '",';
	$endpoint_js .= '"platform": "connections",';

	if ($authentication_method == 'oauth1') {
		$endpoint_js .= '"authenticator": new OAuth({"loginUi": null,
				"url": "' . $deploy_url . '"}),';
	} else if ($authentication_method == 'basic') {
		$endpoint_js .= '"authenticator": new Basic({';
		$endpoint_js .= '"url": "' . $deploy_url . '", "actionUrl": "' . plugins_url(PLUGIN_NAME) . '/core/index.php?classpath=services&class=Proxy&method=route&basicAuthRequest=true&_redirectUrl=' . getCurrentPage() . '"}),';
	}

	$endpoint_js .= '"proxyPath": "connections",';
	$endpoint_js .= '"isAuthenticated": "false",';
	$endpoint_js .= '"transport": new Transport({}),';
	$endpoint_js .= '"serviceMappings": {},';
	$endpoint_js .= '"name": "connections",';
	$endpoint_js .= '"authenticationErrorCode": "401",';
	$endpoint_js .= '"baseUrl": "' . $url . '",';
	$endpoint_js .= '"apiVersion": "4.0",';
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

