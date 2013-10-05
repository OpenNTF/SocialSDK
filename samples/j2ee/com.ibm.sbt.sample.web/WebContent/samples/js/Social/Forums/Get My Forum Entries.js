require(["sbt/connections/ForumService", "sbt/dom"], 
    function(ForumService,dom) {
		var forumToString = function(forum) {
			return (!forum) ? "" : forum.getTitle() + " [uuid=" + forum.getForumUuid() + "]";
		};

		var topicToString = function(topic) {
			return (!topic) ? "" : topic.getTitle() + 
					" [uuid=" + topic.getTopicUuid() + 
					", pinned=" + topic.isPinned() +
					", locked=" + topic.isLocked() +
					", question=" + topic.isQuestion() +
					", answered=" + topic.isAnswered() + "]";
		};

		var replyToString = function(reply) {
			return (!reply) ? "" : reply.getTitle() + " [uuid=" + reply.getReplyUuid() + "]";
		};
		
        var createRow = function(forum, topic, reply) {
            var table = dom.byId("forumsTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.innerHTML = forumToString(forum);
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = topicToString(topic);
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = replyToString(reply);
            tr.appendChild(td);
        };

        var forumService = new ForumService();
        forumService.getMyForums().then(
            function(forums) {
                if (forums.length == 0) {
                    text = "You do not have any forums.";
                } else {
                    var forum = forums[0];
                    createRow(forum, null, null);
                    
                    var forumUuid = forum.getForumUuid();
                    forumService.getForumTopics(forumUuid).then(
                    	function(topics) {
                    		for(var i=0; i<topics.length; i++) {
                                var topic = topics[i];
                                createRow(null, topic, null);
                                
                                var topicUuid = topic.getTopicUuid();
                                forumService.getForumReplies(topicUuid).then(
                                	function(replies) {
                                		for(var i=0; i<replies.length; i++){
                                            var reply = replies[i];
                                            createRow(null, null, reply);
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