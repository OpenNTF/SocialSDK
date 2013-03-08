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

import com.ibm.sbt.log.SbtLogger;


/**
 * Debug service hook factory.
 * 
 * @author Philippe Riand
 */
public class DebugServiceHookFactory {
	
	public enum Type {
		PROXY
	};

	private static final DebugServiceHookFactory instance = new DebugServiceHookFactory();
	public static DebugServiceHookFactory get() {
		return instance;
	}
	
	private DebugOutput debugOutput;
	
	protected DebugServiceHookFactory() {
		this.debugOutput = new DebugStreamOutput();
	}
	
	
	public DebugServiceHook get(Type type, HttpServletRequest request, HttpServletResponse response) {
		if(type==Type.PROXY) {
			if(SbtLogger.PROXY.isTraceDebugEnabled()) {
				return new DebugProxyHook(debugOutput, request,response);
			}
		}
		return null;
	}
}
