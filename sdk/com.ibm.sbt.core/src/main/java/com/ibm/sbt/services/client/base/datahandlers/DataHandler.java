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

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This abstract class defines an interface to decouple the data format used so that it can be be replaced easily for another format. 
 * @author Carlos Manias
 *
 * @param <DataType>
 */
public interface DataHandler<DataType> {
	
	public String getAsString(FieldEntry field);
	public String getAsString(String field);
	public Date getAsDate(FieldEntry fieldName, final Locale locale) throws DataHandlerException;
	public Date getAsDate(FieldEntry field) throws DataHandlerException;
	public Date getAsDate(String field) throws DataHandlerException;
	public Date getAsDate(String field, final Locale locale) throws DataHandlerException;
	public int getAsInt(FieldEntry fieldName);
	public int getAsInt(String fieldName);
	public float getAsFloat(FieldEntry fieldName);
	public float getAsFloat(String fieldName);
	public boolean getAsBoolean(FieldEntry fieldName);
	public boolean getAsBoolean(String fieldName);
	public List<DataType> getEntries(FieldEntry path);
	public List<DataType> getEntries(String path);
	public DataType getEntry(FieldEntry path);
	public DataType getEntry(String path);
	public void setData(final Object data);
	public Object getData();
	public Long getAsLong(String fieldName);
	public Long getAsLong(FieldEntry fieldName);
	public String[] getAsArray(String fieldName);
	public String[] getAsArray(FieldEntry fieldName);
	
}

