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

package com.ibm.sbt.services.client.base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.DataHandlerException;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * This class provides acts as a base class for all the entities, implementing some behaviour common to all Entities 
 * @author Carlos Manias
 *
 */
public class BaseEntity {

	private BaseService svc;
	protected DataHandler<?> dataHandler;
    protected Map<String, Object> fields = new HashMap<String, Object>();

	protected BaseEntity() {} 
	
	/**
	 * Constructor 
	 * 
	 * @param svc
	 * @param dataHandler
	 */
	public BaseEntity(BaseService svc, DataHandler<?> dataHandler) {
		this.svc = svc;
		this.dataHandler = dataHandler;
	}
	
	/**
	 *	This method retrieves the value of a field as a String. If the field has been modified it retrieves the value from the fields Map.
	 *	Otherwise it delegates to the data handler the task to retrieve the field from the data object. 
	 *
	 * @param field
	 * @return
	 */
	public String getAsString(FieldEntry field){
		if (fields.containsKey(field.getName())){
			Object value = fields.get(field.getName());
			return (value == null) ? null : value.toString();
		}
		if (dataHandler != null){
			return dataHandler.getAsString(field);
		}
		return null;
	}
	
	/**
	 *	This method retrieves the value of a field as a String. If the field has been modified it retrieves the value from the fields Map.
	 *	Otherwise it delegates to the data handler the task to retrieve the field from the data object. 
	 *
	 * @param fieldName
	 * @return
	 */
	public String getAsString(String fieldName){
		if (fields.containsKey(fieldName)){
			Object value = fields.get(fieldName);
			return (value == null) ? null : value.toString();
		}
		if (dataHandler != null){
			return dataHandler.getAsString(fieldName);
		}
		return null;
	}
	
	/**
	 * This method updates the value of a field by modifying an internal map. Used to create or update an entity.
	 * 
	 * @param fieldName
	 * @param value
	 */
	public BaseEntity setAsString(FieldEntry field, String value) {
		fields.put(field.getName(), value);
		return this;
	}
	/**
	 * This method updates the value of a field. Used to create or update an entity.
	 * 
	 * @param field
	 * @param value
	 */
	
