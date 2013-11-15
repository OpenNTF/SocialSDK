require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", 
          "sbt/connections/CommunityConstants", "sbt/base/BaseService", "sbt/base/XmlDataHandler" ], 
    function(XmlDataHandler,lang,dom,json,consts,BaseService,XmlDataHandler) {
    
        var baseService = new BaseService({
            endpoint : "connections"
        });
        
        var results = [];
        try {
            var args = {
                test : "deleteEntity",
            };
            
            var options = {
                method : "DELETE",
                handleAs : "text",
                query : { communityUuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}" }
            };
            
            var url = baseService.constructUrl(consts.AtomCommunityInstance, {}, {authType : ""});
            var promise = baseService.deleteEntity(url, options, "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}", args);
            promise.then(
                function(data) {
                    results.push({
                        "callback" : "deleteEntity",
                        "data" : data,
                        "response" : promise.response});
                    dom.setText("json", json.jsonBeanStringify(results));
                },
                function(error) {
                    results.push(error);
                    dom.setText("json", json.jsonBeanStringify(results));
                }
            );
        } catch (error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    }
);
