require(["sbt/dom", 
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/controls/profiles/ProfileGrid",
         "sbt/connections/CommunityService",
         "sbt/connections/controls/bootstrap/CommunityRendererMixin",
         "sbt/connections/controls/bootstrap/ProfileRendererMixin",
         "sbt/lang"], 
		
function(dom, CommunityGrid, ProfileGrid,CommunityService,CommunityRendererMixin,ProfileRendererMixin, lang) {
	
	var communityService = new CommunityService();

    //Create a new community Grid
	var communitiesGrid = new CommunityGrid({
	     type: "my"
	});
	
	lang.mixin(communitiesGrid.renderer, CommunityRendererMixin);
	
	//Set the custom template
	var domNode = dom.byId("communityRow");
    var CustomCommunityRow = domNode.text || domNode.textContent;
	communitiesGrid.renderer.template = CustomCommunityRow;  
	
	dom.byId("communitiesDiv").appendChild(communitiesGrid.domNode);
          
	communitiesGrid.update();
	
	//create a new profile grid
	var profileGrid = new ProfileGrid({
	    type : "colleagues",
	    email : "%{name=sample.email1}"
	});
	
	lang.mixin(profileGrid.renderer, ProfileRendererMixin);

	dom.byId("profilesDiv").appendChild(profileGrid.domNode);

	profileGrid.update();
	
	//event handler for the add button
	dom.byId("addButton").onclick = function(evt) {
		var profiles = profileGrid.getSelected(); 
		var communities = communitiesGrid.getSelected(); 
		
		if(profiles && communities){
			if(profiles.length >0 && communities.length >0){
				for(i in communities){
					var communityId = communities[i].data.uid;
					console.log("Getting community -" +communities[i].data.title +"- id="+communityId);
					communityService.getCommunity({
				            id: communityId,
				            load: function(community) {
				        		for (i in profiles) {
				        			var email = "";
				        			console.log("adding user "+email+" to community");
				        			email = profiles[i].data.getValue("email");
				        			community.addMember({id : email},{       
				        		        load : function(member){
				        		            dom.byId("success").style.display = "";
				        		            dom.byId("error").style.display = "none";
				        		            dom.setText("success", "Successfully Added Users");
				        		        },
				        		        error : function(error){
				        		        	dom.byId("success").style.display = "none";
				        		            dom.byId("error").style.display = "";
				        		            dom.setText("error","Could not add the selecedted user to the selected community");
				        		        }
				        		    });
				        		}
				        		
				            },
				            error: function(error) {
				               console.log(error);
				            }       
				        });
				}
			}else if(profiles.length >0 && !communities.length >0){
				alert("You have not selected any communities");
			}else if(communities.length > 0 && !profiles.length >0){
				alert("You Have not selected any users");
			}else if(!profiles.length > 0 & !communities.length > 0){
				alert("You have not selected any users or communities");
			}
		}
	};
});
