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

dojo.provide("sbt.controls.view.TabBar");

/**
 * 
 */
define([ "../../declare", "../../lang", "../../stringUtil", "../../log",
         "./_ActionContainer","../../text!../../controls/view/templates/TabBar.html",
        "../../text!../../controls/view/templates/Tab.html"],
		function(declare, lang, stringUtil, log, _ActionContainer, TabBarTemplate, TabItemTemplate) {

	/*
	 * @module sbt.controls.view.TabBar
	 */
	var TabBar = declare([ _ActionContainer ], {
		
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
		templateString : TabBarTemplate,

		/**
		 * 
		 */
		actionTemplate : TabItemTemplate,
		
		/**
		 * 
		 */
		selectedClass : "lotusSelected",
		/*
		 * 
		 */
		_actionElements : null,

		/**
		 * Constructor method for the TabBar.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			lang.mixin(this, args);
			
			if (args) {
				this.templateString = args.tabBarTemplate;
				this.actionTemplate = args.tabItemTemplate;
				this.selectedClass = args.selectedClass;
			}
		},
		
		/**
		 * @method addItem
		 * @param item
		 * @param addToBeginning
		 */
		addItem : function(item, addToBeginning) {
			
			var element = this._renderElement(this.domNode, item, addToBeginning);
			element.id = item.id;
			this._elementsToItems[element.id] = item;
			
			this._updateActionState(element);			

			if (addToBeginning) {
				this._place(element, this.domNode, "first");
			} else {
				this._place(element, this.domNode);
			}
		},
		
		/**
		 * Removes all tabs.
		 * 
		 * @method removeAll
		 */
		removeAll : function() {
			this._elementsToItems = {};
			
			var el = this.domNode;
			while (el.hasChildNodes()) {
				el.removeChild(el.lastChild);
			}
		},

		//
		// Internals
		//

		_updateActionState : function(element) {
			var item = this._elementsToItems[element.id];
			
			if (item == null) {
				return;
			}
	
			if (item.isSelected()) {
				if (this._selectedElement != null) {
					this._selectedElement.setAttribute("class", "");
					var selectedItem = this._elementsToItems[this._selectedElement.id];
					selectedItem.setSelected(false);
					this._elementsToItems[this._selectedElement.id] = selectedItem;
				}
				
				element.setAttribute("class", this.selectedClass);
				this._selectedElement = element;
			} else {
				element.setAttribute("class", "");
			}
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
			try {
				var item = this._elementsToItems[element.id];

				if (item.isSelected()) {
					item.setSelected(false);
				} else {
					item.setSelected(true);
				}
	
				this._elementsToItems[element.id] = item;
				this._updateActionState(element);
				action.execute(this.selection, this.context, element);
			} catch(err) {
				log.error(err);
			}
		}

	});

	return TabBar;
});