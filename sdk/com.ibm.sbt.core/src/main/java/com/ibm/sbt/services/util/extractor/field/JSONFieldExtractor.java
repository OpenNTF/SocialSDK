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
import com.ibm.commons.util.NotImplementedException;
import com.ibm.commons.util.io.json.JsonObject;

/**
 * @author Carlos Manias
 */
public class JSONFieldExtractor implements DataExtractor<JsonObject> {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.sbt.services.util.extractor.FieldExtractor#get(com.ibm.sbt.services.util.navigable.NavigableObject
	 * , java.lang.String)
	 */
	@Override
	public String get(JsonObject data, String fieldName) {
		throw new NotImplementedException();
	}

	@Override
	public Set<String> getKnownFields() {
		throw new NotImplementedException();
	}

	@Override
	public List<JsonObject> getNestedEntities(JsonObject Data, String field) {
		throw new NotImplementedException();
	}

	@Override
	public List<JsonObject> getEntitiesFromServiceResult(JsonObject data) {
		// TODO Auto-generated method stub
		return null;
	}
}
