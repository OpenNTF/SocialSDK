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
 * Moderation Transformer provides helper methods for construction of File Moderation XML payload. 
 * <p>
 * @author Vimal Dhupar
 */

public class ModerationTransformer extends AbstractBaseTransformer {
	private String	SOURCEPATH = "/com/ibm/sbt/services/client/connections/files/templates/";
	
	public ModerationTransformer() {
	}
	
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		
		String xml = getTemplateContent(SOURCEPATH + "FileModerationEntryTmpl.xml");
		String contentXml = "";
		String actionXml = "";
		String refXml = "";
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.equalsIgnoreCase("content")){
				contentXml = getXMLRep(getStream(SOURCEPATH + "FileContentTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("action")){
				actionXml = getXMLRep(getStream(SOURCEPATH + "FileModerationActionTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("fileId")){
				refXml = getXMLRep(getStream(SOURCEPATH + "FileModerationRefTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
				Object entity = fieldmap.get("entity");
				if(entity != null) {
					refXml = getXMLRep(refXml, "entity", XmlTextUtil.escapeXMLChars(entity.toString()));
				}
			}
		}
		if(StringUtil.isNotEmpty(actionXml)){
			xml = getXMLRep(xml, "getAction", actionXml);
		}
		
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent", contentXml);
		}
		
		if(StringUtil.isNotEmpty(refXml)){
			xml = getXMLRep(xml, "getRef", refXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
