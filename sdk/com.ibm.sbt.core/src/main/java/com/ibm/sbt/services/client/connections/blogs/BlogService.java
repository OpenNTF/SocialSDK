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

package com.ibm.sbt.services.client.connections.blogs;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;
import com.ibm.sbt.services.client.connections.blogs.transformers.BaseBlogTransformer;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.ProfileParams;
import com.ibm.sbt.services.client.connections.profiles.ProfileUrls;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.transformers.TransformerException;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.IFeedHandler;
/**
 * BlogService
 * 
 * @author Swati Singh
 * @author Carlos Manias
 */

public class BlogService extends BaseService {
	
	private static final long serialVersionUID = 2838345461937687654L;
	
	public String contextRoot = "blogs";
	public String defaultHomepageHandle = "homepage";
	public static String BLOG_HOMEPAGE_KEY = "blogHomepageHandle";
	
	/**
	 * Constructor Creates BlogService Object with default endpoint
	 */
	public BlogService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates BlogService Object with the specified endpoint
	 */
	public BlogService(String endpoint) {
		super(endpoint);
		this.setHomepageFromEndpoint(this.getEndpoint());
	}
	
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates BlogService Object with the specified endpoint
	 */
	public BlogService(Endpoint endpoint) {
		super(endpoint);
		this.setHomepageFromEndpoint(this.getEndpoint());
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "blogs";
	}
	
