require([ "sbt/lang", "sbt/dom", "sbt/json", "sbt/config" ], 
    function(lang,dom,json,config) {
        try {
            dom.setText("json", json.jsonBeanStringify(config));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
