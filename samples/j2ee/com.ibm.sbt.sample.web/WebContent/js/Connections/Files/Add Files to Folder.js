require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	fileService.addFilesToFolder({
		collectionId : "%{sample.folderId}",
		fileIds : "%{sample.fileId}",
		load : function(status) {			
			dom.setText("content", "Success");
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
