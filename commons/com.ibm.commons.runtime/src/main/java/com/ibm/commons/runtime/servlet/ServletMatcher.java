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
package com.ibm.commons.runtime.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Matcher.
 * 
 * This interface is used by the servlet dispatcher to find the right servlet to dispatch a request to.
 * 
 * @author priand
 */
public interface ServletMatcher {
	
	/**
	 * Return the weight of this matcher.
	 * When multiple matchers can handle the same request, then the dispatcher choose the one with the
	 * highest weight.
	 * @return
	 */
	public int matchLengh();
	
	/**
	 * Execute the request on a particular matcher.
	 * After the dispatcher choose the right matcher, the it dispatches the actual request to it. The
	 * matcher generally holds a pointer to the servlet to dispatch to.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
