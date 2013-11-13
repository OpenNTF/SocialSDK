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

package com.ibm.sbt.services.client.connections.blogs.transformers;

import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.blogs.Blog;
import com.ibm.sbt.services.client.connections.blogs.Comment;
import com.ibm.sbt.services.client.connections.blogs.BlogPost;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;


/**
 * BlogTransformer provides helper methods for construction of Forums XML payload. 
 * <p>
 * @author Swati Singh
 */

public class BaseBlogTransformer extends AbstractBaseTransformer {
	private BaseBlogEntity entity;
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/blogs/templates/";

	/*
	 * Tranformer needs instance of blog
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */
	
	public BaseBlogTransformer(BaseBlogEntity entity) {
		this.entity = entity;
	}

	@Override
	public String transform(Map<String, Object> fieldmap) throws TransformerException {
		
		String xml = getTemplateContent(sourcepath+"BaseEntryTmpl.xml");
		
		String tagsXml = "";
		String contentXml = "";
		String titleXml = "";
		String handleXml = "";
		String timeZoneXml = "";
		String replyToXml = "";
				
	for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.contains("tag")){
				tagsXml += getXMLRep(getStream(sourcepath+"CategoryTagTmpl.xml"),"tag",XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("content")){
				contentXml = getXMLRep(getStream(sourcepath+"ContentTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("title")){
				titleXml = getXMLRep(getStream(sourcepath+"TitleTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("handle")){
				handleXml = getXMLRep(getStream(sourcepath+"HandleTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("timeZone")){
				timeZoneXml = getXMLRep(getStream(sourcepath+"TimeZoneTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		if(StringUtil.isNotEmpty(tagsXml)){
			xml = getXMLRep(xml, "getTags",tagsXml);
		}
		if(StringUtil.isNotEmpty(handleXml)){
			xml = getXMLRep(xml, "getHandle",handleXml);
		}
		if(StringUtil.isNotEmpty(timeZoneXml)){
			xml = getXMLRep(xml, "getTags",timeZoneXml);
		}
		if(entity instanceof Comment){
			replyToXml = getXMLRep(getStream(sourcepath+"ReplyTmpl.xml"), "entryId", ((Comment)entity).getPostUuid());
			xml = getXMLRep(xml, "getReplyTo", replyToXml);
		}
		xml = removeExtraPlaceholders(xml);

		return xml;
	}

	
}