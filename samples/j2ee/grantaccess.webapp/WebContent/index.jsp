<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.sbt.security.credential.store.DBCredentialStore"%>
<%@page import="java.security.Principal"%>
<%@page import="grantaccess.webapp.Users"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Social Business Toolkit - Grant Access</title>
    <link href="images/sbt.png" rel="shortcut icon">

	<script type="text/javascript">
	var djConfig = {
	    parseOnLoad: true,
	    locale: 'en'
	};
	</script>
	<script type="text/javascript" src="/sbt.dojo180/dojo/dojo.js.uncompressed.js"></script>
	<script type="text/javascript" src="/grantaccess.webapp/library?lib=dojo&amp;ver=1.8.0"></script>

    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap-sbt.css" rel="stylesheet"></link>
    <link href="/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css" rel="stylesheet"></link>
  </head>

  <body>
    <div id="wrap">
      <!-- main content starts -->
      <div class="hero-unit">
        <h3>Grant Access Sample</h3>
        <p>This sample demonstrates how to check if logged on user has granted access for this application to access their IBM Connections data.</p>
        <p class="text-info">User Principal: <code><%= request.getUserPrincipal() %></code></p>
        
        <div id="grantAccessDiv" style="display: none;">
        	<div class="alert alert-error" id="grantAccessErrorDiv" style="display: none;"></div>
			<div id="desc">You have not yet granted access for this application to use your IBM Connections account.</div>
	    	<button class="btn btn-primary" id="grantAccessBtn">Grant Access</button>
		</div>
		
		<p><a href="logout.jsp">Log Out</a></p>
		
		<div id="checkAccessDiv">
			<div id="desc">Checking if you have granted access for this application to use your IBM Connections account.</div>
		</div>
	
        <div id="accessGrantedDiv" style="display: none;">
			<div id="desc">You have already granted access for this application to use your IBM Connections account.</div>
		</div>
		
		<script type="text/javascript">
		require([ "sbt/dom", "sbt/config" ],
			function(dom, config, FileService) {
				var endpoint = config.findEndpoint("connections");
				endpoint.isAuthenticationValid().then(function(response) {
					if (response.result) {
						displayAcessGranted(dom);
					} else {
						displayGrantAccess(dom, config);
					}
				}, function() {
					displayGrantAccess(dom, config);
				});
			});
		
			function displayAccessGranted(dom) {
				var checkAccessDiv = dom.byId("checkAccessDiv");
				checkAccessDiv.style.display = "none";
				var grantAccessDiv = dom.byId("grantAccessDiv");
				grantAccessDiv.style.display = "none";
				var grantAccessDiv = dom.byId("accessGrantedDiv");
				grantAccessDiv.style.display = "";
			}
		
			function grantAccess(dom, config) {
				config.Properties["loginUi"] = "popup";
				var endpoint = config.findEndpoint("connections");
				endpoint.authenticate().then(
					function() {
						displayAccessGranted(dom);
					},
					function(error) {
						displayGrantAccess(dom, config, error);
					}
				);
			}
		
			function displayGrantAccess(dom, config, error) {
				var checkAccessDiv = dom.byId("checkAccessDiv");
				checkAccessDiv.style.display = "none";
		
				var grantAccessDiv = dom.byId("grantAccessDiv");
				grantAccessDiv.style.display = "";
		
				var grantAccessBtn = dom.byId("grantAccessBtn");
				grantAccessBtn.onclick = function(evt) {
					grantAccess(dom, config);
				};
				
				var grantAccessErrorDiv = dom.byId("grantAccessErrorDiv");
				if (error) {
					grantAccessErrorDiv.style.display = "";
					grantAccessErrorDiv.innerHTML = error.message;
				} else {
					grantAccessErrorDiv.style.display = "none";
				}
			}
		</script>
		
		<%if (request.isUserInRole("admin")) {%>
			<h3>All Users</h3>
			<%
			List<String> users = Users.getUsers();
			if (users.isEmpty()) {
				%>
				<div class="alert alert-info">No users have granted access yet.</div>
				<%				
			} else {
				%><table><%
				for (String user : users) {
					%><tr><td><%=user%> is a member of <%=Users.getCommunityMembershipCount(user)%> communities.</td></tr><%
				}
				%></table><%				
			}
			%>
		<%}%>
		
      </div>
      <!-- main content ends -->
    </div>
  </body>
</html>
