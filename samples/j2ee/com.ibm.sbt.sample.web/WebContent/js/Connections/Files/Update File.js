require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	var file = fileService.getFile({
		id : "%{sample.fileId}",
		loadIt : false
	});

	file.setLabel("Updated Label1");
	file.setSummary("Updated Summary1");
	file.setVisibility("public");

	fileService.updateFile(file, {
		load : function(file) {
			var content = "Updated File : " + file.getName();
			dom.setText("content", content);
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	}, {});
});
