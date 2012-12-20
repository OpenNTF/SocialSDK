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
package com.ibm.sbt.services.client.smartcloud.files;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;

public class RepositoryInfo<DataFormat> extends BaseEntity<DataFormat> implements Serializable {
	private static final String	sourceClass			= RepositoryInfo.class.getName();
	private static final Logger	logger				= Logger.getLogger(sourceClass);

	private static final long	serialVersionUID	= -8891317255436465379L;

	public RepositoryInfo() {
		super();
	}

	public RepositoryInfo(String uuid, BaseService svc) {
		super(uuid, svc);
		initParams();
	}

	public RepositoryInfo(DataFormat data, BaseService svc) {
		super(data, svc);
		initParams();
		this.uuid = getIdFromUrl(get("idUrl"));
	}

	/**
	 * This method parses the url containing the communityId and returns the id
	 * 
	 * @param url
	 * @return
	 */
	private String getIdFromUrl(String url) {
		String[] halves = url.split("communityUuid=");
		String[] quarters = halves[1].split("&");
		return quarters[0];
	}

	private void initParams() {
		this.selfLoadUrl = "/files/basic/cmis/my/servicedoc";
		this.nameParameterId = "repositoryId";
		this.dataFormat = svc.getDataFormat();
	}

	public String getRepositoryID() {
		// TODO Auto-generated method stub
		return get("repositoryId");
	}

	@Override
	public void load() throws ClientServicesException {
		// TODO fix loading so that it doesn't force the uuid retrieval
		Map<String, String> parameters = new HashMap<String, String>();

		DataFormat data = null;

			data = (DataFormat) svc.retrieveData(selfLoadUrl, parameters, null);
	
		setData(data);

	}

	@Override
	protected String[][] getXMLFieldMapping() {
		return new String[][] { { "repositoryId", "/service/workspace/repositoryInfo/repositoryId" } };
	}

	@Override
	protected String[][] getJsonFieldMapping() {
		return null;
	}

}
