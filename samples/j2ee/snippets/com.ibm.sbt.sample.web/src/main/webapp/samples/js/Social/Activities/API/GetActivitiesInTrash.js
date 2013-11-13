require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();       
        var promise = activitiesService.getActivitiesInTrash({
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
