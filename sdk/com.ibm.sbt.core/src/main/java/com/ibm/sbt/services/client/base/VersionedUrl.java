/* * ©© Copyright IBM Corp. 2014 * 
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

	
/**
 * 
 * A url for a particular version of Connections
 * 
 * @author Carlos Manias
 *
 */
public class VersionedUrl {
	private final Version version;
	private URLPattern urlPattern;

	public VersionedUrl(Version version, String urlPattern) {
		this.version = version;
		this.urlPattern = new URLPattern(urlPattern);
	}
	
	public Version getVersion(){
		return version;
	}

	public URLPattern getUrlPattern(){
		return urlPattern;
	}

	public String format(NamedUrlPart... args){
		return urlPattern.format(args);
	}

}
