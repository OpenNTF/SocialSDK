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

package com.ibm.sbt.services.util.navigable;

import java.util.Collection;
import java.util.Set;
import com.ibm.sbt.services.util.extractor.field.DataExtractor;

/**
 * @author Carlos Manias
 */
public class NavigableObject<DataFormat> {
	protected final DataExtractor<DataFormat>	extractor;
	protected final DataFormat					data;

	/**
	 * Constructor
	 * 
	 * @param _data
	 *            the data
	 * @param _extractor
	 *            the data extractor
	 */
	public NavigableObject(DataFormat _data, DataExtractor<DataFormat> _extractor) {
		this.extractor = _extractor;
		this.data = _data;
	}

	/**
	 * Returns the value of a field within the data object
	 * 
	 * @param fieldName
	 *            the field name
	 * @return value the value of the field
	 */
	public String get(String fieldName) {
		return extractor.get(data, fieldName);
	}

	/**
	 * Returns the data object
	 * 
	 * @return data the data
	 */
	public DataFormat getData() {
		return data;
	}

	/**
	 * Returns the known fields from the data extractor
	 * 
	 * @return fields the known fields
	 */
	public Set<String> getAllFields() {
		return extractor.getKnownFields();
	}

	/**
	 * Returns a collection of entities of the given data format
	 * 
	 * @return entities the entities
	 */
	public Collection<DataFormat> getEntities() {
		return extractor.getEntitiesFromServiceResult(data);
	}

	/**
	 * Returns a collection of entities of the given data format extracted from the field
	 * 
	 * @param field
	 *            the field
	 * @return entities the entities
	 */
	public Collection<DataFormat> getEntities(String field) {
		return extractor.getNestedEntities(data, field);
	}
}
