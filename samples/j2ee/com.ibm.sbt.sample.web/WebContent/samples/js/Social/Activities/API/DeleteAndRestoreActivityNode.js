require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom, json, ActivityService) {
	var activitiesService = new ActivityService();
	var activityNodeId = "%{name=sample.activityNodeId|helpSnippetId=Social_Activities_API_GetActivityNode}";
	var promise = activitiesService.deleteActivityNode(activityNodeId);
	promise.then(function() {
		dom.setText("json", json.jsonBeanStringify({
			status : "Success"
		}));
		activitiesService.restoreActivityNode(activityNodeId).then(function() {
			dom.setText("json", json.jsonBeanStringify({
				status : "Success"
			}));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});
});
