require(["sbt/dom", "sbt/connections/controls/profiles/ProfileTagsGrid"], function(dom, ProfileTagsGrid) {
    var grid = new ProfileTagsGrid({
        type : "list",
        targetEmail : "%{name=sample.email1}",
        sourceEmail : "%{name=sample.email2}",
        sourceName : "%{name=sample.displayName2}"
    });
    
    grid.profileTagAction = {
    		
        getTooltip : function(item) {
        	return string.substitute("Display details for ${term}", { title : item.getValue("term") });
        },

        execute : function(item,opts,event) {
            var str =
                "term: " + item.getValue("term") + "\n" +
                "uid: " + item.getValue("uid") + "\n" +
                "frequency: " + item.getValue("frequency");
            alert(str);
        }
    
    };

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});
