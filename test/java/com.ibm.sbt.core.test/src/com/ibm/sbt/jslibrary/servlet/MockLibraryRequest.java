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
package com.ibm.sbt.jslibrary.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

import com.ibm.sbt.jslibrary.MockSBTEnvironment;
import com.ibm.sbt.servlet.MockHttpServletRequest;
import com.ibm.sbt.servlet.MockHttpServletResponse;

/**
 * @author mwallace
 *
 */
public class MockLibraryRequest extends LibraryRequest {

	/**
	 * Constructor
	 */
	public MockLibraryRequest() throws ServletException, IOException {
		super(new MockHttpServletRequest(), new MockHttpServletResponse());
		init(new MockSBTEnvironment(),
			"http://localhost:8080/sbt", 
			"http://localhost:8080/sbt/js/sdk", 
			"http://localhost:8080/proxy", 
			"/xhr/IFrameContent.html");
	}

}
