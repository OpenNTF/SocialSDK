/*
 * � Copyright IBM Corp. 2014
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
package com.ibm.sbt.web.resources;

import net.jazz.ajax.model.Resource;
import net.jazz.ajax.model.ResourceProvider;

public class LibraryProvider extends ResourceProvider {
	
	static final String SBT_LIBRARY_INIT = "sbt.library.init";

	@Override
	public Resource provide(String id) {
		System.out.println("LibraryProvider.provide: "+id);
		Resource resource = new LibraryResource(id);
		System.out.println("LibraryProvider.provide: "+resource);
		return resource;
	}
}
