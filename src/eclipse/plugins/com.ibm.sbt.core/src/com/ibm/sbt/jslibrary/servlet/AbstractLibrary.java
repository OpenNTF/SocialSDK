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
package com.ibm.sbt.jslibrary.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.util.io.json.JsonReference;
import com.ibm.sbt.jslibrary.SBTEnvironment.Property;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.endpoints.GadgetEndpoint;
import com.ibm.sbt.services.endpoints.GadgetOAuthEndpoint;
import com.ibm.sbt.services.endpoints.OAuth2Endpoint;
import com.ibm.sbt.services.endpoints.OAuthEndpoint;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.services.util.AuthUtil;
import com.ibm.sbt.util.SBTException;

/**
 * Handles initialising the Social Business Toolkit library with the appropriate bridge to the underlying JavaScript library i.e. dojo or jquery or ... This class need to be thread safe, only a single instance will be created.
 * 
 * @author mwallace
 * @author cmanias
 */
abstract public class AbstractLibrary {

	private final String			libraryName;
	private final String			minimumVersion;
	private final String			maximumVersion;

	public static boolean			minify							= false;

	public static final String		REFERER							= "Referer";						//$NON-NLS-1$

	public static final String		APPLICATION_JAVASCRIPT			= "application/javascript";		//$NON-NLS-1$
	public static final String		UTF8							= "utf-8";							//$NON-NLS-1$

	public static final String		PROP_API_VERSION				= "apiVersion";
	public static final String		PROP_TRANSPORT					= "transport";
	public static final String		PROP_INVALID					= "invalid";
	public static final String		PROP_AUTHENTICATOR				= "authenticator";
	public static final String		PROP_PROXY_PATH					= "proxyPath";
	public static final String		PROP_PROXY						= "proxy";
	public static final String		PROP_BASE_URL					= "baseUrl";
	public static final String		PROP_TOOLKIT_URL				= "sbtUrl";
	public static final String		PROP_SERVICE_URL				= "serviceUrl";
	public static final String		PROP_LIBRARY_URL				= "libraryUrl";
	public static final String		PROP_AUTH_TYPE					= "authType";
	public static final String		PROP_LOGIN_PAGE					= "loginPage";
	public static final String		PROP_LOGIN_DIALOG_PAGE			= "loginDialogPage";
	public static final String		PROP_LOGIN_UI					= "loginUi";
	public static final String		PROP_AUTO_AUTHENTICATE			= "autoAuthenticate";
	public static final String		IS_AUTHENTICATED				= "isAuthenticated";
	public static final String		PROP_AUTHENTICATION_ERROR_CODE	= "authenticationErrorCode";
	public static final String		PROP_ENDPOINT_ALIAS	            = "name";
	public static final String		PROP_PLATFORM    	            = "platform";
	public static final String      PROP_SERVICE_MAPPINGS           = "serviceMappings";

	public static final String		PROP_MODULE_PREFIX				= "_module";
	public static final String		PROP_MODULE_AUTHENTICATOR		= "_moduleAuthenticator";
	public static final String		PROP_MODULE_TRANSPORT			= "_moduleTransport";

	public static final String		REF_SBT_TRANSPORT				= "sbtTransport";
	public static final String		REF_ERR_TRANSPORT				= "errTransport";
	public static final String		REF_SBT_PROXY					= "sbtProxy";
	public static final String		REF_SBT_AUTHENTICATOR			= "sbtAuthenticator";

	public static final String		MODULE_SBT						= "sbt";
	public static final String		MODULE_SBTX						= "sbtx";
	public static final String		MODULE_DECLARE					= "sbt/_bridge/declare";
	public static final String		MODULE_IFRAMETRANSPORT			= "sbt/_bridge/IFrameTransport";
	public static final String		MODULE_TRANSPORT				= "sbt/_bridge/Transport";
	public static final String		MODULE_REQUESTTRANSPORT			= "sbt/_bridge/RequestTransport";
	public static final String		MODULE_MOCK_TRANSPORT			= "sbt/MockTransport";
	public static final String		MODULE_DEBUG_TRANSPORT			= "sbt/DebugTransport";
	public static final String		MODULE_ERROR_TRANSPORT			= "sbt/ErrorTransport";
	public static final String		MODULE_GADGET_TRANSPORT			= "sbt/GadgetTransport";
	public static final String		MODULE_CACHE					= "sbt/Cache";
	public static final String		MODULE_CONFIG					= "sbt/config";
	public static final String		MODULE_DOM						= "sbt/dom";
	public static final String		MODULE_ENDPOINT					= "sbt/Endpoint";
	public static final String		MODULE_GADGET					= "sbt/Gadget";
	public static final String		MODULE_IWIDGET					= "sbt/IWidget";
	public static final String		MODULE_JSONPATH					= "sbt/jsonpath";
	public static final String		MODULE_LANG						= "sbt/lang";
	public static final String		MODULE_PORTLET					= "sbt/Portlet";
	public static final String		MODULE_PROXY					= "sbt/Proxy";
	public static final String		MODULE_XML						= "sbt/xml";
	public static final String		MODULE_XPATH					= "sbt/xpath";
	public static final String		MODULE_XSL						= "sbt/xsl";
	public static final String		MODULE_BASIC					= "sbt/authenticator/Basic";
	public static final String		MODULE_OAUTH					= "sbt/authenticator/OAuth";

