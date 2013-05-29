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

 * Social Business Toolkit SDK - XSL utilities.
 * Borrowed from the Connections source code, for now
 */

/**
 * @module sbt.xsl
 */
define(['./Cache','./xml','./lang'],function(cache,sbtml,lang) {
	return {
	
		/**
		 * Transform an XML document using an XSL stylesheet.
		 * The XML document can be either a string or an actual DOM document. In case of a
		 * string, if it starts with "http", then the string is read from the URL. Then, it
		 * is parsed to a DOM document.
		 * The XSL must be a string
		 */
		xsltTransform: function(xml,xsl) {
			if(!xml) return null;
			
			// Resolve the XML if it is a URL
			if(lang.isString(xml)) {
				xml = sbtml.parse(sbt.cache.get(xml));
			}
	
			// Resolve the XSL if it is a URL
			if(!xsl) return lang.clone(xml);
			xsl = sbt.cache.get(xsl);
			
			// Run the transformation
			if(window.ActiveXObject) {
				return xml.transformNode(xsl);
			} else if(document.implementation && document.implementation.createDocument) {
				var xslt = new XSLTProcessor();
				xslt.importStyleSheet(xsl);
				return xslt.transformToFragment(xml,document);
			}
			
			// No XSLT engine is available, just return the document as is
			return lang.clone(xml);
		}
	};
});