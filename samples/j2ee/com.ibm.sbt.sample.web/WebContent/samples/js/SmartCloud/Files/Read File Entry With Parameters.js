require([ "sbt/smartcloud/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	fileService.getFileEntry({
		loadIt : true,
		id : "%{sample.smartcloud.fileEntryId}",
		filter : ["cmis:name"],
		load : function(file) {
			var content = "";
			content = content + file.getId() + " : " + file.getName();			
			dom.setText("content", content);
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
