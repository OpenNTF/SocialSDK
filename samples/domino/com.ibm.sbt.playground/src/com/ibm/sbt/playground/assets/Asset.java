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
package com.ibm.sbt.playground.assets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;



/**
 * Description of an asset.
 */
public class Asset {

	private String unid;
	private Properties properties; 
	
	public Asset() {
	}
	
	public Properties getProperties() {
		return properties;
	}
	public String getPropertiesAsString() {
		if(properties!=null) {
			try {
				StringWriter sw = new StringWriter();
				properties.store(sw, null);
				return sw.toString();
			} catch(IOException ex) {
				// just log and return null
				Platform.getInstance().log(ex);
			}
		}
		return null;
	}
	
	public String getProperty(String name) {
		if(properties!=null) {
			return properties.getProperty(name);
		}
		return null;
	}
	
	public String[] getPropertyArray(String name) {
		return getPropertyArray(name,',');
	}
	public String[] getPropertyArray(String name, char c) {
		String value = getProperty(name);
		if(value!=null) {
			return StringUtil.splitString(value,c,true);
		}
		return null;
	}
	
	public boolean setProperty(String name, String value) {
		if(properties!=null) {
			properties.put(name,value);
			return true;
		}
		return false;
	}	
	public boolean setPropertyArray(String name, String[] values) {
		return setPropertyArray(name,values,',');
	}
	public boolean setPropertyArray(String name, String[] values, char c) {
		if(values!=null) {
			return setProperty(name,StringUtil.concatStrings(values,c,true));
		}
		return setProperty(name,null);
	}
	
	public void init(Properties props) {
		this.properties = props;
	}

	public String getUnid() {
		return unid;
	}
	public void setUnid(String unid) {
		this.unid = unid;
	}

	public String getDescription() {
		return getProperty("description");
	}
	public void setDescription(String description) {
		setProperty("description",description);
	}

	public String[] getTags() {
		return getPropertyArray("tags");
	}
	public void setTags(String[] tags) {
		setPropertyArray("tags",tags);
	}

	public String[] getDocumentation() {
		return getPropertyArray("documentation");
	}
	public void setDocumentation(String[] documentation) {
		setPropertyArray("documentation",documentation);
	}

	public String[] getLabels() {
		return getPropertyArray("labels");
	}
	public void setLabels(String[] labels) {
		setPropertyArray("labels",labels);
	}
}
