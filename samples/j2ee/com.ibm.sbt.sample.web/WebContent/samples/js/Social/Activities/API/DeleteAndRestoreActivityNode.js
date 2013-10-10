require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom, json, ActivityService) {
	var activitiesService = new ActivityService();
	var activityNodeId = "%{name=sample.activityNodeId|helpSnippetId=Social_Activities_API_GetActivityNodes}";
	activitiesService.deleteActivityNode(activityNodeId).then(function(response) {
		return response;
	}).then(function(response) {
		activitiesService.restoreActivityNode(activityNodeId).then(function(response) {
			dom.setText("json", json.jsonBeanStringify(response));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	});
});
