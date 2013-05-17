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

        _stopEvent: function(event) {
            dojo.stopEvent(event);
        },
        
        _connect: function(object, event, method) {
            return this.connect(object, event, method);
        },
        
        _mixin: function(dest,sources) {
            return dojo.mixin(dest,sources);
        }
        
    });
    
    return _TemplatedWidget;
});