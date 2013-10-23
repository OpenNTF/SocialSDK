require(["sbt/dom", 
         "sbt/controls/grid/Grid", 
         "sbt/connections/controls/ConnectionsGridRenderer",
         "sbt/connections/WikiService"], 
    function(dom, Grid, GridRenderer, WikiService) {
		// display My Wikis
		var wikiGrid = createMyWikisGrid(dom, Grid, GridRenderer);
	    
	    // create WikiService
	    var wikiService = new WikiService();
	    addStartWikiBehaviour(wikiGrid, wikiService, dom);
	}
);

function addStartWikiBehaviour(wikiGrid, wikiService, dom) {
	var actionBtn = dom.byId("actionBtn");
    actionBtn.onclick = function(evt) {
    	var title = prompt("Please enter a title for the new wiki", "");
        if (title) {
        	var wiki = wikiService.newWiki(); 
            wiki.setTitle(title);
            var promise = wikiService.createWiki(wiki);
            promise.then(
                function(wiki) {
                	wikiGrid.refresh();
                	alert("Wiki started successfully!");
                },
                function(error) {
                	alert("Error starting wiki: "+error);
                }
            );
        }
    };
}

function createMyWikisGrid(dom, Grid, GridRenderer) {
    var gridRenderer = new GridRenderer({
         template: "<tr><td class='lotusFirstCell'><h4>${title}</h4></td></tr>",
         getValue: function(item, name) {
             if (item.hasOwnProperty(name)) {
                 return item[name];
             } else {
                 return item.getValue(name);
             }
         }
    });
     
    var grid = new Grid({
        storeArgs : {
            url : "/wikis/basic/api/mywikis/feed",
            attributes : { "title" :"a:title" }
        },
        renderer : gridRenderer
    });
     
    dom.byId("gridDiv").appendChild(grid.domNode);
     
    grid.update();
    
    return grid;
}

