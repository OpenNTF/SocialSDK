/*
 * © Copyright IBM Corp. 2013
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.BlogsFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.CommentsFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.PostsFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.TagFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.transformers.BaseBlogTransformer;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;


/**
 * BlogService
 * 
 * @author Swati Singh
 */

public class BlogService extends BaseService {
	
	/**
	 * Used in constructing REST APIs
	 */
	public static final String	BASE_URL				= "/blogs/"; // need to change for Blog Handle
	private static final String BASIC_URL				= "atom/";
	public static final String OAUTH_URL				= "oauth/atom/";

	public static String BLOG_HANDLE 					= "homepage";
	
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
	}
	
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates BlogService Object with the specified endpoint
	 */
	public BlogService(Endpoint endpoint) {
		super(endpoint);
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public BlogList getBlogs() throws BlogServiceException{
		return getBlogs(null);
		
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @param parameters
	 * @return
	 * @throws BlogServiceException
	 */
	public BlogList getBlogs(Map<String, String> parameters) throws BlogServiceException {
		BlogList blogs;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String blogsUrl = resolveUrl(BLOG_HANDLE, FilterType.ALL, null);
			blogs = (BlogList)getEntities(blogsUrl, parameters, new BlogsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogs;
	}
	
	
	/**
	 * This method returns My blogs
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public BlogList getMyBlogs() throws BlogServiceException{
		return getMyBlogs(null);
		
	}
	
	/**
	 * This method returns My blogs
	 * 
	 * @param parameters
	 * @return
	 * @throws BlogServiceException
	 */
	public BlogList getMyBlogs(Map<String, String> parameters) throws BlogServiceException {
		BlogList blogs;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String myBlogsUrl = resolveUrl(BLOG_HANDLE, FilterType.MY, null);
			blogs = (BlogList)getEntities(myBlogsUrl, parameters, new BlogsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogs;
	}

	/**
	 * This method returns the featured blogs
	 * 
	 * @return BlogList
	 * @throws BlogServiceException
	 */
	public BlogList getFeaturedBlogs() throws BlogServiceException{
		return getFeaturedBlogs(null);
		
	}
	
	/**
	 * This method returns the featured blogs
	 * 
	 * @param parameters
	 * @return BlogList
	 * @throws BlogServiceException
	 */
	public BlogList getFeaturedBlogs(Map<String, String> parameters) throws BlogServiceException {
		BlogList blogs;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String featuredBlogsUrl = resolveUrl(BLOG_HANDLE, FilterType.FEATURED_BLOGS, null);
			blogs = (BlogList)getEntities(featuredBlogsUrl, parameters, new BlogsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogs;
	}
	/**
	 * This method returns the most recent Blog posts
	 * 
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList getBlogsPosts() throws BlogServiceException{
		return getBlogsPosts(null);
		
	}
	
	/**
	 * This method returns the most recent Blog posts
	 * 
	 * @param parameters
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList getBlogsPosts(Map<String, String> parameters) throws BlogServiceException {
		PostList posts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String latestPostsUrl = resolveUrl(BLOG_HANDLE, FilterType.BLOGS_POSTS, null);
			posts = (PostList)getEntities(latestPostsUrl, parameters, new PostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return posts;
	}
	
	/**
	 * This method returns the featured Blog posts
	 * 
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList GetFeaturedBlogsPosts() throws BlogServiceException{
		return GetFeaturedBlogsPosts(null);
		
	}
	
	/**
	 * This method returns the featured Blog posts
	 * 
	 * @param parameters
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList GetFeaturedBlogsPosts(Map<String, String> parameters) throws BlogServiceException {
		PostList posts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String featuredPostsUrl = resolveUrl(BLOG_HANDLE, FilterType.FEATURED_BLOGS_POSTS, null);
			posts = (PostList)getEntities(featuredPostsUrl, parameters, new PostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return posts;
	}
	
	/**
	 * This method returns the recommended Blogs posts
	 * 
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList GetRecommendedBlogsPosts() throws BlogServiceException{
		return GetRecommendedBlogsPosts(null);
		
	}
	
	/**
	 * This method returns the recommended Blogs posts
	 * 
	 * @param parameters
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList GetRecommendedBlogsPosts(Map<String, String> parameters) throws BlogServiceException {
		PostList posts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String recommendedPostsUrl = resolveUrl(BLOG_HANDLE, FilterType.RECOMMENDED_BLOGS_POSTS, null);
			posts = (PostList)getEntities(recommendedPostsUrl, parameters, new PostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return posts;
	}
	
	/**
	 * This method returns the latest comments on blogs
	 * 
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getBlogsComments() throws BlogServiceException{
		return getBlogsComments(null);
		
	}
	
	/**
	 * This method returns the latest comments on blogs
	 * 
	 * @param parameters
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getBlogsComments(Map<String, String> parameters) throws BlogServiceException {
		CommentList comments;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String commentsUrl = resolveUrl(BLOG_HANDLE, FilterType.BLOGS_COMMENTS, null);
			comments = (CommentList)getEntities(commentsUrl, parameters, new CommentsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return comments;
	}
	
	/**
	 * This method returns the tags on all blogs
	 * 
	 * @return TagList
	 * @throws BlogServiceException
	 */
	public TagList getBlogsTags() throws BlogServiceException {
		TagList tags;
		try {
			String allTagsUrl = resolveUrl(BLOG_HANDLE, FilterType.BLOGS_TAGS, null);
			tags = (TagList)getEntities(allTagsUrl, null, new TagFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return tags;
	}
	
	/**
	 * This method returns the most recent posts for a particular Blog
	 * 
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList getBlogPosts(String blogHandle) throws BlogServiceException{
		return getBlogPosts(blogHandle, null);
		
	}
	
	/**
	 * This method returns the most recent posts for a particular Blog
	 * 
	 * @param parameters
	 * @return PostList
	 * @throws BlogServiceException
	 */
	public PostList getBlogPosts(String blogHandle, Map<String, String> parameters) throws BlogServiceException {
		PostList posts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String latestPostsUrl = resolveUrl(blogHandle, FilterType.BLOG_POSTS, null);
			posts = (PostList)getEntities(latestPostsUrl, parameters, new PostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return posts;
	}
	
	/**
	 * This method returns the latest comments for a particular Blog
	 * 
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getBlogComments(String blogHandle) throws BlogServiceException{
		return getBlogComments(blogHandle, null);
		
	}
	
	/**
	 * This method returns the latest comments for a particular Blog
	 * 
	 * @param parameters
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getBlogComments(String blogHandle, Map<String, String> parameters) throws BlogServiceException {
		CommentList comments;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String latestCommentsUrl = resolveUrl(blogHandle, FilterType.BLOG_COMMENTS, null);
			comments = (CommentList)getEntities(latestCommentsUrl, parameters, new CommentsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return comments;
	}
	
	/**
	 * This method returns the tags for a particular blog
	 * 
	 * @return TagList
	 * @throws BlogServiceException
	 */
	public TagList getBlogTags(String blogHandle) throws BlogServiceException {
		TagList tags;
		try {
			String allTagsUrl = resolveUrl(blogHandle, FilterType.BLOG_TAGS, null);
			tags = (TagList)getEntities(allTagsUrl, null, new TagFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return tags;
	}
	
	/**
	 * Wrapper method to create a Blog
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * 
	 * @param Blog
	 * @return Blog
	 * @throws BlogServiceException
	 */
	public Blog createBlog(Blog blog) throws BlogServiceException {
		if (null == blog){
			throw new BlogServiceException(null,"null blog");
		}
		Response result = null;
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(blog);
			Object 	payload = transformer.transform(blog.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String createBlogUrl = resolveUrl(BLOG_HANDLE, FilterType.MY, null);
			result = createData(createBlogUrl, null, headers, payload);
			blog = (Blog) new BlogsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error creating blog");
		}

        return blog;
	}
	
	/**
	 * Wrapper method to update a Blog 
	 * <p>
	 * 
	 * @param Post
	 * @param blogHandle
	 * @throws BlogServiceException
	 */
	public void updateBlog(Blog blog) throws BlogServiceException {
		if (null == blog){
			throw new BlogServiceException(null,"null blog");
		}
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(blog);
			Object 	payload = transformer.transform(blog.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String updateBlogUrl = resolveUrl(BLOG_HANDLE, FilterType.UPDATE_REMOVE_BLOG, blog.getUid());
			// not using super.updateData, as unique id needs to be provided, along with passing params, since no params
			//is passed, it'll throw NPE in BaseService updateData - check with Manish
			getClientService().put(updateBlogUrl, null,headers, payload,ClientService.FORMAT_NULL);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error updating blog");
		}

	}
	
	/**
	 * Wrapper method to delete a post
	 * <p>
	 * User should be logged in as a owner of the Blog to call this method.
	 * 
	 * @param String
	 * 				postUuid which is to be deleted
	 * @throws BlogServiceException
	 */
	public void removeBlog(String blogUuid) throws BlogServiceException {
		if (StringUtil.isEmpty(blogUuid)){
			throw new BlogServiceException(null, "null blog id");
		}
		try {
			String deleteBlogUrl = resolveUrl(BLOG_HANDLE, FilterType.UPDATE_REMOVE_BLOG, blogUuid);
			getClientService().delete(deleteBlogUrl);
		} catch (Exception e) {
			throw new BlogServiceException(e,"error deleting post");
		} 	
		
	}
	
	/**
	 * Wrapper method to get a particular Blog Post
	 * <p>
	 * 
	 * @param blogHandle
	 * @param postId
	 * @return Post
	 * @throws BlogServiceException
	 */
	public Post getBlogPost(String blogHandle, String postId) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postId)){
			throw new BlogServiceException(null,"postID is null");
		}
		Post post;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("entryid", postId);
			String getPostUrl = resolveUrl(blogHandle, FilterType.BLOG_POST, null);
			post = (Post)getEntity(getPostUrl, params, new PostsFeedHandler(this));
			
		} catch (Exception e) {
			throw new BlogServiceException(e, "error getting blog post");
		}
        return post;
	}
	
	public void recommendPost(String blogHandle, String postId) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postId)){
			throw new BlogServiceException(null,"postID is null");
		}
		try {
			//String recommendPostUrl = "/blogs/"+blogHandle+"/api/recommend/entries/"+postId;
			String recommendPostUrl = resolveUrl(blogHandle, FilterType.RECOMMEND_POST, postId);
			super.createData(recommendPostUrl, null, null);
		} catch (Exception e) {
			throw new BlogServiceException(e, "error recommending blog post");
		}
		
	}
	
	public void unRecommendPost(String blogHandle, String postId) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postId)){
			throw new BlogServiceException(null,"postID is null");
		}
		try {
		//	String recommendPostUrl = "/blogs/"+blogHandle+"/api/recommend/entries/"+postId;
			String recommendPostUrl = resolveUrl(blogHandle, FilterType.RECOMMEND_POST, postId);
			super.deleteData(recommendPostUrl, null, null);
		} catch (Exception e) {
			throw new BlogServiceException(e, "error un-recommending blog post");
		}
	}
	/**
	 * Wrapper method to create a Blog Post
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * 
	 * @param Post
	 * @return Post
	 * @throws BlogServiceException
	 */
	public Post createBlogPost(Post post, String blogHandle) throws BlogServiceException {
		if (null == post){
			throw new BlogServiceException(null,"null post");
		}
		Response result = null;
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(post);
			Object 	payload = transformer.transform(post.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String createPostUrl = resolveUrl(blogHandle, FilterType.CREATE_BLOG_POST, null);
			result = createData(createPostUrl, null, headers, payload);
			post = (Post) new PostsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error creating blog post");
		}
        return post;
	}
	
	/**
	 * Wrapper method to update a Blog Post
	 * <p>
	 * 
	 * @param Post
	 * @param blogHandle
	 * @throws BlogServiceException
	 */
	public void updateBlogPost(Post post, String blogHandle) throws BlogServiceException {
		if (null == post){
			throw new BlogServiceException(null,"null post");
		}
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(post);
			Object 	payload = transformer.transform(post.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String updatePostUrl = resolveUrl(blogHandle, FilterType.UPDATE_REMOVE_POST, post.getUid());
			getClientService().put(updatePostUrl, null,headers, payload,ClientService.FORMAT_NULL);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error updating blog post");
		}

	}
	
	/**
	 * Wrapper method to delete a post
	 * <p>
	 * User should be logged in as a owner of the Blog to call this method.
	 * 
	 * @param String
	 * 				postUuid which is to be deleted
	 * @throws BlogServiceException
	 */
	public void removeBlogPost(String postUuid, String blogHandle) throws BlogServiceException {
		if (StringUtil.isEmpty(postUuid)){
			throw new BlogServiceException(null, "null post id");
		}

		try {
			String deletePostUrl = resolveUrl(blogHandle, FilterType.UPDATE_REMOVE_POST, postUuid);
			getClientService().delete(deletePostUrl);
		} catch (Exception e) {
			throw new BlogServiceException(e,"error deleting post");
		} 	
		
	}
	

	/**
	 * Wrapper method to get a particular Blog Comment
	 * <p>
	 * 
	 * @param blogHandle
	 * @param commentId
	 * @return Comment
	 * @throws BlogServiceException
	 */
	public Comment getBlogComment(String blogHandle, String commentId) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(commentId)){
			throw new BlogServiceException(null,"commentID is null");
		}
		Comment comment;
		try {
			String getCommentUrl = resolveUrl(blogHandle, FilterType.GET_REMOVE_COMMENT, commentId);
			comment = (Comment)getEntity(getCommentUrl, null, new CommentsFeedHandler(this));
		} catch (Exception e) {
			throw new BlogServiceException(e, "error getting blog comment");
		}
        return comment;
	}
	
	/**
	 * Wrapper method to create a Blog Comment
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * 
	 * @param Comment
	 * @return Comment
	 * @throws BlogServiceException
	 */
	public Comment createBlogComment(Comment comment, String blogHandle, String postId) throws BlogServiceException {
		if (null == comment){
			throw new BlogServiceException(null,"null comment");
		}
		Response result = null;
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(comment);
			comment.setPostUuid(postId);
			
			Object 	payload = transformer.transform(comment.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String createCommentUrl = resolveUrl(blogHandle, FilterType.CREATE_COMMENT, null);
			
			result = createData(createCommentUrl, null, headers, payload);
			comment = (Comment) new CommentsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error creating blog comment");
		}

        return comment;
	}
	
	/**
	 * Wrapper method to remove a particular Blog Comment
	 * <p>
	 * 
	 * @param blogHandle
	 * @param commentId
	 * @return Comment
	 * @throws BlogServiceException
	 */
	public void removeBlogComment(String blogHandle, String commentId) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(commentId)){
			throw new BlogServiceException(null,"commentID is null");
		}
		try {
			String getCommentUrl = resolveUrl(blogHandle, FilterType.GET_REMOVE_COMMENT, commentId);
			getClientService().delete(getCommentUrl);
		} catch (Exception e) {
			throw new BlogServiceException(e, "error deleting blog comment");
		}
	}
	/*
	 * Util methods
	 */
	
	
	/*
	 * Method to generate appropriate REST URLs
	 * 
	 */
	protected String resolveUrl(String handle, FilterType filterType, String id) {
		return resolveUrl(handle, filterType, id, null);
	}

	/*
	 * Method to generate appropriate REST URLs
	 * 
	 */
	protected String resolveUrl(String handle, FilterType filterType, String id,  Map<String, String> params) {
		StringBuilder baseUrl = new StringBuilder(BASE_URL);
		
		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase(ConnectionsConstants.OAUTH)) {
			baseUrl.append(OAUTH_URL);
		}
		
		// todo : Add oauth logic
		if(StringUtil.isNotEmpty(filterType.toString())){
			baseUrl.append(handle).append(ConnectionsConstants.SEPARATOR).append(filterType.getFilterType());
		}else{
			baseUrl.append(handle);
		}
		if(StringUtil.isNotEmpty(id)){
			baseUrl.append(id);
		}
		
		// Add required parameters
		if (null != params && params.size() > 0) {
			baseUrl.append(ConnectionsConstants.INIT_URL_PARAM);
			boolean setSeparator = false;
			for (Map.Entry<String, String> param : params.entrySet()) {
				String key = param.getKey();
				if (StringUtil.isEmpty(key)) continue;
				String value = EntityUtil.encodeURLParam(param.getValue());
				if (StringUtil.isEmpty(value)) continue;
				if (setSeparator) {
					baseUrl.append(ConnectionsConstants.URL_PARAM);
				} else {
					setSeparator = true;
				}
				baseUrl.append(key).append(ConnectionsConstants.EQUALS).append(value);
			}
		}
		return baseUrl.toString();
	}

}
