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

package com.ibm.commons.preferences;

import com.ibm.commons.IPlatformService;
import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

/**
 * Preferences service object.
 * @ibm-not-published 
 */
public abstract class PreferencesService implements IPlatformService {

    public static final String SERVICE_ID = "com.ibm.commons.services.preferences"; // $NON-NLS-1$
    
    public static PreferencesService getInstance() {
        return (PreferencesService)Platform.getInstance().getPlatformService(SERVICE_ID);
    }
    
    protected PreferencesService() {
    }

    public abstract String getString(String key);
    public abstract int getInteger(String key);
    public abstract long getLong(String key);
    public abstract double getDouble(String key);
    public abstract boolean getBoolean(String key);
    public Preferences getPreferences(String key) {
        Preferences p = new Preferences();
        String s = getString(key);
        if(StringUtil.isNotEmpty(s)) {
            p.readFromString(s);
        }
        return p;
    }

    public abstract void putString(String key, String value);
    public abstract void putInteger(String key, int value);
    public abstract void putLong(String key, long value);
    public abstract void putDouble(String key, double value);
    public void putPreferences(String key, Preferences value) {
        String s = value.writeToString();
        putString(key, s);
    }
}
