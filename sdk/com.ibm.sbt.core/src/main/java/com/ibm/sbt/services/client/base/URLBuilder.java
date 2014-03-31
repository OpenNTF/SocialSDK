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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * URLBuilder provides a unified way to generate urls
 * 
 * @author Carlos Manias
 */
public class URLBuilder {
	private final TreeMap<Version, URLPattern> urlVersions;
    private static Pattern paramsPattern = Pattern.compile("\\{(.*?)\\}");
	private static final String START_GROUP = "{";
	private static final String END_GROUP = "}";
	private static final String ESCAPED_START_GROUP = "\\{";
	private static final String ESCAPED_END_GROUP = "\\}";
	private static final String EMPTY_STRING = "";
	
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
		NamedUrlPart authType = service.getAuthType();
		Map<String, String> serviceMappings = service.getEndpoint().getServiceMappings();
		List<NamedUrlPart> namedParts = Arrays.asList(args);
		namedParts.add(authType);
		String url = urlPattern.format(namedParts);
		url = substituteServiceMapping(url, serviceMappings);
		return url;
	}

	public String format(Endpoint endpoint, NamedUrlPart... args) {
		URLPattern urlPattern = getPattern(Version.parse(endpoint.getApiVersion()));
		NamedUrlPart authType = new NamedUrlPart("authType",AuthType.getAuthTypePart(endpoint));
		Map<String, String> serviceMappings = endpoint.getServiceMappings();
		List<NamedUrlPart> namedParts = Arrays.asList(args);
		namedParts.add(authType);
		String url = urlPattern.format(namedParts);
		url = substituteServiceMapping(url, serviceMappings);
		return url;
	}

	protected String substituteServiceMapping(String url, Map<String, String> serviceMappings){
        Matcher paramsMatcher = paramsPattern.matcher(url);
        
        while(paramsMatcher.find()){
            String subOut = paramsMatcher.group(1);
            String subIn = serviceMappings.get(subOut);
            if(subIn != null){
                return url.replaceFirst(ESCAPED_START_GROUP + subOut + ESCAPED_END_GROUP, subIn);
            }
        }
        
        return url.replace(START_GROUP, EMPTY_STRING).replace(END_GROUP, EMPTY_STRING);
    }
	
}
