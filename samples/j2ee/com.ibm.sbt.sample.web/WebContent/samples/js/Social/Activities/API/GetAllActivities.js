require([ "sbt/dom", "sbt/json", "sbt/connections/ActivitiesService" ], function(dom,json,ActivitiesService) {
        var activitiesService = new ActivitiesService();
        var promise = activitiesService.getAllActivities({
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
