require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();        
        var member = activitiesService.newMember({           	
        	"userId" : "%{name=sample.userId2}"
        });
        var activityId = "%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetMyActivities}";
        activitiesService.addMember(activityId, member).then(
            function(activityMember) {
                dom.setText("json", json.jsonBeanStringify(activityMember));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
