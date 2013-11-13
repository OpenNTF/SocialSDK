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

package com.ibm.sbt.services.client.connections.files.transformers;

import java.util.List;
import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;


/**
 * MultipleFile Transformer provides helper methods for construction of Files Feed XML payload, 
 * in case the payload contains multiple entries. 
 * <p>
 * @author Vimal Dhupar
 */

public class MultipleFileTransformer extends AbstractBaseTransformer {
	private String	SOURCEPATH = "/com/ibm/sbt/services/client/connections/files/templates/";
	
	public MultipleFileTransformer() {
	}
	
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		return null;
	}
	
	public String transform(List<String> ids, String category) throws TransformerException {
		String xml = getTemplateContent(SOURCEPATH + "FileFeedTmpl.xml");
		
		String entriesXml = "";
		
		for(String currentElement : ids) {
			String entryXml = getTemplateContent(SOURCEPATH + "FileItemEntryTmpl.xml");
			if(StringUtil.isNotEmpty(category)) { 
				String categoryXml = getXMLRep(getStream(SOURCEPATH + "FileCategoryTmpl.xml"), "category", XmlTextUtil.escapeXMLChars(category));
				entryXml = getXMLRep(entryXml, "getCategory", categoryXml);
			}
			String itemXml = getXMLRep(getStream(SOURCEPATH + "FileItemIdTmpl.xml"), "fileId", XmlTextUtil.escapeXMLChars(currentElement));
			entryXml = getXMLRep(entryXml, "getItem", itemXml);
			entriesXml += removeExtraPlaceholders(entryXml);
		}
		
		if(StringUtil.isNotEmpty(entriesXml)){
			xml = getXMLRep(xml, "getEntries", entriesXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
