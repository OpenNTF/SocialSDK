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
 * Helpers for the base capabilities of data handlers.
 */
define(['sbt/_bridge/declare','sbt/xml','sbt/xpath'],function(declare, xml, xpath) {

	var BaseHandler = declare("sbt.base.BaseHandler", null, {	
		
		_dataType : "base",
		
		constructor : function (options){
		},
		
		_extractEntryFromSingleEntryFeed : function (data, ioArgs){			
			return data;
		},
		
		_extractIdFromEntry : function(entryData){
			return null;			
		},
		
		_extractSummaryFromEntitiesFeed : function (data, ioArgs){
			return null;
		},
		_extractEntriesFromEntitiesFeed : function (data, ioArgs){
			return data;
		},
		_constructPayload : function (requestArgs){
			return null;
		}
	
	});
	return BaseHandler;
});