require(["sbt/connections/WikiService", "sbt/dom"], 
    function(WikiService,dom) {
        var createRow = function(wikiPage) {
            var title = wikiPage.getTitle(); 
            var uuid = wikiPage.getUuid(); 
            var label = wikiPage.getLabel(); 
        	
            var table = dom.byId("wikisTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.appendChild(dom.createTextNode(title));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(uuid));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(label));
            tr.appendChild(td);
        };

        var wikiService = new WikiService();
        var wikiLabel = "%{name=WikiService.wikiLabel|helpSnippetId=Social_Wikis_Get_My_Wikis}";
        wikiService.getMyWikiPages(wikiLabel).then(
            function(wikiPages) {
                if (wikiPages.length == 0) {
                    text = "You do not have any wiki pages.";
                } else {
                    for(var i=0; i<wikiPages.length; i++){
                        var wikiPage = wikiPages[i];
                        createRow(wikiPage);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);