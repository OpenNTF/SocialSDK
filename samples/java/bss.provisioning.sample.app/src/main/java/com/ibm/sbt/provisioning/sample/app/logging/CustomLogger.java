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

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a container for the logic regarding custom logging initialization.
 * */
public class CustomLogger {
	
	private static FileHandler fileHandler ;
	private static FileHandler orgApacheHandler ;
	private static CustomFormatter customFormatter ;
	private static CustomFilter customFilter ;
	private static Logger orgApache ;
	
	/**
	 * Custom logging initialization 
	 * <p>
	 * This method associates two different file handlers, both using the same formatter, 
	 * with the root logger and the "org.apache" logger in order to increase 
	 * the log readability.
	 * The root logger file handler will redirect the logger's output to a file named "bssProvisioning.log".
	 * The "org.apache" logger file handler will redirect the logger's output to a file named "bssProvisioning.log".
	 * Then a filter is associated with all the root logger's handlers in order to avoid to have the root logger output 
	 * polluted by the "org.apache" logger output. 
	 */
	public static boolean setup() throws IOException {
		boolean loggerFileCreated = false ; 
		// get the root logger to configure it
		Logger rootLogger = Logger.getLogger("");
		orgApache = Logger.getLogger("org.apache");
		rootLogger.setLevel(Level.FINEST);
		orgApache.setLevel(Level.FINEST);
		fileHandler = new FileHandler("bssProvisioning.log");
		orgApacheHandler = new FileHandler("orgApacheOutput.log");
		loggerFileCreated = true ;
		// create a TXT formatter
		customFormatter = new CustomFormatter();
		customFilter = new CustomFilter();
		
		fileHandler.setFormatter(customFormatter);
		orgApacheHandler.setFormatter(customFormatter);
		orgApacheHandler.setLevel(Level.FINEST);
		rootLogger.addHandler(fileHandler);
		orgApache.addHandler(orgApacheHandler);
		
		for( Handler handler : rootLogger.getHandlers() ){
			if (handler instanceof ConsoleHandler) {
				handler.setLevel(Level.FINEST);
				handler.setFormatter(customFormatter);
				handler.setFilter(customFilter);
			}
			if (handler instanceof FileHandler) {
				handler.setFilter(customFilter);
			}
		}
		return loggerFileCreated ;
	}
}
