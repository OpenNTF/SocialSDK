package com.ibm.sbt.services.client.connections.communities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import com.ibm.commons.util.HtmlTextUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;

/**
 * This File represents Community
 * <p>
 * 
 * @Represents Connections Community
 * @author Swati Singh
 */

public class Community {

	static final String					sourceClass		= Community.class.getName();
	static final Logger					logger			= Logger.getLogger(sourceClass);
	static final Map<String, String>	xpathMap;
	private final Map<String, String>	fieldsMap		= new HashMap<String, String>();
	// this holds the values which api consumer sets while creating updating profile Servicename reference
	private CommunityService			communityService;
	private Document					data;
	private String						communityUuid;
	private String 						dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	static NamespaceContext	nameSpaceCtx = new NamespaceContext() {

		@Override
		public String getNamespaceURI(String prefix) {
			String uri;
			if (prefix.equals("h")) {
				uri = "http://www.w3.org/1999/xhtml";
			} else if (prefix.equals("a")) {
				uri = "http://www.w3.org/2005/Atom";
			} else if (prefix.equals("snx")) {
				uri = "http://www.ibm.com/xmlns/prod/sn";
			} else {
				uri = null;
			}
			return uri;
		}

		// Dummy implementation - not used!
		@Override
		public Iterator<String> getPrefixes(String val) {
			return null;
		}

		// Dummy implemenation - not used!
		@Override
		public String getPrefix(String uri) {
			return null;
		}

		@Override
		public Iterator<String> getPrefixes() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	static {
		xpathMap = new HashMap<String, String>();
		String[][] pairs = { { "communityUuid", "/a:entry/snx:communityUuid" },
			{ "title", "/a:entry/a:title" }, { "summary", "/a:entry/a:summary[@type='text']" },
			{ "logoUrl", "/a:entry/a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href" },
			{ "membersUrl", "/a:entry/a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href" },
			{ "communityUrl", "/a:entry/a:link[@rel='self']/@href" },
			{ "tags", "/a:entry/a:category/@term" }, { "content", "/a:entry/a:content[@type='html']" },
			{ "memberCount", "/a:entry/snx:membercount"},
			{ "communityType", "/a:entry/snx:communityType"}, 
			{ "published" , "/a:entry/a:published"},{ "updated"  , "/a:entry/a:updated"},
			{ "authorUid" ,	"/a:entry/a:author/snx:userid"}, {"authorName", "/a:entry/a:author/a:name"},
			{ "authorEmail"	, "/a:entry/a:author/a:email"}, {"contributorUid" , "/a:entry/a:contributor/snx:userid"},
			{ "contributorName"	,"/a:entry/a:contributor/a:name"}, {"contributorEmail", "/a:entry/a:contributor/a:email"}
		};
		for (String[] pair : pairs) {
			xpathMap.put(pair[0], pair[1]);
		}
	}

	// check
	public Community() {
	}

	public Community(CommunityService communityService, String communityUuid) {
		this.communityService = communityService;
		this.communityUuid = communityUuid;
	}

	public Community(String communityUuid) {
		this.communityUuid = communityUuid;
	}

	/**
	 * getData
	 * 
	 * @return Data
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * Method sets the response from the API call to data method for locally referring the data in future.
	 * 
	 * @param data
	 */
	public void setData(Document data) {
		this.data = data;
	}

	public static com.ibm.commons.xml.NamespaceContext getNameSpaceCtx() {
		return nameSpaceCtx;
	}

	public void setNameSpaceCtx(com.ibm.commons.xml.NamespaceContext nameSpaceCtx) {
		this.nameSpaceCtx = nameSpaceCtx;
	}

	public Map<String, String> getFieldsMap() {
		return fieldsMap;
	}
	
	/**
	 * getCommunityUuid
	 * 
	 * @return communityUuid
	 */
	public String getCommunityUuid() {
		return communityUuid;
	}

	/**
	 * @sets the communityUuid
	 * 
	 * @param communityUuid
	 */
	public void setCommunityUuid(String communityUuid) {
		this.communityUuid = communityUuid;
	}

	/**
	 * getTitle
	 * 
	 * @return title
	 */
	public String getTitle() {

		if (StringUtil.isEmpty(fieldsMap.get("title"))) {
			return get("title");
		} else {
			return fieldsMap.get("title");
		}
	}
	
	/**
	 * @sets the title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		fieldsMap.put("title", title);
	}

	/**
	 * getContent
	 * 
	 * @return content
	 */
	public String getContent() {

		if (StringUtil.isEmpty(fieldsMap.get("content"))) {
			return get("content");
		} else {
			return fieldsMap.get("content");
		}

	}

	/**
	 * getCommunityUrl
	 * 
	 * @return communityUrl
	 */
	public String getCommunityUrl() {
		return get("communityUrl");
	}

	/**
	 * getLogoUrl
	 * 
	 * @return logoUrl
	 */
	public String getLogoUrl() {
		return get("logoUrl");
	}

	/**
	 * getSummary
	 * 
	 * @return summary
	 */
	public String getSummary() {
		return get("summary");
	}

	/**
	 * @sets the content
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		fieldsMap.put("content", content);
	}

	/**
	 * @sets the tag
	 * @param addedTags
	 */
	public void setAddedTags(String addedTags) {
		fieldsMap.put("addedTags", addedTags);
	}

	/**
	 * @return the array of Tags
	 * @throws XMLException
	 */
	public String[] getTags() throws XMLException {
		String xpQuery = getXPathQuery("tags");
		String[] categoryFields = DOMUtil.value(this.data, xpQuery, nameSpaceCtx).split(" ");
		String[] tags = new String[categoryFields.length - 1];
		for (int i = 1; i < categoryFields.length; i++) {// remove occurence of category term which has scheme
															// of "http://www.ibm.com/xmlns/prod/sn/type"
			tags[i - 1] = categoryFields[i];
		}
		return tags;
	}

	/**
	 * @sets the tag
	 */
	public void setDeletedTags(String deletedTags) {
		fieldsMap.put("deletedTags", deletedTags);
	}
	
	/**
	 * @return the memberCount
	 */
	public String getMemberCount(){
		return get("memberCount");
	}
	/**
	 * @return the communityType
	 */
	public String getCommunityType(){
		return get("communityType");		
	}
	/**
	 * @return the authorUid
	 */
	public String getAuthorId(){
		return get("authorUid");
	}
	/**
	 * @return the ContributorId
	 */
	public String getContributorId(){

		return get("contributorUid");
	}
	
	/**
	 * @return the published date of community
	 */
	public Date getPublished(){
		Date publishedDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			publishedDate = (Date)formatter.parse(get("published"));
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Error in parsing date");;
		}
		return publishedDate;
	}
	
