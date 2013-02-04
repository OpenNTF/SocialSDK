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

import java.util.logging.Logger;


/**
 * Empty LogMgr implementation.
 * @ibm-not-published
 */
public class EmptyLogMgr implements LogMgr {

    public static final LogMgr instance = new EmptyLogMgr();

    public EmptyLogMgr() {
    }

    public Logger getLogger() {
        return null;
    }

    public String getDescription() {
        return "Empty Log Manager"; // $NLI-EmptyLogMgr.EmptyLogManager-1$
    }

    public boolean isTraceDebugEnabled() {
        return false;
    }

    public boolean isTraceEntryExitEnabled() {
        return false;
    }

    public boolean isTraceEventEnabled() {
        return false;
    }

    public boolean isInfoEnabled() {
        return false;
    }

    public boolean isWarnEnabled() {
        return false;
    }

    public boolean isErrorEnabled() {
        return false;
    }

    public boolean isFatalEnabled() {
        return false;
    }

    public void setLogLevel(int newLevel) {
        
    }
    
    public void info(String msg, Object...parameters) {
    }
    public void info(Throwable t, String msg, Object...parameters) {
    }
    public void infop(Object clazz, String method, String msg, Object...parameters) {
    }
    public void infop(Object clazz, String method, Throwable t, String msg, Object...parameters) {
    }
    
    public void warn(String msg, Object...parameters) {
    }
    public void warn(Throwable t, String msg, Object...parameters) {
    }
    public void warnp(Object clazz, String method, String msg, Object...parameters) {
    }
    public void warnp(Object clazz, String method, Throwable t, String msg, Object...parameters) {
    }
    
    public void error(String msg, Object...parameters) {
    }
    public void error(Throwable t, String msg, Object...parameters) {
    }
    public void errorp(Object clazz, String method, String msg, Object...parameters) {
    }
    public void errorp(Object clazz, String method, Throwable t, String msg, Object...parameters) {
    }
    
    public void traceEvent(String msg, Object...parameters) {
    }
    public void traceEvent(Throwable t, String msg, Object...parameters) {
    }
    public void traceEventp(Object clazz, String method, String msg, Object...parameters) {
    }
    public void traceEventp(Object clazz, String method, Throwable t, String msg, Object...parameters) {
    }
    
    public void traceDebug(String msg, Object...parameters) {
    }
    public void traceDebug(Throwable t, String msg, Object...parameters) {
    }
    public void traceDebugp(Object clazz, String methodName, String msg, Object...parameters) {
    }
    public void traceDebugp(Object clazz, String methodName, Throwable t, String msg, Object...parameters) {
    }

    public void traceEntry(Object clazz, String methodName) {
    }
    public void traceEntry(Object clazz, String methodName, Object... parameters) {
    }

    public void traceExit(Object clazz, String methodName) {
    }
    public void traceExit(Object clazz, String methodName, Object ret) {
    }
}