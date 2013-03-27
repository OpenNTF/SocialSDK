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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.FastStringBuffer;
import com.ibm.commons.util.StringUtil;

/**
 * Preferences object.
 * A preference object is storing and retrieving preferences.
 * @ibm-not-published 
 */
public class Preferences {

    private HashMap<String, String> _properties;
    
    public Preferences() {
        this._properties = new HashMap<String, String>();
    }

    
    // ======================================================================
    // Getters
    // ======================================================================
    
    public String getString(String key) {
        String s = _properties.get(key);
        if(s==null) {
            s="";
        }
        return s;
    }
    
    public int getInteger(String key) {
        String s = _properties.get(key);
        if(StringUtil.isNotEmpty(s)) {
            try {
                return Integer.parseInt(s);
            } catch(NumberFormatException ex) {}
        }
        return 0;
    }
    
    public long getLong(String key) {
        String s = _properties.get(key);
        if(StringUtil.isNotEmpty(s)) {
            try {
                return Long.parseLong(s);
            } catch(NumberFormatException ex) {}
        }
        return 0;
    }
    
    public double getDouble(String key) {
        String s = _properties.get(key);
        if(StringUtil.isNotEmpty(s)) {
            try {
                return Double.parseDouble(s);
            } catch(NumberFormatException ex) {}
        }
        return 0.0;
    }
    
    public boolean getBoolean(String key) {
        String s = _properties.get(key);
        if(StringUtil.isNotEmpty(s)) {
            return s.equalsIgnoreCase("true"); // $NON-NLS-1$
        }
        return false;
    }

    
    // ======================================================================
    // Setters
    // ======================================================================

    public void putString(String key, String value) {
        checkKey(key);
        checkValue(value);
        if(StringUtil.isNotEmpty(value)) {
            _properties.put(key, value);
        } else {
            _properties.remove(key);
        }
    }

    public void putInteger(String key, int value) {
        checkKey(key);
        if(value!=0) {
            _properties.put(key, Integer.toString(value));
        } else {
            _properties.remove(key);
        }
    }

    public void putLong(String key, long value) {
        checkKey(key);
        if(value!=0) {
            _properties.put(key, Long.toString(value));
        } else {
            _properties.remove(key);
        }
    }

    public void putDouble(String key, double value) {
        checkKey(key);
        if(value!=0) {
            _properties.put(key, Double.toString(value));
        } else {
            _properties.remove(key);
        }
    }

    public void putBoolean(String key, boolean value) {
        checkKey(key);
        if(value) {
            _properties.put(key, "true"); // $NON-NLS-1$
        } else {
            _properties.remove(key);
        }
    }

    private void checkKey(String key) {
        if(StringUtil.isEmpty(key)) {
            throw new IllegalArgumentException("Empty key"); // $NLS-Preferences.Emptykey-1$
        }
        if(key.indexOf((char)0)>=0) {
            throw new IllegalArgumentException("Key cannot contain '\\0' character"); // $NLS-Preferences.Keycannotcontaina0character-1$
        }
        if(key.indexOf('=')>=0) {
            throw new IllegalArgumentException("Key cannot contain '=' character"); // $NLS-Preferences.Keycannotcontainancharacter-1$
        }
    }

    private void checkValue(String value) {
        if(StringUtil.isNotEmpty(value)) {
            if(value.indexOf((char)0)>=0) {
                throw new IllegalArgumentException("Value cannot contain '\\0' character"); // $NLS-Preferences.Valuecannotcontaina0character-1$
            }
        }
    }
    
    // ======================================================================
    // Initialization
    // ======================================================================

//  public Iterator keys() {
//      return _properties.keySet().iterator();
//  }

    public void remove(String key) {
        checkKey(key);
        _properties.remove(key);
    }

    public void clear() {
        _properties.clear();
    }
    
    public void reset(Preferences prefs) {
        _properties.clear();
        if(prefs!=null) {
            for( Map.Entry<String,String> e: prefs._properties.entrySet() ) {
                String key = e.getKey();
                String value = e.getValue();
                _properties.put(key, value);
            }
        }
    }

    
    // ======================================================================
    // Serialization
    // ======================================================================

    public String writeToString() {
        FastStringBuffer b = new FastStringBuffer(4096);
        for( Map.Entry<String,String> e: _properties.entrySet() ) {
            String key = e.getKey();
            String value = e.getValue();
            if(b.length()>0) {
                b.append((char)0);
            }
            b.append(key);
            b.append('=');
            b.append(value);
        }
        return b.toString();
    }

    public void readFromString(String s) {
        _properties.clear();
        int len = s.length();
        int pos = 0;
        while(pos<len) {
            int idx = s.indexOf('=',pos);
            if(idx>0 && idx<len-1) {
                int idx2 = s.indexOf((char)0,idx+1);
                if(idx2<0) {
                    idx2 = len;
                }
                String key = s.substring(pos,idx);
                String val = s.substring(idx+1,idx2);
                _properties.put(key,val);
                pos = idx2+1;
            } else {
                break;
            }
        }
    }
    
    public void dump(PrintStream out) {
        for( Map.Entry<String,String> e: _properties.entrySet() ) {
            String key = e.getKey();
            String value = e.getValue();
            out.print(key);
            out.print("=");
            out.println(value);
        }
    }
    
    public static void main(String[] args) {
        Preferences p = new Preferences();
        p.putString("s1", "value1"); // $NON-NLS-1$ $NON-NLS-2$
        p.putString("s1.s2", "value2=toto"); // $NON-NLS-1$ $NON-NLS-2$
        p.putString("s1.s2.s3", "value2\ntoto"); // $NON-NLS-1$ $NON-NLS-2$
        System.out.println("Initial:"); // $NON-NLS-1$
        p.dump(System.out);
        
        String s = p.writeToString();

        System.out.println("\nSerialization:"); // $NON-NLS-1$
        System.out.println(s);
        
        Preferences p2 = new Preferences();
        p2.readFromString(s);
        System.out.println("\nRead:"); // $NON-NLS-1$
        p2.dump(System.out);
    }
}
