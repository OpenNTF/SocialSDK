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
<title>Unsuspend Customer</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
	try {
    	final CustomerManagementService customerManagement = new CustomerManagementService("smartcloudC1");
		EntityList<JsonEntity> customerList = customerManagement.getCustomers();
		
		if (!customerList.isEmpty()) {
			JsonEntity customer = customerList.get(0);
			String customerId = "" + customer.getAsLong("Id");
			customer = customerManagement.getCustomerById(customerId);
			
			JsonJavaObject rootObject = customer.getJsonObject();
			JsonJavaObject customerObject = rootObject.getAsObject("Customer");
			
 			if(customerObject.get("CustomerState").equals("ACTIVE")){
 				customerManagement.suspendCustomer(customerId);	
 			}
 			
 			final String custId = customerId;
 			final JspWriter fout = out;
			StateChangeListener stateChangeListener = new StateChangeListener(){
				@Override
				public void stateChanged(JsonEntity jsonEntity){
					try{
						customerManagement.unsuspendCustomer(custId);
						fout.println("Unsuspended Customer Id " + custId + "<br/>");
					}catch(Exception e){
						try{
							fout.println(e.getMessage());
						}catch(Exception ex){}
					}
				}
			};
			
			if(!customerManagement.waitCustomerState(customerId,"SUSPENDED",5,1000,stateChangeListener)){
				out.println("Timeout waiting for customer to be unsuspended");
			}
			
		}
	} catch (Throwable e) {
		e.printStackTrace();
		out.println("Error unsuspending customer caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>
