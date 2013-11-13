require([ "sbt/lang", "sbt/dom", "sbt/json", "sbt/base/BaseService" ], 
    function(lang,dom,json,BaseService) {
        try {
            var baseService = new BaseService({
                endpoint : "connections"
            });
            dom.setText("json", json.jsonBeanStringify(baseService));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
