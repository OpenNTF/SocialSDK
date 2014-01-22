<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">

var djConfig = {
		parseOnLoad: true,
		locale: 'en',
		packages: [
		    		
		    		{
		    			name: "sbt",
		    			location: "***REMOVED*** print($_SERVER['CONTEXT_PREFIX']); ?>sbt"
		    			//location: "sbt/js/sdk/sbt"
		    		}],
		paths: { "sbt/_config" : "***REMOVED*** print($_SERVER['CONTEXT_PREFIX']); ?>jsconfig.php?noext"},
		deps: ["sbt.js", "sbt/_config"]
		}; 

</script>


<link rel="stylesheet" type="text/css" title="Style"
	href="http://infolib.lotus.com/resources/oneui/3.0/css/base/core.css"></link>

<link rel="stylesheet" type="text/css" title="Style"
	href="http://infolib.lotus.com/resources/oneui/3.0/css/defaultTheme/defaultTheme.css"></link>


<script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojo/dojo.js.uncompressed.js"></script>
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojo/resources/dojo.css">
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dijit/themes/claro/claro.css">
<link rel="stylesheet" type="text/css" title="Style"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojox/grid/enhanced/resources/claro/EnhancedGrid.css">
<link rel="stylesheet" type="text/css" title="Style"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css">

	

</head>
<body>

***REMOVED***
	require_once '../externals/vendor/autoload.php';
	require_once 'private/config.php';
	
	use Guzzle\Http\Client;
	use Guzzle\Plugin\Oauth\OauthPlugin;
	
	$client = new Client($config['smartcloud.url']);
	
	$oauth = new OauthPlugin(array(
			'consumer_key'    => $config['smartcloud.consumerKey'],
			'consumer_secret' => $config['smartcloud.consumerSecret'],
			'token'           => $config['smartcloud.requestTokenURL'],
			'token_secret'    => $config['smartcloud.accessTokenURL']
	));
	$client->addSubscriber($oauth);
	

?>

<div id="json"></div>
<script type="text/javascript">

require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
    var communityService = new CommunityService();
    var promise = communityService.getPublicCommunities({
            asc : true,
            page : 1,
            ps : 10,
            since : "2009-01-04T20:32:31.171Z",
            sortField  : "lastmod"
        });
    promise.then(
        function(communities) {
            dom.setText("json", json.jsonBeanStringify(communities));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
    
});

</script>



</body>
</html>