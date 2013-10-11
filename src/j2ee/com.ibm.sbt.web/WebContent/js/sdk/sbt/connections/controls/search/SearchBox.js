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
        "../../SearchService"], 
		function(declare, lang, dom, _TemplatedWidget, SearchBoxRenderer,SearchService){
	/**
	 * @class SearchBox
	 * @namespace sbt.connections.controls.search
	 * @module sbt.connections.controls.search.SearchBox
	 */
	var SearchBox = declare(_TemplatedWidget,{
		
		/** Loading template */
		templateString: "<div><strong> Loading... </strong></div>",
        
		/** The Renderer For this control */
		renderer: null,
        
		/** The Application the user will select for example files, wikis etc, initially set to all */
        selectedApplication: "allconnections",        	
        
        /** The phrase the user will search for */
        searchQuery: "",
        
        /** Search Service, used to perform the search */
        searchService: null,
        
        /**Selected row is used for keyboard navigation in the applications pop up*/
		_selectedRow : -1,
		
		/** This list is used to keep track of selected members **/
		_members : [],
		
		
		
        /**
         * @method constructor The constructor for the SearchBox class
         * @param args
         */
		constructor: function(args){
			lang.mixin(this, args);
		},
		/**
		 * function is called after this class has been constructed
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
			this.searchBoxAction.removeHighlight(element,object,event);
		},
		
		/**
		 * When the user types a search query it is stored in this.searchQuery
		 * @method setSearchQuery
		 * @param event The Event 
		 */
		setSearchQuery: function(element,object,event){
			this.searchBoxAction.setSearchQuery(event,this);
		},
		
		/**
		 * perform a search based on the users query
		 * @method search
		 * @param event The event
		 */
		search: function(element,object,event){
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
			this.searchBoxAction.onKeyPress(element, obj, event, this);
		},
		
		suggest: function(element, obj, event){
			if(this.searchSuggest == "on"){
				this.searchBoxAction.suggest(event, this);
			}			
		},
		
		
		/**SearchBoxAction contains functions to handle events
		 * this should be overridden to change the action 
		 * of the event handler function  */
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

				// Prepare HTML for comparison
				var memberNode = item.parentNode;
				var memberNodeHtml = memberNode.innerHTML;
				memberNodeHtml = memberNodeHtml.replace(/\s+/g, '');
				memberNodeHtml = memberNodeHtml.replace(/ /g, '');
				
				// Remove member from list
				for (var i = 0; i < context._members.length; i++) {
					var member = context._members[i];
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
						context._members[i].html = null;
						context._members[i].id = null;
						context._members[i].name = null;
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
			
			
			setSuggestedSearch: function(event,popUp,context){
				var value = event.target.textContent;
				var id = event.target.id;
				var input = this._searchInput;

				this.searchQuery = value;
				
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
				
				popUp.innerHTML = "";
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
			suggest: function(event,context){
				
				var applicationParam = context.selectedApplication.toLocaleLowerCase();
            	applicationParam = applicationParam.replace(/ /g,'');
				
            	var inputBox = event.target;
				this._searchInput = inputBox;
				
				var query = inputBox.value;
				var popUp = context.renderer.renderSuggestionPopUp(context,context.domNode);
				this._suggestionPopUp = popUp;
				
				popUp.innerHTML = "";
				
				var requestArgs = {};
				
				if(context.constraint){
					requestArgs = {"component": applicationParam, constraint: context.constraint };
				}else{
					requestArgs = {"component": applicationParam};
				}
	
				if(query && query != ""){
					
					searchService = new SearchService();
				    var promise = searchService.getMyResults(query,requestArgs);
	
			        promise.then(
			            function(results) {
			            	context.searchBoxAction.handleSuggestResult(results,context,popUp);
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
			handleSuggestResult: function(results,context,popUp){
				for(var i=0;i<results.length;i++){
            		var row = document.createElement("tr");
            		var data = document.createElement("td");
            		data.innerHTML = results[i].getTitle();
            		data.id = results[i].getId();
            		data.style = "cursor:pointer";
            		data.onclick = function (event) { 
            			
            			context.searchBoxAction.setSuggestedSearch(event,popUp,context);
            		};    		
            		row.appendChild(data);
            		popUp.appendChild(row);
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
					var requestArgs = {};
					
					if(context.constraint){
						requestArgs = {"component": applicationParam, constraint: context.constraint }; 
					}else{
						requestArgs = {"component": applicationParam};
					}
					
					searchService = new SearchService();
				    var promise = searchService.getMyResults(context.searchQuery,requestArgs);
				    
				    var self = context;
			        promise.then(
			            function(results) {
			            	if (context.memberList) {
			            		// If the member list feature is enabled then we need
			            		// to use a different search result event since we want the
			            		// members to be added to the members list and NOT
			            		// just displayed in the results table
			            		for(var i = 0; i < results.length; i++) {
			            			// Render each item in the search results
			            			context.renderer.renderMemberListItem(context, results[i].getTitle(), results[i].getId());
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
				
			},
		}
	});

	return SearchBox;
});