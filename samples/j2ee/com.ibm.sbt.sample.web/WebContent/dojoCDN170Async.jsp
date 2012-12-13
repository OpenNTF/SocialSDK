<!DOCTYPE html>
<html><!-- closing tag is in included jsSamples.jsp -->
<head><!-- closing tag is in included jsSamples.jsp -->
<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>

<script type="text/javascript">
	var djConfig = {
		async : true,
		parseOnLoad: true
	};
</script>

<%
String baseUrl = UrlUtil.getBaseUrl(request); 
request.setAttribute("dojoPath","http://ajax.googleapis.com/ajax/libs/dojo/1.7.4/dojo/dojo.js");  
request.setAttribute("dojoTheme","tundra");  
request.setAttribute("dojoCSSPath","http://ajax.googleapis.com/ajax/libs/dojo/1.7.4/dijit/themes/tundra/tundra.css");
request.setAttribute("toolkit",PathUtil.concat(baseUrl,"/library?ver=1.7.4",'/'));
%>
<%@include file="jsSamples.jsp"%>
