require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
        var forumUuid = "%{name=ForumService.forumUuid|helpSnippetId=Social_Forums_Get_My_Forums}";
        var topicTitle = "%{name=ForumService.topicTitle|required=false}" || "Test forum topic " + (new Date()).getTime();
        var topicContent = "%{name=ForumService.topicContent|required=false}" || "Test forum topic content " + (new Date()).getTime();

        var forumService = new ForumService();  
        var forumTopic = forumService.newForumTopic(); 
        forumTopic.setForumUuid(forumUuid);
        forumTopic.setTitle(topicTitle);
        forumTopic.setContent(topicContent);
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