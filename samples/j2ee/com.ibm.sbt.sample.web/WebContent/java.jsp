<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Social Business Toolkit - Java Samples</title>
    <link href="images/sbt.png" rel="shortcut icon">
    <link rel="stylesheet" href="/sbt.dojo180/dijit/themes/claro/claro.css">
    <link rel="stylesheet" href="libs/codemirror/lib/codemirror.css">
    <script src="libs/codemirror/lib/codemirror.js.uncompressed.js"></script>
    <script src="libs/codemirror/mode/javascript/javascript.js"></script>
    <script src="libs/codemirror/mode/xml/xml.js"></script>
    <script src="libs/codemirror/mode/css/css.js"></script>
    <script src="libs/codemirror/mode/htmlmixed/htmlmixed.js"></script>
    <script src="libs/codemirror/mode/htmlembedded/htmlembedded.js"></script>
    <script src="libs/codemirror/lib/util/formatting.js"></script>
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
    <script src="/sbt.dojo180/dojo/dojo.js"></script>
    <%=Util.getHomeTheme(request)%>
  </head>

  <body class="claro <%=Util.getHomeBodyClass(request)%>">
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
                            <a href="#">JavaServer Page</a>
                        </li>
                        <li>
                            <a href="#">Documentation</a>
                        </li>
                    </ul>
                    <div id="snippetContainer" style="border-style:solid;border-width:1px;width=500px;height:300px;border-color:#D3D3D3;overflow:none;">
                        <%@include file="includes/java_snippet.jsp"%>
                    </div>
                </div>
                <div><hr/></div>
                <%@include file="includes/java_runner.jsp"%>
            </div>
        </div>
    </div>

    <script src="/sbt.jquery180/js/jquery-1.8.0.min.js"></script>
    <!-- <script src="/sbt.jqueryui1823/js/jquery-ui-1.8.23.custom.min.js"></script> -->
    <script src="js/java_jquery/jqueryNavbar.js"></script>
    <script src="js/java_jquery/jqueryAjax.js"></script>
  </body>
</html>
