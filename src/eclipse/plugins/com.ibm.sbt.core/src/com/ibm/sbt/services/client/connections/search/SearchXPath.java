package com.ibm.sbt.services.client.connections.search;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum SearchXPath implements FieldEntry{
	
	entry("/a:entry"),
	uid("./id"),
	title("a:title"),
	content("a:content"),
	updated("a:updated"),
	authorUserid("a:author/snx:userid"),
    authorName("a:author/a:name"),
    authorEmail("a:author/a:email"),
    rank("snx:rank"),
    score("./relevance:score"),
    summary("a:summary"),
    authorState("a:author/snx:userState"),
    type("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term"),
    application("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term"),
    applicationCount("count(a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term)"),
	tags("a:category[not(@scheme)]/@term"),
    primaryComponent("a:category[ibmsc:field[@id='primaryComponent']]/@term"),
    commentCount("snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']"),
    resultLink("a:link[not(@rel)]/@href"),
    bookmarkLink("ibmsc:field[@id='dogearURL']"),
    eventStartDate("ibmsc:field[@id='eventStartDate']"),
    authorJobTitle("a:content/xhtml:div/xhtml:span/xhtml:div[@class='title']"),
    authorJobLocation("a:content/xhtml:div/xhtml:span/xhtml:div[@class='location']"),
    authorCount("count(a:contributor)"),
    contributorCount("count(a:author)"),
    tagCount("count(a:category[not(@scheme)])"),
    highlightField("ibmsc:field[@id='highlight']"),
    fileExtension("ibmsc:field[@id='fileExtension']"),
    memberCount("snx:membercount"),
    communityUuid("snx:communityUuid"),
    containerType("ibmsc:field[@id='container_type']"),
    communityParentLink("a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container' and @type='text/html']/@href"),
    parentageMetaID("ibmsc:field[contains(@id, 'ID')]/@id"),
    parentageMetaURL("ibmsc:field[contains(@id, 'URL')]"),
    parentageMetaURLID("ibmsc:field[contains(@id, 'URL')]/@id"),
    objectRefDisplayName("ibmsc:field[@id='FIELD_OBJECT_REF_DISPLAY_NAME']"),
    objectRefUrl("ibmsc:field[@id='FIELD_OBJECT_REF_URL']"),
    accessControl("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/accesscontrolled']/@term"),
    commentsSummary("ibmsc:field[@id='commentsSummary']"),
    searchLink("./link");


	private final XPathExpression path;
	
	private SearchXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	
	@Override
	public Object getPath() {
		return path;
	}

	@Override
	public String getName() {
		return this.name();
	}

}
