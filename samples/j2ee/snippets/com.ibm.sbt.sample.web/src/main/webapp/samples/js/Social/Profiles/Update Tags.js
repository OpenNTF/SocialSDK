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
	var tagsArray = ['testTag1', 'testTag2'];
	profileService.updateTags(tagsArray, targetProfileId , sourceProfileId).then(
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