/**
 * Update the label in the toolbar 
 */
function updateLabel(r) {
	var tt = dojo.byId("CurrentLabel");
	if(tt) {
		var label = r ? (r.category+"/"+(r.name||"")) : "[New Gadget]";
		// Use text here!
		tt.innerHTML = label; 
	}
}

/**
 * Create a new gadget 
 */
function createSnippet() {
	pageGlobal.id = "";
	pageGlobal.unid = "";
	if(pageGlobal.gadgetEditor) {
		pageGlobal.gadgetEditor.setValue("");
		selectTab(pageGlobal.tabGadget);
	}
	if(pageGlobal.htmlEditor) {
		pageGlobal.htmlEditor.setValue("");
	}
	if(pageGlobal.jsEditor) {
		pageGlobal.jsEditor.setValue("");
	}
	if(pageGlobal.cssEditor) {
		pageGlobal.cssEditor.setValue("");
	}
	if(pageGlobal.propertiesEditor) {
		pageGlobal.propertiesEditor.setValue("");
	}
	if(pageGlobal.documentationPanel) {
		pageGlobal.documentationPanel.innerHTML = "";
	}
	
	dojo.byId("preview").src = pageGlobal._previewFrame;	
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
			if(pageGlobal.gadgetEditor) { pageGlobal.gadgetEditor.setValue(r.gadget); selectTab(pageGlobal.tabGadget); }
			if(pageGlobal.htmlEditor) pageGlobal.htmlEditor.setValue(r.html);
			if(pageGlobal.jsEditor) pageGlobal.jsEditor.setValue(r.js);
			if(pageGlobal.cssEditor) pageGlobal.cssEditor.setValue(r.css);
			if(pageGlobal.propertiesEditor) pageGlobal.propertiesEditor.setValue(r.properties);
			if(pageGlobal.documentationPanel) pageGlobal.documentationPanel.innerHTML = r.documentation;
			selectTab(pageGlobal.tabGadget);
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

	// Compose the HTML code
	var html = pageGlobal.htmlEditor.getValue();
	var js = pageGlobal.jsEditor.getValue();
	var css = pageGlobal.cssEditor.getValue();
	var properties = pageGlobal.propertiesEditor.getValue();
	
	// Get the current environment
	var env = dojo.byId(pageGlobal.cbEnv).value;
	var lib = dojo.byId(pageGlobal.cbLibrary).selectedIndex;
	var options = {
		env: env,
		debug: debug,
		lib: lib
	}
	
	// And update the frame by executing a post to a servlet
	var form = dojo.byId("PreviewForm");
	form["fm_html"].value = html;
	form["fm_js"].value = js;
	form["fm_css"].value = css;
	form["fm_properties"].value = properties;
	form["fm_options"].value = dojo.toJson(options);
	form.submit();
}

/**
 * Show source code 
 */
function showCode() {
	// Compose the HTML code
	var html = pageGlobal.htmlEditor.getValue();
	var js = pageGlobal.jsEditor.getValue();
	var css = pageGlobal.cssEditor.getValue();
	var properties = pageGlobal.propertiesEditor.getValue();
	var form = dojo.byId("PreviewForm");
	form["fm_html"].value = html;
	form["fm_js"].value = js;
	form["fm_css"].value = css;
	form["fm_properties"].value = properties;
	
	// And update the frame by executing a post to a servlet
	dojo.xhrPost({
		form: form,
		handleAs: "text",
		load: function(data){
			window._codeData = data; 
			//alert(data);
			XSP.openDialog(pageGlobal.codeDialog);			
		},
        error: function(error){
			alert(error);
		}			
	});
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

function initOSContainer() {
	// Call close on mySite and call navigate again with a new URL
	var container = new osapi.container.Container({});
	var myGadget = dojo.byId("mygadget");
	var mySite = container.newGadgetSite(myGadget);
	var viewParams = {}
	var renderParams = {}
	renderParams[osapi.container.RenderParam.HEIGHT] = '100%'; 
	renderParams[osapi.container.RenderParam.WIDTH] = '100%'; 
	container.navigateGadget(mySite,"https://raw.github.com/OpenSocial/explorer/master/gadget-specs/src/main/specs/welcome/gadget.xml",viewParams,renderParams);
}
