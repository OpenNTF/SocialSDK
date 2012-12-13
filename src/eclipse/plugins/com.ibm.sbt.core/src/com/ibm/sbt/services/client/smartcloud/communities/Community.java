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
 * @Represents Smartcloud Community
 * @author Carlos Manias
 */
public class Community<DataFormat> extends BaseEntity<DataFormat> implements Serializable {

	private static final long		serialVersionUID	= -7593580292404705548L;

	private final static String[][]	xmlMappingData		= { { "idUrl", "a:id" }, { "title", "a:title" },
			{ "author", "a:author/a:name" }, { "summary", "a:summary" },
			{ "membersUrl", "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href" },
			{ "communityUrl", "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href" } };

	public Community() {
		super();
	}

	public Community(String uuid, BaseService svc) {
		super(uuid, svc);
		initParams();
	}

	public Community(DataFormat data, BaseService svc) {
		super(data, svc);
		initParams();
		this.uuid = getIdFromUrl(get("idUrl"));
	}

	private String getIdFromUrl(String url) {
		String[] halves = url.split("communityUuid=");
		String[] quarters = halves[1].split("&");
		return quarters[0];
	}

	private void initParams() {
		this.selfLoadUrl = "/communities/service/atom/community/instance";
		this.nameParameterId = "communityUuid";
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return get("title");
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return get("author");
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return get("summary");
	}

	/**
	 * @return the membersUrl
	 */
	public String getMembersUrl() {
		return get("membersUrl");
	}

	/**
	 * @return the communityUrl
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
