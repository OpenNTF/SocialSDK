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

package com.ibm.sbt.services.endpoints;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.runtime.Context;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * Bean that provides a authentication using an LTPA token.
 * 
 * @author Philippe Riand
 * @author Niklas Heidloff
 */
public class SSOEndpoint extends AbstractEndpoint {

    private static final long serialVersionUID = 1L;

    public SSOEndpoint() {
    }

    @Override
	public boolean isAuthenticated() throws ClientServicesException {
        return true;
    }

    @Override
	public void authenticate(boolean force) throws ClientServicesException {
    }

    public void redirect() {
    }

    @Override
	public void initialize(DefaultHttpClient httpClient) {
        HttpRequestInterceptor ltpaInterceptor = new LtpaInterceptor(getUrl());
        httpClient.addRequestInterceptor(ltpaInterceptor, 0);
    }

    private static class LtpaInterceptor implements HttpRequestInterceptor {

        String _domain;

        public LtpaInterceptor(String url) {
            _domain = url.substring(url.indexOf("//")+2);
            if(_domain.indexOf(":")!=-1) {
                _domain = _domain.substring(0, _domain.indexOf(":"));
            }
        }

        @Override
		@SuppressWarnings("unchecked")
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {

            CookieStore cookieStore;
            cookieStore = new BasicCookieStore();

            Context ctx = Context.get();

            java.util.Map<java.lang.String, java.lang.Object> cookieMap = ctx.getRequestCookieMap();
            if(cookieMap.containsKey("LtpaToken")) {
                javax.servlet.http.Cookie cookie = (javax.servlet.http.Cookie) cookieMap.get("LtpaToken");
                BasicClientCookie2 cookie1 = new BasicClientCookie2(cookie.getName(), cookie.getValue());
                if(cookie.getDomain()!=null) {
                    cookie1.setDomain(cookie.getDomain());
                }
                else {
                    cookie1.setDomain(_domain);
                }
                if(cookie.getPath()!=null) {
                    cookie1.setPath(cookie.getPath());
                }
                else {
                    cookie1.setPath("/");
                }
                cookieStore.addCookie(cookie1);
            }

            if(cookieMap.containsKey("LtpaToken2")) {
                javax.servlet.http.Cookie cookie = (javax.servlet.http.Cookie) cookieMap.get("LtpaToken2");
                BasicClientCookie2 cookie2 = new BasicClientCookie2(cookie.getName(), cookie.getValue());
                if(cookie.getDomain()!=null) {
                    cookie2.setDomain(cookie.getDomain());
                }
                else {
                    cookie2.setDomain(_domain);
                }
                if(cookie.getPath()!=null) {
                    cookie2.setPath(cookie.getPath());
                }
                else {
                    cookie2.setPath("/");
                }
                cookieStore.addCookie(cookie2);
            }

            context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }
    }
}