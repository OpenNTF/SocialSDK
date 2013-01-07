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

package com.ibm.commons.runtime.mime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

/**
 * Definition of the MIME types.
 */
public class MIME {

	/**
	 * Get the extension of the file.
	 * <p>
	 * returns the part after the last dot.
	 * </p> 
	 * @param path
	 * @return
	 * @ibm-api
	 */
	public static String getFileExtension( String path ) {
        int pos = path.lastIndexOf('.'); //$NON-NLS-1$
        if( pos>=0 ) {
            return path.substring(pos+1);
        }
        return ""; //$NON-NLS-1$
    }

	/**
	 * Get the MIME type from an extension.
	 * <p>
	 * returns the MIME type.
	 * </p> 
	 * @param path
	 * @return
	 * @ibm-api
	 */
	public static String getMIMETypeFromExtension( String ext ) {
		//KNAA7VW9YW
		//map will return incorrect mime type if extension is not lower case
		if(StringUtil.isNotEmpty(ext)){
			String mime= (String)map.get(ext.toLowerCase());
			return mime!=null?mime:""; //$NON-NLS-1$
		}
		 
		return ""; //$NON-NLS-1$
    }

    private static final HashMap map = new HashMap();
    static {
    	init();
    }
    
    private static void init() {
        try {
            Reader r = new InputStreamReader(MIME.class.getResourceAsStream("mimeextensions.cnf")); //$NON-NLS-1$
            try {
                loadMime(r);
            } finally {
                r.close();
            }
        } catch( Exception e ) {
            Platform.getInstance().log(e);
        }
    }
    private static void loadMime(Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);
        do {
            String s = br.readLine();
            if( s==null ) {
                break;
            }
            // Remove the comment
            int pos = s.indexOf('#'); //$NON-NLS-1$
            if( pos>=0 ) {
                s = s.substring(0,pos);
            }
            // Do not take care of blank lines
            s = s.trim();
            if( s.length()==0 ) {
                continue;
            }
            // Get the extension/mime type
            int comma = s.indexOf(','); //$NON-NLS-1$
            if( comma>0 ) {
                String ext = s.substring(0,comma).trim();
                String mimeType = s.substring(comma+1).trim();
                // And fill the table
                if( ext.charAt(0)=='.' ) {
                    ext = ext.substring(1);
                }
                if( ext.length()>0 && mimeType.length()>0 ) {
                    map.put( ext, mimeType );
                }
            }
        } while(true);
    }
    
    /**
     * Find the best extension foe a MIME type.
     * <p>
     * Returns appropriate filename extension based on mime type. Returns empty
     * string "" if mimetype not resolved.
     * </p>
     * @param str
     * @param mimetype
     * @return
     * @ibm-api
     */
    public static String getExtensionFromMIMEType(String mimetype){
    	
    	Iterator i = map.entrySet().iterator();
    	while (i.hasNext()) {
    		Map.Entry entry = (Map.Entry)i.next();
    		if(mimetype.equalsIgnoreCase((String)entry.getValue())){
    			return "." + (String)entry.getKey(); //$NON-NLS-1$
    		}
    	}
    	return ""; //$NON-NLS-1$
    }
}
