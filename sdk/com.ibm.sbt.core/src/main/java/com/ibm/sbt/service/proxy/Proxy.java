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

package com.ibm.sbt.service.proxy;


/**
 * Defines Proxy prefernces
 * <p>
 * Proxy stores preferences to be used by a Proxy implementation like ProxyUrl
 * </p>
 * @author Manish Kataria
 */

public interface Proxy {
	public String getProxyUrl();
	public String rewriteUrl(String apiUrl);
	public String getProxyModule();

}
