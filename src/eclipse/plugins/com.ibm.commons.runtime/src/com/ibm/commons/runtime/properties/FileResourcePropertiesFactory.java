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

package com.ibm.commons.runtime.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.ibm.commons.util.StringUtil;



/**
 * SBT Properties factory.
 *
 * This class reads a sbt.properties file from a file in the user directory.
 *
 * @author Mark Wallace
 */
public class FileResourcePropertiesFactory extends AbstractPropertiesFactory {

	public static final String DEFAULT_PROPERTIES = "sbt.properties";

	private final Properties properties;

	public FileResourcePropertiesFactory() {
		this(DEFAULT_PROPERTIES);
	}

	@Override
	public String getProperty(String name) {
		return properties!=null ? properties.getProperty(name) : null;
	}

	public FileResourcePropertiesFactory(String fileName) {
		this.properties = readFactoriesFromFile(fileName);
	}

	public Properties readFactoriesFromFile(String fileName) {
		if(StringUtil.isEmpty(fileName)) {
			return null;
		}

		String userDir = getDirectory();
		if (StringUtil.isEmpty(userDir)) {
			return null;
		}
		File file = new File(userDir, fileName);
		if (file.exists()) {
			try {
				Properties properties = new Properties();
				InputStream is = new FileInputStream(file);
				properties.load(is);
				return properties;
			} catch (IOException ioe) {
			}
		}

		return null;
	}

	private String getDirectory() {
		String directory = System.getProperty("sbt.dir");
		if (StringUtil.isEmpty(directory)) {
			directory = System.getProperty("user.dir");
		}
		return directory;
	}
}
