package com.ibm.sbt.services.client.connections.profiles.serializers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.profiles.Profile;

public class ProfileSerializer extends AtomEntitySerializer<Profile> {

	protected static final String PROFILE = "profile";
	protected static final String PERSON = "person";
	protected static final String CATEGORIES = "categories";
	protected static final String SNX_ATTRIB = "com.ibm.snx_profiles.attrib";
	protected static final String SNX_GUID = "com.ibm.snx_profiles.base.guid";
	protected static final String SNX_EMAIL = "com.ibm.snx_profiles.base.email";
	protected static final String SNX_UID = "com.ibm.snx_profiles.base.uid";
	protected static final String SNX_DISTINGUISHEDNAME = "com.ibm.snx_profiles.base.distinguishedName";
	protected static final String SNX_DISPLAYNAME = "com.ibm.snx_profiles.base.displayName";
	protected static final String SNX_GIVENNAMES = "com.ibm.snx_profiles.base.givenNames";
	protected static final String SNX_SURNAME = "com.ibm.snx_profiles.base.surname";
	protected static final String SNX_USERSTATE = "com.ibm.snx_profiles.base.userState";
	protected static final String GUID = "guid";
	protected static final String DISTINGUISHEDNAME = "distinguishedName";
	protected static final String DISPLAYNAME = "displayName";
	protected static final String GIVENNAMES = "givenNames";
	protected static final String SURNAME = "surname";
	protected static final String USERSTATE = "userState";
	protected static final String BEGIN_VCARD = "BEGIN:VCARD";
	protected static final String END_VCARD = "END:VCARD";
	protected static final String VCARD_V21 = "VERSION:2.1";
	protected static final String JOBTITLE = "jobTitle";
	protected static final String ADDRESS = "address";
	protected static final String TELEPHONENUMBER = "telephoneNumber";
	protected static final String BUILDING = "building";
	protected static final String FLOOR = "floor";
	protected static final String STREETADRESS = "streetAddress";
	protected static final String EXTENDEDADDRESS = "extendedAddress";
	protected static final String LOCALITY = "locality";
	protected static final String REGION = "region";
	protected static final String POSTALCODE = "postalCode";
	protected static final String COUNTRYNAME = "countryName";
	protected static final String VCARD_ADDR = "ADR;WORK:;;";
	protected static final String NL = "\n";

	public ProfileSerializer(Profile entity) {
		super(entity);
	}

	public void generateCreatePayload() {
		Node entry = entry();
		
		appendChildren(entry,
				category(),
				contentCreate()
		);
	}

	public void generateUpdatePayload() {
		Node entry = entry();
		
		appendChildren(entry,
				category(),
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
	
	protected Element category() {
		return element(CATEGORY, 
				attribute(SCHEME, Namespace.TYPE.getUrl()), 
				attribute(TERM, PROFILE));
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
		return StringUtil.isEmpty(value)?"":new StringBuilder(name).append(COLON).append(value).toString();
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
		return hasAddress?sb.toString():"";
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
