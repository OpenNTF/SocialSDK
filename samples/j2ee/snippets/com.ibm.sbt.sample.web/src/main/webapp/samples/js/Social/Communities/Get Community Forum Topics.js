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
            td.appendChild(dom.createTextNode(title));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(forumUuid));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(communityUuid));
            tr.appendChild(td);
        };

        var communityService = new CommunityService();
        communityService.getForumTopics("%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}").then(
            function(topics) {
                if (topics.length == 0) {
                	dom.setText("content", "Community does not have any forums topics.");
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