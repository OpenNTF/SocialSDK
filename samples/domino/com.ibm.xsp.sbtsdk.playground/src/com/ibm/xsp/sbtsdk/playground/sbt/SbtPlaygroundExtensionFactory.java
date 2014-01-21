/*
 *  Copyright IBM Corp. 2012
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
package com.ibm.xsp.sbtsdk.playground.sbt;

import nsf.playground.extension.Endpoints;
import nsf.playground.extension.ImportOptions;
import nsf.playground.extension.PlaygroundFragment;

import com.ibm.sbt.playground.extension.PlaygroundExtensionFactory;
import com.ibm.xsp.sbtsdk.playground.sbt.extension.SbtEndpoints;
import com.ibm.xsp.sbtsdk.playground.sbt.extension.SbtFragment;
import com.ibm.xsp.sbtsdk.playground.sbt.extension.SbtImportOptions;


/**
 * Playground extension factory for the Social Business APIs.
 * 
 * @author priand
 */
public class SbtPlaygroundExtensionFactory extends PlaygroundExtensionFactory {
	
	public PlaygroundFragment fragment;
	public ImportOptions importOptions;
	public SbtEndpoints endpoints;

	public SbtPlaygroundExtensionFactory() {
	}

	public Object getExtension(Class<?> clazz) {
		if(clazz==PlaygroundFragment.class) {
			if(fragment==null) {
				fragment = new SbtFragment();
			}
			return fragment;
		}
		if(clazz==ImportOptions.class) {
			if(importOptions==null) {
				importOptions = new SbtImportOptions();
			}
			return importOptions;
		}
		if(clazz==Endpoints.class) {
			if(endpoints==null) {
				endpoints = new SbtEndpoints();
			}
			return endpoints;
		}
		return null;
	}
}
