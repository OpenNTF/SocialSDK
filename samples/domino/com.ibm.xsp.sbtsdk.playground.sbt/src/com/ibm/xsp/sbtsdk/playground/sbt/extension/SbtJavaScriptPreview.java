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
package com.ibm.xsp.sbtsdk.playground.sbt.extension;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.minifier.DojoDependencyList;
import com.ibm.xsp.minifier.ResourceFactory;

import nsf.playground.extension.JavaScriptPreviewExtension;
import nsf.playground.playground.PreviewJavaScriptHandler.Renderer;
import nsf.playground.playground.PreviewJavaScriptHandler.RequestParams;



/**
 * JavaScript preview extension for the Playground.
 * 
 * @author priand
 */
public class SbtJavaScriptPreview extends JavaScriptPreviewExtension {
	
	public SbtJavaScriptPreview() {
	}

	@Override
	public Renderer findRenderer(HttpServletRequest req, HttpServletResponse resp, RequestParams requestParams, boolean createDefault) throws IOException {
		boolean create = createDefault;
		if(!create) {
			return null;
		}
		return new Renderer(req,resp,requestParams) {
			protected void emitDojoAggregatedConfig(PrintWriter pw, String serverUrl) throws IOException {
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
				String xpagesLayer = aggregatorAddModules(serverUrl,requestParams.js,false);
				pw.println("        	'"+xpagesLayer+"',");
				//pw.println("        	'"+sbtPath+"/js/sdk/_layers/sbt-core-dojo-amd.js',");
				pw.println("        	'"+sbtPath+"/js/sdk/_layers/sbt-extra-controls-dojo-amd.js'");
				pw.println("        ]");
			}
			
			protected void aggregatorAddLibraryModules(DojoDependencyList dojoResources) throws IOException {
		        ResourceFactory factory = dojoResources.getFactory();
		        DojoLibrary dojoLibrary = dojoResources.getDojoLibrary();
		
		//        if(includeSDKModules) {
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
			}
		};
	}
}
