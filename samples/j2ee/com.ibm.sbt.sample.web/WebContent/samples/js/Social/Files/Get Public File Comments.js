require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
	var createRow = function(i) {
		var table = dom.byId("filesTable");
		var tr = document.createElement("tr");
		table.appendChild(tr);
		var td = document.createElement("td");
		td.setAttribute("id", "id" + i);
		tr.appendChild(td);
		td = document.createElement("td");
		td.setAttribute("id", "comment" + i);
		tr.appendChild(td);
	};

	var fileService = new FileService();

	fileService.getPublicFiles().then(function(files) {
		if (files.length == 0) {
			dom.setText("content", "Threre are no public files");
		} else {
			var file = files[0];
			fileService.getPublicFileComments(file.getAuthor().authorUserId, file.getId()).then(function(comments) {
				if (comments.length == 0) {
					text = "There are no comments for this file.";
				} else {
					for ( var i = 0; i < comments.length; i++) {
						var comment = comments[i];
						createRow(i);
						dom.setText("id" + i, comment.getId());
						dom.setText("comment" + i, comment.getContent());
					}
				}
			}, function(error) {
				dom.setText("content", "Error code:" + error.code + ", message:" + error.message);
			});
		}
	});

});