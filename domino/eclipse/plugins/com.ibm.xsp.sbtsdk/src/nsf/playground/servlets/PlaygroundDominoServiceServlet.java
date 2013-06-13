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

package nsf.playground.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.playground.beans.DataAccessBean;
import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.sbtsdk.servlets.DominoServiceServlet;


public class PlaygroundDominoServiceServlet extends DominoServiceServlet {

	private static final long serialVersionUID = 1L;

	public PlaygroundDominoServiceServlet() {
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The API explorer sets the current environment as a request header
		// If this parameter exists, then we initialize it
		String envName = request.getHeader("x-env");
		if(StringUtil.isNotEmpty(envName)) {
			DataAccessBean dataAccess = DataAccessBean.get();
			PlaygroundEnvironment env = dataAccess.getEnvironment(envName);
			env.prepareEndpoints();
		}
		
		super.service(request, response);
	}
	
}
