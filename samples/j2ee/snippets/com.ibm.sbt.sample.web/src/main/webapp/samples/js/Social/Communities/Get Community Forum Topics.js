require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService,dom) {
        var createRow = function(topic) {
            var title = topic.getTitle(); 
            var forumUuid = topic.getForumUuid(); 
            var communityUuid = topic.getCommunityUuid(); 
        	
            var table = dom.byId("forumsTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.innerHTML = title;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = forumUuid;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = communityUuid;
            tr.appendChild(td);
        };

        var communityService = new CommunityService();
        communityService.getForumTopics("%{sample.communityId}").then(
            function(topics) {
                if (topics.length == 0) {
                    text = "Community does not have any forums topics.";
                } else {
                    for(var i=0; i<topics.length; i++){
                        var topic = topics[i];
                        createRow(topic);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);