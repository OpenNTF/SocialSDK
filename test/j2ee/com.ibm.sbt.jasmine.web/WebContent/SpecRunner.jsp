<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.sbt.playground.snippets.SnippetNode"%>
<%@page import="com.ibm.sbt.playground.snippets.RootNode"%>
<%@page import="com.ibm.sbt.playground.snippets.Snippet"%>
<%@page import="com.ibm.sbt.playground.snippets.AbstractNode"%>
<%@page import="com.ibm.sbt.playground.snippets.CategoryNode"%>
<%@page import="com.ibm.sbt.jasmine.SpecFactory"%>
<%@page import="com.ibm.sbt.jasmine.SpecCategory"%>
<%@page import="com.ibm.sbt.jasmine.SpecNode"%>
<%@page import="com.ibm.sbt.jasmine.SnippetFactory"%>
<%@page import="java.util.List"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Unit Tests running with <%=request.getParameter("title")%></title>

  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  
  <link rel="stylesheet" type="text/css" href="jasmine/jasmine.css">
  <script type="text/javascript" src="jasmine/jasmine.js"></script>
  <script type="text/javascript" src="jasmine/jasmine-html.js"></script>

  <!-- include source files here... -->
  <script type="text/javascript" src="<%=request.getParameter("path")%>" <%=request.getParameter("params")%>></script>
  <script type="text/javascript" src="/sbt.jasmine.web/library?lib=<%=request.getParameter("lib")%>&ver=<%=request.getParameter("ver")%>"></script>
  <script type="text/javascript" src="js/ExecJasmine.js"></script>
  
  <!-- include spec files here... -->
  <script type="text/javascript" src="spec/SpecHelper.js"></script>
  <%
  	RootNode rootNode = SpecFactory.getSpecs(application);
  	List<AbstractNode> children = SpecFactory.getSpecNodes(rootNode);
	for (int i = 0; i < children.size(); i++) {
		AbstractNode node = children.get(i);
		if (node.isSnippet()) {
			String src = ((SpecNode)node).getSrc();
  %>  <script type="text/javascript" src="<%=src%>"></script>
  <%
		}
	}
  %>  
</head>

<body>
  <!-- include test html here -->
  <div id="specHtml" style="visibility:hidden">
  <%
	for (int i = 0; i < children.size(); i++) {
		AbstractNode node = children.get(i);
		if (node.isSnippet()) {
			Snippet snippet = rootNode.loadSnippet(SpecFactory.getRootFile(application), node.getUnid());
			String html = snippet.getHtml();
			if (html != null && html.length() != 0) {
				out.println(html);
			}
		}
	}
  %>
  </div>
    
  <script type="text/javascript">
    execJasmine();
  </script>  
    
</body>
</html>
