require(["sbt/connections/ForumService", "sbt/dom"], 
    function(ForumService,dom) {
        var createRow = function(topic) {
            var table = dom.byId("topicsTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.appendChild(dom.createTextNode(topic.getTitle()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(topic.getTopicUuid()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(topic.isLocked()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(topic.isPinned()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(topic.isQuestion()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(topic.isAnswered()));
            tr.appendChild(td);
        };

        var forumService = new ForumService();
        forumService.getMyTopics().then(
        	function(topics) {
        		if (topics.length == 0) {
        			dom.setText("content", "You have no topics.");
        		}
        		for(var i=0; i<topics.length; i++) {
                    var topic = topics[i];
                    createRow(topic);
                }
        	},
        	function(error) {
        		dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
        	}
        );
    }
);