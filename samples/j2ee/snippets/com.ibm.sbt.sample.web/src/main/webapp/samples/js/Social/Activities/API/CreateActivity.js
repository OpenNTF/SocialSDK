require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();
        var activity = activitiesService.newActivity({        	
        	"title" : "Activity Created By JS Snippet "+ new Date(), 
        	"type" : "Activity", 
        	"content" : "Activity Goal Created By JS Snippet "+ new Date(),
        });
        var promise = activitiesService.createActivity(activity);        
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
