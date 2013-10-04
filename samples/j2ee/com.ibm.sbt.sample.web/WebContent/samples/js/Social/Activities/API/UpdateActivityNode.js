require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService", "sbt/connections/ActivityConstants" ], function(dom, json, ActivityService,
		ActivityConstants) {
	var activityService = new ActivityService();

	var activityNode = activityService.newActivityNode({    	
    	"title" : "New Title Updated by JS Snippet on "+ new Date()
    });	
	activityService.updateActivityNode("%{name=sample.activityNodeId|helpSnippetId=Social_Activities_API_UpdateActivityNode}", activityNode).then(
			function(activityNode) {
				dom.setText("json", json.jsonBeanStringify(activityNode));
			}, function(error) {
				dom.setText("json", json.jsonBeanStringify(error));
			});
}

);
