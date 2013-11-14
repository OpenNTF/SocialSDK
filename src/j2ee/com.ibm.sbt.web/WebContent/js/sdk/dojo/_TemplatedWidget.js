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
         "dijit/_Widget", "dijit/_Templated"], 
        function(declare, _Widget, _Templated) {

    /**
     * @module sbt.widget._TemplatedWidget
     */
    var _TemplatedWidget = declare([ dijit._Widget, dijit._Templated ], {
        
        _blankGif: dijit._Widget.prototype._blankGif,
        
        _place: function(node, refNode, pos) {
        	dojo.place(node, refNode, pos);
        },

        _stopEvent: function(event) {
            dojo.stopEvent(event);
        },
        
        _connect: function(object, event, method) {
            return this.connect(object, event, method);
        },
        
        _mixin: function(dest,sources) {
            return dojo.mixin(dest,sources);
        },
        
        _destroy: function(node) {
            dojo.destroy(node);
        },
        
        _create: function(name, attribs, parent) {
            return dojo.create(name, attribs, parent);
        },
        
        _toDom: function(frag, doc) {
            return dojo._toDom(frag, doc);
        },
        
        _isString: function(obj) {
            return dojo.isString(obj);
        },
        
        _substitute: function(template, map, transform, thisObject) {
        	return dojo.string.substitute(template, map, transform, thisObject);
        },
        
        _getObject: function(name, create, context) {
            return dojo.getObject(name, create, context);
        },
        
        _hitch: function(scope, method) {
            if (arguments.length > 2) {
                return dojo._hitchArgs.apply(dojo, arguments);
            } else {
                return dojo.hitch(scope, method);
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
                var attachEvent = (nodes[i].getAttribute) ? nodes[i].getAttribute("data-dojo-attach-event") : null;
                if (attachEvent) {
                    nodes[i].removeAttribute("data-dojo-attach-event");
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