<!-- /*
 * � Copyright IBM Corp. 2012
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
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.profiles.ProfileService"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.profiles.Profile"%>
<%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SmartCloud ProfileService Java </title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	
	
	<%
		try {   
				ProfileService service = new ProfileService("smartcloud"); //If no endpoint is specified, it uses default end point i.e connections
				String contactGUID = Context.get().getProperty("sample.smartcloud.contactGUID");
				Profile profile = service.getContactByGUID(contactGUID);
				if(profile != null)
				{
					out.println("<B> Contact's Name : 	</B>" + profile.getDisplayName());
				}
				else
				{
					out.println("No Results");
				}
				
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");	
		}	
			%>
	 <br>
</body>
</html>