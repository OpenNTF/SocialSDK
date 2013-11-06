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
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants;


/**
 * ProfileTransformer provides helper methods for construction of Profiles XML payload. 
 * <p>
 * @author Swati Singh
 */
public class ProfileTransformer  extends AbstractBaseTransformer {

	private Profile profile;
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/profiles/templates/Profile/";
	private String  requestType;
	private String  tmplFile; 
	private String  attributeTmplFile;
	private String  createProfileTmpl = "CreateProfileTmpl.xml" ; 
	private String  createProfileAttributeTmpl = "CreateProfileAttributeTmpl.xml" ;
	
	private String  updateProfileTmpl = "UpdateProfileTmpl.xml" ;
	private String  updateProfileAttributeTmpl = "UpdateProfileAttributeTmpl.txt" ;
	private String  updateProfileAddressTmpl = "UpdateProfileAddressTmpl.txt" ;
	
	/*
	 * Tranformer needs instance of Profile 
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */
	public ProfileTransformer(Profile profile) {
		this.profile = profile;
	}
	
	public String createTransform(Map<String, Object> fieldmap)throws TransformerException{
		requestType = "create";
		tmplFile = createProfileTmpl;
		attributeTmplFile = createProfileAttributeTmpl;
		return transform(fieldmap);
		
	}
	
	public String updateTransform(Map<String, Object> fieldmap) throws TransformerException{
		requestType = "update";
		tmplFile = updateProfileTmpl;
		attributeTmplFile = updateProfileAttributeTmpl;
		return transform(fieldmap);
	}
	
	public String getKeyIdentifier(String fieldMapkey){

    	String mappedKey ="";
    	
    	if(requestType.equalsIgnoreCase("create")){
	    	if((ProfilesConstants.createFieldsIdentifierMap).get(fieldMapkey)!=null)
	    		mappedKey = (ProfilesConstants.createFieldsIdentifierMap).get(fieldMapkey);
    	}
    	else{
    		if((ProfilesConstants.updateFieldsIdentifierMap).get(fieldMapkey)!=null){
    			mappedKey = (ProfilesConstants.updateFieldsIdentifierMap).get(fieldMapkey);
    		}
    	}
    	return mappedKey;

	}

	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		String	finalXml = getTemplateContent(sourcepath+tmplFile);
		String attributeXml = getTemplateContent(sourcepath+attributeTmplFile);
		String contentXml = "";
		String addressXml = getTemplateContent(sourcepath+updateProfileAddressTmpl);

		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			String currentElement = xmlEntry.getKey();
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			
			// for address
			if(currentElement.equalsIgnoreCase("streetAddress") || currentElement.equalsIgnoreCase("extendedAddress") ||
					 currentElement.equalsIgnoreCase("locality") || currentElement.equalsIgnoreCase("region") || 
					 	currentElement.equalsIgnoreCase("postalCode") || currentElement.equalsIgnoreCase("countryName")){
				String addressFields = getXMLRep(addressXml,currentElement,XmlTextUtil.escapeXMLChars(currentValue));
				addressXml = getXMLRep(addressFields,currentElement,XmlTextUtil.escapeXMLChars(currentValue));
				finalXml = getXMLRep(getTemplateContent(sourcepath+tmplFile),"address",addressXml);
			}
			else{
				String attrKeyXml = getXMLRep(attributeXml,"attributeName",XmlTextUtil.escapeXMLChars(getKeyIdentifier(currentElement)));
				contentXml = getXMLRep(attrKeyXml,"attributeValue",XmlTextUtil.escapeXMLChars(currentValue));
			}
			finalXml = getXMLRep(finalXml,currentElement,contentXml);
		}
		finalXml = removeExtraPlaceholders(finalXml);
		return finalXml;
	}
	
}

