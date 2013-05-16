/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.sample.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.runtime.properties.FileResourcePropertiesFactory;
import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.activitystreams.ActivityStreamService;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * This class demonstrates how to publish to any user's ActivityStream
 * from a standalone class
 * 
 * @author Carlos Manias
 * @date 13 May 2013
 */
public class AdminPublishActivityStream {
	
	private static final String  APPLICATION_JSON  = "application/json";
	
	/*
	 * Reads template file
	 */
	private String readFile(String filePath) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			String sCurrentLine;
			
			URL path = getClass().getResource(filePath);
			if(path!=null) {
				File f;
				f = new File(path.toURI());
				br = new BufferedReader(new FileReader(f));
				while ((sCurrentLine = br.readLine()) != null) {
					sb.append(sCurrentLine);
				}
			} else {
				throw new FileNotFoundException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/*
	 * Merges template with data
	 */
	private JsonJavaObject mergeData(String templatePath, String propertiesPath) throws JsonException{
		String template = readFile(templatePath);
		FileResourcePropertiesFactory frpf = new FileResourcePropertiesFactory();
		Properties props = frpf.readFactoriesFromFile(propertiesPath);
		template = ParameterProcessor.process(template, props);
		
		JsonJavaObject templateObj = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, template);
		return templateObj;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length >=2) {
			AdminPublishActivityStream paas = new AdminPublishActivityStream();
			RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone();
			Application application = runtimeFactory.initApplication(null);
			Context context = Context.init(application, null, null);
			BasicEndpoint endpoint = (BasicEndpoint)EndpointFactory.getEndpoint(ActivityStreamService.DEFAULT_ENDPOINT_NAME);
			try {
				JsonJavaObject data = paas.mergeData(args[0], args[1]);
				endpoint.login("admin", "passw0rd", true);
				ActivityStreamService _service = new ActivityStreamService();
		        Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", APPLICATION_JSON);
				_service.postEntry(data, header);
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SBTServiceException e) {
				e.printStackTrace();
			} catch (JsonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Context.destroy(context);
			Application.destroy(application);
		} else {
			System.out.println("Usage: PublishAnyActivityStream templateFilePath dataFilePath");
		}
	}
}
