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

define(["../../../declare",
        "../../../lang",
        "../../../text!./templates/SearchBoxTemplate.html",
        "../../../text!./templates/SearchSuggestTemplate.html",
        "../../../text!./templates/PopUpTemplate.html",
        "../../../text!./templates/SuggestPopUpTemplate.html",
        "../ConnectionsGridRenderer",
        "../../../i18n!./nls/SearchBoxRenderer",
        "../../../text!./templates/MemberListItemTemplate.html",
        "../../../text!./templates/MemberListTemplate.html"], 
        function(declare,lang, template, SuggestTemplate, PopUpTemplate, SuggestionPopUp, ConnectionsGridRenderer,nls,
        		MemberListItemTemplate, MemberListTemplate){
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
		getDomNode: function(SearchBox){
			
			var htmlTemplate = "";
			
			if(SearchBox.searchSuggest == "on"){
				var domStr = this._substituteItems(SuggestTemplate, this);
				SuggestTemplate = domStr;
				htmlTemplate = SuggestTemplate;
			}else{
				var domStr = this._substituteItems(template, this);
				template = domStr;
				htmlTemplate = template;
			}

			var div = this._convertToDomNode(htmlTemplate);
			return div.firstChild;	
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
			
			var div = this._convertToDomNode(PopUpTemplate);
			return div;	
		},
		
		/**
		 * Converts the HTML member list item template into a DOM node.
		 * Creates a div and sets it's inner html to be the member list item template
		 * @method getMemberListItemNode
		 * @param memberName The name to display
		 * @returns the applications list pop up DOM Node
		 */
		getMemberListItemNode: function(memberName){
			var domstr = this._substituteItems(MemberListItemTemplate, this);
			domstr = this._substituteMemberName(domstr, memberName);
			
			var obj = this._convertToDomNodeNoWrapper(domstr);
			
			return obj;	
		},
		
		/**
		 * Converts the HTML member list template into a DOM node.
		 * Creates a div and sets it's inner html to be the member list template
		 * @method getMemberListItemNode
		 * @returns the applications list pop up DOM Node
		 */
		getMemberListNode: function(){
			
			var domstr = this._substituteItems(MemberListTemplate, this);
			MemberListTemplate = domstr;
			
			var div = this._convertToDomNode(MemberListTemplate);
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
		_appsPopUp: null,
		renderPopUp: function(searchBox,el){
			
			if(!this._appsPopUp){
				this._appsPopUp = this.getPopUpNode();
				this._doAttachEvents(searchBox,this._appsPopUp,{});	
			}
			
			el.appendChild(this._appsPopUp);
			this._appsPopUp.firstChild.focus();
			return this._appsPopUp;
		},
		
		/**
		 * renders a member list item
		 * @method renderMemberListItem
		 * @param memberName The member name to display
		 * @param memberId The member's profile id - this acts as a unique identifier (needed when, for example,
		 * 					  creating a new community)
		 */
		_appsMemberListItem: null,
		renderMemberListItem: function(searchBox, memberName, memberId){
			// Get node
			this._appsMemberListItem = this.getMemberListItemNode(memberName);
			
			// Create member object for storage
			var newMember = new Object();
			newMember.html = this._appsMemberListItem.innerHTML;
			newMember.id = memberId;
			// Make sure that the member hasn't already been selected
			for (var i = 0; i < searchBox._members.length; i++) {
				var member = searchBox._members[i];
				if (member.html == newMember.html) {
					return;
				}
			}
			
			// Add it to the list
			this._appsMemberList.appendChild(this._appsMemberListItem);
			
			// Attach event listeners
			this._doAttachEvents(searchBox,this._appsMemberListItem,{});	
			
			// Keep track of the added member
			searchBox._members.push(newMember);
			
			return this._appsMemberListItem;
		},
		/**
		 * renders the member list
		 * @method renderMemberList
		 */
		_appsMemberList: null,
		renderMemberList: function(el){
			
			if(!this._appsMemberList){
				// Generate the DOM object representing the member list
				this._appsMemberList = this.getMemberListNode();
			}
			
			// Append the list to the parent
			el.appendChild(this._appsMemberList);
		
			// Request focus
			this._appsMemberList.firstChild.focus();
			
			return this._appsMemberList;
		},
		
		
		_suggestionPopUp: null,
		renderSuggestionPopUp: function(searchBox,el){
			
			if(!this._suggestionPopUp){
				this._suggestionPopUp= this._convertToDomNode(SuggestionPopUp);
				this._suggestionPopUp = this._suggestionPopUp.firstElementChild;
			}
			
			el.appendChild(this._suggestionPopUp);
			return el.lastChild;
		},
		
	
		
		/**
		 * Removes the applications pop up
		 * @param searchBoxElement The searchBox HTML Element
		 */
		removePopUp: function(searchBoxElement,popUp){
			searchBoxElement.removeChild(popUp);
		},
		
		removeSuggestionPopUp: function(searchBoxElement,popUpElement){
			searchBoxElement.removeChild(popUpElement);
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
		 * Converts a HTML String to a DOM Node, wrapping a div around it
		 * @method _convertToDomNode
		 * @param template the html string to be converted to a DOM node
		 * @returns A DOM Node 
		 */
		_convertToDomNode: function(template){
			var div = null;
			if(typeof template =="string"){
				var wrapper= document.createElement('div');
				wrapper.innerHTML= template;
				wrapper.tabIndex = 0;
				div= wrapper;
			}
			return div;	
		},
		
		/**
		 * Converts a HTML String to a DOM Node, without wrapping a div around it
		 * @method _convertToDomNode
		 * @param template the html string to be converted to a DOM node
		 * @returns A DOM Node 
		 */
		_convertToDomNodeNoWrapper: function(template){
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
		},
		
		/**
		 * Override _substituteMemberName as there are only NLS strings to be substituted 
		 * no XPath values, functions etc. This function substitutes the member name in the template
		 * with the actual member item name.
		 * @method _substituteMemberName
		 * @param template
		 * @param renderer
		 * @returns
		 */
		_substituteMemberName: function(template,memberName){
			return template.replace("${memberName}", memberName);
		}
	});
	
	return SearchBoxRenderer;
});