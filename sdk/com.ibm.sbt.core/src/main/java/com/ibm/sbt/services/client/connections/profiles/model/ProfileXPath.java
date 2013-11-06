package com.ibm.sbt.services.client.connections.profiles.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum ProfileXPath implements FieldEntry {
	entry("/a:feed/a:entry"),
	uid("./a:contributor/snx:userid"),
	name("./a:contributor/a:name"),
	email("./a:contributor/a:email"),
    groupwareMail("./a:content/h:div/h:span/h:div[@class='x-groupwareMail']"),
	photo("./a:content/h:div/h:span/h:div/h:img[@class='photo']/@src"),
	jobTitle("./a:content/h:div/h:span/h:div[@class='title']"),
	organizationUnit("./a:content/h:div/h:span/h:div[@class='org']/h:span[@class='organization-unit']"),
	fnUrl("./a:content/h:div/h:span/h:div/h:a[@class='fn url']/@href"),			
	telephoneNumber("./a:content/h:div/h:span/h:div[@class='tel']/h:span[@class='value']"),			
	building("./a:content/h:div/h:span/h:div/h:span[@class='x-building']"),			
	floor("./a:content/h:div/h:span/h:div/h:span[@class='x-floor']"),
	streetAddress("./a:content/h:div/h:span/h:div/h:div[@class='street-address']"),
	extendedAddress("./a:content/h:div/h:span/h:div/h:div[@class='extended-address x-streetAddress2']"),
	locality("./a:content/h:div/h:span/h:div/h:span[@class='locality']"),
	postalCode("./a:content/h:div/h:span/h:div/h:span[@class='postal-code']"),
	region("./a:content/h:div/h:span/h:div/h:span[@class='region']"),
	countryName("./a:content/h:div/h:span/h:div/h:div[@class='country-name']"),			
	soundUrl("./a:content/h:div/h:span/h:div/h:a[@class='sound url']/@href"),	
	summary("./a:summary"),
	tagEntry("/app:categories/a:category"),
	targetEmail("app:categories/snx:targetEmail"),
	numberOfContributors("/app:categories/@snx:numberOfContributors"),
	term("./@term"),
	frequency("./@snx:frequency"),
	intensity("./@snx:intensityBin"), 
	visibility("./@snx:visibilityBin"),
	contributorName("./a:name"),
	contributorUserid("./a:userid"),
	contributorEmail("./a:email");
	
	private final XPathExpression path;
	
	private ProfileXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	@Override
	public XPathExpression getPath() {
		return path;
	}
	
	@Override
	public String getName() {
		return this.name();
	}
}
