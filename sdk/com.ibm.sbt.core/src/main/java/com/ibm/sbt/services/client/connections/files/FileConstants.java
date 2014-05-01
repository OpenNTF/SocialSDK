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
package com.ibm.sbt.services.client.connections.files;

/**
 * Class used to maintain the constants used by Files API and related classes. 
 * 
 * @author Vimal Dhupar
 */
public final class FileConstants {

	public final static char SEPARATOR   = '/';
	
	public static final String LOCKTYPE_HARD	= "HARD";
	public static final String LOCKTYPE_NONE	= "NONE";
	public static final String CATEGORY	= "category";
	public static final String CATEGORY_DOCUMENT	= "document";
	public static final String CATEGORY_COMMENT	= "comment";
	public static final String CATEGORY_COLLECTION	= "collection";
	public static final String CATEGORY_COMMUNITY	= "community";
	public static final String DIRECTION_INBOUND	= "inbound";
	public static final String DIRECTION_OUTBOUND	= "outbound";
	public static final String VISIBILITY	= "visibility";
	public static final String VISIBILITY_PUBLIC	= "public";
	public static final String VISIBILITY_PRIVATE	= "private";
	public static final String X_UPDATE_NONCE	= "X-Update-Nonce";
    
	
}
