***REMOVED***
	extract($args);
	echo $before_widget;
	echo $before_title . 'Files' . $after_title; 
	?>
            <div role="main">
				<table class="table table-bordered" id="filesTable">
					<tr class="label label-info">
						<th>Title</th>
						<th>Id</th>
					</tr>
				</table>
			</div> 
          	
            <script type="text/javascript">
            require(["sbt/connections/FileService", "sbt/dom"], 
            	    function(FileService,dom) {
            		    var createRow = function(file) {
            		        var table = dom.byId("filesTable");
            		        var tr = document.createElement("tr");
            		        table.appendChild(tr);
            		        var td = document.createElement("td");
            		        var a = document.createElement("a");
            		        a.href = file.getDownloadUrl();
            		        a.appendChild(document.createTextNode(file.getTitle()));
            		        td.appendChild(a);
            		        tr.appendChild(td);
            		        td = document.createElement("td");
            		        tr.appendChild(td);
            		        td.appendChild(document.createTextNode(file.getFileId()));	       
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
            	                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            	            }       
            	    	);
            	    }
            	);
				</script>            
        ***REMOVED*** echo $after_widget; ?>