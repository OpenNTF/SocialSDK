require(["sbt/dom", "sbt/json", "sbt/smartcloud/ProfileService"], 
    function(dom,json,ProfileService) {
    var results = null;
    try {
        var profileService = new ProfileService();
        var promise = profileService.getProfile("%{name=sample.smartcloud.subscriberId|helpSnippetId=Social_Profiles_SmartCloud_Get_Profile}");
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
    	"getUserId" : profile.getId(),
        "getDisplayName" : profile.getDisplayName(),
        "getThumbnailUrl" : profile.getThumbnailUrl(),
        "getEmail" : profile.getEmail(),
        "getAddress" : profile.getAddress(),
        "getDepartment" : profile.getDepartment(),
        "getJobTitle" : profile.getJobTitle(),
        "getProfileUrl" : profile.getProfileUrl(),
        "getTelephoneNumber" : profile.getTelephoneNumber(),
        "getCountry" : profile.getCountry(),
        "getOrgId" : profile.getOrgId(),
        "getOrg" : profile.getOrg(),
        "getAbout" : profile.getAbout()
    };
}