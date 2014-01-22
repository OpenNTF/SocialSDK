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
define([ "../declare", "../lang", "../Promise", "../data/AtomReadStore", "dijit/form/ComboBox" ],
		function(declare, lang, Promise, AtomReadStore, ComboBox) {

	/*
	 * @module sbt.widget._TypeAhead
	 */
	var _TypeAhead = declare([ ComboBox ], {
		
		/**
		 * Constructor method for the TypeAhead.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
		},
		
        /*
         * Creates an instance of an atom read store.
         */
		_createDefaultStore: function(args) {
            return new AtomReadStore(args);
        },        
        
        _hitch: function(scope, method) {
            if (arguments.length > 2) {
                return dojo._hitchArgs.apply(dojo, arguments);
            } else {
                return dojo.hitch(scope, method);
            }
        }
        
	});

	return _TypeAhead;
});