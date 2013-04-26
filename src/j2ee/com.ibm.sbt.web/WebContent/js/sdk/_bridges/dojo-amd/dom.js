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
define(['dojo/dom','dojo/_base/window', 'dojo/dom-construct', 'dojo/dom-class'],function(dom,win,domConstruct,domClass) {
	return {
		byId: function(id) {
			return dom.byId(id);
		},
		createTextNode: function(text) {
			//return dojo.doc.createTextNode(text);
			//change also made to define, added 'dojo/_base/window'
			return win.doc.createTextNode(text);
		},
		create: function(element, props, refNode) {
			return domConstruct.create(element, props, refNode);
		},
        destroy: function(node) {
            return domConstruct.destroy(node);
        },
		removeAll: function(node) {
			node = this.byId(node);
			if(node) {
				while(node.firstChild) node.removeChild(node.firstChild);
			}
            return node;
		},
		setText: function(node,text) {
			node = this.byId(node);
			if(node) {
				this.removeAll(node);
				node.appendChild(this.createTextNode(text)); 		
			}
            return node;
		},	
		setAttr: function(node,attr,text) {
			node = this.byId(node);
			if(node) {
				if(text) {
					node.setAttribute(attr,text);
				} else {
					node.removeAttribute(attr);
				}
			}
            return node;
		}	
	};
});