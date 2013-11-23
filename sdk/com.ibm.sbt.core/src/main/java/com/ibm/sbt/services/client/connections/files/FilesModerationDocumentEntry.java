package com.ibm.sbt.services.client.connections.files;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.connections.files.util.Messages;

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
 * @author Vimal Dhupar
 * @Represents Class to represent the Service Document for Files Moderation Service. To be Used by the
 *             Moderators for Moderating community files/comments.
 * @date Apr 18, 2013
 */
public class FilesModerationDocumentEntry {

	public static Document	data;
	static final String		sourceClass	= FilesModerationDocumentEntry.class.getName();
	static final Logger		logger		= Logger.getLogger(sourceClass);

	public FilesModerationDocumentEntry(Document data) {
		super();
		this.data = data;
	}

	public static Document getData() {
		return data;
	}

	public static void setData(Document data) {
		FilesModerationDocumentEntry.data = data;
	}

	/**
	 * get
	 * 
	 * @param fieldName
	 * @return String
	 */
	public String get(String fieldName) {
		String xpQuery = getXPathQuery(fieldName);
		return getFieldUsingXPath(xpQuery);
	}

	/**
	 * getXPathQuery
	 * 
	 * @return xpath query for specified field. Field names follow IBM Connections naming convention
	 */
	private String getXPathQuery(String fieldName) {
		return ContentMapFiles.moderationMap.get(fieldName);
	}

	/**
	 * getFieldUsingXPath
	 * <p>
	 * Execute xpath query on Profile XML
	 * 
	 * @return String
	 */
	private String getFieldUsingXPath(String xpathQuery) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFieldUsingXPath");
		}
		String result = null;
		try {
			result = DOMUtil.value(this.data, xpathQuery, NamespacesConnections.nameSpaceCtx);
		} catch (XMLException e) {
			logger.log(Level.SEVERE, Messages.MessageGenericError + "getFieldUsingXPath");
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getFieldUsingXPath");
		}
		return result;
	}

}
