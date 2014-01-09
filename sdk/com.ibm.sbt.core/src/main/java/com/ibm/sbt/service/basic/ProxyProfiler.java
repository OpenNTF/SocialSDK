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
package com.ibm.sbt.service.basic;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Basic profiler, currently tailored for logging the proxy requests.
 * 
 * @author Lorenzo Boccaccia
 * 
 */
public class ProxyProfiler {

	private static final Level LOG_LEVEL = Level.INFO;
	private final static Logger statLogger = Logger.getLogger("ProxyProfiler");


	public ProxyProfiler() {
	}

	/**
	 * get a timed object or null if profiler is not enabled.
	 * @return a timed object
	 */
	static public Object getTimedObject() {
		if (enabled()) {
			return System.nanoTime();
		}
		return null;
	}

	/**
	 * Use this to avoid constructing costly log message when profiler not in use
	 * @return
	 */
	static public boolean enabled() {
		return statLogger.isLoggable(LOG_LEVEL);
	}

	/**
	 * if the profiler is enabled it measures the time difference from the given timed object
	 * and prints it in the log with the given message
	 */
	static public void profileTimedRequest(Object timedObject, String message) {
		

		if (enabled() && timedObject != null) {
			long st = System.nanoTime() - (Long) timedObject;
			statLogger.log(LOG_LEVEL,  message + " performed in "
					+ TimeUnit.MILLISECONDS.convert(st, TimeUnit.NANOSECONDS)
					+ "ms");
		}
	}

}
