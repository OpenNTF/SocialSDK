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

package com.ibm.sbt.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.ibm.sbt.services.client.smartcloud.communities.Community;

/**
 * @author Carlos Manias
 *
 */
public class XMLPayloadBuilder implements PayloadBuilder {

	private static final String sourceClass = Community.class.getName();
    private static final Logger logger = Logger.getLogger(sourceClass);
    
	protected XMLPayloadBuilder() {}
	
	private DocumentBuilder getDocumentBuilder(){
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "createPayload", new Object[] {  });
        }
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.throwing(sourceClass, "createPayload", e);
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "createPayload", new Object[] { docBuilder });
        }
		return docBuilder;
	}
	
	@Override
	public Document createPayload(){
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "createPayloadDocument", new Object[] {  });
        }
		DocumentBuilder docBuilder = getDocumentBuilder();
		Document doc = docBuilder.newDocument();
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "createPayloadDocument", new Object[] { doc });
        }
		return doc;
	}
}
