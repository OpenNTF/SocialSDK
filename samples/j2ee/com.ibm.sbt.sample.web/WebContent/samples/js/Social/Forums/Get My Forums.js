require(["sbt/connections/ForumService", "sbt/dom"], 
    function(ForumService,dom) {
        var createRow = function(title, id) {
            var table = dom.byId("forumsTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.innerHTML = title;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = id;
            tr.appendChild(td);
        };

        var forumService = new ForumService();
        forumService.getMyForums().then(
            function(forums) {
                if (forums.length == 0) {
                    text = "You do not have any forums.";
                } else {
                    for(var i=0; i<forums.length; i++){
                        var forum = forums[i];
                        var title = forum.getTitle(); 
                        var id = forum.getId(); 
                        createRow(title, id);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);