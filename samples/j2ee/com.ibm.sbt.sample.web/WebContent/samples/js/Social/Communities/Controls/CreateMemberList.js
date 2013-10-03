require(["sbt/dom", 
         "sbt/connections/controls/profiles/ProfileGrid",
         "sbt/connections/controls/bootstrap/ProfileRendererMixin",
         "sbt/connections/CommunityService",
         "sbt/lang",
         "sbt/connections/controls/search/SearchBox"], 

function(dom, ProfileGrid, ProfileRendererMixin, CommunityService, lang, SearchBox) {
    // Search logic
	var searchBox = new SearchBox({
		type:"full",
       	searchSuggest: "on",
       	memberList: true
	});
	
	dom.byId("searchBox").appendChild(searchBox.domNode);
       
	searchBox.domNode.addEventListener("searchResultEvent",function(event) {
		if(!event){
			event = window.event;
		}
		var resultDiv = dom.byId("results");
		resultDiv.innerHTML = "";
		//Create a table to display results
		var table = document.createElement("table");
		if(event.results.length >0){
			for(var i=0;i<event.results.length;i++){
				var title = event.results[i].getTitle();
				var row = document.createElement("tr");
				var data = document.createElement("td");
				row.innerHTML = title;
				row.appendChild(data);
				table.appendChild(row);
			}
		} else {
			var row = document.createElement("tr");
			var data = document.createElement("td");
			row.innerHTML = "Your Search Returned No Results";
			row.appendChild(data);
			table.appendChild(row);
		}
       	
		resultDiv.appendChild(table);
       	
	},false);
	
	// Community service logic
	var communityService = new CommunityService(); 
	
//	var grid = new ProfileGrid({
//        type : "colleagues",
//        email : "%{name=sample.email1}",
//    });
//    
//    lang.mixin(grid.renderer, ProfileRendererMixin);
//    
//  
//
//    grid.update();
//    
//    dom.byId("selectedBtn").onclick = function(evt) {
//        
//    	var profiles = grid.getSelected();
//        
//        var title = document.getElementById("titleTextField").value;
//        
//        if(!title || !title.length > 0){
//        	dom.byId("success").style.display = "none";
//            dom.byId("error").style.display = "";
//            dom.setText("error", "You Must Enter A Title For The Community");
//        	return;
//        }
//        var content = document.getElementById("contentTextField").value;
//        var tags = document.getElementById("tagsTextField").value;
//        
//        var community = communityService.getCommunity({ loadIt : false }); 
//        community.setTitle(title);
//        
//        if(content && content.length > 0){
//        	community.setContent(content);	
//        }
//        if(tags && tags.length > 0){
//        	community.setTags(tags);
//        }
//        
//        communityService.createCommunity(community, {               
//            load : function(community) { 
//            	dom.byId("success").style.display = "";
//	            dom.byId("error").style.display = "none";
//	            dom.setText("success", "Successfully Created Community");
//                
//                if(profiles && profiles.length > 0){
//                	for(var i=0;i<profiles.length;i++){
//                		var email = profiles[i].data.getValue("email");
//                		community.addMember({id : email},{       
//                		   load : function(member){
//                			   dom.byId("success").style.display = "";
//	        		            dom.byId("error").style.display = "none";
//	        		            dom.setText("success", "Successfully Created Community "+ title+ " And " +
//	        		            		"Added Selected Users");
//                		   },
//                		   error : function(error){
//                			   dom.byId("success").style.display = "none";
//	        		            dom.byId("error").style.display = "";
//	        		            dom.setText("error", "Community Created But Could Not Add Users");
//                		   }
//                		});
//                	}
//                }  
//            },
//            error : function(error) {
//            	dom.byId("success").style.display = "none";
//	            dom.byId("error").style.display = "";
//	            dom.setText("error", "Could Not Create Community");
//            }
//        });
//        
//        
//    };
   
});