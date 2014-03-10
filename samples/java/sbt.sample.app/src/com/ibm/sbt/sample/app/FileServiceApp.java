/*
 * © Copyright IBM Corp. 2014
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.files.AccessType;
import com.ibm.sbt.services.client.connections.files.Categories;
import com.ibm.sbt.services.client.connections.files.Comment;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.client.connections.files.FileServiceURIBuilder;
import com.ibm.sbt.services.client.connections.files.ResultType;
import com.ibm.sbt.services.client.connections.files.SubFilters;
import com.ibm.sbt.services.client.connections.files.model.Headers;
import com.ibm.sbt.services.client.connections.files.util.Messages;
import com.ibm.sbt.services.client.connections.profiles.Profile;


/**
 * @author mwallace
 *
 */
public class FileServiceApp extends BaseApp {
	
	private FileService fileService;
	
	public FileServiceApp(String url, String user, String password) {
		super(url, user, password);
	}
	
    public FileService getFileService() {
    	if (fileService == null) {
    		fileService = new FileService(getBasicEndpoint());
    	}
    	return fileService;
    }
    
    public Comment addCommentToFile(String fileId, String comment, String userId) throws FileServiceException, TransformerException {
    	return getFileService().addCommentToFile(fileId, comment, userId, null);
    }
    
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.FileServiceApp <url> <user> <password>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		
		FileServiceApp fsa = null;
		try {
			fsa = new FileServiceApp(url, user, password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
