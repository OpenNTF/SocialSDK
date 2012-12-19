<!DOCTYPE html>
<html><!-- closing tag is in included jsSamples.jsp -->
<head><!-- closing tag is in included jsSamples.jsp -->
<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>

<script type="text/javascript">
	var djConfig = {
		parseOnLoad: true
	};
</script>

<%
String baseUrl = UrlUtil.getBaseUrl(request);
request.setAttribute("jsLibPath","/portal_dojo/v1.4.3/dojo/dojo.js");  
request.setAttribute("jsLibTheme","tundra");  
request.setAttribute("jsLibCSSPath","/portal_dojo/v1.4.3/dijit/themes/tundra/tundra.css");
request.setAttribute("toolkit",PathUtil.concat(baseUrl,"/library?ver=1.4.3",'/'));

%>
<%@include file="jsSamples.jsp"%>
