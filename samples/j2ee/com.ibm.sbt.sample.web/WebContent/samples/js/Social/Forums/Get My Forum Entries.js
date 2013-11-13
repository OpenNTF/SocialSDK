require(["sbt/connections/ForumService", "sbt/dom", "sbt/lang"], 
    function(ForumService,dom,lang) {
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
			return (!reply) ? "" : lang.trim(reply.getContent()) + " [uuid=" + reply.getReplyUuid() + "]";
		};
		
        var addForumReply = function(forumReply, postUuid, forumService, ul) {
        	// only display replies to the specified post
        	if (postUuid != forumReply.getReplyToPostUuid()) {
        		return;
        	}
        	
            var li = document.createElement("li");
            li.appendChild(dom.createTextNode(replyToString(forumReply)));
            ul.appendChild(li);
            
            ul = document.createElement("ul");
            li.appendChild(ul);
            
            getForumReplies({ replyUuid: forumReply.getReplyUuid() }, forumService, ul);
        };
        
        var addForumTopic = function(forumTopic, forumService, ul) {
            var li = document.createElement("li");
            li.appendChild(dom.createTextNode(topicToString(forumTopic)));
            ul.appendChild(li);
            
            ul = document.createElement("ul");
            li.appendChild(ul);
            
            getForumReplies({ topicUuid : forumTopic.getTopicUuid() }, forumService, ul);
        };
        
        var addForum = function(forum, forumService) {
        	var ul = dom.byId("forums");
            var li = document.createElement("li");
            li.appendChild(dom.createTextNode(forumToString(forum)));
            ul.appendChild(li);
            
            ul = document.createElement("ul");
            li.appendChild(ul);
            
            getForumTopics(forum, forumService, ul);
        };
        
		var getForumReplies = function(args, forumService, ul) {
            forumService.getForumReplies(args).then(
            	function(replies) {
            		for(var i=0; i<replies.length; i++) {
                        var reply = replies[i];
                        var postUuid = args.topicUuid || args.replyUuid;
                        addForumReply(reply, postUuid, forumService, ul);
                    }
            	},
            	function(error) {
            		displayError(error);
            	}
            );
        };
		
		var getForumTopics = function(forum, forumService, ul) {
            var forumUuid = forum.getForumUuid();
            forumService.getForumTopics(forumUuid).then(
            	function(topics) {
            		for(var i=0; i<topics.length; i++) {
                        var topic = topics[i];
                        addForumTopic(topic, forumService, ul);
                    }
            	},
            	function(error) {
            		displayError(error);
            	}
            );
        };
		
        var displayMessage = function(message) {
        	dom.setText("info", message);
        };

        var displayError = function(error) {
        	dom.setText("error", error.message);
        };

        var forumService = new ForumService();
        forumService.getMyForums().then(
            function(forums) {
                if (forums.length == 0) {
                	displayMessage("You do not have any forums.");
                } else {
                	for (var i in forums) {
                		addForum(forums[i], forumService);
                	}
                }
            },
            function(error) {
            	displayError(error);
            }       
    	);
    }
);