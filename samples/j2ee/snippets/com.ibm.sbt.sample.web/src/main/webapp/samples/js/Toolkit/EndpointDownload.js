require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(config,lang,dom,json,xml) {
    
	var fileId = "%{name=FileService.fileId}";
	var libraryId = "%{name=FileService.libraryId}";
	
    var options = { 
        method : "GET"
    };
    
    var endpoint = config.findEndpoint("connections");
    var url = "files/basic/api/library/"+libraryId+"/document/"+fileId+"/media";
    var promise = endpoint.request(url, options);

    var results = [];
    promise.then(
        function(data) {
            results.push({data : data || "<empty>"});
            dom.setText("json", json.jsonBeanStringify(results));
        }, 
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );    
    
});
