package nsf.playground.playground;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.playground.beans.DataAccessBean;
import nsf.playground.beans.OptionsBean;
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
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.context.DojoLibraryFactory;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.minifier.DojoDependencyList;
import com.ibm.xsp.minifier.DojoResource;
import com.ibm.xsp.minifier.MinifierResourceProvider;
import com.ibm.xsp.minifier.ResourceFactory;
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
		if(jsLibraryPath.endsWith("/")) {
			jsLibraryPath = jsLibraryPath.substring(0,jsLibraryPath.length()-1);
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
				pw.println("    @import \""+jsLibraryPath+"/dijit/themes/claro/claro.css\";");
				pw.println("    @import \""+jsLibraryPath+"/dojo/resources/dojo.css\";");
				pw.println("    @import \""+jsLibraryPath+"/dijit/themes/dijit.css\";");
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
				pw.println("    @import \""+jsLibraryPath+"/dijit/themes/claro/claro.css\";");
				pw.println("    @import \""+jsLibraryPath+"/dojo/resources/dojo.css\";");
				pw.println("    @import \""+jsLibraryPath+"/dijit/themes/dijit.css\";");
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
			pw.println("    @import \""+jsLibraryPath+"/dijit/themes/claro/claro.css\";");
			pw.println("    @import \""+jsLibraryPath+"/dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "claro";
		} else if(StringUtil.equals(theme, "dojo-tundra")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+jsLibraryPath+"/dijit/themes/tundra/tundra.css\";");
			pw.println("    @import \""+jsLibraryPath+"/dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "tundra";
		} else if(StringUtil.equals(theme, "dojo-soria")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+jsLibraryPath+"/dijit/themes/soria/soria.css\";");
			pw.println("    @import \""+jsLibraryPath+"/dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "soria";
		} else if(StringUtil.equals(theme, "dojo-nihilo")) {
			pw.println("  <style type=\"text/css\">");
			pw.println("    @import \""+jsLibraryPath+"/dijit/themes/nihilo/nihilo.css\";");
			pw.println("    @import \""+jsLibraryPath+"/dojo/resources/dojo.css\";");
			pw.println("  </style>");
			bodyTheme = "soria";
		}
		
		int jsAggregator = OptionsBean.JS_AGG_NONE;
		switch(jsLib.getLibType()) {
			case DOJO: {
				// Only for the default library, 1.8.1
				if(libIdx==0) {
					jsAggregator = OptionsBean.get().getJavaScriptAggregator();
				}
		        				
				switch(jsAggregator) {
					case OptionsBean.JS_AGG_NONE:
					case OptionsBean.JS_AGG_XPAGES: {
						pw.println("  <script type=\"text/javascript\">");
						pw.println("  	dojoConfig = {");
						pw.println("  	    parseOnLoad: true,");
						if(jsLib.isAsync()) {
							pw.println("  	    async: true,");
						}
						if(debug) {
							pw.println("        isDebug: true,");
						}
						pw.println("  	};");
						pw.println("  </script>");
						String dojoPath = PathUtil.concat(jsLibraryPath,"/dojo/dojo.js",'/');
						pw.println("  <script type=\"text/javascript\" src=\""+dojoPath+"\"></script>");
						if(jsAggregator==OptionsBean.JS_AGG_XPAGES) {
							String xpagesLayer = getXPagesLayer(serverUrl,requestParams.js,true);
							pw.println("  <script type=\"text/javascript\" src=\""+xpagesLayer+"\"></script>");
						}
					} break;
					case OptionsBean.JS_AGG_SDK: {
						// Using the dojo layers
						//jsLibraryPath = PathUtil.concat(jsLibraryPath,"/dojo/dojo.js",'/');
						pw.println("  <script type=\"text/javascript\">");
						pw.println("  	dojoConfig = {");
						pw.println("  	    parseOnLoad: true,");
						if(jsLib.isAsync()) {
							pw.println("  	    async: true,");
						}
						if(debug) {
							pw.println("        isDebug: true,");
						}
						pw.println("        packages: [");
						// https://dominosbt/xsp/.ibmxspres/dojoroot-1.8.1/dojo/dojo.js
						pw.println("          {name:'dojo',   location:'"+jsLibraryPath+"/dojo'},");
						pw.println("          {name:'dijit',  location:'"+jsLibraryPath+"/dijit'},");
						pw.println("          {name:'dojox',  location:'"+jsLibraryPath+"/dojox'},");
						// https://dominosbt/xsp/.ibmxspres/.sbtsdk/js/sdk/sbt
						String sbtPath = getDefautSbtPath(serverUrl);
						//pw.println("          {name:'sbt/_bridge', location:'"+sbtPath+"/js/sdk/_bridges/dojo-amd'},");
						pw.println("          {name:'sbt',    location:'"+sbtPath+"/js/sdk/sbt'}");
						pw.println("        ],");
						//https://dominosbt/xsp/.ibmxspres/.sbtsdk/js/sdk/_bridges/dojo-amd/Transport.js
						String libVersion = jsLib.getLibVersion();
						pw.println("        paths: {");
						pw.println("        	'sbt/_config': '"+composeToolkitUrl(dbUrl)+"?lib=dojo&ver="+libVersion+"&layer=true&noext',");
						pw.println("        	'sbt/_bridge': '"+sbtPath+"/js/sdk/_bridges/dojo-amd',");
						pw.println("        	'sbt/widget': '"+sbtPath+"/js/sdk/dojo2'");
						pw.println("        },");
						//https://dominosbt/xsp/.ibmxspres/.sbtsdk/js/sdk/_layers/sbt-core-dojo-amd.js
						pw.println("        deps: [");
						String xpagesLayer = getXPagesLayer(serverUrl,requestParams.js,false);
						pw.println("        	'"+xpagesLayer+"',");
						//pw.println("        	'"+sbtPath+"/js/sdk/_layers/sbt-core-dojo-amd.js',");
						pw.println("        	'"+sbtPath+"/js/sdk/_layers/sbt-extra-controls-dojo-amd.js'");
						pw.println("        ]");
						//pw.println("        paths: { 'sbt/_config' : '"+composeToolkitUrl(dbUrl)+"?lib=dojo&ver="+libVersion+"&debug=true&layer=true&noext'},");
						//pw.println("        deps: ['/sbt.sample.web/js/sbt-core.js.uncompressed.js']");
						pw.println("  	};");
						pw.println("  </script>");
						String dojoPath = PathUtil.concat(jsLibraryPath,"/dojo/dojo.js",'/');
						pw.println("  <script type=\"text/javascript\" src=\""+dojoPath+"\"></script>");
					} break;
				}
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
					String jqueryPath = PathUtil.concat(jsLibraryPath,"/jquery.min.js",'/');
					pw.println("  <script type=\"text/javascript\" src=\"/xsp/.ibmxspres/.sbtsdk/js/libs/require.js\"></script>");
					pw.println("  <script type=\"text/javascript\" src=\""+jqueryPath+"\"></script>");
				}
			} break;
		}
		
		// Add the library servlet, if no dojo layer is being used
		if(jsAggregator!=OptionsBean.JS_AGG_SDK) {
			String libType = jsLib.getLibType().toString();
			String libVersion = jsLib.getLibVersion();
			pw.print("  <script type=\"text/javascript\" src=\""+composeToolkitUrl(dbUrl)+"?lib="+libType+"&ver="+libVersion);
			pw.print("&env=");
			pw.print(envName);
			pw.println("\"></script>");
			//pw.println("  <script type=\"text/javascript\" src=\""+PathUtil.concat(env.getSbt(),"toolkit",'/')+"?ver=1.6.1&\"+(Stringendpoints?endpoints)+"></script>");
		}
		
		EnvParameterProvider prov = new EnvParameterProvider(env);

		// Text content for simple output
		String html = "<div id=\"content\"></div>\n";
		
		// Read and process the HTML/JS/CSS
		
		// Custom HTML
		String customHtml = requestParams.html;
		html = html + ParameterProcessor.process(customHtml, prov);
		
		// Hidden progress indicator image 
		html = html + "\n<div id='loading' style='visibility: hidden'><img src='../progressIndicator.gif' alt='loading...'></img></div>";
		
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
	
	private static Pattern amdRequireRegEx = Pattern.compile("require\\s*\\(\\s*\\[(.*)\\]");
	private static Pattern amdRequireRegEx2 = Pattern.compile("[\'\"]([^\'\"]*)[\'\"]");
	
	private String getXPagesLayer(String serverUrl, String js, boolean includeSDKModules) throws IOException {
		// XPages aggregator
        ResourceFactory factory = ResourceFactory.get();
        DojoLibrary dojoLibrary = DojoLibraryFactory.getDefaultLibrary(false);
        String dojoLocale = "en-us";
        DojoDependencyList dojoResources = new DojoDependencyList(factory,dojoLibrary,dojoLocale);

    	// Common dojo resources
        dojoResources.addResource(factory.getDojoResource("dojo.parser",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.date",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.date.locale",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.regexp",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.i18n",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.string",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.cache",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.text",dojoLibrary));
        
        // Controls
        dojoResources.addResource(factory.getDojoResource("dijit._WidgetBase",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dijit._TemplatedMixin",dojoLibrary));
        dojoResources.addResource(factory.getDojoResource("dojo.touch",dojoLibrary));
        
        if(includeSDKModules) {
        	// The resources bellow are generally needed
        	// They should not be included if the SDK layer is already loaded 
            dojoResources.addResource(factory.getDojoResource("sbt.config",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.declare",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.defer",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.lang",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.Promise",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.log",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.stringUtil",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.xml",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.i18n",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.ErrorTransport",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.Endpoint",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.pathUtil",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.Proxy",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.Cache",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.xpath",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.util",dojoLibrary));
            dojoResources.addResource(factory.getDojoResource("sbt.dom",dojoLibrary));
	        dojoResources.addResource(factory.getDojoResource("sbt.text",dojoLibrary));
            
	        // These ones don't have direct dep in the source code
	    	// The are loaded by the config
	    	//sbt/ErrorTransport', 'sbt/Endpoint', 'sbt/Proxy', 'sbt/_bridge/Transport', 'sbt/authenticator/Basic', 'sbt/util
	        dojoResources.addResource(factory.getDojoResource("sbt.ErrorTransport",dojoLibrary));
	        dojoResources.addResource(factory.getDojoResource("sbt.Endpoint",dojoLibrary));
	        dojoResources.addResource(factory.getDojoResource("sbt.Proxy",dojoLibrary));
	        dojoResources.addResource(factory.getDojoResource("sbt._bridge.Transport",dojoLibrary));
	        dojoResources.addResource(factory.getDojoResource("sbt._bridge.i18n",dojoLibrary));
	        dojoResources.addResource(factory.getDojoResource("sbt.authenticator.Basic",dojoLibrary));
	        dojoResources.addResource(factory.getDojoResource("sbt.util",dojoLibrary));
	        
	        // Extract the entries from the JS file
	        // This might be in the SDK layer already, so we don't load them if not fully using the XPages agg but the SDK one
	        if(StringUtil.isNotEmpty(js)) {
	        	Matcher matcher = amdRequireRegEx.matcher(js);
	        	while(matcher.find()) {
	        		String s = matcher.group(1);
		        	Matcher matcher2 = amdRequireRegEx2.matcher(s);
		        	while(matcher2.find()) {
		        		String mod = StringUtil.replace(matcher2.group(1),'/','.');
		        		DojoResource res = factory.getDojoResource(mod,dojoLibrary);
		        		if(res!=null) {
		        			dojoResources.addResource(res);
		        		}
		        	}
	        	}
	        }
        }
        
        
        StringBuilder b = new StringBuilder();
        b.append(MinifierResourceProvider.URL_DOJO);
        if(dojoLibrary.isUncompressed() || dojoLibrary!=DojoLibraryFactory.getDefaultLibrary(false)) {
            b.append('-');
            b.append(dojoLibrary.getVersionTag());
        }
        b.append('/');
        b.append(dojoResources.getUrlParameter());
        b.append(".js"); // $NON-NLS-1$
        String fullUrl = b.toString();
        String dojoAgg = PathUtil.concat(serverUrl, fullUrl, '/');
        return dojoAgg;
	}
}
