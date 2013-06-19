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
package com.ibm.sbt.services.client;

import com.ibm.commons.util.AbstractException;

/**
 * Exception to group al SDK Checked exceptions; as such, constructors are protected.
 * 
 * @author Lorenzo Boccaccia
 * @date Dec 6, 2012
 */

public class SBTServiceException extends AbstractException {

	private static final long	serialVersionUID	= 201452561795877253L;

	protected SBTServiceException(Throwable nextException) {
		super(nextException);
	}

	protected SBTServiceException(Throwable nextException, String message) {
		super(nextException, message);
	}
	
	protected SBTServiceException(Throwable nextException, String message, Object...params) {
		super(nextException, message, params);
	}
	

}
