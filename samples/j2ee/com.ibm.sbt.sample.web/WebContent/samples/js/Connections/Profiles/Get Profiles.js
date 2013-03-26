require(["sbt/connections/ProfileService", "sbt/dom", "sbt/json"], 
    function(ProfileService, dom, json) {
	
        var emails = [ "%{sample.email1}", "%{sample.email2}" ];
    
        var text = "";
    
        var profileService = new ProfileService();
        for (var i=0; i<emails.length; i++) {
            profileService.getProfile({
                id: emails[i],
                handle: function(response) {
                    text += "\n" + json.jsonBeanStringify(response);
                    dom.setText("json", text);
                }
            });
        }
    }
); 
