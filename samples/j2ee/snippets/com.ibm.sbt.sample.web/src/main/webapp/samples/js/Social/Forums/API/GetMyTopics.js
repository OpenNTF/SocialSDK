require([ "sbt/dom", "sbt/json", "sbt/connections/ForumService" ], function(dom,json,ForumService) {
        var forumService = new ForumService();
        var promise = forumService.getMyTopics({
                asc : true,
                page : 1,
                ps : 10
            });
        promise.then(
            function(forums) {
                dom.setText("json", json.jsonBeanStringify(forums));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
