package com.ibm.sbt.opensocial.domino.modules;

import java.util.Iterator;
import java.util.Properties;

import org.apache.shindig.common.PropertiesModule;

/**
 * Provides properties to Shindig.
 * This module will read the default properties from Shindig and then override them with ones specified
 * in our properties file.
 *
 */
//TODO Should probably use/override the similar class from the OpenSocial Explorer
public class DominoPropertiesModule extends PropertiesModule {
	private static final String DEFAULT_PROPERTIES = "config/domino-shindig.properties";
	  private Properties properties;

	  public DominoPropertiesModule() {
	    super((Properties) null);
	    Properties shindigProperties = readPropertyFile(getDefaultPropertiesPath());
	    Properties playgroundProperties = readPropertyFile(DEFAULT_PROPERTIES);
	    
	    // Merge the properties together.  Playground properties take precedence
	    this.properties = mergeProperties(shindigProperties, playgroundProperties);
	  }

	  @Override
	  protected Properties getProperties() {
	    return this.properties;
	  }
	  
	  protected Properties mergeProperties(Properties...propertiesArray) {
	    if (propertiesArray.length == 0) {
	      return null;
	    }
	    
	    if (propertiesArray.length == 1) {
	      return propertiesArray[0];
	    }
	    
	    Properties merged = new Properties();
	    Iterator<String> keyItr;
	    String key;
	    for (Properties p : propertiesArray) {
	      keyItr = p.stringPropertyNames().iterator();
	      while (keyItr.hasNext()) {
	        key = keyItr.next();
	        merged.setProperty(key, p.getProperty(key));
	      }
	    }
	    return merged;
	  }
}