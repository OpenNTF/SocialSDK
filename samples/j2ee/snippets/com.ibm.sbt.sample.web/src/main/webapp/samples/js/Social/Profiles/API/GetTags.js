require(["sbt/dom", "sbt/json", "sbt/connections/ProfileService"], 
    function(dom,json,ProfileService) {
    var profileId = "%{name=sample.email1}";
    try {
        var profileService = new ProfileService();
        var promise = profileService.getTags(profileId);
        promise.then(    
            function(tags) {
                dom.setText("json", json.jsonBeanStringify(tags));
            },
            function(error) {            	
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch(error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }
});
