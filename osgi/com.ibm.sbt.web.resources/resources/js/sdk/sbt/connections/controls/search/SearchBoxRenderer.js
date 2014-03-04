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
 
dojo.provide("sbt.connections.controls.search.SearchBoxRenderer");
/*
define('sbt/connections/controls/search/SearchBoxRenderer',["sbt/declare",
        "sbt/lang",
        "sbt/text!sbt/connections/controls/search/templates/SearchBoxTemplate.html",
        "sbt/text!sbt/connections/controls/search/templates/SearchSuggestTemplate.html",
        "sbt/text!sbt/connections/controls/search/templates/PopUpTemplate.html",
        "sbt/text!sbt/connections/controls/search/templates/SuggestPopUpTemplate.html",
        "sbt/i18n!sbt/connections/controls/search/nls/SearchBoxRenderer",
        "sbt/text!sbt/connections/controls/search/templates/MemberListItemTemplate.html",
        "sbt/text!sbt/connections/controls/search/templates/MemberListTemplate.html",
        "sbt/text!sbt/connections/controls/search/templates/SingleApplicationSearch.html",
        "sbt/text!sbt/connections/controls/search/templates/SingleSearchPopUp.html"], 
        function(declare,lang, template, SuggestTemplate, PopUpTemplate, SuggestionPopUp ,nls,
        		MemberListItemTemplate, MemberListTemplate, SingleApplicationSearch, SingleSearchPopUp){
*/
define('sbt/connections/controls/search/SearchBoxRenderer',["sbt/declare",
        "sbt/lang",
        //"sbt/text!sbt/connections/controls/search/templates/SearchBoxTemplate.html",
        //"sbt/text!sbt/connections/controls/search/templates/SearchSuggestTemplate.html",
        //"sbt/text!sbt/connections/controls/search/templates/PopUpTemplate.html",
        //"sbt/text!sbt/connections/controls/search/templates/SuggestPopUpTemplate.html",
        "sbt/i18n!sbt/connections/controls/search/nls/SearchBoxRenderer",
        //"sbt/text!sbt/connections/controls/search/templates/MemberListItemTemplate.html",
        //"sbt/text!sbt/connections/controls/search/templates/MemberListTemplate.html",
        //"sbt/text!sbt/connections/controls/search/templates/SingleApplicationSearch.html",
        //"sbt/text!sbt/connections/controls/search/templates/SingleSearchPopUp.html" 
        ],
        function(declare,lang, nls){
	/**
	 * @class SearchBoxRenderer
	 * @namespace sbt.connections.controls.search
	 * @module sbt.connections.controls.search.SearchBoxRenderer
	 */
     var template = '<span class="lotusTitleBar2">' +
                        '<span style="max-width:100%; padding:0px 0px 0px 0px;" class="lotusWrapper">' +
                            '<span style="border-bottom: none;"class="lotusInner">' +
                                '<div style="position:relative; margin-top:0px; top:0px;" class="lotusSearch" >' +
                                        '<table cellspacing="0" role="presentation" class="lotusLayout">' +
                                            '<tbody>' +
                                                '<tr>' +
                                                    '<td>' +
                                                        '<div class="lotusHidden">${nls.refine}</div>' +
                                                        '<div>' +
                                                            '<a data-dojo-attach-event="onclick: handleClick" class="lotusScope" href="javascript:;">' +
                                                                '<img src="images/blank.gif?etag=20120919.054848"' +
                                                                    'alt="${nls.allConnectionsIcon}" class="lotusIcon lconnSprite lconnSprite-iconConnections16">' +
                                                                '<span>${nls.allConnections}</span>' +
                                                                '<span role="presentation" class="lotusAltText">▼</span>' +
                                                            '</a>' +
                                                        '</div>' +
                                                    '</td>' +
                                                    
                                                    '<td>' +
                                                        '<span><label for="input" style="display:none;">text</label>' +
                                                            '<input id="input" type="text" data-dojo-attach-event="onkeyup: suggest, blur: setSearchQuery" class="lotusText lotusInactive" />' +
                                                        '</span>' +
                                                    '</td>' +
                                                    
                                                    '<td>' +
                                                        '<span class="lotusBtnImg"> ' +
                                                            '<input data-dojo-attach-event="click: search" class="lotusSearchButton" type="image" title="${nls.search}" alt="${nls.search}"' +
                                                                'src="images/blank.gif?etag=20120919.054848"> ' +
                                                            '<a class="lotusAltText" alt="${nls.search}" href="javascript:;">${nls.search}</a>' +
                                                        '</span>' +
                                                    '</td>' +
                                                '</tr>' +
                                            '</tbody>' +
                                        '</table>' +
                                '</div>' +
                            '</span>' +
                        '</span>' +
                    '</span>';
    var SuggestTemplate = '<span class="lotusTitleBar2">' +
                            '<span style="max-width:100%; padding:0px 0px 0px 0px;" class="lotusWrapper">' +
                                '<span style="border-bottom: none;"class="lotusInner">' +
                                    '<div style="position:relative; margin-top:0px; top:0px;" class="lotusSearch" >' +
                                            '<table cellspacing="0" role="presentation" class="lotusLayout">' +
                                                '<tbody>' +
                                                    '<tr>' +
                                                        '<td>' +
                                                        '<div class="lotusHidden">${nls.refine}</div>' +
                                                            '<div>' +
                                                                '<a data-dojo-attach-event="onclick: handleClick" class="lotusScope" href="javascript:;">' +
                                                                    '<img src="images/blank.gif?etag=20120919.054848"' +
                                                                        'alt="${nls.allConnectionsIcon}" class="lotusIcon lconnSprite lconnSprite-iconConnections16">' +
                                                                    '<span>${nls.allConnections}</span>' +
                                                                    '<span role="presentation" class="lotusAltText">▼</span>' +
                                                                '</a>' +
                                                            '</div>' +
                                                        '</td>' +
                                                        
                                                        '<td>' +
                                                            '<span ><label for="com.ibm.sbt.search.input" style="display:none;">text</label>' +
                                                                '<input id="com.ibm.sbt.search.input" type="text" data-dojo-attach-event="onkeyup: suggest, blur: setSearchQuery" class="lotusText lotusInactive" />' +
                                                                
                                                            '</span>' +
                                                        '</td>' +
                                                        
                                                        '<td>' +
                                                            '<span class="lotusBtnImg"> ' +
                                                                '<input data-dojo-attach-event="click: search" class="lotusSearchButton" type="image" title="${nls.search}" alt="${nls.search}"' +
                                                                    'src="images/blank.gif?etag=20120919.054848"> ' +
                                                                '<a class="lotusAltText" alt="${nls.search}" href="javascript:;">${nls.search}</a>' +
                                                            '</span>' +
                                                        '</td>' +
                                                    '</tr>' +

                                                '</tbody>' +
                                            '</table>' +
                                    '</div>' +
                                '</span>' +
                            '</span>' +
                        '</span>';
                        
    var PopUpTemplate='<table role="presentation" tabindex="0"' +
                        'data-dojo-attach-event="blur: handleBlur, onkeypress: onKeyPress"' +
                        'class="dijit dijitMenu dijitReset dijitMenuTable lotusNavMenu lconnSearchScope dijitMenuActive"' +
                        'style="position:fixed;">' +
                        '<tbody tabindex="0" class="dijitReset">' +
                            '<tr data-dojo-attach-event="onmouseenter: displayHighlight, onmouseleave: removeHighlight, onclick: setSelectedApplication"' +
                                'class="dijitReset dijitMenuItem lotusAlignLeft">' +

                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconConnections16" />' +
                                '</td>' +

                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.allConnections}</td>' +
                            '</tr>' +
                            '<tr' +
                                'data-dojo-attach-event="onmouseenter: displayHighlight, onmouseleave: removeHighlight, onclick: setSelectedApplication"' +
                                'class="dijitReset dijitMenuItem">' +

                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconStatusUpdate16" />' +
                                '</td>' +

                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.statusUpdates}</td>' +
                            '</tr>' +
                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconActivities16" />' +
                                '</td>' +
                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.activities}</td>' +
                            '</tr>' +

                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconBlogs16" /></td>' +
                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.blogs}</td>' +
                            '</tr>' +

                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconBookmarks16" />' +
                                '</td>' +
                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.bookmarks}</td>' +
                            '</tr>' +

                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconCommunities16" />' +
                                '</td>' +

                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.communities}</td>' +

                            '</tr>' +

                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconFiles16" /></td>' +
                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.files}</td>' +
                            '</tr>' +
                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconForums16" /></td>' +
                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.forums}</td>' +
                            '</tr>' +
                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconProfiles16" />' +
                                '</td>' +
                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.profiles}</td>' +
                            '</tr>' +
                            '<tr' +
                                'data-dojo-attach-event="onclick: setSelectedApplication, onmouseenter: displayHighlight, onmouseleave: removeHighlight"' +
                                'class="dijitReset dijitMenuItem dijitMenuItemSelected">' +
                                '<td class="dijitReset"><img alt=""' +
                                    'class="dijitMenuItemIcon lconnSprite lconnSprite-iconWikis16" /></td>' +
                                '<td colspan="2" class="dijitReset dijitMenuItemLabel">${nls.wikis}</td>' +
                            '</tr>' +
                        '</tbody>' +
                    '</table>';
                    
    var SuggestionPopUp = '<table style="border-radius:0px; list-style:none; cursor: pointer; width: 190px; position:absolute; z-index:100; background-color:#EEEEEE; left:13em; " role="presentation"></table>';
    
    var MemberListItemTemplate = '<span class="lotusFilters">' +
            '<a class="lotusFilter" href="javascript:;" data-dojo-attach-event="click: closeMemberItem" >' +
                '<span>${memberName}</span>' +
                '<span class="lotusClose">X</span>' +
            '</a>&nbsp;&nbsp;' +
        '</span>';
    
    var MemberListTemplate = '<div style="padding-top: 15px;" class="lotusHeader"></div>';
    
    var SingleApplicationSearch = '<span style="position:relative">' +
            '<span>' +
                '<span>' +
                    '<div>' +
                            '<table>' +
                                '<tbody>' +
                                    '<tr>' +
                                        '<td>' +
                                            '<span><label style="display:none;">text</label>' +
                                                '<input style="width: 350px; height:2em;" type="text" data-dojo-attach-event="onkeyup: suggest,blur: setSearchQuery"  />' +
                                            '</span>' +
                                        '</td>' +
                                    '</tr>' +
                                '</tbody>' +
                            '</table>' +
                    '</div>' +
                '</span>' +
            '</span>' +
        '</span>';
    
    var SingleSearchPopUp = '<ul style="z-index:10000 !important; width:345px !important;list-style-type: none !important; padding-left:5px; !important" class="dijitReset dijitMenu lotusui30dojo" role="presentation"></ul>';
    
	var SearchBoxRenderer = declare(null,{
		
		nls: null,
		
		_appsPopUp: null,
		
		_appsMemberListItem: null,
		
		_appsMemberList: null,
		
		_suggestionPopUp: null,
		
		_suggestionContainer: null,
		
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
			
			this._suggestionContainer = document.createElement("span");
			div.appendChild(this._suggestionContainer);
			
			var temp = div.getElementsByTagName("input");
			var input = temp[0];
			SearchBox._searchInput = input;
			
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
			for (var i = 0; i < searchBox.members.length; i++) {
				var member = searchBox.members[i];
				if (member.html == newMember.html) {
					return;
				}
			}
			
			// Add it to the list
			this._appsMemberList.appendChild(this._appsMemberListItem);
			
			// Attach event listeners
			this._doAttachEvents(searchBox,this._appsMemberListItem,{});	
			
			// Keep track of the added member
			searchBox.members.push(newMember);
			
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
			this._suggestionContainer.appendChild(this._suggestionPopUp);
			return this._suggestionContainer.firstChild;
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
			this._suggestionContainer.removeChild(popUpElement);
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
