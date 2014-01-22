<div  style="float:left; width:30%;" role="content">
	<div id="error" style="display:none;" class="alert alert-error"></div>
	<div id="success" style="display: none;" class="alert alert-success"></div>
	<div class="control-group">
	<legend>Create New Community</legend>
		<div class="controls">
			<label class="control-label" for="titleTextField">Title:</label> <input id="titleTextField" type="text" /> 
		</div>
		<div class="controls">
			<label class="control-label" for="contentTextField">Content:</label><input id="contentTextField" type="text" /> 
		</div>
		<div class="controls">
			<label class="control-label" for="tagsTextField">Tags:</label><input id="tagsTextField" type="text" /> 
		</div>
	</div>
	<button class="btn" id="selectedBtn">Create Community</button>
</div>
          	
          	
            <script type="text/javascript">
            require(["sbt/dom", 
                     "sbt/connections/CommunityService",
                     "sbt/lang"], 

            function(dom, CommunityService, lang) {
                
            	var communityService = new CommunityService(); 
                
                dom.byId("selectedBtn").onclick = function(evt) {
                    
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
                        	dom.byId("success").style.display = "block";
            	            dom.byId("error").style.display = "none";
            	            dom.setText("success", "Successfully Created Community");
                        },
                        error : function(error) {
                        	console.log(error);
                        }
                    });
                };
               
            });
				</script>            
