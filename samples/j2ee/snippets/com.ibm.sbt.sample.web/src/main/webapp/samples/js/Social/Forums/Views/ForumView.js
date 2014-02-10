require(["sbt/declare", "sbt/dom", "sbt/connections/controls/forums/ForumView"], 
    function(declare, dom, ForumView) {
	    
		//Add CKeditor support 
		var ckeditorUrl =  "%{name=sample.ckeditorUrl}";
		if(ckeditorUrl != ""){
			dom.byId("ckeditorScript").setAttribute("src",ckeditorUrl);
		}
		
		var forumView = new ForumView({
	    	gridArgs:{
	    		type : "forumTopics",
	    		forumUuid: "%{name=sample.forumUuid|helpSnippetId=Social_Forums_Get_My_Forums}"
	    	}
	    });
	    dom.byId("forumViewDiv").appendChild(forumView.domNode);
	    
	}
);