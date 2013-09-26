require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService", "sbt/connections/ActivityConstants" ], function(dom, json, ActivityService,
		ActivityConstants) {
	var activityService = new ActivityService();

	activityService.moveEntryToSection("%{name=sample.activityNodeId|helpSnippetId=Social_Activities_API_MoveActivityNodeToSection}",
			"%{name=sample.activityId|helpSnippetId=Social_Activities_API_MoveActivityNodeToSection}",
			"%{name=sample.sectionId|helpSnippetId=Social_Activities_API_MoveActivityNodeToSection}", "Entry Moved to Section by JS Snippet").then(
			function(activityNode) {
				dom.setText("json", json.jsonBeanStringify(activityNode));
			}, function(error) {
				dom.setText("json", json.jsonBeanStringify(error));
			});
}

);
