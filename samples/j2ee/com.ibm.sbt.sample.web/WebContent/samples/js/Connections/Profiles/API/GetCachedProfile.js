var theProfile = null;
var count = 0;
var results = [];

function getTheProfile(profileService, dom, json, recurse) {
    var profileId = "%{sample.email1}";
    profileService.getProfile(profileId).then(    
        function(profile) {
            if (!theProfile) {
                profile.hash = "foobar";
                theProfile = profile;
                count++;
                results.push({ profileLoaded : count });
                dom.setText("json", json.jsonBeanStringify(results));
            } else {
                if (!profile.hash) {
                    results.push({ code : 1 , message : "New Profile instance was returned."});
                } else {
                    count++;
                    results.push({ profileLoaded : count });
                    dom.setText("json", json.jsonBeanStringify(results));
                }
            }
            if (recurse) {
                getTheProfile(profileService, dom, json, false);
            }
        },
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
}

require(["sbt/dom", "sbt/json", "sbt/connections/ProfileService"], 
    function(dom,json,ProfileService) {
    var profileService = new ProfileService();
    for (var i=0; i<10; i++) {
        getTheProfile(profileService, dom, json, true);
    }
});
