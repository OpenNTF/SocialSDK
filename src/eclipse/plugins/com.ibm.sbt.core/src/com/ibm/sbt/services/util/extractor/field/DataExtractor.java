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

package com.ibm.sbt.services.util.extractor.field;

import java.util.List;
import java.util.Set;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.smartcloud.files.FileEntry;

/**
 * The DataExtractor encapsulates every access to the backing data of a specific entity Access is performed
 * trough queries. A query is specific for each backing implementation, may be a reference to an extractor or
 * an expression to be executed to retrieve the data.
 * 
 * @see XMLFieldExtractor XMLFieldExtractor
 * @see JSONFieldExtractor JSONFieldExtractor
 * @author Carlos Manias
 */
public interface DataExtractor<DataFormat> {

	/**
	 * Returns the value of a field from the data object
	 * 
	 * @param data
	 *            the data object
	 * @param query
	 *            the query to retrieve the field's data
	 * @return value the value of the field
	 */
	public String get(DataFormat data, String query);

	/**
	 * Returns the known fields
	 * 
	 * @return fields the known fields
	 */
	public Set<String> getKnownFields();

	/**
	 * This method returns a collection of entities executing the given query
	 * 
	 * @param data
	 *            the backing data for the object
	 * @param query
	 *            is the query to be executed
	 * @return a data object of the same format of the backing data, suitable to be used as backing data for
	 *         another entity
	 * @see {@link FileEntry.UserProfile}
	 */
	public List<DataFormat> getNestedEntities(DataFormat data, String query);

	/**
	 * This method returns a collection of entities executing a default query. The default query is used by
	 * the {@link BaseService} to extract data from a service result when a service returns multiple entities
	 * in a single call.
	 * 
	 * @param data
	 *            the backing data for the object
	 * @param query
	 *            is the query to be executed
	 * @return a data object of the same format of the backing data, suitable to be used as backing data for
	 *         another entity
	 * @see {@link FileEntry.UserProfile}
	 */
	public List<DataFormat> getEntitiesFromServiceResult(DataFormat data);

}
