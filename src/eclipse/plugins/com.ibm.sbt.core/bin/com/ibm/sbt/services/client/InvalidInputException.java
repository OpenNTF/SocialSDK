package com.ibm.sbt.services.client;

import com.ibm.commons.util.AbstractException;

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

/**
 * Exception used to flag invalid inputs to Service api's.
 * 
 * @author mkataria
 * @date Dec 10, 2012
 */
public class InvalidInputException extends AbstractException {

	String						msg;

	private static final long	serialVersionUID	= 1L;

	public InvalidInputException(Throwable nextException) {
		super(nextException);
	}

	public InvalidInputException(Throwable nextException, String msg, Object... params) {
		super(nextException, msg, params);
	}
}
