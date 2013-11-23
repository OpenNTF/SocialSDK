require(["sbt/dom",
         "sbt/connections/FileService",
         "sbt/connections/controls/files/FileGrid"],

function(dom, FileService, FileGrid) {
	var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
	
	var grid = new FileGrid({type : "folders", category : "community", userId : communityId});
	
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
    
    var fileService = new FileService();
	fileService.getMyFiles().then(
		function(files) {
			if (files.length > 0) {
				var file = files[0];
				showFileComments(file.getAuthor().authorUserId, file.getFileId());
			} 
		}, 
		function(error) {
			console.log(error.message);
		}
	); 
 
});

function handleError(dom, error) {
        dom.setText("error", "Error: " + error.message);
}