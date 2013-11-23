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

import com.ibm.commons.util.AbstractException;

/**
 * Logging Exception.
 * <p>This exception is thrown by the logging functions.</p> 
 * @ibm-api
 */
public class LogException extends AbstractException {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public LogException(Throwable nextException) {
        super(nextException);
    }

    /**
     * @param message
     */
    public LogException(Throwable nextException, String message) {
        super(nextException,message);
    }
}
