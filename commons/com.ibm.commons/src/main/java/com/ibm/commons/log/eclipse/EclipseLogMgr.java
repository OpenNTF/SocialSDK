/*
 *  Copyright IBM Corp. 2012-2013
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


package com.ibm.commons.log.eclipse;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.ibm.commons.log.AbstractLogMgr;

/**
 * Eclipse implementation of a log manager.
 * @ibm-not-published
 */
public class EclipseLogMgr extends AbstractLogMgr  {

    protected Logger _logger;

    public EclipseLogMgr(EclipseLogMgrFactory factory, String loggerName, String description)  {
    	super(factory,description);
        _logger = Logger.getLogger(loggerName, null);

    }

    public Logger getLogger() {
    	return _logger;
    }
    
    protected LogRecord createLogRecord(Level level, String msg) {
        LogRecord lr = new LogRecord(level, msg);
        return lr;
    }
}
