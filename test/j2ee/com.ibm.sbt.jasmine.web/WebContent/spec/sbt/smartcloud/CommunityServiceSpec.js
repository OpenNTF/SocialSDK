require(["sbt/smartcloud/CommunityService"], function(CommunityService) {
    describe("sbt.smartcloud.CommunityService", function() {

        beforeEach(function() {
        });
        
        it("Should be defined", function() {
            expect(CommunityService).not.toBeUndefined();
        });
    
        it("Should be able to retrieve all public communities", function() {
            var loaded = false;
            
            runs(function() {
	        	var communityService = new CommunityService();
	        	communityService.getPublicCommunities({
	        		parameters:{
	        			ps:5
	        		},
	        		load: function(communities){
	        			loaded = true;
	        			expect(communities.length).toBeGreaterThan(0);
	        			while(communities.length > 0){
	        				var community = communities.shift();
	        				expect(community.getTitle()).toBeDefined();
	        				expect(community.getTitle()).not.toBe("");
	        			}
	        		},
	        		error: function(error){
	        			expect(error).toBeUndefined();
	        		}		
	        	});
            });
            
            waitsFor(function() {
	            return loaded;
	         }, "The load callback should be invoked: "+loaded, 7000);
	
			 runs(function() {
			     expect(loaded).toBeTruthy();
			 });
        	
        });
        
        it("Should be able to retrieve my communities", function() {
            var loaded = false;
            
            runs(function() {
	        	var communityService = new CommunityService();
	        	communityService.getMyCommunities({
	        		parameters:{
	        			ps:5
	        		},
	        		load: function(communities){
	        			loaded = true;
	        			expect(communities.length).toBeGreaterThan(0);
	        			while(communities.length > 0){
	        				var community = communities.shift();
	        				expect(community.getTitle()).toBeDefined();
	        				expect(community.getTitle()).not.toBe("");
	        			}
	        		},
	        		error: function(error){
	        			expect(error).toBeUndefined();
	        		}		
	        	});
            });
            
            waitsFor(function() {
	            return loaded;
	         }, "The load callback should be invoked: "+loaded, 7000);
	
			 runs(function() {
			     expect(loaded).toBeTruthy();
			 });
        	
        });
        
        it("Should be able to retrieve community title", function() {
            var loaded = false;
            
            runs(function() {
	        	var communityService = new CommunityService();
	        	var communityId = "af20ded8-0daa-45aa-bdd3-0b5829b53581";
	        	communityService.getCommunity({
	        		id: communityId,
	        		load: function(community){
	        			loaded = true;
	        			expect(community.getTitle()).toBe("Open Renovations Stuff");
	        			while(communities.length > 0){
	        				var community = communities.shift();
	        				expect(community.getTitle()).toBeDefined();
	        				expect(community.getTitle()).not.toBe("");
	        			}
	        		},
	        		error: function(error){
	        			expect(error).toBeUndefined();
	        		}		
	        	});
            });
            
            waitsFor(function() {
	            return loaded;
	         }, "The load callback should be invoked: "+loaded, 7000);
	
			 runs(function() {
			     expect(loaded).toBeTruthy();
			 });
        });
        
        it("should be able to retrieve community members", function(){
        	var loaded = false;
        	runs(function() {
	        	var communityService = new CommunityService();
	        	var communityId = "af20ded8-0daa-45aa-bdd3-0b5829b53581";
	        	var community = communityService.getCommunity({
	        		id		: communityId,
	        		loadIt	: false
	        	});
	        	communityService.getMembers(community, {		
	        		load: function(members){
	        			loaded = true;
	        			expect(members.length).toBeGreaterThan(0);
	        			while(members.length > 0){
	        				var member = members.shift();
	        				expect(member.getName()).toBeDefined();
	        				expect(member.getName()).not.toBe("");
	        			}
	        		},
	        		error: function(error){
	        			expect(error).toBeUndefined();
	        		}
	        	});
        	});

            waitsFor(function() {
	            return loaded;
	         }, "The load callback should be invoked: "+loaded, 7000);
	
			 runs(function() {
			     expect(loaded).toBeTruthy();
			 });
        });
        
        it("should be able to retrieve a community member name", function(){
        	var loaded = false;
        	runs(function() {
	        	var communityService = new CommunityService();
	        	var communityId = "af20ded8-0daa-45aa-bdd3-0b5829b53581";
	        	var userId = "20079989";
	        	var community = communityService.getCommunity({
	        		id		: communityId,
	        		loadIt	: false
	        	});
	        	communityService.getMember({		
	        		community: community,
	        		id:userId,
	        		loadIt : true,
	        		load: function(member){
	        			loaded = true;
	        			expect(member.getName()).toBeDefined();
	    				expect(member.getName()).not.toBe("");
	        		},
	        		error: function(error){
	        			expect(error).toBeUndefined();
	        		}
	        	});
	        });

            waitsFor(function() {
	            return loaded;
	         }, "The load callback should be invoked: "+loaded, 7000);
	
			 runs(function() {
			     expect(loaded).toBeTruthy();
			 });
        });
    });
});
