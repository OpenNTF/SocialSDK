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
import com.ibm.sbt.services.client.connections.files.File;


/**
 * Folder Transformer provides helper methods for construction of Folder XML payload. 
 * <p>
 * @author Vimal Dhupar
 */

public class FolderTransformer extends AbstractBaseTransformer {
	private String	SOURCEPATH = "/com/ibm/sbt/services/client/connections/files/templates/";
	
	public FolderTransformer() {
	}
	
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		
		String xml = getTemplateContent(SOURCEPATH + "FolderEntryTmpl.xml");
		String categoryXml = "";
		String idXml = "";
		String labelXml = "";
		String titleXml = "";
		String summaryXml = "";
		String visibilityXml = "";
		String visibilityShareXml = "";
		
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
			}else if(currentElement.equalsIgnoreCase("label")){
				labelXml = getXMLRep(getStream(SOURCEPATH + "FolderLabelTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
				titleXml = getXMLRep(getStream(SOURCEPATH + "FileTitleTmpl.xml"), "title", XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("summary")){
				summaryXml = getXMLRep(getStream(SOURCEPATH + "FileSummaryTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("visibility")){
				visibilityXml = getXMLRep(getStream(SOURCEPATH + "FileVisibilityTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("shareWithId")){
				visibilityShareXml = getXMLRep(getStream(SOURCEPATH + "FileVisibilityShareTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
				Object shareWithWhat = fieldmap.get("shareWithWhat");
				if(shareWithWhat != null) {
					visibilityShareXml = getXMLRep(visibilityShareXml, "shareWithWhat", XmlTextUtil.escapeXMLChars(shareWithWhat.toString()));
				}
				Object shareWithRole = fieldmap.get("shareWithRole");
				if(shareWithRole != null) {
					visibilityShareXml = getXMLRep(visibilityShareXml, "shareWithRole", XmlTextUtil.escapeXMLChars(shareWithRole.toString()));
				}
			}
		}
		if(StringUtil.isNotEmpty(categoryXml)){
			xml = getXMLRep(xml, "getCategory", categoryXml);
		}
		
		if(StringUtil.isNotEmpty(idXml)){
			xml = getXMLRep(xml, "getId", idXml);
		}
		
		if(StringUtil.isNotEmpty(labelXml)){
			xml = getXMLRep(xml, "getFolderLabel", labelXml);
			xml = getXMLRep(xml, "getTitle", titleXml);
		}
		
		if(StringUtil.isNotEmpty(summaryXml)){
			xml = getXMLRep(xml, "getSummary", summaryXml);
		}
		
		if(StringUtil.isNotEmpty(visibilityXml)){
			xml = getXMLRep(xml, "getVisibility", visibilityXml);
		}
		
		if(StringUtil.isNotEmpty(visibilityShareXml)){
			xml = getXMLRep(xml, "getVisibilityShare", visibilityShareXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
