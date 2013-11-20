var currentWiki = null;

require(["sbt/config", "sbt/connections/WikiService", "sbt/dom"], function(config, WikiService, dom) {
    dom.setText("success", "Please wait... Loading your wikis");
    
    var wikiService = new WikiService();
    loadMyWikis(wikiService, dom);

    dom.byId("refreshBtn").onclick = function(evt) {
    	loadMyWikis(wikiService, dom);
    };
});

function loadMyWikis(wikiService, dom) {
    displayMessage(dom, "Please wait... Loading your wikis");

    wikiService.getMyWikis({ includeTags:true, acls:true }).then(
        function(wikis) {
        	if (wikis.length == 0) {
        		displayError("You do not have any wikis. Please login to IBM Connecitons and create one to use this sample.");
            } else {
            	handleMyWikis(wikis, wikiService, dom);
            }
        },
        function(error) {
        	handleError(dom, error);
        }       
	);
}

function handleMyWikis(wikis, wikiService, dom) {
	currentWiki = wikis[0];

    resetWiki(currentWiki, dom);
    
    addOnClickHandlers(wikiService, dom);

    showMyWikis(wikis, dom);
    
    displayMessage(dom, "Successfully loaded your wiki: " + wikis[0].getLabel());
}

function startWiki(wikiService, title, summary, tags, label, dom) {
    displayMessage(dom, "Please wait... Creating wiki: " + title);
    
    currentWiki = null;
    var wiki = wikiService.newWiki(); 
    wiki.setTitle(title);
    wiki.setSummary(summary);
    wiki.setTags(tags);
    wiki.setLabel(label);
    wikiService.createWiki(wiki).then(  
        function(wiki) { 
        	wiki.load().then(
                function(wiki) { 
                    handleWikiCreated(wiki, dom);
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

function updateWiki(wiki, title, summary, tags, dom) {
    displayMessage(dom, "Please wait... Updating wiki: " + wiki.getLabel());
    
    wiki.setTitle(title);
    wiki.setSummary(summary);
    wiki.setTags(tags);
    wiki.update().then(               
        function(wiki) {
        	wiki.load({ includeTags:true, acls:true }).then(
                function(wiki) { 
                    handleWikiUpdated(wiki, dom);
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

function deleteWiki(wiki, dom) {
    displayMessage(dom, "Please wait... Deleting wiki: " + wiki.getLabel());
    
    wiki.remove().then(               
        function() { 
            handleWikiRemoved(wiki, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function handleWikiCreated(wiki, dom) {   
    currentWiki = wiki;
    resetWiki(wiki, dom);
    resetButtons(dom);

    if (!wiki) {
        displayMessage(dom, "Unable to create wiki."); 
        return;
    }

    displayMessage(dom, "Successfully created wiki: " + wiki.getLabel());
}

function handleWikiRemoved(wiki, dom) {
    currentWiki = null;
	resetWiki(null, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully deleted wiki: " + wiki.getLabel());
}

function handleWikiUpdated(wiki, dom) {
	resetWiki(wiki, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully updated wiki: " + wiki.getLabel());
}

function addOnClickHandlers(wikiService, dom) {
    dom.byId("startBtn").onclick = function(evt) {
        dom.byId("wikiUuid").value = "";
        var title = dom.byId("wikiTitle");       
        var summary = dom.byId("wikiSummary");
        var tags = dom.byId("wikiTags");
        var label = dom.byId("wikiLabel");
        startWiki(wikiService, title.value, summary.value, tags.value.split(","), label.value, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        if (currentWiki) {
            deleteWiki(currentWiki, dom);
        }
    };
    dom.byId("updateBtn").onclick = function(evt) {
        if (currentWiki) {
            var title = dom.byId("wikiTitle");       
            var summary = dom.byId("wikiSummary");
            var tags = dom.byId("wikiTags");
            updateWiki(currentWiki, title.value, summary.value, tags.value.split(","), dom);
        }
    };
}

function resetButtons(dom) {
	var deleteBtn = dom.byId("deleteBtn");
	var updateBtn = dom.byId("updateBtn");
	
	if (currentWiki) {
		deleteBtn.disabled = false;
		updateBtn.disabled = false;
	} else {
		deleteBtn.disabled = true;
		updateBtn.disabled = true;
	}
}

function resetWiki(wiki, dom) {
	if (wiki) {
	    dom.byId("wikiUuid").value = wiki.getUuid();
	    dom.byId("wikiLabel").value = wiki.getLabel();
	    dom.byId("wikiTitle").value = wiki.getTitle();
	    dom.byId("wikiSummary").value = wiki.getSummary();
	    dom.byId("wikiTags").value = wiki.getTags().join();
	} else {
	    dom.byId("wikiUuid").value = "";
	    dom.byId("wikiLabel").value = "";
	    dom.byId("wikiTitle").value = "";
	    dom.byId("wikiSummary").value = "";
	    dom.byId("wikiTags").value = "";
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

function showMyWikis(wikis, dom) {
    var tableBody = dom.byId("wikisTableBody");
    while (tableBody.childNodes[0]) {
        dom.destroy(tableBody.childNodes[0]);
    }
    
    for(var i=0; i<wikis.length; i++){
        var wiki = wikis[i];
        createRow(wiki, dom);
    }
}

function createRow(wiki, dom) {
    var title = wiki.getTitle(); 
    var uuid = wiki.getUuid(); 
    var label = wiki.getLabel(); 
    var summary = wiki.getSummary();
    var tags = wiki.getTags();
	
    var tableBody = dom.byId("wikisTableBody");
    var tr = document.createElement("tr");
    tableBody.appendChild(tr);
    var td = document.createElement("td");
    td.appendChild(dom.createTextNode(title));
    tr.appendChild(td);
    td = document.createElement("td");
    td.appendChild(dom.createTextNode(label));
    tr.appendChild(td);
    td = document.createElement("td");
    td.appendChild(dom.createTextNode(summary));
    tr.appendChild(td);
    td = document.createElement("td");
    td.appendChild(dom.createTextNode(tags.join()));
    tr.appendChild(td);
}

