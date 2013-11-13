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

package com.ibm.sbt.services.client.connections.activity.transformers;

import java.util.Map;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.base.util.EntityUtil;

/**
 * ActivityMemberTransformer provides helper methods for construction of Activity Member XML payload. 
 * <p>
 * 
 * @author Vimal Dhupar
 */

public class ActivityMemberTransformer extends AbstractBaseTransformer {
	
	private String	SOURCEPATH = "/com/ibm/sbt/services/client/connections/activity/templates/";

	@Override
	public String transform(Map<String, Object> fieldmap) throws TransformerException {
		
		String templateXml = getTemplateContent(SOURCEPATH + "MemberTmpl.xml");
		String roleXml = "";
		String useridXml = "";
		String categoryXml = "";
		
		for (Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()) {
			String currentElement = xmlEntry.getKey();
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			
			if (currentElement.contains("role")) {
				roleXml = getXMLRep(getStream(SOURCEPATH + "RoleTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
				templateXml = getXMLRep(templateXml, "getRole", roleXml);
			} else if (currentElement.equalsIgnoreCase("id") || currentElement.equalsIgnoreCase("userid")
					|| currentElement.equalsIgnoreCase("contributorUserUuid") ||currentElement.equalsIgnoreCase("contributorUserEmail")) {
				if (EntityUtil.isEmail(currentValue)) {
					useridXml = getXMLRep(getStream(SOURCEPATH+ "EmailTmpl.xml"), "email", currentValue);
					templateXml = getXMLRep(templateXml, "getEmail", useridXml);
				} else {
					useridXml = getXMLRep(getStream(SOURCEPATH+ "UseridTmpl.xml"), "userid", currentValue);
					templateXml = getXMLRep(templateXml, "getUserid", useridXml);
				}
			} else if(currentElement.contains("Category")) {
				categoryXml = getXMLRep(getStream(SOURCEPATH + "CategoryTmpl.xml"), currentElement, XmlTextUtil.escapeXMLChars(currentValue));
				templateXml = getXMLRep(templateXml, "getCategory", categoryXml);
			}
		}
		
		templateXml = removeExtraPlaceholders(templateXml);
		return templateXml;
	}
}
