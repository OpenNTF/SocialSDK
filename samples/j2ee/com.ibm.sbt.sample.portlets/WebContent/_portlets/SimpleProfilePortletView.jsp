<!-- /*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */ -->
<%@page session="false" contentType="text/html" pageEncoding="ISO-8859-1" import="java.util.*,javax.portlet.*,com.ibm.sbt.portlet.sample.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>        
<%@ taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model" %>        
<portlet:defineObjects/>
<portlet-client-model:init>
      <portlet-client-model:require module="ibm.portal.xml.*"/>
      <portlet-client-model:require module="ibm.portal.portlet.*"/>   
</portlet-client-model:init>
<%@ include file="../dojo_init.jspf"%>


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

<%@ include file="SimpleProfilePortletView.html"%>
