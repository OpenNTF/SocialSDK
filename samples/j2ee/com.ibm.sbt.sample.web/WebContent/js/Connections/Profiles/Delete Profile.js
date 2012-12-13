function deleteProfile(){
	require(["sbt/connections/ProfileAdminService","sbt/dom"], function(ProfileAdminService,dom) {
	    var profileAdminService = new ProfileAdminService();
	    var profile = profileAdminService.getProfile({
	    	id: 	"QWERAB04-F2E1-1222-4825-7A700026E92C", 
	    	loadIt:  false
	    });
	    profileAdminService.deleteProfile(profile,{    	
	    	load: function(){
	    		dom.setText("load", "Profile deleted");
	    	},
	    	error: function(error){
	    		dom.setText("load","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
	    	}
	    });
	});
}
