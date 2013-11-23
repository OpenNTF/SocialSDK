package com.ibm.sbt.test.lib;
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



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * 
 * @author Carlos Manias
 * 
 */
public class MockService extends ClientService {

	public enum MockMode {
		RECORD, REPLAY, PASSTHROUGH
	}

	private MockMode mode;
	private ClientService service;
	private MockSerializer serializer;

	public MockService(ClientService svc) {
		this(svc, "passthrough");
	}

	public MockService(ClientService svc, String mockMode) {
		this.service = svc;
		this.endpoint = svc.getEndpoint();
		serializer = new MockSerializer(endpoint);
		this.mode = StringUtil.isEmpty(mockMode) ? MockMode.PASSTHROUGH : MockMode
				.valueOf(mockMode.toUpperCase());
	}

	@Override
	protected HttpResponse executeRequest(HttpClient httpClient,
			HttpRequestBase httpRequestBase, Args args)
			throws ClientServicesException {

		switch (this.mode) {
		case REPLAY:
			return serializer.replayResponse();
		case PASSTHROUGH:
			return super.executeRequest(httpClient, httpRequestBase, args);
		case RECORD:
			HttpResponse response;
			response = super.executeRequest(httpClient, httpRequestBase, args);
			return serializer.recordResponse(response);
		default:
			throw new UnsupportedOperationException("Invalid mode " + this.mode);
		}
	}

	protected void initialize(DefaultHttpClient httpClient)
			throws ClientServicesException {
		if (TestEnvironment.getRequiresAuthentication()) {
			super.initialize(httpClient);
		}

	}

}
