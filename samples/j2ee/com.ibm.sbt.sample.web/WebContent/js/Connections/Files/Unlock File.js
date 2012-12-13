require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	var file = fileService.getFile({
		id : "%{sample.fileId}",
		loadIt : false
	});
	fileService.unlockFile(file, {
		load : function(status) {				
			var content = "Unlocked File Id %{sample.fileId} successfuly!";			
			dom.setText("content", content);
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
