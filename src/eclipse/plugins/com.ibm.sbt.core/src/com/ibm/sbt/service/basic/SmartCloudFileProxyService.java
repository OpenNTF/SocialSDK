package com.ibm.sbt.service.basic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.sbt.services.client.ClientService.Content;
import com.ibm.sbt.services.client.ClientService.ContentStream;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class SmartCloudFileProxyService extends AbstractFileProxyService {

	static final String sourceClass = SmartCloudFileProxyService.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);

	@Override
	protected Content getFileContent(File file, long length, String name) {
		try {
			ContentStream contentFile = new ContentStream(new FileInputStream(file), length, name);
			return contentFile;
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}

	}


}
