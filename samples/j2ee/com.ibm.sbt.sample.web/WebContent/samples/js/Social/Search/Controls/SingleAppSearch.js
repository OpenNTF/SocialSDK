require(["sbt/dom", "sbt/connections/controls/search/SearchBox","sbt/connections/controls/search/SearchGrid", "sbt/config"], 
	function(dom, SearchBox, SearchGrid, config) {
        
		var endpoint = config.findEndpoint("connections");
		var auth = endpoint.isAuthenticated;   
	
	    var searchBox = new SearchBox({
        	searchSuggest: "on",
        	selectedApplication: "communities",	
        	memberList: true,
        	predefinedSearch: true,
        	wildcard:true
        });

        dom.byId("searchBox").appendChild(searchBox.domNode);

});