require(["sbt/connections/FileService", "sbt/dom"], 
    function(FileService,dom) {
        var createRow = function(i) {
            var table = dom.byId("filesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.setAttribute("id", "title"+i);
            tr.appendChild(td);
            td = document.createElement("td");
            td.setAttribute("id", "id"+i);
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
                        createRow(i);
                        var aElement = document.createElement("a");
            			aElement.setAttribute("id", "a" + i);
            			aElement.setAttribute("href", file.getDownloadUrl());			            			                       
            			var title = document.getElementById("title"+i);
            			title.appendChild(aElement);
            			dom.setText("a" + i, file.getTitle());
                        dom.setText("id"+i, file.getId()); 
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);