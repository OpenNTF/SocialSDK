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
define([ "../_bridge/declare","../_bridge/dom","../_bridge/lang",
         "dojo/_base/lang", "dojo/string", "dojo/dom-construct", "dojo/_base/connect", "dojo/touch", "dijit/_WidgetBase", "dijit/_TemplatedMixin"], 
        function(declare, dom, sbtLang, lang, string, domConstruct, connect, touch, _WidgetBase, _TemplatedMixin) {

    /**
     * @module sbt.widget._TemplatedWidget
     */
    var _TemplatedWidget = declare([ dijit._WidgetBase, dijit._TemplatedMixin ], {
        
        _blankGif: dijit._WidgetBase.prototype._blankGif,

        _attachEventAttribute: "data-dojo-attach-event",
        
        _attachPointAttribute: "data-dojo-attach-point",
        
        _place: function(node, refNode, pos) {
                domConstruct.place(node, refNode, pos);
        },
        
        _stopEvent: function(event) {
            dojo.stopEvent(event);
        },
        
        _connect: function(object, event, method) {
            return connect.connect(object, touch[event] || event, method);
        },
         
        _substitute: function(template, map, transformer, thisObject) {
           if (!transformer) {
                 transformer = function(value, key) {
                 if (typeof value == "undefined") {
                     // check the renderer for the property
                     value = sbtLang.getObject(key, false, self);
                 }
                 
                 if (typeof value == "undefined" || value == null) {
                     return "";
                 }
 
                 return value;
                 };
           }
           if (!thisObject) {
                   thisObject = this;
           }
           return string.substitute(template, map, transformer, thisObject);
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
                             var attachPoint = (nodes[i].getAttribute) ? nodes[i].getAttribute(this._attachPointAttribute) : null;
                        if (attachPoint) {
                                var att = nodes[i].getAttribute(this._attachPointAttribute);
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
                                event = sbtLang.trim(eventFunc[0]);
                                func = sbtLang.trim(eventFunc[1]);
                            } else {
                                event = sbtLang.trim(event);
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