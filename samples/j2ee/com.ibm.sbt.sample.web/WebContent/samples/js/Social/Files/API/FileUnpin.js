require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.getFile("%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}").then(function(file) {
		file.unpin().then(function(fileId) {

			dom.setText("json", json.jsonBeanStringify({
				fileId : fileId
			}));
			;

		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});