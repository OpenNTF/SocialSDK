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
define(['./jquery'], function() {
	return {
        byId: function(id) {
        	if (typeof id === 'string') {
        		return jQuery("#"+id)[0];
        	} else {
        		return id;
        	}
        },
        createTextNode: function(text) {
            return document.createTextNode(text);
        },
        create: function(element, props, refNode) {
        	var node = jQuery(element).attr(props);
        	if (refNode) {
        		node.appendTo(jQuery(refNode));
        	}
			return node[0];
		},
		destroy: function(node) {
		    jQuery(node).remove();
		},
        removeAll: function(id) {
            var node = this.byId(id);
            if (node) {
                while (node.firstChild) {
                    node.removeChild(node.firstChild);
                }
            }
            return node;
        },
        setText: function(id,text) {
            var node = this.byId(id);
            if (node) {
                this.removeAll(id);
                node.appendChild(this.createTextNode(text));        
            }
            return node;
        },  
        setAttr: function(id,attrName,attrValue) {
            var node = this.byId(id);
            if (node) {
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