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
 * 
 */
define([ "../_bridge/declare",
         "dojo/_base/lang", "dojo/string", "dojo/dom-construct", "dojo/_base/connect", "dojo/touch", "dijit/_WidgetBase", "dijit/_TemplatedMixin"], 
        function(declare, lang, string, domConstruct, connect, touch, _WidgetBase, _TemplatedMixin) {

    /**
     * @module sbt.widget._TemplatedWidget
     */
    var _TemplatedWidget = declare([ dijit._WidgetBase, dijit._TemplatedMixin ], {
        
        _blankGif: dijit._WidgetBase.prototype._blankGif,

        _place: function(node, refNode, pos) {
        	domConstruct.place(node, refNode, pos);
        },
        
        _stopEvent: function(event) {
            dojo.stopEvent(event);
        },
        
        _connect: function(object, event, method) {
            // TODO handle disconnecting when widget is destroyed
            return connect.connect(object, touch[event] || event, method);
        },
        
        _mixin: function(dest,sources) {
        	return lang.mixin.apply(this, arguments);
        },
        
        _destroy: function(node) {
            domConstruct.destroy(node);
        },
        
        _create: function(name, attribs, parent) {
            return domConstruct.create(name, attribs, parent);
        },
        
        _toDom: function(template, parent) {
            return domConstruct.toDom(template, parent);
        },
        
        _isString: function(obj) {
            return lang.isString(obj);
        },
        
        _substitute: function(template, map, transform, thisObject) {
        	return string.substitute(template, map, transform, thisObject);
        },
        
        _getObject: function(name, create, context) {
            return lang.getObject(name, create, context);
        },
        
        _hitch: function(scope, method) {
            if (arguments.length > 2) {
                return lang._hitchArgs.apply(dojo, arguments);
            } else {
                return lang.hitch(scope, method);
            }
        },
        
        _doAttachPoints: function(scope,el){
       	 var nodes = (el.all || el.getElementsByTagName("*"));
	            for (var i in nodes) {
	                var attachPoint = (nodes[i].getAttribute) ? nodes[i].getAttribute("data-dojo-attach-point") : null;
	                if (attachPoint) {
	                	
	                	var att = nodes[i].getAttribute("data-dojo-attach-point");
	                	scope[att] = nodes[i];
	                }
	            }
       },      
        _doAttachEvents: function(el, scope) {
            var nodes = (el.all || el.getElementsByTagName("*"));
            for (var i in nodes) {
                var attachEvent = (nodes[i].getAttribute) ? nodes[i].getAttribute(this.AttachEventAttribute) : null;
                if (attachEvent) {
                    nodes[i].removeAttribute(this.AttachEventAttribute);
                    var event, events = attachEvent.split(/\s*,\s*/);
                    while((event = events.shift())) {
                        if (event) {
                            var func = null;
                            if (event.indexOf(":") != -1) {
                                var eventFunc = event.split(":");
                                event = this._trim(eventFunc[0]);
                                func = this._trim(eventFunc[1]);
                            } else {
                                event = this._trim(event);
                            }
                            if (!func) {
                                func = event;
                            }
                            var callback = this._hitch(this, this[func], nodes[i], scope);
                            this._connect(nodes[i], event, callback);
                        }
                    }
                }
            }
        }
    
    });
    
    return _TemplatedWidget;
});