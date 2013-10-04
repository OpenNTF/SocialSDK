require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom, json, ActivityService) {
	var activitiesService = new ActivityService();
	var activityNodeId = "%{name=sample.deletedActivityNodeId|helpSnippetId=Social_Activities_API_GetActivityNodeFromTrash}";
	activitiesService.getActivityNodeFromTrash(activityNodeId).then(function(activityNode) {
		dom.setText("json", json.jsonBeanStringify(activityNode));
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});
});
