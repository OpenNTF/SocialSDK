require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/base/BaseService" ], 
    function(config,lang,dom,json,BaseService) {
        try {
        	var results = [];
        	
            var baseService = new BaseService({
                endpoint : "connections"
            });
            results.push(baseService);
            
            var endpoint = config.findEndpoint("connections");
            baseService = new BaseService({
                endpoint : endpoint
            });
            results.push(baseService);
            
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
