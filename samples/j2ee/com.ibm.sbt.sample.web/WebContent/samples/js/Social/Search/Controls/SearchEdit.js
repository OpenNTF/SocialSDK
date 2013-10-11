require(["sbt/dom", "sbt/connections/controls/search/SearchBox"], function(dom, SearchBox) {
        
	    var searchBox = new SearchBox({
        	type:"full",
        	constraint: {type:"field",id:"title" } 
        });
                
        dom.byId("searchBox").appendChild(searchBox.domNode);
        
        searchBox.domNode.addEventListener("searchResultEvent",function(event){
        	if(!event){
        		event = window.event;
        	}
        	var resultDiv = dom.byId("results");
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
        	
        },false);
        
});