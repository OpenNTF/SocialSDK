require([ "sbt/smartcloud/FileService", "sbt/dom" ], function(FileService, dom) {
	dom.byId("loading").style.visibility = "visible"; 
	var fileService = new FileService();
	fileService.getFileEntry({
		loadIt : true,
		id : "%{sample.smartcloud.fileEntryId}",
		load : function(file) {
			var content = "";
			content = content + file.getId() + " : " + file.getName();			
			dom.setText("content", content);
			dom.byId("loading").style.visibility = "hidden"; 
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
			dom.byId("loading").style.visibility = "hidden"; 
		}
	});
});
