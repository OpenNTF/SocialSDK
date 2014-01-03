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
define([ "../../declare", "../../lang", "../../stringUtil", "../../log",
		"../../widget/_TemplatedWidget", "../../connections/controls/search/SearchBox",
		"../../text!../../controls/view/templates/TabbedBaseView.html",
		"../../controls/view/TabBar",
		"../../controls/view/NavTree"
		],
		function(declare, lang, stringUtil, log, _TemplatedWidget, SearchBox, BaseViewTmpl, TabBar, NavTree) {

	/*
	 * @module sbt.controls.view.TabbedBaseView
	 */
	var TabbedBaseView = declare([ _TemplatedWidget ], {

		/**
		 * 
		 */
		href : "#",
		
		/**
		 * 
		 */
		hideTabBar : false,
		
		/**
		 * 
		 */
		hideNavTree : false,

		/**
		 * 
		 */
		templateString : BaseViewTmpl,

		/**
		 * Constructor method for the BaseView.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
		},

		/**
		 * 
		 */
		postMixInProperties : function() {
			this.title = this.title || "";
		    this.searchBox = this.searchBox || new SearchBox(this.searchArgs || {});
		    this.tabBar = this.tabBar || new TabBar();
		    this.navTree = this.navTree || new NavTree();
		},

		
		/**
		 * Post create function is called after tabbed base view has been created.
		 * 
		 * @method - postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
		},

		/**
		 * Construct the UI for this widget from a template, setting
		 * this.domNode.
		 */
		buildRendering : function() {
			this.inherited(arguments);

			if (!this.hideSearchBox) {
				this.searchNode.appendChild(this.searchBox.domNode);
			}
			
			if (!this.hideTabBar) {
//				this.contentNode.appendChild(this.tabBar.domNode);
				this.headerNode.appendChild(this.tabBar.domNode);
			}
			
			if (!this.hideNavTree) {
//				this.addToLeftCol(this.navTree.domNode);
				this.navMenu.appendChild(this.navTree.domNode);
			}
			
		},
		
		/**
		 * @method addTabItem
		 * @param item
		 * @param addToBeginning
		 */
		addTabItem : function(item, addToBeginning) {
			this.tabBar.addItem(item, addToBeginning);
		},
		
		/**
		 * @method addNavTreeNode
		 * @param node
		 * @param addToBeginning
		 */
		addNavTreeNode : function(node, addToBeginning) {
			this.navTree.addNode(node, addToBeginning);
		},
		
		/**
		 * @method clearContent
		 */
		clearContent : function() {
			var el = this.contentNode;
			while (el.hasChildNodes()) {
				el.removeChild(el.lastChild);
			}
		},
		
		/**
		 * @method removeAllTabs
		 */
		removeAllTabs : function() {
			this.tabBar.removeAll();
		},
		
		/**
		 * @method setContent
		 * @param content
		 */
		setContent : function(content) {
			// remove old content
			var el = this.contentNode;
			
			this.clearContent();

			el.appendChild(content.domNode);
	
			if (content.update) {
				content.update();
			}
		},

		/**
		 * @method addToLeftCol
		 * @param node
		 * @param addToBeginning
		 */
		addToLeftCol : function(node, addToBeginning) {
			this.leftColNode.appendChild(node);
		},

		/**
		 * @method addToRightCol
		 * @param node
		 * @param addToBeginning
		 */
		addToRightCol : function(node, addToBeginning) {
		},
		
		//
		// Internals
		//

	});

	return TabbedBaseView;
});