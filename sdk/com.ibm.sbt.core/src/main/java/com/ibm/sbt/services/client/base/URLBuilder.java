/* * �� Copyright IBM Corp. 2014 * 
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

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * URLBuilder provides a unified way to generate urls
 * 
 * @author Carlos Manias
 */
public class URLBuilder {
	private final TreeMap<Version, URLPattern> urlVersions;
	
	public URLBuilder(VersionedUrl... args) {
		urlVersions = new TreeMap<Version, URLPattern>();
		for (int i = 0; i < args.length; i++) {
			urlVersions.put(args[i].getVersion(), args[i].getUrlPattern());
		}
	}

	/**
	 * Returns URL pattern for the specified version
	 * <p>
	 * Returns an entry for a version of Connections less than or equal the version requested
	 * </p>
	 * @param version
	 * @return
	 */
	public URLPattern getPattern(Version version){
		Entry<Version, URLPattern> entry = urlVersions.floorEntry(version);
		if (entry == null) {
			throw new IllegalArgumentException("No support found for Connections version "+version.toString());
		}
		return entry.getValue();
	}

	/**
	 * Returns the formatted URL for the specified version of Connections
	 * @param service
	 * @param args
	 * @return
	 */
	public String format(BaseService service, NamedUrlPart... args) {
		URLPattern urlPattern = getPattern(service.getApiVersion());
		NamedUrlPart[] serviceMappings = service.getServiceMappings();
		NamedUrlPart[] namedParts = Arrays.copyOf(args, args.length + 2 + serviceMappings.length);
		namedParts[args.length] = service.getAuthType();

		String isAuthenticated = "anonymous";
		Endpoint ep = service.getEndpoint();

		if(ep != null){
			try {
				if(ep.isAuthenticated()){
					isAuthenticated = "";
				}
			} catch (ClientServicesException e) {
				e.printStackTrace();
			}
		}
		namedParts[args.length + 1] = new NamedUrlPart("authenticated", isAuthenticated);

		System.arraycopy(serviceMappings, 0, namedParts, args.length + 2, serviceMappings.length);

		return urlPattern.format(namedParts);
	}
}
