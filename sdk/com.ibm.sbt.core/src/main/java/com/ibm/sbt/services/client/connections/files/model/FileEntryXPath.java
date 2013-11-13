package com.ibm.sbt.services.client.connections.files.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum FileEntryXPath implements FieldEntry {
	Email(".//a:entry/a:author/a:email"),
	UserUuid(".//a:author/snx:userid"),
	UserName(".//a:author/a:name"),
	UserState(".//a:author/snx:userState"),
	ModifierUserState(".//td:modifier/snx:userState"),
	ModifierName(".//td:modifier/a:name"),
	ModifierUserUuid(".//td:modifier/snx:userid"),
	ModifierEmail(".//td:modifier/a:email"),
	FileName("/a:feed/a:entry/a:title"),
	FileUuid("/a:feed/a:entry/td:uuid"),
	DownloadUrl(".//a:link[@rel=\"edit-media\"]/@href"),
	Comment(".//a:content"),
	Entry("/a:feed/a:entry"),
	Uuid(".//td:uuid"),
	Lock(".//td:lock/@type"),
	Label(".//td:label"),
	Category(".//a:category/@label"),
	Modified(".//td:modified"),
	Visibility(".//td:visibility"),
	LibraryType(".//td:libraryType"),
	VersionUuid(".//td:versionUuid"),
	RestrictedVisibility(".//td:restrictedVisibility"),
	Title(".//a:title"),
	TotalResults("/a:feed/opensearch:totalResults"),
	Published(".//a:published"),
	Updated(".//a:updated"),
	Created(".//td:created"),
	LastAccessed(".//td:lastAccessed"),
	LibraryId(".//td:libraryId"),
	VersionLabel(".//td:versionLabel"),
	Propagation(".//td:propagation"),
	TotalMediaSize(".//td:totalMediaSize"),
	ObjectTypeId(".//td:objectTypeId"),
	DeleteModeration(".//a:link[@rel=\"edit\"]/@href"),
	DeleteWithRecord(".//td:deleteWithRecord"),
	Language(".//td:language"),
	Content(".//a:content[@type='text']"),
	RecommendationsCount(".//snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']"),
	CommentsCount(".//snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']"),
	SharesCount(".//snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/share']"),
	FoldersCount( ".//snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/collections']"),
	AttachmentsCount(".//snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/attachments']"),
	VersionsCount(".//snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/versions']"),
	ReferencesCount(".//snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/references']"),
	Acls("td:permissions"),
	HitCount("snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']"),
	AnonymousHitCount("snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/anonymous_hit']"),
	Tags(".//a:category/@term"),
	Summary(".//a:summary"),
	ContentUrl(".//a:content/@src"),
	ContentType(".//a:content/@type"),
	SelfUrl(".//a:link[@rel='self']/@href"),
    AlternateUrl(".//a:link[@rel='alternate']/@href"),
    Type(".//a:link[@rel='enclosure']/@type"),
	EditLink(".//a:link[@rel='edit']/@href"),
	EditMediaLink(".//a:link[@rel='edit-media']/@href"),
	ThumbnailUrl(".//a:link[@rel='thumbnail']/@href"),
	CommentsUrl(".//a:link[@rel='replies']/@href");
	
	
	private final XPathExpression path;
	
	private FileEntryXPath(String xpath) {
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
