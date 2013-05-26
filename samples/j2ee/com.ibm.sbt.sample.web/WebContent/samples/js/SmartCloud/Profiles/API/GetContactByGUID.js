require(["sbt/dom", "sbt/json", "sbt/smartcloud/ProfileService"], 
    function(dom,json,ProfileService) {
    var results = null;
    try {
        var profileService = new ProfileService();
        var promise = profileService.getContactByGUID("966797"); // Alan Goodwin's contact id 
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
    return profile.getData();
}