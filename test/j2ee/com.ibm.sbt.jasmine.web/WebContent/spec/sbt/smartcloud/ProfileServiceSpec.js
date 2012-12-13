require(["sbt/smartcloud/ProfileService"], function(ProfileService) {
    describe("sbt.smartcloud.ProfileService", function() {

        beforeEach(function() {
        });
        
        it("Should be defined", function() {
            expect(ProfileService).not.toBeUndefined();
        });
        
        it("Should be able to read a Smartcloud profile", function() {
            var service = new ProfileService();
            service.getProfile({
                load: function(profile){
                    expect(profile.getDisplayName()).toBe("Shane Sloan");
                },
                error: function(error){
                    handled = true;
                    expect(error).toBeUndefined();
                }
            });
        });
        it("Should be able to call getProfile with invalid argument email", function() {
            var email = "abc";
            var service = new ProfileService();
            service.getProfile({
                id: email,
                handle: function(profile){
                	expect(profile).not.toBe(null);
                },
                load: function(profile){
                	expect(profile).not.toBe(null);
                },
                error: function(error){
                    expect(error).toBeUndefined();
                }
            });
        });
        
        it("Should be able to call getProfile with invalid argument loadIt", function() {            
            var service = new ProfileService();
            service.getProfile({
                loadIt: true,
                handle: function(object){
                	expect(profile).not.toBe(null);
                },
                load: function(profile){
                	expect(profile).not.toBe(null);
                },
                error: function(error){
                	expect(error).toBeUndefined();
                }
            });
         });
        
         it("Should be able to call getProfile with invalid argument loadIt", function() {            
             var service = new ProfileService();
             service.getProfile({
                 loadIt: false,
                 handle: function(object){
                 	expect(profile).not.toBe(null);
                 },
                 load: function(profile){
                 	expect(profile).not.toBe(null);
                 },
                 error: function(error){
                	 expect(error).toBeUndefined();
                 }
             });
         });
    });
});
