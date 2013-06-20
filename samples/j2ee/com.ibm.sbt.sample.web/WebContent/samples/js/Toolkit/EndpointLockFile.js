require([ "sbt/connections/ConnectionsConstants", "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/xpath" ], 
    function(conn,config,lang,dom,json,xml,xpath) {
    
    var endpoint = config.findEndpoint("connections");
    var results = [];
    
    endpoint.request("files/basic/api/myuserlibrary/document/%{sample.fileId}/entry", { method : "GET" }).then(
        function(entry) {
            var document = xml.parse(entry);
            var lockType = xpath.selectText(document, "/a:entry/td:lock/@type", conn.Namespaces);
            results.push({lockType : lockType || "<empty>"});
            dom.setText("json", json.jsonBeanStringify(results));
            
            // lock or unlock the file
            var options = { 
                method : "POST",
                headers : {
                    "X-Update-Nonce" : "{X-Update-Nonce}"
                },
                query : {
                    type : (lockType == "HARD") ? "NONE" : "HARD"
                }
            };
            
            var promise = endpoint.request("files/basic/api/document/%{sample.fileId}/lock", options);
            promise.then(
                function(data) {
                    results.push({data : data || "<empty>"});
                    dom.setText("json", json.jsonBeanStringify(results));
                }, 
                function(error) {
                    results.push(error);
                    dom.setText("json", json.jsonBeanStringify(results));
                }
            );    
            promise.response.then(
                function(response) {
                    results.push(response);
                    dom.setText("json", json.jsonBeanStringify(results));
                }, function(response) {
                    results.push(response);
                    dom.setText("json", json.jsonBeanStringify(results));
                }
            );
        }, 
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );    
    
});
