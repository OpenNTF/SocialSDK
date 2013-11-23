require(["sbt/connections/FileService", "sbt/connections/controls/files/FileGrid", "sbt/dom"], function(FileService, FileGrid, dom) {
	
	var showFileComments = function(userId, fileId) {
        var grid = new FileGrid({
             type : "myFileComments",
             userId : userId,
             documentId : fileId
        });
                 
        dom.byId("gridDiv").appendChild(grid.domNode);
                 
        grid.update();
	};
    
    var fileService = new FileService();
	fileService.getMyFiles().then(
		function(files) {
			if (files.length == 0) {
				dom.setText("content", "You are not an owner of any files.");
			} else {
				var file = files[0];
				showFileComments(file.getAuthor().authorUserId, file.getFileId());
			}
		}, 
		function(error) {
			dom.setText("content", "Error reading your files: "+error.message);
		}
	); 
    	
});