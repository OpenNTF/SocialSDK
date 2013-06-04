<%@page import="com.ibm.sbt.sample.web.util.Util"%>
<!DOCTYPE html>
<html lang="en">
  <head>
  <%boolean smartcloud = request.getParameter("env") != null && request.getParameter("env").equals("smartcloudEnvironment") ? true : false; %>
    <meta charset="utf-8">
    <title>Social Business Toolkit - JavaScript Samples</title>
    <link href="images/sbt.png" rel="shortcut icon">
    <link rel="stylesheet" href="/sbt.dojo180/dijit/themes/claro/claro.css">
    <!-- <link rel="stylesheet" href="/sbt.jqueryui1823/css/ui-lightness/jquery-ui-1.8.23.custom.css"> -->
    <link rel="stylesheet" href="libs/codemirror/lib/codemirror.css">
    <script src="libs/codemirror/lib/codemirror.js.uncompressed.js"></script>
    <script src="libs/codemirror/mode/javascript/javascript.js"></script>
    <script src="libs/codemirror/mode/xml/xml.js"></script>
    <script src="libs/codemirror/mode/css/css.js"></script>
    <script src="libs/codemirror/lib/util/formatting.js"></script>
    <script src="/sbt.dojo180/dojo/dojo.js"></script>
    <%if(smartcloud){ %>
      <link rel="stylesheet" href="https://apps.na.collabservtest.lotus.com/theming/theme/css/3" type="text/css" /> 
    <%} %>
    
    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap.css" rel="stylesheet"></link>
   
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
    </style>
    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css" rel="stylesheet"></link>
  </head>

  <body class="claro lotusui30 lotusui30_body lotusui30_fonts scloud3">
  <%if(smartcloud){%>
    <script src="https://apps.na.collabservtest.lotus.com/navbar/banner/smartcloudExt?oneui=3"></script>
  <%}%>
  
  <%@include file="includes/header.jsp" %>  

    <!-- main content starts -->
    <div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<%@include file="includes/outline.jsp"%>
			</div>
			<div class="span8">
                <div>
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#">JavaScript</a>
                        </li>
                        <li>
                            <a href="#">HTML</a>
                        </li>
                        <li>
                            <a href="#">CSS</a>
                        </li>
                        <li>
                            <a href="#">Documentation</a>
                        </li>
                    </ul>
                    <div id="snippetContainer" style="border-style:solid;border-width:1px;width=500px;height:300px;border-color:#D3D3D3;overflow:none;">
                        <%@include file="includes/js_snippet.jsp"%>
                    </div>
                </div>
				
				<div><hr/></div>
                <div id="demoContainer">
                    <%@include file="includes/js_runner.jsp"%>
                </div>
				
			</div>
		</div>
	</div>
    <script src="/sbt.jquery180/js/jquery-1.8.0.min.js"></script>
    <!-- <script src="/sbt.jqueryui1823/js/jquery-ui-1.8.23.custom.min.js"></script> -->
    <script src="js/js_jquery/jqueryNavbar.js"></script>
    <script src="js/js_jquery/jqueryAjax.js"></script>
  </body>
</html>
