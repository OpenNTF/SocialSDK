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
package com.ibm.sbt.services.client.smartcloud.bss;

import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * Callback interface used to notify a client when the state changes for a specific entity.
 *  
 * @author mwallace
 */
public interface StateChangeListener {
	
	/**
	 * Called when the entity state changes to the specified value.
	 * 
	 * @param jsonEntity
	 */
	void stateChanged(JsonEntity jsonEntity);
}
