require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	fileService.getFolder({
		collectionId : "%{sample.folderId}",
		load : function(folder) {
			var content = "";
			content = content + folder.getId() + " : " + folder.getName();
			dom.setText("content", content);
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
