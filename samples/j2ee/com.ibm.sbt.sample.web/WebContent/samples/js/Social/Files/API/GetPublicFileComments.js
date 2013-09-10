require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.getFile("%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}").then(function(file) {
		fileService.getPublicFileComments(file.getAuthor().authorUserId, file.getId()).then(function(comments) {
			dom.setText("json", json.jsonBeanStringify(comments));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});
});