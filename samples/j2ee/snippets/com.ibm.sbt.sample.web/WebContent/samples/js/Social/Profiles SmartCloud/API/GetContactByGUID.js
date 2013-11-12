require(["sbt/dom", "sbt/json", "sbt/smartcloud/ProfileService"], 
    function(dom,json,ProfileService) {
	try {
        var profileService = new ProfileService();
        var promise = profileService.getContact("%{name=sample.smartcloud.contactGUID|helpSnippetId=Social_Profiles_SmartCloud_Get_Profile}"); 
        promise.then(    
            function(profile){
            	var result = {"Name" : profile.getDisplayName()}; 
              	dom.setText("json", json.jsonBeanStringify(result));
              },
              function(error) {
                  dom.setText("json", json.jsonBeanStringify(error));
              }
          );
       } catch(error) {
          dom.setText("json", json.jsonBeanStringify(error));
       }
});
