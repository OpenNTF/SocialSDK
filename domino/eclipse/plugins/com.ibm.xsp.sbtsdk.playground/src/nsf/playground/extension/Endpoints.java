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

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.RequestParameters;

import nsf.playground.environments.PlaygroundEnvironment;



/**
 * Endpoints used by the Playground 
 */
public abstract class Endpoints {
	
	public static class Category {
		private String label; 
		private Property[] properties; 
		public Category(String label, Property[] properties) {
			this.label = label;
			this.properties = properties;
		}
		public String getLabel() {
			return label;
		}
		public Property[] getProperties() {
			return properties;
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
