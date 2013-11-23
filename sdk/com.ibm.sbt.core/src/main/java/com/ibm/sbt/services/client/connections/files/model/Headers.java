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
package com.ibm.sbt.services.client.connections.files.model;

/**
 * Files Headers
 * 
 * @author Vimal Dhupar
 */
public class Headers {

	public static final String	ATOM			= "application/atom+xml";
	public static final String BINARY 			= "application/octet-stream";
	public static final String	UTF				= "UTF-8";
	public static final String	XUpdateNonce	= "X-Update-Nonce";
	public static final String	ContentType		= "Content-Type";
	public static final String	ContentLanguage	= "Content-Language";
	public static final String	Slug			= "Slug";
	public static final String	IfModifiedSince	= "If-Modified-Since";
	public static final String	IfNoneMatch		= "If-None-Match";

}
