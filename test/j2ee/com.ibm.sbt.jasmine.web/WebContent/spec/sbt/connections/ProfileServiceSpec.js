require(["sbt/connections/ProfileService"], function(ProfileService) {
    describe("sbt.connections.ProfileService", function() {

        beforeEach(function() {
        });
        
        it("Should be defined", function() {
            expect(ProfileService).not.toBeUndefined();
        });
    
        it("Should be able to read a Connections profile", function() {
            var email = sbt.Properties["sample.email1"];
            var service = new ProfileService();
            service.getProfile({
                id: email,
                loaded: true,
                handle: function(profile){
                    expect(profile.getDisplayName()).toBe(sbt.Properties["sample.displayName1"]);
                },
                error: function(error){
                    handled = true;
                    expect(error).toBeUndefined();
                }
            });
        });
        it("Should be able to call getProfile with invalid email", function() {
            var email = "abc";
            var service = new ProfileService();
            service.getProfile({
                id: email,
                loadIt: true,
                handle: function(profile){
                	expect(profile).not.toBe(null);
                    expect(profile.getDisplayName()).toBe(null);
                },
                load: function(profile){
                	expect(profile).not.toBe(null);
                    expect(profile.getDisplayName()).toBe(null);
                },
                error: function(error){
                    expect(error).toBeUndefined();
                }
            });
        });
        it("Should be able to call getProfile with no email parameter", function() {            
            var service = new ProfileService();
            service.getProfile({
                loadIt: true,
                handle: function(object){
                    expect(object.code).toEqual(400);
                },
                load: function(profile){
                    expect(profile.getDisplayName()).toBe(null);
                },
                error: function(error){
                    expect(error.code).toEqual(400);
                }
            });
        });
        
        it("Should be able to call getProfile method of profileService object with no email parameter", function() {            
        	var service = new ProfileService();
        	var profile = service.getProfile({loadIt:false});
        	expect(profile).toBeUndefined();
        });
        
        it("Should be able to call getProfile method of profileService object with no email parameter #2", function() {            
        	var service = new ProfileService();
        	var profile = service.getProfile();
        	expect(profile).toBeUndefined();
        });
        
        it("Should be able to call load method of profile object with email parameter", function() {  
        	var email = sbt.Properties["sample.email1"];
        	var service = new ProfileService();
        	var profile = service.getProfile({loadIt:false, email:email});
        	expect(profile).not.toBeUndefined();
        	expect(profile.data).toBeNull();
        	profile.load({
        		handle: function(object){
        			expect(object.getDisplayName()).toBe("Frank Adams");
                },
                load: function(profile){
                    expect(profile.getDisplayName()).toBe("Frank Adams");
                },
                error: function(error){
                    expect(error).toBeUndefined();
                }
        	});        	
        }); 
        
        it("Should be able to handle with one object", function(){
        	var loadedProfile1 = false;
        	var loadedProfile2 = false;
        	var loadedProfile3 = false;
        	var profile1 = null;
        	var profile2 = null;
        	var profile3 = null;
        	var email1 = "BillJordan@renovations.com";
        	var email2 = "TedAmado@renovations.com";
        	var email3 = "AllieSingh@renovations.com";
        	var service = new ProfileService({cacheSize:10});	
        	runs(function (){          	
	        	profile1 = service.getProfile({loadIt:false, email:email1});
	        	expect(profile1).not.toBeUndefined();
	        	expect(profile1.data).toBeNull();
	        	profile1.load({
	        		load: function(profile){
	        			loadedProfile1 = true;
	        		}
	        	});  
	        	profile2 = service.getProfile({loadIt:false, email:email2});
	        	profile2.load({
	        		load: function(profile){
	        			loadedProfile2 = true;
	        		}
	        	});  
	        	profile3 = service.getProfile({loadIt:false, email:email3});        	
	        	profile3.load({
	        		load: function(profile){
	        			loadedProfile3 = true;
	        		}
	        	});  
        	});
        	waitsFor(function(){
        		return loadedProfile1 && loadedProfile2 && loadedProfile3;
        		}, "The profile should be loaded", 7500);
        	
        	runs(function(){        		
        		expect(profile1.getDisplayName()).toBe("Bill Jordan");
        		expect(profile2.getDisplayName()).toBe("Ted Amado");
        		expect(profile3.getDisplayName()).toBe("Allie Singh");
        	}); 
        	
        });
        
        it("Should be able to call load method of profile object with email parameter and cache size 10", function() {  
        	var loadedProfile1 = false;
        	var loadedProfile2 = false;
        	var loadedProfile3 = false;
        	var profile1 = null;
        	var profile2 = null;
        	var profile3 = null;
        	var profile4 = null;
        	var email1 = "BillJordan@renovations.com";
        	var email2 = "TedAmado@renovations.com";
        	var email3 = "AllieSingh@renovations.com";
        	var service1 = new ProfileService({cacheSize:5});	
        	runs(function (){        	
	        	
	        	
	        	profile2 = service1.getProfile({loadIt:false, email:email2});
	        	expect(profile2).not.toBeUndefined();
	        	expect(profile2.data).toBeNull();
	        	profile2.load({
	        		load: function(profile2){
	        			loadedProfile2 = true;
	        		}
	        	});         	
	        	profile3 = service1.getProfile({loadIt:false, email:email3});
	        	expect(profile3).not.toBeUndefined();
	        	expect(profile3.data).toBeNull();
	        	profile3.load({
	        		load: function(profile3){
	        			loadedProfile3 = true;
	        		}
	        	});	
	        	
        	});        	
        	waitsFor(function(){
        		return loadedProfile2 && loadedProfile3;
        		}, "All the three profiles should be loaded", 750);
        
        	runs(function(){        		
        		expect(profile2.getDisplayName()).toBe("Ted Amado");
        		expect(profile3.getDisplayName()).toBe("Allie Singh");  
        		
        		profile4 = service1.getProfile({
        			loadIt:true, 
        			email:email3,
        			load : function(profile4){
        				expect(profile4.getDisplayName()).toBe("Allie Singh");  
        			}
        		});

	        		 
        	});       
        });  
        
        
        it("Should be able to access fields after update", function() {
            var email = sbt.Properties["sample.email1"];
            var service = new ProfileService();
            var profile = service.getProfile({ id: email, loadIt: false});
            
            profile.setTitle("Dr");
            service.updateProfile(profile);
            
            // fields still contain the value that was set
            
            expect(profile.getTitle()).toBe("Dr");            
            setInterval(function(){
                // fields should still contain the value that was set
                // after update operation has completed            	 
                expect(profile.getTitle()).toBe("Dr");
            }, 7000);
            
            profile.load({
               handle: function(profile) {
                   // fields should have been cleared by load operation
                   expect(profile.getTitle()).toBe("Dr");
               } 
            });
        });
        
        it("Should return a response when called with an invalid email", function() {
            var handled = false;
            var loaded = false;
            
            runs(function() {
                var email = "invalid@unknown.com";
                var service = new ProfileService();
                service.getProfile({
                    id: email,
                    loadIt: true,
                    handle: function(data){
                        handled = true;
                        expect(data).not.toBeUndefined();
                        // TODO validate the data object
                    },
                    load: function(data){
                        loaded = true;
                        expect(data).not.toBeUndefined();
                        // TODO validate the data object
                    }
                });
            });
                        
            waitsFor(function() {
              return handled && loaded;
            }, "The handle and load callbacks should be invoked: "+handled+","+loaded, 7000);

            runs(function() {
                expect(handled && loaded).toBeTruthy();
            });
            
        });
        
        it("Should be able to handle multiple requests for the same profile", function() {
            var handled1 = false;
            var handled2 = false;
            var handled3 = false;
            
            runs(function() {
                var email = sbt.Properties["sample.email1"];
                var service = new ProfileService();
                service.getProfile({
                    id: email,
                    loaded: true,
                    handle: function(profile){
                        handled1 = true;
                    },
                    error: function(error){
                        handled1 = true;
                        expect(error).toBeUndefined();
                    }
                });
                service.getProfile({
                    id: email,
                    loaded: true,
                    handle: function(profile){
                        handled2 = true;
                    },
                    error: function(error){
                        handled2 = true;
                        expect(error).toBeUndefined();
                    }
                });
                service.getProfile({
                    id: email,
                    loaded: true,
                    handle: function(profile){
                        handled3 = true;
                    },
                    error: function(error){
                        handled3 = true;
                        expect(error).toBeUndefined();
                    }
                });
                
            });
            
            waitsFor(function() {
              return handled1 && handled2 && handled3;
            }, "The handlers should all be invoked: "+handled1+","+handled2+","+handled3, 7000);

            runs(function() {
                expect(handled1 && handled2 && handled3).toBeTruthy();
            });
        });
    });
});
