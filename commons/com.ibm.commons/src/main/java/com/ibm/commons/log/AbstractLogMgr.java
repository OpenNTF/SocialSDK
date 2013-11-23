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
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.ibm.commons.util.StringUtil;

/**
 * Base implementation of a log manager.
 * @ibm-not-published
 */
public abstract class AbstractLogMgr implements LogMgr {
	
	private LogMgrFactory factory;
	private String description;
	private int indent;
	
	public AbstractLogMgr(LogMgrFactory factory, String description) {
		this.factory = factory;
		this.description = description;
	}
	
	public LogMgrFactory getFactory() {
		return factory;
	}

	public String getDescription() {
		return description;
	}

    public void incIndent() {
    	indent++;
    }
    
    public void decIndent() {
    	indent--;
    }
    
    public int getIndentationLevel() {
    	return indent;
    }

    
    // ==========================================================
    // Abstract methods
    // ==========================================================

    // Default implementation using a JDK logger
    protected abstract LogRecord createLogRecord(Level level, String msg);
    
    protected void log(Throwable t, Level level, String msg, Object[] objs) {
        LogRecord lr = createLogRecord(level, msg);
        if(t != null)  {
            lr.setThrown(t);
        }
        if(objs != null)  {
        	//serializeCheck(objs);
            lr.setParameters(objs);
        }
        getLogger().log(lr);
    }
    protected void logp(Object clazz, String methodName, Throwable t, Level level, String msg, Object[] objs) {
        LogRecord lr = createLogRecord(level, msg);
        if(t != null)  {
            lr.setThrown(t);
        }
        if( clazz!=null ) {
        	lr.setSourceClassName(toClassName(clazz));
        }
        if( methodName!=null ) {
        	lr.setSourceMethodName(methodName);
        }
        if(objs != null)  {
        	//serializeCheck(objs);
            lr.setParameters(objs);
        }
        getLogger().log(lr);
    }
//    private Object[] serializeCheck(Object[] objs) {
//        if(objs==null) {
//            return null;
//        }
//        
//        Object[] ret = null;
//        
//        //check objects are serializable + convert to string if not
//        for(int i=0; i<objs.length; i++) {
//            if(objs[i]!=null) {
//                if(!(objs[i] instanceof Serializable)) {
//                    String msg = "Logging error. Object of type {0} does not implement {1}. Value: {2}";
//                    msg = StringUtil.format(msg, objs[i].getClass().getName(), "java.lang.Serializable", objs[i].toString()); //$NON-NLS-1$
//                    try {
//                        throw new NotSerializableException();
//                    } catch(NotSerializableException e) {
//                        getLogger().log(Level.WARNING, msg, e);
//                    }
//                    if(ret==null) {
//                        ret = new Object[objs.length];
//                    }
//                    ret[i] = objs[i].toString();
//                }
//            }
//        }
//        //end serializable check
//        if(ret==null) {
//            //ok
//            return objs;
//        }
//        for(int i=0; i<objs.length; i++) {
//            if(ret[i]==null) {
//                ret[i] = objs[i];
//            }
//        }
//        return ret;
//    }

    protected void logEntry(String className, String methodName, Object[] parameters) {
    	getLogger().entering(className, methodName, parameters);
    }
    protected void logExit(String className, String methodName, Object retValue) {
    	getLogger().exiting(className, methodName, retValue);
    }

    private String toClassName(Object clazz) {
    	if(clazz instanceof String) {
    		return clazz.toString();
    	} else if(clazz instanceof Class) {
    		return ((Class<?>)clazz).getName();
    	} else {
    		return clazz.getClass().getName();
    	}
    }
    
    
    
    public boolean isTraceDebugEnabled() {
        return(getLogger().isLoggable(Level.FINEST));
    }

    public boolean isTraceEntryExitEnabled() {
        return(getLogger().isLoggable(Level.FINER));
    }

    public boolean isTraceEntryEnabled() {
        return(getLogger().isLoggable(Level.FINER));
    }

