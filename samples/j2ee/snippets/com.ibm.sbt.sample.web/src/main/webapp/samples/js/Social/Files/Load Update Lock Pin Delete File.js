var currentFile = null;
var isLocked = false;
var isPinned = false;

require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var fileService = new FileService();
	fileService.getMyFiles({
		ps : 1
	}).then(function(files) {
		if (files.length == 0) {
			handleError(dom, "You are not an owner of any files.");
		} else {
			var file = files[0];
			dom.byId("fileId").value = file.getFileId();
			if (file.getLockType() == "HARD") {
				dom.setText("lockUnlock","Unlock");
			}
			handleFileLoaded(file, dom);
			handleLoggedIn(fileService, dom);
		}
	});
});

function loadFile(fileService, dom) {
	currentFile = null;
	var fileId = dom.byId("fileId").value;
	if (fileId) {
		fileService.getFile(fileId, {includeTags : true}).then(function(file) {
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

function lockFile(fileService, id, dom) {
	fileService.lockFile(id).then(function(status) {
		dom.setText("lockUnlock","Unlock");
		displayMessage(dom, "Successfully Locked file: " + id);
		isLocked = true;
	}, function(error) {
		handleError(dom, error);
	});
}

function unlockFile(fileService, id, dom) {
	fileService.unlockFile(id).then(function(status) {
		dom.setText("lockUnlock","Lock");
		displayMessage(dom, "Successfully Unlocked file: " + id);
		isLocked = false;
	}, function(error) {
		handleError(dom, error);
	});
}

function pinFile(fileService, id, dom) {
	fileService.pinFile(id).then(function(status) {
		dom.setText("pinUnPin",  "UnPin");
		displayMessage(dom, "Successfully Pinned file: " + id);
		isPinned = true;
	}, function(error) {
		handleError(dom, error);
		dom.setText("pinUnPin",  "UnPin");
		isPinned = true;
	});
}

function unPinFile(fileService, id, dom) {
	fileService.unpinFile(id).then(function(status) {
		dom.setText("pinUnPin",  "Pin");
		displayMessage(dom, "Successfully removed pin from file: " + id);
		isPinned = false;
	}, function(error) {
		handleError(dom, error);
		dom.setText("pinUnPin",  "Pin");
		isPinned = false;
	});
}

function deleteFile(fileService, id, dom) {
	fileService.deleteFile(id).then(function(status) {
		dom.byId("fileId").value = "";
		dom.byId("label").value = "";
		dom.byId("summary").value = "";
		dom.byId("visibility").value = "";
		displayMessage(dom, "Deleted file: " + id);
		currentFile = null;
	}, function(error) {
		handleError(dom, error);
	});
}

function updateFile(fileService, file, label, summary, visibility, dom) {
	file.setLabel(label);
	file.setSummary(summary);
	file.setVisibility(visibility);
	fileService.updateFileMetadata(file).then(function(file) {
		handleFileUpdated(file, dom);
	}, function(error) {
		handleError(dom, error);
	});
	displayMessage(dom, "Please wait... Updating file: " + file.getFileId());
}

function handleLoggedIn(fileService, dom) {	
	addOnClickHandlers(fileService, dom);	
}

function handleFileLoaded(file, dom) {
	if (!file) {
		dom.byId("fileId").value = "";
		dom.byId("label").value = "";
		dom.byId("summary").value = "";
		dom.byId("visibility").value = "";
		displayMessage(dom, "Unable to load file.");
		return;
	}

	dom.byId("fileId").value = file.getFileId();
	dom.byId("label").value = file.getLabel();
	dom.byId("summary").value = file.getSummary();
	dom.byId("visibility").value = file.getVisibility();
	var tagsStr = "";
	var tags = file.getTags();
	for(var counter in tags){
		tagsStr = tagsStr + tagsStr == "" ? ", " : "" + tags[counter];
	}
	dom.byId("tags").value = tagsStr;
	currentFile = file;

	displayMessage(dom, "Successfully loaded file: " + file.getFileId());
}

function handleFileUpdated(file, dom) {
	displayMessage(dom, "Successfully updated file: " + file.getFileId());
}

function addOnClickHandlers(fileService, dom) {
	dom.byId("loadBtn").onclick = function(evt) {
		loadFile(fileService, dom);
	};

	dom.byId("download").onclick = function(evt) {
		if (currentFile) {
			fileService.downloadFile(currentFile.getFileId(), currentFile.getLibraryId());
		}
	};

	dom.byId("updateBtn").onclick = function(evt) {
		if (currentFile) {
			var label = dom.byId("label");
			var summary = dom.byId("summary");
			var visibility = dom.byId("visibility");
			updateFile(fileService, currentFile, label.value, summary.value, visibility.value, dom);
		}
	};

	dom.byId("lockUnlock").onclick = function(evt) {
		var id = dom.byId("fileId").value;
		if (currentFile) {
			if (currentFile.getLockType() == "HARD" || isLocked) {
				unlockFile(fileService, id, dom);
			} else {
				lockFile(fileService, id, dom);
			}
		}
	};

	dom.byId("pinUnPin").onclick = function(evt) {
		var id = dom.byId("fileId").value;
		if (currentFile) {
			if (isPinned) {
				unPinFile(fileService, id, dom);
			} else {
				pinFile(fileService, id, dom);
			}
		}
	};

	dom.byId("delete").onclick = function(evt) {
		var id = dom.byId("fileId").value;
		if (currentFile) {
			deleteFile(fileService, id, dom);
		}

	};
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
