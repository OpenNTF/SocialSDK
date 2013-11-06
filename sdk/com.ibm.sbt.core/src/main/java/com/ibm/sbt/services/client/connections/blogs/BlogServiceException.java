/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.services.client.connections.blogs;

import com.ibm.sbt.services.client.SBTServiceException;

/**
 * This class represents Blog Service Exception   
 * 
 * @author Swati Singh
 */

public class BlogServiceException extends SBTServiceException {

	private static final long	serialVersionUID	= -3217791404553288961L;
	
	protected BlogServiceException(Throwable nextException) {
		super(nextException);
	}

	public BlogServiceException(Throwable nextException, String message) {
		super(nextException, message);
	}
	
	public BlogServiceException(Throwable nextException, String message, Object...params) {
		super(nextException, message, params);
	}
}