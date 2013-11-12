require(["sbt/dom", 
         "sbt/connections/controls/forums/ForumGrid",
         "sbt/connections/ForumService"], 
    function(dom, ForumGrid, ForumService) {
		// display My Forums
		var forumGrid = new ForumGrid({
	        type : "my"
	    });
	    dom.byId("gridDiv").appendChild(forumGrid.domNode);
	    forumGrid.update();
	    
	    // create ForumService
	    var forumService = new ForumService();
	    addStartForumBehaviour(forumGrid, forumService, dom);
	}
);

function addStartForumBehaviour(forumGrid, forumService, dom) {
	var actionBtn = dom.byId("actionBtn");
    actionBtn.onclick = function(evt) {
    	var title = prompt("Please enter a title for the new forum", "");
        if (title) {
        	var forum = forumService.newForum(); 
            forum.setTitle(title);
            forum.setContent("");
            var promise = forumService.createForum(forum);
            promise.then(
                function(forum) {
                	forumGrid.refresh();
                	alert("Forum started successfully!");
                },
                function(error) {
                	alert("Error starting forum: "+error);
                }
            );
        }
    };
}