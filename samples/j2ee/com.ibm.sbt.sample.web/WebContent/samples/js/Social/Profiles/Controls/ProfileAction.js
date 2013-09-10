require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "profile",
        userid : "%{name=sample.userId1|helpSnippetId=Social_Profiles_Get_Profile}"
    });
    
    grid.profileAction = {
    		
        getTooltip : function(item) {
        	return string.substitute("Display details for ${name}", { title : item.getValue("name") });
        },

        execute : function(item,opts,event) {
            var str =
                "userid: " + item.getValue("userid") + "\n" +
                "name: " + item.getValue("name") + "\n" +
                "email: " + item.getValue("email") + "\n" +
                "profileUrl: " + item.getValue("profileUrl");
            alert(str);
        }
    
    };
   
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
  
});