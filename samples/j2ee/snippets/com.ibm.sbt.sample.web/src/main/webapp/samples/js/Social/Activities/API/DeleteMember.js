require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom, json, ActivityService) {
	var activitiesService = new ActivityService();
	var activityId = "%{name=sample.activityId|helpSnippetId=Social_Activities_API_CreateActivity}";
	var memberId = "%{name=sample.memberId|helpSnippetId=Social_Activities_API_GetActivityMembers}";
	var promise = activitiesService.deleteMember(activityId, memberId);
	promise.then(function(response) {
		dom.setText("json", response);
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});
});
