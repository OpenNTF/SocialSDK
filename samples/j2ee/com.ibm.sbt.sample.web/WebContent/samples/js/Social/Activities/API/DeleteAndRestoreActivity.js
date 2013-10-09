require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom, json, ActivityService) {
	var activitiesService = new ActivityService();
	var activityId = "%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetMyActivities}";
	activitiesService.deleteActivity(activityId).then(function(response) {
		return response;
	}).then(function(response) {
		activitiesService.restoreActivity(activityId).then(function(response) {
			dom.setText("json", json.jsonBeanStringify(response));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	});
});
