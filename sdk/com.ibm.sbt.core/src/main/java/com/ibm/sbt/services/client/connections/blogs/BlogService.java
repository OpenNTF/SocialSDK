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

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;
import com.ibm.sbt.services.client.connections.blogs.serializers.BlogCommentSerializer;
import com.ibm.sbt.services.client.connections.blogs.serializers.BlogPostSerializer;
import com.ibm.sbt.services.client.connections.blogs.serializers.BlogSerializer;
import com.ibm.sbt.services.endpoints.Endpoint;
/**
 * BlogService
 * 
 * @author Swati Singh
 * @author Carlos Manias
 */

public class BlogService extends ConnectionsService {
	
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
	
	//------------------------------------------------------------------------------------------------------------------
	// Getting Blog feeds
	//------------------------------------------------------------------------------------------------------------------

	
	/**
	 * Get a feed that includes all of the blogs hosted by the Blogs application. You can narrow down 
	 * the blogs that are returned by passing parameters to the request that you use to retrieve the feed. 
	 * If you do not specify any input parameters, the server returns all of the blogs that are displayed 
	 * in the Public Blogs>Blogs Listing view from the product user interface.
	 * 
	 * @deprecated use getAllBlogs instead
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getBlogs() throws ClientServicesException{
		return getAllBlogs();
	}
	
	/**
	 * Get a feed that includes all of the blogs hosted by the Blogs application. You can narrow down 
	 * the blogs that are returned by passing parameters to the request that you use to retrieve the feed. 
	 * If you do not specify any input parameters, the server returns all of the blogs that are displayed 
	 * in the Public Blogs>Blogs Listing view from the product user interface.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getAllBlogs() throws ClientServicesException{
		return getAllBlogs(null);
	}
	
	/**
	 * Get a feed that includes all of the blogs hosted by the Blogs application. You can narrow down 
	 * the blogs that are returned by passing parameters to the request that you use to retrieve the feed. 
	 * If you do not specify any input parameters, the server returns all of the blogs that are displayed 
	 * in the Public Blogs>Blogs Listing view from the product user interface.
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
	 * Get a feed that includes my blogs hosted by the Blogs application. You can narrow down 
	 * the blogs that are returned by passing parameters to the request that you use to retrieve the feed. 
	 * If you do not specify any input parameters, the server returns all of the blogs that are displayed 
	 * in the Public Blogs>Blogs Listing view from the product user interface.
	 * 
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getMyBlogs() throws ClientServicesException{
		return getMyBlogs(null);
	}
	
	/**
	 *  Get a feed that includes my blogs hosted by the Blogs application. You can narrow down 
	 * the blogs that are returned by passing parameters to the request that you use to retrieve the feed. 
	 * If you do not specify any input parameters, the server returns all of the blogs that are displayed 
	 * in the Public Blogs>Blogs Listing view from the product user interface.
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
	 * Get the featured blogs feed to find the blogs that have had the most activity across all of the blogs 
	 * hosted by the Blogs application in the past two weeks.
	 * 
	 * @return EntityList<Blog>
	 * @throws ClientServicesException
	 */
	public EntityList<Blog> getFeaturedBlogs() throws ClientServicesException {
		return getFeaturedBlogs(null);
	}
	
	/**
	 * Get the featured blogs feed to find the blogs that have had the most activity across all of the blogs 
	 * hosted by the Blogs application in the past two weeks.
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
	 * Get a feed that lists all of the posts in a specific blog from most recent to oldest.
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getAllPosts() throws ClientServicesException {
		return getAllPosts(null);
	}
	
	/**
	 * Get a feed that lists all of the posts in a specific blog from most recent to oldest.
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
	 * Get the featured posts feed to find the blog posts that have had the most activity 
	 * across all of the blogs hosted by the Blogs application within the past two weeks.
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getFeaturedPosts() throws ClientServicesException {
		return getFeaturedPosts(null);
	}
	
	/**
	 * Get the featured posts feed to find the blog posts that have had the most activity 
	 * across all of the blogs hosted by the Blogs application within the past two weeks.
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
	 * Get a feed that includes all of the recommended blog posts in all of the blogs hosted by the Blogs application.
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getRecommendedPosts() throws ClientServicesException {
		return getRecommendedPosts(null);
	}
	
	/**
	 * Get a feed that includes all of the recommended blog posts in all of the blogs hosted by the Blogs application.
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
	 * Get a feed that includes all of the comments added to the postings in all of the blogs hosted by the 
	 * Blogs application from most recent to oldest.
	 * 
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getAllComments() throws ClientServicesException{
		return getAllComments(null);
	}
	
	/**
	 * Get a feed that includes all of the comments added to the postings in all of the blogs hosted by the 
	 * Blogs application from most recent to oldest.
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
	 * Get a category document that lists the tags that have been assigned to all of 
	 * the blogs hosted by the Blogs application.
	 * 
	 * @return EntityList<Tag>
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getAllTags() throws ClientServicesException {
		String url = BlogUrls.ALL_BLOG_TAGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		return getTagEntityList(url, null);
	}
	
	/**
	 * Get a category document that lists the tags that have been assigned to all of 
	 * the blogs hosted by the Blogs application.
	 * 
	 * @return EntityList<BlogPost>
	 * @throws ClientServicesException
	 */
	public EntityList<BlogPost> getBlogPosts(String blogHandle) throws ClientServicesException {
		return getBlogPosts(blogHandle, null);
	}
	
