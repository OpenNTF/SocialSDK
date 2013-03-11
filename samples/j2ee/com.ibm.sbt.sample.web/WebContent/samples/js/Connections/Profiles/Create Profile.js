function createProfile(){
	require(["sbt/connections/ProfileAdminService","sbt/dom"], function(ProfileAdminService,dom) {
	    var profileAdminService = new ProfileAdminService();
	    var profile = profileAdminService.getProfile({
	    	id: 	"QWERAB04-F2E1-1222-4825-7A700026E92C", 
	    	loadIt:  false
	    });
	    profile.set("guid", "QWERAB04-F2E1-1222-4825-7A700026E92C");
	    profile.set("email", "qwe@renovations.com");
	    profile.set("uid", "qwe");
	    profile.set("distinguishedName", "CN=qwe r,o=renovations");
	    profile.set("displayName", "qwe r");
	    profile.set("givenNames", "qwe");
	    profile.set("surname", "rr");
	    profile.set("userState", "active");
	    profileAdminService.createProfile(profile,{
	    	load: function(profile){
	    		dom.setText("load","Profile created with email " + profile.getEmail());
	    	},
	    	error: function(error){
	    		dom.setText("load","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
	        	}
	        });
	});
}