package nsf.playground.playground;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.playground.beans.DataAccess;
import nsf.playground.environments.PlaygroundEnvironment;


import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.context.DojoLibraryFactory;
import com.ibm.xsp.extlib.servlet.FacesContextServlet;


public class PreviewServlet extends FacesContextServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String LAST_REQUEST = "playground.lastrequest"; 
	static class RequestParams implements Serializable {
		private static final long serialVersionUID = 1L;
		String sOptions;
		String html;
		String js;
		String css;
        public RequestParams(String sOptions, String html, String js, String css) {
        	this.sOptions = sOptions;
        	this.html = html;
        	this.js = js;
        	this.css = css;
        }
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestParams requestParams = (RequestParams)req.getSession().getAttribute(LAST_REQUEST);
		if(requestParams!=null) {
	        execRequest(req, resp, requestParams);
		} else {
			PrintWriter pw = resp.getWriter();
			pw.println("Social Business Tooolkit Playground - Preview Servlet");
			pw.flush();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sOptions = req.getParameter("fm_options");
		String html = req.getParameter("fm_html");
		String js = req.getParameter("fm_js");
        String css = req.getParameter("fm_css");
        RequestParams requestParams = new RequestParams(sOptions,html,js,css);
        req.getSession().setAttribute(LAST_REQUEST, requestParams);
        execRequest(req, resp, requestParams);
	}

	protected void execRequest(HttpServletRequest req, HttpServletResponse resp, RequestParams requestParams) throws ServletException, IOException {
		resp.setContentType("text/html");

		String sOptions = requestParams.sOptions;
		JsonJavaObject options = new JsonJavaObject();
		try {
			options = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, sOptions);
		} catch(Exception ex) {}
		boolean debug = options.getBoolean("debug");
		
		DataAccess dataAccess = DataAccess.get();
		
		String envName = options.getString("env");
		PlaygroundEnvironment env = dataAccess.getEnvironment(envName);
		env.updateBeans();
		
		String serverUrl = composeServerUrl(req);
		String dbUrl = composeDatabaseUrl(req,serverUrl);
		
		//DojoLibrary dojoLib = DojoLibraryFactory.getDefaultLibrary();
		String dojoPath = getDojoPath(null,serverUrl);

//		Map m = req.getParameterMap();
//		for(Object k: m.keySet()) {
//			Object v = m.get(k);
//			System.out.println("Key:"+k+", Value:"+StringUtil.toString(v,32));
//		}
		PrintWriter pw = resp.getWriter();
		
		pw.println("<!DOCTYPE html>");
		pw.println("<html lang=\"en\">");
		//pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		//pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		
		pw.println("<head>");
		pw.println("  <title>Social Business Playground</title>");
		
		pw.println("  <style type=\"text/css\">");
		pw.println("    @import \""+dojoPath+"dijit/themes/claro/claro.css\";");
		pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
		pw.println("    @import \"/xsp/.ibmxspres/.extlib/bootstrap/css/bootstrap.min.css\";");
		pw.println("  </style>");

		pw.println("  <script>");
		pw.println("    dojoConfig= {");
		pw.println("      parseOnLoad: true,");
		pw.println("      isDebug: true");
		pw.println("    };");
		pw.println("  </script>");		

		//pw.println("  <script type=\"text/javascript\" src=\""+dojoPath+"dojo/dojo.js\"></script>");
		pw.println("  <script type=\"text/javascript\" src=\""+dojoPath+"dojo/dojo.js\" data-dojo-config=\"parseOnLoad: true\"></script>");

		
		String dojoVersion = "1.6.1"; //dojoLib.getVersionTag();
		pw.print("  <script type=\"text/javascript\" src=\""+composeToolkitUrl(dbUrl)+"?ver="+dojoVersion);
		pw.print("&env=");
		pw.print(envName);
		pw.println("\"></script>");
		//pw.println("  <script type=\"text/javascript\" src=\""+PathUtil.concat(env.getSbt(),"toolkit",'/')+"?ver=1.6.1&\"+(Stringendpoints?endpoints)+"></script>");
		
		EnvParameterProvider prov = new EnvParameterProvider(env);

		// Text content for simple output
		String html = "<div id=\"content\"></div>\n";
		
		// Read and process the HTML/JS/CSS
		
		// Custom HTML
		String customHtml = requestParams.html;
		html = html + ParameterProcessor.process(customHtml, prov);
		
		// Hidden progress indicator image 
		html = html + "\n<img id='loading' src='../progressIndicator.gif' style='visibility: hidden'></img>";
		
		// Read an process the JS
		String js = requestParams.js;
		js = ParameterProcessor.process(js, prov);
		
		// Read and process the CSS
        String css = requestParams.css;
		css = ParameterProcessor.process(css, prov);
		if(StringUtil.isNotEmpty(css)) {
			String s = "  <style>"+css+"</style>";
			pw.println(s);
		}
		
		// Script for the dojo parser
		pw.println("  <script>");
		pw.println("    require(['dojo/parser']);"); // avoid dojo warning
		pw.println("  </script>");		
		
		// Add the firebug lite debugging tools
		if(debug) {
			pw.println("  <script type=\"text/javascript\" src=\"/xsp/.ibmxspres/.extlib/firebug/js/firebug-lite.js\"></script>\n");
		}
		
		pw.println("</head>");
		pw.println("<body class=\"claro\">");
		pw.println(html);
		if(StringUtil.isNotEmpty(js)) {
			String s =   "<script>\n"
						+"try {\n"
						+js
						+"} catch(e) {;\n"
						+"  document.getElementById('content').innerHTML = 'Exception:'+e.toString();"
						+"}\n"
						+"</script>\n";
			pw.println(s);
		}
		pw.println("</body>");
		pw.println("</html>");
		
		pw.flush();
		pw.close();
	}
	
	private static class EnvParameterProvider implements com.ibm.commons.runtime.util.ParameterProcessor.ParameterProvider {
		SBTEnvironment env;
		EnvParameterProvider(SBTEnvironment env) {
			this.env = env;
		}
		public String getParameter(String name) {
			if(env!=null) {
				return env.getPropertyValueByName(name);
			}
			return null;
		}
	}

	
	private String getDojoPath(DojoLibrary lib, String serverUrl) {
//		//DOJO_URLPATH 		= "/xsp/.ibmxspres/dojoroot-1.8.0/";
//		//return serverUrl+DOJO_URLPATH;
		if(lib==null) {
			lib = DojoLibraryFactory.getDefaultLibrary();
		}
        String s = '-'+lib.getVersionTag();
		return serverUrl+"/xsp/.ibmxspres/dojoroot"+s+"/";
	}
	
	private String composeServerUrl(HttpServletRequest req) {		
//		System.out.println("scheme:"+req.getScheme());
//		System.out.println("server:"+req.getServerName());
//		System.out.println("serverPath:"+req.getServletPath());
//		System.out.println("contextPath:"+req.getContextPath());
//		System.out.println("pathInfo:"+req.getPathInfo());
//		System.out.println("BaseURL:"+b.toString());
		
		StringBuilder b = new StringBuilder();
		String scheme = req.getScheme();
		String server = req.getServerName();
		int port = req.getServerPort();
		
		b.append(scheme);
		b.append("://");
		b.append(server);
		if( !(((port==80)&&scheme.equals("http")) || ((port==443)&&scheme.equals("https"))) ) {
			b.append(':');
			b.append(Integer.toString(port));
		}
		
		return b.toString();
	}
	
	private String composeDatabaseUrl(HttpServletRequest req, String serverUrl) {
		String contextPath = req.getContextPath();
		return serverUrl+contextPath;
	}
	
	private String composeToolkitUrl(String databaseUrl) {
		return PathUtil.concat(databaseUrl,RuntimeConstants.get().getConstant(RuntimeConstants.LIBRARY_BASEURL),'/');
	}
}
