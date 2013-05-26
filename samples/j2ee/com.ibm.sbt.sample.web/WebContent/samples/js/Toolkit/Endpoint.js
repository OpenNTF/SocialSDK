require([ "sbt/lang", "sbt/dom", "sbt/json", "sbt/Endpoint" ], 
    function(lang,dom,json,Endpoint) {
        try {
            var endpoint = Endpoint.find("connections");
            dom.setText("json", json.jsonBeanStringify(endpoint));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
