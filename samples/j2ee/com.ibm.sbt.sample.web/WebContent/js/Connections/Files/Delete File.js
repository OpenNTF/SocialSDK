require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	var file = fileService.getFile({
		id : "%{sample.deleteFileId}",
		loadIt : false
	});
	fileService.deleteFile(file, {
		load : function(status) {
			var content = "File Id %{sample.deleteFileId} deletion Status: " + status;
			dom.setText("content", content);
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
