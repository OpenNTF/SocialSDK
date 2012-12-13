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
package com.ibm.sbt.service.basic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;

/**
 * Basic proxy using a url.
 * <p>
 * This proxy is using the following syntax:
 *      http://myproxy.com/.../[http/myserver.com/mypath?myquerytring]
 * </p>
 * @author Philippe Riand
 */
public abstract class ProxyUrlService extends ProxyService {

    private String requestURI;
    
    public ProxyUrlService() {
    }
    
    protected String getProxyUrlPath() {
        return null;
    }

    @Override
    protected void initProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String pathinfo = request.getPathInfo();
        // Skip the URL_PATH of the proxy
        //   /xsp/.proxy/[URL_PATH/][http/...]
        String urlPath = getProxyUrlPath();
        int urlPathLength = urlPath!=null ? urlPath.length() : 0; 
        int startEndPoint = urlPathLength>0 ? getProxyUrlPath().length()+1 : 1;
        if(startEndPoint<pathinfo.length()) {
            String url = pathinfo.substring(startEndPoint+1).replaceFirst("\\/", "://");
            requestURI = url;
            return;
        }
        StringBuffer b = request.getRequestURL();
        String q = request.getQueryString();
        if(StringUtil.isNotEmpty(q)) {
            b.append('?');
            b.append(q);
        }
        throw new ServletException(StringUtil.format("Invalid url {0}",b.toString()));
    }
    
    @Override
    protected String getRequestURIPath(HttpServletRequest request) throws ServletException {
        return requestURI;
    }
}