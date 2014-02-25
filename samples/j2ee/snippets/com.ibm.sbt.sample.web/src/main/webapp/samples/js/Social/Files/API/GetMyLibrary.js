require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {
	var fileService = new FileService();
	fileService.getMyLibrary().then(
		function(library) {
			dom.setText("json", json.jsonBeanStringify(library));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		}
	);
});