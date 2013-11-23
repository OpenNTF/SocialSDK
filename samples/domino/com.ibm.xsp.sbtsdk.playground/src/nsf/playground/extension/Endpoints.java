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

package nsf.playground.extension;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.extension.PlaygroundExtensionFactory;
import com.ibm.xsp.context.FacesContextEx;



/**
 * Endpoints used by the Playground 
 */
public abstract class Endpoints {
	
	public static Categories categories = new Categories();
	
	public static class Categories {
		private List<Category> categories;
		public Categories() {
			this.categories = new ArrayList<Endpoints.Category>();
			List<Endpoints> envext = PlaygroundExtensionFactory.getExtensions(Endpoints.class); // Get the categories for all the platforms
			for(int i=0; i<envext.size(); i++) {
				Category[] cats = envext.get(i).getPropertyList();
				if(cats!=null) {
					for(int j=0; j<cats.length; j++) {
						categories.add(cats[j]);
					}
				}
			}
		}
		public List<Category> getCategories() {
			return categories;
		}
		public String[] getCategoryNames() {
			String[] s = new String[categories.size()];
			for(int i=0; i<categories.size(); i++) {
				s[i] = categories.get(i).toString();
			}
			return s;
		}
		public Category findCategory(String catName) {
			if(StringUtil.isNotEmpty(catName)) {
				for(int i=0; i<categories.size(); i++) {
					if(StringUtil.equals(categories.get(i).getLabel(), catName)) {
						return categories.get(i);
					}
				}
			}
			return null;
		}
		public List<Property> getAllProperties() {
			List<Property> allProps = new ArrayList<Endpoints.Property>();
			for(int i=0; i<categories.size(); i++) {
				Property[] props = categories.get(i).properties;
				if(props!=null) {
					for(int j=0; j<props.length; j++) {
						allProps.add(props[j]);
					}
				}
			}
			return allProps;
		}
	}
	public static class Category {
		private String platform; 
		private String label; 
		private String tabLabel; 
		private Property[] properties; 
		private Group[] groups;
		private String runtimeProperties;
		private PropertyValues[] propertyValues; 
		public Category(String platform, String label, String tabLabel, Property[] properties, Group[] groups, String runtimeProperties) {
			this(platform, label, tabLabel, properties, groups, runtimeProperties, null);
		}
		public Category(String platform, String label, String tabLabel, Property[] properties, Group[] groups, String runtimeProperties, PropertyValues[] propertyValues) {
			this.platform = platform;
			this.label = label;
			this.tabLabel = tabLabel;
			this.properties = properties;
			this.groups = groups;
			this.runtimeProperties = runtimeProperties;
			this.propertyValues = propertyValues;
		}
		public String toString() {
			return label;
		}
		public String getRuntimePlatform() {
			return platform;
		}
		public String getLabel() {
			return label;
		}
		public String getTabLabel() {
			return tabLabel;
		}
		public Group[] getGroups() {
			return groups;
		}
		public String[] getGroupNames() {
			String[] s = new String[groups.length];
			for(int i=0; i<groups.length; i++) {
				s[i] = groups[i].toString();
			}
			return s;
		}
		public Group findGroup(String groupName) {
			for(int i=0; i<groups.length; i++) {
				if(StringUtil.equals(groups[i].getLabel(), groupName)) {
					return groups[i];
				}
			}
			return null;
		}
		public Property[] getProperties() {
			return properties;
		}
		public String[] getPropertyNames() {
			String[] s = new String[properties.length];
			for(int i=0; i<properties.length; i++) {
				s[i] = properties[i].toString();
			}
			return s;
		}
		public Property findProperty(String propName) {
			for(int i=0; i<properties.length; i++) {
				if(StringUtil.equals(properties[i].getName(), propName)) {
					return properties[i];
				}
			}
			return null;
		}
		public String getRuntimeProperties() {
			return runtimeProperties;
		}
		public PropertyValues[] getPropertyValues() {
			return propertyValues;
		}
		public PropertyValues getPropertyValues(String name) {
			if(propertyValues!=null) {
				for(int i=0; i<propertyValues.length; i++) {
					PropertyValues p = propertyValues[i];
					if(StringUtil.equals(p.getName(), name)) {
						return p;
					}
				}
			}
			return null;
		}
	}
	public static class Property {
		private String name; 
		private String label; 
		private String defaultValue; 
		public Property(String name, String label) {
			this(name, label, null);
		}
		public Property(String name, String label, String defaultValue) {
			this.name = name;
			this.label = label;
			this.defaultValue = defaultValue;
		}
		public String toString() {
			return label;
		}
		public String getName() {
			return name;
		}
		public String getLabel() {
			return label;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
	}
	public static class PropertyValues {
		private String name; 
		private String[] values;
		public PropertyValues(String name, String[] values) {
			this.name = name;
			this.values = values;
		}
		public String toString() {
			return name;
		}
		public String getName() {
			return name;
		}
		public String[] getValues() {
			return values;
		}
	}
	public static class Group {
		private String label; 
		private String[] properties; 
		private String runtimeProperties;
		private int helpType;
		public Group(String label, String[] properties, String runtimeProperties) {
			this(label, properties, runtimeProperties, 0);
		}
		public Group(String label, String[] properties, String runtimeProperties, int helpType) {
			this.label = label;
			this.properties = properties;
			this.runtimeProperties = runtimeProperties;
			this.helpType = helpType;
		}
		public String toString() {
			return label;
		}
		public String getLabel() {
			return label;
		}
		public String[] getProperties() {
			return properties;
		}
		public boolean hasProperty(String name) {
			if(properties!=null) {
				for(int i=0; i<properties.length; i++) {
					if(StringUtil.equals(properties[i], name)) {
						return true;
					}
				}
			}
			return false;
		}
		public String getRuntimeProperties() {
			return runtimeProperties;
		}
		public String getHelpText() {
			switch(helpType) {
				case 1: {
					return getHelpTextOAuth2();
				}
			}
			return null;
		}
		public String getHelpTextOAuth2() {
			HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String s = UrlUtil.getRequestUrl(req,UrlUtil.URL_CONTEXTPATH);
			return StringUtil.format("OAuth 2 callback: {0}",PathUtil.concat(s, "xsp/.sbtservice/oauth20_cb", '/'));
		}
	}

    public Endpoints() {
	}
    
	public String getEndpointNames() {
		return null;
	}
    
	public void prepareEndpoints(PlaygroundEnvironment env) {
	}

	public Category[] getPropertyList() {
		return null;
	}
	
	// Helpers
    protected void pushProperty(FacesContextEx ctx, PlaygroundEnvironment env, String name) {
    	String p = env.getPropertyValueByName(name);
    	if(StringUtil.isNotEmpty(p)) {
//            RequestParameters rq = ctx.getRequestParameters();
//    		rq.setProperty(name, p);
    		ctx.setSessionProperty(name, p);
    	} else {
    		ctx.setSessionProperty(name, null);
    	}
    }
	
}
