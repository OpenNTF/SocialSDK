require([ "sbt/dom", "sbt/config" ], function(dom, config) {
    config.Properties["loginUi"] = "dialog";
    
    var endpoint = config.findEndpoint("connections");
    
    endpoint.request("file/basic/api/myuserlibrary/feed?includeQuota=true").then(
        function(response) {
        	dom.setText("content", response);
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});