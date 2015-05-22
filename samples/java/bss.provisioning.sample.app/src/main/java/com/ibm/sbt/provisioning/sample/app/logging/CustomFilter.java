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

import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * This class represents a custom filter for log statements.
 * Its <code>isLoggable</code> method has been overriden in order to return false 
 * in case the log statement is produced by one of the following logger :<br>
 * -"org.apache";<br>
 * -"com.ibm.sbt.service.proxy.SBTProxy";<br>
 * -"com.ibm.sbt.services.client.ClientService".<br>
 * */
public class CustomFilter implements Filter{
	@Override
	public boolean isLoggable(LogRecord record) {
		boolean isLoggable = true ;
		if(record.getLoggerName() == null || record.getLoggerName().startsWith("org.apache") ||
				record.getLoggerName().startsWith("com.ibm.sbt.service.proxy.SBTProxy") ||
				record.getLoggerName().startsWith("com.ibm.sbt.services.client.ClientService") ){
			isLoggable = false ;
		}
		return isLoggable ;
	}
}
