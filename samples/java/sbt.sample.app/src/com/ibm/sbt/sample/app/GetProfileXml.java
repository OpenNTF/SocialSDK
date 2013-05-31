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
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace, Francis
 * @date 10 Dec 2012
 */
public class GetProfileXml {

    private RuntimeFactory runtimeFactory;
    private Context context;
    private Application application;
    private BasicEndpoint endpoint;
    
    /**
     * Default constructor. Initialises the Context and a connections endpoint.
     * 
     * Be sure to call the destroy() method in this class if you don't intend to keep the initialised Context around.
     */
    public GetProfileXml() {
        this("connections", true);
    }
    
    /**
     * 
     * @param endpointName The name of the endpoint to use.
     * @param initEnvironment - True if you want a Context initialised, false if there is one already. destroy() should be called when finished using this class if a context is initialised here. 
     */
    public GetProfileXml(String endpointName, boolean initEnvironment){
        if(initEnvironment)
            this.initEnvironment();
        this.setEndpoint((BasicEndpoint)EndpointFactory.getEndpoint(endpointName));
    }
    
    /**
     * 
     * @return The endpoint used in this class.
     */
    public BasicEndpoint getEndpoint(){
        return this.endpoint;
    }
    
    /**
     * 
     * @param endpoint The endpoint you want this class to use.
     */
    public void setEndpoint(BasicEndpoint endpoint){
        this.endpoint = endpoint;
    }
    
    /**
     * Initialise the Context, needed for Services and Endpoints.
     */
    public void initEnvironment(){
        runtimeFactory = new RuntimeFactoryStandalone();
        application = runtimeFactory.initApplication(null);
        context = Context.init(application, null, null);
    }
    
    /**
     * Destroy the Context.
     */
    public void destroy(){
        if (context != null)
            Context.destroy(context);
        if (application != null)
            Application.destroy(application);
    }
    
    /**
     * Get Profile Xml sample. Retrieves the xml definition of the user with the specified email address.
     * @param email The connections email of the profile to retrieve.
     * @return Xml representation of the user's profile.
     * @throws ClientServicesException
     * @throws XMLException
     */
    public String getProfileXml(String email) throws ClientServicesException, XMLException{
        String profileUrl = "profiles/atom/profile.do";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", email);
        Object result = endpoint.xhrGet(profileUrl, parameters, ClientService.FORMAT_XML);
        String xml = DOMUtil.getXMLString((Node)result);
        return xml;
    }
    
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	    GetProfileXml app = new GetProfileXml();
	    try {
            String xml = app.getProfileXml("FrankAdams@renovations.com");
            System.out.println(xml);
        } catch (ClientServicesException e) {
            e.printStackTrace();
        } catch (XMLException e) {
            e.printStackTrace();
        } finally {
            app.destroy();
        }
		

		
	}
}
