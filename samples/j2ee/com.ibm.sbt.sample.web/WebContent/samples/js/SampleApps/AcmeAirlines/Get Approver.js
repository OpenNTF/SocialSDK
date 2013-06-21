require(["sbt/config", "sbt/json", "sbt/dom", "sbt/xml", "sbt/xpath", "sbt/connections/ConnectionsConstants"], 
    function(config, json, dom, xml, xpath, connections) {

    var basicReportingChain = "/profiles/atom/reportingChain.do?userid="; 
    var oauthReportingChain = "/profiles/oauth/atom/reportingChain.do?userid="; 
    
    var approverEmailXPath = "/a:feed/a:entry[2]/a:contributor/a:email";
    var approverUserIdXPath = "/a:feed/a:entry[2]/a:contributor/snx:userid";

    var endpoint = config.findEndpoint('connections');
    
    var reportingChain = null;
    
    var getReportingChain = function(onSuccess, onError) {
        var path = basicReportingChain;
        if (endpoint.authType == 'oauth') {
            path = oauthReportingChain;
        }
        path += "%{sample.id1}";
        
        endpoint.request(path).then(
            function(response) {
            	reportingChain = xml.parse(response);
                if(onSuccess) {
                    onSuccess(reportingChain);
                }
            },
            function(error){
                reportingChain = null;
                if(onError) {
                    onError(error);
                }
            }
        );
    };

    getApproverEmail = function() {
        if (!reportingChain) {
            return null;
        }
        return xpath.selectText(reportingChain, approverEmailXPath, connections.Namespaces);
    };
    
    getApproverUserId = function() {
        if (!reportingChain) {
            return null;
        }
        return xpath.selectText(reportingChain, approverUserIdXPath, connections.Namespaces);
    };

    // demonstrate calling the method
    var onSuccess = function(reportingChain) {
        var approver = { email : getApproverEmail(), userid : getApproverUserId() };
        dom.setText("json", json.jsonBeanStringify(approver));
    };
    var onError = function(error) {
        dom.setText("json", json.jsonBeanStringify(error));
    };
    getReportingChain(onSuccess, onError);
});