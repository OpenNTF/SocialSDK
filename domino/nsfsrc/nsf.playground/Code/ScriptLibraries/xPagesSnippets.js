/**
 * Update the label in the toolbar 
 */
function updateLabel(r) {
	var tt = dojo.byId("CurrentLabel");
	if(tt) {
		var label = r ? (r.category+"/"+(r.name||"")) : "";
		tt.innerHTML = label; 
	}
}

/**
 * Create a new snippet 
 */
function createSnippet() {
	pageGlobal.id = "";
	pageGlobal.unid = "";
	if(pageGlobal.xPagesEditor) {
		pageGlobal.xPagesEditor.setValue("");
	}
	if(pageGlobal.propertiesEditor) {
		pageGlobal.propertiesEditor.setValue("");
	}
	if(pageGlobal.documentationPanel) {
		pageGlobal.documentationPanel.innerHTML = "";
	}
	
	createPropertyPanel(null);
	selectStack(pageGlobal.previewParams);
	updateLabel(null);
	updateNavSelection();
}

/**
 * Load a snippet from the server using a JSON RPC call. 
 */
function loadSnippet(id) {
	var deferred = server.loadSnippet(id)
	deferred.addCallback(function(r) {
		if(r.status=="ok") {
			pageGlobal.id = id;
			pageGlobal.unid = r.unid;
			pageGlobal.params = r.params;
			if(pageGlobal.xPagesEditor) pageGlobal.xPagesEditor.setValue(r.xPages);
			if(pageGlobal.propertiesEditor) pageGlobal.propertiesEditor.setValue(r.properties);
			if(pageGlobal.documentationPanel) pageGlobal.documentationPanel.innerHTML = r.documentation;
			selectTab(pageGlobal.tabXPages);
			createPropertyPanel(pageGlobal.params);
			if(pageGlobal.previewStack) {
				selectStack(pageGlobal.previewParams);
			}
			updateLabel(r);
			updateNavSelection();
			runCode(false);
		} else {
			alert("Error:\n"+r.msg);
		}
	});	
}
function selectTab(tab) {
	var tc = dijit.byId(pageGlobal.tabContainer);
	var pn = dijit.byId(tab);
	tc.selectChild(pn);
}
function selectStack(stack) {
	var tc = dijit.byId(pageGlobal.previewStack);
	var pn = dijit.byId(stack);
	tc.selectChild(pn);
}

/**
 * Run
 */
function runCode(debug) {
	if(pageGlobal._loadingFrame) {
		// This can fail is the iFrame was redirected to a different domain
		// ex: OAuth dance
		try {
			var iDoc = window.frames['preview'].document;
			var b = iDoc.getElementsByTagName("body")[0];
			b.innerHTML = "<span>Loading...</span>";
		} catch(e) {} 
	}
	
	// Get the current environment
	var env = dojo.byId(pageGlobal.cbEnv).value;
	var params = gatherParams();
	var options = {
		env: env,
		debug: debug,
		params: params
	}

	if(pageGlobal.previewStack) {
		selectStack(pageGlobal.previewPreview);
	}
	
	// And update the frame by executing a post to a servlet
	var form = dojo.byId("PreviewForm");
	form["fm_id"].value = pageGlobal.unid;
	form["fm_options"].value = dojo.toJson(options);
	form.submit();
}

/**
 * Update the selection for view.
 */
function updateNavSelection() {
	// When a tree is created
	if(dojo.byId(pageGlobal.snippetsTree)) {
		treeSelectId(pageGlobal.snippetsTree,pageGlobal.id);
	}
}
