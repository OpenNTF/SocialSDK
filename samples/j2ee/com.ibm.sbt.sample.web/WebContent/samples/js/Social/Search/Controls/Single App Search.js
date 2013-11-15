require(["sbt/dom", "sbt/connections/controls/search/SearchBox","sbt/connections/controls/search/SearchGrid", "sbt/config"], 
	function(dom, SearchBox, SearchGrid, config) {
        
		var endpoint = config.findEndpoint("connections");
		var auth = endpoint.isAuthenticated;   
	
	    var searchBox = new SearchBox({
        	searchSuggest: "on",
        	selectedApplication: "communities",	
        	memberList: true,
        	predefinedSearch: true,
        	searchSuffix : "*",
			constraint: {type:"field",id:"title"},
			searchArgs: {scope:"communities",scope:"personalOnly"}, 
			searchType:"myCommunities"
			//popUpStyle: "width:250px;overflow: hidden;"
        });

        dom.byId("searchBox").appendChild(searchBox.domNode);

});