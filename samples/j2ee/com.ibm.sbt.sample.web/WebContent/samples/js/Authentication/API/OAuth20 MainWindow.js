require(["sbt/Endpoint", "sbt/dom", "sbt/config"], function(Endpoint, dom, config) {
    var endpoint = Endpoint.find("connectionsOA2");

    endpoint.authenticate({ forceAuthentication: false }).then(
    	function(response){
            dom.setText("content", "Successfully logged in");    
        },
        function(response){
            dom.setText("content", "Cancelled log in");
        }      
    );
});