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
package com.ibm.sbt.sample.web.util;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XResult;
import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetBrowser;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippet;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetNodeFactory;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.ServletVFS;
import com.ibm.sbt.playground.vfs.VFSFile;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * Factory for retrieving JS and Java Snippets.
 */
public class SnippetFactory {

	static String jsRootPath = "/samples/js/";
	static String javaRootPath = "/samples/java/";
	
	/**
	 * Get a JSSnippet by name, if it is not found in sbt.sample.web it will look in sbtx.sample.web.
	 * 
	 * @param context
	 * @param request
	 * @param snippetName
	 * @return JSSnippet corresponding to snippetName 
	 */
	public static JSSnippet getJsSnippet(ServletContext context, HttpServletRequest request, String snippetName) {
		JSSnippet snippet = (JSSnippet) getAsset(context, request, snippetName, new JSSnippetNodeFactory(), jsRootPath);
        if (snippet == null) {
            snippet = loadAssetRemote(context, request, snippetName);
        }
        return snippet;
    }
	
	/**
	 * Get a RootNode object which allows JSSnippets to be searched and manipulated.
	 * 
	 * @param context
	 * @return RootNode of the local JSSnippets.
	 */
	public static RootNode getJsSnippets(ServletContext context){
		return getSnippets(context, new JSSnippetNodeFactory(), jsRootPath);
	}
	
	/**
	 * Get JS snippets in JSON format, including sbtx.sample.web's JS snippets.
	 * 
	 * @param context
	 * @param request
	 * @return JSSnippets in JSON format. 
	 */
	public static String getJsSnippetsAsJson(ServletContext context, HttpServletRequest request) {
	    RootNode root = getJsSnippets(context);
	    String json = root.getAsJson();
	    String jsonEx = readRemoteJson(context, request);
	    if (StringUtil.isNotEmpty(jsonEx)) {
	        jsonEx = jsonEx.substring("[{\"id\":\"_root\",\"name\":\"_root\",\"children\":[".length());
	        json = json.substring(0, json.length()-3) + "," + jsonEx;
	    }
	    return json;
	}

	/**
	 * Get a JavaSnippet by name.
	 * 
	 * @param context
	 * @param request
	 * @param snippetName
	 * @return JavaSnippet corresponding to snippetName 
	 */
	public static JavaSnippet getJavaSnippet(ServletContext context, HttpServletRequest request, String snippetName) {
		JavaSnippet result = (JavaSnippet) getAsset(context, request, snippetName, new JavaSnippetNodeFactory(), javaRootPath);
        return result;
    }
	
	/**
	 * Get a RootNode object which allows JavaSnippets to be searched and manipulated.
	 * 
	 * @param context
	 * @return RootNode of the local JavaSnippets.
	 */
	public static RootNode getJavaSnippets(ServletContext context){
		return getSnippets(context, new JavaSnippetNodeFactory(), javaRootPath);
	}
	
	/**
	 * Returns the root VFSFile for JS Samples.
	 * 
	 * @param context
	 * @return
	 */
	public static VFSFile getJsRootFile(ServletContext context){
		return getRootFile(context, jsRootPath);
	}

	/**
	 * Returns the root VFSFile for Java Samples.
	 * 
	 * @param context
	 * @return
	 */
	public static VFSFile getJavaRootFile(ServletContext context){
		return getRootFile(context, javaRootPath);
	}

	/**
	 * Returns an Asset based on its name, NodeFactory type and root path.
	 * 
	 * @param context
	 * @param request
	 * @param assetName
	 * @param nodeFactory
	 * @param rootPath
	 * @return
	 */
	private static Asset getAsset(ServletContext context, HttpServletRequest request, String assetName, NodeFactory nodeFactory, String rootPath){
		try {
	        RootNode root = getSnippets(context, nodeFactory, rootPath);
	        return root.loadAsset(getRootFile(context, rootPath), assetName);
	    } catch (IOException ioe) {
	        return null;
	    }
	}

	/**
	 * Returns a VFSFile for the given path and context.
	 * 
	 * @param context
	 * @param relPath The folder path.
	 * @return VFSFile from the relPath and context.
	 */
	private static VFSFile getRootFile(ServletContext context, String relPath) {
		ServletVFS vfs = new ServletVFS(context, relPath);
		return vfs.getRoot();
	}

