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
package com.ibm.sbt.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.HtmlTextUtil;
import com.ibm.commons.util.StringUtil;

/**
 * Provides a base class for HTTP servlets. <code>BaseHttpServlet</code> provides utility methods
 * for returning standard responses and handling exceptions.
 * 
 * @author priand
 * @author mwallace
 *
 */
@SuppressWarnings("deprecation")
abstract public class BaseHttpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @param config
	 * @param paramToolkitUrl
	 * @param defaultToolkitUrl
	 * @return
	 */
	protected String getInitParameter(ServletConfig config, String name, String defaultValue) {
		String value = config.getInitParameter(name);
		return StringUtil.isEmpty(value) ? defaultValue : value;
	}
	
	/**
	 * 
	 * @param application
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	protected String getAppParameter(Application application, String name, String defaultValue) {
		String value = application.getProperty(name);
		return StringUtil.isEmpty(value) ? defaultValue : value;
	}
	
	/**
	 * Send a 404 error response with the specified formatted message.
	 * 
	 * @param request
	 * @param response
	 * @param fmt
	 * @param parameters
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void service400(HttpServletRequest request, HttpServletResponse response, String fmt, Object...parameters) throws ServletException, IOException {
        String s = StringUtil.format(fmt, parameters);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType( "text/html" ); 
        PrintWriter w = new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"utf-8")); 
        try {
            w.println("<html>");
            w.println("<body>");
            w.println("<h1>Error 400</h1>");
            w.println(StringUtil.format("URL {0}",UrlUtil.getRequestUrl(request)));
            w.println("<br/>");
            w.println("<br/>");
            w.println(HtmlTextUtil.toHTMLContentString(s, true));
            w.println("</body>");
            w.println("</html>");
        } finally {
            w.flush();
        }
    }
    
	/**
	 * Send a 404 error response with the specified formatted message.
	 * 
	 * @param request
	 * @param response
	 * @param fmt
	 * @param parameters
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void service404(HttpServletRequest request, HttpServletResponse response, String fmt, Object...parameters) throws ServletException, IOException {
        String s = StringUtil.format(fmt, parameters);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType( "text/html" ); 
        PrintWriter w = new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"utf-8")); 
        try {
            w.println("<html>");
            w.println("<body>");
            w.println("<h1>Error 404</h1>");
            w.println(StringUtil.format("URL {0}",UrlUtil.getRequestUrl(request)));
            w.println("<br/>");
            w.println("<br/>");
            w.println(HtmlTextUtil.toHTMLContentString(s, true));
            w.println("</body>");
            w.println("</html>");
        } finally {
            w.flush();
        }
    }
    
	/**
	 * Send a 500 error response with the specified formatted message.
	 * 
	 * @param request
	 * @param response
	 * @param fmt
	 * @param parameters
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void service500(HttpServletRequest request, HttpServletResponse response, String fmt, Object...parameters) throws ServletException, IOException {
        String s = StringUtil.format(fmt, parameters);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType( "text/html" ); //$NON-NLS-1$
        PrintWriter w = new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"utf-8")); 
        try {
            w.println("<html>"); 
            w.println("<body>"); 
            w.println("<h1>Error 500</h1>");
            w.println(StringUtil.format("URL {0}",UrlUtil.getRequestUrl(request)));
            w.println("<br/>");
            w.println("<br/>");
            w.println(HtmlTextUtil.toHTMLContentString(s, true));
            w.println("</body>");
            w.println("</html>");
        } finally {
            w.flush();
        }
    }
    
	/**
	 * Send a service exception response message.
	 * 
	 * @param request
	 * @param response
	 * @param t
	 * @param lang
	 * @param isRTL
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
    public static void serviceException(HttpServletRequest request, HttpServletResponse response, Throwable t, String lang, boolean isRTL) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType( "text/html" ); //$NON-NLS-1$

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"utf-8")); 

        writeErrorPageHeader(writer, "Runtime Error", lang, isRTL); 

        writer.println("<h1>"+"Unexpected runtime error"+"</h1>");
        writer.println("The runtime has encountered an unexpected error."+"<br/>"); 
        writer.println(StringUtil.format("URL {0}",UrlUtil.getRequestUrl(request)));
        writer.println("<br/>");
        writer.println("<br/>");

        boolean verbose = true;
        if(verbose) { 
            writeExceptionMessage(writer, t);
            writeExceptionTrace(writer, t, isRTL);
        }

        writeErrorPageFooter(writer);
        
        writer.flush();
    }

    //
    // Internals
    //
    
    private static void writeErrorPageHeader(PrintWriter printWriter, String title, String lang, boolean isRTL) throws IOException {
        String leading = "left";
        String dir = "ltr";
        
        if(isRTL){
            leading = "right";
            dir = "rtl";
        }
        
        if(lang != null){
            printWriter.println("<html lang=\"" + lang + "\" dir=\"" + dir + "\">"); 
        }else{
            printWriter.println("<html dir=\"" + dir + "\">"); 
        }
        
        printWriter.println("<head>"); 
        printWriter.println("<title>"+title+"</title>"); 
        printWriter.println("<style type=\"text/css\">"); 
        printWriter.println("body {margin-" + leading + ": 2em;font-family: 'Verdana', 'Arial', 'Helvetica', 'sans-serif';font-size: 9pt}");
        printWriter.println("h1 {color: #352BFF; font-size : 1.5em; background: #ddd; line-height:2.0em;}"); 
        printWriter.println("h2 {color: #352BFF; font-size : 1.2em;}"); 
        printWriter.println("pre { font-family: 'Courier new', 'Courier', 'monospace'; font-size : 10pt; line-height:1.2em;}");
        printWriter.println("pre { border : 1px solid #ddd; margin-" + leading + ": 2em; }"); 
        printWriter.println(".row { margin-" + leading + ": 2em; }"); 
        printWriter.println("</style>"); 
        printWriter.println("</head>"); 
        printWriter.println("<body>"); 
        printWriter.println("<script>");
        printWriter.println("function toggleElement(id){"); 
        printWriter.println("  var e=document.getElementById(id);"); 
        printWriter.println("  var e1=document.getElementById(id+'_collapse');");
        printWriter.println("  var e2=document.getElementById(id+'_expand');");
        printWriter.println("  if(!e || !e1 || !e2)return true;"); 
        printWriter.println("  if(e.style.display==\"none\"){"); 
        printWriter.println("    e.style.display=\"block\""); 
        printWriter.println("    e1.style.display=\"inline\""); 
        printWriter.println("    e2.style.display=\"none\""); 
        printWriter.println("  } else {"); 
        printWriter.println("    e.style.display=\"none\""); 
        printWriter.println("    e1.style.display=\"none\"");
        printWriter.println("    e2.style.display=\"inline\"");
        printWriter.println("  }");
        printWriter.println("  return true;");
        printWriter.println("}");
        printWriter.println("</script>"); 
    }
    
    private static void writeErrorPageFooter(PrintWriter printWriter) throws IOException {
        printWriter.println("</body>"); 
        printWriter.println("</html>"); 
    }

    private static void writeExceptionMessage(PrintWriter printWriter, Throwable t) {
        printWriter.println("<br/><h2>"+"Exception"+"</h2>"); 
        String lastMessage = null; // Do not duplicate messages...
        for (Throwable tt=t; tt != null; tt = getCause(tt)) {
            String s = tt.getLocalizedMessage();
            if (StringUtil.isNotEmpty(s) && !StringUtil.equals(lastMessage, s)) {
                printWriter.print("<span class=\"row\">"+s+"</span>"); 
                printWriter.println("<br/>");
                lastMessage = s;
            }
        }
    }
    
    private static void writeExceptionTrace(PrintWriter writer, Throwable e, boolean isRTL) {
        String leading = "left"; 
        String stackIcon = "&#x25BA;";
        
        if(isRTL){
            leading = "right"; 
            stackIcon = "&#x25C4;";
        }
        
        writer.println("<p style=\"margin-" + leading + ": 2em;\">"); 
        writer.println("<a id=\"stacktrace_expand\" onclick=\"toggleElement('stacktrace')\" style=\"cursor:pointer\">" + stackIcon + "</a>"); 
        writer.println("<a id=\"stacktrace_collapse\" onclick=\"toggleElement('stacktrace')\" style=\"display:none;cursor:pointer\">&#x25BC; </a>"); 
        writer.println("<b>"+"Stack Trace"+"</b><br/>"); 
        writer.println("<pre id='stacktrace' style='display:none'>");
        writeExceptionTrace2(writer, e, 0);
        writer.println("</pre>"); 
        writer.println("</p>"); 
    }
    
    private static void writeExceptionTrace2(PrintWriter writer, Throwable e, int rows) {
        if(e!=null) {
            StackTraceElement[] elt = e.getStackTrace();
            writer.println(e.toString()); 
            for(int i=0; i<elt.length; i++) {
                writer.println("    "+elt[i].toString()); 
                
                // Limit the size of the trace, in case of an stack overflow exception...
                rows++;
                if(rows>200) {
                    return;
                }
            }
            writeExceptionTrace2(writer, getCause(e), rows);
        }
    }

    private static Throwable getCause(Throwable ext) {
        if(ext instanceof ServletException) {
            Throwable cause = ((ServletException)ext).getRootCause();
            if(cause!=null) {
                return cause;
            }
        }
        if(ext instanceof SQLException) {
            Throwable cause = ((SQLException)ext).getNextException();
            if(cause!=null) {
                return cause;
            }
        }
        
        Throwable cause = ext.getCause();
        return cause;
    }
 	
}
