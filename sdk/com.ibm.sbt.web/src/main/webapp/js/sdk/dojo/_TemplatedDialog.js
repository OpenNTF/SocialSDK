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
define([ "../_bridge/declare", "dijit/Dialog", "dijit/_Templated"], 
    function(declare, Dialog, _Templated) {

    /**
     * @module sbt.widget._TemplatedDialog
     */
    var _TemplatedDialog = declare([ Dialog, _Templated ], {        
    	
    	nls : null,
    	
    	/**
		 * _TemplatedDialog class constructor function
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor: function(args) {	
			this.nls = args.nls || {};
		}
    });
    
    return _TemplatedDialog;
});