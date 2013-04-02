require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	var file = fileService.getFile({
		id : "%{sample.fileId}",
		loadIt : false
	});

	fileService.pinFile(file, {
		load : function(data) {
			dom.setText("content", "File with ID " + file.getId() + " pinned successfuly.");
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});

});
