require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.unpinFile("%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}").then(function(fileId) {

		dom.setText("json", json.jsonBeanStringify({
			fileId : fileId
		}));
		;

	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});