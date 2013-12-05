require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "communityFiles",
	         communityId:"%{name=sample.communityId1|helpSnippetId=Social_Communities_Get_My_Communities}",
	    });
        
        dom.byId("gridDiv").appendChild(grid.domNode);
        
        grid.update();
});