<table class="table table-bordered" id="filesTableComments">
	<tr class="label label-info">
		<th><strong>Comment</strong></th>
	</tr>
</table>
          	
            <script type="text/javascript">
            require([ "sbt/connections/FileService", "sbt/dom" ], function(FileService, dom) {
            	var createRow = function(i) {
            		var table = dom.byId("filesTableComments");
            		var tr = document.createElement("tr");
            		table.appendChild(tr);
            		var td = document.createElement("td");
            		td.setAttribute("id", "comment" + i);
            		tr.appendChild(td);
            	};

            	var fileService = new FileService();
            	fileService.getMyFiles().then(function(files) {
            		if (files.length == 0) {
            			dom.setText("content", "Threre are no files in your library.");
            		} else {
            			var file = files[0];
            			fileService.getMyFileComments(file.getAuthor().authorUserId, file.getFileId()).then(function(comments) {
            				if (comments.length == 0) {
            					dom.setText("content", "There are no comments for this file.");
            				} else {
            					for ( var i = 0; i < comments.length; i++) {
            						var comment = comments[i];
            						createRow(i);
            						dom.setText("comment" + i, comment.getContent() + '\n\n (file: ' + file.getTitle() + ')');
            					}
            				}
            			}, function(error) {
            				console.log(error);
            			});
            		}
            	}, function(error) {
            		dom.setText("content", "Error code:" + error.code + ", message:" + error.message);
            	});

            });
				</script>            