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
define([ "../../declare", "../../lang", "../../stringUtil", "../../log", "../../dom",
         "../../widget/_TemplatedWidget" ],
		function(declare, lang, stringUtil, log, dom, _TemplatedWidget) {

	/*
	 * @module sbt.controls.view._ActionContainer
	 */
	var _ActionContainer = declare([ _TemplatedWidget ], {

		/**
		 * 
		 */
		selection : null,
		
		/**
		 * 
		 */
		_elementsToItems : {},
		
		/**
		 * 
		 */
		_selectedElement : null,

		/**
		 * 
		 */
		context : null,

		/**
		 * 
		 */
		templateString : null,

		/**
		 * 
		 */
		actionTemplate : null,
		
		/*
		 * 
		 */
		_actionElements : null,

		/**
		 * Post create function is called after action container has been created.
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
			this._actionElements = this._actionElements || [];
			this.selection = this.selection || [];
			this.context = this.context || {};
		},

		/**
		 * 
		 */
		destroy : function() {
			this._deleteActionElements();
			this.inherited(arguments);
		},

		/**
		 * Construct the UI for this widget from a template, setting
		 * this.domNode.
		 */
		buildRendering : function() {
			this.inherited(arguments);

			// create node for each action
			for (var i=0; i<this._actionElements.length; i++) {
				this.domNode.appendChild(this.renderAction(this.domNode, this._actionElements[i].action));
			}

			this._connect(this.domNode, "onclick", this._hitch(this, this._onClick));
		},

		//
		// Internals
		//
		
		_place : function() {
	        this.inherited(arguments);      
		},
		
		_renderElement : function(parentNode, action, addToBeginning) {
			var node;
			if (lang.isString(this.actionTemplate)) {
				var domStr = stringUtil.transform(this.actionTemplate, action);
				node = dom.toDom(domStr, parentNode.ownerDocument);
			} else {
				node = this.actionTemplate.cloneNode(true);
			}
			parentNode.appendChild(node);
			node = parentNode.lastElementChild;
			
			this._doAttachPoints(node, action);
			this._doAttachEvents(node, action);
			
			this._registerActionElement(node, action, addToBeginning);

			return node;
		},
		
		_updateActionState : function(actionElement) {
			var element = actionElement.element;
			if (!element || !element.nodeType) {
				return actionElement.state;
			}

			var oldState = actionElement.state || {};
			var newState = dojo.clone(oldState); // TODO fix this
			var action = actionElement.action;
			action.selectionChanged(newState, this.selection, this.context);
			actionElement.state = newState;
			
			if (newState.name != oldState.name) {
				this._setActionName(actionElement, newState.name);
			}
			   
			if (newState.tooltip != oldState.tooltip) {
				this._setActionTooltip(actionElement, newState.name);
			}
			
			if (newState.enabled != oldState.enabled) {
				this._setActionEnabled(actionElement, newState.enabled);
			}
			   
			if (newState.visible != oldState.visible) {
				this._setActionVisible(actionElement, newState.visible);
			}
			   
			return newState;
		},
		
		_setActionName : function(actionElement, name) {
			var nameNode = actionElement.action.actionNameNode;
			if (nameNode) {
				dom.setText(nameNode, name || "");
			}
		},

		_setActionTooltip : function(actionElement, tooltip) {
			if (tooltip) {
				actionElement.element.title = tooltip;
			}
		},

		_setActionEnabled : function(actionElement, enabled) {
			if (enabled == false) {
				dom.addClass(actionElement.element, this.disabledClass);
				actionElement.element.firstElementChild.setAttribute("disabled",true);
			} else {
				dom.removeClass(actionElement.element, this.disabledClass);
				actionElement.element.firstElementChild.removeAttribute("disabled");
			}
		},

		_setActionVisible : function(actionElement, visible) {
			if (visible == false) {
				actionElement.element.style.display = "none";
			} else {
				actionElement.element.style.display = "";
			}
		},

		_registerActionElement : function(element, action, addToBeginning) {
			var actionElement = { action:action, element:element };
			this._updateActionState(actionElement);
			if (addToBeginning) {
				this._actionElements.unshift(actionElement);
			} else {
				this._actionElements.push(actionElement);
			}
		},
		
		_getAction : function(element) {
			for (var i=0; i<this._actionElements.length; i++) {
				if (this._actionElements[i].element == element) {
					return this._actionElements[i].action;
				}
			}
		},

		_onClick : function(event) {
			var target = event.target;
			while (target && target != this.domNode) {
				if (this._isActionElement(target)) {
					this._stopEvent(event);
					this._executeActionElement(target);
					break;
				} else {
					target = target.parentNode;
				}
			}
		},

		_isActionElement : function(element) {
			var action = this._getAction(element);
			return action;
		},

		_executeActionElement : function(element) {
			var action = this._getAction(element);
			var state = element.state;
			
			// only proceed if action is visible and enabled
			if (state && (!state.enabled || !state.visible)) {
				return;
			}
			
			this._executeAction(action, element);
		},

		_executeAction : function(action, element) {
		}

	});

	return _ActionContainer;
});