<!DOCTYPE html>
<%@page import="com.ibm.commons.runtime.Context"%>
<html lang="en">
  <head>
  <% 
  Context ctx = Context.get();
  String defaultEndpoint = ctx.getProperty("endpoint.default");
  String environment = ctx.getProperty("environment");
  String envParam = request.getParameter("env");
  boolean smartcloud = false;
  if(envParam == null)
      smartcloud = environment != null && environment.equals("smartcloudEnvironment") ? true : false;
  else
      smartcloud = envParam.equals("smartcloudEnvironment") ? true : false;
  %>
    <meta charset="utf-8">
    <title>Social Business Toolkit - Samples</title>
    <link href="images/sbt.png" rel="shortcut icon">

    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap.css" rel="stylesheet"></link>
    <link href="css/stickyfooter.css" rel="stylesheet"></link>
    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css" rel="stylesheet"></link>
    <%if(smartcloud){ %>
      <link rel="stylesheet" href="https://apps.na.collabservtest.lotus.com/theming/theme/css/3" type="text/css" /> 
    <%} %>
  </head>

  <body class="<%if(smartcloud){ %>lotusui30_body<%}%>">
	<%@include file="includes/header.jsp" %>  
    
    <div id="wrap">
      <!-- main content starts -->
      <div class="hero-unit">
        <h2>Samples Application</h2>
        <p>The Social Business Toolkit SDK is a set of libraries and code samples that you use for connecting to the IBM Social Platform.
        The samples application is a web-based live demonstration of the JavaScript and Java APIs that are exposed by the SDK.
        The sample application contains a large set of code snippets and examples that you can use in your own applications.</p>
        <p><a class="btn btn-primary btn-large" target="_blank" href="http://bit.ly/ibmsbtcommunity">Learn More &raquo;</a></p>
      </div>
      <div class="container">
        <img src="https://dw1.s81c.com/developerworks/mydeveloperworks/blogs/0f357879-ccee-4927-98c1-7bb88d5dc81f/resource/ssbanner.png" class="img-polaroid">
      </div>
      <!-- main content ends -->
      <div id="push"></div>
    </div>

	<%@include file="includes/footer.jsp" %>  
  </body>
</html>
