require(["sbt/dom", "sbt/connections/controls/search/SearchBox"], function(dom, SearchBox) {
        
	    var searchBox = new SearchBox({
        	type:"full",
        	constraint: { type:"field",id:"title" } 
        });
                
        dom.byId("searchNode").appendChild(searchBox.domNode);
        
        searchBox.domNode.addEventListener("searchResultEvent",function(event){
        	if(!event){
        		event = window.event;
        	}
        	var resultDiv = dom.setText("results", "");
        	//Create a table to display results
        	var table = document.createElement("table");
        	if(event.results.length > 0){
        		for(var i=0;i<event.results.length;i++){
	        		var title = event.results[i].getTitle();
	        		var row = document.createElement("tr");
	        		var data = document.createElement("td");
	        		dom.setText(row, title);
	        		row.appendChild(data);
	        		table.appendChild(row);
        		}
        	}else{
        		var row = document.createElement("tr");
        		var data = document.createElement("td");
        		dom.setText(row, "Your Search Returned No Results");
        		row.appendChild(data);
        		table.appendChild(row);
        	}
        	
        	
        	resultDiv.appendChild(table);
        	
        },false);
        
});