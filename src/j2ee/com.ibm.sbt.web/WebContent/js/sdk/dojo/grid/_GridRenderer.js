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
define([ "../../_bridge/declare" ], 
         function(declare) {

    /**
     * @module sbt.widget.grid._GridRenderer
     */
    var _GridRenderer = declare(null, {
        
        _blankGif: dijit._Widget.prototype._blankGif,
        
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
        
        _getObject: function(name, create, context) {
            return dojo.getObject(name, create, context);
        },
        
        _trim: function(str) {
            return dojo.trim(str);
        },
        
        _hitch: function(scope, method) {
            if (arguments.length > 2) {
                return dojo._hitchArgs.apply(dojo, arguments);
            } else {
                return dojo.hitch(scope, method);
            }
        },
        _addClass: function(node,className){
			return dojo.addClass(node,className);
		},
		_removeClass: function(node, className){
			return dojo.removeClass(node,className);
		}
       
    });
    
    return _GridRenderer;
});