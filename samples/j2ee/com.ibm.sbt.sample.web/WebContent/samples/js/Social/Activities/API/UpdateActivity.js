require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService", "sbt/connections/ActivityConstants" ], function(dom, json, ActivityService,
		ActivityConstants) {
	var activityService = new ActivityService();

	var activityId = "%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetMyActivities}";
	var activity = activityService.newActivity({
		"id" : activityId,
		"title" : "New Title Updated by JS Snippet on " + new Date(),
		"type" : ActivityConstants.ActivityNodeTypes.Activity
	});
	activityService.updateActivity(activity).then(function(activity) {
		dom.setText("json", json.jsonBeanStringify(activity));
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});
}

);
