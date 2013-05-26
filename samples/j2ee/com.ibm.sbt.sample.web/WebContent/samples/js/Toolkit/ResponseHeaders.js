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
    
    var options = { 
        method : "POST",
        headers : {
            "Content-Type" : "application/atom+xml"
        },
        data : postData
    };
    
    var endpoint = Endpoint.find("connections");
    var promise = endpoint.request("/communities/service/atom/communities/my", options);
    
    promise.response.then(
        function(response) {
            response = lang.mixin({ 
                Location : response.getHeader("Location"),
                Date : response.getHeader("Date"),
                Server : response.getHeader("Server"),
                XLConnAuth : response.getHeader("X-LConn-Auth"),
                XUACompatible : response.getHeader("X-UA-Compatible"),
                LastModified : response.getHeader("Last-Modified"),
                Expires : response.getHeader("Expires"),
                CacheControl : response.getHeader("Cache-Control"),
                Vary : response.getHeader("Vary"),
                ContentEncoding : response.getHeader("Content-Encoding"),
                KeepAlive : response.getHeader("Keep-Alive"),
                Connection : response.getHeader("Connection"),
                ContentLanguage : response.getHeader("Content-Language"),
                ContentType : response.getHeader("Content-Type"),
                TransferEncoding : response.getHeader("Transfer-Encoding")
            }, response);
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(response) {
            dom.setText("json", json.jsonBeanStringify(response));
        }
    );

});
