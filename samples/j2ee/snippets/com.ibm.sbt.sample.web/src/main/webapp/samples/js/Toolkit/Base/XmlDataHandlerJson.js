require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", 
          "sbt/connections/ProfileConstants", "sbt/base/XmlDataHandler" ], 
    function(XmlDataHandler,lang,dom,json,consts,VCardDataHandler) {
        var domNode = dom.byId("ProfileEntry");
        var ProfileEntry = domNode.text || domNode.textContent;

        try {
            var dataHandler = new XmlDataHandler({
                data : ProfileEntry,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileXPath
            });
            
            dom.setText("json", json.jsonBeanStringify(dataHandler.toJson()));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
