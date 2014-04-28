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
 */-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.commons.util.io.json.JsonJavaObject"%>
<%@page import="com.ibm.sbt.services.client.base.JsonEntity"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.bss.*"%>
<%@page import="java.util.*"%> 
				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>Suspend Subscription</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
	try {
		final String subscriptionId = Context.get().getProperty("bss.subscriptionId");
		out.println("Subscription Id: " + subscriptionId + "<br/>");
		if (StringUtil.isEmpty(subscriptionId)) {
			out.println("Please provide a valid subscription id in the sbt.properties.");
			return;
		}
		
    	final SubscriptionManagementService subscriptionManagement = new SubscriptionManagementService("smartcloud");
		
		JsonEntity subscription = subscriptionManagement.getSubscriptionById(subscriptionId);
			
		JsonJavaObject rootObject = subscription.getJsonObject();
		JsonJavaObject subscriptionObject = rootObject.getAsObject("Subscription");
			
 		if(!subscriptionObject.get("SubscriptionState").equals("ACTIVE")){
 			subscriptionManagement.unsuspendSubscription(subscriptionId);
 		}
 			
 		final String subscriptId = subscriptionId;
 		final JspWriter fout = out;
		StateChangeListener stateChangeListener = new StateChangeListener(){
			@Override
			public void stateChanged(JsonEntity jsonEntity){
				try{
					subscriptionManagement.suspendSubscription(subscriptId);
					fout.println("Suspended Subscription Id " + subscriptId + "<br/>");
				}catch(Exception e){
					try{
						fout.println(e.getMessage());
					}catch(Exception ex){}
				}
			}
		};
		
		if(!subscriptionManagement.waitSubscriptionState(subscriptionId,"ACTIVE",5,1000,stateChangeListener)){
			out.println("Timeout waiting for subscription to be suspended");
		}
	}	
	catch (BssException be) {
		out.println("<pre>");
		out.println("Error suspending subscription caused by: "+ be.getResponseJson());
		out.println("</pre>");	
	}
	catch (Throwable e) {
		e.printStackTrace();
		out.println("Error suspending subscription caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>
