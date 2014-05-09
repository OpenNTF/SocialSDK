/*
 * © Copyright IBM Corp. 2014
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

package com.ibm.sbt.services.client.connections.profiles.serializers;

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.COLON;
import static com.ibm.sbt.services.client.base.CommonConstants.COMMA;
import static com.ibm.sbt.services.client.base.CommonConstants.EMPTY;
import static com.ibm.sbt.services.client.base.CommonConstants.NL;
import static com.ibm.sbt.services.client.base.CommonConstants.SEMICOLON;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.DATA;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.KEY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.PERSON;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TAGS;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TEXT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.VALUE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.BEGIN_VCARD;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CATEGORIES;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.COUNTRYNAME;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.END_VCARD;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.EXTENDEDADDRESS;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.LOCALITY;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.POSTALCODE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.PROFILE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.REGION;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.SNX_ATTRIB;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.STREETADRESS;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.VCARD_ADDR;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.VCARD_V21;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;
import com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.ProfileAttribute;
import com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.VCardField;

/**
 * 
 * @author Carlos Manias
 *
 */
public class ProfileSerializer extends AtomEntitySerializer<Profile> {

	public ProfileSerializer(Profile entity) {
		super(entity);
	}

	protected void generateCreatePayload() {
		Node entry = entry();
		
		appendChildren(entry,
				profile(),
				contentCreate()
		);
	}

	protected void generateUpdatePayload() {
		Node entry = entry();
		
		appendChildren(entry,
				profile(),
				contentUpdate()
		);
	}

	protected void generateTagsPayload() {
		String[] tags = entity.getAsString(TAGS).split(COMMA);
		rootNode(categories(tags));
	}
	
	public String updatePayload(){
		generateUpdatePayload();
		return serializeToString();
	}

	public String createPayload(){
		generateCreatePayload();
		return serializeToString();
	}
	
	public String tagsPayload(){
		generateTagsPayload();
		return serializeToString();
	}
	
	@Override
	protected Node entry() {
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.APP.getNSPrefix(), Namespace.APP.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.THR.getNSPrefix(), Namespace.THR.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.FH.getNSPrefix(), Namespace.FH.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.SNX.getNSPrefix(), Namespace.SNX.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.OPENSEARCH.getNSPrefix(), Namespace.OPENSEARCH.getUrl());
		Node root = rootNode(element);
		return root;
	}
	
	protected Element profile() {
		return category(Namespace.TYPE.getUrl(), PROFILE);
	}

	protected Element contentCreate() {
		Element element = element(CONTENT,
				attribute(TYPE, APPLICATION_XML));
		element.appendChild(person());
		return element;
	}

	protected Element contentUpdate() {
		Element element = element(CONTENT,
				attribute(TYPE, TEXT));
		addText(element, vcard());
		return element;
	}

	private String vcard(){
		StringBuilder sb = new StringBuilder();
		sb.append(BEGIN_VCARD).append(NL)
		  .append(VCARD_V21).append(NL);
		
		for(VCardField field : VCardField.values()){
			if (field.name().equalsIgnoreCase(VCardField.WORK_LOCATION.name())){
				String value = vcardAddressAttribute();
				if (StringUtil.isNotEmpty(value)){
					sb.append(value);
				}
			}
			String value = entity.getAsString(field.getEntityValue());
			if (StringUtil.isEmpty(value)){
				ProfileXPath xpath = ProfileXPath.getByName(field.getEntityValue());
				if (xpath != null){
					value = entity.getAsString(xpath);
				}
			}
			if (StringUtil.isNotEmpty(value)){
				sb.append(field.getVCardValue()).append(COLON).append(value).append(NL);
			}
		}
		sb.append(END_VCARD).append(NL);
		return sb.toString();
	}

	private String vcardAddressAttribute(){
		String[] addressParts = {LOCALITY, REGION, POSTALCODE, COUNTRYNAME};
		String streetAddress = entity.getAsString(STREETADRESS);
		String extendedAddress = entity.getAsString(EXTENDEDADDRESS);
		StringBuilder sb = new StringBuilder(VCARD_ADDR);
		boolean hasAddress = false;
		if (StringUtil.isNotEmpty(streetAddress)){
			sb.append(streetAddress);
			hasAddress = true;
		}
		sb.append(COMMA);
		if (StringUtil.isNotEmpty(extendedAddress)){
			sb.append(entity.getAsString(EXTENDEDADDRESS));
			hasAddress = true;
		}
		sb.append(SEMICOLON);
		for (String part : addressParts) {
			String value = entity.getAsString(part);
			if (StringUtil.isNotEmpty(value)){
				sb.append(value);
				hasAddress = true;
			}
			sb.append(SEMICOLON);
		}
		return hasAddress?sb.toString():EMPTY;
	}
	
	protected Element person(){
		Element element = element(Namespace.OPENSOCIAL.getUrl(), PERSON);
		element.appendChild(attrib());
		return element;
	}

	protected Element attrib(){
		Element element = element(SNX_ATTRIB);
		for (ProfileAttribute attr : ProfileAttribute.values()){
			appendChildren(element, attributeEntry(attr.getAtomName(), entity.getAsString(attr.getEntityName())));
		}
		return element;
	}

	protected Element categoryTag(String tagTerm) {
		return element(Namespace.ATOM.getUrl(), CATEGORY, 
				attribute(TERM, tagTerm));
	}
	
	protected Element[] tagElements(String[] tags){
		Element[] elements = new Element[tags.length];
		int i = 0;
		for (String tag : tags) {
			elements[i++] = categoryTag(tag);
		}
		return elements;
	}

	protected Element categories(String[] tags) {
		Element element = element(Namespace.APP.getUrl(), CATEGORIES);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.ATOM.getNSPrefix(), Namespace.ATOM.getUrl());
		appendChildren(element, 
				tagElements(tags));
		return element;
	}
	
	private Element attributeEntry(String attributeName, String attributeValue){
		Element entry = element(ENTRY);
		
		appendChildren(entry,
				key(attributeName),
				value(attributeValue)
		);
		return entry;
	}
	
	private Element key(String attributeName){
		return textElement(KEY, attributeName);
	}
	
	private Element value(String attributeValue){
		Element element = element(VALUE);
		appendChildren(element, 
							text(),
							data(attributeValue));
		return element;
	}
	
	private Element text(){
		return textElement(TYPE, TEXT);
	}

	private Element data(String attributeValue){
		return textElement(DATA, attributeValue);
	}

}
