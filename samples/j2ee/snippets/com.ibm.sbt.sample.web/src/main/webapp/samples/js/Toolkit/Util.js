require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/util" ], 
    function(config,lang,dom,json,util) {
    
    var results = { 
        "https:\/\/localhost:8443\/sbt.sample.web\/service": util.makeAbsoluteUrl("https:\/\/localhost:8443\/sbt.sample.web\/service"), 
        "\/sbt.sample.web\/service": util.makeAbsoluteUrl("\/sbt.sample.web\/service"), 
        "sbt.sample.web\/service": util.makeAbsoluteUrl("sbt.sample.web\/service")
    };
    
    dom.setText("json", json.jsonBeanStringify(results));

});
