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

import nsf.playground.environments.PlaygroundEnvironment;



/**
 * Endpoints used by the Playground 
 */
public abstract class Endpoints {
	
	public static class Category {
		private String name; 
		private Property[] properties; 
		public Category(String name, Property[] properties) {
			this.name = name;
			this.properties = properties;
		}
		public String getName() {
			return name;
		}
		public Property[] getProperties() {
			return properties;
		}
	}
	public static class Property {
		private String name; 
		private String label; 
		public Property(String name, String label) {
			this.name = name;
			this.label = label;
		}
		public String getName() {
			return name;
		}
		public String getLabel() {
			return label;
		}
	}

    public Endpoints() {
	}
    
	public void prepareEndpoints(PlaygroundEnvironment env) {
	}

	public Category[] getPropertyList() {
		return null;
	}
}
