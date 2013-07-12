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
package com.ibm.sbt.services.client.base.transformers;

import java.io.InputStream;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/**
 * AbstractBaseTransformer provides helper methods for reading and updating template files
 * <p>
 * @author Manish Kataria
 */

public abstract class AbstractBaseTransformer {
	
	public abstract String transform(Map<String,Object> fieldmap) throws TransformerException;
	
	protected String getTemplateContent(String templatepath) throws TransformerException{
		try {
			InputStream fisTargetFile = AbstractBaseTransformer.class.getResourceAsStream(templatepath);
			String targetFileStr = IOUtils.toString(fisTargetFile);
			return targetFileStr;
		} catch (Exception e) {
			throw new TransformerException(e);
		}
	}
	
	protected String getXMLRep(InputStream templatefile, String placeholder, String value) throws TransformerException{
		try {
			String targetFileStr = IOUtils.toString(templatefile);
			return getXMLRep(targetFileStr,placeholder,value);
		} catch (Exception e) {
			throw new TransformerException(e);
		}
	}
	
	protected String getXMLRep(String xml, String placeholder, String value){
		placeholder = "${"+placeholder+"}";
		xml = xml.replace(placeholder,  value);
		return xml;
	}
	
	protected String removeExtraPlaceholders(String xmlbody){
		while(xmlbody.contains("${")){
			int startIndex = xmlbody.indexOf("${");
			int endIndex = xmlbody.indexOf("}", startIndex);
			String tempStart = xmlbody.substring(0,startIndex);
			String tempEnd = xmlbody.substring(endIndex+1,xmlbody.length());
			xmlbody = tempStart+tempEnd;
		}
		return xmlbody;
	}
	
	protected InputStream getStream(String templatepath){
		InputStream fisTargetFile = AbstractBaseTransformer.class.getResourceAsStream(templatepath);
		return fisTargetFile;
	}

}
