<!DOCTYPE html>
<html><!-- closing tag is in included jsSamples.jsp -->
<head><!-- closing tag is in included jsSamples.jsp -->

<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>

<%
String baseUrl = UrlUtil.getBaseUrl(request);
request.setAttribute("jsLibPath","/sbt.jquery182/js/jquery-1.8.0.min.js");  
request.setAttribute("jsLibTheme","tundra");  
request.setAttribute("jsLibCSSPath","/sbt.jquery182/css/ui-lightness/jquery-ui-1.8.23.custom.css");
request.setAttribute("toolkit",PathUtil.concat(baseUrl,"/library?lib=jquery&ver=1.8.0",'/'));
request.setAttribute("requirejsPath","/sbt/js/libs/require.js");
%>
<%@include file="jsSamples.jsp"%> 