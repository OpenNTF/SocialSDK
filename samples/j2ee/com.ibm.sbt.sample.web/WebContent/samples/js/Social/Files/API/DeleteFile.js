require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.deleteFile("%{sample.fileId}").then(function(response) {
		dom.setText("json", json.jsonBeanStringify({
			status : "Success"
		}));

	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});