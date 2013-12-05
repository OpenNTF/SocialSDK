package com.ibm.sbt.services.client.connections.blogs.feedhandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.blogs.Blog;
import com.ibm.sbt.services.client.connections.blogs.BlogList;
import com.ibm.sbt.services.client.connections.blogs.BlogService;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumsXPath;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;


/**
 * Feed handler for Blog Service
 * 
 * @author Swati Singh
 */

public class BlogsFeedHandler implements IFeedHandler  {
	
	private final BlogService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public BlogsFeedHandler(BlogService service){
		this.service = service;
	}

	@Override
	public BaseEntity createEntity(Response response) {
		Node data = (Node)response.getData();
		return createEntityFromData(data);
	}

	@Override
	public BaseEntity createEntityFromData(Object data) {
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)ForumsXPath.singleEntry.getPath() : null;
		BaseBlogEntity blog = new Blog(service, (Node)data, ConnectionsConstants.nameSpaceCtx, expr);
		return blog;
	}

	@Override
	public EntityList<? extends BaseEntity> createEntityList(Response requestData) {
		return new BlogList((Response)requestData, this);
	}

	@Override
	public BaseService getService() {
		return service;
	}

}
