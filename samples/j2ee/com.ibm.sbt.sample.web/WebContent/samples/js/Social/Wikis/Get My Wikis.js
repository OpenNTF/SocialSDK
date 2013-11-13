require(["sbt/connections/WikiService", "sbt/dom"], 
    function(WikiService,dom) {
        var createRow = function(wiki) {
            var title = wiki.getTitle(); 
            var uuid = wiki.getUuid(); 
            var label = wiki.getLabel(); 
        	
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
        wikiService.getMyWikis({ since : 0 }).then(
            function(wikis) {
                if (wikis.length == 0) {
                    text = "You do not have any wikis.";
                } else {
                    for(var i=0; i<wikis.length; i++){
                        var wiki = wikis[i];
                        createRow(wiki);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);