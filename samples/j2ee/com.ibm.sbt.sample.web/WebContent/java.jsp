<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Social Business Toolkit - Java Samples</title>
    <link href="images/sbt.png" rel="shortcut icon">
    <link rel="stylesheet" href="/sbt.dojo180/dijit/themes/claro/claro.css">
    
    <%
    String[] styleHrefs = Util.getStyles(request, "oneui");
    for (String styleHref : styleHrefs) {
    %>
    <link rel="stylesheet" type="text/css" title="Style" href="<%=styleHref%>"></link>
    <%
    }
    %>

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
  </head>

  <body class="claro">
    <%@include file="includes/header.jsp" %>  
    
    <!-- main content starts -->
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span3">
                <%@include file="includes/outline.jsp"%>
            </div>
            <div class="span8">
                <div id="snippetContainer">
                    <%@include file="includes/java_snippet.jsp"%>
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
