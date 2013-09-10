require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.addCommentToMyFile("%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}", "Comment Added from JS Sample").then(function(comment) {

		dom.setText("json", json.jsonBeanStringify(comment));

	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});