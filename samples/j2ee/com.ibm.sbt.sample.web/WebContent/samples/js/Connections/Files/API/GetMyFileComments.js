require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();
	fileService.getFile("%{sample.fileId}", true).then(function(file) {
		fileService.getMyFileComments(file.getAuthor().authorUserId, file.getId()).then(function(comments) {
			dom.setText("json", json.jsonBeanStringify(comments));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});