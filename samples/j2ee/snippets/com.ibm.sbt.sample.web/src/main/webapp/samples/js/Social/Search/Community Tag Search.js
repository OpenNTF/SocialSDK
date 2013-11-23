require([ "sbt/connections/SearchService", "sbt/dom" ], 
	function(SearchService, dom) {
	
		var searchService = new SearchService();
	
		dom.byId("searchBtn").onclick = function(ev) {
		dom.byId("error").style.display = "none";
		dom.byId("communityTable").style.display = "none";
		dom.byId("searching").appendChild(dom.createTextNode("Searching..."));
		
		var tag = dom.byId("tagInput").value;
		
		var requestArgs = {
			scope : "communities"
		};
		
		searchService.getResultsByTag(tag, requestArgs).then(
			function(results) {
				var searching = dom.byId("searching");
				while(searching.firstChild) searching.removeChild(searching.firstChild);
	            if (results.length == 0) {
	            	showError("No communities associated with tag: " + topic);
                } else {
                    for(var i=0; i<results.length; i++){
                        var result = results[i];
                        createRow(result);
                    }
                    dom.byId("communityTable").style.display = "";
                }
			},
			function(error) {
				dom.byId("searching").appendChild(dom.createTextNode(""));
				showError(error.message);
			}
		);
	};
	
	var showError = function(message) {
		var errorDiv = dom.byId("error");
		errorDiv.style.display = "";
		errorDiv.appendChild(dom.createTextNode(message));
	};
	
	var createRow = function(result) {
        var table = dom.byId("communityTable");
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
