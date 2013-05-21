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
 * Helpers for handling xml data from the feed.
 */
define(['../declare', './BaseHandler','../xml','../xpath'],function(declare, BaseHandler, xml, xpath) {

	var XmlHandler = declare(BaseHandler, {	
		
		_dataType : "xml",
		_xpath_map : null,
		_xpath_feed_map : null,
		_nameSpaces : null,

		constructor : function (options){
			this._xpath_map = options.xpath_map;
			this._xpath_feed_map = options.xpath_feed_map;
			this._nameSpaces = options.nameSpaces;
		},
		
		_extractEntryFromSingleEntryFeed : function (data, ioArgs){			
			var entry = xpath.selectNodes(xml.parse(data), this._xpath_map["entry"], this._nameSpaces);
			var node = entry[0];
			return node;
		},
		
		_extractIdFromEntry : function(entryData){
			return xpath.selectText(entryData ,this._xpath_map["uid"], this._nameSpaces);			
		},
		
		_extractSummaryFromEntitiesFeed : function (data, ioArgs){
			var xmlData = xml.parse(data); 
			var summary = {};
			summary.totalResults = xpath.selectText(xmlData, this._xpath_feed_map.totalResults, this._nameSpaces);
			summary.startIndex = xpath.selectText(xmlData, this._xpath_feed_map.startIndex, this._nameSpaces);
			summary.itemsPerPage = xpath.selectText(xmlData, this._xpath_feed_map.itemsPerPage, this._nameSpaces);	
			return summary;
		},
		_extractEntriesFromEntitiesFeed : function (data, ioArgs){
			var xmlData = xml.parse(data);	
			var entities = xpath.selectNodes(xmlData, this._xpath_feed_map["entry"], this._nameSpaces);
			return entities;
		},
		_constructPayload : function (requestArgs){
			if(requestArgs.xmlPayload){
				return requestArgs.xmlPayload;
			}else{
				var retXml = requestArgs.xmlPayloadTemplate.replace(/{(\w*)}/g,function(m,key){return requestArgs.xmlData.hasOwnProperty(key)?xml.encodeXmlEntry(requestArgs.xmlData[key]):"";});
				return retXml;
			}
		}
	
	});
	return XmlHandler;
});