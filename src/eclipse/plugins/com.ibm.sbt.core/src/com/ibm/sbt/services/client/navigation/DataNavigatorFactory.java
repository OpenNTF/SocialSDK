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
package com.ibm.sbt.services.client.navigation;

import org.w3c.dom.Node;

import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.JsonNavigator;
import com.ibm.sbt.util.XmlNavigator;

/**
 * @Represents Smartcloud Community Member
 * @author Carlos Manias
 */
public class DataNavigatorFactory {

	/**
	 * Static factory method to return the appropriate instance to handle each data format.
	 * @param data
	 * @param dataFormat
	 * @return
	 */
	public static DataNavigator getNavigator(Object data, Handler dataFormat) {
		if (dataFormat == ClientService.FORMAT_XML) {
			return new XmlNavigator((Node)data);
		} else if (dataFormat == ClientService.FORMAT_JSON) {
			return new JsonNavigator((JsonObject)data);
		} else return null;
	}
}
