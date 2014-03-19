/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.sample.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.blogs.BlogPost;
import com.ibm.sbt.services.client.connections.blogs.BlogPostList;
import com.ibm.sbt.services.client.connections.blogs.BlogService;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceException;
import com.ibm.sbt.services.client.connections.blogs.Comment;
import com.ibm.sbt.services.client.connections.blogs.CommentList;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace
 * @date 14 January 2014
 */
public class ExportBlog {

	private RuntimeFactory runtimeFactory;
	private Context context;
	private Application application;
	private BasicEndpoint endpoint;
	private BlogService blogService;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
	/**
	 * Default constructor. Initialises the Context, the FileService, and the
	 * default FileService endpoint.
	 * 
	 * Be sure to call the destroy() method in this class if you don't intend to
	 * keep the initialised Context around.
	 * 
	 * @throws AuthenticationException
	 */
	public ExportBlog(String user, String password) throws AuthenticationException {
		this(user, password, BaseService.DEFAULT_ENDPOINT_NAME, true);
	}

	/**
	 * 
	 * @param endpointName
	 *            The name of the endpoint to use.
	 * @param initEnvironment
	 *            - True if you want a Context initialised, false if there is
	 *            one already. destroy() should be called when finished using
	 *            this class if a context is initialised here.
	 * @throws AuthenticationException
	 */
	public ExportBlog(String user, String password, String endpointName, boolean initEnvironment)
			throws AuthenticationException {
		if (initEnvironment) {
			this.initEnvironment();
		}
		
		BasicEndpoint basicEndpoint = (BasicEndpoint) EndpointFactory.getEndpoint(endpointName);
		if (StringUtil.isNotEmpty(user)) {
			basicEndpoint.login(user, password);
		}

		this.blogService = new BlogService();
		this.setEndpoint(basicEndpoint);
	}

	/**
	 * 
	 * @return The endpoint used in this class.
	 */
	public BasicEndpoint getEndpoint() {
		return this.endpoint;
	}

	/**
	 * 
	 * @param endpoint
	 *            The endpoint you want this class to use.
	 * @throws AuthenticationException
	 */
	public void setEndpoint(BasicEndpoint endpoint)
			throws AuthenticationException {
		this.endpoint = endpoint;
		this.blogService.setEndpoint(this.endpoint);
	}

	/**
	 * Initialise the Context, needed for Services and Endpoints.
	 */
	public void initEnvironment() {
		runtimeFactory = new RuntimeFactoryStandalone();
		application = runtimeFactory.initApplication(null);
		context = Context.init(application, null, null);
	}

	/**
	 * Destroy the Context.
	 */
	public void destroy() {
		if (context != null) {
			Context.destroy(context);
		}
		if (application != null) {
			Application.destroy(application);
		}
	}

	public void export(String blogHandle, String csvFile) throws BlogServiceException, FileNotFoundException {
		long start = System.currentTimeMillis();
		
		File file = new File(csvFile);
		PrintWriter printWriter = new PrintWriter(file);
		
		List<BlogPostEntry> entries = new ArrayList<ExportBlog.BlogPostEntry>();
		
		int page = 0;
		int pageSize = 50;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", StringUtil.toString(page));
		params.put("ps", StringUtil.toString(pageSize));
		params.put("sortBy", "modified");
		params.put("sortOrder", "desc");
		
		BlogPostList postList = blogService.getBlogPosts(blogHandle, params);
		
		int totalResults = postList.getTotalResults();
		int totalPosts = postList.size();
		
		while (true) {
			System.out.println("Read "+totalPosts+" of "+totalResults+" blogs posts.");

			for (Iterator<BaseBlogEntity> iter = postList.iterator(); iter.hasNext(); ) {
				entries.add(new BlogPostEntry((BlogPost)iter.next()));
			}
			
			params.put("page", StringUtil.toString(++page));
			postList = blogService.getBlogPosts(blogHandle, params);
			
			if (postList.isEmpty()) {
				break;
			}
			
			totalResults = postList.getTotalResults();
			totalPosts += postList.size();
		}

		printWriter.print("\"Title\",\"Comment\",\"Author\",\"Published\",\"Contributor\",\"Updated\",\"Content\",\"Comments\",\"Recommendations\",\"Hits\",\"Tags\"\n");
		for (Iterator<BlogPostEntry> iter = entries.iterator(); iter.hasNext(); ) {
			iter.next().write(printWriter);
		}
		
		printWriter.close();
		
		long duration = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Export took: "+duration+"(secs)");
	}
	
