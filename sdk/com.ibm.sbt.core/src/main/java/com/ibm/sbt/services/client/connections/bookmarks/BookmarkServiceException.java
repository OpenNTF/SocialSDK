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
package com.ibm.sbt.services.client.connections.bookmarks;

import com.ibm.sbt.services.client.SBTServiceException;

/**
 * Thrown when an application when an error occurs using the BookmarkService.
 * 
 * @author mwallace
 *
 */
public class BookmarkServiceException extends SBTServiceException {

	private static final long serialVersionUID = -1999186963503118555L;

	/**
	 * Constructs a <code>BookmarkServiceException</code> with the specified cause.
	 * 
	 * @param cause
	 */
	public BookmarkServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a <code>BookmarkServiceException</code> with the specified cause and message.
	 * 
	 * @param cause
	 * @param message
	 */
	public BookmarkServiceException(Throwable cause, String message) {
		super(cause, message);
	}
	
	/**
	 * Constructs a <code>BookmarkServiceException</code> with the specified cause, message and parameters.
	 * 
	 * @param cause
	 * @param message
	 * @param params
	 */
	public BookmarkServiceException(Throwable cause, String message, Object...params) {
		super(cause, message, params);
	}
	
}
