			<table class="table table-bordered" id="filesTableSharedByMe">
				<tr class="label label-info">
					<th><strong>Files shared by me:</strong></th>
				</tr>
			</table>
          	
            <script type="text/javascript">
            require(["sbt/connections/FileService", "sbt/dom"], 
            	    function(FileService,dom) {
            	        var createRow = function(i) {
            	            var table = dom.byId("filesTableSharedByMe");
            	            var tr = document.createElement("tr");
            	            table.appendChild(tr);
            	            var td = document.createElement("td");
            	            td.setAttribute("id", "title"+i);
            	            tr.appendChild(td);
            	        };

            	        var fileService = new FileService();
            	    	fileService.getFilesSharedWithMe().then(
            	            function(files) {
            	                if (files.length == 0) {
            	                    text = "No Files are currently shared with you.";
            	                } else {
            	                    for(var i=0; i<files.length; i++){
            	                        var file = files[i];
            	                        createRow(i);
            	                        dom.setText("title"+i, file.getTitle()); 
            	                    }
            	                }
            	            },
            	            function(error) {
            	            	console.log(error);
            	            }       
            	    	);
            	    }
            	);
				</script>            