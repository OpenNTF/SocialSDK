require(["sbt/dom", 
         "sbt/connections/controls/profiles/ProfileGrid",
         "sbt/connections/controls/bootstrap/ProfileRendererMixin",
         "sbt/connections/CommunityService",
         "sbt/lang"], 

function(dom, ProfileGrid, ProfileRendererMixin, CommunityService, lang) {
    
	var communityService = new CommunityService(); 
	
	var grid = new ProfileGrid({
        type : "colleagues",
        email : "%{name=sample.email1}",
    });
    
    lang.mixin(grid.renderer, ProfileRendererMixin);
    
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
    
    dom.byId("selectedBtn").onclick = function(evt) {
        
    	var profiles = grid.getSelected();
        
        var title = document.getElementById("titleTextField").value;
        
        if(!title || !title.length > 0){
        	dom.byId("success").style.display = "none";
            dom.byId("error").style.display = "";
            dom.setText("error", "You Must Enter A Title For The Community");
        	return;
        }
        var content = document.getElementById("contentTextField").value;
        var tags = document.getElementById("tagsTextField").value;
        
        var community = communityService.newCommunity(); 
        community.setTitle(title);
        
        if(content && content.length > 0){
        	community.setContent(content);	
        }
        if(tags && tags.length > 0){
        	community.setTags(tags);
        }
        
        communityService.createCommunity(community, {               
            load : function(community) { 
            	dom.byId("success").style.display = "";
	            dom.byId("error").style.display = "none";
	            dom.setText("success", "Successfully Created Community");
                
                if(profiles && profiles.length > 0){
                	for(var i=0;i<profiles.length;i++){
                		var email = profiles[i].data.getValue("email");
                		community.addMember({id : email},{       
                		   load : function(member){
                			   dom.byId("success").style.display = "block";
	        		            dom.byId("error").style.display = "none";
	        		            dom.setText("success", "Successfully created community "+ title+ " and " +
	        		            		"added selected users");
                		   },
                		   error : function(error){
                			   dom.byId("success").style.display = "none";
	        		            dom.byId("error").style.display = "";
	        		            dom.setText("error", "Community Created But Could Not Add Users");
                		   }
                		});
                	}
                }  
            },
            error : function(error) {
            	dom.byId("success").style.display = "none";
	            dom.byId("error").style.display = "";
	            dom.setText("error", "Could Not Create Community");
            }
        });
        if(!profiles || profiles.length == 0){
        	dom.byId("success").style.display = "block";
	        dom.byId("error").style.display = "none";
	        dom.setText("success", "Successfully created community \""+ title + "\"");
        }
    };
   
});