require(["sbt/dom", "sbt/connections/controls/search/SearchBox", "sbt/config", "sbt/connections/controls/search/SearchGrid"], function(dom, SearchBox,config,SearchGrid) {
   
	var endpoint = config.findEndpoint("connections");
	var auth = endpoint.isAuthenticated;  
	
	    var searchBox = new SearchBox({
        	type:"full",
        	userIsAuthenticated:auth,
        	wildcard:true,
        	searchSuggest:"on"
        });
                
        dom.byId("searchBox").appendChild(searchBox.domNode);
        
        searchBox.domNode.addEventListener("searchResultEvent",function(event){
        	
        	if(!event){
        		event = window.event;
        	}
        	var resultDiv = document.getElementById("gridDiv");
        	resultDiv.innerHTML = "";
        	//Create a table to display results
        	var table = document.createElement("table");
        	if(event.results.length >0){
        		for(var i=0;i<event.results.length;i++){
	        		var title = event.results[i].getTitle();
	        		var row = document.createElement("tr");
	        		var data = document.createElement("td");
	        		row.innerHTML = title;
	        		row.appendChild(data);
	        		table.appendChild(row);
        		}
        	}else{
        		var row = document.createElement("tr");
        		var data = document.createElement("td");
        		row.innerHTML = "Your Search Returned No Results";
        		row.appendChild(data);
        		table.appendChild(row);
        	}
        	
        	
        	resultDiv.appendChild(table);
    		
        });
        
});