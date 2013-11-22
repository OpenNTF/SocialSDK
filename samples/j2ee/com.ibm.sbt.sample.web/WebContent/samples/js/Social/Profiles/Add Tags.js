require(["sbt/connections/ProfileService","sbt/dom", "sbt/json"], function(ProfileService,dom, json) {
	var createRow = function(tag) {
        var table = dom.byId("tagsTable");
        var tr = document.createElement("tr");
        table.appendChild(tr);
        var td = document.createElement("td");
        dom.setText(td, tag.getTerm());
        tr.appendChild(td);
        td = document.createElement("td");
        dom.setText(td, tag.getFrequency());
        tr.appendChild(td);
    };
	
	var targetProfileId = "%{name=sample.email2}";
	var sourceProfileId = "%{name=sample.email1}";
	var profileService = new ProfileService();
	var tagsToAdd = ['newTestTag1', 'newTestTag2'];
	var tagsArray = [];
	// Getting already existing tags so that they can be sent along with new tags.
	// passing sourceEmail parameter so that only those tags are returned which were contributed by user with sourceEmail
	profileService.getTags(targetProfileId,{sourceEmail:sourceProfileId}).then(    
        function(tags) {
        	return tags;
        }
    ).then(  
		function(tags) {
	    	for (var i=0;i<tags.length;i++){
	    		tagsArray.push(tags[i].getTerm());
	    	}
	    	tagsArray = tagsToAdd.concat(tagsArray);  // Concatenating existing and new tags
	    	return profileService.updateTags(tagsArray, targetProfileId , sourceProfileId);
		}
	).then(
		function(){
			return profileService.getTags(targetProfileId);
		}
	).then(
		function(tags){
            for(var i=0; i<tags.length; i++){
                var tag = tags[i];
                createRow(tag);
            }
        },
        function(error){
            dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
        }  
    );	
});