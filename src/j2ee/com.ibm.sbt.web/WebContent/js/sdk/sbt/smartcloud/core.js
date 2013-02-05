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

/**
 * Social Business Toolkit SDK. 
 * Helpers for the core SmartCloud capabilities
 */
define([ 'sbt/config' ], function(sbt) {

	return sbt.smartcloud = {
		// Namespaces used when parsing the SmartCloud Atom feeds
		namespaces : {
			o : "http://ns.opensocial.org/2008/opensocial",
			app : "http://www.w3.org/2007/app",
			thr : "http://purl.org/syndication/thread/1.0",
			fh : "http://purl.org/syndication/history/1.0",
			snx : "http://www.ibm.com/xmlns/prod/sn",
			opensearch : "http://a9.com/-/spec/opensearch/1.1/",
			a : "http://www.w3.org/2005/Atom",
			h : "http://www.w3.org/1999/xhtml",
			cmis : "http://docs.oasis-open.org/ns/cmis/core/200908/",
			cmism : "http://docs.oasis-open.org/ns/cmis/messaging/200908/",
			cmisra : "http://docs.oasis-open.org/ns/cmis/restatom/200908/",
			lcmis : "http://www.ibm.com/xmlns/prod/sn/cmis",
			sp_0 : "http://www.w3.org/2005/Atom"
		},
	};

});