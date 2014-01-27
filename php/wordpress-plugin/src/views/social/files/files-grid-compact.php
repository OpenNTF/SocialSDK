		<div role="main">
				<table class="table" id="filesTableCompact">
					<tr class="label label-info">
						<th><strong>File name</strong></th>
					</tr>
				</table>
			</div> 
          	
            <script type="text/javascript">
            require(["sbt/connections/FileService", "sbt/dom"], 
            	    function(FileService,dom) {
            		    var createRow = function(file) {
            		        var table = dom.byId("filesTableCompact");
            		        var tr = document.createElement("tr");
            		        table.appendChild(tr);
            		        var td = document.createElement("td");
            		        var a = document.createElement("a");
            		        a.href = file.getDownloadUrl();
            		        a.appendChild(document.createTextNode(file.getTitle()));
            		        td.appendChild(a);
            		        tr.appendChild(td);
            		    };

            	        var fileService = new FileService();        
            	    	fileService.getMyFiles().then(
            	            function(files) {
            	                if (files.length == 0) {
            	                	 dom.setText("content","You are not an owner of any files.");
            	                } else {
            	                    for(var i=0; i<files.length; i++){
            	                        var file = files[i];
            	                        createRow(file);
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