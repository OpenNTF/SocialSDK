require(["sbt/dom", 
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/controls/bootstrap/CommunityRendererMixin",
         "sbt/lang",
         "sbt/connections/CommunityService"], 
    function(dom, CommunityGrid, CommunityRendererMixin, lang, CommunityService, CustomCommunityRow) {
	    
		var communityService = new CommunityService();
		var communitiesGrid = new CommunityGrid();
	    lang.mixin(communitiesGrid.renderer, CommunityRendererMixin);
		
		//Set the custom template
		var domNode = dom.byId("communityRow");
	    var CustomCommunityRow = domNode.text || domNode.textContent;
		communitiesGrid.renderer.template = CustomCommunityRow;  
	    
	    dom.byId("gridDiv").appendChild(communitiesGrid.domNode);       
	    communitiesGrid.update();
	    
	    dom.byId("selectedBtn").onclick = function(evt) {
		        var communities = communitiesGrid.getSelected();
		        
		        if(!communities || !communities.length > 0){
		        	dom.byId("success").style.display = "none";
		            dom.byId("error").style.display = "";
		            dom.setText("error","No Communities Selected");
		        }
		        
		        var userEmail = document.getElementById("emailTextField").value;
		        if(!userEmail || !userEmail.length > 0){
		        	dom.byId("success").style.display = "none";
		            dom.byId("error").style.display = "";
		            dom.setText("error","No Email Entered");
		        }
		       
		        if(!userEmail.length > 0 && !communities.length > 0){
		        	dom.byId("success").style.display = "none";
		            dom.byId("error").style.display = "";
		            dom.setText("error","No Email Or Community Selected");
		        }
		       
		        if(userEmail && userEmail != "" && communities && communities.length > 0){
			        for (i in communities) {
			        	 var selected = communities[i].data.getValue("uid");
			        	 communityService.getCommunity({
					            id: selected,
					            load: function(community) {
					            	community.addMember({id : userEmail},{       
				        		        load : function(member){
				        		            dom.byId("success").style.display = "";
				        		            dom.byId("error").style.display = "none";
				        		            dom.setText("success", "Successfully Added User To Communities");
				        		        },
				        		        error : function(error){
				        		        	dom.byId("success").style.display = "none";
				        		            dom.byId("error").style.display = "";
				        		            dom.setText("error","Could not add the selecedted user to the selected community");
				        		            console.error(error);
				        		        }
				        		    });
					            },
					            error: function(error) {
						              console.log("Error Loading Communities");
						              console.error(error);
						        }
			        	 });
		           
			        }
		        }		        
		};
});