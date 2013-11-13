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

package com.ibm.sbt.services.client.connections.profiles.transformers;

import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.profiles.ColleagueConnection;


/**
 * ProfileTransformer provides helper methods for construction of Profiles XML payload. 
 * <p>
 * @author Swati Singh
 */
public class ColleagueConnectionTransformer  extends AbstractBaseTransformer {

	private ColleagueConnection colleagueConnection;
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/profiles/templates/ColleagueConnection/";
	private String  inviteTmplFile = "InviteTmpl.xml"; 
	private String status = "pending";

	/*
	 * Tranformer needs instance of ColleagueConnection 
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */
	public ColleagueConnectionTransformer(ColleagueConnection colleagueConnection) {
		this.colleagueConnection = colleagueConnection;
	}
	
	public String createTransform(Map<String, Object> fieldmap)throws TransformerException{
		
		return transform(fieldmap);
		
	}
	
	public String updateTransform(String inviteAction, Map<String, Object> fieldmap)throws TransformerException{
		status = inviteAction;
		return transform(fieldmap);
		
	}

	@Override
	public String transform(Map<String, Object> fieldmap)throws TransformerException {
		
		String xml = getTemplateContent(sourcepath+inviteTmplFile);
		String idXml = "";
		String titleXml = "";
		String contentXml = "";
		String statusXml = "";
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey();
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			
			if(currentElement.equalsIgnoreCase("id")){
				idXml = getXMLRep(getStream(sourcepath+"ColleagueConnectionIdTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("content")){
				contentXml = getXMLRep(getStream(sourcepath+"ColleagueConnectionContentTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase("title")){
				titleXml = getXMLRep(getStream(sourcepath+"ColleagueConnectionTitleTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
		
		}
		statusXml = getXMLRep(getStream(sourcepath+"ColleagueConnectionCategoryStatus.xml"),"status",status);
		xml = getXMLRep(xml, "getStatus",statusXml);
		if(StringUtil.isNotEmpty(idXml)){
			xml = getXMLRep(xml, "getId",idXml);
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	
	}
}
