require(["sbt/Endpoint","sbt/ErrorTransport","sbt/dom","sbt/xml","sbt/xpath","sbt/connections/core"], function(Endpoint,ErrorTransport,dom,xml,xpath,conn) {
    describe("sbt.Endpoint", function() {

        beforeEach(function() {
        });
        
        it("Connections endpoints should be defined", function() {
            var connEndpoint = Endpoint.find("connections");
            expect(connEndpoint).not.toBeUndefined();
            var connOA2Endpoint = Endpoint.find("connectionsOA2");
            expect(connOA2Endpoint).not.toBeUndefined();
        });
    
        it("SmartCloud endpoints should be defined", function() {
            var scldEndpoint = Endpoint.find("smartcloud");
            expect(scldEndpoint).not.toBeUndefined();
            var scldOA2Endpoint = Endpoint.find("smartcloudOA2");
            expect(scldOA2Endpoint).not.toBeUndefined();
        });
        
        it("Invalid endpoint should be defined", function() {
            var endpoint = Endpoint.find("noendpoint");
            expect(endpoint).not.toBeUndefined();
            var transport = endpoint.transport;
            expect(transport).not.toBeUndefined();
            var message = transport._message;
            expect(message).not.toBeUndefined();
            expect(message).toBe("Required endpoint is not available: noendpoint");
        });

        it("Invalid endpoint should be defined", function() {
            var endpoint = Endpoint.find("connectionsNoExist");
            expect(endpoint).not.toBeUndefined();
            var transport = endpoint.transport;
            expect(transport).not.toBeUndefined();
            var message = transport._message;
            expect(message).not.toBeUndefined();
            expect(message).toBe("Required endpoint is not available: connectionsNoExist");
        });

        it("Invalid endpoint should be defined", function() {
            var endpoint = Endpoint.find("connectionsNoClient");
            expect(endpoint).not.toBeUndefined();
            var transport = endpoint.transport;
            expect(transport).not.toBeUndefined();
            var message = transport._message;
            expect(message).not.toBeUndefined();
            expect(message).toBe("Client access disallowed for: connectionsNoClient");
        });

        it("Should be able to read a Connections profile", function() {
            var endpoint = Endpoint.find("connections");
            endpoint.xhrGet({
                serviceUrl : "/profiles/atom/profile.do",
                handleAs : "text",
                content : {
                    email : sbt.Properties["sample.email1"]
                },
                load : function(response) {
                    var doc = xml.parse(response);
                    var name = xpath.selectText(doc,"/a:feed/a:entry/a:contributor/a:name",conn.namespaces);
                    expect(name).toBe(sbt.Properties["sample.displayName1"]);
                },
                error : function(error) {
                    expect(error).toBe(undefined);
                }
            });
        });
        
        it("Should fail gracefully for an undefined endpoint", function() {
            var endpoint = Endpoint.find("noendpoint");
            var transport = endpoint.transport;
            expect(transport instanceof ErrorTransport).toBe(true);
            transport._called = true;
            endpoint.xhrGet({
                serviceUrl : "/profiles/atom/profile.do",
                handleAs : "text",
                content : {
                    email : sbt.Properties["sample.email1"]
                },
                load : function(response) {
                    expect(error).toBe(undefined);
                },
                error : function(error) {
                    expect(error).not.toBe(undefined);
                }
            });
        });
            
        it("Should return a profile when calling with invalid parameters", function() {
            var endpoint = Endpoint.find("connections");
            endpoint.xhrGet({
                serviceUrl : "/profiles/atom/profile.do",
                handleAs : "text",
                content : {
                    email : "invalid@unknown.com"
                },
                load : function(response) {
                    expect(response).not.toBeUndefined();
                },
                error : function(error) {
                    expect(error).toBeUndefined();
                },
                handle : function(data) {
                    expect(data).not.toBeUndefined;
                }
            });
        });
    });
});
