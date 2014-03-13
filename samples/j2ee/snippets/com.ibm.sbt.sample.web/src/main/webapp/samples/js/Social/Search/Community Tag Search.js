require([ "sbt/connections/SearchService", "sbt/dom" ], 
	function(SearchService, dom) {
	
	var searchService = new SearchService();
	
	dom.byId("searchBtn").onclick = function(ev) {
		showError();
    	clearResults();
		showSearching(true);
		
		var tag = dom.byId("tagInput").value;
		
		var requestArgs = {
			scope : "communities"
		};
		
		searchService.getResultsByTag(tag, requestArgs).then(
			function(results) {
				var searching = dom.byId("searching");
				while(searching.firstChild) searching.removeChild(searching.firstChild);
	            if (results.length == 0) {
	            	showError("No communities associated with tag: " + tag);
                } else {
                    for(var i=0; i<results.length; i++){
                        var result = results[i];
                        createRow(result);
                    }
                    dom.byId("resultTable").style.display = "";
                }
			},
			function(error) {
				dom.byId("searching").appendChild(dom.createTextNode(""));
				showError(error.message);
			}
		);
	};
	
	var clearResults = function() {
		var table = dom.byId("tableBody");
		while (table.childNodes[0]) {
            dom.destroy(table.childNodes[0]);
        }
		table.style.display = "none";
	}
	
	var showError = function(message) {
		if (message) {
		    dom.setText("error", "Error: " + message);
		    dom.byId("error").style.display = "";
		} else {
		    dom.setText("error", "");
		    dom.byId("error").style.display = "none";
		}
	};
	
	var showSearching = function(searching) {
		if (searching) {
		    dom.setText("searching", "Searching...");
		    dom.byId("searching").style.display = "";
		} else {
		    dom.setText("searching", "");
		    dom.byId("searching").style.display = "none";
		}
	};
	
	var createRow = function(result) {
        var table = dom.byId("resultTable");
        var tr = document.createElement("tr");
        table.appendChild(tr);
        var td = document.createElement("td");
        td.appendChild(dom.createTextNode(result.getTitle()));
        tr.appendChild(td);
        td = document.createElement("td");
        td.appendChild(dom.createTextNode(result.getId()));
        tr.appendChild(td);
    };
}
);
