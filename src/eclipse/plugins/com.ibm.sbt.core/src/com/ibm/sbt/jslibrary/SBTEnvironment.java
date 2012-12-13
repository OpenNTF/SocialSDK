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
package com.ibm.sbt.jslibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.sbt.services.endpoints.EndpointFactory;


/**
 * SBT Environment
 * 
 * @author Philippe Riand
 */
public class SBTEnvironment {

	/**
	 * Push the context of an environment
	 */
	public static void push(Context context, String name) {
		SBTEnvironment env = SBTEnvironmentFactory.get(name);
		if(env!=null) {
			push(context,env);
		}
	}
	public static void push(Context context, SBTEnvironment env) {
		// Add the aliases as properties
		Endpoint[] endpoints = env.getEndpointsArray();
		if(endpoints!=null) {
			for(int i=0; i<endpoints.length; i++) {
				Endpoint e = endpoints[i];
				if(StringUtil.isNotEmpty(e.getAlias())) {
					String pName = EndpointFactory.SERVERPROP_PREFIX+e.getName();
					String pValue = e.getAlias();
					context.setProperty(pName,pValue);
				}
			}
		}
		
		// Add the properties
		Property[] props = env.getPropertiesArray();
		if(props!=null) {
			for(int i=0; i<props.length; i++) {
				Property p = props[i];
				context.setProperty(p.getName(), p.getValue());
			}
		}
	}
	

	/**
	 * Definition for an endpoint.
	 * @author priand
	 */
	public static class Endpoint {
		private String name;
		private String alias;
		public Endpoint() {
		}
		public Endpoint(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
	}

	/**
	 * Definition for a property.
	 * @author priand
	 */
	public static class Property {
		private String name;
		private String value;
		public Property() {
		}
		public Property(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	private String	name;
	private Endpoint[] endpoints;
	private Property[] properties;
	
	public SBTEnvironment() {
	}
	
	public SBTEnvironment(String name, Endpoint[] endpoints, Property[] properties) {
		this.name = name;
		this.endpoints = endpoints;
		this.properties = properties;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Endpoint[] getEndpointsArray() {
		return endpoints;
	}
	public void setEndpointsArray(Endpoint[] endpoints) {
		this.endpoints = endpoints;
	}
	public Property[] getPropertiesArray() {
		return properties;
	}
	public void setPropertiesArray(Property[] properties) {
		this.properties = properties;
	}
	public String getProperties() {
		try {
			return serializeProperties(properties);
		} catch(IOException ex) {
			Platform.getInstance().log(ex);
		}
		return "";
	}
	public void setProperties(String properties) {
		try {
			this.properties = parseProperties(properties);
		} catch(IOException ex) {
			Platform.getInstance().log(ex);
		}
	}
	public void setEndpoints(String endpoints) {
		this.endpoints = parseEndpoints(endpoints);
	}
	

	//
	// Utilities to access properties
	//
	public Property getPropertyByName(String name) {
		if(properties!=null) {
			for(int i=0; i<properties.length; i++) {
				Property p = properties[i];
				if(StringUtil.equals(p.getName(), name)) {
					return p;
				}
			}
		}
		return null;
	}
	public String getPropertyValueByName(String name) {
		Property p = getPropertyByName(name);
		return p!=null ? p.getValue() : null;
	}

	
	//
	// Utilities to parse comma delimited strings
	//
	
	/**
	 * Parse a list of endpoints from a comma delimited string and create an array if non empty.
	 * @param sEndpoints
	 * @return
	 * @throws IOException
	 */
	public static Endpoint[] parseEndpoints(String sEndpoints) {
		if(StringUtil.isNotEmpty(sEndpoints)) {
			String[] a = StringUtil.splitString(sEndpoints, ',');
			if(a.length>0) {
				Endpoint[] endpoints = new Endpoint[a.length];
				for(int i=0; i<a.length; i++) {
					Endpoint ep = endpoints[i] = new Endpoint();
					// name[:alias]
					String s = a[i];
					int pos = s.indexOf(':');
					if(pos>=0) {
						ep.setName(s.substring(0,pos).trim());
						ep.setAlias(s.substring(pos+1).trim());
					} else {
						ep.setName(s);
					}
				}
				return endpoints;
			}
		}
		return null;
	}
		
	//
	// Utilities to read a list of strings as rows
	// These are intended to be used by inheriting classes 
	//
	
	/**
	 * Read a list of endpoints from a string and create an array if non empty.
	 * @param sEndpoints
	 * @return
	 * @throws IOException
	 */
	public static Endpoint[] readEndpoints(String sEndpoints) throws IOException {
		if(StringUtil.isNotEmpty(sEndpoints)) {
			String[] a = splitRows(sEndpoints);
			if(a.length>0) {
				Endpoint[] endpoints = new Endpoint[a.length];
				for(int i=0; i<a.length; i++) {
					Endpoint ep = endpoints[i] = new Endpoint();
					// name[:alias]
					String s = a[i];
					int pos = s.indexOf(':');
					if(pos>=0) {
						ep.setName(s.substring(0,pos).trim());
						ep.setAlias(s.substring(pos+1).trim());
					} else {
						ep.setName(s);
					}
				}
				return endpoints;
			}
		}
		return null;
	}
	
	/**
	 * Read a list of properties from a string and create an array if non empty.
	 * @param sEndpoints
	 * @return
	 * @throws IOException
	 */
	public static Property[] parseProperties(String sProperty) throws IOException {
		if(StringUtil.isNotEmpty(sProperty)) {
			Properties props = new Properties();
			StringReader r = new StringReader(sProperty);
			props.load(new ReaderInputStream(r));
			List<Property> properties = new ArrayList<Property>();
			for(Entry<Object, Object> e: props.entrySet()) {
				Property p = new Property((String)e.getKey(),(String)e.getValue());
				properties.add(p);
			}
			return (Property[])properties.toArray(new Property[properties.size()]);
		}
		return null;
	}
	public static String serializeProperties(Property[] property) throws IOException {
		if(property!=null) {
			Properties props = new Properties();
			for(int i=0; i<property.length; i++) {
				Property p = property[i];
				if(StringUtil.isNotEmpty(p.getName()) && StringUtil.isNotEmpty(p.getValue())) {
					props.put(p.getName(),p.getValue());
				}
			}
			StringWriter writer = new StringWriter();
			props.list(new PrintWriter(writer));
			return writer.getBuffer().toString();
		}
		return null;
	}
	
	public static String[] splitRows(String s) throws IOException {
		BufferedReader r = new BufferedReader(new StringReader(s));
		try {
			List<String> result = new ArrayList<String>();
			for(String line=r.readLine(); line!=null; line=r.readLine()) {
				String l = line.trim();
				if(StringUtil.isNotEmpty(l)) {
					result.add(l);
				}
			}
			return result.toArray(new String[result.size()]); 
		} finally {
			r.close();
		}
	}
 
}