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

package com.ibm.sbt.services.client.base.datahandlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.ibm.commons.util.io.json.JsonJavaObject;

/**
 * This class provides an implementation of the DataHandler class to use data in JSON format. 
 * @author Carlos Manias
 * @author Manish Kataria
 */
public class JsonDataHandler implements DataHandler<JsonJavaObject>{
	
	private JsonJavaObject data;

	/**
	 * Constructor
	 *  
	 * @param jsonObject
	 */
	public JsonDataHandler(JsonJavaObject jsonObject) {
		this.data = jsonObject;
	}
	
	/**
	 * @param field
	 * @return value 
	 */
	@Override
	public String getAsString(FieldEntry field) {
		return getAsString((String)field.getPath());
	}
	
	/**
	 * @param path
	 * @return value 
	 */
	@Override
	public String getAsString(String path) {
		return path.indexOf("/")==-1?data.getString(path):getNestedField(path);
	}
	
	/*
	 * @param path
	 * @return
	 */
	private String getNestedField(String path){
		String value = "";
		String[] pathParts = path.split("/");
		JsonJavaObject currentObject = data;
		int index = 0;
		for (String part : pathParts){
			if (!containsKey(currentObject,part)) break;
			if (index<pathParts.length-1) {
				currentObject = currentObject.getJsonObject(part);
			} else {
				value = currentObject.getString(part);
			}
			index++;
		}
		return value;
	}
	
	public boolean containsKey(JsonJavaObject j, String key) {
	    Iterator<String> i = j.getProperties();
	    while(i.hasNext()) {
	        if(i.next().matches(key))return true;
	    }
	    return false;
	}
	
	private Object getNestedObject(String path){
		String[] pathParts = path.split("/");
		JsonJavaObject currentObject = data;
		int index = 0;
		for (String part : pathParts){
			if (!containsKey(currentObject, part)) break;
			if (index<pathParts.length-1) {
				currentObject = currentObject.getJsonObject(part);
			}
			index++;
		}
		return currentObject;
	}
	
	@Override
	public JsonJavaObject getEntry(FieldEntry field) {
		return getEntry((String)field.getPath());
	}

	@Override
	public JsonJavaObject getEntry(String path) {
		return data.getJsonObject(path);
	}
	
	/**
	 * @param field
	 * @return list of entries
	 */
	@Override
	public List<JsonJavaObject> getEntries(FieldEntry field) {
		return getEntries((String)field.getPath());
	}
	
	/**
	 * @param path
	 * @return list of entries
	 */
	@Override
	public List<JsonJavaObject> getEntries(String path) {
		return getAsList(path);
	}
	
	/**
	 * @param field
	 * @return list of entries
	 */
	public List<JsonJavaObject> getAsList(FieldEntry field){
		return getAsList((String)field.getPath());
	}
	
