require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();
	fileService.getFile("%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}").then(function(file) {
		file.setLabel("New Label"+(new Date()).getTime());
		file.setSummary("New Summary");
		file.setVisibility("public");
		file.update().then(function(file) {
			dom.setText("json", json.jsonBeanStringify(file));

		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});