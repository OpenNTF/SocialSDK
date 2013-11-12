require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();
        var promise = activitiesService.getActivityNode("%{name=sample.activityNodeId|helpSnippetId=Social_Activities_API_GetActivityNodes}");
        promise.then(
            function(activityNode) {
                dom.setText("json", json.jsonBeanStringify(activityNode));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
