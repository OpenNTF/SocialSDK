require(["sbt/connections/ForumService", "sbt/dom"], 
    function(ForumService,dom) {
        var createRow = function(forum) {
            var title = forum.getTitle(); 
            var forumUuid = forum.getForumUuid(); 
            var communityUuid = forum.getCommunityUuid(); 
        	
            var table = dom.byId("forumsTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.appendChild(dom.createTextNode(title));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(forumUuid));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(communityUuid || "<none>"));
            tr.appendChild(td);
        };

        var forumService = new ForumService();
        forumService.getMyForums({ since : 0 }).then(
            function(forums) {
                if (forums.length == 0) {
                    text = "You do not have any forums.";
                } else {
                    for(var i=0; i<forums.length; i++){
                        var forum = forums[i];
                        createRow(forum);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);