require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	var file = fileService.getFile({
		id : "%{sample.fileId}",
		loadIt : false
	});	
	fileService.addCommentToMyFile(file, {
		comment : "Comment Added from JS Sample!",
		load : function(comment) {			
			var content = "Added Comment: " + comment.getComment();
			dom.setText("content", content);
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
