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
package com.ibm.sbt.service.debug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.sbt.service.debug.DebugServiceHookFactory.Type;



/**
 * Proxy hook.
 * 
 * @author Philippe Riand
 */
public class DebugProxyHook extends DebugServiceHook {
	
	private DumpRequest proxiedRequest = new DumpRequest();
	private DumpResponse proxiedResponse = new DumpResponse();

	public DebugProxyHook(DebugOutput debugOutput, HttpServletRequest request, HttpServletResponse response) {
		super(debugOutput, Type.PROXY, request, response);
	}
	
	public DumpRequest getDumpRequest() {
		return proxiedRequest;
	}
	
	public DumpResponse getDumpResponse() {
		return proxiedResponse;
	}
}
