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

import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.debug.DebugServiceHook.DumpRequest;
import com.ibm.sbt.service.debug.DebugServiceHook.DumpResponse;
import com.ibm.sbt.service.debug.DebugServiceHook.NameValue;


/**
 * Debug output in memory.
 * 
 * @author Philippe Riand
 */
public class DebugStreamOutput extends DebugOutput {
	
	private PrintStream ps;
	private int indent;
	
	public DebugStreamOutput() {
		this(null);
	}
	public DebugStreamOutput(PrintStream ps) {
		this.ps = ps!=null ? ps : Platform.getInstance().getOutputStream();
	}
	
	public PrintStream getOutputStream() {
		return ps;
	}

	@Override
	public synchronized void add(DebugServiceHook hook) {
	}
	
	@Override
	public synchronized void terminate(DebugServiceHook hook) {
		outHeader(hook);
		indent++;
		
		out(">>> Request");
		indent++;
		outRequest(hook,hook.getRequestWrapper().getServiceRequest());
		indent--;
		if(hook instanceof DebugProxyHook) {
			DebugProxyHook dhook = (DebugProxyHook)hook;
			out(">>> Proxied Request");
			indent++;
				outRequest(hook,dhook.getDumpRequest());
			indent--;
		}

		if(hook instanceof DebugProxyHook) {
			DebugProxyHook dhook = (DebugProxyHook)hook;
			out(">>> Proxied Response");
			indent++;
				outResponse(hook,dhook.getDumpResponse());
			indent--;
		}
		out(">>> Response");
		indent++;
		outResponse(hook,hook.getResponseWrapper().getServiceResponse());
		indent--;
		
		indent--;
	}
	protected void outHeader(DebugServiceHook hook) {
		out("{0}, {1}",(new Date(hook.getStartTS())),hook.getType().toString());
	}
	protected void outRequest(DebugServiceHook hook, DumpRequest request) {
		outValue("Method", request.getMethod());
		outValue("Url", request.getUrl());
		outList("-- Parameters",request.getParameters());
		outList("-- Headers",request.getHeaders());
		outList("-- Cookies",request.getCookie());
	}
	protected void outResponse(DebugServiceHook hook, DumpResponse response) {
		outValue("Status", Integer.toString(response.getStatus()));
		outList("-- Headers",response.getHeaders());
		outList("-- Cookies",response.getCookie());
	}

	protected void out(String fmt, Object...parameters) {
		String msg = StringUtil.format(fmt, parameters);
		if(indent>0) {
			ps.print(StringUtil.repeat("  ", indent));
		}
		ps.println(msg);
	}

	protected void outList(String title, List<NameValue> values) {
		if(values!=null && !values.isEmpty()) {
			out("{0}",title);
			for(NameValue nv: values) {
				indent++;
				outValue(nv.getName(),nv.getValue());
				indent--;
			}
		}
	}
	protected void outValue(String name, String value) {
		if(StringUtil.isNotEmpty(value)) {
			out("{0}: {1}",name, value);
		}
	}
}
