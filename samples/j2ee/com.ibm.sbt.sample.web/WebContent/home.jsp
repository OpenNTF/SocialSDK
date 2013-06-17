<!DOCTYPE html>
<%@page import="com.ibm.commons.runtime.Context"%>
<html lang="en">
  <head>
  <% 
  String environment = Context.get().getProperty("environment");
  boolean smartcloud = environment != null && environment.equals("smartcloudEnvironment") ? true : false;
  String smartcloudUrl = Context.get().getProperty("smartcloud.url");
  %>
    <meta charset="utf-8">
    <title>Social Business Toolkit - Samples</title>
    <link href="images/sbt.png" rel="shortcut icon">

    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap.css" rel="stylesheet"></link>
    <link href="css/stickyfooter.css" rel="stylesheet"></link>
    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css" rel="stylesheet"></link>
    <%if(smartcloud){ %>
      <link rel="stylesheet" href="<%=smartcloudUrl%>/theming/theme/css/3" type="text/css" /> 
    <%} %>
  </head>

  <body class="<%if(smartcloud){ %>lotusui30_body<%}%>">
	<%@include file="includes/header.jsp" %>  
    
    <div id="wrap">
      <!-- main content starts -->
      <div class="hero-unit" style="background-image:url('images/cover-bg.gif');color:white;">
        <h2>Samples Application</h2>
        <p>The Social Business Toolkit SDK is a set of libraries and code samples that you use for connecting to the IBM Social Platform.
        The samples application is a web-based live demonstration of the JavaScript and Java APIs that are exposed by the SDK.
        The sample application contains a large set of code snippets and examples that you can use in your own applications.</p>
        <p><a class="btn btn-primary btn-large" target="_blank" href="https://www.ibmdw.net/social/">Learn More</a></p>
        <div class="container">
          <img src="images/banner.jpg">
        </div>
      </div>
      <!-- main content ends -->
    </div>

	<%@include file="includes/footer.jsp" %>  
  </body>
</html>
