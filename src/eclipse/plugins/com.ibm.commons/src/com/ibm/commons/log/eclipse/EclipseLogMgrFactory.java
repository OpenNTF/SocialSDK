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


package com.ibm.commons.log.eclipse;

import com.ibm.commons.log.AbstractLogMgrFactory;
import com.ibm.commons.log.LogMgr;

/**
 * Eclipse implementation of a log manager factory.
 * @ibm-not-published
 */
public class EclipseLogMgrFactory extends AbstractLogMgrFactory {

    public EclipseLogMgrFactory() {
    }

    protected LogMgr createLogMgr(String loggerName, String description) {
    	return new EclipseLogMgr(this, loggerName, description);
    }
}
