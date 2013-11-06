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

package com.ibm.sbt.services.client.connections.communities.transformers;

import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.base.util.EntityUtil;


/**
 * InviteTransformer provides helper methods for construction of Community Invite XML payload. 
 * <p>
 * @author Swati Singh
 */

public class InviteTransformer extends AbstractBaseTransformer {
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/communities/templates/";
	
		
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		
		String xml = getTemplateContent(sourcepath+"InviteTmpl.xml");
		String titleXml = "";
		
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.equalsIgnoreCase("title")){
				titleXml = getXMLRep(getStream(sourcepath+"CommunityTitleTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("contributorEmail")){
				xml = getXMLRep(xml,currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("contributorUid")){
				xml = getXMLRep(xml,currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if (EntityUtil.isEmail(currentValue)) {
				String emailXml = getXMLRep(getStream(sourcepath+ "EmailTmpl.xml"), "email", currentValue);
				xml = getXMLRep(xml, "getEmail", emailXml);
			} else {
				String useridXml = getXMLRep(getStream(sourcepath+ "UseridTmpl.xml"), "userid", currentValue);
				xml = getXMLRep(xml, "getUserid", useridXml);
			}
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
