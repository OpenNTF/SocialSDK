<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>IBM Social Business Toolkit Unit Tests</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="/sbt/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/sbt/bootstrap/css/bootstrap-responsive.css"
	  rel="stylesheet">
</head>
<body>
	<b></b>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="hero-unit">
					<h2>IBM Social Business Toolkit Unit Tests</h2>
					<p>Start here to run the unit test by first selecting the JavaScript library to use. Select from the options listed below.</p>
				</div>
			<%
			String[][] jsLibs = new String[][] {
				// Title, Description, jsLib, jsVer
				{ "Dojo 1.8.0", "Dojo Toolkit version 1.8.0", "/sbt.dojo180/dojo/dojo.js", "djConfig%3D%22parseOnLoad%3A%20true%22", "dojo", "1.8.0" },
				{ "Dojo 1.7.4", "Dojo Toolkit version 1.7.4 (CDN)", "http://ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/dojo.js", "djConfig%3D%22parseOnLoad%3A%20true%22", "dojo", "1.7.4" },
				{ "Dojo 1.6.1", "Dojo Toolkit version 1.6.1 (CDN)", "http://ajax.googleapis.com/ajax/libs/dojo/1.6.1/dojo/dojo.js", "djConfig%3D%22parseOnLoad%3A%20true%22", "dojo", "1.6.1" },
				{ "Dojo 1.4.3", "Dojo Toolkit version 1.4.3 (CDN)", "http://ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js", "djConfig%3D%22parseOnLoad%3A%20true%22", "dojo", "1.4.3" }
			};
				
			for (int i=0; i<jsLibs.length; i++) {
				String title = jsLibs[i][0];
				String desc = jsLibs[i][1];
				String path = jsLibs[i][2];
				String params = jsLibs[i][3];
				String lib = jsLibs[i][4];
				String ver = jsLibs[i][5];
			%>
				<div class="row-fluid">
 		    		<div class="span4">
              			<h2><%=title%></h2>
              			<p><%=desc%></p>
              			<p><a class="btn" href="UnitTestRunner.jsp?title=<%=title%>&path=<%=path%>&params=<%=params%>&lib=<%=lib%>&ver=<%=ver%>">Run &raquo;</a></p>
            		</div>
				</div>
			<%
			}
			%>
			</div>
		</div>
	</div>
</body>
</html>