require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom, json, ActivityService) {
	var activitiesService = new ActivityService();
	var activityId = "%{name=sample.activityId|helpSnippetId=Social_Activities_API_GetActivity}";
	activitiesService.deleteActivity(activityId).then(function(response) {
		return response;
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	}).then(function(response) {
		var promise = activitiesService.restoreActivity(activityId);
		promise.then(function(response) {
			dom.setText("json", json.jsonBeanStringify(response));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	});
});
