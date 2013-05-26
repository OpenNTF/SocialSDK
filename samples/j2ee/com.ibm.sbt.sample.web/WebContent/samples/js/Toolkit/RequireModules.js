function setText(id,text) {
    var node = document.getElementById(id);
    node.innerHTML = text;        
}


var moduleIds = [ "sbt/_bridge/declare", "sbt/_bridge/dom", "sbt/_bridge/i18n", "sbt/_bridge/IFrameTransport", "sbt/_bridge/json", 
                  "sbt/_bridge/lang", "sbt/_bridge/ready", "sbt/_bridge/text", "sbt/_bridge/Transport",
                  "sbt/_bridge/ui/BasicAuth_Dialog", "sbt/_bridge/ui/OAuth10_Dialog",
                  "sbt/Cache", "sbt/compat", "sbt/config", "sbt/declare", "sbt/dom", "sbt/emailService", 
                  "sbt/Endpoint", "sbt/ErrorTransport", 
                  "sbt/i18n", "sbt/itemFactory", "sbt/json", "sbt/Jsonpath", "sbt/lang", "sbt/log", 
                  "sbt/pathUtil", "sbt/Promise", "sbt/Proxy", "sbt/ready", "sbt/stringUtil", "sbt/text",
                  "sbt/util", "sbt/validate", "sbt/xml", "sbt/xpath", "sbt/xsl", 
                  /*"sbt/store/parameter", "sbt/store/AtomStore", "sbt/data/AtomReadStore",*/ 
                  "sbt/base/BaseConstants", "sbt/base/BaseEntity", "sbt/base/DataHandler", "sbt/base/BaseService", 
                  "sbt/connections/ConnectionsConstants", "sbt/base/JsonDataHandler", "sbt/base/XmlDataHandler",
                  "sbt/authenticator/Basic", "sbt/authenticator/OAuth", "sbt/authenticator/SSO"/*,
                  "sbt/widget/_TemplatedWidget", "sbt/widget/grid/_Grid", "sbt/widget/grid/_GridRenderer"*/ ];

var results = "";
for (var i=0; i<moduleIds.length; i++) {
    try {
        require([ moduleIds[i] ], function(module) {
            results += "'" + moduleIds[i] + "' : loaded <br/>";
        });
    } catch (error) {
        results += "<b>'" + moduleIds[i] + "'</b> : " + error.message + " <br/>";
    }
}
setText("content", results);
