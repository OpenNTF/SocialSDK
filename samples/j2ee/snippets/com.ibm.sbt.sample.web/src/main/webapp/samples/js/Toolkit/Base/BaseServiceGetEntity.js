require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", 
          "sbt/connections/CommunityConstants", "sbt/base/BaseService", "sbt/base/BaseEntity", "sbt/base/XmlDataHandler" ], 
    function(XmlDataHandler,lang,dom,json,consts,BaseService,BaseEntity,XmlDataHandler) {
    
        var baseService = new BaseService({
            endpoint : "connections"
        });
        
        var results = [];
        try {
            var createEntity = function(service, data, response) {
                results.push({
                    "callback" : "createEntity",
                    "data" : data,
                    "response" : response});
                dom.setText("json", json.jsonBeanStringify(results));

                var entryHandler = new XmlDataHandler({
                    data : data, 
                    namespaces : consts.Namespaces, 
                    xpath : consts.CommunityXPath
                });
                return new BaseEntity({
                    id : entryHandler.getEntityId(),
                    service : baseService, 
                    dataHandler : entryHandler
                });
            };

            var args = {
                test : "getEntity",
            };

            var entityCallbacks = { 
                createEntity : createEntity
            };
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : { communityUuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}" }
            };
            var url = baseService.constructUrl(consts.AtomCommunityInstance, {}, {authType : ""});
            var promise = baseService.getEntity(url, options, "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}", entityCallbacks, args);
            promise.then(
                function(response) {
                    results.push(response);
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
