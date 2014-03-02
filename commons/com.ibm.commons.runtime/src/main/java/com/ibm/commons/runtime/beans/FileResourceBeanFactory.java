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

package com.ibm.commons.runtime.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commons.util.StringUtil;


/**
 * SBT ManagedBean Factory.
 *
 * This class reads a managed-beans file from a file in the user directory.
 * 
 * @author Mark Wallace
 */
public class FileResourceBeanFactory extends AbstractXmlConfigBeanFactory {

	public static final String DEFAULT_RESOURCENAME =  EnvironmentConfig.INSTANCE.getEnvironmentConfigFile();

	static final String sourceClass = FileResourceBeanFactory.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);

	public FileResourceBeanFactory() {
		this(DEFAULT_RESOURCENAME);
	}

	public FileResourceBeanFactory(String fileName) {
		setFactories(readFactoriesFromFile(fileName));
	}

	public Factory[] readFactoriesFromFile(String fileName) {
		if(StringUtil.isEmpty(fileName)) {
			return null;
		}
		String userDir = getDirectory();
		if (StringUtil.isEmpty(userDir)) {
			return null;
		}
		File file = new File(userDir, fileName);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Reading file bean factories from: : " + file + " (" + file.exists() + ")");
		}
		if (file.exists()) {
			try {
				InputStream is = new FileInputStream(file);
				return readFactories(is);
			} catch (IOException ioe) {
			}
		}
		return null;
	}

	private String getDirectory() {
		String directory = System.getProperty("sbt.dir");
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Property sbt.dir set to: " + directory);
		}
		if (StringUtil.isEmpty(directory)) {
			directory = System.getProperty("user.dir");
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Property user.dir set to: " + directory);
			}
		}
		return directory;
	}
}
