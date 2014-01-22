require(["sbt/declare", "sbt/dom", "sbt/connections/controls/forums/ForumView"], 
    function(declare, dom, ForumView) {
	    var forumView = new ForumView({gridArgs:{type : "forumTopics",forumUuid: "c843c7b6-47e3-4296-b221-ad8ac8e426be"}});
	    dom.byId("forumViewDiv").appendChild(forumView.domNode);
	}
);