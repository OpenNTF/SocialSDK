require(["sbt/dom", "sbt/json", "sbt/connections/ProfileService"], 
    function(dom,json,ProfileService) {
    var profileId = "%{name=sample.email1}";
    var results = null;
    try {
        var profileService = new ProfileService();
        var promise = profileService.getProfile(profileId, {output: "vcard"});
        promise.then(    
            function(profile){
            	results = getResults(profile);
                dom.setText("json", json.jsonBeanStringify(results));
            },
            function(error){            	
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch(error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }
});

function getResults(profile) {
    return {
        "getUserid" : profile.getUserid(),
        "getEmail" : profile.getEmail(),
        "getName" : profile.getName(),
        "getThumbnailUrl" : profile.getThumbnailUrl(),    
        "getJobTitle" : profile.getJobTitle(),    
        "getDepartment" : profile.getDepartment(),    
        "getTelephoneNumber" : profile.getTelephoneNumber(),    
        "getPronunciationUrl" : profile.getPronunciationUrl()    
    };
}