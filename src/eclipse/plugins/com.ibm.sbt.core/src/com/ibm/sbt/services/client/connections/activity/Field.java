package com.ibm.sbt.services.client.connections.activity;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

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
