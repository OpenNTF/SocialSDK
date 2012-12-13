<%@page session="false" contentType="text/html" pageEncoding="ISO-8859-1" import="java.util.*,javax.portlet.*,com.ibm.sbt.portlet.sample.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>        
<%@ taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model" %>        
<portlet:defineObjects/>
<portlet-client-model:init>
      <portlet-client-model:require module="ibm.portal.xml.*"/>
      <portlet-client-model:require module="ibm.portal.portlet.*"/>   
</portlet-client-model:init>
<%@ include file="../../../dojo_init.jspf"%>


<!-- include PortletHelper class and create object -->
<script type="text/javascript">
dojo.require("portlet.namespace_7bea1ae563.portlethelper.PhilPortletsPortletView");//The initial path segment portlet.namespace_7bea1ae563 is already registerd.

//the portletHelper object may be used in writing the business logic related to JavaScript for this portlet
//and its methods can be called in the format: portletHelper_<portlet:namespace/>.handler(argsToPass);
var portletHelper_<portlet:namespace/>;

dojo.addOnLoad( function() {
      portletHelper_<portlet:namespace/> = new portlet.namespace_7bea1ae563.portlethelper.PhilPortletsPortletView({portlet: portlet_<portlet:namespace/>});
} );
</script>
<!-- end include PortletHelper class and create object -->

<script>
function sbtGetProperty(ns,name) {
	return sbtContext[name];
}
</script>

<script>
    dojo.require("dijit.Declaration");
</script>

<div dojoType="dijit.Declaration" widgetClass="dijit_<portlet:namespace/>" defaults="{ns:'<portlet:namespace/>'}">
	<jsp:include page="PhilPortletsPortletView.html" />
</div>
<div dojoType="dijit_<portlet:namespace/>" ns="<portlet:namespace/>" />
