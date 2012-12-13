require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();	
	fileService.getFile({
		id : "%{sample.fileId}",
		loadIt : true,
		load : function(file) {			
			fileService.getFileComments(file, {
				load : function(comments) {
					var content = "";
					for (counter in comments) {
						content = content + comments[counter].getComment() + ((counter == comments.length - 1) ? "" : ", ");
					}
					if (comments.length == 0) {
						content = "No Result Found";
					}
					dom.setText("content", content);
				},
				error : function(error) {
					dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
				}
			});
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
