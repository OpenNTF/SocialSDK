<!DOCTYPE html>
<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%
String baseUrl = UrlUtil.getBaseUrl(request); 
request.setAttribute("dojoPath","/sbt.dojo172/dojo/dojo.js");  
request.setAttribute("dojoTheme","tundra");  
request.setAttribute("dojoCSSPath","/sbt.dojo172/dijit/themes/tundra/tundra.css");
request.setAttribute("toolkit",PathUtil.concat(baseUrl,"/library?ver=1.7.2",'/'));
%>
<%@include file="jsSamples.jsp"%>
