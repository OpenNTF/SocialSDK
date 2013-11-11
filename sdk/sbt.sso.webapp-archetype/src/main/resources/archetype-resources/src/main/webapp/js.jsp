#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<!DOCTYPE html>
<html lang="en" style="height: 100%;">

<head>

<link href="images/sbt.png" rel="shortcut icon">



<link rel="stylesheet" type="text/css" title="Style"
	href="http://infolib.lotus.com/resources/oneui/3.0/css/base/core.css"></link>

<link rel="stylesheet" type="text/css" title="Style"
	href="http://infolib.lotus.com/resources/oneui/3.0/css/defaultTheme/defaultTheme.css"></link>



<script src="//ajax.googleapis.com/ajax/libs/dojo/1.9.1/dojo/dojo.js"></script>
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.9.1/dojo/resources/dojo.css">
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.9.1/dijit/themes/claro/claro.css">
<link rel="stylesheet" type="text/css" title="Style"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.9.1/dojox/grid/enhanced/resources/claro/EnhancedGrid.css">
<link rel="stylesheet" type="text/css" title="Style"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.9.1/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css">



<script type="text/javascript"
	src="library/"></script>

<script type="text/javascript">
	require(
			[ "sbt/dom", "sbt/connections/controls/communities/CommunityGrid" ],
			function(dom, CommunityGrid) {
				var grid = new CommunityGrid({
			         type: "my"
			    });

				dom.byId("gridDiv").appendChild(grid.domNode);

				grid.update();
			});
</script>

</head>
<body class="lotusui30_body lotusui30_fonts lotusui30"
	style="width: 90%; height: 100%;">
	<div id="gridDiv"></div>
</body>
</html>