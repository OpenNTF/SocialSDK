/**
 * Update the label in the toolbar 
 */
function updateLabel(r) {
	var tt = dojo.byId("CurrentLabel");
	if(tt) {
		var label = r ? (r.category+"/"+(r.name||"")) : "[New Snippet]";
		// Use text here!
		tt.innerHTML = label; 
	}
}


/**
 * Extract the AMD module from the JS code
 */
function extractAMDModules(js) {
	var a = [];
	if(js) {
		var rx = /require\s*\(\s*\[\s*((?:,?\s*['"][^'"]*['"]\s*)*)\]/g;
		for(var m=rx.exec(js); m; m=rx.exec(js)) {
			var dep = m[1];
			var rx2 = /['"]([^'"]*)['"]/g;
			for(var m2=rx2.exec(dep); m2; m2=rx2.exec(dep)) {
				a.push(m2[1]);
			}
		}
	}
	return a;
}

function docUrl(s) {
	if(XSP.startsWith(s,"sbt/")) {
		var urlRoot = "http://infolib.lotus.com/resources/social_business_toolkit/jsdoc/modules/"
		return urlRoot+s.replace(/\//g,".")+".html";
	}
	return null;
}

function updateDocumentation() {
	var a = extractAMDModules(pageGlobal.jsEditor.getValue());
	if(a.length) {
		dojo.empty("jsdoclist");
		for(var i=0; i<a.length; i++) {
			var url = docUrl(a[i])
			if(url) {
				var li = dojo.create("li", {} , "jsdoclist");
				var lk = dojo.create("a", {href: url, target: 'blank', innerHTML: a[i]} , li);
			}
		}
		dojo.style("jsdoc","display","block");
	} else {
		dojo.style("jsdoc","display","none");
	}
}


/**
 * Create a new snippet 
 */
function createSnippet() {
	pageGlobal.id = "";
	pageGlobal.unid = "";
	if(pageGlobal.htmlEditor) {
		pageGlobal.htmlEditor.setValue("");
		selectTab(pageGlobal.tabHtml);
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
	
	createPropertyPanel(null);
	selectStack(pageGlobal.previewParams);
	updateLabel(null);
	updateNavSelection();
	updateDocumentation();
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
			if(pageGlobal.htmlEditor) pageGlobal.htmlEditor.setValue(r.html);
			if(pageGlobal.jsEditor) pageGlobal.jsEditor.setValue(r.js);
			if(pageGlobal.cssEditor) pageGlobal.cssEditor.setValue(r.css);
			if(pageGlobal.propertiesEditor) pageGlobal.propertiesEditor.setValue(r.properties);
			if(pageGlobal.documentationPanel) pageGlobal.documentationPanel.innerHTML = r.documentation;
			createPropertyPanel(pageGlobal.params);
			if(pageGlobal.previewStack) {
				selectStack(pageGlobal.previewParams);
			}
			updateLabel(r);
			updateNavSelection();
			updateDocumentation();
			if(shouldAutoExec(pageGlobal.params)) {
				runCode(false);
			}
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
	

	// Compose the HTML code
	var html = pageGlobal.htmlEditor.getValue();
	var js = pageGlobal.jsEditor.getValue();
	var css = pageGlobal.cssEditor.getValue();
	var properties = pageGlobal.propertiesEditor.getValue();
	
	// Get the current environment
	var env = dojo.byId(pageGlobal.cbEnv).value;
	var lib = dojo.byId(pageGlobal.cbLibrary).selectedIndex;
	var params = gatherParams();
	var options = {
		env: env,
		debug: debug,
		lib: lib,
		params: params
	}

	if(pageGlobal.previewStack) {
		selectStack(pageGlobal.previewPreview);
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
