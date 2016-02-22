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

$agnostic_deploy_url = str_replace('http://', '//', $deploy_url);

function plugins_url() {
	return str_replace('http://', '//', BASE_LOCATION);
}
?>
<script type="text/javascript">
	var djConfig = {
	baseUrl: "<?php echo $agnostic_deploy_url; ?>",
	parseOnLoad: true,
	locale: "en"
};

</script>

<script src="<?php echo $js_library; ?>"></script>
<link rel="stylesheet" type="text/css" title="Style" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.10/dojox/grid/enhanced/resources/claro/EnhancedGrid.css">
<link rel="stylesheet" type="text/css" title="Style" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.10/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css">

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
					"serviceUrl": "<?php echo plugins_url(); ?>/index.php?classpath=services&class=Proxy&&uid=<?php global $USER; echo $USER->id; ?>&method=fileOperations&_redirectUrl=",
					"sbtUrl": "<?php echo $agnostic_deploy_url; ?>/js/sdk"
				};
				sbt.Endpoints = {
						<?php 
							foreach($endpoints as $endpoint) {
								echo generateEndpoint($endpoint->auth_type, $endpoint->server_url, $agnostic_deploy_url, $endpoint->name, $endpoint->api_version);
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

<?php 



/**
 * Generates the JavaScript endpoint.
 */

function generateEndpoint($authentication_method, $url, $deploy_url, $name, $api_version) {
	global $USER;
	if ($api_version == "" || $api_version == null) {
		$api_version = "2.0";
	}
	$authType = str_replace('1', '', $authentication_method);
	$authType = str_replace('2', '', $authType);
	$endpoint_js = '"' . $name . '": new Endpoint({';
	$endpoint_js .= '"authType": "' . $authType . '",';
	$endpoint_js .= '"platform": "connections",';

	if ($authentication_method == 'oauth1' || $authentication_method == 'oauth2') {
		$endpoint_js .= '"authenticator": new OAuth({"loginUi": null,
				"url": "' . $deploy_url . '"}),';
	} else if ($authentication_method == 'basic') {
		$endpoint_js .= '"authenticator": new Basic({';
		$endpoint_js .= '"url": "' . $deploy_url . '", "actionUrl": "' . plugins_url() . '/index.php?classpath=services&class=Proxy&method=route&endpointName=' . $name . '&basicAuthRequest=true&uid=' . $USER->id . '&_redirectUrl=' . getCurrentPage() . '"}),';
	}

	if ($type == 'smartcloud') {
		$endpoint_js .= '"isSmartCloud": true,';
	}
	
	$endpoint_js .= '"proxyPath": "connections",';
	$endpoint_js .= '"isAuthenticated": "false",';
	$endpoint_js .= '"transport": new Transport({}),';
	$endpoint_js .= '"serviceMappings": {},';
	$endpoint_js .= '"name": "' . $name . '",';
	

	$endpoint_js .= '"authenticationErrorCode": "401",';
	$endpoint_js .= '"baseUrl": "' . $url . '",';
	$endpoint_js .= '"apiVersion": "' . $api_version . '",';
	$endpoint_js .=	'"proxy": new Proxy({';
	$endpoint_js .=	'"proxyUrl": "' . plugins_url() . '/index.php?classpath=services&class=Proxy&method=route&endpointName=' . $name . '&uid=' . $USER->id . '&_redirectUrl="})}),';

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

