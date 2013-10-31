/*
 * © Copyright IBM Corp. 2011
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
package sbt;

import com.ibm.sbt.services.client.connections.profiles.ProfileService;
import com.ibm.sbt.services.endpoints.Endpoint;

public class ConnectionsProfileService extends ProfileService {

	public ConnectionsProfileService() {
		super(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	public ConnectionsProfileService(String endpointName) {
		super(endpointName, DEFAULT_CACHE_SIZE);
	}

	public ConnectionsProfileService(Endpoint endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}
}
