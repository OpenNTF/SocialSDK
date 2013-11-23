require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/stringUtil",
          "sbt/connections/CommunityConstants", "sbt/base/BaseService", "sbt/base/BaseEntity", "sbt/base/XmlDataHandler" ], 
    function(XmlDataHandler,lang,dom,json,stringUtil,consts,BaseService,BaseEntity,XmlDataHandler) {
    
        var baseService = new BaseService({
            endpoint : "connections"
        });
        
        var results = [];
        try {
            var domNode = dom.byId("postTmpl");
            var PostTmpl = domNode.text || domNode.textContent;
            domNode = dom.byId("putTmpl");
            var PutTmpl = domNode.text || domNode.textContent;
            
            var now = new Date();
            var communityJson = {
                communityType : "public",
                title : "Community Title " + now,
                content : "Community Content"
            };
            var communityPostData = stringUtil.transform(PostTmpl, communityJson);
            communityPostData = stringUtil.trim(communityPostData);
            
            var args = {
                test : "updateEntity",
            };

            var entityCallbacks = {};
            entityCallbacks.createEntity = function(service, data, response) {
                results[0] = {
                    "callback" : "createEntity",
                    "data" : data,
                    "response" : response};
                dom.setText("json", json.jsonBeanStringify(results));

                var url = response.getHeader("Location");
                return baseService.getLocationParameter(response, "communityUuid");
            };
            
            var postOptions = {
                method : "POST",
                headers : consts.AtomXmlHeaders,
                data : communityPostData
            };
            
            // create a new Community
            var url = baseService.constructUrl(consts.AtomCommunitiesMy, {}, {authType : ""});
            var promise = baseService.updateEntity(url, postOptions, entityCallbacks, args);
            promise.then(
                function(data) {
                    results[1] = {
                        "callback" : "updateEntity",
                        "data" : data,
                        "response" : promise.response};
                    dom.setText("json", json.jsonBeanStringify(results));
                    
                    // data for the PUT request to update the community
                    communityJson.communityUuid = data;
                    var communityPutData = stringUtil.transform(PutTmpl, communityJson);
                    communityPutData = stringUtil.trim(communityPutData);
                    
                    var putOptions = {
                        method : "PUT",
                        headers : consts.AtomXmlHeaders,
                        data : communityPutData,
                        query : { "communityUuid" : data }
                    };
                    
                    entityCallbacks.createEntity = function(service, data, response) {
                        results[2] = {
                            "callback" : "createEntity",
                            "data" : data,
                            "response" : response};
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
                    
                    // update the Community we just created
                    var url = baseService.constructUrl(consts.AtomCommunityInstance, {}, {authType : ""});
                    promise = baseService.updateEntity(url, putOptions, entityCallbacks, args);
                    promise.then(
                        function(data) {
                            results[3] = {
                                "callback" : "updateEntity",
                                "data" : data,
                                "response" : promise.response};
                            dom.setText("json", json.jsonBeanStringify(results));
                        },
                        function(error) {
                            results.push(error);
                            dom.setText("json", json.jsonBeanStringify(results));
                        }
                    );
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
