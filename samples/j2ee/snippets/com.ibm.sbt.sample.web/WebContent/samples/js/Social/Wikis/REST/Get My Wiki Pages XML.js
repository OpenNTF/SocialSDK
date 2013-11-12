require([ "sbt/config","sbt/dom" ], function(config,dom) {
	var endpoint = config.findEndpoint("connections");
    
	var wikiLabel = "%{name=WikiService.wikiLabel|helpSnippetId=Social_Wikis_Get_My_Wikis}";
    var url = "/wikis/basic/api/wiki/" + encodeURIComponent(wikiLabel) + "/mypages";
    
    var options = { 
        method : "GET", 
        handleAs : "text",
        query : { includeTags:true, acls:true }
    };
    
    endpoint.request(url, options).then(
    	function(response) {
    		dom.setText("content", response);
        },
        function(error){
            dom.setText("content", error);
        }
    );
});