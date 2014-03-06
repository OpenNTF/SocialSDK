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
define([ "../declare", "../lang", "../stringUtil", "../widget/_TypeAhead", "./StoreMixin" ],
	function(declare, lang, stringUtil, _TypeAhead, StoreMixin) {

	/*
	 * @module sbt.controls.TypeAhead
	 */
	var TypeAhead = declare([ _TypeAhead, StoreMixin ], {
		
		className: "",
		
		templateString: "<input class=\"${className}\" type=\"text\" size=\"${size}\" dojoAttachPoint=\"domNode,textbox,focusNode,comboNode\"/>",
		
		size: "",
		
		autoComplete: true,
        
		hasDownArrow: false,
		
		searchDelay: 300,
        
        /**
         * Constructor method for the TypeAhead.
         * Creates a default store, if none have been already created
         * @method constructor
         * @param args
         */
        constructor : function(args) {
        }
        
        //
        // Internals
        //
        		
	});

	return TypeAhead;
});