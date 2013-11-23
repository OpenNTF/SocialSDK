<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Social Business Toolkit - Grant Access</title>
    <link href="images/sbt.png" rel="shortcut icon">
    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap-sbt.css" rel="stylesheet"></link>
    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css" rel="stylesheet"></link>
  </head>

  <body>
    <div id="wrap">
      <!-- main content starts -->
      <div class="hero-unit">
        <h2>Grant Access Sample</h2>
        <div class="alert alert-info">User '<%=request.getRemoteUser()%>' has been logged out.</div>
        <a href="index.jsp">Home</a>
		<% session.invalidate(); %>		
      </div>
      <!-- main content ends -->
    </div>
  </body>
</html>
