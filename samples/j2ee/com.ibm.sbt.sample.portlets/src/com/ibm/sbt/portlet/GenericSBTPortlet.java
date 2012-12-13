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

package com.ibm.sbt.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * SBT GenericPortlet.
 * This portlet can be used as a base class for HTML/JSbased portlets.
 */
public abstract class GenericSBTPortlet extends GenericPortlet {

	public void init() throws PortletException{
		super.init();
	}
	
	protected abstract String getResourcePath(RenderRequest request);
	protected String[] getVariableNames() {
		return EMPTY_STRINGS;
	}
	private static String[] EMPTY_STRINGS = new String[0];
	protected boolean hasVariable(String name) {
		String[] allVars = getVariableNames();
		if(allVars!=null) {
			for(int i=0; i<allVars.length; i++) {
				if(allVars[i].equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		// Set the MIME type for the render response
		response.setContentType(request.getResponseContentType());
		
		// Generate the variable values
		// This should use a real JSON generator with proper encoding
		String[] vars = getVariableNames();
		if(vars!=null && vars.length>0) {
			PrintWriter pw = response.getWriter();
			pw.println("<script>");
			String jsName = "sbtContext";
			pw.print("var ");
			pw.print(jsName);
			pw.println("={");
			for(int i=0; i<vars.length; i++) {
				String name = vars[i];
				Object value = getVar(request, name);
				if(value!=null) {
					pw.print(name);
					pw.print(":'");
					pw.print(value);
					if(i<vars.length-1) {
						pw.println("',");
					} else {
						pw.println("'");
					}
				}
			}
			pw.println("};");
			pw.println("</script>");
		}

		// Invoke the page to render
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(getResourcePath(request));
		rd.include(request,response);
	}

//	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, java.io.IOException {
//	}

	@Override
	public void processEvent(EventRequest request, EventResponse response) throws PortletException, IOException {
		super.processEvent(request, response);
		
		String propName = request.getEvent().getName();
		if(hasVariable(propName)) {
			Object value = request.getEvent().getValue();
			setVar(request, propName, value);
		}
	}
	
	
	protected static final String VAR_MAP = "sbt.vars";
	
	protected Object getVar(PortletRequest req, String name) {
		Map<String,Object> vars = getVarMap(req, false);
		if(vars!=null) {
			return vars.get(name);
		}
		return null;
	}
	protected void setVar(PortletRequest req, String name, Object value) {
		Map<String,Object> vars = getVarMap(req, true);
		if(value!=null) {
			vars.put(name, value);
		} else {
			vars.remove(name);
		}
	}
	protected Map<String,Object> getVarMap(PortletRequest req, boolean create) {
		Map<String,Object> vars = (Map<String,Object>)req.getPortletSession().getAttribute(VAR_MAP);
		if(vars==null && create) {
			vars = new HashMap<String, Object>();
			req.getPortletSession().setAttribute(VAR_MAP, vars);
		}
		return vars;
	}
}
