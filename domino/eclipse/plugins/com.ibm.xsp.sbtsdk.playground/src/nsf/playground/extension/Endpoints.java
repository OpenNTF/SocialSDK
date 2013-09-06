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

import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.extension.PlaygroundExtensionFactory;
import com.ibm.xsp.context.FacesContextEx;



/**
 * Endpoints used by the Playground 
 */
public abstract class Endpoints {
	
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
		public int findCategory(String catName) {
			for(int i=0; i<categories.size(); i++) {
				if(StringUtil.equals(categories.get(i), catName)) {
					return i;
				}
			}
			return -1;
		}
	}
	public static class Category {
		private String platform; 
		private String label; 
		private String tabLabel; 
		private Property[] properties; 
		private Group[] groups;
		public Category(String platform, String label, String tabLabel, Property[] properties, Group[] groups) {
			this.platform = platform;
			this.label = label;
			this.tabLabel = tabLabel;
			this.properties = properties;
			this.groups = groups;
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
		public int findGroup(String groupName) {
			for(int i=0; i<groups.length; i++) {
				if(StringUtil.equals(groups[i], groupName)) {
					return i;
				}
			}
			return -1;
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
		public int findProperty(String propName) {
			for(int i=0; i<properties.length; i++) {
				if(StringUtil.equals(properties[i], propName)) {
					return i;
				}
			}
			return -1;
		}
	}
	public static class Property {
		private String name; 
		private String label; 
		private String[] valueList; 
		public Property(String name, String label) {
			this(name, label, null);
		}
		public Property(String name, String label, String[] valueList) {
			this.name = name;
			this.label = label;
			this.valueList = valueList;
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
		public String[] getValueList() {
			return valueList;
		}
	}
	public static class Group {
		private String label; 
		private String[] properties; 
		public Group(String label, String[] properties) {
			this.label = label;
			this.properties = properties;
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
