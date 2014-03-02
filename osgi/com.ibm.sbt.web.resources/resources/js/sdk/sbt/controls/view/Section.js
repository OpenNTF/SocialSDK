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

dojo.provide("sbt.controls.view.Section");

/**
 * 
 */
define([ "../../declare", "../../lang", "../../dom", "../../stringUtil", "../../widget/_TemplatedWidget",
		"../../text!../../controls/view/templates/Section.html" ],
		function(declare, lang, dom, stringUtil, _TemplatedWidget, SectionTmpl) {

	/*
	 * @module sbt.controls.view.Section
	 */
	var Section = declare([ _TemplatedWidget ], {
		
		nls : {
			closeSection : "Close Section",
			openSection : "Open Section"
		},

		/**
		 * 
		 */
		templateString : SectionTmpl,
		
		/**
		 * 
		 */
		title : null,
		
		/**
		 * 
		 */
		contentTemplate : null,
		
		/**
		 * Constructor method for the Section.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
		},
		
		/**
		 * Post create function is called after section has been created.
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
			this.inherited(arguments);
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
		
		/**
		 * Set the title for the section.
		 * 
		 * @method setTitle
		 */
		setTitle : function(title) {
			this.title = title;
			
			if (this.titleNode) {
				dom.removeAll(this.titleNode);
				this.titleNode.appendChild(dom.createTextNode(title)); 		
			}
		},
		
		/**
		 * Set the content.
		 * 
		 * @method setContent
		 */
		setContent : function(content) {
			this.removeContent();
			
			if (content) {
				var node;
                if (lang.isString(content)) {
                    node = dom.toDom(content, this.sectionNode.ownerDocument);
                } else {
                    node = content;
                }
                this.sectionNode.appendChild(node);
			}
		},

		/**
		 * Remove the content.
		 * 
		 * @method removeContent
		 */
		removeContent : function() {
			while (this.sectionNode.childNodes[0]) {
	           dom.destroy(this.sectionNode.childNodes[0]);
	        }
		},

		/**
		 * Open the content area.
		 * 
		 * @method openContent
		 */
		openContent : function() {
			this.openNode.style.display = "";
			this.closedNode.style.display = "none";
			this.sectionNode.style.display = "";
		},

		/**
		 * Close the content area.
		 * 
		 * @method closeContent
		 */
		closeContent : function() {
			this.openNode.style.display = "none";
			this.closedNode.style.display = "";
			this.sectionNode.style.display = "none";
		},

		/**
		 * Toggle the content area.
		 * 
		 * @method toggleContent
		 */
		toggleContent : function() {
			if (this.sectionNode.style.display == "") {
				closeContent();
			} else {
				openContent();
			}
		},
		
		/**
		 * Render the content using the contentTemplate (if available)
		 * 
		 * @param renderContent
		 */
		renderContent : function(context) {
			if (this.contentTemplate) {
				var domStr = stringUtil.transform(this.contentTemplate, context, null, context);
				var content = dom.toDom(domStr, this.sectionNode.ownerDocument);
				this.setContent(content);
			}
		},

		/**
		 * Display an error.
		 * 
		 * @method showError 
		 */
		showError : function(error) {
			
		}
		
		//
		// Internals
		//


	});

	return Section;
});