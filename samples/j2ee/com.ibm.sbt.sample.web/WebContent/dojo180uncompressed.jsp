<!DOCTYPE html>
<html><!-- closing tag is in included jsSamples.jsp -->
<head><!-- closing tag is in included jsSamples.jsp -->
<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%
String baseUrl = UrlUtil.getBaseUrl(request);
request.setAttribute("dojoPath","/sbt.dojo180/dojo/dojo.js.uncompressed.js");  
request.setAttribute("dojoTheme","tundra");  
request.setAttribute("dojoCSSPath","/sbt.dojo180/dijit/themes/tundra/tundra.css");
request.setAttribute("toolkit",PathUtil.concat(baseUrl,"/library?ver=1.8.0",'/'));
%>
<%@include file="jsSamples.jsp"%>