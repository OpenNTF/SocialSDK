require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	dom.byId("loading").style.visibility = "visible";
	var fileService = new FileService();
	fileService.getMyFiles({
		load : function(files) {
			for (counter in files) {
				var liElement = document.createElement("li");
				liElement.setAttribute("id", "li" + counter);
				document.getElementById("content").appendChild(liElement);
				var aElement = document.createElement("a");
				aElement.setAttribute("id", "a" + counter);
				aElement.setAttribute("href", files[counter].getDownloadLink());
				document.getElementById("li" + counter).appendChild(aElement);
				dom.setText("a" + counter, files[counter].getName());
			}
			if (files.length == 0) {
				dom.setText("content", "No Result Found");
			}
			dom.byId("loading").style.visibility = "hidden"; 
		},
		error : function(error) {
			dom.setText("content", "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
			dom.byId("loading").style.visibility = "hidden"; 
		}
	});
});
