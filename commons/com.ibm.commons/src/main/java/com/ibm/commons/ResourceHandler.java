/*
 * © Copyright IBM Corp. 2012-2013
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

/**
 * This class is originally generated via Designer
 */
package com.ibm.commons;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** Resource handler class **/
public class ResourceHandler {

    private static ResourceBundle _resourceBundle;
    private static ResourceBundle _loggingResourceBundle;
    

    /**
     * @return ResourceBundle
     */
    private static ResourceBundle getResourceBundle(String bundle) {
        try {
            String bundlePackage = buildResourcePath(bundle); //NON-NLS-1
            return ResourceBundle.getBundle( bundlePackage );
        }
        catch (MissingResourceException e) {
            // does nothing - this method will return null and
            // getString(String) will return the key
            // it was called with
        }
        return null;
    }
    
   /**
     * @param name
     * @return String
     */
    private static String buildResourcePath(String name) {
        String clName = ResourceHandler.class.getName();
        return clName.substring( 0, clName.lastIndexOf('.') + 1 ) + name; 
    }

    /**
     * @param key
     * @return String
     */
    public static String getString(String key) {
        if (_resourceBundle == null) {
            _resourceBundle = getResourceBundle("messages"); // $NON-NLS-1$
        }
      return getResourceBundleString(_resourceBundle, key);
        
    }
    
    
    /**
     * @param key
     * @return String
     */
    public static String getLoggingString(String key) {
        if (_loggingResourceBundle == null) {
            _loggingResourceBundle = getResourceBundle("logging"); // $NON-NLS-1$
        }
             return getResourceBundleString(_loggingResourceBundle, key);
            
    }
    
    public static String getResourceBundleString(ResourceBundle _bundle, String key){
       if (_bundle != null) {
            try {
                return _bundle.getString(key);
            }
            catch (MissingResourceException e) {
                return "!" + key + "!";//NON-NLS-2//NON-NLS-1
            }
        }
        else {
            return "!" + key + "!";//NON-NLS-2//NON-NLS-1
        }
    
    }
    
}
    
   
   
