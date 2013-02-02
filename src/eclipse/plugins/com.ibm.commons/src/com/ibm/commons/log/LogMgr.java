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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Log Manager.
 * 
 * <p>This class is used to defines logger that then delegates to the JDK implementation.
 * Initially (pre JDK 1.4) they were designed to log to different APIs (LOG4J...). Now,
 * they also propose an easier to use API particularly through the use of String
 * formatter using parameters.</p>
 * 
 * @ibm-api
 */
public interface LogMgr {

    public static final int LOG_ERROR_LEVEL = Level.SEVERE.intValue();
    public static final int LOG_WARN_LEVEL = Level.WARNING.intValue();
    public static final int LOG_INFO_LEVEL = Level.INFO.intValue();

    public static final int LOG_TRACEDEBUG_LEVEL = Level.FINEST.intValue();
    public static final int LOG_TRACEEVENT_LEVEL = Level.FINE.intValue();
    public static final int LOG_TRACEENTRY_LEVEL = Level.FINER.intValue();
    public static final int LOG_TRACEEXIT_LEVEL = Level.FINER.intValue();
    public static final int LOG_TRACEENTRYEXIT_LEVEL = Level.FINER.intValue();
    
    /**
     * Get the logger description.
     * @ibm-api
     */
    public String getDescription();

    /** 
     * Returns whether traceDebug level is enables
     * @return boolean
     * @ibm-api
     */
    public boolean isTraceDebugEnabled();

    /** 
     * Returns whether traceEntryExit level is enables
     * @return boolean
     * @ibm-api
     */
    public boolean isTraceEntryExitEnabled();

    /** 
     * Returns whether traceEvent level is enables
     * @return boolean
     * @ibm-api
     */
    public boolean isTraceEventEnabled();

    /**
     * Returns whether info level is enabled.
     * @return boolean
     * @ibm-api
     */
    public boolean isInfoEnabled();

    /**
     * Returns whether warning level is enabled.
     * @return boolean
     * @ibm-api
     */
    public boolean isWarnEnabled();

    /**
     * Returns whether error level is enabled.
     * @return boolean
     * @ibm-api
     */
    public boolean isErrorEnabled();

    /**
     * Sets the Level for the Logger.
     * @param newLevel
     * @ibm-api
     */
    public void setLogLevel(int newLevel);
    

    /**
     * Get the underlying Java logger, if any.
     * @return
     * @ibm-api
     */
    public Logger getLogger();
    
    
    // ========================================================================
    //  New tracing methods
    // ========================================================================

    // Info functions
    /**
     * Log an information string.
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void info(String msg, Object...parameters);

    /**
     * Log an information string.
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void info(Throwable t, String msg, Object...parameters);

    /**
     * Log an information string.
     * @param clazz the object class
     * @param method the method name
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void infop(Object clazz, String method, String msg, Object...parameters);

    /**
     * Log an information string.
     * @param clazz the object class
     * @param method the method name
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void infop(Object clazz, String method, Throwable t, String msg, Object...parameters);
    
    // Warn functions
    /**
     * Log a warning string.
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void warn(String msg, Object...parameters);

    /**
     * Log a warning string.
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void warn(Throwable t, String msg, Object...parameters);

    /**
     * Log a warning string.
     * @param clazz the object class
     * @param method the method name
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void warnp(Object clazz, String method, String msg, Object...parameters);
    
    /**
     * Log a warning string.
     * @param clazz the object class
     * @param method the method name
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void warnp(Object clazz, String method, Throwable t, String msg, Object...parameters);
    
    // Error functions
    /**
     * Log an error string.
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void error(String msg, Object...parameters);

    /**
     * Log an error string.
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void error(Throwable t, String msg, Object...parameters);

    /**
     * Log an error string.
     * @param clazz the object class
     * @param method the method name
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void errorp(Object clazz, String method, String msg, Object...parameters);

    /**
     * Log an error string.
     * @param clazz the object class
     * @param method the method name
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void errorp(Object clazz, String method, Throwable t, String msg, Object...parameters);
    
    // Event functions (Level=FINE)
    /**
     * Trace an event (Level=FINE).
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceEvent(String msg, Object...parameters);
    /**
     * Trace an event (Level=FINE).
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceEvent(Throwable t, String msg, Object...parameters);
    /**
     * Trace an event (Level=FINE).
     * @param clazz the object class
     * @param method the method name
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceEventp(Object clazz, String method, String msg, Object...parameters);
    /**
     * Trace an event (Level=FINE).
     * @param clazz the object class
     * @param method the method name
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceEventp(Object clazz, String method, Throwable t, String msg, Object...parameters);
    
    // Debug functions (Level=FINEST)
    /**
     * Trace an event for debug (Level=FINEST).
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceDebug(String msg, Object...parameters);
    /**
     * Trace an event for debug (Level=FINEST).
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceDebug(Throwable t, String msg, Object...parameters);
    /**
     * Trace an event for debug (Level=FINEST).
     * @param clazz the object class
     * @param methodName the method name
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceDebugp(Object clazz, String methodName, String msg, Object...parameters);
    /**
     * Trace an event for debug (Level=FINEST).
     * @param clazz the object class
     * @param methodName the method name
     * @param t the exception to log
     * @param msg the message to log
     * @param parameters the parameters used to format the message
     * @ibm-api
     */
    public void traceDebugp(Object clazz, String methodName, Throwable t, String msg, Object...parameters);

    // Trace when a method is entered
    /**
     * Trace when a method is entered.
     * @param clazz the object class
     * @param methodName the method name
     * @ibm-api
     */
    public void traceEntry(Object clazz, String methodName);
    /**
     * Trace when a method is entered.
     * @param clazz the object class
     * @param methodName the method name
     * @param parameters the parameters passed to the method when invoked
     * @ibm-api
     */
    public void traceEntry(Object clazz, String methodName, Object... parameters);

    // Trace when a method is exited
    /**
     * Trace when a method is exited.
     * @param clazz the object class
     * @param methodName the method name
     * @ibm-api
     */
    public void traceExit(Object clazz, String methodName);
    /**
     * Trace when a method is exited.
     * @param clazz the object class
     * @param methodName the method name
     * @param Object the value being returned by the method
     * @ibm-api
     */
    public void traceExit(Object clazz, String methodName, Object ret);
}
