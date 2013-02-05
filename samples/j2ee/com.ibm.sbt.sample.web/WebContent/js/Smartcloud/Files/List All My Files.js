require([ "sbt/smartcloud/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	fileService.getMyFiles({
		load : function(files) {
			var content = "";
			for (counter in files) {
				var userProfile = files[counter].getCreatedBy();
				content = content + files[counter].getId() + " : " + files[counter].getName() + " : " + userProfile.getName() +((counter == files.length - 1) ? "" : " , ");
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