	private class BlogPostEntry {
		
		private String delim = ",";
		private BlogPost blogPost;
		private CommentList commentList;
		
		BlogPostEntry(BlogPost blogPost) throws BlogServiceException {
			this.blogPost = blogPost;
			
			if (blogPost.getCommentCount() > 0) {
				System.out.println("Reading "+blogPost.getCommentCount()+" comments from "+blogPost.getTitle());
				Map<String, String> params = new HashMap<String, String>();
				params.put("page", StringUtil.toString(0));
				params.put("ps", StringUtil.toString(blogPost.getCommentCount()));
				params.put("sortBy", "modified");
				params.put("sortOrder", "asc");
				this.commentList = blogPost.getComments(params);
			}
		}

		public void write(PrintWriter printWriter) {
			StringBuilder sb = new StringBuilder();
			sb.append("\"").append(encode(blogPost.getTitle())).append("\"").append(delim);
			sb.append("\"\"").append(delim);
			sb.append("\"").append(encode(blogPost.getAuthor().getName())).append("\"").append(delim);
			sb.append("\"").append(encode(blogPost.getPublished())).append("\"").append(delim);
			sb.append("\"").append(encode(blogPost.getContributor().getName())).append("\"").append(delim);
			sb.append("\"").append(encode(blogPost.getUpdated())).append("\"").append(delim);
			sb.append("\"").append(encode(blogPost.getContent())).append("\"").append(delim);
			sb.append("\"").append(blogPost.getCommentCount()).append("\"").append(delim);
			sb.append("\"").append(blogPost.getRecommendationsCount()).append("\"").append(delim);
			sb.append("\"").append(blogPost.getHitCount()).append("\"").append(delim);
			sb.append("\"").append(encode(blogPost.getTags())).append("\"").append(delim);
			sb.append("\n");
			
			printWriter.write(sb.toString());
			
			if (commentList != null) {
				for (Iterator<BaseBlogEntity> iter = commentList.iterator(); iter.hasNext(); ) {
					Comment comment = (Comment)iter.next();
					
					sb = new StringBuilder();
					sb.append("\"\"").append(delim);
					sb.append("\"").append(encode(comment.getContent())).append("\"").append(delim);
					sb.append("\"").append(encode(comment.getAuthor().getName())).append("\"").append(delim);
					sb.append("\"").append(encode(comment.getPublished())).append("\"").append(delim);
					sb.append("\"\"").append(delim);
					sb.append("\"").append(encode(comment.getUpdated())).append("\"").append(delim);
					sb.append("\"\"").append(delim);
					sb.append("\"\"").append(delim);
					sb.append("\"\"").append(delim);
					sb.append("\"").append(encode(comment.getTags())).append("\"").append(delim);
					sb.append("\n");
					
					printWriter.write(sb.toString());
				}
			}
		}
		
		private String encode(String value) {
			//value = StringUtil.replace(value, '\n', ' ');
			//value = StringUtil.replace(value, ',', ' ');
			value = StringUtil.replace(value, "\"", "\"\"");
			return value;
		}
		
		private String encode(List<String> values) {
			return StringUtil.concatStrings((String[])values.toArray(), ' ', true);
		}
		
		private String encode(Date value) {
			return dateFormat.format(value);
		}
	}

	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.ExplortBlog <blog_handle> <output_csv>");
			return;
		}
		
		String user = (args.length > 2) ? args[2] : null;
		String password = (args.length > 3) ? args[3] : null;
		
		ExportBlog exportBlog = null;
		try {
			exportBlog = new ExportBlog(user, password);
			exportBlog.export(args[0], args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (exportBlog != null) {
				exportBlog.destroy();
			}
		}

	}
	
}