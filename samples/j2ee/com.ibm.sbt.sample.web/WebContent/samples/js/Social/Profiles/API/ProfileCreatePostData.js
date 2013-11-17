require(["sbt/dom", "sbt/connections/ProfileService"], 
    function(dom, ProfileService) {
        var profileService = new ProfileService();
        var profile = profileService.newProfile();
        
        // create fields
        profile.setAsString("guid", "guid");
        profile.setAsString("email", "email");
        profile.setAsString("uid", "uid");
        profile.setAsString("distinguishedName", "distinguishedName}");
        profile.setAsString("displayName", "displayName}");
        profile.setAsString("givenNames", "givenNames}");
        profile.setAsString("surname", "surname}");
        profile.setAsString("userState", "userState}");
        
        // update fields
        profile.setAsString("jobTitle", "jobTitle");
        profile.setAsString("streetAddress", "streetAddress");
        profile.setAsString("telephoneNumber", "telephoneNumber");
        profile.setAsString("building", "building");      	
                
        dom.setText("newPost", profile.createPostData());
        profile._update = true;
        dom.setText("newPut", profile.createPostData());
                
	}
);
