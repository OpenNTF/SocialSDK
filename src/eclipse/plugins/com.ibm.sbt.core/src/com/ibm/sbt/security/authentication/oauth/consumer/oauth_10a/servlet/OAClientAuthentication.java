/*
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
 */

package com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.core.handlers.AbstractServiceHandler;
import com.ibm.sbt.service.core.servlet.ServiceServlet;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * OAuth client authentication.
 * <p>
 * This servlet gets is used to trigger the authentication from a Javascript client,
 * generally in a pop-up window.
 * </p>
 * @author Philippe Riand
 */
public class OAClientAuthentication extends AbstractServiceHandler {

    public static final String URL_PATH = "oauth10_jsauth";
    
    public static final String MODE_MAINWINDOW	= "main";
    public static final String MODE_POPUP		= "popup";
    public static final String MODE_DIALOG		= "dialog";
    

    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

    @Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// The URL to call the service should be of the form:
    	//   .../oauth10_jsauth/[endpoint]
    	int len = URL_PATH.length()+2;
    	String pathInfo = req.getPathInfo();
    	if(pathInfo.length()<=len) {
    		ServiceServlet.service500(req, resp, "Empty endpoint name");
    		return;
    	}
    	String epName = pathInfo.substring(len);
    	Endpoint ep = EndpointFactory.getEndpoint(epName);
    	if(ep==null) {
    		ServiceServlet.service500(req, resp, "Cannot find endpoint {0}",epName);
    		return;
    	}
    	
    	try {
    		// If the endpoint is not authenticated, then authenticate
    		// else redirect the main page
	    	if(!ep.isAuthenticationValid()) {
				ep.authenticate(true);
	    	} else {
	    		generateCloseScript(req, resp);
	    	}
		} catch (ClientServicesException ex) {
			throw new ServletException(ex);
		}
    }
    
    protected void generateCloseScript(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String mode = req.getParameter("loginUi");
    	
    	PrintWriter pw = resp.getWriter();
    	try {
    		pw.println("<html>");
    		pw.println("<head>");
    		pw.println("</head>");
    		pw.println("<body>");
    		pw.println("<script>");
			if(StringUtil.isEmpty(mode) || mode.equalsIgnoreCase(MODE_MAINWINDOW)) {
				String redirect = req.getParameter("oaredirect");
				pw.println("  window.location.href = '"+redirect+"';");
			} else if(mode.equalsIgnoreCase(MODE_POPUP)) {
				pw.println("  if (window.opener && !window.opener.closed) {");
    			//pw.println("    window.opener.location.reload();");
				pw.println("window.opener.sbt.callback();");
				pw.println("delete window.opener.sbt.callback;");
    			pw.println("  }");
    			pw.println("  window.close();");
    		} else if(mode.equalsIgnoreCase(MODE_DIALOG)) {
//    			
//    			pw.println("  if (window.opener && !window.opener.closed) {");
//    			pw.println("    window.opener.location.reload();");
//    			pw.println("  }");
//    			pw.println("  window.close();");
    		} else {
    			throw new ServletException(StringUtil.format("Invalid mode {0}", mode));
    		}
    		pw.println("</script>");
    		pw.println("</body>");
    		pw.println("</html>");
    	} finally {
    		pw.flush();
    	}
    }
}
