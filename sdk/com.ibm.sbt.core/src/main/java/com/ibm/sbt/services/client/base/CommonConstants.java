/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.base;

/**
 * This class contains all the constants used in all service classes.
 * 
 * @author Carlos Manias
 *
 */
public final class CommonConstants {

	/**
	 * Common String characters
	 */
	public static final String 	NL = "\n";
	public static final String 	EMPTY = "";
	public static final String 	COMMA = ",";
	public static final String 	SEMICOLON = ";";
	public static final String 	COLON = ":";
	public static final String 	DOT = ".";
	public static final String 	AT = "@";
	public static final char 	CH_SLASH	= '/';
	public static final char 	CH_COLON	= ':';

	public static final char 	CH_LEFT_BRACE = '{';
	public static final char 	CH_RIGHT_BRACE = '}';
	public static final String 	SLASH = "/";
	public static final String 	RE_QUESTION_MARK = "\\?";
	public static final String 	AMPERSAND = "&";
	public static final String 	DOUBLE_SLASH = "//";
	public static final String 	EQUALS	= "=";
	
	/**
	 * URL parameter construction
	 */
	public static final char 	INIT_URL_PARAM	= '?';
	public static final char 	URL_PARAM	= '&';
	public static final char 	CH_EQUALS		= '=';

	/**
	 * Headers parameters
	 */
	public static final String	IMAGE_				= "image/";
	public static final String 	IMAGE_JPG			= "image/jpg";
	public static final String 	SLUG				= "slug";
	public static final String 	CONTENT_ENCODING	= "Content-Encoding";
	public static final String 	GZIP				= "gzip";

	/**
	 * File extensions
	 */
	public static final String 	JPG	= "jpg";

	/**
	 * Authentication
	 */
	public static final String	OAUTH		= "oauth";
	public static final String	AUTH_TYPE	= "authType";
	
	/**
	 * Encoding
	 */
	public static final String	UTF8		= "UTF-8";
	public static final String	BINARY		= "binary";
	
	/**
	 * Data formats
	 */
	public static final String	JSON		= "json";
	public static final String	XML			= "xml";
	
	/**
	 * Boolean value names
	 */
	public static final String	TRUE			= "true";
	public static final String	FALSE			= "false";

	/**
	 * Content Type
	 */
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String BINARY_OCTET_STREAM = "binary/octet-stream";

	/**
	 * General Services
	 */
	public static final String LOCATION_HEADER = "Location";
	
	public enum HTTPCode {
		OK(200),
		CREATED(201),
		NO_CONTENT(204),
		BAD_REQUEST(400),
		UNAUTHORIZED(401),
		FORBIDDEN(403),
		NOT_FOUND(404),
		INTERNAL_SERVER_ERROR(500),
		NOT_IMPLEMENTED(501),
		SERVICE_UNAVAILABLE(503);
		
		private final int code;

		HTTPCode(int code){
			this.code = code;
		}
		
		public int getCode(){
			return code;
		}
		
		public boolean checkCode(int code){
			return this.code == code;
		}
	}
}
