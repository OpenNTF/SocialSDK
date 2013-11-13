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

import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;


/**
 * FileTransformer provides helper methods for construction of Files XML payload. 
 * <p>
 * @author Vimal Dhupar
 */

public class FileTransformer extends AbstractBaseTransformer {
	private String	SOURCEPATH = "/com/ibm/sbt/services/client/connections/files/templates/";
	
	public FileTransformer() {
	}
	
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		
		String xml = getTemplateContent(SOURCEPATH + "FileEntryTmpl.xml");
		String categoryXml = "";
		String idXml = "";
		String uuidXml = "";
		String labelXml = "";
		String titleXml = "";
		String summaryXml = "";
		String visibilityXml = "";
		String itemXml = "";
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.contains("category")){
				categoryXml += getXMLRep(getStream(SOURCEPATH + "FileCategoryTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("id")){
				idXml = getXMLRep(getStream(SOURCEPATH + "FileIdTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("uuid")){
				idXml = getXMLRep(getStream(SOURCEPATH + "FileUuidTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("label")){
				labelXml = getXMLRep(getStream(SOURCEPATH + "FileLabelTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
				titleXml = getXMLRep(getStream(SOURCEPATH + "FileTitleTmpl.xml"), "title", XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("summary")){
				summaryXml = getXMLRep(getStream(SOURCEPATH + "FileSummaryTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("visibility")){
				visibilityXml = getXMLRep(getStream(SOURCEPATH + "FileVisibilityTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.contains("itemId")){
				itemXml += getXMLRep(getStream(SOURCEPATH + "FileItemIdTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}
		}
		if(StringUtil.isNotEmpty(categoryXml)){
			xml = getXMLRep(xml, "getCategory", categoryXml);
		}
		
		if(StringUtil.isNotEmpty(idXml)){
			xml = getXMLRep(xml, "getId", idXml);
		}
		
		if(StringUtil.isNotEmpty(uuidXml)){
			xml = getXMLRep(xml, "getUuid", uuidXml);
		}
		
		if(StringUtil.isNotEmpty(labelXml)){
			xml = getXMLRep(xml, "getLabel", labelXml);
			xml = getXMLRep(xml, "getTitle", titleXml);
		}
		
		if(StringUtil.isNotEmpty(summaryXml)){
			xml = getXMLRep(xml, "getSummary", summaryXml);
		}
		
		if(StringUtil.isNotEmpty(visibilityXml)){
			xml = getXMLRep(xml, "getVisibility", visibilityXml);
		}
		
		if(StringUtil.isNotEmpty(itemXml)){
			xml = getXMLRep(xml, "getItem", itemXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
