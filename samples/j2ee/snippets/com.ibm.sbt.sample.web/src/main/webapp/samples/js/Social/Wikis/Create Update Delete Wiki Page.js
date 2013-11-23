var currentWiki = null;
var currentWiki = null;

require(["sbt/config", "sbt/connections/WikiService", "sbt/dom"], function(config, WikiService, dom) {
    dom.setText("success", "Please wait... Loading your wiki pages");
    
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

function loadMyWikiPages(wikiHandle, wikiService, dom) {
    dom.setText("success", "Please wait... Loading your wiki pages from: "+wikiHandle);

    wikiService.getMyWikiPages(wikiHandle, { includeTags:true, acls:true }).then(
        function(wikiPages) {
        	if (wikiPages.length == 0) {
        		displayError("You do not have any wiki pages in: "+wikiHandle+". Please login to IBM Connecitons and create one to use this sample.");
            } else {
            	handleMyWikiPages(wikiPages, wikiService, dom);
            }
        },
        function(error) {
        	handleError(dom, error);
        }       
	);
}

function handleMyWikis(wikis, wikiService, dom) {
	currentWiki = wikis[0];
	
	dom.byId("wikiLabel").value = wikis[0].getLabel();

    loadMyWikiPages(wikis[0].getLabel(), wikiService, dom);
    
    displayMessage(dom, "Successfully loaded your wiki: " + wikis[0].getLabel());
}

function handleMyWikiPages(wikiPages, wikiService, dom) {
	currentWikiPage = wikiPages[0];

    resetWikiPage(currentWikiPage, dom);
    
    addOnClickHandlers(wikiService, dom);
    
    displayMessage(dom, "Successfully loaded your wiki page: " + wikiPages[0].getLabel());
}

function createWikiPage(wikiService, title, summary, tags, label, dom) {
    displayMessage(dom, "Please wait... Creating wiki page: " + title);
    
    currentWikiPage = null;
    var wikiPage = wikiService.newWikiPage(); 
    wikiPage.setWikiLabel(currentWiki.getLabel());
    wikiPage.setTitle(title);
    wikiPage.setSummary(summary);
    wikiPage.setTags(tags);
    wikiPage.setLabel(label);
    wikiService.createWikiPage(wikiPage).then(  
        function(wikiPage) { 
        	wikiPage.load().then(
                function(wikiPage) { 
                    handleWikiPageCreated(wikiPage, dom);
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

function updateWikiPage(wikiPage, title, summary, tags, dom) {
    displayMessage(dom, "Please wait... Updating wiki page: " + wikiPage.getLabel());
    
    wikiPage.setTitle(title);
    wikiPage.setSummary(summary);
    wikiPage.setTags(tags);
    wikiPage.update().then(               
        function(wikiPage) {
        	wikiPage.load({ includeTags:true, acls:true }).then(
                function(wikiPage) { 
                    handleWikiPageUpdated(wikiPage, dom);
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

function deleteWikiPage(wikiPage, dom) {
    displayMessage(dom, "Please wait... Deleting wiki page: " + wikiPage.getLabel());
    
    wikiPage.remove().then(               
        function() { 
            handleWikiPageRemoved(wikiPage, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function handleWikiPageCreated(wikiPage, dom) {   
    currentWikiPage = wikiPage;
    resetWikiPage(wikiPage, dom);
    resetButtons(dom);

    if (!wiki) {
        displayMessage(dom, "Unable to create wiki page."); 
        return;
    }

    displayMessage(dom, "Successfully created wiki page: " + wikiPage.getLabel());
}

function handleWikiPageRemoved(wikiPage, dom) {
    currentWikiPage = null;
	resetWikiPage(null, dom);
	resetButtons(dom);
	
    displayMessage(dom, "SuccessfuwikiPagedeleted wiki page: " + wikiPage.getLabel());
}

function handleWikiPageUpdated(wikiPage, dom) {
	resetWikiPage(wikiPage, dom);
	resetButtons(dom);
	
    displayMessage(dom, "Successfully updated wiki page: " + wikiPage.getLabel());
}

function addOnClickHandlers(wikiService, dom) {
    dom.byId("createBtn").onclick = function(evt) {
        dom.byId("wikiPageUuid").value = "";
        var title = dom.byId("wikiPageTitle");       
        var summary = dom.byId("wikiPageSummary");
        var tags = dom.byId("wikiPageTags");
        var label = dom.byId("wikiPageLabel");
        createWikiPage(wikiService, title.value, summary.value, tags.value.split(","), label.value, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        if (currentWiki) {
            deleteWikiPage(currentWiki, dom);
        }
    };
    dom.byId("updateBtn").onclick = function(evt) {
        if (currentWiki) {
            var title = dom.byId("wikiPageTitle");       
            var summary = dom.byId("wikiPageSummary");
            var tags = dom.byId("wikiPageTags");
            updateWikiPage(currentWikiPage, title.value, summary.value, tags.value.split(","), dom);
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

function resetWikiPage(wikiPage, dom) {
	if (wikiPage) {
	    dom.byId("wikiPageUuid").value = wikiPage.getUuid();
	    dom.byId("wikiPageLabel").value = wikiPage.getLabel();
	    dom.byId("wikiPageTitle").value = wikiPage.getTitle();
	    dom.byId("wikiPageSummary").value = wikiPage.getSummary();
	    dom.byId("wikiPageTags").value = wikiPage.getTags().join();
	} else {
	    dom.byId("wikiPageUuid").value = "";
	    dom.byId("wikiPageLabel").value = "";
	    dom.byId("wikiPageTitle").value = "";
	    dom.byId("wikiPageSummary").value = "";
	    dom.byId("wikiPageTags").value = "";
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