	/**
	 * @return the update date of community
	 */
	public Date getUpdated(){
		Date updatedDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			updatedDate = (Date)formatter.parse(get("updated"));
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Error in parsing date");
		}
		return updatedDate;
	}
	/**
	 * @return the authorUid
	 */
	public Member getAuthor(){
		Member author = new Member(this.communityService,this.get("authorUid"),this.get("authorName"),this.get("authorEmail"));
		return author;
		
	}
	/**
	 * @return the ContributorId
	 */
	public Member getContributor(){
		Member contributor = new Member(this.communityService,this.get("contributorUid"),this.get("contributorName"),this.get("contributorEmail"));
		return contributor;
	}
	/**
	 * @return the membersUrl
	 */
	public String getMembersUrl() {
		return get("membersUrl");
	}

	/**
	 * @return value for specified field. Field names follow IBM Connections naming convention
	 */
	public String get(String fieldName) {
		String xpQuery = getXPathQuery(fieldName);
		return getFieldUsingXPath(xpQuery);
	}

	/**
	 * @return xpath query for specified field. Field names follow IBM Connections naming convention
	 */
	public String getXPathQuery(String fieldName) {
		return xpathMap.get(fieldName);
	}

	/**
	 * @return Execute xpath query on Community XML
	 */
	public String getFieldUsingXPath(String xpathQuery) {
		try {
			return DOMUtil.value(this.data, xpathQuery, nameSpaceCtx);
		} catch (XMLException e) {
			logger.log(Level.SEVERE, "Error executing xpath query on Community XML", e);
			return null;
		}
	}

	public void clearFieldsMap() {
		fieldsMap.clear();
	}
	
	/**
	 * This method is used by communityService wrapper methods to construct request body for Add/Update operations
	 * @return Object
	 */
	public Object constructCreateRequestBody() {

		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">";

		Iterator<Map.Entry<String, String>> entries = fieldsMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> fieldMapPairs = entries.next();
			// title and content are mandatory fields , if not provided in the update request then retrieved
			// values will be used
			if (fieldMapPairs.getKey().equalsIgnoreCase("title") || !(body.contains("<title type=\"text\">"))) {
				body += "<title type=\"text\">" + escapeHtmlSpecialChars(getTitle()) + "</title>";
			}

			if (fieldMapPairs.getKey().equalsIgnoreCase("content") || !(body.contains("<content type=\"html\">"))) {
				body += "<content type=\"html\">" + escapeHtmlSpecialChars(getContent()) + "</content>";
			}

			if (fieldMapPairs.getKey().equalsIgnoreCase("addedTags")) {

				String[] originalTags;
				try {
					originalTags = getTags();
					for (String originalTag : originalTags) {// add original tags in the request
						body += "<category term=\"" + originalTag + "\"/>";
					}
					body += "<category term=\"" + fieldMapPairs.getValue() + "\"/>";

				} catch (XMLException e) {
					logger.log(Level.SEVERE, "Error creating request body", e);
				}
			}
			if (fieldMapPairs.getKey().equalsIgnoreCase("deletedTags")) {
				String[] originalTags;
				try {
					originalTags = getTags();
					for (int i = 0; i < originalTags.length; i++) {
						if (!fieldMapPairs.getValue().equalsIgnoreCase(originalTags[i])) {
							body += "<category term=\"" + originalTags[i] + "\"/>";
						}
					}
				} catch (XMLException e) {
					logger.log(Level.SEVERE, "Error creating request body", e);
				}
			}
		}// end while

		body += "<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><snx:communityType xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">public</snx:communityType></entry>";

		return body;

	}

	@Override
	public String toString() {
		try {
			return DOMUtil.getXMLString(data);
		} catch (XMLException e) {
			logger.log(Level.SEVERE, "Error converting xml to String", e);
			return "";
		}
	}
	
	private String escapeHtmlSpecialChars(String content){
		String contentStr = HtmlTextUtil.toHTMLContentString(content, false);
		return contentStr;
	}

}
