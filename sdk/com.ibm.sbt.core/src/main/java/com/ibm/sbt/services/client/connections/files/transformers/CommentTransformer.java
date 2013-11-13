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
 * Comment Transformer provides helper methods for construction of Comment XML payload. 
 * <p>
 * @author Vimal Dhupar
 */

public class CommentTransformer extends AbstractBaseTransformer {
	private String	SOURCEPATH = "/com/ibm/sbt/services/client/connections/files/templates/";
	
	public CommentTransformer() {
	}
	
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		
		String xml = getTemplateContent(SOURCEPATH + "FileCommentsTmpl.xml");
		String categoryXml = "";
		String contentXml = "";
		String deleteXml = "";
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.contains("category")){
				categoryXml += getXMLRep(getStream(SOURCEPATH + "FileCategoryTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("content")){
				contentXml = getXMLRep(getStream(SOURCEPATH + "FileContentTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("deleteWithRecord")){
				deleteXml = getXMLRep(getStream(SOURCEPATH + "FileDeleteCommentTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
			}
			
		}
		if(StringUtil.isNotEmpty(categoryXml)){
			xml = getXMLRep(xml, "getCategory", categoryXml);
		}
		
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent", contentXml);
		}
		
		if(StringUtil.isNotEmpty(deleteXml)){
			xml = getXMLRep(xml, "getDeleteComment", deleteXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
