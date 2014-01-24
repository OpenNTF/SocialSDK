require(["sbt/connections/FileService", "sbt/dom"], 
    function(FileService,dom) {
        var createItem = function(file) {
            var table = dom.byId("filesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            tr.appendChild(td);
            var a = document.createElement("a");
            a.href = file.getDownloadUrl();
            a.appendChild(document.createTextNode(file.getTitle()));
            td.appendChild(a);
        };

        var fileService = new FileService();
        fileService.getMyFiles().then(
            function(files) {
                if (files.length == 0) {
                	dom.setText("filesTable", "You do not own any files.");
                } else {
                    for(var i=0; i<files.length; i++){
                        var file = files[i];
                        createItem(file);
                    }
                }
            },
            function(error) {
                dom.setText("filesTable", error.message);
            }       
    	);
    }
);