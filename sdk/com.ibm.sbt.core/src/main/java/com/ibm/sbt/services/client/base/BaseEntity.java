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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.DataHandlerException;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;
import com.ibm.sbt.services.endpoints.AbstractEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * This class provides acts as a base class for all the entities, implementing some behaviour common to all Entities 
 * @author Carlos Manias
 *
 */
public class BaseEntity implements Externalizable {

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
	 * @return {String}
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
	 * @return {String}
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
	 * @param field
	 * @param value
	 */
	public void setAsString(FieldEntry field, String value) {
		fields.put(field.getName(), value);
	}

	/**
	 * This method updates the value of a field. Used to create or update an entity.
	 * 
	 * @param field
	 * @param value
	 */
	public void setAsString(String field, String value) {
		fields.put(field , value);
	}
	/**
	 * Returns the value of a field as an int
	 * 
	 * @param field
	 * @return {int} returns -1, if the field doesn't exist
	 */
	public int getAsInt(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Integer)fields.get(field.getName());
		}
		if (dataHandler != null){
			return dataHandler.getAsInt(field);
		}
		return -1;
	}
	
	/**
	 * Returns the value of a field as a long
	 * 
	 * @param field
	 * @return null, if the field doesn't exist
	 */
	public Long getAsLong(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Long)fields.get(field.getName());
		}
		if (dataHandler != null){
			return dataHandler.getAsLong(field);
		}
		return null;
	}
	
	/**
	 * Returns the value of a field as a long
	 * 
	 * @param fieldName
	 * @return null, if the field doesn't exist
	 */
	public Long getAsLong(String fieldName){
		if (fields.containsKey(fieldName)){
			return (Long)fields.get(fieldName);
		}
		if (dataHandler != null){
			return dataHandler.getAsLong(fieldName);
		}
		return null;
	}
	
	/**
	 * Returns the value of a field as a float
	 * 
	 * @param field
	 * @return returns -1, if the field doesn't exist
	 */
	public float getAsFloat(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Float)fields.get(field.getName());
		}
		if (dataHandler != null){
			return dataHandler.getAsFloat(field);
		}
		return -1;
	}
	
	/**
	 * Returns the value of a field as a boolean
	 * 
	 * @param field
	 * @return returns false, if the field doesn't exist
	 */
	public boolean getAsBoolean(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (Boolean)fields.get(field.getName());
		}
		if (dataHandler != null){
			return dataHandler.getAsBoolean(field);
		}
		return false;
	}
	
	/**
	 * Returns the value of a field as a boolean
	 * 
	 * @param fieldName
	 * @return returns false, if the field doesn't exist
	 */
	public boolean getAsBoolean(String fieldName){
		if (fields.containsKey(fieldName)){
			return (Boolean)fields.get(fieldName);
		}
		if (dataHandler != null){
			return dataHandler.getAsBoolean(fieldName);
		}
		return false;
	}
	
	/**
	 * Returns the value of a field as a date
	 * 
	 * @param field
	 * @return returns null, if the field doesn't exist
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
		return null;
	}
	
	/**
	 * Returns the value of a field as a date
	 * 
	 * @param fieldName
	 * @return returns null, if the field doesn't exist
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
		return null;
	}
	
	/**
	 * Returns the value of a field as a map
	 * 
	 * @param fields
	 * @return {Map<String, String>}
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
	 * @param fieldNames
	 * @return {Map<String, String>}
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
	 * @return {String[]}
	 */
	public String[] getAsArray(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return (String[])fields.get(field.getName());
		}
		if (dataHandler != null) {
			return dataHandler.getAsArray(field);
		}
		return null;
	}
	
	/**
	 * Returns the value of a field as a map
	 * 
	 * @param fieldName
	 * @return {String[]}
	 */
	public String[] getAsArray(String fieldName){
		if (fields.containsKey(fieldName)){
			return (String[])fields.get(fieldName);
		}
		if (dataHandler != null) {
			return dataHandler.getAsArray(fieldName);
		}
		return null;
	}
	
	/**
	 * Returns true if a field exists
	 * 
	 * @param field
	 * @return {boolean}
	 */
	public boolean exists(FieldEntry field){
		if (fields.containsKey(field.getName())){
			return true;
		}
		if (dataHandler != null) {
			return (dataHandler.getEntry(field) != null);
		}
		return false;
	}
		
	/**
	 * Receives an int as the value of a field and stores it as an Integer on the internal map 
	 * 
	 * @param field
	 * @param value
	 */
	public void setAsInt(FieldEntry field, int value){
		fields.put(field.getName(), new Integer(value));
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
	public void setAsFloat(String fieldName, float value){
		fields.put(fieldName, new Float(value));
	}
	
	/**
	 * Receives a boolean as the value of a field and stores it as a Boolean on the internal map 
	 * 
	 * @param field
	 * @param value
	 */
	public void setAsBoolean(FieldEntry field, boolean value){
		fields.put(field.getName(), new Boolean(value));
	}
		
	/**
	 * Receives a boolean as the value of a field and stores it as a Boolean on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setAsBoolean(String fieldName, boolean value){
		fields.put(fieldName, new Boolean(value));
	}
		
	/**
	 * Receives a long as the value of a field and stores it as a Boolean on the internal map 
	 * 
	 * @param field
	 * @param value
	 */
	public void setAsLong(FieldEntry field, long value){
		fields.put(field.getName(), new Long(value));
	}
		
	/**
	 * Receives a long as the value of a field and stores it as a Boolean on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setAsLong(String fieldName, long value){
		fields.put(fieldName, new Long(value));
	}
		
	/**
	 * Receives a Date as the value of a field and stores it as a Date on the internal map 
	 * 
	 * @param field
	 * @param value
	 */
	public void setAsDate(FieldEntry field, Date value){
		setAsDate(field.getName(), value);
	}
	
	/**
	 * Receives a Date as the value of a field and stores it as a Date on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setAsDate(String fieldName, Date value){
		fields.put(fieldName, value);
	}
	
	/**
	 * Receives an array as the value of a field and stores it as an array on the internal map 
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setAsArray(String fieldName, String[] value){
		fields.put(fieldName, value);
	}
	
	/**
	 * Receives an array as the value of a field and stores it as an array on the internal map 
	 * 
	 * @param field
	 * @param value
	 */
	public void setAsArray(FieldEntry field, String[] value){
		fields.put(field.getName(), value);
	}
	
	/**
	 * Receives an object as the value of a field and stores it as an object on the internal map 
	 * 
	 * @param field
	 * @param value
	 */
	public void setAsObject(FieldEntry field, Object value){
		fields.put(field.getName(), value);
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
	 * sets the service
	 */
	public void setService(BaseService service){
		this.svc = service;
	}
	
	/**
	 * 
	 * @return {DataHandler<?>} dataHandler
	 */
	public DataHandler<?> getDataHandler(){
		return dataHandler;
	}
	
	public void setDataHandler(DataHandler<?> dataHandler){
		this.dataHandler = dataHandler;
	}
	/**
	 * 
	 * @return fields map
	 */
	public Map<String, Object> getFieldsMap(){
		return fields;
	}
	
	@Override
	public void readExternal(ObjectInput inputStream) throws IOException, ClassNotFoundException {
		// Retrieve the entity data(handler), service and endpoint name
		Map<String, Object> changedfields = (Map<String, Object>) inputStream.readObject();
		DataHandler<?> dataHandler = (DataHandler<?>) inputStream.readObject();
		String endpointName = inputStream.readUTF();
		BaseService service = (BaseService) inputStream.readObject();
		Endpoint endPoint = EndpointFactory.getEndpoint(endpointName);

		fields = changedfields;
		setDataHandler(dataHandler);
		service.setEndpoint(endPoint);
		setService(service);
	}

	@Override
	public void writeExternal(ObjectOutput outputStream) throws IOException {
	
		// Serialize the data elements
		outputStream.writeObject(fields);	   // persist fields that could have been changed
		outputStream.writeObject(dataHandler); 

		// persist the endpoint name
		outputStream.writeUTF(((AbstractEndpoint)getService().getEndpoint()).getName());

		// persist the service name
		outputStream.writeObject(getService());
	}

}
