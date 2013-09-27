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

package com.ibm.sbt.services.client.connections.activity;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;
/**
 * Abstract Class representing Field
 * @author Vimal Dhupar
 *
 */
public abstract class Field extends BaseEntity{

	protected String TYPE ;
	
	public Field() {
	}
	
	public Field(String type) {
		setType(type);
	}

	public Field(String type, BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
		setType(type);
	}
	
	public Field(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public String getFid() {
		return getAsString(ActivityXPath.fieldFid);
	}
	
	public String getHidden() {
		return getAsString(ActivityXPath.fieldHidden);
	}
	
	public String getName() {
		return getAsString(ActivityXPath.fieldName);
	}
	
	public String getPosition() {
		return getAsString(ActivityXPath.fieldPosition);
	}
	
	public String getType() {
		if(StringUtil.isNotEmpty(TYPE))
			return TYPE;
		return getAsString(ActivityXPath.fieldType);
	}
	
	public String getFieldValue() {
		return getAsString(ActivityXPath.field);
	}
	
	public void setFid(String fid) {
		setAsString(ActivityXPath.fieldFid, fid);
	}
	
	public void setHidden(String hidden) {
		setAsString(ActivityXPath.fieldHidden, hidden);
	}
	
	public void setFieldName(String name) {
		setAsString(ActivityXPath.fieldName, name);
	}
	
	public void setPosition(int position) {
		String positionStr = "\"" + position + "\"";
		setAsString(ActivityXPath.fieldPosition, positionStr);
	}
	
	public void setType(String type) {
		setAsString(ActivityXPath.fieldType, type);
		this.TYPE = type;
	}
	
	public void setFieldValue(String value) {
		setAsString(ActivityXPath.field, value);
	}
}
