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
         "dojo/_base/lang", "dojo/_base/connect", "dojo/touch", "dijit/_WidgetBase", "dijit/_TemplatedMixin"], 
        function(declare, lang, connect, touch, _WidgetBase, _TemplatedMixin) {

    /**
     * @module sbt.widget._TemplatedWidget
     */
    var _TemplatedWidget = declare([ dijit._WidgetBase, dijit._TemplatedMixin ], {
        
        _blankGif: dijit._WidgetBase.prototype._blankGif,

        _stopEvent: function(event) {
            dojo.stopEvent(event);
        },
        
        _connect: function(object, event, method) {
            // TODO handle disconnecting when widget is destroyed
            return connect.connect(object, touch[event] || event, method);
        },
        
        _mixin: function(dest,sources) {
            return lang.mixin(dest,sources);
        }
    
    });
    
    return _TemplatedWidget;
});