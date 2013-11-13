require([ "sbt/connections/FileService", "sbt/connections/CommunityService", "sbt/dom", "sbt/json" ], function(FileService, CommunityService, dom, json) {

	var fileService = new FileService();

	var date = new Date();

	var fileId = "%{name=sample.communityFileId|helpSnippetId=Social_Files_Get_Community_Files}";
	var communityId = "%{name=sample.fileCommunityId|helpSnippetId=Social_Communities_Get_My_Communities}";

	if (fileId && communityId) {
		fileService.addCommentToCommunityFile(fileId, "Comment Added to Community File from JS Sample on " + date, communityId).then(function(comment) {
			dom.setText("json", json.jsonBeanStringify(comment));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	}
});