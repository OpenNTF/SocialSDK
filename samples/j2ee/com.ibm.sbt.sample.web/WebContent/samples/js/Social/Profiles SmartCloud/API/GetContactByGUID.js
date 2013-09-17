require(["sbt/dom", "sbt/json", "sbt/smartcloud/ProfileService"], 
    function(dom,json,ProfileService) {
	var results = [];  
    try {
        var profileService = new ProfileService();
        var promise = profileService.getContact("%{name=sample.smartcloud.contactGUID|helpSnippetId=Social_Profiles_SmartCloud_Get_Profile}"); 
        promise.then(    
            function(profile){
            	var result = {"Name" : profile.getDisplayName()}; 
                results.push(result);
              	dom.setText("json", json.jsonBeanStringify(results));
              },
              function(error) {
              	results.push(error);
                  dom.setText("json", json.jsonBeanStringify(results));
              }
          );
       } catch(error) {
       	results.push(error);
          dom.setText("json", json.jsonBeanStringify(results));
       }
});
