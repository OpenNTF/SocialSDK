<!DOCTYPE html>
<%@page import = "java.util.Map" %>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URI"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.ibm.commons.runtime.Context" %>

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
<legend id="legend">Input the data required by the sample:</legend>
<!-- Hard coded small width is fine since the table will expand to suit the content -->
<table style="width:20px" class="table" >
    <tr>
        <th scope="col">Property</th>
        <th scope="col">Value</th>
    </tr>
    
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
          if(parameter.equals("callback")){
              continue;
          }
          String[] values = parameters.get(parameter);     
            for(String value : values){
                if(value == null || value.equals("null")){
                    value = "";
                }
                String label = Context.get().getProperty(parameter + ".label");
                if(label == null){
                    label = parameter;
                }
    %>
              
              <tr>
                <th style="vertical-align:middle;" scope="row"><%=label+":" %></th>
                <td><input style="vertical-align:middle;margin-bottom:0px;" type="text" name="<%=parameter%>" value="<%=value%>" ></td>
              </tr>
    <%
            }
      }
    %>
    
    
    
</table>

</fieldset>
<input class="btn-primary" type="submit" value="Run">
</form>
</body>
</html>
