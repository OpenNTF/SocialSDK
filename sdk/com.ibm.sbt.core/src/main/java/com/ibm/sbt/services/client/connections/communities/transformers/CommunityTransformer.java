/*
 * � Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.communities.transformers;

import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;


/**
 * CommunityTransformer provides helper methods for construction of Communities XML payload. 
 * <p>
 * @author Manish Kataria
 */

public class CommunityTransformer extends AbstractBaseTransformer {
	private Community community;
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/communities/templates/";
	
	/*
	 * Tranformer needs instance of community 
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */
	public CommunityTransformer(Community community) {
		this.community = community;
	}
	
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		
		String xml = getTemplateContent(sourcepath+"CommunityTmpl.xml");
		String tagsXml = "";
		String contentXml = "";
		String titleXml = "";
		String typeXml = "";
		String communityUuidXml = "";
		String communityIdXml = "";
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.contains(CommunityXPath.tags.toString())){
				tagsXml += getXMLRep(getStream(sourcepath+"CategoryTmpl.xml"),"tag",XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(CommunityXPath.content.toString())){
				contentXml = getXMLRep(getStream(sourcepath+"CommunityContentTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(CommunityXPath.title.toString())){
				titleXml = getXMLRep(getStream(sourcepath+"CommunityTitleTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(CommunityXPath.communityType.toString())){
				typeXml = getXMLRep(getStream(sourcepath+"CommunityTypeTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(CommunityXPath.communityUuid.toString())){
				communityUuidXml = getXMLRep(getStream(sourcepath+"CommunityUuidTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
				communityIdXml = getXMLRep(getStream(sourcepath+"CommunityIdTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		else{ // need to provide empty content in payload, in case it is not set
			contentXml = getXMLRep(getStream(sourcepath+"CommunityContentTemplate.xml"),"content","");
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		if(StringUtil.isNotEmpty(tagsXml)){
			xml = getXMLRep(xml, "getTags",tagsXml);
		}
		if(StringUtil.isNotEmpty(communityUuidXml)){
			xml = getXMLRep(xml, "getCommunityUuid",communityUuidXml);
		}
		if(StringUtil.isNotEmpty(communityIdXml)){
			xml = getXMLRep(xml, "getCommunityId",communityIdXml);
		}
		
		if(StringUtil.isNotEmpty(typeXml)){
			xml = getXMLRep(xml, "getCommunityType",typeXml);
		}else{
			String commType = "";
			try {
				commType = community.getCommunityType();
			} catch (Exception e) {
			}
			if(StringUtil.isEmpty(commType)){ // must be for creating a new community, default to public
				typeXml = getXMLRep(getStream(sourcepath+"CommunityTypeTemplate.xml"),"communityType","public");
			}else{ // for update scenario
				typeXml = getXMLRep(getStream(sourcepath+"CommunityTypeTemplate.xml"),"communityType",community.getCommunityType());
			}
			xml = getXMLRep(xml, "getCommunityType",typeXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
