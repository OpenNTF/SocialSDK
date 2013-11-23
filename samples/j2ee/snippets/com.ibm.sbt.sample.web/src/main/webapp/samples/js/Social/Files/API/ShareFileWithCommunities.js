require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();
	var fileId = "%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}";
	var communityId = "%{name=sample.fileCommunityId|helpSnippetId=Social_Communities_Get_My_Communities}";

	fileService.shareFileWithCommunities(fileId, [communityId]).then(function(data) {
		dom.setText("json", json.jsonBeanStringify(data));
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});