	/**
	 * @param path
	 * @return list of entries
	 */
	public List<JsonJavaObject> getAsList(String path){
		List<JsonJavaObject> jsonList = new ArrayList<JsonJavaObject>();
		
		if(path.contains("/")){
			JsonJavaObject object = (JsonJavaObject) getNestedObject(path);
			String pathinfo = path.substring(path.lastIndexOf("/")+1,path.length());
			if(containsKey(object,pathinfo)){
				for (Object obj : (List)object.get(pathinfo)) {
					jsonList.add((JsonJavaObject)obj);
				}
			}else{
				return null;
			}
		}else{
			for (Object obj : (List)data.get(path)) {
				jsonList.add((JsonJavaObject)obj);
			}
		}
		return jsonList;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.DataHandler#getAsArray(com.ibm.sbt.services.client.base.datahandlers.FieldEntry)
	 */
	@Override
	public String[] getAsArray(FieldEntry field) {
		return getAsArray((String)field.getPath());
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.DataHandler#getAsArray(java.lang.String)
	 */
	@Override
	public String[] getAsArray(String path) {
		List<String> strList = new ArrayList<String>();
		
		if(path.contains("/")){
			JsonJavaObject object = (JsonJavaObject) getNestedObject(path);
			String pathinfo = path.substring(path.lastIndexOf("/")+1,path.length());
			if(containsKey(object,pathinfo)){
				for (Object obj : (List)object.get(pathinfo)) {
					strList.add((String)obj);
				}
			}else{
				return null;
			}
		}else{
			for (Object obj : (List)data.get(path)) {
				strList.add((String)obj);
			}
		}
		return strList.toArray(new String[strList.size()]);
	}
	
	/**
	 * @set data
	 */
	@Override
	public void setData(final Object data) {
		this.data = (JsonJavaObject)data;
	}
	
	/**
	 * @return data
	 */
	@Override
	public JsonJavaObject getData() {
		return data;
	}

    /**
     * @param field
     * @param locale
     * @return value as Date 
     * @throws DataHandlerException 
     */
	@Override
	public Date getAsDate(FieldEntry field, final Locale locale) throws DataHandlerException {
		String value = getAsString(field);
		Date date = null;
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		try {
			date = format.parse(value);
		} catch(ParseException e) {
			throw new DataHandlerException(e, "Error parsing date string");
		}
		return date;
	}
	
    /**
     * @param fieldName
     * @param locale
     * @return value as Date 
     * @throws DataHandlerException 
     */
	@Override
	public Date getAsDate(String fieldName, final Locale locale) throws DataHandlerException {
		String value = getAsString(fieldName);
		Date date = null;
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		try {
			date = format.parse(value);
		} catch(ParseException e) {
			throw new DataHandlerException(e, "Error parsing date string");
		}
		return date;
	}
	
    /**
     * @param field
     * @return value as Date 
     * @throws DataHandlerException 
     */
	@Override
	public Date getAsDate(FieldEntry field) throws DataHandlerException {
		return getAsDate(field, Locale.getDefault());
	}
	
    /**
     * @param fieldName
     * @return value as Date 
     * @throws DataHandlerException 
     */
	@Override
	public Date getAsDate(String fieldName) throws DataHandlerException {
		return getAsDate(fieldName, Locale.getDefault());
	}

    /**
     * @param field
     * @return value as int 
     */
	@Override
	public int getAsInt(FieldEntry field) {
		return Integer.parseInt(getAsString(field));
//		return data.getAsInt((String)field.getPath());
	}

    /**
     * @param fieldName
     * @return value as int 
     */
	@Override
	public int getAsInt(String fieldName) {
		try {
			return Integer.parseInt(getAsString(fieldName));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
     * @param fieldName
     * @return value as long 
     */
	@Override
	public Long getAsLong(String fieldName) {
		try {
			return Long.parseLong(getAsString(fieldName));
		} catch (NumberFormatException e) {
			return (long) 0;
		}
	}
	
	/**
     * @param fieldName
     * @return value as long 
     */
	@Override
	public Long getAsLong(FieldEntry fieldName) {
		String field = (String)fieldName.getPath();
		try {
			return Long.parseLong(getAsString(field));
		} catch (NumberFormatException e) {
			return (long) 0;
		}
	}

    /**
     * @param field
     * @return value as float 
     */
	@Override
	public float getAsFloat(FieldEntry field) {
		return (float)data.getDouble((String)field.getPath());
	}

    /**
     * @param fieldName
     * @return value as float 
     */
	@Override
	public float getAsFloat(String fieldName) {
		return (float)data.getDouble(fieldName);
	}

    /**
     * @param field
     * @return value as Date 
     */
	@Override
	public boolean getAsBoolean(FieldEntry field) {
		return data.getBoolean((String)field.getPath());
	}

    /**
     * @param fieldName
     * @return value as boolean 
     */
	@Override
	public boolean getAsBoolean(String fieldName) {
		return data.getBoolean(fieldName);
	}
}
