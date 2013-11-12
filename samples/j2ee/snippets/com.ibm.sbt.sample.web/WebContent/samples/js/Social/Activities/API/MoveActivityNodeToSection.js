require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService", "sbt/connections/ActivityConstants" ], function(dom, json, ActivityService,
		ActivityConstants) {
	var activityService = new ActivityService();

	var activityNodeId = "%{name=sample.activityNodeId|helpSnippetId=Social_Activities_API_GetActivityNodes}";

	var activityNode = activityService.newActivityNode({
		"id" : activityNodeId,
		"title" : "Entry Moved to Section by JS Snippet " + new Date(),
	});
	var sectionId = "%{name=sample.sectionId}";
	activityService.moveEntryToSection(activityNode, sectionId).then(
			function(activityNode) {
				dom.setText("json", json.jsonBeanStringify(activityNode));
			}, function(error) {
				dom.setText("json", json.jsonBeanStringify(error));
			});
});
