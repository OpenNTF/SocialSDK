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
package com.ibm.commons.runtime.impl.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.impl.AbstractContext;
import com.ibm.commons.util.StringUtil;

/**
 * @author Mark Wallace
 * @date 6 Dec 2012
 */
public class ContextStandalone extends AbstractContext {

	/**
	 * @param application
	 * @param request
	 * @param response
	 */
	public ContextStandalone(Application application, Object request, Object response) {
		super(application, request, response);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Context#getCurrentUserId()
	 */
	@Override
	public String getCurrentUserId() {
		return ANONYMOUS;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Context#sendRedirect(java.lang.String)
	 */
	@Override
	public void sendRedirect(String redirectUrl) throws IOException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Context#encodeUrl(java.lang.String)
	 */
	@Override
	public String encodeUrl(String url) {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Context#createSessionMap()
	 */
	@Override
	protected Map<String, Object> createSessionMap() {
		return new HashMap<String, Object>();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Context#createRequestMap()
	 */
	@Override
	protected Map<String, Object> createRequestMap() {
		return new HashMap<String, Object>();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Context#createRequestParameterMap()
	 */
	@Override
	protected Map<String, Object> createRequestParameterMap() {
		return new HashMap<String, Object>();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Context#createRequestCookieMap()
	 */
	@Override
	protected Map<String, Object> createRequestCookieMap() {
		return new HashMap<String, Object>();
	}

	@Override
	public String getProperty(String propertyName) {
		String property = System.getProperty(propertyName);
		return StringUtil.isEmpty(property)?getProperty(propertyName,null):property;
	}

}
