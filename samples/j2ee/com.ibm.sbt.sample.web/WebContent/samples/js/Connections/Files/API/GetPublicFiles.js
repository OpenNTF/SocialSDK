require(["sbt/connections/FileService", "sbt/dom", "sbt/json"], 
    function(FileService,dom,json) {
       
        var fileService = new FileService();
    	fileService.getPublicFiles().then(
            function(files) {
            	dom.setText("json", json.jsonBeanStringify(files));
            },
            function(error) {
            	dom.setText("json", json.jsonBeanStringify(error));
            }       
    	);
    }
);