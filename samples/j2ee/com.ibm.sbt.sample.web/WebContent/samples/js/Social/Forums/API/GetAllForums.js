require([ "sbt/dom", "sbt/json", "sbt/connections/ForumService" ], function(dom,json,ForumService) {
        var forumService = new ForumService();
        var promise = forumService.getAllForums({
                asc : true,
                page : 1,
                ps : 2
            });
        promise.then(
            function(activities) {
                dom.setText("json", json.jsonBeanStringify(activities));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
