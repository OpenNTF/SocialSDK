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
package com.ibm.sbt.service.core.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.core.IServiceHandler;

/**
 * Abstract Proxy Handler.
 * @author priand
 */
public abstract class AbstractServiceHandler implements IServiceHandler {
    
    public AbstractServiceHandler() {
    }
    
    @Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String req = request.getMethod();
    	if(StringUtil.endsWithIgnoreCase(req, "get")) {
    		doGet(request, response);
    	} else if(StringUtil.endsWithIgnoreCase(req, "post")) {
    		doPost(request, response);
    	} else if(StringUtil.endsWithIgnoreCase(req, "put")) {
    		doPut(request, response);
    	} else if(StringUtil.endsWithIgnoreCase(req, "delete")) {
    		doDelete(request, response);
    	}
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}