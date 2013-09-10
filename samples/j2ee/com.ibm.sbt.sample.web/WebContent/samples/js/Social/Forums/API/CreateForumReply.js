require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
        var forumService = new ForumService();  
        var topicUuid = "%{name=ForumService.topicUuid}";
        var forumReply = forumService.newForumReply(); 
        forumReply.setTopicUuid(topicUuid);
        var now = new Date();
        forumReply.setTitle("Test forum reply " + now.getTime());
        forumReply.setContent("Test forum reply created: " + now);
        var promise = forumService.createForumReply(forumReply);
        promise.then(
            function(forumReply) {
               dom.setText("json", json.jsonBeanStringify(forumReply.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);