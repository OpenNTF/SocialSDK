/*
 * © Copyright IBM Corp. 2014
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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;
import com.ibm.sbt.services.client.connections.wikis.Wiki;
import com.ibm.sbt.services.client.connections.wikis.WikiPage;
import com.ibm.sbt.services.client.connections.wikis.WikiService;
import com.ibm.sbt.services.client.connections.wikis.WikiXPath;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author mwallace
 * @since 14 January 2014
 */
public class ExportWiki {

    private BasicEndpoint endpoint;
    private WikiService wikiService;

    /**
     * 
     * @param url
     * @param user
     * @param password
     * @throws AuthenticationException
     */
    public ExportWiki(String url, String user, String password)
            throws AuthenticationException {
        BasicEndpoint basicEndpoint = new BasicEndpoint();
        basicEndpoint.setUrl(url);
        basicEndpoint.setForceTrustSSLCertificate(true);
        basicEndpoint.setUser(user);
        basicEndpoint.setPassword(password);

        this.wikiService = new WikiService();
        this.setEndpoint(basicEndpoint);
    }
    
    /**
     * 
     * @return {BasicEndpoint}
     */
    public BasicEndpoint getEndpoint(){
        return endpoint;
    }

    /**
     * 
     * @param endpoint
     *            The endpoint you want this class to use.
     * @throws AuthenticationException
     */
    public void setEndpoint(BasicEndpoint endpoint)
            throws AuthenticationException {
        this.endpoint = endpoint;
        this.wikiService.setEndpoint(this.endpoint);
    }

    /*
     * Appends all of the entity's fields to the given JsonJavaObject.
     * 
     * @param entity - The entity we will extract fields from.
     * 
     * @param xpathValues - The array of field values, gotten from any of the
     * Enums using <Xpath implements FieldEntry>.values();
     * 
     * @param jsonObject - The json object to append fields to. If null, it will
     * be instantiated.
     * 
     * @return {JsonJavaObject}
     */
    private JsonJavaObject entityToJsonObject(BaseEntity entity,
            FieldEntry[] xpathValues, JsonJavaObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JsonJavaObject();
        }
        for (FieldEntry xpath : xpathValues) {
            String value = entity.getAsString(xpath);
            if (value != null) {
                jsonObject.put(xpath.getName(), value);
            }
        }
        return jsonObject;
    }

    /*
     * returns an array of the json objects describing each image in the wikiPage.
     */
    public JsonJavaArray extractImagesFromWikiPage(String pageHTML){
    	JsonJavaArray imageAttributeObjects = new JsonJavaArray();
        
        Pattern p = Pattern.compile("<img([^>]*)>([^<>]*)</img>");
        Matcher m = p.matcher(pageHTML);
        while(m.find()) {
        	String imgAttrs = m.group(1);
        	if(imgAttrs==null){
        		break;
        	}
        	JsonJavaObject jsonAttrs = new JsonJavaObject();
        	imgAttrs = imgAttrs.trim();
        	String[] attrs = imgAttrs.split("\" ");
        	
        	for(int i = 0; i < attrs.length; i++){
        		String attr = attrs[i];
        		attr = attr.replaceAll("\"", "");
        		String[] nameValue = attr.split("=");
        		if(nameValue.length == 2){
        			jsonAttrs.put(nameValue[0], nameValue[1]);
        		}else if(nameValue.length == 1){
        			jsonAttrs.put(nameValue[0], "");
        		}
        	}
        	imageAttributeObjects.add(jsonAttrs);
        }
        
        return imageAttributeObjects;
    }
    
    /**
     * 
     * @param wikiLabel
     * @param outputFile
     * @throws ClientServicesException
     * @throws IOException
     * @throws JsonException
     */
    public void export(String wikiLabel, String outputFile)
            throws ClientServicesException, JsonException, IOException {
    	long start = System.currentTimeMillis();
        
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("includeTags", "true");
        params.put("ps", "20");
        Wiki wiki = wikiService.getWiki(wikiLabel, params);
        JsonJavaObject result = entityToJsonObject(wiki, WikiXPath.values(),
                null);
        
        JsonJavaArray pagesArray = new JsonJavaArray();
        int page = 1;
        int writtenPages = 0;
        boolean morePages = true;
        do{
        	params.put("page", Integer.toString(page));
        	EntityList<WikiPage> wikiPages = wikiService.getWikiPages(wikiLabel, params);
	        for (Iterator<WikiPage> iter = wikiPages.iterator(); iter.hasNext();) {
	            JsonJavaObject wikiEntry = new JsonJavaObject();
	            WikiPage wikiPage = iter.next();
	            entityToJsonObject(wikiPage, AtomXPath.values(), wikiEntry);
	            entityToJsonObject(wikiPage, WikiXPath.values(), wikiEntry);
	            String pageHTML = wikiPage.getContent();
	            wikiEntry.put("content", pageHTML);

	            wikiEntry.putArray("pageImages", extractImagesFromWikiPage(pageHTML));
	            pagesArray.add(wikiEntry);
	        }
	        writtenPages += wikiPages.size();
	        int total = wikiPages.getTotalResults();
	        if(writtenPages < total){
            	morePages = true;
                page++;
            }else{
            	morePages = false;
            }
        }while(morePages);
        
        result.putArray("pages", pagesArray);
        writeToFile(result, outputFile);
        double duration = (System.currentTimeMillis() - start) / 1000d;
        System.out.println("Export took: " + duration + "(secs)");
    }

    private void writeToFile(JsonJavaObject jsonObject, String outPutFile)
            throws JsonException, IOException {
        File file = new File(outPutFile);
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.write(JsonGenerator.toJson(JsonJavaFactory.instanceEx,
                jsonObject, false));
        printWriter.flush();
        printWriter.close();
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out
                    .println("Usage: java com.ibm.sbt.sample.app.ExplortWiki <connections_url> <wiki_label> <output_folder> <username> <password>");
            return;
        }

        String url = args[0];
        String wikiLabel = args[1];
        String outputFolder = args[2];
        String user = args[3];
        String password = args[4];

        ExportWiki exportWiki = null;
        try {
            exportWiki = new ExportWiki(url, user, password);
            exportWiki.export(wikiLabel, outputFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}