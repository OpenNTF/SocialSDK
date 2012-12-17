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
package com.ibm.sbt.sample.app;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace
 * @date 10 Dec 2012
 */
public class SampleApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone();
		Application application = runtimeFactory.initApplication(null);
		Context context = Context.init(application, null, null);

		try {
			Endpoint endpoint = EndpointFactory.getEndpoint("connections");
			String profileUrl = "profiles/atom/profile.do";
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("email", "FrankAdams@renovations.com");
			Object result = endpoint.xhrGet(profileUrl, parameters, ClientService.FORMAT_XML);
			String xml = DOMUtil.getXMLString((Node)result);
			System.out.println(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Context.destroy(context);
		Application.destroy(application);
	}
}
