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
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.runtime.properties.FileResourcePropertiesFactory;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
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
public class PublishAnyActivityStream {
	
	private static final String  APPLICATION_JSON  = "application/json";
	
	/*
	 * Reads template file
	 */
	private String readFile(String filePath) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filePath));
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
		} catch (IOException e) {
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
		FileResourcePropertiesFactory frpf = new FileResourcePropertiesFactory(propertiesPath);
		JsonJavaObject templateObj = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, template);
		for (Iterator<String> it = templateObj.getJsonProperties(); it.hasNext(); ) {
			String key=it.next();
			String value = frpf.getProperty(key);
			if (null!=value) {
				templateObj.putJsonProperty(key, value);
			}
		}
		return templateObj;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length >=3) {
			PublishAnyActivityStream paas = new PublishAnyActivityStream();
			RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone();
			Application application = runtimeFactory.initApplication(null);
			Context context = Context.init(application, null, null);
			BasicEndpoint endpoint = (BasicEndpoint)EndpointFactory.getEndpoint(ActivityStreamService.DEFAULT_ENDPOINT_NAME);
			try {
				JsonJavaObject data = paas.mergeData(args[1], args[2]);
				endpoint.login("admin", "passw0rd", true);
				ActivityStreamService _service = new ActivityStreamService();
		        Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", APPLICATION_JSON);
				_service.postEntry(data, header);
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SBTServiceException e) {
				// TODO Auto-generated catch block
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
