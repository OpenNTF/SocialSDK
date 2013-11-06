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

import java.util.ArrayList;
import java.util.List;


/**
 * Debug output in memory.
 * 
 * @author Philippe Riand
 */
public class DebugMemoryOutput extends DebugOutput {
	
	public static final int MAX_ENTRIES	= 64;  
	
	private List<DebugServiceHook> entries = new ArrayList<DebugServiceHook>(MAX_ENTRIES);
	
	public DebugMemoryOutput() {
	}
	
	public synchronized DebugServiceHook[] getEntries() {
		return entries.toArray(new DebugServiceHook[entries.size()]);
	}

	@Override
	public synchronized void add(DebugServiceHook hook) {
		if(entries.size()==MAX_ENTRIES) {
			entries.remove(0);
		}
		entries.add(hook);
	}
	
	@Override
	public synchronized void terminate(DebugServiceHook hook) {
	}
}
