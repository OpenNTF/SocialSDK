require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
        var forumService = new ForumService();  
    	var forumUuid = "%{ForumService.forumUuid}";	
        var promise = forumService.getForum(forumUuid);
        promise.then(
            function(forum) {
               dom.setText("json", json.jsonBeanStringify(forum));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);