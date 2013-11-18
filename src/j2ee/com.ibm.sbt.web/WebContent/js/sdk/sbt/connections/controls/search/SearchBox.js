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

define(["../../../declare", "../../../lang", "../../../dom", "../../../widget/_TemplatedWidget", "./SearchBoxRenderer",
        "../../SearchService", "../../CommunityService"], 
		function(declare, lang, dom, _TemplatedWidget, SearchBoxRenderer,SearchService,CommunityService){
	/**
	 * @class SearchBox
	 * @namespace sbt.connections.controls.search
	 * @module sbt.connections.controls.search.SearchBox
	 */
	var SearchBox = declare(_TemplatedWidget,{
		
		// TODO why is type not listed here? what does that parameter mean?
		
		/** 
		 * TODO The "Loading" string needs to be externalied into nls. This should be in the renderer?
		 * Loading template 
		 */
		templateString: "<div><strong> Loading... </strong></div>",
        
		/**
		 * The Renderer For this control 
		 */
		renderer: null,
        
		/** 
		 * The Application the user will select for example files, wikis etc, initially set to all 
		 */
        selectedApplication: "allconnections",       	
        
        /** 
         * TODO should this be public?
         * The phrase the user will search for.
         */
        searchQuery: "",
        
        /** 
         * Search type, valid values are 'my' || 'public' (defaut value is 'my')
         */
        searchType: "my",

        /** 
         * Search suffix, will be appended to every search query 
         */
        searchSuffix: null,
        
        /**
         * Search arguments, will be added to each search request
         */
        searchArgs: null,

        /** 
         * Search Service, used to perform the search 
         */
        searchService: null,
        
        /**
         * Community service to perform searches on my Communities
         */
        communityService: null,

		/** 
		 * 	TODO  Better pattern is to set is during postMixInProperties 
		 * This list is used to keep track of selected members 
		 */
		members: [],
		
		/**
		 * 	TODO  Better pattern is to set is during postMixInProperties 
		 * The result the user has chosen from the search suggestions 
		 */
		_selectedResultItem : {text:"",id:""},
		
		/*
		 * Selected row is used for keyboard navigation in the applications pop up
		 */
		_selectedRow : -1,
		
		/*
		 * 
		 */
		_searchInput: null,
		
        /**
         * @method constructor The constructor for the SearchBox class
         * @param args
         */
		constructor: function(args){
			lang.mixin(this, args);
			
		},

		/**
		 * TODO Document Me 
		 */
		setInputValue: function(value){
			this._searchInput.value = value;
		},
		
		/**
		 * Invoked before rendering occurs, and before any dom nodes are created.
		 * This is the place to change the widget properties before it is rendered.
		 */
		postMixInProperties : function() {
		},
		
		/**
		 * TODO Document Me 
		 */
		getInputValue: function(){
			return this._searchInput.value;
		},
		
		/**
		 * Set the disabled state of the input control for the SearchBox.
		 * 
		 * @param disabled True to disable the input control
		 * @return this
		 */
		setInputDisabled: function(disabled) {
			this._searchInput.disabled = disabled;
			return this;
		},
		
		/**
		 * Return the disabled state of the input control for the SearchBox.
		 * 
		 * @return True is the input control is disabled and otherwise false
		 */
		isInputDisabled: function(){
			return this._searchInput.disabled;
		},
		
		/**
		 * TODO Document Me 
		 */
		getSelectedResult: function(){
			return this._selectedResultItem;
		},
		
		/**
		 * TODO is this needed
		 * TODO Document Me 
		 */
		setSelectedResult: function(name,id){
			if(name){
				this._selectedResultItem.name = name;
			}
			if(id){
				this._selectedResultItem.id = id;
			}
		},
		 
		/**
		 * Function is called after this class has been constructed
		 * the functions in the post create need to be called after the class has been
		 * instantiated so parameters in the dijit base classes can be initialised. 
		 * @method postCreate 
		 * @param args
		 */
		postCreate: function(args){
			this.createDefaultRenderer(args);
			this.domNode = this.renderer.getDomNode(this);
			this.renderer.render(this,this.domNode,{});
			
			if (this.memberList) {
				// Create member list
				this.renderer.renderMemberList(this.domNode);
			}
		},
		
		/**
		 * Creates a SearchBoxRenderer and sets it as the renderer for this class.
		 * @method CreateDefaultRenderer
		 * @param args
		 */
		createDefaultRenderer: function(args){
			this.renderer = new SearchBoxRenderer(args);
		},
		
		/** 
		 * When the user clicks the apps lists, this function renderers a pop up
		 * of all the different applications they can select from
		 * @method handleClick
		 */
		handleClick: function(){
			this.searchBoxAction.renderPopUp(this);
		},
		
		/**
		 * event handler for blur events
		 * @method handleBlur
		 * @param element the element that fired the event
		 * @param obj
		 * @param event the blur event
		 */
		handleBlur: function(element,obj,event){
			if(!event){
				event = window.event;
			}
			// TODO why two seperate calls to removePopUp, logic looks overly complex here
			//For Keyboard Accessibility, this only needs to work for firefox(accessible path) if other browsers 
			//do not support this property that is okay
			if(event.explicitOriginalTarget){
				if(event.explicitOriginalTarget.nodeName == "TD" || event.explicitOriginalTarget.nodeName == "TR" || event.explicitOriginalTarget.nodeName == "#text"){
					
				}else{
					this.searchBoxAction.removePopUp(this);
				}
			}else{
				this.searchBoxAction.removePopUp(this);
			}	
		},
		
		/**
		 * When a user selects an application from the apps pop up
		 * there choice is stored, and the pop up is removed. 
		 * @method setSelectedApplication
		 * @param element  The pop up HTML element
		 * @param object 
		 * @param event  The Event 
		 */
		setSelectedApplication: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.setSelectedApplication(element,object,event,this);
		},

		/**
		 * When the user hovers over an application in the applications pop up, the background gets highlighted
		 * @method displayHighlight
		 * @param element The pop up HTML element
		 * @param object
		 * @param event The event
		 */
		displayHighlight: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.highLight(element,object,event);
		},
		
		/**
		 * Closes a member item (by removing it from its parent; the member list)
		 * @method closeMemberItem
		 * @param element  The member list item
		 * @param object 
		 * @param event  The Event 
		 */
		closeMemberItem: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.closeMemberItem(this, element,object,event);
		},
		
		/**
		 * When the user moves the mouse away from the application item in the pop up
		 * the background highlight it removed.
		 * @method removeHighlight
		 * @param element The pop up HTML element
		 * @param object
		 * @param event The Event
		 */
		removeHighlight: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.removeHighlight(element,object,event);
		},
		
		/**
		 * When the user types a search query it is stored in this.searchQuery
		 * @method setSearchQuery
		 * @param event The Event 
		 */
		setSearchQuery: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.setSearchQuery(event,this);
		},
		
		/**
		 * perform a search based on the users query
		 * @method search
		 * @param event The event
		 */
		search: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.search(event,this);
		},
		
		/**
		 * Used to handle keyboard events
		 * @method onKeyPress
		 * @param element the HTML element
		 * @param obj
		 * @param event the Event
		 */
		onKeyPress: function(element, obj, event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.onKeyPress(element, obj, event, this);
		},
		
		/**
		 * TODO Document Me
		 * @param element
		 * @param obj
		 * @param event
		 */
		suggest: function(element, obj, event){
			if(!event){
				event = window.event;
			}
			if(this.searchSuggest == "on"){
				this.searchBoxAction.suggest(event, this);
			}			
		},
		
		/**
		 * SearchBoxAction contains functions to handle events
		 * this should be overridden to change the action 
		 * of the event handler function  
		 */
		searchBoxAction : {
			
			_appsPopUp: null,
			
			_suggestionPopUp: null,
			
			_searchInput: null,
			
			/**
			 * Handles keyboard navigation 
			 * @param element the table element that fired the onKeypressEvent
			 * @param obj
			 * @param event the Event
			 */
			onKeyPress: function(element, obj, event,self){
				//If the user presses enter
				if(event.key == "Enter"){
					self.setSelectedApplication(element.rows[self._selectedRow], obj, event);
					self._selectedRow = -1;
				//If the user press the down arrow key to navigate down the list	
				}else if(event.key == "Down"){
					
					if(self._selectedRow != element.rows.length ){
						if(self._selectedRow != element.rows.length-1){
							self._selectedRow++;
						}
						self.displayHighlight(element.rows[self._selectedRow], obj, event);
						if(self._selectedRow > 0){
							self.removeHighlight(element.rows[self._selectedRow-1], obj, event);
						}	
					}
				//If the user presses the up key to navigate up the list	
				}else if(event.key == "Up"){
					
					if(self._selectedRow <= 0){
						self.searchBoxAction.removePopUp(self);
						self._selectedRow = -1;
					}else{
						self._selectedRow--;
						self.removeHighlight(element.rows[self._selectedRow+1], obj, event);
						if(self.selectedRow !=0){
							self.displayHighlight(element.rows[self._selectedRow], obj, event);
						}		
					}	
					
				}else if(event.key == "Tab"){
					self._selectedRow = -1;
				}	
			},
			
			/**
			 * Closes a member item (by removing it from its parent; the member list)
			 * @method closeMemberItem
			 * @param context
			 * @param element  The member list item
			 * @param object 
			 * @param event  The Event 
			 */
			closeMemberItem: function(context, element,object,event){
				// Get parent node
				var item = event.target.parentNode;

				// TODO if someone overrides the renderer will this work?
				
				// Prepare HTML for comparison
				var memberNode = item.parentNode;
				var memberNodeHtml = memberNode.innerHTML;
				memberNodeHtml = memberNodeHtml.replace(/\s+/g, '');
				memberNodeHtml = memberNodeHtml.replace(/ /g, '');
				
				// Remove member from list
				for (var i = 0; i < context.members.length; i++) {
					var member = context.members[i];
					var html = member.html;
					
					// Skip null entries (null entries represent entries that have been deleted)
					if (html == null) {
						continue;
					}
					
					// Remove dojo action listeners
					html = html.replace(/data-dojo-attach-event=".*"/, "");
					html = html.replace(/data-dojo-attach-event='.*'/, "");
					
					// Remove whitespace
					html = html.replace(/\s+/g, '');
					html = html.replace(/ /g, ' ');
					
					// Compare strings
					if (html == memberNodeHtml) {
						// Delete objects by setting their properties to null
						context.members[i].html = null;
						context.members[i].id = null;
						context.members[i].name = null;
						break;
					}
				}
				
				// Remove it
				item.parentNode.removeChild(item);
			},
			
			/**
			 * Opens the pop up showing a list of applications
			 * @method renderPopUp 
			 * @param self Context
			 */
			renderPopUp: function(self){
				// TODO shouldn't this just pass thru to the renderer?
				if(this._suggestionPopUp ){
					for(var i=0;i<self.domNode.children.length;i++){
						if(self.domNode.children[i] === this._suggestionPopUp){
							self.renderer.removeSuggestionPopUp(self.domNode,this._suggestionPopUp);
						}
					}
				}
				this._appsPopUp = self.renderer.renderPopUp(self,self.domNode);
			},
			
			/**
			 * Remove the Applications pop up
			 * @method removePopUp
			 * @param self Context 
			 */
			removePopUp: function(self){
				self.renderer.removePopUp(self.domNode,this._appsPopUp);
			},
			
			/**
			 * Stores the name of the application the user has selected and closes the application pop up
			 * @method setSelectedApplication
			 * @param element The HTML element 
			 * @param object
			 * @param event The Event
			 * @param self Context
			 */
			setSelectedApplication: function(element,object,event,self){
				this.removeHighlight(element, object, event);
				self.selectedApplication = element.children[1].textContent;
				self.renderer.removePopUp(self.domNode,this._appsPopUp);
				self.renderer.changeSelectedApplication(element.children[1].textContent,element.children[0].children[0]);
				
			},
			
			/**
			 * TODO Document Me
			 * 
			 * @param event
			 * @param popUp
			 * @param context
			 */
			setSuggestedSearch: function(event,popUp,context){
				var targetElement;
				var value;
				if(!event){
					event = window.event;
					targetElement = event.srcElement;
					value = targetElement.innerText;
				}else{
					targetElement = event.target;
					value = targetElement.textContent;
				}
			 
				var id = targetElement.id;
				var input = this._searchInput;
				
    			context._selectedResultItem.text = value;
    			context._selectedResultItem.id = id;
    			
				context.searchQuery = value;
				
				
				// Member list feature enabled
				if (context.memberList) {
					// We don't want to display the suggested search term (instead
					// we will just add it to the members list - see below)
					input.value = "";
					
					// Create member list item
					context.renderer.renderMemberListItem(context, value, id);

				} else {
					input.value = value;
				}
				
				while (popUp.firstChild) { 
				    popUp.removeChild(popUp.firstChild); 
				}
				context.renderer.removeSuggestionPopUp(context.domNode,popUp);
			},
			
			/**
			 * Highlights an element
			 * @method highLight
			 * @param element The HTML element to highlight
			 * @param object
			 * @param event the Event
			 */
			highLight: function(element,object,event){
				element.style.backgroundColor = "#E1F4F9";
			},
			
			/**
			 * Remove background highLight from an element which looses focus
			 * @method removeHighlight
			 * @param element the HTML element to remove the background highlight from
			 * @param object
			 * @param event the event 
			 */
			removeHighlight: function(element,object,event){
				element.style.backgroundColor = "#FFFFFF";
			},
			
			/**
			 * Sets the user's search query
			 * @method setSearchQuery
			 * @param event
			 * @param self
			 */
			setSearchQuery: function(event,self){
				self.searchQuery = event.target.value;
			},
			
			/**
			 * Provides a suggestion as to what the user is trying to search for
			 * @method suggest
			 * @param event the event
			 * @param context the context this
			 */
			// TODO What is the difference between suggest and search?
			suggest: function(event,context){
				var applicationParam = context.selectedApplication.toLocaleLowerCase();
            	applicationParam = applicationParam.replace(/ /g,'');
				
            	var inputBox = event.target;
				this._searchInput = inputBox;
				
				var query = inputBox.value;
				if(context.searchSuffix) {
					query = query + context.searchSuffix;
				}
				var popUp = context.renderer.renderSuggestionPopUp(context,context.domNode);
				this._suggestionPopUp = popUp;								
				
				var requestArgs;
				if(context.constraint){
					var jsonString = JSON.stringify(context.constraint);
					requestArgs = {"component": applicationParam, constraint:jsonString};
				}else{
					requestArgs = {"component": applicationParam};
				}
				if(context.searchArgs){
					lang.mixin(requestArgs,context.searchArgs);	
				}
				
				// TODO the && is not needed. Why do all the previous stuff if empty query is ignored?
				if(query && query != ""){
					// TODO This should only happen once!
					if(context.endpoint){
						searchService = new SearchService({endpoint:context.endpoint});
					}else{
						searchService = new SearchService();
					}
					
					var promise;
					if (context.searchType == "my") {
						promise = searchService.getMyResults(query, requestArgs);
					} else if (context.searchType == "myCommunities"){
						
						var args = {search:query};
						if(context.endpoint){
							communityService = new CommunityService({endpoint:context.endpoint});
						}else{
							communityService = new CommunityService();
						}						
						
						promise = communityService.getMyCommunities(args);
					}	else {
					
						promise = searchService.getResults(query, requestArgs);
					}
			        promise.then(
			            function(results) {
			            	context.searchBoxAction.handleSuggestResult(results,context,popUp,context.searchType);
			            },
			            
			            function(error) {
			                console.log(error);
			            }
			        );
				}
			},
			
			/**
			 * 
			 * @method handleSuggestResult
			 * @param results the results from the suggested search
			 * @param context the This of the outer class
			 * @param popUp the popUp Element where results are displayed 
			 */
			handleSuggestResult: function(results,context,popUp,searchType){
				// TODO should the renderer handle this?
				while (popUp.firstChild) { 
				    popUp.removeChild(popUp.firstChild); 
				}
				for(var i=0;i<results.length;i++){
					var data = document.createElement("li");
            		var title = results[i].getTitle();
            		var id="";
            		if(searchType=="myCommunities"){
            			id = results[i].getCommunityUuid();
            		}else{
            		    id = results[i].getId();
            		}
            		dom.setText(data, title);
            		data.id = id;

            		data.onclick = function (event) { 
            			context.searchBoxAction.setSuggestedSearch(event,popUp,context);
            		};    		

            		popUp.appendChild(data);
            	}
				
			},
			
			/**
			 * When the user clicks the search button 
			 * @method search
			 * @param event The Event
			 */
			search: function(event,context){
				if(this._suggestionPopUp ){
					for(var i=0;i<context.domNode.children.length;i++){
						if(context.domNode.children[i] === this._suggestionPopUp){
							context.renderer.removeSuggestionPopUp(context.domNode,this._suggestionPopUp);
						}
					}
				}
				
				var applicationParam = context.selectedApplication.toLocaleLowerCase();
            	applicationParam = applicationParam.replace(/ /g,'');
				
            	//if this control is going to retrieve the search results from the server
				if(context.type == "full"){
					var requestArgs;
					if(context.constraint){
						var jsonString = JSON.stringify(context.constraint);
						requestArgs = {"component": applicationParam, constraint:jsonString};
					}else{
						requestArgs = {"component": applicationParam};
					}
					if(context.searchArgs){
						lang.mixin(requestArgs,context.searchArgs);	
					}
					if(context.endpoint){
						searchService = new SearchService({endpoint:context.endpoint});
					}else{
						searchService = new SearchService();
					}
					
					var query = context.searchQuery;
					if(context.searchSuffix) {
						query = query + context.searchSuffix;
					}
					var self = context;
				   
					var promise;
					if (context.searchType == "my") {
						promise = searchService.getMyResults(query, requestArgs);
					} else if (context.searchType == "myCommunities"){
						
						var args = {search:query};
						if(context.endpoint){
							communityService = new CommunityService({endpoint:context.endpoint});
						}else{
							communityService = new CommunityService();
						}						
						
						promise = communityService.getMyCommunities(args);
					}	else {
					
						promise = searchService.getResults(query, requestArgs);
					}
			        promise.then(
			            function(results) {
			            	if (context.memberList) {
			            		// If the member list feature is enabled then we need
			            		// to use a different search result event since we want the
			            		// members to be added to the members list and NOT
			            		// just displayed in the results table
			            		for(var i = 0; i < newResults.length; i++) {
			            			// Render each item in the search results
			            			if(context.searchType="myCommunities"){
			            				context.renderer.renderMemberListItem(context, newResults[i].getTitle(), results[i].getCommunityUuid());
			            			}else{
			            				context.renderer.renderMemberListItem(context, newResults[i].getTitle(), results[i].getId());
			            			}
			            			
			            		}	   
			            	} else {
			            		var evt = document.createEvent("Event");
			            		evt.initEvent("searchResultEvent",true,true);
			            		evt.results = results;
			            		self.domNode.dispatchEvent(evt);
			            		evt = null;				
			            	}
			            },
			            function(error) {
			                console.log(error);
			            }
			        );
				}else {
					//use another component to retrieve
					var evt = document.createEvent("Event");
	            	evt.initEvent("searchResultEvent",true,true);
	            	evt.selectedApplication = applicationParam;
	            	evt.searchQuery = context.searchQuery;
	            	
	            	context.domNode.dispatchEvent(evt);
	            	evt = null;
				}
				
			}
		
		}
		
	});

	return SearchBox;
});