	private void setHomepageFromEndpoint(Endpoint endpoint){
		Map<String, String> serviceMap = endpoint.getServiceMappings();
		if(serviceMap != null){
			String homepage = serviceMap.get(BLOG_HOMEPAGE_KEY);
			if(StringUtil.isNotEmpty(homepage)){
				this.defaultHomepageHandle = homepage;				
			}
		}
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @deprecated use getAllBlogs instead
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getBlogs() throws ClientServicesException{
		return getAllBlogs();
		
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getAllBlogs() throws ClientServicesException{
		return getAllBlogs(null);
		
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @param parameters
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getAllBlogs(Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.ALL_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getBlogEntityList(url, parameters);
	}
	
	
	/**
	 * This method returns My blogs
	 * 
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getMyBlogs() throws ClientServicesException{
		return getMyBlogs(null);
	}
	
	/**
	 * This method returns My blogs
	 * 
	 * @param parameters
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getMyBlogs(Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.MY_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getBlogEntityList(url, parameters);
	}

	/**
	 * This method returns the featured blogs
	 * 
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getFeaturedBlogs() throws ClientServicesException {
		return getFeaturedBlogs(null);
	}
	
	/**
	 * This method returns the featured blogs
	 * 
	 * @param parameters
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getFeaturedBlogs(Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.FEATURED_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getBlogEntityList(url, parameters);
	}
	/**
	 * This method returns the most recent Blog posts
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getAllPosts() throws ClientServicesException {
		return getAllPosts(null);
	}
	
	/**
	 * This method returns the most recent posts
	 * 
	 * @param parameters
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getAllPosts(Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.ALL_BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getBlogPostEntityList(url, parameters);
	}
	
	/**
	 * This method returns the featured posts
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getFeaturedPosts() throws ClientServicesException {
		return getFeaturedPosts(null);
	}
	
	/**
	 * This method returns the featured posts
	 * 
	 * @param parameters
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getFeaturedPosts(Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.ALL_FEATURED_BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getBlogPostEntityList(url, parameters);
	}
	
	/**
	 * This method returns the recommended posts
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getRecommendedPosts() throws ClientServicesException {
		return getRecommendedPosts(null);
	}
	
	/**
	 * This method returns the recommended posts
	 * 
	 * @param parameters
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getRecommendedPosts(Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.ALL_RECOMMENDED_BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getBlogPostEntityList(url, parameters);
	}
	
	/**
	 * This method returns the latest comments on blogs
	 * 
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getAllComments() throws ClientServicesException{
		return getAllComments(null);
	}
	
	/**
	 * This method returns the latest comments on blogs
	 * 
	 * @param parameters
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getAllComments(Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.ALL_BLOG_COMMENTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getCommentEntityList(url, parameters);
	}
	
	/**
	 * This method returns the tags on all blogs
	 * 
	 * @return EntityList<Tag>
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getAllTags() throws ClientServicesException {
		String url = BlogUrls.ALL_BLOG_TAGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getTagEntityList(url, null);
	}
	
	/**
	 * This method returns the most recent posts for a particular Blog
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getBlogPosts(String blogHandle) throws ClientServicesException {
		return getBlogPosts(blogHandle, null);
	}
	
	/**
	 * This method returns the most recent posts for a particular Blog
	 * 
	 * @param blogHandle
	 * @param parameters
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getBlogPosts(String blogHandle, Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getBlogPostEntityList(url, parameters);
	}
	
	/**
	 * This method returns the latest comments for a particular Blog
	 * 
	 * @param blogHandle
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getBlogComments(String blogHandle) throws ClientServicesException {
		return getBlogComments(blogHandle, null);
	}
	
	/**
	 * This method returns the latest comments for a particular Blog
	 * 
	 * @param blogHandle
	 * @param parameters
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getBlogComments(String blogHandle, Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.BLOG_COMMENTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getCommentEntityList(url, parameters);
	}
	
	/**
	 * This method returns the comments for a particular Blog entry
	 * 
	 * @param blogHandle
	 * @param entryAnchor
	 * @param parameters
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getEntryComments(String blogHandle, String entryAnchor, Map<String, String> parameters) throws ClientServicesException {
		String url = BlogUrls.BLOG_ENTRYCOMMENTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getCommentEntityList(url, parameters);
	}
	
	/**
	 * This method returns the tags for a particular blog
	 * 
	 * @param blogHandle
	 * @return EntityList<Tag>
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getBlogTags(String blogHandle) throws ClientServicesException {
		String url = BlogUrls.BLOG_TAGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getTagEntityList(url, null);
	}
	
	/**
	 * Wrapper method to create a Blog
	 *
	 * User should be authenticated to call this method
	 * 
	 * @param Blog
	 * @return Blog
	 * @throws ClientServicesException
	 */
	public Blog createBlog(Blog blog) throws ClientServicesException {
		if (null == blog){
			throw new ClientServicesException(null,"null blog");
		}
		Response result = null;
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(blog);
			Object 	payload = transformer.transform(blog.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String createBlogUrl = BlogUrls.MY_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			result = createData(createBlogUrl, null, headers, payload);
			blog = getBlogFeedHandler().createEntity(result);
		} catch (IOException e) {
			throw new ClientServicesException(e, "error creating blog post");
		} catch (TransformerException e) {
			throw new ClientServicesException(e, "error creating blog post");
		}

        return blog;
	}
	/**
	 * Wrapper method to get a Blog 
	 * 
	 * @param Blog
	 * @throws ClientServicesException
	 */
	public Blog getBlog(String blogUuid) throws ClientServicesException {
	
		if(StringUtil.isEmpty(blogUuid)){
			throw new ClientServicesException(null, "null blogUuid");
		}
		String url = BlogUrls.GET_UPDATE_REMOVE_BLOG.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle), BlogUrlParts.entryAnchor.get(blogUuid));		
		return getBlogEntity(url, null);
	}
	/**
	 * Wrapper method to update a Blog 
	 * 
	 * @param Blog
	 * @throws ClientServicesException
	 */
	public void updateBlog(Blog blog) throws ClientServicesException {
		if (null == blog){
			throw new ClientServicesException(null,"null blog");
		}
		try {
			if(blog.getFieldsMap().get(AtomXPath.title)== null)
				blog.setTitle(blog.getTitle());
			if(blog.getFieldsMap().get(AtomXPath.summary)== null)
				blog.setSummary(blog.getSummary());
			if(!blog.getFieldsMap().toString().contains(BlogXPath.tags.toString()))
				blog.setTags(blog.getTags());

			BaseBlogTransformer transformer = new BaseBlogTransformer(blog);
			Object 	payload = transformer.transform(blog.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String updateBlogUrl = BlogUrls.GET_UPDATE_REMOVE_BLOG.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle), BlogUrlParts.entryAnchor.get(blog.getUid()));
			// not using super.updateData, as unique id needs to be provided, along with passing params, since no params
			//is passed, it'll throw NPE in BaseService updateData - check with Manish
			getClientService().put(updateBlogUrl, null,headers, payload,ClientService.FORMAT_NULL);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error updating blog");
		}

	}
	
