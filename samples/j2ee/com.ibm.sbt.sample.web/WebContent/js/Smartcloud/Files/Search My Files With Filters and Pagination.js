require([ "sbt/smartcloud/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	fileService.getMyFilesWithPagination({
		loadIt : true,
		id : "%{sample.smartcloud.fileEntryId}",
		filter : ["cmis:name"],
		maxItems : 2,
		skipCount : 1,
		load : function(files) {
			var content = "";
			for (counter in files) {				
				content = content + files[counter].getId() + " : " + files[counter].getName() + ((counter == files.length - 1) ? "" : " , ");
			}
			if (files.length == 0) {
				content = "No Result Found";
			}
			dom.setText("content", content);
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
});
