var currentTopic = null;
var currentReply = null;

require(["sbt/config", "sbt/connections/ForumService", "sbt/dom"], function(config, ForumService, dom) {
    dom.setText("success", "Please wait... Loading your forums");
    
    var forumService = new ForumService();
    loadMyTopics(forumService, dom);

    dom.byId("refreshBtn").onclick = function(evt) {
    	loadMyTopics(forumService, dom);
    };
});

function loadMyTopics(forumService, dom) {
    displayMessage(dom, "Please wait... Loading your topics");

    forumService.getMyTopics({ since : 0 }).then(
        function(topics) {
        	if (topics.length == 0) {
        		displayError("You do not have any topics. Please login to IBM Connecitons and create one to use this sample.");
            } else {
            	handleMyTopics(topics, forumService, dom);
            }
        },
        function(error) {
        	handleError(dom, error);
        }       
	);
}

function handleMyTopics(topics, forumService, dom) {
	currentTopic = topics[0];
    dom.byId("topicTitle").value = currentTopic.getTitle();
    dom.byId("topicUuid").value = currentTopic.getTopicUuid();

    resetReply(null, dom);
    
    addOnClickHandlers(forumService, dom);
    
    displayMessage(dom, "Successfully loaded your forum reply: " + currentTopic.getForumUuid());
}

function createReply(forumService, title, content, dom) {
    displayMessage(dom, "Please wait... Creating forum reply: " + title);
    
    currentReply = null;
    var reply = forumService.newForumReply(); 
    reply.setTopicUuid(currentTopic.getTopicUuid());
    reply.setTitle(title);
    reply.setContent(content);
    forumService.createForumReply(reply).then(  
        function(reply) { 
            handleReplyCreated(reply, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function updateReply(reply, title, content, answer, dom) {
    displayMessage(dom, "Please wait... Updating reply: " + reply.getReplyUuid());
    
    reply.setTitle(title);
    reply.setContent(content);
    reply.setAnswer(answer);
    reply.update().then(               
        function(reply) {
        	reply.load().then(
                function(reply) { 
                	handleReplyUpdated(reply, dom);
                },
                function(error) {
                    handleError(dom, error);
                }
            );
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function deleteReply(reply, dom) {
    displayMessage(dom, "Please wait... Deleting reply: " + reply.getReplyUuid());
    
    reply.remove().then(               
        function() { 
            handleReplyRemoved(reply, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function createRecommendation(reply, dom) {
    displayMessage(dom, "Please wait... Creating forum reply recommendation: " + reply.getReplyUuid());
    
    reply.createRecommendation().then(  
        function(recommendation) { 
        	reply.load().then(
                function(reply) { 
                    handleRecommendationCreated(reply, dom);
                },
                function(error) {
                    handleError(dom, error);
                }
            );
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function deleteRecommendation(reply, dom) {
    displayMessage(dom, "Please wait... Deleting forum reply recommendation: " + reply.getReplyUuid());
    
    reply.deleteRecommendation().then(               
        function() { 
        	reply.load().then(
                function(reply) { 
                	handleRecommendationRemoved(reply, dom);
                },
                function(error) {
                    handleError(dom, error);
                }
            );
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function handleReplyCreated(reply, dom) {
    currentReply = reply;
	resetReply(reply, dom);
    resetButtons(dom);

    if (!reply) {
        displayMessage(dom, "Unable to create reply."); 
        return;
    }
    displayMessage(dom, "Successfully created reply: " + reply.getReplyUuid());
}

function handleReplyRemoved(reply, dom) {
    currentReply = null;
	resetReply(null, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully deleted reply: " + reply.getReplyUuid());
}

function handleReplyUpdated(reply, dom) {
	resetReply(reply, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully updated reply: " + reply.getReplyUuid());
}

function handleRecommendationCreated(reply, dom) {
	resetReply(reply, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully added recommendation to reply: " + reply.getReplyUuid());
}

function handleRecommendationRemoved(reply, dom) {
	resetReply(reply, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully removed recommendation from reply: " + reply.getReplyUuid());
}

function addOnClickHandlers(forumService, dom) {
    dom.byId("createBtn").onclick = function(evt) {
        dom.byId("replyUuid").value = "";
        var title = dom.byId("replyTitle");       
        var content = dom.byId("replyContent");
        createReply(forumService, title.value, content.value, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        if (currentReply) {
            deleteReply(currentReply, dom);
        }
    };
    dom.byId("updateBtn").onclick = function(evt) {
        if (currentReply) {
            var title = dom.byId("replyTitle");       
            var content = dom.byId("replyContent");
            var answer = dom.byId("replyAnswer");
            updateReply(currentReply, title.value, content.value, answer.checked, dom);
        }
    };
    dom.byId("likeBtn").onclick = function(evt) {
        if (currentReply) {
        	if (currentReply.isNotRecommendedByCurrentUser()) {
        		createRecommendation(currentReply, dom);
        	} else {
        		deleteRecommendation(currentReply, dom);
        	}
        }
    };
}

function resetButtons(dom) {
	var deleteBtn = dom.byId("deleteBtn");
	var updateBtn = dom.byId("updateBtn");
	var likeBtn = dom.byId("likeBtn");
	
	if (currentReply) {
		deleteBtn.disabled = false;
		updateBtn.disabled = false;
		while(likeBtn.firstChild) likeBtn.removeChild(likeBtn.firstChild);
		likeBtn.appendChild(dom.createTextNode(currentReply.isNotRecommendedByCurrentUser() ? "Like" : "Unlike"));
		likeBtn.disabled = false;
	} else {
		deleteBtn.disabled = true;
		updateBtn.disabled = true;
		while(likeBtn.firstChild) likeBtn.removeChild(likeBtn.firstChild);
		likeBtn.appendChild(dom.createTextNode("Like Topic"));
		likeBtn.disabled = true;
	}
}

function resetReply(reply, dom) {
	if (reply) {
	    dom.byId("replyUuid").value = reply.getReplyUuid();
	    dom.byId("replyTitle").value = reply.getTitle();
	    dom.byId("replyContent").value = reply.getContent();
	    dom.byId("replyAnswer").checked = reply.isAnswer();
	    dom.byId("replyAnswer").disabled = false;
	    dom.byId("replyNRBCU").checked = reply.isNotRecommendedByCurrentUser();
	} else {
	    dom.byId("replyUuid").value = "";
	    dom.byId("replyTitle").value = "";
	    dom.byId("replyContent").value = "";
	    dom.byId("replyAnswer").checked = false;
	    dom.byId("replyAnswer").disabled = true;
	    dom.byId("replyNRBCU").checked = true;
	}
}

function displayMessage(dom, msg) {
    dom.setText("success", msg); 
    
    dom.byId("success").style.display = "";
    dom.byId("error").style.display = "none";
}

function displayError(dom, message) {
    dom.setText("error", "Error: " + message);
    
    dom.byId("success").style.display = "none";
    dom.byId("error").style.display = "";
}

function handleError(dom, error) {
    dom.setText("error", "Error: " + error.message);
    
    dom.byId("success").style.display = "none";
    dom.byId("error").style.display = "";
}

function clearError(dom) {
    dom.setText("error", "");
    
    dom.byId("error").style.display = "none";
}
