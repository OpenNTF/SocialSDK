package com.ibm.sbt.services.client.connections.blogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import com.ibm.sbt.services.client.SerializationUtil;

/**
 * @author Swati Singh
 *
 */
public class BlogSerializationTests extends BaseBlogServiceTest {
	
	
	@Test
	public final void testBlogSerialization() throws Exception {
		Blog blog = createBlog();
		Blog blogGot = blogService.getBlog(blog.getBlogUuid());
		final String handle = blogGot.getHandle();
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				Blog blogObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					blogObject = (Blog) ois.readObject();
					assertEquals(blogObject.getHandle(), handle);
				} catch (Exception e) {}
				
			}
		}.isSerializable(blogGot);
	}
	
		
	@Test
	public final void testBlogListSerialization() throws Exception {
		BlogList blogs = blogService.getAllBlogs();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				BlogList allblogs = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allblogs = (BlogList) ois.readObject();
					for (Iterator iterator = allblogs.iterator(); iterator.hasNext();) {
						Blog localBlog = (Blog) iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allblogs.size()>0));
			}
		}.isSerializable(blogs);
	}
	
	@Test
	public final void testBlogPostSerialization() throws Exception {
		BlogPost blogPost = createBlogPost();
		BlogPost blogPostGot = blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
		final String postUuid = blogPostGot.getPostUuid();
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				BlogPost blogPostObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					blogPostObject = (BlogPost) ois.readObject();
					assertEquals(blogPostObject.getPostUuid(), postUuid);
				} catch (Exception e) {}
				
			}
		}.isSerializable(blogPostGot);
	}
	
		
	@Test
	public final void testBlogPostListSerialization() throws Exception {
		BlogPostList posts = blogService.getAllPosts();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				BlogPostList allposts = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allposts = (BlogPostList) ois.readObject();
					for (Iterator iterator = allposts.iterator(); iterator.hasNext();) {
						BlogPost localBlogPost = (BlogPost) iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allposts.size()>0));
			}
		}.isSerializable(posts);
	}
	
	@Test
	public final void testBlogCommentSerialization() throws Exception {
		Comment comment = createBlogComment();
		Comment commentGot = blogService.getBlogComment(blog.getHandle(), comment.getCommentUuid());
		final String commentUuid = commentGot.getCommentUuid();
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				Comment commentObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					commentObject = (Comment) ois.readObject();
					assertEquals(commentObject.getCommentUuid(), commentUuid);
				} catch (Exception e) {}
				
			}
		}.isSerializable(commentGot);
	}
	
		
	@Test
	public final void testBlogCommentListSerialization() throws Exception {
		CommentList comments = blogService.getAllComments();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				CommentList allcomments = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allcomments = (CommentList) ois.readObject();
					for (Iterator iterator = allcomments.iterator(); iterator.hasNext();) {
						Comment localcoment = (Comment) iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allcomments.size()>0));
			}
		}.isSerializable(comments);
	}
	
}