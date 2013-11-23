var currentForum = null;
var currentForumTopic = null;

require(["sbt/config", "sbt/connections/ForumService", "sbt/dom"], function(config, ForumService, dom) {
    dom.setText("success", "Please wait... Loading your forums");
    
    var forumService = new ForumService();
    loadMyForums(forumService, dom);

    dom.byId("refreshBtn").onclick = function(evt) {
    	loadMyForums(forumService, dom);
    };
});

function loadMyForums(forumService, dom) {
    displayMessage(dom, "Please wait... Loading your forums");

    forumService.getMyForums({ since : 0 }).then(
        function(forums) {
        	if (forums.length == 0) {
        		displayError("You do not have any forums. Please login to IBM Connecitons and create one to use this sample.");
            } else {
            	handleMyForums(forums, forumService, dom);
            }
        },
        function(error) {
        	handleError(dom, error);
        }       
	);
}

function handleMyForums(forums, forumService, dom) {
	currentForum = forums[0];
    dom.byId("forumTitle").value = currentForum.getTitle();
    dom.byId("forumUuid").value = currentForum.getForumUuid();

    resetTopic(null, dom);
    
    addOnClickHandlers(forumService, dom);
    
    displayMessage(dom, "Successfully loaded your forum: " + forums[0].getForumUuid());
}

function startTopic(forumService, title, content, tags, question, dom) {
    displayMessage(dom, "Please wait... Creating forum topic: " + title);
    
    currentForumTopic = null;
    var topic = forumService.newForumTopic(); 
    topic.setForumUuid(currentForum.getForumUuid());
    topic.setTitle(title);
    topic.setContent(content);
    topic.setTags(tags);
    topic.setQuestion(question);
    forumService.createForumTopic(topic).then(  
        function(topic) { 
        	topic.load().then(
                function(topic) { 
                    handleTopicCreated(topic, dom);
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

function updateTopic(topic, title, content, tags, question, pinned, locked, dom) {
    displayMessage(dom, "Please wait... Updating topic: " + topic.getTopicUuid());
    
    topic.setTitle(title);
    topic.setContent(content);
    topic.setTags(tags);
    topic.setPinned(pinned);
    topic.setLocked(locked);
    topic.setQuestion(question);
    topic.update().then(               
        function(topic) {
        	topic.load().then(
                function(topic) { 
                    handleTopicUpdated(topic, dom);
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

function deleteTopic(topic, dom) {
    displayMessage(dom, "Please wait... Deleting topic: " + topic.getTopicUuid());
    
    topic.remove().then(               
        function() { 
            handleTopicRemoved(topic, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function createRecommendation(topic, dom) {
    displayMessage(dom, "Please wait... Creating forum topic recommendation: " + topic.getTopicUuid());
    
    topic.createRecommendation().then(  
        function(recommendation) { 
        	topic.load().then(
                function(topic) { 
                    handleRecommendationCreated(topic, dom);
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

function deleteRecommendation(topic, dom) {
    displayMessage(dom, "Please wait... Deleting forum topic recommendation: " + topic.getTopicUuid());
    
    topic.deleteRecommendation().then(               
        function() { 
        	topic.load().then(
                function(topic) { 
                	handleRecommendationRemoved(topic, dom);
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

function handleTopicCreated(topic, dom) {   
    currentTopic = topic;
    resetTopic(topic, dom);
    resetButtons(dom);

    if (!topic) {
        displayMessage(dom, "Unable to create topic."); 
        return;
    }

    displayMessage(dom, "Successfully created topic: " + topic.getTopicUuid());
}

function handleTopicRemoved(topic, dom) {
    currentTopic = null;
	resetTopic(null, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully deleted topic: " + topic.getTopicUuid());
}

function handleTopicUpdated(topic, dom) {
	resetTopic(topic, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully updated topic: " + topic.getTopicUuid());
}

function handleRecommendationCreated(topic, dom) {
	resetTopic(topic, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully added recommendation to topic: " + topic.getTopicUuid());
}

function handleRecommendationRemoved(topic, dom) {
	resetTopic(topic, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully removed recommendation from topic: " + topic.getTopicUuid());
}

function addOnClickHandlers(forumService, dom) {
    dom.byId("startBtn").onclick = function(evt) {
        dom.byId("topicUuid").value = "";
        var title = dom.byId("topicTitle");       
        var content = dom.byId("topicContent");
        var tags = dom.byId("topicTags");
        var question = dom.byId("topicQuestion");
        startTopic(forumService, title.value, content.value, tags.value, question.checked, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        if (currentTopic) {
            deleteTopic(currentTopic, dom);
        }
    };
    dom.byId("updateBtn").onclick = function(evt) {
        if (currentTopic) {
            var title = dom.byId("topicTitle");       
            var content = dom.byId("topicContent");
            var tags = dom.byId("topicTags");
            var pin = dom.byId("topicPin");
            var lock = dom.byId("topicLock");
            var question = dom.byId("topicQuestion");
            updateTopic(currentTopic, title.value, content.value, tags.value, question.checked, pin.checked, lock.checked, dom);
        }
    };
    dom.byId("likeBtn").onclick = function(evt) {
        if (currentTopic) {
        	if (currentTopic.isNotRecommendedByCurrentUser()) {
        		createRecommendation(currentTopic, dom);
        	} else {
        		deleteRecommendation(currentTopic, dom);
        	}
        }
    };
}

function resetButtons(dom) {
	var deleteBtn = dom.byId("deleteBtn");
	var updateBtn = dom.byId("updateBtn");
	var likeBtn = dom.byId("likeBtn");
	
	if (currentTopic) {
		deleteBtn.disabled = false;
		updateBtn.disabled = false;
		while(likeBtn.firstChild) likeBtn.removeChild(likeBtn.firstChild);
		likeBtn.appendChild(dom.createTextNode(currentTopic.isNotRecommendedByCurrentUser() ? "Like" : "Unlike"));
		likeBtn.disabled = false;
	} else {
		deleteBtn.disabled = true;
		updateBtn.disabled = true;
		while(likeBtn.firstChild) likeBtn.removeChild(likeBtn.firstChild);
		likeBtn.appendChild(dom.createTextNode("Like Topic"));
		likeBtn.disabled = true;
	}
}

function resetTopic(topic, dom) {
	if (topic) {
	    dom.byId("topicUuid").value = topic.getTopicUuid();
	    dom.byId("communityUuid").value = topic.getCommunityUuid();
	    dom.byId("topicTitle").value = topic.getTitle();
	    dom.byId("topicContent").value = topic.getContent();
	    dom.byId("topicTags").value = topic.getTags().join();
	    dom.byId("topicQuestion").checked = topic.isQuestion();
	    dom.byId("topicLock").checked = topic.isLocked();
	    dom.byId("topicLock").disabled = false;
	    dom.byId("topicPin").checked = topic.isPinned();
	    dom.byId("topicPin").disabled = false;
	    dom.byId("topicNRBCU").checked = topic.isNotRecommendedByCurrentUser();
	} else {
	    dom.byId("topicUuid").value = "";
	    dom.byId("communityUuid").value = "";
	    dom.byId("topicTitle").value = "";
	    dom.byId("topicContent").value = "";
	    dom.byId("topicTags").value = "";
	    dom.byId("topicQuestion").checked = false;
	    dom.byId("topicLock").checked = false;
	    dom.byId("topicLock").disabled = true;
	    dom.byId("topicPin").checked = false;
	    dom.byId("topicPin").disabled = true;
	    dom.byId("topicNRBCU").checked = true;
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
