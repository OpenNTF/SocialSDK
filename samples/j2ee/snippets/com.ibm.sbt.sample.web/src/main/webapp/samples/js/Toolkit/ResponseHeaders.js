require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/stringUtil" ], 
    function(config,lang,dom,json,xml,stringUtil) {
    console.log('A---------------------------------');
    var now = new Date();
    var title = "EndpointPost Test " + now.getTime();
    var content = "EndpointPost test content: " + now.getTime();
    console.log('B---------------------------------');
    var domNode = dom.byId("communityTmpl");
    var CommunityTmpl = stringUtil.trim(domNode.text || domNode.textContent);
    console.log('C---------------------------------');
    var communityJson = {
        communityType : "public",
        title : title,
        content : content,
    };
    console.log('D---------------------------------');
    var postData = stringUtil.transform(CommunityTmpl, communityJson);
    console.log('E---------------------------------');
    var options = { 
        method : "POST",
        headers : {
            "Content-Type" : "application/atom+xml"
        },
        data : postData
    };
    console.log('F---------------------------------');
    var endpoint = config.findEndpoint("connections");
    var promise = endpoint.request("/communities/service/atom/communities/my", options);
    console.log('G---------------------------------');
    promise.response.then(
        function(response) {
        	console.log('H---------------------------------');
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
            console.log('I---------------------------------');
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(response) {
        	console.log('J---------------------------------');
            dom.setText("json", json.jsonBeanStringify(response));
        }
    );

});
