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
package com.ibm.sbt.service.core.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a simple proxy class provided as an example. Prefer the @see:ProxyServlet class.
 * 
 * Proxy for transferring HTTP GET requests to the specified target URL. The result
 * returned by the target is transferred back to the original request initiator.
 */
public class SimpleServiceServlet extends HttpServlet implements Servlet
{
    /** The name of the original request parameter specifying target URL */
    private static final String PARAM_URL = "url";
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs and sends HTTP GET request to the target URL. The content received
     * in a response is then returned to the original request initiator
     * 
     * @param request the original request, specifies target URL as a value of the
     *            parameter named {@link #PARAM_URL}.
     * @param response the response payload contains the content received from the
     *            target URL
     */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String pathInfo = request.getPathInfo();
    	String contextPath = request.getContextPath();
    	String servletPath = request.getServletPath();
    	String uri = request.getRequestURI();
    	
        // Retrieve target URL
        printRequest(request);
        String url = uri.substring(servletPath.length()+contextPath.length()+1);
        if(url.startsWith("http/")) {
        	url = "http://"+url.substring(5);
        } else if(url.startsWith("https/")) {
        	url = "https://"+url.substring(6);
        } else {
            sendError(HttpServletResponse.SC_BAD_REQUEST, response, "Invalid protocol for proxied url");
        }
    	String qs = request.getQueryString();
    	if(qs!=null && qs.length()>0) {
    		url = url + '?' + qs; //URLDecoder.decode(qs);
    	}
        log("Target URL = '" + url + "'");
        
        // Establish HTTP connection to the target
        HttpURLConnection httpCon = connect(url, request, response);
        if (httpCon == null) {
            return;
        }
        
        try {
        	setResponseContent(httpCon, response);
        } finally {
            httpCon.disconnect();
        }
    }
    
    /**
     * Opens HTTP connection to the target URL
     * 
     * @param url the target URL
     * @param response is used for sending error reply
     * @return the connection if succeeded to connect; <code>null</code> otherwise.
     *         In case of failure the error reply is sent to the original request
     *         initiator
     */
    private HttpURLConnection connect(final String url, HttpServletRequest request, HttpServletResponse response) {
        try {
        	URL targetUrl = new URL(url);
            URLConnection con = targetUrl.openConnection();
            if (!(con instanceof HttpURLConnection)) {
                sendError(HttpServletResponse.SC_BAD_REQUEST, response, "Unexpected URL protocol");
                return null;
            }
            HttpURLConnection httpCon = (HttpURLConnection)con;
            
            // Authentication test
//            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
//            String userpassword = "priand@us.ibm.com" + ":" + "xxx";
//            String encodedAuthorization = enc.encode( userpassword.getBytes() );
//            httpCon.setRequestProperty("Authorization", "Basic "+ encodedAuthorization);
            
            // Copy the headers
            if(false) {
	            for (Enumeration<String> e = (Enumeration<String>) request.getHeaderNames(); e.hasMoreElements();) {
	                String headerName = (String) e.nextElement();
	                // Ignore cookie - treat them separately
	                if (headerName.equalsIgnoreCase("cookie")) { // $NON-NLS-1$
	                    continue;
	                }
	                // Ensure that the header is allowed
	                if(true/*isHeaderAllowed(headerName)*/) {
	                    String headerValue = request.getHeader(headerName);
	                    httpCon.addRequestProperty(headerName, headerValue);
	                }
	            }
            }
            
            httpCon.connect();
            log("Connection to the target URL is established");
            return httpCon;
        } catch (MalformedURLException ex) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, response, "Invalid URL", ex);
        } catch (Exception ex) {
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, "Failed to establish connection to the target URL '" + url + "'", ex);
        }
        return null;
    }
    
    /**
     * Copies the content from the HTTP connection to the response payload, sets the
     * response content length and type. If failed to read the target content, error
     * is sent to the original request initiator
     * 
     * @param con
     * @param response the response to be sent to the original request initiator
     * @param contentType the type of the response content
     * @exception IOException if failed to write the response content
     */
    private void setResponseContent(HttpURLConnection con, HttpServletResponse response) throws IOException {
        int count = 0;
        ByteArrayOutputStream out = null;
        try {
            int len = con.getContentLength();
            log("Target content length=" + len);
            if(len>0) {
	            // First put content into temporary buffer to check the length. Sometimes
	            // the target content length is unknown (-1), so we just need to read 
	            // until EOF is reached. Otherwise read at most len bytes
	            InputStream in = con.getInputStream();
	            out = new ByteArrayOutputStream();
	            int next = in.read();
	            while ((len < 0 || count < len) && next != -1) {
	                out.write(next);
	                count++;
	                next = in.read();
	            }
            }
        } catch (IOException ex) {
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, "Failed to retrieve the target content", ex);
            return;
        }
        
        response.setStatus(con.getResponseCode());
        response.setContentType(con.getContentType());
        response.setContentLength(count);
        if(count>0) {
        	response.getOutputStream().write(out.toByteArray());
        }
    }
    
    /**
     * Prints exception details to the log and sends error response. Exception
     * details are not included into the response.
     * 
     * @param status the status code to use
     * @param response the HttpServletResponse object to use for sending the response
     * @param msg the message to send
     * @param ex the exception that occurred
     */
    private void sendError(final int status,
                           final HttpServletResponse response,
                           final String msg,
                           final Exception ex)
    {
        log("Failed to handle request", ex);
        sendError(status, response, msg);
    }
    
    /**
     * Sends given status and message to the client; if failed to send, prints
     * failure details to log and rethrows the exception
     * 
     * @param status the HTTP status code to send
     * @param response the HttpServletResponse object to use for sending the response.
     * @param msg the message to send
     */
    private void sendError(final int status,
                           final HttpServletResponse response,
                           final String msg) {
        try {
            log("ERROR (" + status + "): " + msg);
            response.sendError(status, msg);
        } catch (IOException ex) {
            log("Failed to send error response", ex);
        }
    }
    
    /**
     * Prints values of all request headers to the servlet log file.
     * 
     * @param request the request
     */
    private void printRequest(HttpServletRequest request) {
        StringBuffer buf = new StringBuffer(
            "\n***********************************************************\n" +
            request.getMethod() + " " +
            request.getRequestURL().toString() + "\n\nParameters:\n");
        
        // Print all request parameters
        Map.Entry param;
        Iterator iter = request.getParameterMap().entrySet().iterator();
        while (iter.hasNext())
        {
            param = (Map.Entry)iter.next();
            buf.append(param.getKey() + " = " + ((String[])param.getValue())[0] + "\n");
        }
        
        // Print request headers
        buf.append("\nHeaders:\n");
        Enumeration headers = request.getHeaderNames();
        String name;
        while (headers.hasMoreElements())
        {
            name = (String)headers.nextElement();
            buf.append(name + ": " + request.getHeader(name) + "\n");
        }
        buf.append("***********************************************************\n");
        
        log("Original HTTP request " + buf.toString());
    }
}