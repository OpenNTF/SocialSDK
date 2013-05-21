/*
 * © Copyright IBM Corp. 2013
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
 * I18n utilities
 */
define(["./lang", "./xpath", "./base/core"], function(lang, xpath, core) {
    
    var XPathCountFunction = /^count\(.*\)$/;

    /**
     * @module sbt.itemFactory
     */
    return {
        
        createItems: function(document, attributes, thisObject, decoder) {
            var nodes = xpath.selectNodes(document, core.feedXPath.entry, core.namespaces);
            var items = [];
            if (nodes.length == 0) {
                nodes = xpath.selectNodes(document, "a:entry", core.namespaces);
            } 
            for (var i=0; i<nodes.length; i++) {
                items.push(this.createItem(nodes[i], attributes, thisObject, decoder));
            }
            return items;
        },
        
        createItem: function(element, attributes, thisObject, decoder) {
            // TODO add item.index and item.attribs
            var item = { 
                element : element,
                getValue : function(attrib) { return this[attrib]; }
            };
            var attribs = this.getAttribs(attributes);
            for (var i=0; i<attribs.length; i++) {
                var attrib = attribs[i];
                var access = attributes[attrib];
                if (lang.isFunction(access)) {
                    item[attrib] = access(thisObject, item);
                } else if (access.match(XPathCountFunction)){
                    item[attrib] = xpath.selectNumber(element, access, core.namespaces);
                } else {
                    var nodes = xpath.selectNodes(element, access, core.namespaces);
                    if (nodes && nodes.length == 1) {
                        item[attrib] = nodes[0].text || nodes[0].textContent;
                    } else if (nodes) {
                        item[attrib] = [];
                        for (var j=0; j<nodes.length; j++) {
                            item[attrib].push(nodes[j].text || nodes[j].textContent);
                        }
                    } else {
                        item[attrib] = null;
                    }
                }
                
                item[attrib] = (decoder) ? decoder.decode(item[attrib]) : item[attrib];
            }
            return item;
        },
        
        getAttribs: function(attributes) {
            var attribs = [];
            for (var name in attributes) {
                if (attributes.hasOwnProperty(name)) {
                    attribs.push(name);
                }
            }
            return attribs;
        },
        
    };
    
});