	public static final String		PATH_SBT						= "sbt";							//$NON-NLS-1$
	public static final String		PATH_SBTX						= "sbtx";							//$NON-NLS-1$

	private static final String		PADDING							= "    ";
	private static final String		NL								= "\n";
	private static 	String nl										= NL;
	private static final String[][]	REGISTER_EXT_MODULES			= { { MODULE_SBTX, PATH_SBTX } };

	static final String				sourceClass						= AbstractLibrary.class.getName();
	static final Logger				logger							= Logger.getLogger(sourceClass);

	/**
	 * This is used to determine how to generate the module URL
	 *
	 */
	protected enum ModuleType {
		SBT_MODULE, SBTX_MODULE, JS_LIBRARY
	};
	
	/**
	 * @param libraryName
	 * @param minimumVersion
	 * @param maximumVersion
	 * @throws ClassNotFoundException
	 */
	public AbstractLibrary(String libraryName, String minimumVersion, String maximumVersion) {
		this.libraryName = libraryName;
		this.minimumVersion = normalizeVersion(minimumVersion);
		this.maximumVersion = normalizeVersion(maximumVersion);
	}

	/**
	 * Return true if the associated <code>BaseLibrary</code> can handle this request.
	 * 
	 * @param request
	 * @return true if the associated <code>BaseLibrary</code> can handle this request
	 */
	public boolean isMatch(LibraryRequest request) {
		String jsLib = request.getJsLib();
		if (!libraryName.equals(jsLib)) {
			return false;
		}

		String jsVersion = request.getJsVersion();
		if (!StringUtil.isEmpty(jsVersion) && !isSupportedVersion(jsVersion)) {
			return false;
		}
		return true;
	}

