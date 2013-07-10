package com.ibm.sbt.services.client.connections.files;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum FileEntryXPath implements FieldEntry {
	Email("/a:feed/a:author/a:email"),
	EmailFromEntry(".//a:entry/a:author/a:email"),
	UserUuidFromEntry(".//a:author/snx:userid"),
	NameOfUserFromEntry(".//a:author/a:name"),
	UserStateFromEntry(".//a:author/snx:userState"),

	UserStateModifier(".//td:modifier/snx:userState"),
	NameModifier(".//td:modifier/a:name"),
	UserUuidModifier(".//td:modifier/snx:userid"),
	EmailModifier(".//td:modifier/a:email"),

	FileName("/a:feed/a:entry/a:title"),
	FileUuid("/a:feed/a:entry/td:uuid"),
	DownLinkFromEntry(".//a:link[@rel=\"edit-media\"]/@href"),
	CommentFromEntry(".//a:content"),
	Comment("/a:feed/a:entry/a:content"),
	Entry("/a:feed/a:entry"),
	UuidFromEntry(".//td:uuid"),
	LockFromEntry(".//td:lock/@type"),
	LabelFromEntry(".//td:label"),
	CategoryFromEntry(".//a:category/@label"),
	ModifiedFromEntry(".//td:modified"),

	VisibilityFromEntry(".//td:visibility"),
	LibraryTypeFromEntry(".//td:libraryType"),
	VersionUuidFromEntry(".//td:versionUuid"),
	SummaryFromEntry(".//a:summary"),
	RestrictedVisibilityFromEntry(".//td:restrictedVisibility"),
	TitleFromEntry(".//a:title"),
	TotalResults("/a:feed/opensearch:totalResults"),
	PublishedFromEntry(".//a:published"),
	UpdatedFromEntry(".//a:updated"),
	CreatedFromEntry(".//td:created"),
	LastAccessedFromEntry(".//td:lastAccessed"),

	LibraryIdFromEntry(".//td:libraryId"),
	VersionLabelFromEntry(".//td:versionLabel"),
	PropagationFromEntry(".//td:propagation"),
	TotalMediaSizeFromEntry(".//td:totalMediaSize"),
	ObjectTypeIdFromEntry(".//td:objectTypeId"),
	DeleteModeration(".//a:link[@rel=\"edit\"]/@href");
	
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
