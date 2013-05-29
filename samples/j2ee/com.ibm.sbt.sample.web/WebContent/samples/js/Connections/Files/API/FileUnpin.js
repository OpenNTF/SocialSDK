require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.getFile("%{sample.fileId}").then(function(file) {
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