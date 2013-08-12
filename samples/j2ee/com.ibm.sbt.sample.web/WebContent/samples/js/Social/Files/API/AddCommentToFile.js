require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();
	
	var date = new Date();

	fileService.getFile("%{sample.fileId}").then(
		function(file) {
			fileService.addCommentToFile(file.getAuthor().authorUserId, file.getId(), "Comment Added from JS Sample on "+date).then(
				function(comment) {
					dom.setText("json", json.jsonBeanStringify(comment));
				},
				function(error) {
					dom.setText("json", json.jsonBeanStringify(error));
				}
			);
		},
		function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		}
	);
});