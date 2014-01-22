<div role="main">
	<table class="table table-bordered" id="foldersTable">
		<tr class="label label-info">
			<th><strong>Folder</strong></th>
		</tr>
	</table>
</div>
          	
            <script type="text/javascript">
            require(["sbt/connections/FileService", "sbt/dom"], 
            	    function(FileService,dom) {
            	        var createRow = function(i) {
            	            var table = dom.byId("foldersTable");
            	            var tr = document.createElement("tr");
            	            table.appendChild(tr);
            	            var td = document.createElement("td");
            	            td.setAttribute("id", "title"+i);
            	            tr.appendChild(td);
            	        };

            	        var fileService = new FileService();
            	    	fileService.getMyFolders().then(
            	            function(folders) {
            	                if (folders.length == 0) {
            	                    text = "You are not an owner of any folders.";
            	                } else {
            	                    for(var i=0; i<folders.length; i++){
            	                        var file = folders[i];
            	                        createRow(i);
            	                        dom.setText("title"+i, file.getTitle()); 
            	                        dom.setText("id"+i, file.getFileId()); 
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