function addOnClickHandlers(fileService, dom) {

	dom.byId("uploadBtn").onclick = function(evt) {
		uploadFile(fileService, dom);
	};
}

function handleLoggedIn(fileService, dom) {
	addOnClickHandlers(fileService, dom);
}

function uploadFile(fileService, dom) {

	dom.byId("loading").style.visibility = "visible";
	dom.setText('status', '');

	// "your-files" is the ID of the HTML5 File Control. Refer to Upload File.html
	fileService.uploadFile("your-files", {
		// additional paramertes to add file metadata			
		visibility : "public"
	}).then(function(file) {
		displayMessage(dom, "File with ID " + file.getFileId() + " uploaded successfuly");
		dom.byId("fileId").value = file.getFileId();
		dom.byId("label").value = file.getLabel();
		dom.byId("summary").value = file.getSummary();
		dom.byId("visibility").value = file.getVisibility();
		dom.byId("loading").style.visibility = "hidden";
	}, function(error) {
		handleError(dom, error);
		dom.byId("loading").style.visibility = "hidden";
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

require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	// To make sure authentication happens before upload 
	fileService.endpoint.authenticate().then(function() {
		handleLoggedIn(fileService, dom);
	});
});

