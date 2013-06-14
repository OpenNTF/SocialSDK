/* ***************************************************************** */
/*                                                                   */
/* IBM Confidential                                                  */
/*                                                                   */
/* OCO Source Materials                                              */
/*                                                                   */
/* Copyright IBM Corp. 2004, 2011                                    */
/*                                                                   */
/* The source code for this program is not published or otherwise    */
/* divested of its trade secrets, irrespective of what has been      */
/* deposited with the U.S. Copyright Office.                         */
/*                                                                   */
/* ***************************************************************** */

package nsf.playground.extension;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.FacesContextEx;

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
    		ctx.setSessionProperty(name, p);
    	}
    }
	
}
