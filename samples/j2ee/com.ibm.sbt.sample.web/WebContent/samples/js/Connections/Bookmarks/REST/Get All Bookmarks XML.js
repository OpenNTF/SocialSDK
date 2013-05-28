require(["sbt/dom", "sbt/config"], function(dom) {
    var endpoint = sbt.Endpoints['connections'];
    
    var url = "/dogear/atom";
    
    var options = { 
        method : "GET", 
        handleAs : "text"
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