	public BaseEntity setAsString(String field, String value) {
		fields.put(field , value);
		return this;
	}
	/**
	 * Returns the value of a field as an int
	 * 
	 * @param field
	 * @return
	 */
	public int getAsInt(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Integer)fields.get(field.getName());
		}
		if (dataHandler != null)
			return dataHandler.getAsInt(field);
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",field.getName()));
	}
	
	/**
	 * Returns the value of a field as a long
	 * 
	 * @param field
	 * @return
	 */
	public Long getAsLong(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Long)fields.get(field.getName());
		}
		if (dataHandler != null)
			return dataHandler.getAsLong(field);
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",field.getName()));
	}
	
	/**
	 * Returns the value of a field as a float
	 * 
	 * @param field
	 * @return
	 */
	public float getAsFloat(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Float)fields.get(field.getName());
		}
		if (dataHandler != null)
			return dataHandler.getAsFloat(field);
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",field.getName()));
	}
	
	/**
	 * Returns the value of a field as a boolean
	 * 
	 * @param field
	 * @return
	 */
	public boolean getAsBoolean(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Boolean)fields.get(field.getName());
		}
		if (dataHandler != null)
			return dataHandler.getAsBoolean(field);
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",field.getName()));
	}
	
	/**
	 * Returns the value of a field as a date
	 * 
	 * @param field
	 * @return
	 */
	public Date getAsDate(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Date)fields.get(field.getName());
		}
		if (dataHandler != null) {
			try {
				return dataHandler.getAsDate(field);
			} catch (DataHandlerException e) {
			}
		}
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",field.getName()));
	}
	
	/**
	 * Returns the value of a field as a date
	 * 
	 * @param field
	 * @return
	 */
	public Date getAsDate(String fieldName){
		if (fields.containsKey(fieldName)){
			return (Date)fields.get(fieldName);
		}
		if (dataHandler != null) {
			try {
				return dataHandler.getAsDate(fieldName);
			} catch (DataHandlerException e) {
			}
		}
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",fieldName));
	}
	
	/**
	 * Returns the value of a field as a map
	 * 
	 * @param field
	 * @return
	 */
	public Map<String, String> getAsMap(FieldEntry[] fields){
		Map<String, String> map = new HashMap<String, String>();
		for (FieldEntry field : fields) {
			map.put(field.getName(), getAsString(field));
		}
		return map;
	}
	
	/**
	 * Returns the value of a field as a map
	 * 
	 * @param field
	 * @return
	 */
	public Map<String, String> getAsMap(String[] fieldNames){
		Map<String, String> map = new HashMap<String, String>();
		for (String fieldName : fieldNames) {
			map.put(fieldName, getAsString(fieldName));
		}
		return map;
	}
	
	/**
	 * Returns the value of a field as a map
	 * 
	 * @param field
	 * @return
	 */
	public String[] getAsArray(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (String[])fields.get(field.getName());
		}
		if (dataHandler != null) {
			return dataHandler.getAsArray(field.getName());
		}
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",field.getName()));
	}
	
	/**
	 * Returns the value of a field as a map
	 * 
	 * @param field
	 * @return
	 */
	public String[] getAsArray(String fieldName){
		if (fields.containsKey(fieldName)){
			return (String[])fields.get(fieldName);
		}
		if (dataHandler != null) {
			return dataHandler.getAsArray(fieldName);
		}
		throw new NullPointerException(StringUtil.format("Field {0} was not found or had no value",fieldName));
	}
	
	/**
	 * Receives an int as the value of a field and stores it as an Integer on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setAsInt(String fieldName, int value){
		fields.put(fieldName, new Integer(value));
	}
	
	/**
	 * Receives a float as the value of a field and stores it as a Float on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public BaseEntity setAsFloat(String fieldName, float value){
		fields.put(fieldName, new Float(value));
		return this;
	}
	
	/**
	 * Receives a boolean as the value of a field and stores it as a Boolean on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public BaseEntity setAsBoolean(String fieldName, boolean value){
		fields.put(fieldName, new Boolean(value));
		return this;
	}
	
	/**
	 * Receives a Date as the value of a field and stores it as a Date on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public BaseEntity setAsDate(String fieldName, Date value){
		fields.put(fieldName, value);
		return this;
	}
	
	/**
	 * Receives an array as the value of a field and stores it as an array on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public BaseEntity setAsArray(String fieldName, String[] value){
		fields.put(fieldName, value);
		return this;
	}
	
	/**
	 * Receives an array as the value of a field and stores it as an array on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public BaseEntity setAsArray(FieldEntry field, String[] value){
		fields.put(field.getName(), value);
		return this;
	}
	
	/**
	 * Removes a field from the internal map
	 * 
	 * @param fieldName
	 */
	public void remove(String fieldName){
		fields.remove(fieldName);
	}
	
	/**
	 * Clears the internal map
	 */
	public void clearFieldsMap() {
		fields.clear();
	}
	
	/**
	 * Updates the data object on the data handler 
	 * @param data
	 */
	public void setData(Object data) {
		dataHandler.setData(data);
	}
	
	/**
	 * Returns the service
	 *  
	 * @return service
	 */
	public BaseService getService(){
		return svc;
	}
	
	/**
	 * 
	 * @set service
	 */
	public void setService(BaseService service){
		this.svc = service;
	}
	
	/**
	 * 
	 * @return dataHandler
	 */
	public DataHandler<?> getDataHandler(){
		return dataHandler;
	}
	
	/**
	 * 
	 * @return fields map
	 */
	public Map<String, Object> getFieldsMap(){
		return fields;
	}
}
