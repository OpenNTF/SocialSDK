require(["sbt/dom", "sbt/connections/controls/search/SearchBox","sbt/connections/controls/search/SearchGrid"], function(dom, SearchBox, SearchGrid) {
        
	    var searchBox = new SearchBox({
        	type:"full",
        	searchSuggest: "on",
        	application: "communities",	
        	memberList: true	
        });
                
        dom.byId("searchBox").appendChild(searchBox.domNode);
        
        console.log("!");
});