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

define(["../../../declare",
        "../../../lang",
        "../../../text!./templates/SearchBoxTemplate.html",
        "../../../text!./templates/PopUpTemplate.html",
        "../ConnectionsGridRenderer",
        "../../../i18n!./nls/SearchBoxRenderer"], 
        function(declare,lang, template, PopUpTemplate, ConnectionsGridRenderer,nls){
	/**
	 * @class SearchBoxRenderer
	 * @namespace sbt.connections.controls.search
	 * @module sbt.connections.controls.search.SearchBoxRenderer
	 */
	var SearchBoxRenderer = declare(ConnectionsGridRenderer,{
		
		/**
		 * SearchBoxRenderer class constructor function
		 * @method constructor
		 * @param args
		 */
		constructor: function(args){	
			lang.mixin(this.nls,nls);
		},
		
		/**
		 * Converts the HTML pop up template into a DOM node.
		 * Creates a Div element and uses the template as the div's inner HTML
		 * @method getDomeNode
		 * @returns The Search Box Dom Node 
		 */
		getDomNode: function(){
			var domStr = this._substituteItems(template, this);
			template = domStr;
			
			var div = null;
			if(typeof template =="string"){
				var wrapper= document.createElement('div');
				wrapper.innerHTML= template;
				div= wrapper.firstChild;
			}
			return div;	
		},
		
		/**
		 * Converts the HTML pop up template into a DOM node.
		 * Creates a div and sets it's inner html to be the pop up template
		 * @method getPopUpNode
		 * @returns the applications list pop up DOM Node
		 */
		getPopUpNode: function(){
			
			var domstr = this._substituteItems(PopUpTemplate, this);
			PopUpTemplate = domstr;
			
			var div = null;
			if(typeof PopUpTemplate =="string"){
				var wrapper= document.createElement('div');
				wrapper.innerHTML= PopUpTemplate;
				wrapper.tabIndex = 0;
				div= wrapper;
			}
			return div;	
		},
		
		/**
		 * Attaches events to the template 
		 * @method render
		 * @param searchBox
		 * @param el
		 * @param data
		 */
		render: function(searchBox, el, data) {
			searchBox.templateString = template;
			this._doAttachEvents(searchBox,el,data);			
		},
		
		/**
		 * renders the applications lists pop up
		 * @method renderPopUp
		 * @param searchBox The SearchBox class
		 * @param el The searchBox Element
		 */
		renderPopUp: function(searchBox,el){
			var popUp = this.getPopUpNode();
			this._doAttachEvents(searchBox,popUp,{});
			el.appendChild(popUp);
			popUp.firstChild.focus();
			
		},
		
		/**
		 * Removes the applications pop up
		 * @param searchBoxElement The searchBox HTML Element
		 */
		removePopUp: function(searchBoxElement){
			searchBoxElement.removeChild(searchBoxElement.children[1]);
		},
		
		/**
		 * Changes the Text displayed in the "currently selected" application text field 
		 * @param selectedApplication The name of the application to display
		 */
		changeSelectedApplication: function(selectedApplication){
			var elements = document.getElementsByTagName("span");
			for(var i=0;i<elements.length;i++){
				if(elements[i].textContent == nls.selectedApplication){
					elements[i].textContent = selectedApplication;
					nls.selectedApplication = selectedApplication;
				}
			}
		},
		
		/**
		 * Override _substitureItems as there are only NLS strings to be substituted 
		 * no XPath values, functions etc. 
		 * @param template
		 * @param renderer
		 * @returns
		 */
		_substituteItems: function(template,renderer){
			var text = template;
			if(text.indexOf("${nls.") != -1){
				var nls = renderer.nls;

				var startIndex = text.indexOf("${nls.");
				var endIndex = text.indexOf("}",startIndex);
	
				var nlsIndex = text.substring(startIndex+6,endIndex);
				var stringToReplace = text.substring(startIndex,endIndex+1);
	
				var replacingString = nls[nlsIndex];
	
				text = text.replace(stringToReplace,replacingString);
				
				//if there are more strings to substitute keep substituting 
				if(text.indexOf("${nls.") != -1){
					return this._substituteItems(text,renderer);
				}
			}
			//if no more strings to substitute return the final string	
			return text;
		}
	});
	
	return SearchBoxRenderer;
});