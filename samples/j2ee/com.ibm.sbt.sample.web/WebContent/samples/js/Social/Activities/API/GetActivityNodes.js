require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();
        var activityId = "%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetMyActivities}";
        var promise = activitiesService.getActivityNodes(activityId, {
                asc : true,
                page : 1,
                ps : 2
            });
        promise.then(
            function(activityNodes) {
                dom.setText("json", json.jsonBeanStringify(activityNodes));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
