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
package demo;

import java.io.IOException;

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
import com.ibm.sbt.playground.assets.AssetBrowser;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.ServletVFS;
import com.ibm.sbt.playground.vfs.VFSFile;
import com.ibm.sbt.services.util.SSLUtil;


/**
 * Definition of a JS code snippet factory.
 */
public class SnippetFactory extends JSSnippetNodeFactory {

    public static String getSnippetsAsJson(ServletContext context, HttpServletRequest request) {
        RootNode root = SnippetFactory.getSnippets(context);
        String json = root.getAsJson();
        String jsonEx = readRemoteJson(context, request);
        if (StringUtil.isNotEmpty(jsonEx)) {
            jsonEx = jsonEx.substring("[{\"id\":\"_root\",\"name\":\"_root\",\"children\":[".length());
            json = json.substring(0, json.length()-3) + "," + jsonEx;
        }
        return json;
    }
    
    public static JSSnippet getSnippet(ServletContext context, HttpServletRequest request, String snippetName) {
        RootNode root = SnippetFactory.getSnippets(context);
        JSSnippet snippet = null;
        try {
            snippet = (JSSnippet)root.loadAsset(SnippetFactory.getRootFile(context), snippetName);
        } catch (IOException ioe) {
        }
        if (snippet == null) {
            snippet = loadAssetRemote(context, request, snippetName);
        }
        return snippet;
    }
    
	public static RootNode getSnippets(ServletContext context) {
		RootNode root = null;
		if(root==null) {
			try {
				root = readAssets(context);
			} catch(IOException ex) {
				root = new RootNode();
			}
		}
		return root;
	}
	
	public static VFSFile getRootFile(ServletContext context) {
		ServletVFS vfs = new ServletVFS(context, "/samples/js/");
		return vfs.getRoot();
	}
	
	@Override
	public AssetNode createAssetNode(com.ibm.sbt.playground.assets.CategoryNode parent, String name) {
	    return new DemoJSSnippetNode(parent, name);
	}
	
    private static RootNode readAssets(ServletContext context) throws IOException {
        VFSFile file = getRootFile(context);
        AssetBrowser imp = new AssetBrowser(file,new SnippetFactory());
        return (RootNode)imp.readAssets(new RootNode(),null);
    }
    
	private static String readRemoteJson(ServletContext context, HttpServletRequest request) {
	    String baseUrl = computeSbtxSampleWebUrl(request);
	    return httpGet(baseUrl + "/snippet?format=json");
	}
	
    private static JSSnippet loadAssetRemote(ServletContext context, HttpServletRequest request, String snippetId) {
        String baseUrl = computeSbtxSampleWebUrl(request);
        String xml = httpGet(baseUrl + "/snippet?snippet=" + snippetId);
        if (xml != null) {
            return createSnippetFromXml(xml);
        }
        return null;
    }
    
    private static String computeSbtxSampleWebUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/sbtx.sample.web";
    }
    
    private static String httpGet(String url) {
        try {
            DefaultHttpClient httpClient = SSLUtil.wrapHttpClient(new DefaultHttpClient());
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity content = response.getEntity();
                java.util.Scanner scanner = new java.util.Scanner(content.getContent()).useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
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
            XResult js = DOMUtil.evaluateXPath(document.getDocumentElement(), "js");
            XResult html = DOMUtil.evaluateXPath(document.getDocumentElement(), "html");
            XResult css = DOMUtil.evaluateXPath(document.getDocumentElement(), "css");
            
            JSSnippet snippet = new JSSnippet();
            if (js != null) snippet.setJs(js.getStringValue());
            if (html != null) snippet.setHtml(html.getStringValue());
            if (css != null) snippet.setCss(css.getStringValue());
            return snippet;
        } catch (Exception e) {
            return null;
        }
    }

}
