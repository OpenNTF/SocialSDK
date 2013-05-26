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
    	fileService.getFilesSharedByMe().then(
            function(files) {
                if (files.length == 0) {
                    text = "No Files are currently shared by you.";
                } else {
                    for(var i=0; i<files.length; i++){
                        var file = files[i];
                        createRow(i);
                        dom.setText("title"+i, file.getTitle()); 
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