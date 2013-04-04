package com.ibm.sbt.service.basic;

import java.io.File;

import com.ibm.sbt.services.client.ClientService.Content;
import com.ibm.sbt.services.client.ClientService.ContentFile;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class ConnectionsFileProxyService extends AbstractFileProxyService {

	@Override
	protected Content getFileContent(File file, long length, String name) {
		return new ContentFile(file);
	}

	@Override
	protected String getFileType() {		
		return "connections";
	}
}
