require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();
        var activityId = "%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetMyActivities}";
        var promise = activitiesService.getActivityTags(activityId, {
                asc : true,
                page : 1,
                ps : 2
            });
        promise.then(
            function(tags) {
                dom.setText("json", json.jsonBeanStringify(tags));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
