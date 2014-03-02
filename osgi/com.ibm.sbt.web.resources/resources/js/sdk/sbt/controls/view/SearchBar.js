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

dojo.provide("sbt.controls.view.SearchBar");

/**
 * 
 */
define([ "../../declare", "../../lang", "../../stringUtil", "../../log",
		"../../widget/_TemplatedWidget",
		"../../text!../../controls/view/templates/SearchBar.html" ],
		function(declare, lang, stringUtil, log, _TemplatedWidget, SearchBarTmpl) {

	/*
	 * @module sbt.controls.view.SearchBar
	 */
	var SearchBar = declare([ _TemplatedWidget ], {
		
		nls : { refineSearch : "Refine Search Results", search : "Search" },
		
		/**
		 * 
		 */
		scopeTitle : null,

		/**
		 * 
		 */
		templateString : SearchBarTmpl,

		/**
		 * Constructor method for the SearchBar.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
		},

		/**
		 * Post create function is called after search bar has been created.
		 * 
		 * @method - postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
		},

		/**
		 * 
		 */
		postMixInProperties : function() {
		},

		/**
		 * 
		 */
		destroy : function() {
			this.inherited(arguments);
		},

		/**
		 * Construct the UI for this widget from a template, setting
		 * this.domNode.
		 */
		buildRendering : function() {
			this.inherited(arguments);
		},

	    openMenu: function(event) {
	    },
	    
	    openMenuA11y: function(event) {
	    },
	    
	    closeMenu: function() {
	    },
	    
	    search: function() {
	    },
		
		/**
		 * 
		 * @param event
		 */
		submitForm: function(event) {
		},
		
		/**
		 * 
		 * @param event
		 */
		formSubmitted : function(event) {
	        this.submitForm();
	        this._stopEvent(evt);
	    },
	    
		/**
		 * 
		 */
	    onSubmit : function() {
	    }

	});

	return SearchBar;
});