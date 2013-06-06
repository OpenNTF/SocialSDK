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
package com.ibm.sbt.sample.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;


/**
 * @author Mark Wallace
 * @date 16 Jan 2013
 */
public class Util {

    public static String[][] JS_LIBS = new String[][] {
        // title, id, jsLibPath, jsLibParams, libraryUrl
        { "Dojo Toolkit 1.8.0", "dojo180", "includes/dojo180.jsp", "/library?lib=dojo&ver=1.8.0" },
        { "Dojo Toolkit 1.8.0 CDN", "dojo180cdn", "includes/dojo180cdn.jsp", "/library?lib=dojo&ver=1.8.0" },
        { "Dojo Toolkit 1.8.0 CDN Async", "dojo180cdnasync", "includes/dojo180cdnasync.jsp", "/library?lib=dojo&ver=1.8.0" },
        { "Dojo Toolkit 1.7.0 CDN", "dojo170cdn", "includes/dojo170cdn.jsp", "/library?lib=dojo&ver=1.7.0" }, 
        { "Dojo Toolkit 1.7.0 CDN Async", "dojo170cdnasync", "includes/dojo170cdnasync.jsp", "/library?lib=dojo&ver=1.7.0" }, 
        { "Dojo Toolkit 1.4.3", "dojo143", "includes/dojo143.jsp", "/library?lib=dojo&ver=1.4.3" }, 
        { "jQuery 1.8.0", "jquery180", "includes/jquery180.jsp", "/library?lib=jquery&ver=1.8.0" },
        { "jQuery 1.8.0 ScriptTag", "jquery180ST", "includes/jquery180ScriptTag.jsp", "/library?lib=jquery&ver=1.8.0" },
        //{ "jQuery 1.8.0 Debug", "jquery180debug", "includes/jquery180debug.jsp", "/library?lib=jquery&ver=1.8.0" }, 
        { "jQuery 1.9.1 CDN", "jquery191cdn", "includes/jquery191cdn.jsp", "/library?lib=jquery&ver=1.9.1" },
        { "jQuery 1.9.1 CDN Debug", "jquery191cdndebug", "includes/jquery191cdndebug.jsp", "/library?lib=jquery&ver=1.9.1" }
    };

    public static String[][] THEMES = new String[][] {
        // title, id
        { "Dojo Claro", "dojo" },
        { "Bootstrap", "bootstrap" },
        { "One UI", "oneui" }
    };
    
    public static String[][] ENVIRONMENTS = new String[][] {
        // title, id
        { "Default", "defaultEnvironment" },
        { "SmartCloud", "smartcloudEnvironment" },
        { "Open Social", "openSocial"}
    };
    
    public static String[] BOOTSTRAP_STYLES = {   
        "/sbt.bootstrap211/bootstrap/css/bootstrap.css",
        "/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css"
    };
    
    public static String[] DOJO_STYLES = new String[0];
    
    public static String[] ONE_UI_STYLES = { 
        // "/sbt.oneui303/css/base/core.css", // this theme doesn't seem to be used in connections.
        "/connections/resources/web/_style?include=com.ibm.lconn.core.styles.oneui3/base/package3.css",
        "/connections/resources/web/_style?include=com.ibm.lconn.core.styles.oneui3/sprites.css",
        "/connections/resources/web/_lconntheme/default.css?version=oneui3&rtl=false",
        "/connections/resources/web/_lconnappstyles/default/search.css?version=oneui3&rtl=false"
    };

    private static Map<String, String[]> _styleMap = new HashMap<String, String[]>();
    static {
        _styleMap.put("bootstrap", BOOTSTRAP_STYLES);
        _styleMap.put("dojo", DOJO_STYLES);
        _styleMap.put("oneui", ONE_UI_STYLES);
    }
    
    public static String[] combineStyleArrays(){
        int allStylesLength = 0;
        for(String[] array : _styleMap.values()){
            allStylesLength += array.length;
        }
        
        String[] allStyles = new String[allStylesLength];
        int offset = 0;
        for(String[] array : _styleMap.values()){
            System.arraycopy(array, 0, allStyles, offset, array.length);
            offset+=array.length;
        }
        
        return allStyles;
    }
    
    public static String[] ALL_STYLES = combineStyleArrays();
    
    static{
        _styleMap.put("all", ALL_STYLES);
    };
    
    private static Map<String, String> _bodyClassMap = new HashMap<String, String>();
    static {
        _bodyClassMap.put("bootstrap", "claro");
        _bodyClassMap.put("dojo", "claro");
        _bodyClassMap.put("oneui", "lotusui30_body lotusui30_fonts lotusui30");
        _bodyClassMap.put("all", "lotusui30_body lotusui30_fonts lotusui30");
    }
    
    public static String[][] getJavaScriptLibs(HttpServletRequest request) {
        return JS_LIBS;
    }
    
    public static String getSelectedLibrary(HttpServletRequest request) {
        String id = request.getParameter("jsLibId");
        return (StringUtil.isEmpty(id)) ? JS_LIBS[0][1] : id; 
    }
    
    public static String getLibraryInclude(HttpServletRequest request) {
        String id = request.getParameter("jsLibId");
        if (StringUtil.isEmpty(id)) {
            id = JS_LIBS[0][1];
        }
        String libraryInclude = null;
        for (int i=0; i<JS_LIBS.length; i++) {
            if (JS_LIBS[i][1].equals(id)) {
                libraryInclude = JS_LIBS[i][2];
                break;
            }
        }  
        return libraryInclude;
    }
    
