require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService,dom) {
        var createRow = function(i) {
            var table = dom.byId("communitiesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.setAttribute("id", "title"+i);
            tr.appendChild(td);
            td = document.createElement("td");
            td.setAttribute("id", "id"+i);
            tr.appendChild(td);
        };

        var communityService = new CommunityService();
    	communityService.getMyCommunities().then(
            function(communities) {
                if (communities.length == 0) {
                    text = "You are not a member of any communities.";
                } else {
                    for(var i=0; i<communities.length; i++){
                        var community = communities[i];
                        createRow(i);
                        dom.setText("title"+i, community.getTitle()); 
                        dom.setText("id"+i, community.getCommunityUuid()); 
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);