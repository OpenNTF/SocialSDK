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
 * Social Business Toolkit SDK - Some DOM utilities.
 */
define([],function() {
	return {
		addClass: function(node,className){
			return dojo.addClass(node,className);
		},
		byId: function(id) {
			return dojo.byId(id);
		},
		createTextNode: function(text) {
			return dojo.doc.createTextNode(text);
		},
		create: function(element, props, refNode) {
			return dojo.create(element, props, refNode);
		},
        destroy: function(node) {
            return dojo.destroy(node);
        },
		removeAll: function(id) {
			var node = this.byId(id);
			if(node) {
				while(node.firstChild) {
				    node.removeChild(node.firstChild);
				}
			}
			return node;
		},
		removeClass: function(node, className){
			return dojo.removeClass(node,className);
		},
		setText: function(id,text) {
			var node = this.byId(id);
			if(node) {
				this.removeAll(id);
				node.appendChild(this.createTextNode(text)); 		
			}
			return node;
		},	
		setAttr: function(id,attrName,attrValue) {
			var node = this.byId(id);
			if(node) {
				if(attrValue) {
					node.setAttribute(attrName,attrValue);
				} else {
					node.removeAttribute(attrName);
				}
			}
			return node;
		}	
	};
});