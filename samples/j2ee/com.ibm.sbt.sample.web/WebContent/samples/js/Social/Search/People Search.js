require([ "sbt/connections/SearchService", "sbt/dom" ], 
	function(SearchService, dom) {
	
		var searchService = new SearchService();
	
		dom.byId("searchBtn").onclick = function(ev) {
		dom.byId("error").style.display = "none";
		dom.byId("peopleTable").style.display = "none";
		dom.byId("searching").appendChild(dom.createTextNode("Searching..."));
		
		var topic = dom.byId("topicInput").value;
		
		searchService.getPeople(topic).then(
			function(facets) {
				var searching = dom.byId("searching");
				while(searching.firstChild) searching.removeChild(searching.firstChild);
	            if (facets.length == 0) {
	            	showError("No people associated with topic: " + topic);
                } else {
                    for(var i=0; i<facets.length; i++){
                        var facet = facets[i];
                        createRow(facet);
                    }
                    dom.byId("peopleTable").style.display = "";
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
	
	var createRow = function(facet) {
        var table = dom.byId("peopleTable");
        var tr = document.createElement("tr");
        table.appendChild(tr);
        var td = document.createElement("td");
        td.appendChild(dom.createTextNode(facet.getLabel()));
        tr.appendChild(td);
        td = document.createElement("td");
        td.appendChild(dom.createTextNode(facet.getId()));
        tr.appendChild(td);
    };
}
);
