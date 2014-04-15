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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.APPLICATION_XML;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.BEGIN_VCARD;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.BUILDING;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORIES;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COLON;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COMMA;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COUNTRYNAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.DATA;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.DISPLAYNAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.DISTINGUISHEDNAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.EMPTY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.END_VCARD;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.EXTENDEDADDRESS;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.FLOOR;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.GIVENNAMES;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.GUID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.JOBTITLE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.KEY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LOCALITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.NL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.PERSON;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.POSTALCODE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.PROFILE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.REGION;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SEMICOLON;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_ATTRIB;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_DISPLAYNAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_DISTINGUISHEDNAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_EMAIL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_GIVENNAMES;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_GUID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_SURNAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_UID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_USERSTATE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.STREETADRESS;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SURNAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TAGS;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TELEPHONENUMBER;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TEXT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.USERSTATE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.VALUE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.VCARD_ADDR;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.VCARD_V21;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.profiles.Profile;

/**
 * 
 * @author carlos
 *
 */
public class ProfileSerializer extends AtomEntitySerializer<Profile> {

	public ProfileSerializer(Profile entity) {
		super(entity);
	}

	public void generateCreatePayload() {
		Node entry = entry();
		
		appendChildren(entry,
				profile(),
				contentCreate()
		);
	}

	public void generateUpdatePayload() {
		Node entry = entry();
		
		appendChildren(entry,
				profile(),
				contentUpdate()
		);
	}

	public void generateTagsPayload() {
		String[] tags = entity.getAsString(TAGS).split(COMMA);
		categories(tags);
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
		return vcardParts(	BEGIN_VCARD,
							VCARD_V21,
							vcardAttribute(JOBTITLE),
							vcardAddressAttribute(),
							vcardAttribute(TELEPHONENUMBER),
							vcardAttribute(BUILDING),
							vcardAttribute(FLOOR),
							END_VCARD);
	}

	private String vcardParts(String... parts){
		StringBuilder sb = new StringBuilder();
		
		for (String part : parts) {
			if (StringUtil.isNotEmpty(part)){
				sb.append(part).append(NL);
			}
		}
		return sb.toString();
	}
	
	private String vcardAttribute(String name){
		String value = entity.getAsString(name);
		return StringUtil.isEmpty(value)?EMPTY:new StringBuilder(name).append(COLON).append(value).toString();
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
		appendChildren(element,
							guid(),
							email(),
							uid(),
							distinguishedName(),
							displayName(),
							givenNames(),
							surname(),
							userState());
		return element;
	}
	
	protected Node guid(){
		return attributeEntry(SNX_GUID, entity.getAsString(GUID));
	}

	protected Node email(){
		return attributeEntry(SNX_EMAIL, entity.getEmail());
	}

	protected Node uid(){
		return attributeEntry(SNX_UID, entity.getUserid());
	}

	protected Node distinguishedName(){
		return attributeEntry(SNX_DISTINGUISHEDNAME, entity.getAsString(DISTINGUISHEDNAME));
	}

	protected Node displayName(){
		return attributeEntry(SNX_DISPLAYNAME, entity.getAsString(DISPLAYNAME));
	}

	protected Node givenNames(){
		return attributeEntry(SNX_GIVENNAMES, entity.getAsString(GIVENNAMES));
	}

	protected Node surname(){
		return attributeEntry(SNX_SURNAME, entity.getAsString(SURNAME));
	}

	protected Node userState(){
		return attributeEntry(SNX_USERSTATE, entity.getAsString(USERSTATE));
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
