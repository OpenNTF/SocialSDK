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
			this.domNode = this.renderer.getDomNode();
			this.renderer.render(this,this.domNode,{});		
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
		
		/**SearchBoxAction contains functions to handle events
		 * this should be overridden to change the action 
		 * of the event handler function  */
		searchBoxAction : {
			
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
			 * Opens the pop up showing a list of applications
			 * @method renderPopUp 
			 * @param self Context
			 */
			renderPopUp: function(self){
				self.renderer.renderPopUp(self,self.domNode);
			},
			
			/**
			 * Remove the Applications pop up
			 * @method removePopUp
			 * @param self Context 
			 */
			removePopUp: function(self){
				self.renderer.removePopUp(self.domNode);
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
				self.selectedApplication = element.children[1].textContent;
				self.renderer.removePopUp(self.domNode);
				self.renderer.changeSelectedApplication(element.children[1].textContent);
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
			 * When the user clicks the search button 
			 * @method search
			 * @param event The Event
			 */
			search: function(event,context){
				
				var applicationParam = context.selectedApplication.toLocaleLowerCase();
            	applicationParam = applicationParam.replace(/ /g,'');
				
            	//if this control is going to retrieve the search results from the server
				if(context.type == "full"){
					var requestArgs = {"component": applicationParam};
					searchService = new SearchService();
				    var promise = searchService.getMyResults(context.searchQuery,requestArgs);
				    
				    var self = context;
			        promise.then(
			            function(results) {
			            	var evt = document.createEvent("Event");
			            	evt.initEvent("searchResultEvent",true,true);
			            	evt.results = results;
			            	self.domNode.dispatchEvent(evt);
			            	evt = null;
			            },
			            function(error) {
			                console.log(error);
			            }
			        );
			        
				} else {
					//use another component to retrieve
					var evt = document.createEvent("Event");
	            	evt.initEvent("searchReadyEvent",true,true);
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