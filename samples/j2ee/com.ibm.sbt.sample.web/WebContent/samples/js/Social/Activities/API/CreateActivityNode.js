require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();
        var activityNode = activitiesService.newActivityNode({
        	"activityId" : "%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetMyActivities}",
        	"title" : "Entry Created By JS Snippet "+ new Date(), 
        	"type" : "Entry", 
        	"content" : "Section Created By JS Snippet "+ new Date(),
        });
        var promise = activitiesService.createActivityNode("%{name=sample.activityId|helpSnippetId=Social_Activities_API_CreateActivityNode}", activityNode);        
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
