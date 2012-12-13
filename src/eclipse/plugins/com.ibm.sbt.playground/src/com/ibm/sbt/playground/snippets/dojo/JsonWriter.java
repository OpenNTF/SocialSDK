/*
 * © Copyright IBM Corp. 2010
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

package com.ibm.sbt.playground.snippets.dojo;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.ibm.commons.util.AbstractIOException;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;



/**
 * Specialized JSON writer.
 * 
 * @author Philippe Riand
 */
public class JsonWriter extends JsonGenerator.WriterGenerator {
    
    private int objectLevels = 0;
    private boolean first[] = new boolean[32]; // max 32 for now...
    
    public JsonWriter(Writer writer, boolean compact) {
        super(JsonJavaFactory.instance,writer,compact);
    }
    
    public void startObject() throws IOException {
        nl();
        indent();
        out('{');
        first[++objectLevels]=true;
        incIndent();
    }
    public void endObject() throws IOException {
        nl();
        decIndent();
        indent();
        out('}');
        first[--objectLevels]=false;
    }
    public void startArray() throws IOException {
        nl();
        indent();
        out('[');
        first[++objectLevels]=true;
        incIndent();
    }
    public void endArray() throws IOException {
        nl();
        decIndent();
        indent();
        out(']');
        first[--objectLevels]=false;
    }
    public void startArrayItem() throws IOException {
        if(!first[objectLevels]) {
            out(',');
        }
    }
    public void endArrayItem() throws IOException {
        first[objectLevels]=false;
    }
    public void startProperty(String propertyName) throws IOException {
        if(!first[objectLevels]) {
            out(',');
        } else {
            first[objectLevels]=false;
        }
        nl();
        incIndent();
        indent();
        outPropertyName(propertyName);
        out(':');
    }
    public void endProperty() throws IOException {
        decIndent();
    }

    // Should be moved to the core libs
    @Override
    public void outNumberLiteral(double d) throws IOException {
        long l = (long)d;
        if((double)l==d) {
            String s = Long.toString(l);
            out(s);
        } else {
            String s = Double.toString(d);
            out(s);
        }
    }
    
    // Should be moved to the core libs
    public void outDateLiteral(Date value) throws IOException {
        String s = dateToString(value, true);
        outStringLiteral(s);
    }

    //Time Format
    public static final String TIME_FORMAT_C = "yyyy-MM-dd'T'HH:mm:ssZ"; //$NON-NLS-1$
    public static final String TIME_FORMAT_DO = "yyyy-MM-dd"; //$NON-NLS-1$
    public static final String TIME_FORMAT_TO = "HH:mm:ss"; //$NON-NLS-1$
    public static final String TIME_FORMAT_D = "yyyy-MM-dd'T'HH:mm:ss'Z'"; //$NON-NLS-1$
    public static final String TIME_ZONE_UTC = "UTC"; //$NON-NLS-1$

	public static SimpleDateFormat ISO8601_UTC = null;
	public static SimpleDateFormat ISO8601_DT = new SimpleDateFormat(TIME_FORMAT_C); // $NON-NLS-1$
    
    /**
     * Converts a date to an ISO8601 string.
     * 
     * @param value The date.
     * @param utc   If <code>true</code>, format the time in UTC.  If <code>false</code>,
     *              format the time in the local time zone.
     * @return      The ISO8601 string.
     * @throws IOException
     */
    private String dateToString(Date value, boolean utc) throws IOException {
        String result = null;
        
        if ( utc ) {
            if ( ISO8601_UTC == null ) {
                // Initialize the UTC formatter once
                TimeZone tz = TimeZone.getTimeZone(TIME_ZONE_UTC); 
                ISO8601_UTC = new SimpleDateFormat(TIME_FORMAT_D); 
                ISO8601_UTC.setTimeZone(tz);        
            }
            
            result = ISO8601_UTC.format((Date)value);
        }
        else {
            result = ISO8601_DT.format((Date)value);
        }
        
        return result;
    }
}   