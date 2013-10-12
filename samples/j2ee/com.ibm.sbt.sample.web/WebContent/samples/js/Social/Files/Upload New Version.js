var currentFile = null;

require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	// To make sure authentication happens before upload
	fileService.endpoint.authenticate().then(function() {
		handleLoggedIn(fileService, dom);
	});

	var fileService = new FileService();
	var uploadNewVersionFileId = "%{name=sample.uploadNewVersionFileId|helpSnippetId=Social_Files_Get_My_Files}";
	if (!uploadNewVersionFileId) {
		fileService.getMyFiles({
			ps : 1,
			includeTags : true
		}).then(function(files) {
			if (files.length == 0) {
				handleError(dom, "You are not an owner of any files.");
			} else {
				var file = files[0];
				dom.byId("fileId").value = file.getFileId();
				handleFileLoaded(file, dom);
				handleLoggedIn(fileService, dom);
			}
		});
	} else {
		fileService.getFile(uploadNewVersionFileId, {
			includeTags : true
		}).then(function(file) {
			dom.byId("fileId").value = file.getFileId();
			handleFileLoaded(file, dom);
			handleLoggedIn(fileService, dom);
		}, function(error) {
			handleError(dom, error);
		});
	}
});

function handleFileLoaded(file, dom) {
	if (!file) {
		dom.byId("fileId").value = "";
		dom.byId("label").value = "";
		dom.byId("summary").value = "";
		dom.byId("visibility").value = "";
		dom.byId("tags").value = "";
		dom.byId("removeTags").value = "";
		dom.byId("shareWith").value = "";
		dom.byId("shrePermissions").value = "";
		displayMessage(dom, "Unable to load file.");
		return;
	}

	dom.byId("fileId").value = file.getFileId();
	dom.byId("label").value = file.getLabel();
	dom.byId("summary").value = file.getSummary();
	dom.byId("visibility").value = file.getVisibility();

	var tagsStr = "";
	var tags = file.getTags();
	for ( var counter in tags) {		
		tagsStr += (tagsStr == "" ? "" : ", ");
		tagsStr += tags[counter];
	}
	dom.byId("tags").value = tagsStr;
	dom.byId("removeTags").value = "";
	dom.byId("shareWith").value = "";
	dom.byId("sharePermission").value = "";

	currentFile = file;

	displayMessage(dom, "Successfully loaded file: " + file.getFileId());
}

function addOnClickHandlers(fileService, dom) {

	dom.byId("loadBtn").onclick = function(evt) {
		loadFile(fileService, dom);
	};

	dom.byId("uploadBtn").onclick = function(evt) {
		uploadNewVersion(fileService, dom);
	};

	dom.byId("updateBtn").onclick = function(evt) {
		updateFile(fileService, dom);
	};
}

function handleLoggedIn(fileService, dom) {
	addOnClickHandlers(fileService, dom);
}

function loadFile(fileService, dom) {
	currentFile = null;
	var fileId = dom.byId("fileId").value;
	if (fileId) {
		fileService.getFile(fileId, {
			includeTags : true
		}).then(function(file) {
			handleFileLoaded(file, dom);
		}, function(error) {
			handleError(dom, error);
		});
	} else {
		fileService.getMyFiles({
			ps : 1,
			includeTags : true
		}).then(function(files) {
			var file = (!files || files.length == 0) ? null : files[0];
			handleFileLoaded(file, dom);
		}, function(error) {
			handleError(dom, error);
		});
	}
}

function uploadNewVersion(fileService, dom) {

	if (currentFile) {
		dom.byId("loading").style.visibility = "visible";
		dom.setText('status', '');
		// "your-files" is the ID of the HTML5 File Control. Refer to Upload File.html
		fileService.uploadNewVersion(currentFile.getFileId(), "your-files").then(function(file) {
			displayMessage(dom, "File with ID " + file.getFileId() + "'s new version uploaded successfuly");
			dom.byId("fileId").value = file.getFileId();
			dom.byId("label").value = file.getLabel();
			dom.byId("summary").value = file.getSummary();
			dom.byId("visibility").value = file.getVisibility();
			var tagsStr = "";
			var tags = file.getTags();
			for ( var counter in tags) {
				tagsStr = tagsStr + tagsStr == "" ? ", " : "" + tags[counter];
			}
			dom.byId("tags").value = tagsStr;
			dom.byId("loading").style.visibility = "hidden";
		}, function(error) {
			handleError(dom, error);
			dom.byId("loading").style.visibility = "hidden";
		});
	}
}

function trim(s) {
	return s ? s.replace(/^\s+|\s+$/g, "") : s;
}

function updateFile(fileService, dom) {
	if (currentFile) {
		dom.byId("loading").style.visibility = "visible";
		dom.setText('status', '');
		var params = {};
		var visibility = dom.byId("visibility").value;
		if (visibility && trim(visibility) != "") {
			params.visibility = visibility;
		}
		var shareWith = dom.byId("shareWith").value;
		if (shareWith && trim(shareWith) != "") {
			params.shareWith = shareWith;
		}
		var sharePermission = dom.byId("sharePermission").value;
		if (sharePermission && trim(sharePermission) != "") {
			params.sharePermission = sharePermission;
		}
		var tags = dom.byId("tags").value;
		if (tags && trim(tags) != "") {
			params.tags = tags;
		}
		var removeTags = dom.byId("removeTags").value;
		if (removeTags && trim(removeTags) != "") {
			params.removeTags = removeTags;
		}
		fileService.updateFile(currentFile.getFileId(), params).then(function(file) {
			displayMessage(dom, "File with ID " + file.getFileId() + " updated successfuly");
			dom.byId("fileId").value = file.getFileId();
			dom.byId("label").value = file.getLabel();
			dom.byId("summary").value = file.getSummary();
			dom.byId("visibility").value = file.getVisibility();
			var tagsStr = "";
			var tags = file.getTags();
			for ( var counter in tags) {
				tagsStr = tagsStr + tagsStr == "" ? ", " : "" + tags[counter];
			}
			dom.byId("tags").value = tagsStr;
			dom.byId("loading").style.visibility = "hidden";
		}, function(error) {
			handleError(dom, error);
			dom.byId("loading").style.visibility = "hidden";
		});
	}

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
