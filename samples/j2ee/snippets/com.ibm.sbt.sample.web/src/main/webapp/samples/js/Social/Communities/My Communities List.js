require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService,dom) {
        var createItem = function(community) {
            var ul = dom.byId("communitiesList");
            var li = document.createElement("li");
            ul.appendChild(li);
            var a = document.createElement("a");
            a.appendChild(document.createTextNode(community.getTitle()));
            li.appendChild(a);
        };

        var communityService = new CommunityService();
    	communityService.getMyCommunities().then(
            function(communities) {
                if (communities.length == 0) {
                	dom.setText("communitiesList", "You are not a member of any communities.");
                } else {
                    for(var i=0; i<communities.length; i++){
                        var community = communities[i];
                        createItem(community);
                    }
                }
            },
            function(error) {
                dom.setText("communitiesList", error.message);
            }       
    	);
    }
);