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

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;

import org.junit.Test;

import com.ibm.sbt.services.client.SerializationUtil;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

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
		EntityList<Blog> blogs = blogService.getAllBlogs();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				EntityList<Blog> allblogs = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allblogs = (EntityList<Blog>)ois.readObject();
					for (Iterator<Blog> iterator = allblogs.iterator(); iterator.hasNext();) {
						Blog localBlog = iterator.next();
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
		EntityList<BlogPost> posts = blogService.getAllPosts();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				EntityList<BlogPost> allposts = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allposts = (EntityList<BlogPost>) ois.readObject();
					for (Iterator<BlogPost> iterator = allposts.iterator(); iterator.hasNext();) {
						BlogPost localBlogPost = iterator.next();
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
		EntityList<Comment> comments = blogService.getAllComments();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				EntityList<Comment> allcomments = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allcomments = (EntityList<Comment>) ois.readObject();
					for (Iterator<Comment> iterator = allcomments.iterator(); iterator.hasNext();) {
						Comment localcoment = iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allcomments.size()>0));
			}
		}.isSerializable(comments);
	}
	
}