	/**
	 * @param request
	 * @throws LibraryException
	 */
	protected void doGet(LibraryRequest request) throws LibraryException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "doGet", request);
		}

		try {
			// populate list of endpoint/properties
			Map<String, JsonObject> endpoints = populateEndpoints(request);
			JsonObject properties = populateProperties(request);
			minify = !request.isDebug();
			nl=minify?"":NL;
			// write response
			HttpServletResponse response = request.getHttpResponse();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), UTF8));
			try {
				// concrete library generates the script
				String script = generateJavaScript(request, endpoints, properties);

				// response is of type aplication/javascript
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType(APPLICATION_JAVASCRIPT);
				writer.write(script);
			} finally {
				writer.flush();
			}

		} catch (Exception e) {
			throw new LibraryException(e);
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "doGet");
		}
	}

	/**
	 * 
	 * @return
	 */
	protected String newLine(){
		return nl;
	}
	
	/**
	 * @param sb
	 */
	protected StringBuilder indent(StringBuilder sb) {
		return indent(sb, 1);
	}

	/**
	 * @param sb
	 * @param ammount
	 */
	protected StringBuilder indent(StringBuilder sb, int ammount) {
		for (int i = 0; !minify && i < ammount; i++) {
			sb.append(PADDING);
		}
		return sb;
	}

	/**
	 * @param request
	 * @throws ServletException
	 * @throws IOException
	 */
	protected Map<String, JsonObject> populateEndpoints(LibraryRequest request) throws LibraryException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "populateEndpoints", request);
		}

		Map<String, JsonObject> jsonEndpoints = new HashMap<String, JsonObject>();

		// Read the list of endpoint
		com.ibm.sbt.jslibrary.SBTEnvironment.Endpoint[] endpoints = request.getEnvironment().getEndpointsArray();
		if (endpoints != null) {
			for (com.ibm.sbt.jslibrary.SBTEnvironment.Endpoint endpoint : endpoints) {
				String endpointAlias = endpoint.getAlias();
				String endpointName = endpoint.getName();
				// is an alias in use?
				if (endpointAlias == null) {
					endpointAlias = endpointName;
				}

				Endpoint theEndpoint = EndpointFactory.getEndpointUnchecked(endpointName);
				JsonObject jsonEndpoint = createJsonForEndpoint(request, theEndpoint, endpointAlias, endpointName);
				jsonEndpoints.put(endpointAlias, jsonEndpoint);
			}
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "populateEndpoints", endpoints);
		}
		return jsonEndpoints;
	}

	/**
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	protected JsonObject populateProperties(LibraryRequest request) throws ServletException, IOException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "populateProperties", request);
		}

		JsonObject jsonProperties = new JsonJavaObject();
		// add the built-in properties
		jsonProperties.putJsonProperty(PROP_TOOLKIT_URL, request.getToolkitJsUrl());
		jsonProperties.putJsonProperty(PROP_SERVICE_URL, request.getServiceUrl());
		jsonProperties.putJsonProperty(PROP_LIBRARY_URL, request.getLibraryUrl());

		// add the requested properties
		Property[] properties = request.getEnvironment().getPropertiesArray();
		if (properties != null) {
		    Context context = Context.get();
			for (Property property : properties) {
				String value = property.getValue();
				if (StringUtil.isEmpty(value)) {
					value = context.getProperty(property.getName());
				}
				if (value != null) {
					jsonProperties.putJsonProperty(property.getName(), value);
				}
			}
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "populateProperties", properties);
		}
		return jsonProperties;
	}

	/**
	 * @param endpoint
	 * @param sameDomain
	 *            TODO Need a unit test for this method
	 */
	protected JsonObject createJsonForEndpoint(LibraryRequest request, Endpoint endpoint,
			String endpointAlias, String endpointName) throws LibraryException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createJsonForEndpoint", new Object[] { request, endpoint,
					endpointAlias, endpointName });
		}

		JsonObject jsonEndpoint = new JsonJavaObject();

		// use the base url from the endpoint
		if (isValid(request, endpoint)) {
			// set the endpoint api version
			String apiVersion = endpoint.getApiVersion();
			if (StringUtil.isNotEmpty(apiVersion)) {
				jsonEndpoint.putJsonProperty(PROP_API_VERSION, apiVersion);
			}
			jsonEndpoint.putJsonProperty(PROP_ENDPOINT_ALIAS, endpointAlias);
			// set the endpoint url
			jsonEndpoint.putJsonProperty(PROP_BASE_URL, endpoint.getUrl());
			try {
				jsonEndpoint.putJsonProperty(IS_AUTHENTICATED, endpoint.isAuthenticated());
			} catch (ClientServicesException e) {
				jsonEndpoint.putJsonProperty(IS_AUTHENTICATED, false);
			}
			
			jsonEndpoint.putJsonProperty(PROP_SERVICE_MAPPINGS, endpoint.getServiceMappings());

			// configure endpoint to use proxy
			if (endpoint.isUseProxy()) {
				JsonReference proxyRef = createProxyRef(request, endpoint, endpointName);
				if (proxyRef != null) {
					jsonEndpoint.putJsonProperty(PROP_PROXY, proxyRef);
				}
				String proxyPath = endpoint.getProxyPath(endpointName);
				if (!StringUtil.isEmpty(proxyPath)) {
					jsonEndpoint.putJsonProperty(PROP_PROXY_PATH, proxyPath);
				}
			}

			// configure endpoint to use authenticator
			JsonReference authenticatorRef = createAuthenticatorRef(request, endpoint, endpointName);
			if (authenticatorRef != null) {
				jsonEndpoint.putJsonProperty(PROP_AUTHENTICATOR, authenticatorRef);
				String moduleName = endpoint.getAuthenticator(endpointName, request.getToolkitJsUrl()).getModuleName();
				jsonEndpoint.putJsonProperty(PROP_MODULE_AUTHENTICATOR, moduleName);
			}

			// configure the transport
			JsonReference transportRef = createTransportRef(request, endpoint, endpointName);
			if (transportRef != null) {
				jsonEndpoint.putJsonProperty(PROP_TRANSPORT, transportRef);
				String moduleName = getTransport(request, endpoint, endpointName).getModuleName();
				jsonEndpoint.putJsonProperty(PROP_MODULE_TRANSPORT, moduleName);
			} 

			// configure the authentication
			jsonEndpoint.putJsonProperty(PROP_AUTH_TYPE, AuthUtil.INSTANCE.getAuthValue(endpoint));
			if (endpoint.getLoginPage() != null) {
				jsonEndpoint.putJsonProperty(PROP_LOGIN_PAGE, endpoint.getLoginPage());
			}
			if (endpoint.getDialogLoginPage() != null) {
				jsonEndpoint.putJsonProperty(PROP_LOGIN_DIALOG_PAGE, endpoint.getDialogLoginPage());
			}
			if (endpoint.getLoginUi() != null) {
				jsonEndpoint.putJsonProperty(PROP_LOGIN_UI, endpoint.getLoginUi());
			}
			if (endpoint.getAutoAuthenticate() != null) {
				jsonEndpoint.putJsonProperty(PROP_AUTO_AUTHENTICATE, endpoint.getAutoAuthenticate());
			}
			if (endpoint.getPlatform() != null) {
				jsonEndpoint.putJsonProperty(PROP_PLATFORM, endpoint.getPlatform());
			}
			jsonEndpoint.putJsonProperty(PROP_AUTHENTICATION_ERROR_CODE,
					endpoint.getAuthenticationErrorCode());
			
			// configure client properties
			Map<String, Object> params = endpoint.getClientParams();
			Iterator<String> keys = params.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				Object value = params.get(key);
				jsonEndpoint.putJsonProperty(key, value);
			}

		} else {
			// set the endpoint url
			jsonEndpoint.putJsonProperty(PROP_INVALID, "true");

			// configure a error transport
			String message = getInvalidEndpointMessage(request, endpoint, endpointName);
			jsonEndpoint.putJsonProperty(PROP_TRANSPORT, new JsonReference("new ErrorTransport('"
					+ endpointName + "','" + message + "')"));
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "createJsonForEndpoint", jsonEndpoint);
		}
		return jsonEndpoint;
	}

	protected StringBuilder generateModuleBlock(LibraryRequest request, String[][] registerModules,
			String[][] registerExtModules, String[] requireModules, int indentationLevel) {

		StringBuilder sb = new StringBuilder();

		// register the module paths and required modules
		generateRegisterModules(sb, indentationLevel, request, registerModules, ModuleType.SBT_MODULE);
		if (registerExtModules != null) {
			generateRegisterModules(sb, indentationLevel, request, registerExtModules, ModuleType.SBTX_MODULE);
		}
		generateRequireModules(sb, indentationLevel, requireModules);
		return sb;
	}

	/**
	 * @param request
	 * @param endpoints
	 * @param properties
	 * @param indentationLevel
	 * @return
	 * @throws LibraryException
	 */
	protected StringBuilder generateSbtConfigDefine(LibraryRequest request,
			Map<String, JsonObject> endpoints, JsonObject properties, int indentationLevel)
			throws LibraryException {

		StringBuilder sb = new StringBuilder();

		// open the define invocation
		String[] dependModules = getDependModules(request, endpoints);
		String[] moduleNames = getModuleNames(dependModules);
		String dmList = toQuotedString(dependModules, ",");
		String dmNames = toString(moduleNames, ",");
		indent(sb, indentationLevel).append("define('").append(getDefineModule()).append("',[")
				.append(dmList).append("],function(").append(dmNames).append("){").append(newLine());

		indentationLevel++;

		// Create the sbt object
		indent(sb, indentationLevel).append("var sbt = {};").append(newLine());

		// define the properties
		Iterator<String> jsonProps = properties.getJsonProperties();
		if (jsonProps.hasNext()) {
			indent(sb, indentationLevel).append("sbt.Properties=").append(toJson(properties)).append(";").append(newLine());
		}

		// define the endpoints
		if (!endpoints.isEmpty()) {
			indent(sb, indentationLevel).append("sbt.Endpoints={").append(newLine());
			Iterator<Map.Entry<String, JsonObject>> entrySet = endpoints.entrySet().iterator();
			indentationLevel++;
			while (entrySet.hasNext()) {
				Map.Entry<String, JsonObject> entry = entrySet.next();
				String name = entry.getKey();
				JsonObject endpoint = entry.getValue();
				JsonObject endpointParams = removeModuleProperties(endpoint);
				indent(sb, indentationLevel).append("'").append(toJavaScriptString(name))
						.append("':new Endpoint(").append(toJson(endpointParams)).append(")");
				if (entrySet.hasNext()) {
					sb.append(",").append(newLine());
				} else {
					sb.append(newLine());
					indentationLevel--;
					indent(sb, indentationLevel).append("};").append(newLine()); // Close sbt.Endpoints={
				}
			}
		}
		
		// define the sbt.findEndpoint(endpointName) function
		indent(sb, indentationLevel).append("sbt.findEndpoint=function(endpointName) {\n");
		indentationLevel++;
		indent(sb, indentationLevel).append("return this.Endpoints[endpointName];\n");
		indentationLevel--;
		indent(sb, indentationLevel).append("};\n");

		// close define invocation
		indent(sb, indentationLevel).append("return sbt;").append(newLine());
		indentationLevel--;
		indent(sb, indentationLevel).append("});").append(newLine());

		return sb;
	}

	/**
	 * Generate the JavaScript to be returned for the specified <code>LibraryRequest</code>
	 * 
	 * @param request
	 *            LibraryRequest for which the script is being created
	 * @param endpoints
	 *            Map of JSonObjects representing the endpoints to be created in the JavaScript
	 * @param properties
	 *            JSonObject representing the properties to be included in the script
	 * @throws LibraryException
	 *             if an error occurs while generating the JavaScript
	 */
	protected String generateJavaScript(LibraryRequest request, Map<String, JsonObject> endpoints,
			JsonObject properties) throws LibraryException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "generateJavaScript",
					new Object[] { request, endpoints, properties });
		}

		StringBuilder sb = new StringBuilder();
		int indentationLevel = 1;
		// make sure the library is only defined once (open if)
		sb.append("if(typeof _sbt=='undefined' || window._sbt_bridge_compat){").append(newLine());
		indent(sb).append("_sbt=0;").append(newLine());

		boolean closeElse = false;
		boolean isInnerBlock = false;

		if (enableDefineCheck(request.getJsVersion())) {
			indent(sb).append("if(typeof define=='undefined'){").append(newLine());

			String[][] registerModules = getRegisterModules();
			String[][] registerExtModules = StringUtil.isNotEmpty(request.getToolkitExtUrl()) ? getRegisterExtModules(request)
					: null;
			String[] requireModules = getRequireModules();

			indentationLevel++;
			sb.append(generateModuleBlock(request, registerModules, registerExtModules, requireModules,
					indentationLevel));
			indentationLevel--;

			indent(sb).append("} else {").append(newLine());
			closeElse = true;
			isInnerBlock = true;
		}
		// register the module paths and required modules
		String[][] registerModulesAmd = getRegisterModulesAmd();
		String[][] registerExtModulesAmd = StringUtil.isNotEmpty(request.getToolkitExtUrl()) ? getRegisterExtModulesAmd(request)
				: null;
		String[] requireModulesAmd = getRequireModulesAmd();
		if (isInnerBlock) {
			indentationLevel++;
		}
		sb.append(generateModuleBlock(request, registerModulesAmd, registerExtModulesAmd, requireModulesAmd,
				indentationLevel));
		if (isInnerBlock) {
			indentationLevel--;
		}
		if (closeElse) {
			indent(sb).append("}").append(newLine());
		}
		sb.append(generateSbtConfigDefine(request, endpoints, properties, indentationLevel));
		sb.append("}").append(newLine());

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "generateJavaScript", sb.toString());
		}
		return sb.toString();
	}

	/**
	 * @param sb
	 * @param requireModules
	 */
	protected void generateRequireModules(StringBuilder sb, int indentationLevel, String[] requireModules) {
		for (String requireModule : requireModules) {
			indent(sb, indentationLevel).append(generateRequire(requireModule));
		}
	}

	/**
	 * @param sb
	 * @param request
	 * @param registerModules
	 */
	protected void generateRegisterModules(StringBuilder sb, int indentationLevel, LibraryRequest request,
			String[][] registerModules, ModuleType type) {
		if (registerModules == null) {
			return;
		}
		for (String[] registerModule : registerModules) {
			String moduleUrl = getModuleUrl(request, registerModule[1], type);
			indent(sb, indentationLevel).append(
					generateRegisterModulePath(request, registerModule[0], moduleUrl));
		}
	}

	/**
	 * @param request
	 * @param endpoints
	 * @return
	 */
	protected String[] getDependModules(LibraryRequest request, Map<String, JsonObject> endpoints) {
		List<String> modules = new ArrayList<String>();

		Collection<JsonObject> jsonObjects = endpoints.values();
		for (JsonObject jsonObject : jsonObjects) {
			if (jsonObject.getJsonProperty(PROP_INVALID) != null) {
				// add the error transport
				if (!modules.contains(MODULE_ERROR_TRANSPORT)) {
					modules.add(MODULE_ERROR_TRANSPORT);
				}
			} else {
				// is proxy being used for this endpoint
				if (jsonObject.getJsonProperty(PROP_PROXY) != null) {
					// add proxy module if not already in the list
					if (!modules.contains(MODULE_PROXY)) {
						modules.add(MODULE_PROXY);
					}
				}

				// which transport is being used for this endpoint
				if (jsonObject.getJsonProperty(PROP_TRANSPORT) != null) {
					String transportModule = (String) jsonObject.getJsonProperty(PROP_MODULE_TRANSPORT);
					// add the transport module to the list
					if (!modules.contains(transportModule)) {
						modules.add(transportModule);
					}
				}

				// is an authenticator being used for this endpoint
				if (jsonObject.getJsonProperty(PROP_AUTHENTICATOR) != null) {
					String authenticatorModule = (String) jsonObject
							.getJsonProperty(PROP_MODULE_AUTHENTICATOR);
					// add the authenticator module to the list
					if (!modules.contains(authenticatorModule)) {
						modules.add(authenticatorModule);
					}
				}
			}

			// add endpoint module is not already in the list
			if (!modules.contains(MODULE_ENDPOINT)) {
				modules.add(MODULE_ENDPOINT);
			}
		}

		return modules.toArray(new String[modules.size()]);
	}

	protected String getModuleUrl(LibraryRequest request, String modulePath, ModuleType type) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getModuleUrl", new Object[] { request, modulePath });
		}
		String jsUrl = "";
		String moduleUrl = "";
		switch (type) {
			case SBT_MODULE:
				jsUrl = request.getToolkitJsUrl();
				break;
			case SBTX_MODULE:
				jsUrl = request.getToolkitExtJsUrl();
				break;
			case JS_LIBRARY:
				jsUrl = request.getJsLibraryUrl();
				break;
			default:
				moduleUrl = modulePath;
				break;
		}
		moduleUrl = moduleUrl.isEmpty() ? PathUtil.concat(jsUrl, modulePath, '/') : moduleUrl;
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getModuleUrl", moduleUrl);
		}
		return moduleUrl;
	}
	/**
	 * @param properties
	 * @return
	 * @throws LibraryException
	 */
	protected String toJson(JsonObject properties) throws LibraryException {
		try {
			return JsonGenerator.toJson(JsonJavaFactory.instanceEx, properties, true);
		} catch (Exception e) {
			throw new LibraryException(e);
		}
	}

	/**
	 * @param dependModules
	 * @return
	 */
	protected String[] getModuleNames(String[] modules) {
		List<String> names = new ArrayList<String>();
		for (String module : modules) {
			names.add(getModuleParamName(module));
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * @param module
	 * @return
	 */
	protected String getModuleParamName(String module) {
		int index = module.lastIndexOf('/');
		if (index != -1) {
			module = module.substring(index + 1);
		}
		return module;
	}

	/**
	 * @param array
	 * @param delim
	 * @return
	 */
	protected String toQuotedString(String[] array, String delim) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (String str : array) {
			if (count > 0) {
				sb.append(delim);
			}
			sb.append("'").append(str).append("'");
			count++;
		}
		return sb.toString();
	}

	/**
	 * @param array
	 * @param delim
	 * @return
	 */
	protected String toString(String[] array, String delim) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (String str : array) {
			if (count > 0) {
				sb.append(delim);
			}
			sb.append(str);
			count++;
		}
		return sb.toString();
	}

	/**
	 * Convert the endpoint json to a new object which can be used to create endpoint parameters
	 * 
	 * @param jsonObject
	 * @return
	 */
	protected JsonObject removeModuleProperties(JsonObject jsonObject) {
		JsonObject params = new JsonJavaObject();
		Iterator<String> properties = jsonObject.getJsonProperties();
		while (properties.hasNext()) {
			// add regular properties
			String property = properties.next();
			if (!property.startsWith(PROP_MODULE_PREFIX)) {
				params.putJsonProperty(property, jsonObject.getJsonProperty(property));
			}
		}
		return params;
	}

	/**
	 * @param endpoint
	 * @param logicalName
	 * @return
	 * @throws LibraryException
	 */
	protected JsonReference createAuthenticatorRef(LibraryRequest request, Endpoint endpoint,
			String logicalName) throws LibraryException {
		JSReference authenticator = endpoint.getAuthenticator(logicalName, request.getToolkitJsUrl());
		if (authenticator != null) {
			try {
				String paramValues = JsonGenerator.toJson(JsonJavaFactory.instanceEx,
						authenticator.getProperties());
				String paramName = getModuleParamName(authenticator.getModuleName());
				return new JsonReference("new " + paramName + "(" + paramValues + ")");
			} catch (Exception e) {
				throw new LibraryException(e);
			}
		}
		return null;
	}

	/**
	 * @param request
	 * @param endpoint
	 * @param logicalName
	 * @return
	 * @throws LibraryException
	 */
	protected JsonReference createTransportRef(LibraryRequest request, Endpoint endpoint, String logicalName)
			throws LibraryException {
		JSReference transport = getTransport(request, endpoint, logicalName);
		if (transport != null) {
			try {
				String paramValues = JsonGenerator.toJson(JsonJavaFactory.instanceEx,
						transport.getProperties());
				String paramName = getModuleParamName(transport.getModuleName());
				return new JsonReference("new " + paramName + "(" + paramValues + ")");
			} catch (Exception e) {
				throw new LibraryException(e);
			}
		}
		return null;
	}

	/*
	 * Return the JSReference for the Transport module to use
	 */
	protected JSReference getTransport(LibraryRequest request, Endpoint endpoint, String endpointName) {
		return endpoint.getTransport(endpointName, getDefaultTransport(request));
	}

	/*
	 * Return the JSReference for the Transport module to use
	 */
	protected String getDefaultTransport(LibraryRequest request) {
		if (request.isDebugTransport()) {
			return MODULE_DEBUG_TRANSPORT;
		} else if (request.isMockTransport()) {
			return MODULE_MOCK_TRANSPORT;
		}
		return MODULE_TRANSPORT;
	}

	/*
	 * Return true if the endpoint is valid for the specified request
	 */
	protected boolean isValid(LibraryRequest request, Endpoint endpoint) {
		if (endpoint == null) {
			return false;
		}
		if (!endpoint.isAllowClientAccess()) {
			return false;
		}
		try {
			endpoint.checkValid();
			return true;
		} catch(SBTException ex) {
			return false;
		}
	}

	/*
	 * 
	 */
	protected boolean useGadgetTransport(Endpoint endpoint) {
		if (endpoint instanceof GadgetOAuthEndpoint) {
			return true;
		}
		return false;
	}

	/*
	 * Return error message to be associated with an invalid endpoint
	 */
	protected String getInvalidEndpointMessage(LibraryRequest request, Endpoint endpoint, String logicalName) {
		if(endpoint!=null) {
			if (useGadgetTransport(endpoint)) {
				String serviceName = getServiceName(endpoint);
				if (StringUtil.isEmpty(serviceName)) {
					return "OAuth service name not available for: " + logicalName;
				}
			}
			if (!endpoint.isAllowClientAccess()) {
				return "Client access disallowed for: " + logicalName;
			}
			try {
				endpoint.checkValid();
			} catch(SBTException ex) {
				return ex.getMessage();
			}
		}
		return "Required endpoint is not available: " + logicalName;
	}

	/*
	 * @param endpoint
	 * @return
	 */
	protected String getServiceName(Endpoint endpoint) {
		if (endpoint instanceof OAuthEndpoint) {
			return ((OAuthEndpoint) endpoint).getServiceName();
		} else if (endpoint instanceof OAuth2Endpoint) {
			return ((OAuth2Endpoint) endpoint).getServiceName();
		}
		return null;
	}

	/**
	 * @param request
	 * @param endpoint
	 * @return
	 * @throws LibraryException
	 */
	protected JsonReference createProxyRef(LibraryRequest request, Endpoint endpoint, String logicalName)
			throws LibraryException {
		// define the proxy URL
		if (endpoint.isUseProxy()) {
			String proxyUrl = PathUtil.concat(request.getServiceUrl(), endpoint.getProxyHandlerPath(), '/');
			return new JsonReference("new Proxy({proxyUrl:'" + proxyUrl + "'})");
		}
		return null;
	}

	/**
	 * @param endpoints
	 * @return
	 */
	protected boolean invalidEndpoints(Map<String, JsonObject> endpoints) {
		Collection<JsonObject> jsonObjects = endpoints.values();
		for (JsonObject jsonObject : jsonObjects) {
			String baseUrl = (String) jsonObject.getJsonProperty(PROP_BASE_URL);
			if (StringUtil.isEmpty(baseUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param endpoints
	 * @return
	 */
	protected boolean validEndpoints(Map<String, JsonObject> endpoints) {
		Collection<JsonObject> jsonObjects = endpoints.values();
		for (JsonObject jsonObject : jsonObjects) {
			String baseUrl = (String) jsonObject.getJsonProperty(PROP_BASE_URL);
			if (!StringUtil.isEmpty(baseUrl)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Return true if the version is supported by this library
	 */
	protected boolean isSupportedVersion(String version) {
		version = normalizeVersion(version);

		if (StringUtil.isEmpty(version)) {
			return true;
		}

		if (!StringUtil.isEmpty(minimumVersion)) {
			if (version.compareTo(minimumVersion) < 0) {
				return false;
			}
		}

		if (!StringUtil.isEmpty(maximumVersion)) {
			if (version.compareTo(maximumVersion) > 0) {
				return false;
			}
		}

		return true;
	}

	/*
	 * Return true if the version is supported by this library
	 */
	protected boolean isExceedsVersion(String version, String normalizedMinimumVersion) {
		version = normalizeVersion(version);

		if (StringUtil.isEmpty(version)) {
			return false;
		}

		if (!StringUtil.isEmpty(normalizedMinimumVersion)) {
			if (version.compareTo(normalizedMinimumVersion) >= 0) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Normalize the version number to simplify matching
	 */
	protected String normalizeVersion(String version) {
		if (StringUtil.isEmpty(version)) {
			return "";
		}
		String[] split = StringUtil.splitString(version, '.');
		StringBuilder sb = new StringBuilder();
		for (String part : split) {
			sb.append(String.format("%4s", part));
		}
		return sb.toString();
	}

	/**
	 * @return
	 */
	protected String[][] getRegisterExtModules(LibraryRequest request) {
		return REGISTER_EXT_MODULES;
	}

	/**
	 * @return
	 */
	protected String[][] getRegisterExtModulesAmd(LibraryRequest request) {
		return REGISTER_EXT_MODULES;
	}

	//
	// Abstract stuff
	//

	/**
	 * 
	 */
	abstract public boolean enableDefineCheck(String version);

	/**
	 * Return the library name
	 * 
	 * @return the library name
	 */
	abstract public String getName();

	/**
	 * @return
	 */
	abstract protected String[][] getRegisterModules();

	/**
	 * @return
	 */
	abstract protected String[][] getRegisterModulesAmd();

	/**
	 * @return
	 */
	abstract protected String getDefineModule();

	/**
	 * @return
	 */
	abstract protected String[] getRequireModules();

	/**
	 * @return
	 */
	abstract protected String[] getRequireModulesAmd();

	/**
	 * @param request
	 *            TODO
	 * @return
	 */
	abstract protected String generateRegisterModulePath(LibraryRequest request, String moduleName,
			String moduleUrl);

	/**
	 * @return
	 */
	abstract protected String generateRequire(String module);

	//
	// Internals
	//

	//
	// JavaScript generation utilities
	//

	/** Minimum printable ASCII character */
	private static final int	ASCII_MIN	= 32;
	/** Maximum printable ASCII character */
	private static final int	ASCII_MAX	= 126;

	/**
	 * Utility for changing a string so that its compatbile in javascript and does not prevent backslashes
	 * 
	 * @param StringBuilder
	 * @param string
	 *            converted to comply with javascript
	 */
	public static void appendJavaScriptString(StringBuilder b, String s) {
		appendJavaScriptString(b, s, false);
	}

	/**
	 * Utility for changing a string so that its compatbile in javascript
	 * 
	 * @param buildingString
	 *            String being constructed
	 * @param string
	 *            converted to comply with javascript
	 * @param preventBackslash
	 */
	public static void appendJavaScriptString(StringBuilder b, String s, boolean preventBackslash) {
		int length = s.length();
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\b':
					b.append("\\b");break; //$NON-NLS-1$
				case '\t':
					b.append("\\t");break; //$NON-NLS-1$
				case '\n':
					b.append("\\n");break; //$NON-NLS-1$
				case '\f':
					b.append("\\f");break; //$NON-NLS-1$
				case '\r':
					b.append("\\r");break; //$NON-NLS-1$
				case '\'':
					b.append("\\'");break; //$NON-NLS-1$
				case '\"':
					b.append("\\\"");break; //$NON-NLS-1$
				case '\\':
					if (!preventBackslash) {
						b.append("\\\\");}break; //$NON-NLS-1$
				default: {
					if ((c < ASCII_MIN) || (c > ASCII_MAX)) {
						b.append("\\u"); //$NON-NLS-1$
						b.append(StringUtil.toUnsignedHex(c, 4));
					} else {
						b.append(c);
					}
				}
			}
		}
	}

	/**
	 * Utility for changing a string so that its compatbile in javascript
	 * 
	 * @param String
	 *            to convert
	 * @return string converted to comply with javascript
	 */
	public static String toJavaScriptString(String s) {
		StringBuilder b = new StringBuilder(s.length() + 64);
		appendJavaScriptString(b, s);
		return b.toString();
	}

}
