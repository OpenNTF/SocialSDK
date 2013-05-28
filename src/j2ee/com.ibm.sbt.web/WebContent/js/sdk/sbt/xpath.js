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
 * Social Business Toolkit SDK - XPath utilities.
 * 
 * @module sbt.xpath
 */
define(['./declare'],function(declare) {
	/*
	 * @class sbt.xpath.XPathExpr
	 */
	var XPathExpr = declare(null, {
		ie:		false,
		constructor: function(xpath, nsArray){
		     this.xpath = xpath;    
		     this.nsArray = nsArray || {};
		     if (!document.createExpression) {
		    	 this.ie = true;
		         this.nsString = "";
		         if (this.nsArray) {
		             for(var ns in this.nsArray) {
		                 this.nsString += ' xmlns:' + ns + '="' + this.nsArray[ns] + '"';
		             }
		         }
		     }
		},
	
		selectSingleNode : function(xmlDomCtx) {
			var doc = xmlDomCtx.ownerDocument || xmlDomCtx;
			if (this.ie) {
				try {
					doc.setProperty("SelectionLanguage", "XPath");
					doc.setProperty("SelectionNamespaces", this.nsString);
					if (xmlDomCtx === doc) xmlDomCtx = doc.documentElement;
					return xmlDomCtx.selectSingleNode(this.xpath);
				} catch (ex) {
					throw "XPath is not supported";
				}
			} else {
				var _this = this;
				var result = doc.evaluate(this.xpath, xmlDomCtx,
					function(prefix) {
						return _this.nsArray[prefix];
					}, XPathResult.FIRST_ORDERED_NODE_TYPE, null);
				return result.singleNodeValue;
			}
		},	
		
        selectNumber : function(xmlDomCtx){
            var doc = xmlDomCtx.ownerDocument || xmlDomCtx;
            if (this.ie) {
                try {
                    doc.setProperty("SelectionLanguage", "XPath");
                    doc.setProperty("SelectionNamespaces", this.nsString);
                    if (xmlDomCtx === doc) xmlDomCtx = doc.documentElement;
                    return xmlDomCtx.selectNodes(this.xpath).length;
                } catch (ex) {
                    throw "XPath is not supported";
                }
            } else {
                var _this = this;
                var result = doc.evaluate(this.xpath, xmlDomCtx,
                    function(prefix) {
                        return _this.nsArray[prefix];
                    }, XPathResult.NUMBER_TYPE, null);
                return result.numberValue;
            }
        },
		
		selectNodes : function(xmlDomCtx) {
			var doc = xmlDomCtx.ownerDocument || xmlDomCtx;
			if (this.ie) {
				try {
					doc.setProperty("SelectionLanguage", "XPath");
					doc.setProperty("SelectionNamespaces", this.nsString);
					if (xmlDomCtx === doc) xmlDomCtx = doc.documentElement;
					return xmlDomCtx.selectNodes(this.xpath);
				} catch (ex) {
					throw "XPath is not supported";
				}
			} else {
				var _this = this;
				var result = doc.evaluate(this.xpath, xmlDomCtx, 
					function(prefix) {
						return _this.nsArray[prefix];
					}, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
				var r = [];
				for(var i = 0; i < result.snapshotLength; i++) {
					r.push(result.snapshotItem(i));
				}
				return r;
			}
		},
		
		selectText : function(node) {
			var result = this.selectSingleNode(node);
			return result ? (result.text || result.textContent) : null;
		}
	});
	
	return {
		/**
		 * Selects nodes from XML data object
		 * 
		 * @method selectNodes
		 * @param {Object}
		 *            node xml data to be parsed
		 * @param {String}
		 *            xpath xpath expression
		 * @param {Array}
		 *            nsArray Array of namespaces for the xml.
		 * @return {Array} Array of nodes
		 * @static
		 */
		selectNodes : function(node, xpath, nsArray) {
			var expr = new XPathExpr(xpath, nsArray);
			return expr.selectNodes(node);
		},

		/**
		 * Selects single node from XML data object
		 * 
		 * @method selectSingleNode
		 * @param {Object}
		 *            node xml data to be parsed
		 * @param {String}
		 *            xpath xpath expression
		 * @param {Array}
		 *            nsArray Array of namespaces for the xml.
		 * @return {Object} selected node object
		 * @static
		 */
		selectSingleNode : function(node, xpath, nsArray) {
			var expr = new XPathExpr(xpath, nsArray);
			return expr.selectSingleNode(node);
		},
		

		/**
		 * Selects text from a single node from XML data object
		 * 
		 * @method selectText
		 * @param {Object}
		 *            node xml data to be parsed
		 * @param {String}
		 *            xpath xpath expression
		 * @param {Array}
		 *            nsArray Array of namespaces for the xml.
		 * @return {String} inner text of the node object
		 * @static
		 */
		selectText : function(node, xpath, nsArray) {
			var expr = new XPathExpr(xpath, nsArray);
			return expr.selectText(node);
		},
		
		/**
		 * 
		 * @param node
		 * @param xpath
		 * @param nsArray
		 * @returns
		 */
		selectNumber : function(node, xpath, nsArray){
		    var expr = new XPathExpr(xpath, nsArray);
		    return expr.selectNumber(node);
		}
	};
});