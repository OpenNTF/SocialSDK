require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();
	fileService.getMyFolders().then(function(folders) {
		dom.setText("json", json.jsonBeanStringify(folders));
	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});
});