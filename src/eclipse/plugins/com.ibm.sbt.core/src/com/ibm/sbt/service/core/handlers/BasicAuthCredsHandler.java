/*
R * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.service.core.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;


public class BasicAuthCredsHandler extends AbstractServiceHandler {
	public static final String URL_PATH = "basicAuth";
	public static final String MODE_MAINWINDOW			= "mainWindow";
    public static final String MODE_POPUP				= "popup";
    public static final String AUTH_ACCEPTED			= "accepted";
    public static final String AUTH_DECLINED			= "declined";
    public static final String USER_NAME				= "username";
    public static final String PASSWORD					= "password";
    public static final String LOGIN_UI					= "loginUi";
    public static final String REDIRECT_URL				= "redirectURL";
    public static final String REDIRECT_URL_TO_LOGIN	= "redirectURLToLogin";
    public static final String ENDPOINT_NAME			= "endPointName";
    public static final String JS_APP					= "JSApp";
    public static final String JAVA_APP					= "JavaApp";
    
	
	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
    	Endpoint endpoint = EndpointFactory.getEndpoint(getEndpointName(pathInfo));  //remove hardcoded name ..get second token from path info
    	BasicEndpoint basicendpoint = null;
    	
    	if(endpoint instanceof BasicEndpoint )
    	{
    		basicendpoint = (BasicEndpoint)endpoint;
    	
    		String user = request.getParameter(USER_NAME);
    		String pswd = request.getParameter(PASSWORD);
        	
    		//test if auth info is okay ...we need to check if user/password are valid.
    		try {
				if(basicendpoint.login(user, pswd, false))
				{
					basicendpoint.setUser(user);
		    		basicendpoint.setPassword(pswd);
		    		basicendpoint.writeToStore();
		    		if(getCallerType(pathInfo).equals(JS_APP)){
		    			generateCloseScript(request, response, AUTH_ACCEPTED);
		    		}else if(getCallerType(pathInfo).equals(JAVA_APP)){
		    			redirectToJavaApp(request, response, AUTH_ACCEPTED );
		    		}
				}else{
					response.setStatus(401);
					if(getCallerType(pathInfo).equals(JS_APP)){
		    			generateCloseScript(request, response, AUTH_DECLINED);
		    		}else if(getCallerType(pathInfo).equals(JAVA_APP)){
		    			redirectToJavaApp(request, response, AUTH_DECLINED);
		    		}
				}
			} catch (PasswordException e) {
				Platform.getInstance().log("PasswordException in BasicAuthCredsHandler"+e);
			}
    	}
	}    	
	
	public String getCallerType(String pathInfo){//returns string JSApp or JavaApp
		String callerType = ""; 
			if(pathInfo != null){
				String[] tokens = pathInfo.split("/");
				if(tokens.length > 3){
					callerType = tokens[3];
				}
			}
		return callerType;
	}
	
	public String getEndpointName(String pathInfo){
		String endPointName = ""; 
			if(pathInfo != null){
				String[] tokens = pathInfo.split("/");
				if(tokens.length > 2){
					endPointName = tokens[2];
				}
			}
		return endPointName;
	}
	
	protected void redirectToJavaApp(HttpServletRequest req, HttpServletResponse resp, String authentication) throws ServletException, IOException {
		Context context = Context.get();
		String url = (String)req.getParameter(REDIRECT_URL);
		if(authentication.equals(AUTH_DECLINED)){
			if(url != null){
				if(url.indexOf("showWrongCredsMessage=true") == -1) // don't add again if wrongCreds parameter is already added for declined Authentication
					url = PathUtil.concat(url,"showWrongCredsMessage=true", '?');
			}
		}
		context.sendRedirect(url);
	}
	
	protected void generateCloseScript(HttpServletRequest req, HttpServletResponse resp, String authentication) throws ServletException, IOException {
    	String mode = req.getParameter(LOGIN_UI);
    	if(mode != null){ //mode is null for dojo dialog LOGIN_UI. No action required in that case. 
			String redirectToLogin = req.getParameter(REDIRECT_URL_TO_LOGIN);    	
	    	PrintWriter pw = resp.getWriter();
	    	try {
	    		pw.println("<html>");
	    		pw.println("<head>");
	    		pw.println("</head>");
	    		pw.println("<body>");
	    		pw.println("<script>");
				if(StringUtil.isEmpty(mode) || mode.equalsIgnoreCase(MODE_MAINWINDOW)) {//MainWindow mode
					String redirectURL = URLDecoder.decode(req.getParameter(REDIRECT_URL),"utf-8");
					if(authentication.equals(AUTH_DECLINED)){
	    				pw.println("window.location.href = '"+redirectToLogin+"&redirectURL="+redirectURL+"&loginUi="+mode+"&showWrongCredsMessage=true'");
	    			}else if(authentication.equals(AUTH_ACCEPTED)){
	    				pw.println("  window.location.href = '"+redirectURL+"';");
	    			}
				} else if(mode.equalsIgnoreCase(MODE_POPUP)) {//Popup Mode
	    			pw.println("  if (window.opener && !window.opener.closed) {");
	    			if(authentication.equals(AUTH_DECLINED)){
	    				pw.println("window.location.href = '"+redirectToLogin+"&redirectURL=empty&loginUi="+mode+"&showWrongCredsMessage=true'");
	    			}else if(authentication.equals(AUTH_ACCEPTED)){
	    				//pw.println("window.opener.sbt.Endpoints['"+req.getParameter(ENDPOINT_NAME)+"'].callback();");
	    				pw.println("if(window.opener.sbt.callback){");
	    				pw.println("window.opener.sbt.callback();");
	    				pw.println("delete window.opener.sbt.callback;");
	    				pw.println("}");
	    				pw.println("window.close();");
	    			}
	    			pw.println("}");
				}
	    		
	    		else {
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
    
}

