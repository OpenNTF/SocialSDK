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

import java.util.Iterator;

import com.ibm.sbt.services.client.connections.blogs.BlogList;
import com.ibm.sbt.services.client.connections.blogs.BlogService;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceException;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

/**
 * @author mwallace
 * @date 28 January 2014
 */
public class BlogServiceApp {

    private BlogService blogService;
    
    /**
     * Default constructor
     */
    public BlogServiceApp(String url, String user, String password) {
        this.blogService = new BlogService();
        this.blogService.setEndpoint(createEndpoint(url, user, password));
    }
    
    public void setHomepageHandle(String blogHandle){
    	this.blogService.setHomepageHandle(blogHandle);
    }
    
    private BasicEndpoint createEndpoint(String url, String user, String password) {
    	BasicEndpoint endpoint = new ConnectionsBasicEndpoint();
    	endpoint.setUrl(url);
    	endpoint.setUser(user);
    	endpoint.setPassword(password);
    	endpoint.setForceTrustSSLCertificate(true);
    	return endpoint;
    }
    
    /**
	 * @return the BlogService
	 */
	public BlogService getBlogService() {
		return blogService;
	}
    
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.BlogServiceApp url user password blogHandle(optional)");
			return;
		}
		String url = args[0];
		String user = args[1];
		String password = args[2];
	    BlogServiceApp app = new BlogServiceApp(url, user, password);
        
	    if(args.length > 3){ // if a blog handle has been provided replace the default one.
			app.setHomepageHandle(args[3]);
		}
	    try {
	    	BlogService BlogService = app.getBlogService();
            BlogList blogs = BlogService.getAllBlogs();
            for (Iterator<BaseBlogEntity> iter = blogs.iterator(); iter.hasNext(); ) {
                System.out.println(iter.next().getTitle());
            }
        } catch (BlogServiceException e) {
            e.printStackTrace();
        }
	    
	}
}
