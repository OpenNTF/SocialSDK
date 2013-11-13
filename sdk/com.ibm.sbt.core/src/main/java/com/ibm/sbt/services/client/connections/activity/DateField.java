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

import java.util.Date;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;
/**
 * Class representing the Date Field
 * @author Vimal Dhupar
 *
 */
public class DateField extends Field{

	public DateField() {
		super("date");
	}
	
	public DateField(BaseService svc, DataHandler<?> handler) {
		super("date", svc,handler);
	}
	
	public DateField(Date date) {
		super("date");
		setDate(date);
	}

	public void setDate(Date date) {
		setAsDate(ActivityXPath.field.getName(), date);
	}
	
	public Date getDate() {
		return getAsDate(ActivityXPath.field);
	}
}
