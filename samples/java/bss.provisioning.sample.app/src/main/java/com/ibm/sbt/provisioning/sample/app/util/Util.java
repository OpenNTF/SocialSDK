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
package com.ibm.sbt.provisioning.sample.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
/**
 * Application utility class
 * */
public class Util {
	
	private static final Logger logger = Logger.getLogger(Util.class.getName());
	
	/**
	 * Method used for returning a String representation of the content of a file
	 * <p>
	 * @param  filePath   file path<br>
	 * @return a String representation of the content of the file passed as argument
	 */
	public static String readFully(String filePath) throws IOException {
		File file = new File(filePath);
	    FileInputStream fis = new FileInputStream(file);
	    byte[] data = new byte[(int)file.length()];
	    fis.read(data);
	    fis.close();
	    return new String(data, "UTF-8");
	}
	
	/**
	 * Method needed for creating a <code>BasicEndpoint</code> object starting from the
	 * <code>String</code> representation of the endpoint URL and the user and password used for basic authentication
	 * <p>
	 * @param  url   <code>String</code> representation of the endpoint URL<br>
	 * @param  user  user credential for basic authentication<br>
	 * @param  password   password credential for basic authentication<br>
	 * @return a <code>BasicEndpoint</code> object , a bean providing basic authentication features
	 */
	public static BasicEndpoint createBasicEndpoint(String url, String user, String password) {
		BasicEndpoint basicEndpoint = new BasicEndpoint();
		basicEndpoint.setUrl(url);
		basicEndpoint.setForceTrustSSLCertificate(true);
		basicEndpoint.setUser(user);
		basicEndpoint.setPassword(password);   
		return basicEndpoint ;
    }
	
	/**
	 * This method parses the file at the path passed as argument and return a list of objects 
	 * representing the subscribers to be provisioned .
	 * <p>
	 * @param  subscribersFilePath  a string representing the path to the subscribers json file<br>
	 * @return a <code>List</code> of <code>JsonJavaObject</code> representing the parsed subscribers
	 */
	public static List<JsonJavaObject> parseSubscribers( String subscribersFilePath ){
		List<JsonJavaObject> subscribers = null ;
		String subscribersJson = null ;
		JsonDataHandler handler = null ;
		try{
			subscribersJson = readFully(subscribersFilePath);
			handler = new JsonDataHandler(subscribersJson);
		}catch( IOException ioe ){
			logger.severe("IOException thrown while parsing subscribers file "+subscribersFilePath+"\n"
					+ ioe.getMessage());
		}catch (JsonException je) {
			logger.severe("JsonException thrown while parsing subscribers file "+subscribersFilePath+"\n"
					+ je.getMessage());
		}
		if( subscribersJson != null && handler != null ) {
			subscribers = handler.getAsList("Subscribers") ;
		}
		return subscribers ; 
	}
}
