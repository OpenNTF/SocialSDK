package nsf.playground.playground;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nsf.playground.beans.DataAccessBean;
import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.playground.extension.JavaScriptPreviewExtension;
import com.ibm.sbt.playground.extension.PlaygroundExtensionFactory;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.sbtsdk.servlets.JavaScriptLibraries;


public class PreviewJavaScriptHandler extends PreviewHandler {
	
	protected static class DominoPlaygroundContext extends JavaScriptPreviewExtension.Context {
		
		private PlaygroundEnvironment environment;
		private Properties properties;
		private JavaScriptLibraries.JSLibrary jsLibrary;
		
		protected DominoPlaygroundContext(PlaygroundEnvironment environment, Properties properties, JavaScriptLibraries.JSLibrary jsLibrary) {
			this.environment = environment;
			this.properties = properties; 
			this.jsLibrary = jsLibrary;
		}
		@Override
		public PlaygroundEnvironment getEnvironment() {
			return environment;
		}
		@Override
		public Properties getProperties() {
			return properties;
		}
		@Override
		public String getJavaScriptLibrary() {
			return jsLibrary.getLibType().toString();
		}
		@Override
		public String getJavaScriptLibraryVersion() {
			return jsLibrary.getLibVersion();
		}
	}
	
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
			pw.println("Social Business Toolkit Playground - JavaScript Snippet Preview Servlet");
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
			// Pass the properties from the file
			if(StringUtil.isNotEmpty(requestParams.properties)) {
				properties.load(new ReaderInputStream(new StringReader(requestParams.properties)));
			}
		} catch(Exception ex) {}

		DataAccessBean dataAccess = DataAccessBean.get();
		
		String envName = options.getString("env");
		PlaygroundEnvironment env = dataAccess.getEnvironment(envName);
		env.prepareEndpoints();

		// Push the dynamic parameters to the user session
		JsonObject p = (JsonObject)options.get("params");
		if(p!=null) {
			for(Iterator<String> it= p.getJsonProperties(); it.hasNext(); ) {
				String name = it.next();
				String value = (String)p.getJsonProperty(name);
				env.pushSessionParams(name, value);
			}
		}
		
		String serverUrl = composeServerUrl(req);
		String dbUrl = composeDatabaseUrl(req,serverUrl);
		
		//DojoLibrary dojoLib = DojoLibraryFactory.getDefaultLibrary();
		int libIdx = Math.max(0, options.getInt("lib"));
		JavaScriptLibraries.JSLibrary jsLib = JavaScriptLibraries.LIBRARIES[libIdx]; 
		
		String jsLibraryPath = jsLib.getLibUrl();
		if(libIdx==0) {
			jsLibraryPath = getDefautLibraryPath(serverUrl);
		}
		
		DominoPlaygroundContext pgContext = new DominoPlaygroundContext(env, properties, jsLib);
		List<JavaScriptPreviewExtension> pgExtensions = (List<JavaScriptPreviewExtension>)(List)PlaygroundExtensionFactory.getExtensions(JavaScriptPreviewExtension.class);
		
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
		
		// Extension: head starts
		for(int i=0; i<pgExtensions.size(); i++) {
			pgExtensions.get(i).headerStart(pgContext,pw);
		}
		
		boolean isDojo = jsLib.getLibType()==JavaScriptLibraries.LibType.DOJO;
		if(StringUtil.equals(theme, "bootstrap")) {
			pw.println("  <style type=\"text/css\">");
//			pw.println("    @import \""+dojoPath+"dijit/themes/claro/claro.css\";");
//			pw.println("    @import \""+dojoPath+"dojo/resources/dojo.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.sbtsdk/bootstrap/css/bootstrap.min.css\";");
			pw.println("  </style>");
//			bodyTheme = "claro";
		} else if( (ExtLibUtil.isXPages853() && StringUtil.equals(theme, "oneui")) || StringUtil.equals(theme, "oneuiv2.1")) {
			pw.println("  <style type=\"text/css\">");
			if(isDojo) {
				pw.println("    @import \""+jsLibraryPath+"dijit/themes/claro/claro.css\";");
				pw.println("    @import \""+jsLibraryPath+"dojo/resources/dojo.css\";");
				pw.println("    @import \""+jsLibraryPath+"dijit/themes/dijit.css\";");
			}
			pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/base/core.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/base/dojo.css\";");
			if(isDojo) {
				pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/defaultTheme/defaultTheme.css\";");
				pw.println("    @import \"/xsp/.ibmxspres/domino/oneuiv2.1/defaultTheme/dojoTheme.css\";");
			}
			pw.println("  </style>");
			bodyTheme = "lotusui";
		//} else if( (ExtLibUtil.isXPages900() && StringUtil.equals(theme, "oneui")) || StringUtil.equals(theme, "oneui302")) {
		} else if( (!ExtLibUtil.isXPages853() && StringUtil.equals(theme, "oneui")) || StringUtil.equals(theme, "oneui302")) {
			pw.println("  <style type=\"text/css\">");
			if(isDojo) {
				pw.println("    @import \""+jsLibraryPath+"dijit/themes/claro/claro.css\";");
				pw.println("    @import \""+jsLibraryPath+"dojo/resources/dojo.css\";");
				pw.println("    @import \""+jsLibraryPath+"dijit/themes/dijit.css\";");
			}
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/base/core.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/base/dojo.css\";");
			pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/defaultTheme/defaultTheme.css\";");
			if(isDojo) {
				pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/css/defaultTheme/dojoTheme.css\";");
				pw.println("    @import \"/xsp/.ibmxspres/.oneuiv302/oneui/dojoTheme/lotusui30dojo/lotusui30dojo.css\";");
			}
			pw.println("  </style>");
			bodyTheme = "lotusui30_body lotusui30_fonts lotusui30 lotusui30dojo";
		} else if(StringUtil.equals(theme, "dojo") || StringUtil.equals(theme, "dojo-claro")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+jsLibraryPath+"dijit/themes/claro/claro.css\";");
			pw.println("    @import \""+jsLibraryPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "claro";
		} else if(StringUtil.equals(theme, "dojo-tundra")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+jsLibraryPath+"dijit/themes/tundra/tundra.css\";");
			pw.println("    @import \""+jsLibraryPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "tundra";
		} else if(StringUtil.equals(theme, "dojo-soria")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+jsLibraryPath+"dijit/themes/soria/soria.css\";");
			pw.println("    @import \""+jsLibraryPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "soria";
		} else if(StringUtil.equals(theme, "dojo-nihilo")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+jsLibraryPath+"dijit/themes/nihilo/nihilo.css\";");
			pw.println("    @import \""+jsLibraryPath+"dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "soria";
		}

		switch(jsLib.getLibType()) {
			case DOJO: {
				jsLibraryPath = PathUtil.concat(jsLibraryPath,"/dojo/dojo.js",'/');
				pw.println("  <script type=\"text/javascript\">");
				pw.println("  	dojoConfig = {");
				if(jsLib.isAsync()) {
					pw.println("  	    async: true,");
				}
				if(debug) {
					pw.println("        isDebug: true,");
				}
				pw.println("  	    parseOnLoad: true");
				pw.println("  	};");
				pw.println("  </script>");
				pw.println("  <script type=\"text/javascript\" src=\""+jsLibraryPath+"\"></script>");
			} break;
			case JQUERY: {
				if(true) {
					String jqueryPath = PathUtil.concat(jsLibraryPath,"/jquery.min",'/');
					String jqueryUiPath = PathUtil.concat(jsLibraryPath,"/jquery-ui.min",'/');
					//String jqueryUiCssPath = PathUtil.concat(jsLibraryPath,"/themes/base/jquery-ui.css",'/');
					String jqueryUiCssPath = "//ajax.googleapis.com/ajax/libs/jqueryui/1.8.4/jquery-ui.min.js";
					pw.println("  <script type=\"text/javascript\" src=\"/xsp/.ibmxspres/.sbtsdk/js/libs/require.js\"></script>");
					pw.println("  <script type=\"text/javascript\">");
					pw.println("  	requirejs.config({");
					pw.println("  	  paths: {");
					pw.println("        'jquery' : '"+jqueryPath+"',");
					pw.println("        'jqueryui' : '"+jqueryUiPath+"'");
					pw.println("  	  },");
					pw.println("  	  shim: {");
					pw.println("        'jquery/ui': {");
					pw.println("          deps: ['jquery'],");
					pw.println("          exports: '$'");
					pw.println("        }");
					pw.println("  	  }");
					pw.println("  	});");
					pw.println("  </script>");
					pw.println("  <link rel=\"stylesheet\" type=\"text/css\" title=\"Style\" href=\""+jqueryUiCssPath+"\">");
				} else {
					jsLibraryPath = PathUtil.concat(jsLibraryPath,"/jquery.min.js",'/');
					pw.println("  <script type=\"text/javascript\" src=\"/xsp/.ibmxspres/.sbtsdk/js/libs/require.js\"></script>");
					pw.println("  <script type=\"text/javascript\" src=\""+jsLibraryPath+"\"></script>");
				}
			} break;
		}
		
		String libType = jsLib.getLibType().toString();
		String libVersion = jsLib.getLibVersion();
		pw.print("  <script type=\"text/javascript\" src=\""+composeToolkitUrl(dbUrl)+"?lib="+libType+"&ver="+libVersion);
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
		if(isDojo) {
			pw.println("  <script>");
			pw.println("    require(['dojo/parser']);"); // avoid dojo warning
			pw.println("  </script>");
		}
		
		// Add the firebug lite debugging tools
		if(debug) {
			pw.println("  <script type=\"text/javascript\" src=\"/xsp/.ibmxspres/.extlib/firebug/js/firebug-lite.js\"></script>\n");
		}

		// Extension: head ends
		for(int i=0; i<pgExtensions.size(); i++) {
			pgExtensions.get(i).headerEnd(pgContext,pw);
		}
		
		pw.println("</head>");
		pw.print("<body");
		if(StringUtil.isNotEmpty(bodyTheme)) {
			pw.print(" class=\"");
			pw.print(bodyTheme);
			pw.print("\"");
		}
		pw.println(">");
		pw.print("<div  id='_jsErrors'>");
		pw.print("</div>");
		pw.print("<script type='text/javascript'>");
		pw.print("  window.onerror = function(msg, url, linenumber) {");
		pw.print("    var d =  document.createElement('div');");
		pw.print("    d.innerHTML += 'Unhandled error: '+msg.replace('<', '&lt;').replace('>', '&gt;')+'<br> in page: '+url+'<br>at: '+linenumber;");
		pw.print("    document.getElementById('_jsErrors').appendChild(d);");
		pw.print("    return true;");
		pw.print("  }");
		pw.print("</script>");

		// Extension: body starts
		for(int i=0; i<pgExtensions.size(); i++) {
			pgExtensions.get(i).bodyStart(pgContext,pw);
		}

		pw.println(html);
		if(StringUtil.isNotEmpty(js)) {
			String s =   "<script>\n"
						+js
						+"</script>\n";
			pw.println(s);
		}		

		// Extension: body ends
		for(int i=0; i<pgExtensions.size(); i++) {
			pgExtensions.get(i).bodyEnd(pgContext,pw);
		}
		
		pw.println("</body>");
		pw.println("</html>");
	    		
		pw.flush();
		pw.close();
	}	
}
