require(["sbt/dom","sbt/widget/_TemplatedWidget"],function(dom, Widget){
	
	/**
	 * create an instance of the widget
	 * templateString is an required parameter and must contain valid HTML
	 */
	try{
		var widget = new Widget({
			templateString: "<div></div>"
		});
	
	
		/**
		 * container is a div which contains all of the other elements used in the sample
		 */
		var container = dom.byId("container");	
		
		/**
		 * Demonstrates the _stopEvent() function 
		 */
		try{
			widget.handleClick = function(el,widget,event){
				if(!event){
					event = window.event;
				}
				widget._stopEvent(event); //stop the default action, which is to go to www.ibm.com
				el.textContent = "StopEvent function is working and doAttachEvents is working";
			};
		}catch(error){
			dom.byId(errorDiv).textContent = "Error executing the handle click "+ error;
		}
		
		
		/**
		 * Demonstrates the _doAttachEvents() function
		 */
		try{
			widget._doAttachEvents(container,widget);
		}catch(error){
			dom.byId(errorDiv).textContent = "Error executing the _doAttachEvents() function "+error;
		}
		
		
	    /**
		 * demonstrates the _doAttachPoints() function
		 */
		try{
			widget._doAttachPoints(container,widget);
			widget.testText.textContent = "doAttachPoints function is working";
		}catch(error){
			dom.byId(errorDiv).textContent = "Error in _doAttachPoints() "+error;
		}
		
		
		/**
		 * Demonstrates _hitch(), by passing a different context to a function
		 */
		try{
			var myObj = {
			  method: function(widget){
			    widget.hitchSpan.textContent = "Hitch Function is working";
			  }
			};
			var func = widget._hitch(myObj, "method",widget);
			func();
		}catch(error){
			dom.byId(errorDiv).textContent = "error in _hitch() function" + error;
		}
		
		/**
		 * Demonstrates widget _connect() function
		 */
		try{
			var link = dom.byId("connectLink");
			widget._connect(link, "click", function(){console.log(this.textContent="Connect() function is working");});
		}catch(error){
			dom.byId(errorDiv).textContent = "Error in the _connect() Function";
		}
		
		
		/**
		 * Demonstrates the widgets _substitute() function
		 */
		try{
			var str = "${sub1} ${sub2} ${sub3} ${sub4}";
			var map = {sub1:"Substitute", sub2:"function", sub3: "is", sub4: "working"};
			str = widget._substitute(str,map,null,this);
			dom.byId("substituteSpan").textContent = str;
		}catch(error){
			dom.byId(errorDiv).textContent = "error in the _substitute() function "+error;
		}
		
	    
	    
	}catch(error){
		dom.byId(errorDiv).textContent = "Error creating instance of _TemplatedWidget: "+error;
	}
});