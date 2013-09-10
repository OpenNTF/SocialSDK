require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.deleteFile("%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}").then(function(response) {
		dom.setText("json", json.jsonBeanStringify({
			status : "Success"
		}));

	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});