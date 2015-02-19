/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.provisioning.sample.app.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class represents a custom formatter for log statements.
 * Its <code>format</code> method has been overriden in order to produce a string representing :<br>
 * - the timestamp of the log statement formatted using the pattern "dd MM yyyy HH:mm:ss SSS" ;<br>
 * - the current thread name enclosed in square bracket ;<br>
 * - the logger name enclosed in square bracket ;<br>
 * - the log message.<br>
 * In order to improve readability, for each log statement, in case the corresponding logger is associated with 
 * one of the classes of the package "com.ibm.sbt.provisioning.sample.app", the logger name will be equal to 
 * the class name ( not the fully qualified class name ).
 * */
public class CustomFormatter extends Formatter{

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss SSS");
	
	@Override
	public String format(LogRecord record) {
		StringBuffer buf = new StringBuffer(1000);
		String loggerName = record.getLoggerName() ;
		if(record.getLoggerName().startsWith("com.ibm.sbt.provisioning.sample.app")){
			loggerName = record.getLoggerName().substring(record.getLoggerName().lastIndexOf(".") + 1) ;
		}
		buf.append(
				calcDate(record.getMillis()) + " " + record.getLevel().getName() +
				" ["+Thread.currentThread().getName()+"] "
				+ "["+ loggerName +"] ");
		buf.append(formatMessage(record)+"\n");
		return buf.toString();
	}

	private String calcDate(long millisecs) {
		Date resultdate = new Date(millisecs);
		return sdf.format(resultdate);
	} 
	
}
