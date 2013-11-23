require([ "sbt/connections/ForumService", "sbt/connections/ForumConstants", "sbt/dom", "sbt/json" ], function(ForumService,consts,dom,json) {
    try {
        var topicUuid = "%{name=ForumService.topicUuid|helpSnippetId=Social_Forums_Get_My_Forum_Entries}";
        var topicTitle = "%{name=ForumService.topicTitle|required=false}" || "Updated forum topic " + (new Date()).getTime();
        var topicContent = "%{name=ForumService.topicContent|required=false}" || "Updated forum topic content " + (new Date()).getTime();
    	
        var forumService = new ForumService();
        var forumTopic = forumService.newForumTopic();
        forumTopic.setTopicUuid(topicUuid).setTitle(topicTitle).setContent(topicContent);
        var promise = forumService.updateForumTopic(forumTopic);
        promise.then(function(response) {
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        });
    } catch (error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }

});
