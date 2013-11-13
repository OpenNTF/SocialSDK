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
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.base.util.EntityUtil;

/**
 * CommunityMemberTransformer provides helper methods for construction of Communities Member XML payload. 
 * <p>
 * 
 * @author Manish Kataria
 */

public class CommunityMemberTransformer extends AbstractBaseTransformer {
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/communities/templates/";

	@Override
	public String transform(Map<String, Object> fieldmap) throws TransformerException {
		String roleXml;
		String useridXml;
		String templateXml = getTemplateContent(sourcepath+"MemberTmpl.xml");
		for (Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()) {

			String currentElement = xmlEntry.getKey();
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			
			if (currentElement.contains("role")) {
				roleXml = getXMLRep(getStream(sourcepath + "Role.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
				templateXml = getXMLRep(templateXml, "getRole", roleXml);
			} else if (currentElement.equalsIgnoreCase("id") || currentElement.equalsIgnoreCase("userid")
					|| currentElement.equalsIgnoreCase("contributorUid") ||currentElement.equalsIgnoreCase("contributorEmail"))  {
				// check if user provided email or userid and select the template accodingly
				if (EntityUtil.isEmail(currentValue)) {
					useridXml = getXMLRep(getStream(sourcepath+ "EmailTmpl.xml"), "email", currentValue);
					templateXml = getXMLRep(templateXml, "getEmail", useridXml);
				} else {
					useridXml = getXMLRep(getStream(sourcepath+ "UseridTmpl.xml"), "userid", currentValue);
					templateXml = getXMLRep(templateXml, "getUserid", useridXml);
				}
			}
		}
		
		templateXml = removeExtraPlaceholders(templateXml);
		return templateXml;
	}
}
