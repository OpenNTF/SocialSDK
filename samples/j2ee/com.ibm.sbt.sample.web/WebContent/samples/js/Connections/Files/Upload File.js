function upload() {
	require([ 'sbt/connections/FileService', 'sbt/dom' ], function(FileService, dom) {
		dom.byId("loading").style.visibility = "visible";
		dom.setText('status', '');
		var fileService = new FileService();
		// "your-files" is the ID of the HTML5 File Control. Refer to Upload File.html
		fileService.uploadFile("your-files", {
			// additional paramertes to add file metadata			
			parameters : {
				visibility : "public"
			},
			load : function(file) {
				displayMessage(dom, "File with ID " + file.getId() + " uploaded successfuly");
				dom.byId("fileId").value = file.getId();
				dom.byId("label").value = file.getLabel();
				dom.byId("summary").value = file.getSummary();
				dom.byId("visibility").value = file.getVisibility();				
				dom.byId("loading").style.visibility = "hidden";
			},
			error : function(error) {
				handleError(dom, error);
				dom.byId("loading").style.visibility = "hidden";
			}
		});
	});
}

function displayMessage(dom, msg) {
	dom.setText("success", msg);

	dom.byId("success").style.display = "";
	dom.byId("error").style.display = "none";
}

function handleError(dom, error) {
	dom.setText("error", "Error: " + error.message);

	dom.byId("success").style.display = "none";
	dom.byId("error").style.display = "";
}

function clearError(dom) {
	dom.setText("error", "");

	dom.byId("error").style.display = "none";
}
