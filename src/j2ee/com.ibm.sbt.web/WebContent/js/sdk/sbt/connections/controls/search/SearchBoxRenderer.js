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
        "../../../i18n!./nls/SearchBoxRenderer",
        "../../../text!./templates/MemberListItemTemplate.html",
        "../../../text!./templates/MemberListTemplate.html",
        "../../../text!./templates/SingleApplicationSearch.html",
        "../../../text!./templates/SingleSearchPopUp.html"], 
        function(declare,lang, template, SuggestTemplate, PopUpTemplate, SuggestionPopUp ,nls,
        		MemberListItemTemplate, MemberListTemplate, SingleApplicationSearch, SingleSearchPopUp){
	/**
	 * @class SearchBoxRenderer
	 * @namespace sbt.connections.controls.search
	 * @module sbt.connections.controls.search.SearchBoxRenderer
	 */
	var SearchBoxRenderer = declare(null,{
		
		nls: null,
		
		_appsPopUp: null,
		
		_appsMemberListItem: null,
		
		_appsMemberList: null,
		
		_suggestionPopUp: null,
		
		/**
		 * SearchBoxRenderer class constructor function
		 * @method constructor
		 * @param args
		 */
		constructor: function(args){	
			this.nls = nls;
		},
		
		/**
		 * Converts the HTML pop up template into a DOM node.
		 * Creates a Div element and uses the template as the div's inner HTML
		 * @method getDomeNode
		 * @returns The Search Box Dom Node 
		 */
		getDomNode: function(SearchBox){
			
			var htmlTemplate = "";
			
			if(SearchBox.predefinedSearch){
				var domStr = this._substituteItems(SingleApplicationSearch, this);
				SingleApplicationSearch = domStr;
				htmlTemplate = SingleApplicationSearch;
			}else{
				var domStr = this._substituteItems(template, this);
				template = domStr;
				htmlTemplate = template;
			}

			var div = this._convertToDomNode(htmlTemplate);
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
			
			var obj = this._convertToDomNode(domstr);
			
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
		renderPopUp: function(searchBox,el){
			
			if(!this._appsPopUp){
				this._appsPopUp = this.getPopUpNode();
				this._doAttachEvents(searchBox,this._appsPopUp,{});	
			}
			
			el.appendChild(this._appsPopUp);
			this._appsPopUp.focus();
			return this._appsPopUp;
		},
		
		/**
		 * renders a member list item
		 * @method renderMemberListItem
		 * @param searchBox the SearchBox class
		 * @param memberName The member name to display
		 * @param memberId The member's profile id - this acts as a unique identifier (needed when, for example,
		 * 					  creating a new community)
		 */
		renderMemberListItem: function(searchBox, memberName, memberId){
			// Get node
			this._appsMemberListItem = this.getMemberListItemNode(memberName);
			
			// Create member object for storage
			var newMember = new Object();
			newMember.html = this._appsMemberListItem.innerHTML;
			newMember.id = memberId;
			newMember.name = memberName;
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
		 * @param el HTML Element / DOM Node
		 */
		renderMemberList: function(el){
			
			if(!this._appsMemberList){
				// Generate the DOM object representing the member list
				this._appsMemberList = this.getMemberListNode();
			}
			
			// Append the list to the parent
			el.appendChild(this._appsMemberList);
		
			// Request focus
			this._appsMemberList.focus();
			
			return this._appsMemberList;
		},
		
		
		/**
		 * Renders the suggestions that appear under the search box.
		 * @method renderSuggestionPopUp 
		 * @param searchBox The searchBox class
		 * @param el The SearchBox DOM node / HTML element
		 * @returns The search suggestion pop up as a DOM Node
		 */
		renderSuggestionPopUp: function(searchBox,el){
			
			if(!this._suggestionPopUp){
				if(searchBox.predefinedSearch){
					this._suggestionPopUp= this._convertToDomNode(SingleSearchPopUp);
				}else{
					this._suggestionPopUp= this._convertToDomNode(SuggestionPopUp);
				}
				
			}
			
			el.appendChild(this._suggestionPopUp);
			return el.lastChild;
		},
		
		/**
		 * Removes the applications pop up
		 * @method removePopUp
		 * @param searchBoxElement The searchBox HTML Element
		 * @param popUp The DOM node that represents the popup that displays the list of applications on connections
		 */
		removePopUp: function(searchBoxElement,popUp){
			searchBoxElement.removeChild(popUp);
		},
		
		/**
		 * remove the list of suggestions that appeas when searching
		 * @method removeSuggestionPopUp
		 * @param searchBoxElement The DOM Node that represents the search box
		 * @param popUpElement the DOM node pop up
		 */
		removeSuggestionPopUp: function(searchBoxElement,popUpElement){
			searchBoxElement.removeChild(popUpElement);
		},
		
		/**
		 * Changes the Text displayed in the "currently selected" application text field 
		 * @method changeSelectedApplication
		 * @param selectedApplication The name of the application to display
		 * @param el The table row element
		 */
		changeSelectedApplication: function(selectedApplication, trImgIcon){
			var elements = document.getElementsByTagName("*");
			for(var i=0;i<elements.length;i++){
				if(elements[i].textContent == nls.selectedApplication){
					
					//change the text showing the selected application
					elements[i].textContent = selectedApplication;
					nls.selectedApplication = selectedApplication;
					
					//change the css of the image icon to relate to the selected application
					var previous = i -1; // index
					var imgEl = elements[previous];
					imgEl.classList.remove(imgEl.classList[imgEl.classList.length-1]);
					var index = trImgIcon.classList.length-1;
					var newClass = trImgIcon.classList[index];
					imgEl.classList.add(newClass);
				}
			}
		},
		
		/*
		 * Converts a HTML String to a DOM Node, 
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
			return div.firstChild;	
		},
		
		/*
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
		
		/*
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
		},
		
		/*
		 * connects events to event handlers  
		 */
		_doAttachEvents: function(searchBox, el, data) {
            var nodes = (el.all || el.getElementsByTagName("*"));
            for (var i in nodes) {
                var attachEvent = (nodes[i].getAttribute) ? nodes[i].getAttribute("data-dojo-attach-event") : null;
                if (attachEvent) {
                    nodes[i].removeAttribute("data-dojo-attach-event");
                    var event, events = attachEvent.split(/\s*,\s*/);
                    while((event = events.shift())) {
                        if (event) {
                            var func = null;
                            if (event.indexOf(":") != -1) {
                                var eventFunc = event.split(":");
                                event = lang.trim(eventFunc[0]);
                                func = lang.trim(eventFunc[1]);
                            } else {
                                event = lang.trim(event);
                            }
                            if (!func) {
                                func = event;
                            }
                            var callback = searchBox._hitch(searchBox, searchBox[func], nodes[i], data);
                            searchBox._connect(nodes[i], event, callback);
                        }
                    }
                }
            }
        }
	});
	
	return SearchBoxRenderer;
});