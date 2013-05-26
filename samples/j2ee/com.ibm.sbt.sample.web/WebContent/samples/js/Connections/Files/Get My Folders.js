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
    	fileService.getMyFolders().then(
            function(folders) {
                if (folders.length == 0) {
                    text = "You are not an owner of any folders.";
                } else {
                    for(var i=0; i<folders.length; i++){
                        var file = folders[i];
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