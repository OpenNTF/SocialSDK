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
 * Social Business Toolkit SDK - XML utilities.
 */
define(['./lang'], function(lang) {
	var xml_to_encoded = {
		'&': '&amp;',
		'"': '&quot;',
		'<': '&lt;',
		'>': '&gt;'
	};

	var encoded_to_xml = {
		'&amp;': '&',
		'&quot;': '"',
		'&lt;': '<',
		'&gt;': '>'
	};
		
	return {
		/**
		 * XML Parser.
		 * This function parses an XML string and returns a DOM.
		 */
		parse: function(xml) {
			var xmlDoc=null;
			try {
				if(navigator.appName == 'Microsoft Internet Explorer'){
					xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
					xmlDoc.async="false";
					xmlDoc.loadXML(xml);
				}else{
					if(window.DOMParser){
						parser=new DOMParser();
						xmlDoc=parser.parseFromString(xml,"text/xml");
					}
				}
			}catch(ex){
				console.log(ex.message);
			}
			return xmlDoc;
		},
		asString: function(xmlDoc) {
			if (xmlDoc==null) {
				return "";
			} else if(window.ActiveXObject){
				return xmlDoc.xml;
			} else {
				return (new XMLSerializer()).serializeToString(xmlDoc);
			}
		},
		getText : function (xmlElement){
			if(navigator.appName == 'Microsoft Internet Explorer'){
				return xmlElement.text;
			}else{
				return xmlElement.textContent;
			}
		},
		encodeXmlEntry: function(string) {
		    if (lang.isArray(string)) {
		        string = string.join();
		    }
		    if (!lang.isString(string)) {
		        string = string.toString();
		    }
			return string.replace(/([\&"<>])/g, function(str, item) {
				return xml_to_encoded[item];
			});
		},
		decodeXmlEntry: function (string) {
			return string.replace(/(&quot;|&lt;|&gt;|&amp;)/g,function(str, item) {
				return encoded_to_xml[item];
			});
		}
	};
});