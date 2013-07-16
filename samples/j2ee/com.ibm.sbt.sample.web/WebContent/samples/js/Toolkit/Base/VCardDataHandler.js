require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", 
          "sbt/connections/ProfileConstants", "sbt/base/VCardDataHandler" ], 
    function(XmlDataHandler,lang,dom,json,consts,VCardDataHandler) {
        var domNode = dom.byId("ProfileEntryVCardFull");
        var ProfileEntryVCardFull = domNode.text || domNode.textContent;

        try {
            var dataHandler = new VCardDataHandler({
                data : ProfileEntryVCardFull,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileVCardXPath
            });
            
            var results = {};
            
            var xpath = consts.ProfileVCardXPath;
            for (var i in xpath) {
                try {
                    results[xpath[i]] = dataHandler.getAsString(xpath[i]);
                } catch (error) {
                    results[xpath[i]] = error.message;
                }
            }
            
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
