require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
        var forumService = new ForumService();  
        var forum = forumService.newForum(); 
        var now = new Date();
        forum.setTitle("Test forum " + now.getTime());
        forum.setContent("Test forum created: " + now);
        var promise = forumService.createForum(forum);
        promise.then(
            function(forum) {
               dom.setText("json", json.jsonBeanStringify(forum.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);