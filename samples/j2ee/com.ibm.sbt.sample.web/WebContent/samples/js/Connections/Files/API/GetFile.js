require(["sbt/connections/FileService", "sbt/dom","sbt/json"], 
    function(FileService,dom,json) {        
        var fileService = new FileService();
    	fileService.getFile("%{sample.fileId}").then(
            function(file) {
            	dom.setText("json", json.jsonBeanStringify(file));
            },
            function(error) {
            	dom.setText("json", json.jsonBeanStringify(error));
            }       
    	);
    }
);