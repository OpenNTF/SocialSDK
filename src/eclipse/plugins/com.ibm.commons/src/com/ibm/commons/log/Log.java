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




package com.ibm.commons.log;

import com.ibm.commons.Platform;
import com.ibm.commons.log.jdk.JdkLogMgrFactory;
import com.ibm.commons.util.StringUtil;


/**
 * Base class for all loggers.
 * 
 * <p>Used to define the logging groups. It is a very good practice to define all
 * the groups in the same class so they get initialized when this class is loaded.
 * This ensures that the Java registry is loaded and then can be queried by
 * runtime code.</p>
 * 
 * @ibm-api
 */
public class Log {
    private static JdkLogMgrFactory _logFactory = null;

    private static String developerName = null;
    
    /**
     * Get the name of the current developer.
     * This is used to enable developer specific trace. 
     * @return
     * @ibm-not-published
     */
    public static String getDeveloperName() {
        if(developerName==null) {
            try {
                developerName = Platform.getInstance().getProperty("ibm.developername"); // $NON-NLS-1$
            } catch(Throwable t) {
                CommonsLogger.STANDARD.error(t, "Error while reading the developer name"); // $NLE-Log.Errorwhilereadingthedevelopername-1$
            }
            if(developerName==null) {
                developerName = "anonymous"; // $NON-NLS-1$
            }
        }
        return developerName;
    }
        
    private static LogMgr load(String loggerName, Class<?> loggerClass, String description) {
        try {
            if (_logFactory == null) {
                _logFactory = new JdkLogMgrFactory();
            }
            LogMgr logger = _logFactory.getLogMgr(loggerName, description);
            if (logger == null) {
                throw new NullPointerException(loggerName
                        + ": Failed to get log (null)"); //$NON-NLS-1$
            }
            return logger;
        } catch(Throwable t) {
            // allow this as it indicates some fatal logging error
            System.err.println("Logging error: "+t.getLocalizedMessage()); //$NON-NLS-1$
            t.printStackTrace();
        }
        // Gently fails to an empty LogMgr
        return new EmptyLogMgr();
    }
    @Deprecated
    public static LogMgr load(String loggerName, Class<?> loggerClass) {
        return load(loggerName,loggerClass,null);
    }

    /**
     * Define a new log group.
     * <p>This function is supposed to be called in static constructors.</p>
     * @param loggerName the name of the logger
     * @return
	 * @ibm-api     
	 */
    public static LogMgr load(String loggerName) {
        return load(loggerName,null,null);
    }

    /**
     * Define a new log group.
     * <p>This function is supposed to be called in static constructors. The description
     * is an extension to the standard loggers that can be queried by runtime tools.</p>
     * @param loggerName the name of the logger
     * @param description the group description
     * @return
	 * @ibm-api     
	 */
    public static LogMgr load(String loggerName, String description) {
        return load(loggerName,null,description);
    }

    /**
     * Define a new log group.
     * <p>This function is supposed to be called in static constructors. The description
     * is an extension to the standard loggers that can be queried by runtime tools.</p>
     * @param loggerName the name of the logger
     * @param description the group description
     * @return
	 * @ibm-not-published     
	 */
    public static LogMgr loadDev(String userName) {
        boolean enabled = StringUtil.equals(userName, getDeveloperName());
        if(!enabled) {
            return new EmptyLogMgr();
        }
        
        // Create a new logger
        LogMgr mgr = load("com.ibm.commons.devlog", "Development Logger for "+userName); // $NON-NLS-1$ $NLI-Log.DevelopmentLoggerfor-2$
        mgr.setLogLevel(LogMgr.LOG_TRACEDEBUG_LEVEL);
        return mgr;
    }
    
    /** 
     * Load a logger for the specified class.  The package is determined from the
     * class and the package name is used to locate the correct LogMgr.
     * @param theClass the class that is locating the logger
     * @return LogMgr the logger to use
     * @ibm-not-published
     * @deprecated
     */
    @Deprecated
    static public LogMgr get(Class theClass) {
        return get(getPackageName(theClass));
    }

     /** 
     * Load a logger with a logger name.
     * @param loggerName the name of the logger
     * @return LogMgr the logger to use
     * @ibm-not-published
     * @deprecated
     */
    @Deprecated
    static private LogMgr get(String loggerName) {
        try {
            return ((LogMgrFactory)Platform.getInstance().getLogMgrFactory()).getLogMgr(loggerName,null);
        } catch (LogException e) {
            //Add logging
            e.printStackTrace();
        }
        
        // PHIL: 
        return null; 
    }

    /**
     * Determine the package name for a class
     * @param theClass the class to find the package name for
     * @return String the package name
     * @ibm-not-published
     * @deprecated
     */
    @Deprecated
    static private String getPackageName(Class theClass) {
        Package pack = theClass.getPackage();
        String packageName;
        if (pack == null) {
            String className = theClass.getName();
            int index = className.lastIndexOf('.');
            if (index > 0) {
                packageName = className.substring(0, index);
            }
            else {
                packageName = "";
            }
        }
        else {
            packageName = theClass.getPackage().getName();
        }
        return packageName;
    }
}