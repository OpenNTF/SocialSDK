require([ "sbt/dom", "sbt/json", "sbt/base/BaseService" ], 
    function(dom,json,BaseService) {
    
        var results = [];
        try {
            var baseService = new BaseService();
            
            results.push({ "pass" : baseService.validateField("foo", "bar") || "pass" });
            results.push({ "fail" : baseService.validateField("foo", "") || "error" });
            results.push({ "fail" : baseService.validateField("foo", undefined) || "error" });
            results.push({ "fail" : baseService.validateField("foo", null) || "error" });
            results.push({ "pass" : baseService.validateFields({ "foo" : "bar", "fee" : "bez" }) || "pass" });
            results.push({ "fail" : baseService.validateFields({ "foo" : "bar", "fee" : "" }) || "error" });
            results.push({ "fail" : baseService.validateFields({ "foo" : "bar", "fee" : undefined }) || "error" });
            results.push({ "fail" : baseService.validateFields({ "foo" : "bar", "fee" : null }) || "error" });
        } catch (error) {
            results.push(error);
        }
        dom.setText("json", json.jsonBeanStringify(results));
    }
);
