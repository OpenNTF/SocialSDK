require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
    	var forumService = new ForumService();	
    	var forumUuid = "%{name=ForumService.forumUuid}";	
        var promise = forumService.deleteForum(forumUuid);
        promise.then(
            function(forumUuid) {
                dom.setText("json", json.jsonBeanStringify({ forumUuid : forumUuid }));
            }, function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
