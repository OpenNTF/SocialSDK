require(["sbt/config","sbt/dom"], function(sbt, dom) {
    var endpoint = sbt.Endpoints['connections'];
    
    var url = "/activities/service/atom2/activities";
    
    var options = { 
        method : "GET", 
        handleAs : "text",
        query : {
            email : "%{sample.email1}"
        }
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