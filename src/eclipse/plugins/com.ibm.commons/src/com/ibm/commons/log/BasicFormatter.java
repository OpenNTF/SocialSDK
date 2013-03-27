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
package com.ibm.commons.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * Basic Log Fomatter.
 * This formatter is simply emitting the string as is. As our API formats the string 
 * before creating the log record, we don't to reformat it here. 
 * @ibm-not-published
 */
public class BasicFormatter extends Formatter {
    
	private boolean dateTime;
	
	public BasicFormatter(boolean dateTime) {
		this.dateTime = dateTime;
	}
	
    private static final DateFormat format = new SimpleDateFormat("h:mm:ss"); // $NON-NLS-1$
    private static final String lineSep = System.getProperty("line.separator"); // $NON-NLS-1$

    public String format(LogRecord record) {
    	if(dateTime) {
	        StringBuilder output = new StringBuilder()
	            .append("[")
	            .append(record.getLevel()).append('|')
	            .append(format.format(new Date(record.getMillis())))
	            .append("]: ")
	            .append(record.getMessage())
	            .append(' ')
	            .append(lineSep);
	        return output.toString();
    	} else {
	        StringBuilder output = new StringBuilder()
            	.append(record.getMessage())
            	.append(lineSep);
	        return output.toString();
    	}
    }
}