    public static String getLibraryUrl(HttpServletRequest request) {
        String id = request.getParameter("jsLibId");
        if (StringUtil.isEmpty(id)) {
            id = JS_LIBS[0][1];
        }
        String libraryUrl = null;
        for (int i=0; i<JS_LIBS.length; i++) {
            if (JS_LIBS[i][1].equals(id)) {
                libraryUrl = JS_LIBS[i][3];
                break;
            }
        }  
        String transport = request.getParameter("transport");
        if (StringUtil.isNotEmpty(transport)) {
            libraryUrl += "&transport=" + transport;
        }
        String environment = request.getParameter("env");
        if (StringUtil.isNotEmpty(environment)) {
            libraryUrl += "&env=" + environment;
        }
        String baseUrl = UrlUtil.getBaseUrl(request);
        return PathUtil.concat(baseUrl, libraryUrl,'/');
    }
    
    public static String getJsLibId(HttpServletRequest request) {
        String id = request.getParameter("jsLibId");
        if (StringUtil.isEmpty(id)) {
            id = JS_LIBS[0][1];
        }
        return id;
    }
    
    public static String[][] getThemes(HttpServletRequest request) {
        return THEMES;
    }
    
    public static String getThemeId(HttpServletRequest request) {
        String id = request.getParameter("themeId");
        if (StringUtil.isEmpty(id)) {
            id = THEMES[0][1];
        }
        return id;
    }
    
    public static String getSelectedTheme(HttpServletRequest request) {
        String id = request.getParameter("themeId");
        return (StringUtil.isEmpty(id)) ? THEMES[0][1] : id; 
    }
    
    public static String[] getStyles(HttpServletRequest request, String themeId) {
        if (StringUtil.isEmpty(themeId)) {
            themeId = getThemeId(request);
        }
        
        String jsLibId = getJsLibId(request);
        
        String environment = request.getParameter("env");
        if(StringUtil.isEmpty(environment))
            environment = Context.get().getProperty("environment");
        
        String endpointName = "connections";
        String baseUrl = Context.get().getProperty(endpointName + ".url");
        
        String[] result;
        if (_styleMap.containsKey(themeId)) {
            result = _styleMap.get(themeId);
            if(StringUtil.equals(themeId, "oneui"))
                result = addBaseUrls(baseUrl, result);
        } else if (_styleMap.containsKey(jsLibId + themeId)) {
            result =  _styleMap.get(jsLibId + themeId);
            if(StringUtil.equals(themeId, "oneui"))
                result = addBaseUrls(baseUrl, result);
        } else {
            result = new String[0];
        }
        
        return result;
    }
    
    /**
    * Returns the paths array where every String has been replaced by the same String with baseUrl in front.
    * @param base
    * @param paths
    * @return
    */
    private static String[] addBaseUrls(String baseUrl, String[] paths){
        String[] result = new String[paths.length];
        for (int i = 0; i < paths.length; i++){
            result[i] = baseUrl + paths[i];
        }
    
        return result;
    }
    
    public static String getBodyClass(HttpServletRequest request, String themeId) {
        if (StringUtil.isEmpty(themeId)) {
            themeId = getThemeId(request);
        }
        return _bodyClassMap.get(themeId);
    }
    
    public static String getPageUrl(HttpServletRequest request, String jsLibId, String themeId) {
        return getPageUrl(request, jsLibId, themeId, null);
    }
    
    public static String getPageUrl(HttpServletRequest request, String jsLibId, String themeId, String envId) {
        String url = request.getRequestURL().toString();
        if (url.indexOf('?') != -1) {
            url = url.substring(0, url.indexOf('?'));
        }
        
        if (StringUtil.isEmpty(jsLibId)) {
            jsLibId = getJsLibId(request);
        }
        url += "?jsLibId=" + jsLibId;
        
        if (StringUtil.isEmpty(themeId)) {
            themeId = getThemeId(request);
        }
        url += "&themeId=" + themeId;
        
        if (StringUtil.isEmpty(envId)) {
            envId = getEnvironmentId(request);
        }
        url += "&env=" + envId;
        
        String snippet = request.getParameter("snippet");
        if (StringUtil.isNotEmpty(snippet)) {
            url += "&snippet=" + snippet;
        }
        
        return url;
    }

    public static String getJavaScriptPreview(HttpServletRequest request) {
        String url = "javascriptPreview.jsp";
        
        String jsLibId = getJsLibId(request);
        url += "?jsLibId=" + jsLibId;
        
        String themeId = getThemeId(request);
        url += "&themeId=" + themeId;
        
        String snippet = request.getParameter("snippet");
        if (StringUtil.isNotEmpty(snippet)) {
            url += "&snippet=" + snippet;
        }
        
        return url;
    }
    
    public static String[][] getEnvironments(HttpServletRequest request) {
        return ENVIRONMENTS;
    }

    public static String getEnvironmentId(HttpServletRequest request) {
        String id = request.getParameter("env");
        if (StringUtil.isEmpty(id)) {
            id = ENVIRONMENTS[0][1];
        }
        return id;
    }
    
    //
    // Internals
    // 

}