    public boolean isTraceExitEnabled() {
        return(getLogger().isLoggable(Level.FINER));
    }

    public boolean isTraceEventEnabled() {
        return(getLogger().isLoggable(Level.FINE));
    }

    public boolean isInfoEnabled() {
        return(getLogger().isLoggable(Level.INFO));
    }

    public boolean isWarnEnabled() {
        return(getLogger().isLoggable(Level.WARNING));
    }

    public boolean isErrorEnabled() {
        return(getLogger().isLoggable(Level.SEVERE));
    }

    public boolean isFatalEnabled() {
        return(getLogger().isLoggable(Level.SEVERE));
    }

    public void setLogLevel(int newLevel)  {
        int currentLevel = 0;
        if(getLogger().getLevel() != null)  {
            currentLevel = getLogger().getLevel().intValue();
        }
        Level lLevel = null;
        if(newLevel == LOG_TRACEDEBUG_LEVEL)  {
            lLevel = Level.FINEST;
        } else if(newLevel == LOG_TRACEENTRY_LEVEL  ||  newLevel == LOG_TRACEEXIT_LEVEL  ||  newLevel == LOG_TRACEENTRYEXIT_LEVEL)  {
            lLevel = Level.FINER;
        } else if(newLevel == LOG_TRACEEVENT_LEVEL)  {
            lLevel = Level.FINE;
        } else if(newLevel == LOG_INFO_LEVEL)  {
            lLevel = Level.INFO;
        } else if(newLevel == LOG_WARN_LEVEL)  {
            lLevel = Level.WARNING;
        } else if(newLevel == LOG_ERROR_LEVEL)  {
            lLevel = Level.SEVERE;
        }

        if(lLevel != null  &&  lLevel.intValue() != currentLevel)  {
            resetLevels(getLogger(), lLevel);
        }
    }

    private void resetLevels(Logger loga, Level newLevel)  {
        loga.setLevel(newLevel);
    }

    
    // ==========================================================
    // Std implementation
    // ==========================================================

    // traceentry
    public void traceEntry(Object o, String methodName) {
    	if(isTraceEntryExitEnabled()) {
    		logEntry(o.getClass().getName(), methodName, null);
    	}
    }

    public void traceEntry(Object o, String methodName, Object... parameters) {
    	if(isTraceEntryExitEnabled()) {
        	logEntry(o.getClass().getName(), methodName, parameters);
    	}
    }
	
    public void traceEntry(Class c, String methodName) {
    	if(isTraceEntryExitEnabled()) {
        	logEntry(c.getName(), methodName, null);
    	}
    }

    public void traceEntry(Class c, String methodName, Object... parameters) {
    	if(isTraceEntryExitEnabled()) {
        	logEntry(c.getName(), methodName, parameters);
    	}
    }

    // traceExit
    public void traceExit(Object o, String methodName) {
    	if(isTraceEntryExitEnabled()) {
        	logExit(o.getClass().getName(), methodName, null);
    	}
    }
    
    public void traceExit(Object o, String methodName, Object ret) {
    	if(isTraceEntryExitEnabled()) {
        	logExit(o.getClass().getName(), methodName, ret);
    	}
    }

    public void traceExit(Class c, String methodName) {
    	if(isTraceEntryExitEnabled()) {
        	logExit(c.getName(), methodName, null);
    	}
    }
    
    public void traceExit(Class c, String methodName, Object ret) {
    	if(isTraceEntryExitEnabled()) {
        	logExit(c.getName(), methodName, ret);
    	}
    }

    
    // info
    public void info(String msg, Object...parameters) {
    	if(isInfoEnabled()) {
    		log(null,Level.INFO,StringUtil.format(msg, parameters),null);
    	}
    }
    public void info(Throwable t, String msg, Object...parameters) {
    	if(isInfoEnabled()) {
        	log(t,Level.INFO,StringUtil.format(msg, parameters),null);
    	}
    }
    public void infop(Object clazz, String methodName, String msg, Object...parameters) {
    	if(isInfoEnabled()) {
    		logp(clazz,methodName,null,Level.INFO,StringUtil.format(msg, parameters),null);
    	}
    }
    public void infop(Object clazz, String methodName, Throwable t, String msg, Object...parameters) {
    	if(isInfoEnabled()) {
        	logp(clazz,methodName,t,Level.INFO,StringUtil.format(msg, parameters),null);
    	}
    }
    
