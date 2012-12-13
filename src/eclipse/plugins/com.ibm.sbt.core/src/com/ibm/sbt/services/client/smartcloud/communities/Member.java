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
package com.ibm.sbt.services.client.smartcloud.communities;

import java.io.Serializable;
import com.ibm.commons.util.NotImplementedException;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;

/**
 * @Represents Smartcloud Community Member
 * @author Carlos Manias
 */
public class Member<DataFormat> extends BaseEntity<DataFormat> implements Serializable {

	private static final long		serialVersionUID	= -2128818400493231137L;

	private final static String[][]	xmlMappingData		= { { "idUrl", "a:id" }, { "title", "a:title" },
			{ "role", "a:role" }, { "summary", "a:summary" },
			{ "membersUrl", "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href" },
			{ "communityUrl", "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href" }, };

	public Member() {
		super();
	}

	public Member(String uuid, BaseService svc) {
		super(uuid, svc);
	}

	public Member(DataFormat data, BaseService svc) {
		super(data, svc);
		this.uuid = getIdFromUrl(get("idUrl"));
	}

	/**
	 * This method parses the URL containing the communityId and returns the id
	 * 
	 * @param url
	 * @return
	 */
	private String getIdFromUrl(String url) {
		String[] halves = url.split("userid=");
		String[] quarters = halves[1].split("&");
		return quarters[0];
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return get("title");
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return get("summary");
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return get("role");
	}

	/**
	 * @return the member URL
	 */
	public String getMembersUrl() {
		return get("membersUrl");
	}

	/**
	 * @return the community URL
	 */
	public String getCommunityUrl() {
		return get("communityUrl");
	}

	@Override
	protected String[][] getXMLFieldMapping() {
		return xmlMappingData;
	}

	@Override
	protected String[][] getJsonFieldMapping() {
		throw new NotImplementedException();
	}
}
