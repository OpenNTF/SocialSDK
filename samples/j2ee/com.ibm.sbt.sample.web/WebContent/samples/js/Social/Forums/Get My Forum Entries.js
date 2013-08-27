require(["sbt/connections/ForumService", "sbt/dom"], 
    function(ForumService,dom) {
        var createRow = function(forumTitle, topicTitle, replyTitle) {
            var table = dom.byId("forumsTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.innerHTML = forumTitle;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = topicTitle;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = replyTitle;
            tr.appendChild(td);
        };

        var forumService = new ForumService();
        forumService.getMyForums().then(
            function(forums) {
                if (forums.length == 0) {
                    text = "You do not have any forums.";
                } else {
                    var forum = forums[0];
                    var forumTitle = forum.getTitle(); 
                    createRow(forumTitle, "", "");
                    
                    var forumUuid = forum.getForumUuid();
                    forumService.getForumTopics(forumUuid).then(
                    	function(topics) {
                    		if (topics.length > 0) {
                                var topic = topics[0];
                                var topicTitle = topic.getTitle(); 
                                createRow("", topicTitle, "");
                                
                                var topicUuid = topic.getTopicUuid();
                                forumService.getForumReplies(topicUuid).then(
                                	function(replies) {
                                		for(var i=0; i<replies.length; i++){
                                            var reply = replies[i];
                                            var replyContent = reply.getContent(); 
                                            createRow("", "", replyContent);
                                        }
                                	},
                                	function(error) {
                                		dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
                                	}
                                );
                            }
                    	},
                    	function(error) {
                    		dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
                    	}
                    );
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);