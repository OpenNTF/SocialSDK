/*
 * � Copyright IBM Corp. 2013
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
define([ "../../declare", "../../config", "../../lang", "../../stringUtil", "../../log", "../../dom",
		"../../widget/_TemplatedWidget", 
		"./nls/BaseView", "./ActionBar", "./NavTree",
		"../../text!../../controls/view/templates/BaseView.html" ],
		function(declare, config, lang, stringUtil, log, dom, _TemplatedWidget, nls, ActionBar, NavTree, BaseViewTmpl) {

	/*
	 * @module sbt.controls.view.BaseView
	 */
	var BaseView = declare([ _TemplatedWidget ], {

		/**
		 * 
		 */
		href : "#",
		
		/**
		 * 
		 */
		hideActionBar : false,

		/**
		 * 
		 */
		hideNavTree : false,
		
		/**
		 * 
		 */
		forceAuthentication : true, 
		
		/**
		 * Default endpoint to use
		 */
		defaultEndpoint : "connections",

		/**
		 * 
		 */
		templateString : BaseViewTmpl,
		
		messageSuccessClass : "lotusMessage2 lotusSuccess",
		iconSuccessClass : "lotusIcon lotusIconMsgSuccess",

		messageErrorClass : "lotusMessage2 lotusError",
		iconErrorClass : "lotusIcon lotusIconMsgError",


		/**
		 * Constructor method for the BaseView.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			if (args) {
				lang.mixin(this, args);
			}
			this.nls = lang.mixin({}, nls, this.nls);
		},

		/**
		 * 
		 */
		postMixInProperties : function() {
			this.endpoint = this.getEndpoint();
			this.title = this.title || "";
			this.actionBar = this.actionBar || new ActionBar();
		    this.navTree = this.navTree || new NavTree();
		},

		/**
		 * Post create function is called after widget has been created.
		 * 
		 * @method - postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
			
			if (this.forceAuthentication) {
				this.authenticate();
			}
		},

		/**
		 * Construct the UI for this widget from a template, setting
		 * this.domNode.
		 */
		buildRendering : function() {
			this.inherited(arguments);

			if (!this.hideActionBar && this.contentNode) {
				this.contentNode.appendChild(this.actionBar.domNode);
			}

			if (!this.hideNavTree && this.navMenu) {
				this.navMenu.appendChild(this.navTree.domNode);
			}
		},
		
		/**
		 * Return the endpoint to be used by this view.
		 * 
		 * @method getEndpoint
		 */
		getEndpoint : function() {
			if (!this.endpoint) {
				this.endpoint = config.findEndpoint(this.defaultEndpoint);
			}
			if (lang.isString(this.endpoint)) {
				this.endpoint = config.findEndpoint(this.endpoint);
			}
			return this.endpoint;
		},
		
		/**
		 * Authenticate the view.
		 * 
		 * @method authenticate
		 */
		authenticate : function(force) {
			var endpoint = this.getEndpoint();
			if (force) {
				endpoint.authenticate();
			} else {
				// if not forcing authentication
			}
		},

		/**
		 * @method addAction
		 * @param action
		 * @param addToBeginning
		 */
		addAction : function(action, addToBeginning) {
			action.view = this;
			this.actionBar.addAction(action, addToBeginning);
		},
		
		/**
		 * @method addNode
		 * @param treeNode
		 * @param addToBeginning
		 */
		addTreeNode : function(treeNode, addToBeginning) {
			treeNode.view = this;
			this.navTree.addNode(treeNode, addToBeginning);
		},
		
		/**
		 * @method clearContent
		 * @param fill True if the entire page is to be cleared; false if only the center 
		 * 					pane is to be cleared.
		 */
		clearContent : function(fill) {
			var el = null;
			
			if (fill) {
				el = this.mainNode;
			} else {
				el = this.contentNode;
			}
			while (el.hasChildNodes()) {
				el.removeChild(el.lastChild);
			}
		},
		
		/**
		 * @method setContent
		 * @param content
		 */
		setContent : function(content) {
			// remove old content
			var el = this.contentNode;
			if (this.content) {
				if (this.content.removeSelectionListener) {
					this.content.removeSelectionListener(this);
				}
				
				el.removeChild(content.domNode);
			}
			
			this.content = content;
			el.appendChild(content.domNode);
			
			if (this.content.addSelectionListener) {
				this.content.addSelectionListener(this);
			}
			
			if (this.content.update) {
				this.content.update();
			}
		},

		/**
		 * @method addToLeftCol
		 * @param node
		 * @param addToBeginning
		 */
		addToLeftCol : function(node, addToBeginning) {
		},

		/**
		 * @method addToRightCol
		 * @param node
		 * @param addToBeginning
		 */
		addToRightCol : function(node, addToBeginning) {
		},
		
		/**
		 * @method displayMessage
		 */
		displayMessage : function(template, isError) {
			dom.setAttr(this.messageNode, "class", isError ? this.messageErrorClass : this.messageSuccessClass);
			dom.setAttr(this.messageIcon, "class", isError ? this.iconErrorClass : this.iconSuccessClass);
			dom.setAttr(this.messageIcon, "alt", isError ? this.nls.errorAlt : this.nls.successAlt);
			dom.setText(this.messageIconAlt, isError ? this.nls.errorAlt : this.nls.successAlt);

			while (this.messageBody.hasChildNodes()) {
				this.messageBody.removeChild(this.messageBody.lastChild);
			}
			var node = template;
			if (lang.isString(node)) {
				node = dom.toDom(template);
			}
			this.messageBody.appendChild(node);
			
			this.messageNode.style.display = "";
			
			// refresh the content area to display any update
			if (this.content && this.content.refresh) {
				this.content.refresh();
			}
		},
		
		/**
		 * @method hideMessage
		 */
		hideMessage : function() {
			this.messageNode.style.display = "none";
		},
		
		/**
		 * @method selectionChanged
		 */
		selectionChanged : function(selection, context) {
			if (this.actionBar) {
				this.actionBar.selectionChanged(selection, context || this.content);
			}
		},
		
		//
		// Internals
		//

		/*
		 * Converts a HTML String to a DOM Node, without wrapping a div around it
		 * @method _convertToDomNode
		 * @param template the html string to be converted to a DOM node
		 * @returns A DOM Node 
		 */
		_convertToDomNodeNoWrapper : function(template) {
			var div = null;
			if(typeof template =="string"){
				var wrapper= document.createElement('div');
				wrapper.innerHTML= template;
				wrapper.tabIndex = 0;
				div= wrapper;
			}
			return div.firstChild;	
		},
		
		/**
        Called after the widget has been attached to the dom. Sizing calculations etc should be done here.
        
        @method startup
        **/
		startup : function(){
		    this.content.startup();
		}
		
	});

	return BaseView;
});