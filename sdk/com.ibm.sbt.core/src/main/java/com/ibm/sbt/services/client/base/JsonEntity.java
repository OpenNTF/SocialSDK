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
package com.ibm.sbt.services.client.base;

import java.util.Map.Entry;
import java.util.Set;

import com.ibm.commons.util.AbstractException;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.InvalidInputException;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;

/**
 * @author mwallace
 *
 */
public class JsonEntity extends BaseEntity {

	/**
	 * Default Constructor.
	 */
	public JsonEntity() {
	}
	
	/**
	 * Construct an JsonEntity instance.
	 * 
	 * @param service
	 * @param jsonObject
	 */
	public JsonEntity(BaseService service, JsonJavaObject jsonObject) {
		super(service, new JsonDataHandler(jsonObject));
	}
	
	/**
	 * Construct an JsonEntity instance.
	 * 
	 * @param service
	 * @param dataHandler
	 */
	public JsonEntity(BaseService service, JsonDataHandler dataHandler) {
		super(service, dataHandler);
	}
	
	/**
	 * Construct an JsonEntity instance.
	 * 
	 * @param service
	 */
	public JsonEntity(BaseService service) {
		super(service, null);
	}
	
	/**
	 * Returns the value of a field as an Object
	 * 
	 * @param fieldName
	 * @return returns false, if the field doesn't exist
	 */
	public JsonJavaObject getJsonObject(){
		if (dataHandler != null) {
			Object data = dataHandler.getData();
			return (data instanceof JsonJavaObject) ? (JsonJavaObject)data : null;
		}
		return null;
	}
		
	/**
	 * Returns the value of a field as an Object
	 * 
	 * @param fieldName
	 * @return returns false, if the field doesn't exist
	 */
	public Object getAsObject(String path){
		if (fields.containsKey(path)) {
			return fields.get(path);
		}
		if (dataHandler != null) {
			return ((JsonDataHandler)dataHandler).getAsObject(path);
		}
		return null;
	}
		
	/**
	 * Return JSON string representation of current data.
	 * 
	 * @return
	 */
	public String toJsonString() {
		JsonDataHandler jsonHandler = (JsonDataHandler)getDataHandler();
		return (jsonHandler == null) ? null : jsonHandler.getData().toString();
	}
	
	/**
	 * Build a JSON object using the current fields
	 * 
	 * @return
	 * @throws AbstractException 
	 */
	public JsonJavaObject buildJsonObject() throws AbstractException {
		return buildJsonObject(null);
	}

	/**
	 * Build a JSON object using the current fields
	 * 
	 * @return
	 * @throws AbstractException 
	 */
	public JsonJavaObject buildJsonObject(String jsonString) throws AbstractException {
		JsonJavaObject jsonObject = null;
		if (StringUtil.isEmpty(jsonString)) {
			jsonObject = new JsonJavaObject();
		} else {
			jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, jsonString);
		}
		if (fields != null) {
			Set<Entry<String, Object>> entries = fields.entrySet();
			for (Entry<String, Object> entry : entries) {
				setValue(jsonObject, entry.getKey(), entry.getValue());
			}
		}
		return jsonObject;
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param value
	 * @throws AbstractException 
	 */
	protected void setValue(JsonJavaObject jsonObject, String path, Object value) throws AbstractException {
		String[] pathParts = path.split("/");
		JsonJavaObject currObject = jsonObject;
		for (int i=0; i<pathParts.length; i++) {
			if (i+1 == pathParts.length) {
				currObject.put(pathParts[i], value);
			} else {
				Object nextPart = currObject.get(pathParts[i]);
				if (nextPart == null) {
					nextPart = new JsonJavaObject();
					currObject.put(pathParts[i], nextPart);
				} else {
					if (!(nextPart instanceof JsonJavaObject)) {
						throw new InvalidInputException(null, "Invalid path {0}", path);
					}
				}
				currObject = (JsonJavaObject)nextPart;
			}
		}
	}
	
}
