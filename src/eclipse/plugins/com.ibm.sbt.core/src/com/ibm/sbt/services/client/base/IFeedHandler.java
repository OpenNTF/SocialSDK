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
package com.ibm.sbt.services.client.base;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * 
 * @author Carlos Manias
 *
 */
public interface IFeedHandler<T extends BaseEntity> {
	
	/**
	 * Returns a new entity from the data on the RequestData object
	 * @param dataHolder
	 * @return
	 * @throws SBTServiceException
	 */
	public T createEntity(Response dataHolder);
	
	/**
	 * Return a new entity from a data object
	 * @param data
	 * @return
	 * @throws SBTServiceException
	 */
	public T createEntityFromData(Object data);
	
	/**
	 * Returns a Collection of entities from the data on the RequestData object
	 * @param dataHolder
	 * @return
	 * @throws SBTServiceException
	 */
	public EntityList<T> createEntityList(Response dataHolder);
	
	/**
	 * 
	 * @return the Service
	 */
	public BaseService getService();
	
}
