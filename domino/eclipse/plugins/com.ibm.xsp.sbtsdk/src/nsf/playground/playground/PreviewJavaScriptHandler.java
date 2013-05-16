package nsf.playground.playground;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.playground.beans.DataAccessBean;
import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.context.DojoLibraryFactory;
import com.ibm.xsp.extlib.util.ExtLibUtil;


public class PreviewJavaScriptHandler extends PreviewHandler {
	
	public static class JSLibrary {

		private String label;
		private String libType;
		private String libVersion;
		private String libUrl;
		public JSLibrary(String label, String libType, String libVersion, String libUrl) {
			this.label = label;
			this.libType = libType;
			this.libVersion = 
			this.libUrl = libUrl;
		}
	}
	// See: https://developers.google.com/speed/libraries/devguide
//	public static JSLibrary[] LIBRARIES = {
//		new JSLibrary("<default>","dojo","1.6.1",""),
//		//new JSLibrary("Dojo Toolkit 1.4.3","dojo","1.4.3","//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js"),
//		new JSLibrary("Dojo Toolkit 1.4.3","dojo","1.4.3","//ajax.googleapis.com/ajax/libs/dojo/1.4.3"),
//		new JSLibrary("Dojo Toolkit 1.5.2","dojo","1.5.2","//ajax.googleapis.com/ajax/libs/dojo/1.5.2"),
//		new JSLibrary("Dojo Toolkit 1.6.1","dojo","1.6.1","//ajax.googleapis.com/ajax/libs/dojo/1.6.1"),
//		new JSLibrary("Dojo Toolkit 1.7.4","dojo","1.7.4","//ajax.googleapis.com/ajax/libs/dojo/1.7.4"),
//		new JSLibrary("Dojo Toolkit 1.8.4","dojo","1.8.4","//ajax.googleapis.com/ajax/libs/dojo/1.8.4"),
//		new JSLibrary("Dojo Toolkit 1.9.0","dojo","1.9.0","//ajax.googleapis.com/ajax/libs/dojo/1.9.0"),
//		//new JSLibrary("JQuery 1.8.3","jquery","1.8.3","//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"),
//		new JSLibrary("JQuery 1.8.3","jquery","1.8.3","//ajax.googleapis.com/ajax/libs/jquery/1.8.3"),
//	};

	private static final String LAST_REQUEST = "javascriptsnippet.lastrequest"; 

	static class RequestParams implements Serializable {
		private static final long serialVersionUID = 1L;
		String sOptions;
		String html;
		String js;
		String css;
		String properties;
        public RequestParams(String sOptions, String html, String js, String css, String properties) {
        	this.sOptions = sOptions;
        	this.html = html;
        	this.js = js;
        	this.css = css;
        	this.properties = properties;
        }
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sOptions = req.getParameter("fm_options");
		String html = req.getParameter("fm_html");
		String js = req.getParameter("fm_js");
        String css = req.getParameter("fm_css");
        String properties = req.getParameter("fm_properties");
        RequestParams requestParams = new RequestParams(sOptions,html,js,css,properties);
        req.getSession().setAttribute(LAST_REQUEST, requestParams);
        resp.sendRedirect(UrlUtil.getRequestUrl(req));
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestParams requestParams = (RequestParams)req.getSession().getAttribute(LAST_REQUEST);
		if(requestParams!=null) {
	        execRequest(req, resp, requestParams);
		} else {
			PrintWriter pw = resp.getWriter();
			pw.println("Social Business Tooolkit Playground - JavaScript Snippet Preview Servlet");
			pw.flush();
		}
	}

	protected void execRequest(HttpServletRequest req, HttpServletResponse resp, RequestParams requestParams) throws ServletException, IOException {
		resp.setContentType("text/html");

		String sOptions = requestParams.sOptions;
		JsonJavaObject options = new JsonJavaObject();
		try {
			options = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, sOptions);
		} catch(Exception ex) {}
		boolean debug = options.getBoolean("debug");
		
		Properties properties = new Properties();
		try {
			if(StringUtil.isNotEmpty(requestParams.properties)) {
				properties.load(new ReaderInputStream(new StringReader(requestParams.properties)));
			}
		} catch(Exception ex) {}

		DataAccessBean dataAccess = DataAccessBean.get();
		
		String envName = options.getString("env");
		PlaygroundEnvironment env = dataAccess.getEnvironment(envName);
		env.prepareEndpoints();
		
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
		
		String theme = properties.getProperty("theme");
		String bodyTheme = null;
		
		pw.println("<!DOCTYPE html>");
		pw.println("<html lang=\"en\">");
		//pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		//pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		
		pw.println("<head>");
		pw.println("  <title>Social Business Playground</title>");
		
		if(StringUtil.equals(theme, "bootstrap")) {
			pw.println("  <style type=\"text/css\">");
//			pw.println("    @import \""+dojoPath+"dijit/themes/claro/claro.css\";");
//			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.sbtsdk/bootstrap/css/bootstrap.min.css\";");
			pw.println("  </style>");
//			bodyTheme = "claro";
		} else if( (ExtLibUtil.isXPages853() && StringUtil.equals(theme, "oneui")) || StringUtil.equals(theme, "oneuiv2.1")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+dojoPath+"dijit/themes/claro/claro.css\";");
			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("    @import \""+dojoPath+"dijit/themes/dijit.css\";");			
			pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/base/core.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/base/dojo.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/defaultTheme/defaultTheme.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/defaultTheme/dojoTheme.css\";");
			pw.println("  </style>");
			bodyTheme = "lotusui";
		//} else if( (ExtLibUtil.isXPages900() && StringUtil.equals(theme, "oneui")) || StringUtil.equals(theme, "oneui302")) {
		} else if( (!ExtLibUtil.isXPages853() && StringUtil.equals(theme, "oneui")) || StringUtil.equals(theme, "oneui302")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+dojoPath+"dijit/themes/claro/claro.css\";");
			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("    @import \""+dojoPath+"dijit/themes/dijit.css\";");			
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/base/core.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/base/dojo.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/defaultTheme/defaultTheme.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/defaultTheme/dojoTheme.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/dojoTheme/lotusui30dojo/lotusui30dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "lotusui30_body lotusui30_fonts lotusui30 lotusui30dojo";
		} else if(StringUtil.equals(theme, "dojo") || StringUtil.equals(theme, "dojo-claro")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+dojoPath+"dijit/themes/claro/claro.css\";");
			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "claro";
		} else if(StringUtil.equals(theme, "dojo-tundra")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+dojoPath+"dijit/themes/tundra/tundra.css\";");
			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "tundra";
		} else if(StringUtil.equals(theme, "dojo-soria")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+dojoPath+"dijit/themes/soria/soria.css\";");
			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "soria";
		} else if(StringUtil.equals(theme, "dojo-nihilo")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+dojoPath+"dijit/themes/nihilo/nihilo.css\";");
			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "soria";
		}
		pw.println("  <script>");
		pw.println("    dojoConfig= {");
		pw.println("      parseOnLoad: true,");
		if(debug) {
			pw.println("      isDebug: true");
		}
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
		pw.print("<body");
		if(StringUtil.isNotEmpty(bodyTheme)) {
			pw.print(" class=\"");
			pw.print(bodyTheme);
		}
		pw.println("\">");
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
