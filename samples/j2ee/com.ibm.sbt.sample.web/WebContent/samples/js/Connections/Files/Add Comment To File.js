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
			fileService.addCommentToFile(file.getAuthor().authorUserId, file.getId(), "Comment Added from JS Sample").then(function(comment) {
				createRow(0);
				dom.setText("id" + 0, comment.getId());
				dom.setText("comment" + 0, comment.getContent());

			}, function(error) {
				dom.setText("content", "Error code:" + error.code + ", message:" + error.message);
			});

		}
	}, function(error) {
		dom.setText("content", "Error code:" + error.code + ", message:" + error.message);
	});

});