	/**
	 * Get a RootNode object corresponding to the NodeFactory type and ServletContext
	 * 
	 * @param context
	 * @param nodeFactory The NodeFactory of the type of snippet to be retrieved.
	 * @param path The path to the root of the snippets to be retrieved.
	 * @return RootNode of the Snippets.
	 */
	private static RootNode getSnippets(ServletContext context, NodeFactory nodeFactory, String path){
		RootNode root;
		try {
			VFSFile file = getRootFile(context, path);
			root = readAssets(context, file, nodeFactory);
		} catch(IOException ex) {
			root = new RootNode();
		}
		return root;
	}

	/**
	 * A method which will, for a given root folder and NodeFactory, return the corresponding RootNode of Snippets.
	 * 
	 * @param context
	 * @param file The root folder of the snippets.
	 * @param nodeFactory The NodeFactory corresponding to the snippet type.
	 * @return RootNode of snippets, which allows search and manipulation of snippets.
	 * @throws IOException
	 */
	private static RootNode readAssets(ServletContext context, VFSFile file, NodeFactory nodeFactory) throws IOException{
		AssetBrowser imp = new AssetBrowser(file, nodeFactory);
		return (RootNode)imp.readAssets(new RootNode(), null);
	}

	private static String readRemoteJson(ServletContext context, HttpServletRequest request) {
	    String baseUrl = computeSbtxSampleWebUrl(request);
	    String sbtxJson = httpGet(baseUrl + "/snippet?format=json");
	    
	    baseUrl = computeSbtApiWebUrl(request);
        String apiJson = httpGet(baseUrl + "/snippet?format=json");
        
        String remoteJson = null;
        if (apiJson == null) {
            remoteJson = sbtxJson;
        } else if (sbtxJson == null) {
            remoteJson = apiJson;
        } else {
            apiJson = apiJson.substring("[{\"id\":\"_root\",\"name\":\"_root\",\"children\":[".length());
            remoteJson = sbtxJson.substring(0, sbtxJson.length()-3) + "," + apiJson;
        }
	    
	    return remoteJson;
	}
	
    private static JSSnippet loadAssetRemote(ServletContext context, HttpServletRequest request, String snippetId) {
        String baseUrl = computeSbtxSampleWebUrl(request);
        String xml = httpGet(baseUrl + "/snippet?snippet=" + snippetId);
        if (xml != null) {
            return createSnippetFromXml(xml);
        } else {
            baseUrl = computeSbtApiWebUrl(request);
            xml = httpGet(baseUrl + "/snippet?snippet=" + snippetId);
            if (xml != null) {
                return createSnippetFromXml(xml);
            }
        }
        return null;
    }
    
    private static String computeSbtxSampleWebUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/sbtx.sample.web";
    }
    
    private static String computeSbtApiWebUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/sbt.api.web";
    }
    
    private static String httpGet(String url) {
        try {
            DefaultHttpClient httpClient = SSLUtil.wrapHttpClient(new DefaultHttpClient());
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity content = response.getEntity();
                java.util.Scanner scanner = new java.util.Scanner(content.getContent());
                scanner.useDelimiter("\\A");
                String result = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        
    }
    
    private static JSSnippet createSnippetFromXml(String xml) {
        try {
            Document document = DOMUtil.createDocument(xml);
            XResult unid = DOMUtil.evaluateXPath(document, "unid");
            XResult js = DOMUtil.evaluateXPath(document.getDocumentElement(), "js");
            XResult html = DOMUtil.evaluateXPath(document.getDocumentElement(), "html");
            XResult css = DOMUtil.evaluateXPath(document.getDocumentElement(), "css");
            XResult theme = DOMUtil.evaluateXPath(document.getDocumentElement(), "theme");
            XResult description = DOMUtil.evaluateXPath(document.getDocumentElement(), "description");
            XResult tags = DOMUtil.evaluateXPath(document.getDocumentElement(), "tags");
            XResult labels = DOMUtil.evaluateXPath(document.getDocumentElement(), "labels");
            
            JSSnippet snippet = new JSSnippet();
            if(unid != null) 
            	snippet.setUnid(unid.getStringValue());
            if (js != null) 
            	snippet.setJs(js.getStringValue());
            if (html != null) 
            	snippet.setHtml(html.getStringValue());
            if (css != null) 
            	snippet.setCss(css.getStringValue());
            
            Properties p = new Properties();
            snippet.init(p);
            
            if(theme != null && theme.getStringValue() != null) 
            	snippet.setTheme(theme.getStringValue());
            if(description != null && description.getStringValue() != null) 
            	snippet.setDescription(description.getStringValue());
            if(tags != null && tags.getValues() != null) 
            	snippet.setTags(tags.getValues());
            if(labels != null && labels.getValues() != null) 
            	snippet.setLabels(labels.getValues());
            
            return snippet;
        } catch (Exception e) {
            return null;
        }
    }
    
}
