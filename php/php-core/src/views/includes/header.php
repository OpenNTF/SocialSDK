<?php
/**
 * Plugin Name: Social Business Toolkit integration plugin
 * Plugin URI: http://example.com
 * Description: Plugin providing access to the SBTK javascript api and controls.
 * Version: 0.1
 * Author: Lorenzo Boccaccia, Benjamin Jakobus
 * Author URI: https://github.com/LorenzoBoccaccia
 * License: Apache 2.0
 */

function plugins_url() {
	return BASE_LOCATION;
}
?>
<script type="text/javascript">
	var djConfig = {
	baseUrl: "<?php echo $deploy_url; ?>",
	parseOnLoad: true,
	locale: "en"
};

</script>
<link rel="stylesheet" type="text/css" title="Style" href="http://infolib.lotus.com/resources/oneui/3.0/css/base/core.css"></link>
<link rel="stylesheet" type="text/css" title="Style" href="http://infolib.lotus.com/resources/oneui/3.0/css/defaultTheme/defaultTheme.css"></link>
<script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojo/dojo.js.uncompressed.js"></script>
<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojo/resources/dojo.css">
<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dijit/themes/claro/claro.css">
<link rel="stylesheet" type="text/css" title="Style" href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojox/grid/enhanced/resources/claro/EnhancedGrid.css">
<link rel="stylesheet" type="text/css" title="Style" href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css">

<script>
	if (typeof _sbt == "undefined" || window._sbt_bridge_compat) {
		_sbt = 0;
		require({
			paths: {
				"sbt": "<?php echo $deploy_url; ?>/js/sdk/sbt"
			}
		});
		require({
			paths: {
				"sbt/_bridge": "<?php echo $deploy_url; ?>/js/sdk/_bridges/dojo-amd"
			}
		});
		require({
			paths: {
				"sbt/widget": "<?php echo $deploy_url; ?>/js/sdk/dojo2"
			}
		});

		define("sbt/config", ["sbt/Proxy", "sbt/_bridge/Transport", "sbt/authenticator/Basic", "sbt/Endpoint", "sbt/ErrorTransport", "sbt/authenticator/OAuth"], 
			function (Proxy, Transport, Basic, Endpoint, ErrorTransport, OAuth) {
				var sbt = {};
				sbt.Properties = {
					"libraryUrl": "",
					"serviceUrl": "<?php echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&method=fileOperations&_redirectUrl=",
					"sbtUrl": "<?php echo $deploy_url; ?>/js/sdk"
				};
				sbt.Endpoints = {
						<?php echo generateEndpoint($authentication_method, $url, $deploy_url, $name); ?>
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
 * Generates the JavaScript for an IBM SmartCloud endpoint.
 *
 * @return string		The JavaScript representing an IBM SmartCloud endpoint.
 * @author Benjamin Jakobus
 */
function generateSmartcloudEndpoint($authentication_method, $url, $deploy_url) {
	$endpoint_js = '"smartcloud": new Endpoint({';
	$endpoint_js .= '"authType":';
	$endpoint_js .= '"' . $authentication_method . '",';
	$endpoint_js .= '"isAuthenticated": false,';
	$endpoint_js .= '"transport": new Transport({}),';
	$endpoint_js .= '"name": "smartcloud",';
	$endpoint_js .= '"proxy": new Proxy({
			proxyUrl: "' . plugins_url() . '/index.php?classpath=services&class=Proxy&method=route&_redirectUrl="}),';
	$endpoint_js .= '"proxyPath": "smartcloud",';

	if ($authentication_method == 'oauth1') {
		$endpoint_js .= '"authenticator": new OAuth({"loginUi": null,
				"url": "' . $deploy_url . '"}),';
	} else if ($authentication_method == 'basic') {
		$endpoint_js .= '"authenticator": new Basic({';
		$endpoint_js .= '"url": "' . $deploy_url . '"}),';
	}

	$endpoint_js .= '"platform": "smartcloud",';
	$endpoint_js .= '"isSmartCloud": true,';
	$endpoint_js .= '"serviceMappings": {},';
	$endpoint_js .= '"authenticationErrorCode": 403,';
	$endpoint_js .= '"apiVersion": "3.0",';
	$endpoint_js .= '"baseUrl": "' . $url . '"';
	$endpoint_js .= '}),';

	return $endpoint_js;
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
		$endpoint_js .= '"url": "' . $deploy_url . '", "actionUrl": "' . plugins_url() . '/index.php?classpath=services&class=Proxy&method=route&basicAuthRequest=true&_redirectUrl=' . getCurrentPage() . '"}),';
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
	$endpoint_js .=	'"proxyUrl": "' . plugins_url() . '/index.php?classpath=services&class=Proxy&method=route&_redirectUrl="})}),';

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

