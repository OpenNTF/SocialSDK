/*
 * © Copyright IBM Corp. 2010
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

package nsf.playground.servlets;

import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.xsp.extlib.servlet.ServletFactory;

/**
 * Service factory for the toolkit servlet.
 */
public class PlaygroundToolkitServletFactory extends ServletFactory {

	public static final String LIBRARY_PATHINFO = RuntimeConstants.AbstractNotesDominoConstants.LIBRARY_PATHINFO+"pg";
	
	public PlaygroundToolkitServletFactory() {
		super(LIBRARY_PATHINFO,PlaygroundToolkitServlet.class.getName(),"SBT Library Servlet for the Playground");
	}
 }
