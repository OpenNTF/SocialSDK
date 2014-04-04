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
import nsf.playground.extension.JavaScriptPreviewExtension;
import nsf.playground.extension.PlaygroundExtensionFactory;

import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.context.DojoLibraryFactory;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.minifier.DojoDependencyList;
import com.ibm.xsp.minifier.DojoResource;
import com.ibm.xsp.minifier.MinifierResourceProvider;
import com.ibm.xsp.minifier.ResourceFactory;
import com.ibm.xsp.sbtsdk.servlets.JavaScriptLibraries;


public class PreviewJavaScriptHandler extends PreviewHandler {

	private static final String LAST_REQUEST = "javascriptsnippet.lastrequest"; 

	public static class RequestParams implements Serializable {
		private static final long serialVersionUID = 1L;
		public String html;
		public String js;
		public String css;
		public JsonJavaObject options = new JsonJavaObject();
		public Properties properties = new Properties();
        public RequestParams(String sOptions, String html, String js, String css, String sProperties) {
        	this.html = html;
        	this.js = js;
        	this.css = css;
        	if(StringUtil.isNotEmpty(sOptions)) {
				try {
					options = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, sOptions);
				} catch(Exception ex) {}
        	}
        	if(StringUtil.isNotEmpty(sProperties)) {
    			try {
    				// Pass the properties from the file
    				if(StringUtil.isNotEmpty(sProperties)) {
    					this.properties.load(new ReaderInputStream(new StringReader(sProperties)));
    				}
    			} catch(Exception ex) {}
        	}
        }
	}
	
	// Utilities...
	protected static Pattern amdRequireRegEx = Pattern.compile("require\\s*\\(\\s*\\[(.*)\\]");
	protected static Pattern amdRequireRegEx2 = Pattern.compile("[\'\"]([^\'\"]*)[\'\"]");

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
	        findRenderer(req, resp, requestParams).execRequest();
		} else {
			PrintWriter pw = resp.getWriter();
			pw.println("Social Business Toolkit Playground - JavaScript Snippet Preview Servlet");
			pw.flush();
		}
	}
	
	public Renderer findRenderer(HttpServletRequest req, HttpServletResponse resp, RequestParams requestParams) throws IOException {
		List<JavaScriptPreviewExtension> pgExtensions = (List<JavaScriptPreviewExtension>)PlaygroundExtensionFactory.getExtensions(JavaScriptPreviewExtension.class);
		
		// Find the first renderer that handles it, from the registered extensions
		Renderer r = null;
		for(int i=0; i<pgExtensions.size(); i++) {
			r = pgExtensions.get(i).findRenderer(req, resp, requestParams, false);
			if(r!=null) {
				break;
			}
		}
		if(r==null) {
			for(int i=0; i<pgExtensions.size(); i++) {
				r = pgExtensions.get(i).findRenderer(req, resp, requestParams, true);
				if(r!=null) {
					break;
				}
			}
		}
		if(r==null) {
			r = new Renderer(req, resp, requestParams);
		}
		r.init();
		return r;
	}

	public static class Renderer extends BaseRenderer {
		
		protected HttpServletRequest req;
		protected HttpServletResponse resp;
		protected RequestParams requestParams;
		
		protected boolean debug;
		protected String envName;
		protected PlaygroundEnvironment env;
		protected EnvParameterProvider prov;
		protected JavaScriptLibraries.JSLibrary jsLib;
		protected String jsLibraryPath;
		protected boolean isDojo;
		protected String serverUrl;
		protected String dbUrl;

		protected String bodyTheme;
		
		
		protected Renderer(HttpServletRequest req, HttpServletResponse resp, RequestParams requestParams) throws IOException {
			this.req = req;
			this.resp = resp;
			this.requestParams = requestParams;
		}
		
		protected void init() throws IOException {
			// Check if we are in a debug environment
			this.debug = requestParams.options.getBoolean("debug");

			// Prepare the current environment
			this.envName = requestParams.options.getString("env");
			this.env = DataAccessBean.get().getEnvironment(envName);
			this.prov = new EnvParameterProvider(env);
			env.prepareEndpoints();
			
			// Push the dynamic parameters to the user session
			JsonObject p = (JsonObject)requestParams.options.get("params");
			if(p!=null) {
				for(Iterator<String> it= p.getJsonProperties(); it.hasNext(); ) {
					String name = it.next();
					String value = (String)p.getJsonProperty(name);
					env.pushSessionParams(name, value);
				}
			}
			
			// Compose the URLs
			this.serverUrl = composeServerUrl(req);
			this.dbUrl = composeDatabaseUrl(req,serverUrl);
			
			// Find the library to use
			int libIdx = Math.max(0, requestParams.options.getInt("lib"));
			this.jsLib = JavaScriptLibraries.LIBRARIES[libIdx]; 
			this.isDojo = jsLib.getLibType()==JavaScriptLibraries.LibType.DOJO;
			this.jsLibraryPath = jsLib.getLibUrl();
			if(libIdx==0) {
				this.jsLibraryPath = getDefautLibraryPath(serverUrl);
			}
			if(jsLibraryPath.endsWith("/")) {
				this.jsLibraryPath = jsLibraryPath.substring(0,jsLibraryPath.length()-1);
			}
		}
		
		protected void execRequest() throws ServletException, IOException {
			resp.setContentType("text/html");
			
	//		Map m = req.getParameterMap();
	//		for(Object k: m.keySet()) {
	//			Object v = m.get(k);
	//			System.out.println("Key:"+k+", Value:"+StringUtil.toString(v,32));
	//		}
			PrintWriter pw = resp.getWriter();
			
			pw.println("<!DOCTYPE html>");
			pw.println("<html lang=\"en\">");
			
			generateHead(pw);
			generateBody(pw);
			
			pw.println("</html>");
		    		
			pw.flush();
			pw.close();
		}
		
		public void generateHead(PrintWriter pw) throws IOException {
			pw.println("<head>");
			
			generateHeadTitle(pw);
			generateHeadTheme(pw);
			generateHeadJSLibrary(pw);
			generateHeadFirebug(pw);
	
			pw.println("</head>");
		}

		protected void generateHeadTitle(PrintWriter pw) throws IOException {
			pw.println("  <title>Social Business Playground</title>");
		}

		protected void generateHeadTheme(PrintWriter pw) throws IOException {
			String theme = requestParams.properties.getProperty("theme");
			boolean isDojo = jsLib.getLibType()==JavaScriptLibraries.LibType.DOJO;
			if(StringUtil.equals(theme, "bootstrap")) {
				pw.println("  <style type=\"text/css\">");
				pw.println("    @import \"/xsp/.ibmxspres/.sbtsdk/bootstrap/css/bootstrap.min.css\";");
				pw.println("  </style>");
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
		}

		protected void generateHeadJSLibrary(PrintWriter pw) throws IOException {
			int jsAggregator = OptionsBean.JS_AGG_NONE;
			switch(jsLib.getLibType()) {
				case DOJO: {
					// Only for the default library (bundled with Domino)
					if(jsLib==JavaScriptLibraries.LIBRARIES[0]) {
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
								String xpagesLayer = aggregatorAddModules(serverUrl,requestParams.js,true);
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
							emitDojoAggregatedConfig(pw, serverUrl);
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
			
			// Script for the dojo parser
			if(isDojo) {
				pw.println("  <script>");
				pw.println("    require(['dojo/parser']);"); // avoid dojo warning
				pw.println("  </script>");
			}
		}

		protected void generateHeadFirebug(PrintWriter pw) throws IOException {
			// Add the firebug lite debugging tools
			if(debug) {
				pw.println("  <script type=\"text/javascript\" src=\"/xsp/.ibmxspres/.extlib/firebug/js/firebug-lite.js\"></script>\n");
			}
		}
		
		protected void generateBody(PrintWriter pw) throws IOException {
			generateBodyTag(pw);
			
			generateCssTags(pw);
			generateHtmlTags(pw);
			generateErrorTags(pw);
			generateJsTags(pw);
	
			pw.println("</body>");
		}

		protected void generateBodyTag(PrintWriter pw) throws IOException {
			pw.print("<body");
			if(StringUtil.isNotEmpty(bodyTheme)) {
				pw.print(" class=\"");
				pw.print(bodyTheme);
				pw.print("\"");
			}
			pw.println(">");
		}

		protected void generateCssTags(PrintWriter pw) throws IOException {
	        String css = requestParams.css;
			css = ParameterProcessor.process(css, prov);
			if(StringUtil.isNotEmpty(css)) {
				String s = "  <style>"+css+"</style>";
				pw.println(s);
			}
		}
		
		protected void generateJsTags(PrintWriter pw) throws IOException {
			String js = requestParams.js;
			js = ParameterProcessor.process(js, prov);
			if(StringUtil.isNotEmpty(js)) {
				String s =   "<script>\n"
							+js
							+"</script>\n";
				pw.println(s);
			}		
		}

		protected void generateHtmlTags(PrintWriter pw) throws IOException {
			// Text content for simple output
			String html = "<div id=\"content\"></div>\n";
			
			// Custom HTML
			String customHtml = requestParams.html;
			html = html + ParameterProcessor.process(customHtml, prov);
			
			// Hidden progress indicator image 
			html = html + "\n<div id='loading' style='visibility: hidden'><img src='../progressIndicator.gif' alt='loading...'></img></div>";
			
			pw.println(html);
		}

		protected void generateErrorTags(PrintWriter pw) throws IOException {
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
		}
	
		protected void emitDojoAggregatedConfig(PrintWriter pw, String serverUrl) throws IOException {
		}
		
		protected String aggregatorAddModules(String serverUrl, String js, boolean includeLibraryModules) throws IOException {
			// XPages aggregator
	        ResourceFactory factory = ResourceFactory.get();
	        DojoLibrary dojoLibrary = DojoLibraryFactory.getDefaultLibrary(false);
	        String dojoLocale = "en-us";
	        DojoDependencyList dojoResources = new DojoDependencyList(factory,dojoLibrary,dojoLocale);
	        
	        aggregatorAddDojoModules(dojoResources);
	        if(includeLibraryModules) {
	        	aggregatorAddLibraryModules(dojoResources);
	        }
	        aggregatorAddJSDependencyModules(dojoResources, js);
	
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
			
		protected void aggregatorAddDojoModules(DojoDependencyList dojoResources) throws IOException {
	        ResourceFactory factory = dojoResources.getFactory();
	        DojoLibrary dojoLibrary = dojoResources.getDojoLibrary();
	        
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
		}
		
		protected void aggregatorAddLibraryModules(DojoDependencyList dojoResources) throws IOException {
		}
		
		protected void aggregatorAddJSDependencyModules(DojoDependencyList dojoResources, String js) throws IOException {
	        ResourceFactory factory = dojoResources.getFactory();
	        DojoLibrary dojoLibrary = dojoResources.getDojoLibrary();
	
	        //if(includeSDKModules) {
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
	}
}
