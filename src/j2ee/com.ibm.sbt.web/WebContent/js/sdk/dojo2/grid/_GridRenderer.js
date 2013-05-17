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
define([ "../../_bridge/declare", "dojo/_base/lang", "dojo/dom-construct", "dijit/_WidgetBase" ,'dojo/dom-class'], 
         function(declare, lang, domConstruct, _WidgetBase, domClass) {

    /*
     * @module sbt.widget.grid._GridRenderer
     */
    var _GridRenderer = declare(null, {
        
        _blankGif: dijit._WidgetBase.prototype._blankGif,
        
        /*Utility to mix to objects or classes together*/
        _mixin: function(dest,sources) {
            return lang.mixin(dest,sources);
        },
        
        /*destroy a DOM node
         * @node - the node to be destroyed*/
        _destroy: function(node) {
            domConstruct.destroy(node);
        },
        
        /*Create a DOM node
         * @name - the name of the element, for example DIV
         * @attribs - the attributes of the element, for example id
         * @parent - the element to which the newly created node will be attached to*/
        _create: function(name, attribs, parent) {
            return domConstruct.create(name, attribs, parent);
        },
        
        /*Similar to create, takes an HTML String an converts it into a DOM node
         * This function takes a HTML template which is converted to a DOM node
         * @template - the HTML template to be used
         * @parent - the parent node that the newly created node will attach to
         * */
        _toDom: function(template, parent) {
            return domConstruct.toDom(template, parent);
        },
        
        /*Check if an objects type is String*/
        _isString: function(obj) {
            return lang.isString(obj);
        },
        
        _getObject: function(name, create, context) {
            return lang.getObject(name, create, context);
        },
        
        /*Removes White space from the beginning and end of a string
         * @str - the string to remove white space from */
        _trim: function(str) {
            return lang.trim(str);
        },
       
        /*A function that allows a function to execute in a different scope
         * @scope - the scope you wish to execute in
         * @method - the function to execute*/
        _hitch: function(scope, method) {
            if (arguments.length > 2) {
                return lang._hitchArgs.apply(dojo, arguments);
            } else {
                return lang.hitch(scope, method);
            }
        },
        _addClass: function(node,className){
			return domClass.add(node,className);
		},
		_removeClass: function(node, className){
			return domClass.remove(node,className);
		}
               
    });
    
    return _GridRenderer;
});