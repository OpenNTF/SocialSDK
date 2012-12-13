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

import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of a log manager factory.
 * @ibm-not-published
 */
public abstract class AbstractLogMgrFactory implements LogMgrFactory {
    
	private Map<String,LogMgr> _logs = new HashMap<String,LogMgr>();

    public AbstractLogMgrFactory() {
    }

    public LogMgr getLogMgr(String loggerName, String packageName) throws LogException {
        LogMgr obj = _logs.get(loggerName);
        if(obj==null) {
        	synchronized(this) {
                obj = _logs.get(loggerName);
                if(obj==null) {
                	obj = createLogMgr(loggerName,null);
                	_logs.put(loggerName, obj);
                }
                return obj;
        	}
        }
        return obj;
    }

    protected abstract LogMgr createLogMgr(String loggerName, String description);
}
