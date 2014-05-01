package com.ibm.sbt.automation.core.test.connections;

import org.junit.After;
import org.junit.Before;

import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.FlexibleTest;
import com.ibm.sbt.services.client.connections.blogs.Blog;
import com.ibm.sbt.services.client.connections.blogs.BlogPost;
import com.ibm.sbt.services.client.connections.blogs.BlogService;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceException;
import com.ibm.sbt.services.client.connections.blogs.Comment;

/**
 * 
 * @author Francis
 * @date 29 Apr 2014
 */
public class BaseBlogsTest extends FlexibleTest{
	protected BlogService blogService;
	protected Blog blog;
	protected BlogPost blogPost;
	protected Comment blogComment;
	
	public BaseBlogsTest(){
		setAuthType(BaseApiTest.AuthType.AUTO_DETECT);
	}

	@Before
	public void createBlog() {
		if (blogService == null) {
			blogService = new BlogService();
		}
		if(blog == null){
			Long time = System.currentTimeMillis();
			blog = new Blog(blogService, "");

			blog.setTitle("Test Blog" + time);
			blog.setHandle("TestHandle" + time);
			blog.setTimeZone("Europe/Dublin");
			try {
				blog = blog.save();
			} catch (BlogServiceException e) {
				fail("Problem creating blog", e);
			}
		}
	}

	@After
    public void deleteBlogAndQuit(){
    	try {
			deleteBlog();
		} catch (BlogServiceException e) {
			e.printStackTrace();
		}
    	blog = null;
    	blogPost = null;
    	blogComment = null;
    }
	
	protected BlogPost createBlogPost() throws BlogServiceException {
		if(blogPost != null){
			return blogPost;
		}
		createBlog();
		blogPost = new BlogPost(blogService, "");

		blogPost.setTitle("Test Blog Post" + System.currentTimeMillis());
		blogPost.setContent("Test Blog Post Content"+ System.currentTimeMillis());
		blogPost = blogService.createBlogPost(blogPost, blog.getHandle());
		return blogPost;
	}

	protected Comment createBlogComment() throws BlogServiceException {
		if(blogComment != null){
			return blogComment;
		}
		createBlogPost();
		blogComment = new Comment(blogService, "");

		blogComment.setTitle("Test Blog Comment" + System.currentTimeMillis());
		blogComment.setContent("Test Blog Post Content12"+System.currentTimeMillis());
		blogComment = blogService.createBlogComment(blogComment, blog.getHandle(), blogPost.getPostUuid());

		return blogComment;
	}
	
	protected void deleteBlog() throws BlogServiceException {
		if(blog != null){
			deleteBlog(blog.getBlogUuid());
		}
	}
	
	protected void deleteBlog(String blogUuid) throws BlogServiceException {
		blogService.removeBlog(blogUuid);
	}
	
	protected void deleteBlogPost() throws BlogServiceException {
		deleteBlogPost(blogPost.getPostUuid(), blog.getHandle());
	}
	
	protected void deleteBlogPost(String postUuid, String postHandle) throws BlogServiceException {
		blogService.removeBlogPost(postUuid, postHandle);
	}

	protected void deleteBlogComment() throws BlogServiceException {
		deleteBlogComment(blog.getHandle(), blogComment.getCommentUuid());
	}
	
	protected void deleteBlogComment(String commentUuid, String blogHandle) throws BlogServiceException {
		blogService.removeBlogComment(blogHandle, commentUuid);
	}
}