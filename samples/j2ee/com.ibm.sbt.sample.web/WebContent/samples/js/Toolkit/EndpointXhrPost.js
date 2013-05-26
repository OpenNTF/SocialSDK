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
    };
    var postData = stringUtil.transform(CommunityTmpl, communityJson);
    
    var request = lang.mixin(request, {
        serviceUrl : "/communities/service/atom/communities/my",
        headers :  {
            "Content-Type" : "application/atom+xml"
        },
        postData : postData,
        load : function(response, ioArgs) {
            var result = lang.mixin({response : response || "<empty>"}, ioArgs);
            dom.setText("json", json.jsonBeanStringify(result));
        },
        error : function(error, ioArgs) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    });
    
    var endpoint = Endpoint.find("connections");
    endpoint.xhr("POST", request, true);

});
