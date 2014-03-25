require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", 
          "sbt/connections/CommunityConstants", "sbt/base/BaseService", "sbt/base/BaseEntity", "sbt/base/XmlDataHandler" ], 
    function(XmlDataHandler,lang,dom,json,consts,BaseService,BaseEntity,XmlDataHandler) {
    
        var baseService = new BaseService({
            endpoint : "connections"
        });
        
        var results = [];
        try {
            var createEntities = function(service, data, response) {
                results.push({
                    "callback" : "createEntities",
                    "data" : data,
                    "response" : response });
                dom.setText("json", json.jsonBeanStringify(results));

                return new XmlDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.CommunityFeedXPath
                });
            };
            
            var createEntity = function(service, data, response) {
                results.push({
                    "callback" : "createEntity",
                    "data" : data,
                    "response" : response });
                dom.setText("json", json.jsonBeanStringify(results));

                var entryHandler = new XmlDataHandler({
                    data : data, 
                    namespaces : consts.Namespaces, 
                    xpath : consts.CommunityXPath
                });
                return new BaseEntity({
                    id : entryHandler.getEntityId(),
                    service : service, 
                    dataHandler : entryHandler
                });
            };

            var options = {
                //method : "GET",
                handleAs : "text"
            };
                
            var entitiesCallbacks = { 
                createEntities : createEntities,
                createEntity : createEntity
            };
            
            var args = {
                test : "getEntities"
            };
            
            var url = baseService.constructUrl(consts.AtomCommunitiesAll, {}, {authType : ""});

            var promise = baseService.getEntities(url, options, entitiesCallbacks, args);
            promise.then(
                function(response) {
                    results.push({
                        "callback" : "response",
                        "response" : response,
                        "summary" : promise.summary });
                    dom.setText("json", json.jsonBeanStringify(results));
                },
                function(error) {
                    results.push({
                        "callback" : "error",
                        "error" : error });
                    dom.setText("json", json.jsonBeanStringify(results));
                }
            );

        } catch (error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    }
);
