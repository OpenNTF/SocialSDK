require(["sbt/dom", 
         "sbt/connections/FileService"], 

      function(dom, FileService) {
	var communityId = "%{name=sample.communityId}";
    
    var createRow = function(i, file) {
        var table = dom.byId("filesTable");
        var tr = document.createElement("tr");
        table.appendChild(tr);
        var td = document.createElement("td");
        td.innerHTML = file.getTitle();
        tr.appendChild(td);
        td = document.createElement("td");
        td.innerHTML = file.getFileId();
        tr.appendChild(td);
    };

    var fileService = new FileService();
	fileService.getCommunityFiles(communityId).then(
        function(files) {
            if (files.length == 0) {
            	dom.setText("content", "No files have been shared with this community.");
            } else {
                for(var i=0; i<files.length; i++){
                    var file = files[i];
                    createRow(i, file); 	
                }
            }
        },
        function(error) {
        	handleError(dom, error);
        }       
	);
});

function handleError(dom, error) {
	dom.setText("error", "Error: " + error.message);
}


