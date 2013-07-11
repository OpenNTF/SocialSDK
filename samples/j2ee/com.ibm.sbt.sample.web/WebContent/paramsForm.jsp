<!DOCTYPE html>
<%@page import = "java.util.Map" %>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URI"%>
<%@page import="java.util.StringTokenizer"%>

<html>
<head>
<link href="/sbt.bootstrap211/bootstrap/css/bootstrap-sbt.css" rel="stylesheet"></link>
</head>

<body>
<%
  String callbackParam =  request.getParameter("callback");
  URI callbackURL = new URI(callbackParam);
  String queryString = callbackURL.getQuery();
%>

<form method="GET" action="<%=callbackURL%>">
<fieldset>
<legend>Input the data required by the sample:</legend>
<!-- Hidden fields to return the original javascriptPreview query (snippet id etc). -->
<%
  String[] queryArray = queryString.split("&");
  for(String parameterMapping : queryArray){
      String[] split = parameterMapping.split("=");
      String p = split[0];
      String v = split[1];
%>
  <input type="hidden" name="<%=p%>" value="<%=v%>">
<%
  }
%>
<!--  -->
<%
  Map<String, String[]> parameters = request.getParameterMap();
  for(String parameter : parameters.keySet()) {
      if(parameter.equals("callback"))
          continue;
      String[] values = parameters.get(parameter);     
        for(String value : values){
            if(value == null || value.equals("null"))
                value = "";
%>
          <%=parameter+":" %> <input type="text" name="<%=parameter%>" value="<%=value%>" >
<%
        }
  }
%>
</fieldset>
<input class="btn" type="submit" value="Submit">
</form>
</body>
</html>
