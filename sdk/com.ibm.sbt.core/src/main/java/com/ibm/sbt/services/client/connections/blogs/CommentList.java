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

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.BlogsFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntityList;

/**
 * Class used in representing List of Blog Comments
 * @author Swati Singh
 */

public class CommentList extends BaseBlogEntityList  {

	public CommentList(Response requestData, BaseService service) {
		super(requestData, service);
	}
	
	public CommentList(Response requestData, BlogsFeedHandler feedHandler) {
		super(requestData, feedHandler);
	}

}
