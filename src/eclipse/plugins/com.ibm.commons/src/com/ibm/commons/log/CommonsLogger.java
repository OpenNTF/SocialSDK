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

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;

/**
 * List of Loggers used by the commons API.
 * 
 * @ibm-not-published
 */
public class CommonsLogger extends Log {

    public static LogMgr STANDARD = load("com.ibm.common.standard", "Standard IBM commons output (default logger)"); // $NON-NLS-1$ $NLS-CommonsLogger.StandardIBMcommonsoutputdefaultlo-2$
    public static LogMgr ENCRYPTION = load("com.ibm.common.encryption", ""); // $NON-NLS-1$
}