    // warn
    public void warn(String msg, Object...parameters) {
    	if(isWarnEnabled()) {
        	log(null,Level.WARNING,StringUtil.format(msg, parameters),null);
    	}
    }
    public void warn(Throwable t, String msg, Object...parameters) {
    	if(isWarnEnabled()) {
        	log(t,Level.WARNING,StringUtil.format(msg, parameters),null);
    	}
    }
    public void warnp(Object clazz, String methodName, String msg, Object...parameters) {
    	if(isWarnEnabled()) {
        	logp(clazz,methodName,null,Level.WARNING,StringUtil.format(msg, parameters),null);
    	}
    }
    public void warnp(Object clazz, String methodName, Throwable t, String msg, Object...parameters) {
    	if(isWarnEnabled()) {
        	logp(clazz,methodName,t,Level.WARNING,StringUtil.format(msg, parameters),null);
    	}
    }

    // error
    public void error(String msg, Object...parameters) {
    	if(isErrorEnabled()) {
        	log(null,Level.SEVERE,StringUtil.format(msg, parameters),null);
    	}
    }
    public void error(Throwable t, String msg, Object...parameters) {
    	if(isErrorEnabled()) {
        	log(t,Level.SEVERE,StringUtil.format(msg, parameters),null);
    	}
    }
    public void errorp(Object clazz, String methodName, String msg, Object...parameters) {
    	if(isErrorEnabled()) {
        	logp(clazz,methodName,null,Level.SEVERE,StringUtil.format(msg, parameters),null);
    	}
    }
    public void errorp(Object clazz, String methodName, Throwable t, String msg, Object...parameters) {
    	if(isErrorEnabled()) {
        	logp(clazz,methodName,t,Level.SEVERE,StringUtil.format(msg, parameters),null);
    	}
    }

    // traceEvent
	public void traceEvent(String msg, Object...parameters) {
    	if(isTraceEventEnabled()) {
        	log(null,Level.FINE,StringUtil.format(msg, parameters),null);
    	}
	}
    public void traceEvent(Throwable t, String msg, Object...parameters) {
    	if(isTraceEventEnabled()) {
        	log(t,Level.FINE,StringUtil.format(msg, parameters),null);
    	}
    }
    public void traceEventp(Object clazz, String methodName, String msg, Object...parameters) {
    	if(isTraceEventEnabled()) {
        	logp(clazz,methodName,null,Level.FINE,StringUtil.format(msg, parameters),null);
    	}
    }
    public void traceEventp(Object clazz, String methodName, Throwable t, String msg, Object...parameters) {
    	if(isTraceEventEnabled()) {
        	logp(clazz,methodName,t,Level.FINE,StringUtil.format(msg, parameters),null);
    	}
    }

    // traceDebug
	public void traceDebug(String msg, Object...parameters) {
    	if(isTraceDebugEnabled()) {
        	log(null,Level.FINEST,StringUtil.format(msg, parameters),null);
    	}
	}
    public void traceDebug(Throwable t, String msg, Object...parameters) {
    	if(isTraceDebugEnabled()) {
        	log(t,Level.FINEST,StringUtil.format(msg, parameters),null);
    	}
    }
    public void traceDebugp(Object clazz, String methodName, String msg, Object...parameters) {
    	if(isTraceDebugEnabled()) {
        	logp(clazz,methodName,null,Level.FINEST,StringUtil.format(msg, parameters),null);
    	}
    }
    public void traceDebugp(Object clazz, String methodName, Throwable t, String msg, Object...parameters) {
    	if(isTraceDebugEnabled()) {
        	logp(clazz,methodName,t,Level.FINEST,StringUtil.format(msg, parameters),null);
    	}
    }
}
