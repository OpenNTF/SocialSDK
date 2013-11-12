require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();

	fileService.updateFileMetadata({
		id : "%{name=sample.fileId|helpSnippetId=Social_Files_Get_My_Files}",
		label : "New Label"+(new Date()).getTime(),
		summary : "New Summary",
		visibility : "public"
	}).then(function(file) {
		dom.setText("json", json.jsonBeanStringify(file));

	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});