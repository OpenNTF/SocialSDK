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

import java.util.Collection;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace, Francis
 * @date 11 April 2013
 */
public class DownloadFileApp {

	private RuntimeFactory runtimeFactory;
	private Context context;
	private Application application;
	private BasicEndpoint endpoint;
	private FileService fileService;
	private CommunityService communityService;

	/**
	 * Default constructor. Initialises the Context, the FileService, and the
	 * default FileService endpoint.
	 * 
	 * Be sure to call the destroy() method in this class if you don't intend to
	 * keep the initialised Context around.
	 * 
	 * @throws AuthenticationException
	 */
	public DownloadFileApp() throws AuthenticationException {
		this(BaseService.DEFAULT_ENDPOINT_NAME, true);
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
	public DownloadFileApp(String endpointName, boolean initEnvironment)
			throws AuthenticationException {
		if (initEnvironment)
			this.initEnvironment();

		this.fileService = new FileService();
		this.communityService = new CommunityService();
		this.setEndpoint((BasicEndpoint) EndpointFactory
				.getEndpoint(endpointName));
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
		this.fileService.setEndpoint(this.endpoint);
		this.communityService.setEndpoint(this.endpoint);
		this.endpoint.login("asingh", "passw0rd");
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
		if (context != null)
			Context.destroy(context);
		if (application != null)
			Application.destroy(application);
	}

	public FileService getFileService() {
		return this.fileService;
	}

	public CommunityService getCommunityService() {
		return this.communityService;
	}

	/**
	 * Get a list of public files.
	 * 
	 * @return Collection of public files
	 * @throws FileServiceException
	 */
	public Collection<File> getPublicFiles() throws FileServiceException {
		return fileService.getPublicFiles();
	}

	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DownloadFileApp app = null;

		try {
			app = new DownloadFileApp();
			FileService fileService = app.getFileService();
			Collection<File> files = fileService.getPublicFiles();
			for (File file : files) {
				if (file.getTitle().contains(".txt")) {

					fileService.downloadFile(System.out, file.getFileId(),
							file.getLibraryId(), true);
				}
			}
		} catch (FileServiceException e) {
			e.printStackTrace();
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} finally {
			if (app != null)
				app.destroy();
		}

	}
	
}