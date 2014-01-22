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

package com.ibm.sbt.service.proxy;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing SBT proxy ( Default proxy )
 * @author Manish Kataria
 */

public class SBTProxy extends AbstractProxy {
	
	static final String	sourceClass	= SBTProxy.class.getName();
	static final Logger	logger = Logger.getLogger(sourceClass);
	private final String module = "Proxy";

	@Override
	public String rewriteUrl(String apiUrl) {
    	if (logger.isLoggable(Level.INFO)) {
    		String msg = MessageFormat.format("URL computed from SBTProxy is {0}", apiUrl);
    		logger.log(Level.INFO, msg);
    	}
		return apiUrl;
	}

	@Override
	public String getProxyModule() {
		return module;
	}
}
