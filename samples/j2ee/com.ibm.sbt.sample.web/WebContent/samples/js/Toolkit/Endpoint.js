require([ "sbt/lang", "sbt/dom", "sbt/json", "sbt/config" ], 
    function(lang,dom,json,config) {
        try {
            var endpoint = config.findEndpoint("connections");
            dom.setText("json", json.jsonBeanStringify(endpoint));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
