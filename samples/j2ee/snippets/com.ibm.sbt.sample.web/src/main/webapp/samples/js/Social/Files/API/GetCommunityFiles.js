require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {
	var fileService = new FileService();
	var communityId = "%{name=sample.fileCommunityId|helpSnippetId=Social_Communities_Get_My_Communities}";
	fileService.getCommunityFiles(communityId).then(function(files) {
		dom.setText("json", json.jsonBeanStringify(files));
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});
});