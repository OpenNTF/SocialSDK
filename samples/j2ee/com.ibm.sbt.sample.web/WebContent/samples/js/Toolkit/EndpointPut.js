require([ "sbt/Endpoint", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/stringUtil" ], 
    function(Endpoint,lang,dom,json,xml,stringUtil) {
    
    var now = new Date();
    var title = "EndpointPost Test " + now.getTime();
    var content = "EndpointPost test content: " + now.getTime();

    var domNode = dom.byId("communityTmpl");
    var CommunityTmpl = stringUtil.trim(domNode.text || domNode.textContent);
    var communityJson = {
        communityType : "public",
        title : title,
        content : content,
        communityUuid : "%{sample.communityId}"
    };
    var putData = stringUtil.transform(CommunityTmpl, communityJson);
    
    var options = { 
        method : "PUT",
        headers : {
            "Content-Type" : "application/atom+xml"
        },
        query : {
            communityUuid : "%{sample.communityId}"
        },
        data : putData
    };
    
    var endpoint = Endpoint.find("connections");
    var promise = endpoint.request("communities/service/atom/community/instance", options);
    
    var results = [];
    
    promise.then(
        function(data) {
            results.push({data : data || "<empty>"});
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
    
    promise.response.then(
        function(response) {
            response = lang.mixin({ 
                Location : response.getHeader("Location")
            }, response);
            results.push(response);
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(response) {
            results.push(response);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );

});
