require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();
        var promise = activitiesService.getActivity("%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetMyActivities}");
        promise.then(
            function(activity) {
                dom.setText("json", json.jsonBeanStringify(activity));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