	/**
	 * Wrapper method to delete a post
	 * User should be logged in as a owner of the Blog to call this method.
	 * 
	 * @param String
	 * 				blogUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeBlog(String blogUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(blogUuid)){
			throw new ClientServicesException(null, "null blog Uuid");
		}
		try {
			String deleteBlogUrl = BlogUrls.GET_UPDATE_REMOVE_BLOG.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle), BlogUrlParts.entryAnchor.get(blogUuid));
			getClientService().delete(deleteBlogUrl);
		} catch (Exception e) {
			throw new ClientServicesException(e,"error deleting blog");
		} 	
	}
	
	/**
	 * Wrapper method to get a particular Blog Post
	 * 
	 * @param blogHandle
	 * @param postUuid
	 * @return BlogPost
	 * @throws ClientServicesException
	 */
	public BlogPost getBlogPost(String blogHandle, String postUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new ClientServicesException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postUuid)){
			throw new ClientServicesException(null,"postUuid is null");
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("entryid", postUuid);
			
		String url = BlogUrls.BLOG_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle));
		return getBlogPostEntity(url, params);
	}
	/**
	 * Wrapper method to recommend/like a Blog Post
	 * User should be authenticated to call this method
	 * 
	 * @param blogHandle
	 * @param postUuid
	 * @return BlogPost
	 * @throws ClientServicesException
	 */
	public void recommendPost(String blogHandle, String postUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new ClientServicesException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postUuid)){
			throw new ClientServicesException(null,"postUuid is null");
		}
		try {
			String recommendPostUrl = BlogUrls.RECOMMEND_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(postUuid));
			super.createData(recommendPostUrl, null, null);
		} catch (Exception e) {
			throw new ClientServicesException(e, "error recommending blog post");
		}
	}
	
	/**
	 * Wrapper method to unrecommend/unlike a Blog Post
	 * User should be authenticated to call this method
	 * 
	 * @param blogHandle
	 * @param postUuid
	 * @return BlogPost
	 * @throws ClientServicesException
	 */
	public void unrecommendPost(String blogHandle, String postUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new ClientServicesException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postUuid)){
			throw new ClientServicesException(null,"postUuid is null");
		}
		try {
			String recommendPostUrl = BlogUrls.RECOMMEND_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(postUuid));
			super.deleteData(recommendPostUrl, null, null);
		} catch (Exception e) {
			throw new ClientServicesException(e, "error unrecommending blog post");
		}
	}
	/**
	 * Wrapper method to create a Blog Post
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * @param BlogPost
	 * @param blogHandle
	 * @return BlogPost
	 * @throws ClientServicesException
	 */
	public BlogPost createBlogPost(BlogPost post, String blogHandle) throws ClientServicesException {
		if (null == post){
			throw new ClientServicesException(null,"null post");
		}
		if (StringUtil.isEmpty(blogHandle)){
			throw new ClientServicesException(null,"blog handle is not passed");
		}
		Response result = null;
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(post);
			Object 	payload = transformer.transform(post.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String createPostUrl = BlogUrls.CREATE_BLOG_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle));
			result = createData(createPostUrl, null, headers, payload);
			post = getBlogPostFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating blog post");
		}
		
        return post;
	}
	
	/**
	 * Wrapper method to update a Blog Post
	 * 
	 * @param BlogPost
	 * @param blogHandle
	 * @throws ClientServicesException
	 */
	public BlogPost updateBlogPost(BlogPost post, String blogHandle) throws ClientServicesException {
		if (null == post){
			throw new ClientServicesException(null,"null post");
		}
		try {
			if(post.getFieldsMap().get(AtomXPath.title)== null)
				post.setTitle(post.getTitle());
			if(post.getFieldsMap().get(AtomXPath.content)== null)
				post.setContent(post.getContent());
			if(!post.getFieldsMap().toString().contains(BlogXPath.tags.toString()))
				post.setTags(post.getTags());

			BaseBlogTransformer transformer = new BaseBlogTransformer(post);
			Object 	payload = transformer.transform(post.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String updatePostUrl = BlogUrls.UPDATE_REMOVE_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(post.getUid()));
			Response result = getClientService().put(updatePostUrl, null,headers, payload,ClientService.FORMAT_NULL);
			post = getBlogPostFeedHandler().createEntity(result);
		} catch (Exception e) {
			throw new ClientServicesException(e, "error updating blog post");
		}

		return post;
	}
	
	/**
	 * Wrapper method to delete a post
	 * User should be logged in as a owner of the Blog to call this method.
	 * 
	 * @param String
	 * 				postUuid which is to be deleted
	 * @param blogHandle
	 * @throws ClientServicesException
	 */
	public void removeBlogPost(String postUuid, String blogHandle) throws ClientServicesException {
		if (StringUtil.isEmpty(postUuid)){
			throw new ClientServicesException(null, "null post id");
		}
		try {
			String deletePostUrl = BlogUrls.UPDATE_REMOVE_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(postUuid));
			getClientService().delete(deletePostUrl);
		} catch (Exception e) {
			throw new ClientServicesException(e,"error deleting post");
		} 	
	}
	

	/**
	 * Wrapper method to get a particular Blog Comment
	 * 
	 * @param blogHandle
	 * @param commentUuid
	 * @return Comment
	 * @throws ClientServicesException
	 */
	public Comment getBlogComment(String blogHandle, String commentUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new ClientServicesException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(commentUuid)){
			throw new ClientServicesException(null,"commentUuid is null");
		}
		String url = BlogUrls.GET_REMOVE_COMMENT.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(commentUuid));
		return getCommentEntity(url, null);
	}
	
	/**
	 * Wrapper method to create a Blog Comment
	 * User should be authenticated to call this method
	 * 
	 * 
	 * @param Comment
	 * @return Comment
	 * @throws ClientServicesException
	 */
	public Comment createBlogComment(Comment comment, String blogHandle, String postUuid) throws ClientServicesException {
		if (null == comment){
			throw new ClientServicesException(null,"null comment");
		}
		Response result = null;
		try {
			comment.setPostUuid(postUuid);
			BaseBlogTransformer transformer = new BaseBlogTransformer(comment);
			
			Object 	payload = transformer.transform(comment.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String createCommentUrl = BlogUrls.CREATE_COMMENT.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(""));
			
			result = createData(createCommentUrl, null, headers, payload);
			comment = getCommentFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating blog comment");
		}

        return comment;
	}
	
	/**
	 * Wrapper method to remove a particular Blog Comment
	 * 
	 * @param blogHandle
	 * @param commentUuid
	 * @return Comment
	 * @throws ClientServicesException
	 */
	public void removeBlogComment(String blogHandle, String commentUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new ClientServicesException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(commentUuid)){
			throw new ClientServicesException(null,"commentUuid is null");
		}
		try {
			String getCommentUrl = BlogUrls.GET_REMOVE_COMMENT.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(commentUuid));
			getClientService().delete(getCommentUrl);
		} catch (Exception e) {
			throw new ClientServicesException(e, "error deleting blog comment");
		}
	}
	/*
	 * Util methods
	 */
	
	/**
	 * 
	 * @return The blog homepage currently being used in requests to the blog service.
	 */
	public String getHomepageHandle() {
		return defaultHomepageHandle;
	}
	
    /**
     * Change the blog homepage, to be used in all subsequent requests for this service instance. 
     * This follows /blogs in the connections url, and is 'homepage' by default. 
     * 
     * e.g. CONNECTIONS_URL/blogs/homepage
     * 
     * @param defaultHandle
     */
	public void setHomepageHandle(String defaultHandle) {
		this.defaultHomepageHandle = defaultHandle;
	}
	
	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/
	public IFeedHandler<Blog> getBlogFeedHandler() {
        return new AtomFeedHandler<Blog>(this) {
            @Override
            protected Blog entityInstance(BaseService service, Node node, XPathExpression xpath) {
                return new Blog(service, node, nameSpaceCtx, xpath);
            }
        };
    }
	
	public IFeedHandler<BlogPost> getBlogPostFeedHandler() {
        return new AtomFeedHandler<BlogPost>(this) {
            @Override
            protected BlogPost entityInstance(BaseService service, Node node, XPathExpression xpath) {
                return new BlogPost(service, node, nameSpaceCtx, xpath);
            }
        };
    }
	
	public IFeedHandler<Comment> getCommentFeedHandler() {
        return new AtomFeedHandler<Comment>(this) {
            @Override
            protected Comment entityInstance(BaseService service, Node node, XPathExpression xpath) {
                return new Comment(service, node, nameSpaceCtx, xpath);
            }
        };
    }
	
	/**
	 * Factory method to instantiate a FeedHandler for Tags
	 * @return IFeedHandler<Tag>
	 */
	public IFeedHandler<Tag> getTagFeedHandler() {
		return new AtomFeedHandler<Tag>(this) {
			@Override
			protected Tag entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Tag(service, node, nameSpaceCtx, xpath);
			}
		};
	}
	/***************************************************************
	 * Factory methods
	 ****************************************************************/
	protected Blog getBlogEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, getParameters(parameters), getBlogFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected Comment getCommentEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, getParameters(parameters), getCommentFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected BlogPost getBlogPostEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, getParameters(parameters), getBlogPostFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected EntityList<Blog> getBlogEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, getParameters(parameters), getBlogFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected EntityList<BlogPost> getBlogPostEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, getParameters(parameters), getBlogPostFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected EntityList<Comment> getCommentEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, getParameters(parameters), getCommentFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected EntityList<Tag> getTagEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, getParameters(parameters), getTagFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
}
