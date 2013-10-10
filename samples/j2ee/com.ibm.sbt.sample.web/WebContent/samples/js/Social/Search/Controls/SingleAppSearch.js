require(["sbt/dom", "sbt/connections/controls/search/SearchBox","sbt/connections/controls/search/SearchGrid"], function(dom, SearchBox, SearchGrid) {
        
	    var searchBox = new SearchBox({
        	searchSuggest: "on",
        	selectedApplication: "communities",	
        	memberList: true,
        	predefinedSearch: true
        });
                
        dom.byId("searchBox").appendChild(searchBox.domNode);
        
        console.log("!");
});