	/**
	 * Get a feed that lists all of the posts in a specific blog from most recent to oldest.
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
	 * Get a feed that lists the comments added to the posts in a single blog.
	 * 
	 * @param blogHandle
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getBlogComments(String blogHandle) throws ClientServicesException {
		return getBlogComments(blogHandle, null);
	}
	
	/**
	 * Get a feed that lists the comments added to the posts in a single blog.
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
	 * Get a feed that lists the comments added to the posts in a single blog.
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
	 * Get a category document that lists the tags that have been assigned to all of the posts 
	 * in an individual blog.
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
	 * Create a blog by sending an Atom entry document containing the new blog to the user's blog feed.
	 * 
	 * @param Blog
	 * @return Blog
	 * @throws ClientServicesException
	 */
	public Blog createBlog(Blog blog) throws ClientServicesException {
		return createBlog(blog, null);
	}
	
	/**
	 * Create a blog by sending an Atom entry document containing the new blog to the user's blog feed.
	 * 
	 * @param activity
	 * @param parameters
	 * @return
	 * @throws ClientServicesException 
	 */
	public Blog createBlog(Blog blog, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = BlogUrls.MY_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
		if (null == blog) {
			throw new ClientServicesException(null,"null blog");
		}
		BlogSerializer serializer = new BlogSerializer(blog);
		String payload = serializer.createPayload();
		try {
			Response response = createData(requestUrl, parameters, getAtomHeaders(), payload);
			blog = getBlogFeedHandler().createEntity(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blog;
	}
	
	/**
	 * Gets a single blog identified by the blog Uuid
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

			BlogSerializer serializer = new BlogSerializer(blog);
			String payload = serializer.updatePayload();
			
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
	 * @param entryid
	 * @return BlogPost
	 * @throws ClientServicesException
	 */
	public BlogPost getBlogPost(String blogHandle, String entryid) throws ClientServicesException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new ClientServicesException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(entryid)){
			throw new ClientServicesException(null,"entry id is null");
		}
		String url = BlogUrls.BLOG_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.getEntryId(entryid));
		return getBlogPostEntity(url, null);
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
	 * Wrapper method to create a Blog Post
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
		BlogPostSerializer serializer = new BlogPostSerializer(post);
		String payload = serializer.createPayload();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/atom+xml");
		String createPostUrl = BlogUrls.CREATE_BLOG_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle));
		result = createData(createPostUrl, null, headers, payload);
		post = getBlogPostFeedHandler().createEntity(result);
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
		if(post.getFieldsMap().get(AtomXPath.title)== null)
			post.setTitle(post.getTitle());
		if(post.getFieldsMap().get(AtomXPath.content)== null)
			post.setContent(post.getContent());
		if(!post.getFieldsMap().toString().contains(BlogXPath.tags.toString()))
			post.setTags(post.getTags());
			
		BlogPostSerializer serializer = new BlogPostSerializer(post);
		String payload = serializer.updatePayload();
		System.out.println(payload);
			
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/atom+xml");
			
		String updatePostUrl = BlogUrls.UPDATE_REMOVE_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(post.getUid()));
		Response result = getClientService().put(updatePostUrl, null,headers, payload,ClientService.FORMAT_NULL);
		post = getBlogPostFeedHandler().createEntity(result);
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
			BlogCommentSerializer serializer = new BlogCommentSerializer(comment);
			String payload = serializer.createPayload();
			System.out.println(payload);
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
        return new AtomFeedHandler<Blog>(this, false) {
            @Override
            protected Blog entityInstance(BaseService service, Node node, XPathExpression xpath) {
                return new Blog(service, node, nameSpaceCtx, xpath);
            }
        };
    }
	
	public IFeedHandler<BlogPost> getBlogPostFeedHandler() {
        return new AtomFeedHandler<BlogPost>(this, false) {
            @Override
            protected BlogPost entityInstance(BaseService service, Node node, XPathExpression xpath) {
                return new BlogPost(service, node, nameSpaceCtx, xpath);
            }
        };
    }
	
	public IFeedHandler<Comment> getCommentFeedHandler() {
        return new AtomFeedHandler<Comment>(this, false) {
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
		return new AtomFeedHandler<Tag>(this, false) {
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
		return getEntity(requestUrl, parameters, getBlogFeedHandler());
	}
	
	protected Comment getCommentEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntity(requestUrl, parameters, getCommentFeedHandler());
	}
	
	protected BlogPost getBlogPostEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntity(requestUrl, parameters, getBlogPostFeedHandler());
	}
	
	protected EntityList<Blog> getBlogEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getBlogFeedHandler());
	}
	
	protected EntityList<BlogPost> getBlogPostEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getBlogPostFeedHandler());
	}
	
	protected EntityList<Comment> getCommentEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getCommentFeedHandler());
	}
	
	protected EntityList<Tag> getTagEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getTagFeedHandler());
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
}
