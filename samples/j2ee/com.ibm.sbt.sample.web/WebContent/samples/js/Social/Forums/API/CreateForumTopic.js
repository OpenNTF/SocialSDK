require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
        var forumService = new ForumService();  
        var forumUuid = "%{name=ForumService.forumUuid}";
        var forumTopic = forumService.newForumTopic(); 
        forumTopic.setForumUuid(forumUuid);
        var now = new Date();
        forumTopic.setTitle("Test forum topic " + now.getTime());
        forumTopic.setContent("Test forum topic created: " + now);
        var promise = forumService.createForumTopic(forumTopic);
        promise.then(
            function(forumTopic) {
               dom.setText("json", json.jsonBeanStringify(forumTopic.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);