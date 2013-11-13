package com.ibm.sbt.services.client.connections.profiles.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum ColleagueConnectionXPath implements FieldEntry {
	entry("/a:entry"),
	id("./a:id"),
	title("./a:title"),
	updated("./a:updated"),
	authorName("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/a:name"),
	authorUserId("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/snx:userid"),
	authorEmail("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/a:email"),
	contributorName("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/a:name"),
	contributorUserId("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/snx:userid"),
	contributorEmail("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/a:email"),
	selfLinkFromEntry("./a:link[@rel=\"self\"]/@href"),
	editLinkFromEntry("./a:link[@rel=\"edit\"]/@href"),
	content("./a:content");
	
	private final XPathExpression path;
	
	private ColleagueConnectionXPath(String xpath) {
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
