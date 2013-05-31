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
    
    private RuntimeFactory runtimeFactory;
    private Context context;
    private Application application;
    private BasicEndpoint endpoint;
    private ActivityStreamService _service;
    
    public AdminPublishActivityStream(){
        this(ActivityStreamService.DEFAULT_ENDPOINT_NAME, true);
    }
    
    public AdminPublishActivityStream(String endpointName, boolean initEnvironment){
        if(initEnvironment)
            this.initEnvironment();
        
        this._service = new ActivityStreamService();
        this.setEndpoint((BasicEndpoint)EndpointFactory.getEndpoint(endpointName));
    }
    
    public BasicEndpoint getEndpoint(){
        return this.endpoint;
    }
    
    public void setEndpoint(BasicEndpoint endpoint){
        this.endpoint = endpoint;
        this._service.setEndpoint(endpoint);
    }
    
    public void initEnvironment() {
        runtimeFactory = new RuntimeFactoryStandalone();
        application = runtimeFactory.initApplication(null);
        context = Context.init(application, null, null);
    }
    
    public void destroy(){
        if (context != null)
            Context.destroy(context);
        if (application != null)
            Application.destroy(application);
    }
    
    public JsonJavaObject postToStream(JsonJavaObject template) throws SBTServiceException {
        if(_service == null){
            _service = new ActivityStreamService();
            _service.setEndpoint(this.endpoint);
        }
        JsonJavaObject returnedData = null;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", APPLICATION_JSON);
        returnedData = _service.postEntry(template, header);
        
        return returnedData;
    }
    
    public JsonJavaObject postToStream(String template) throws JsonException, AuthenticationException, SBTServiceException{
        JsonJavaObject data = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, template);
        return postToStream(data);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length >= 2) {
            AdminPublishActivityStream paas = new AdminPublishActivityStream();
            JsonJavaObject template;
            try {
                paas.getEndpoint().login("admin", "***REMOVED***");
                template = paas.mergeData(args[0], args[1]);
                JsonJavaObject streamEntry = paas.postToStream(template);
                if(streamEntry != null)
                    System.out.println("Success, posted ActivityStream entry: " + streamEntry.toString());
            } catch (JsonException e) {
                e.printStackTrace();
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (SBTServiceException e) {
                e.printStackTrace();
            } finally{
                paas.destroy();
            }
        }
        else {
            System.out.println("Usage: PublishAnyActivityStream templateFilePath dataFilePath");
        }
    }
    
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
}
