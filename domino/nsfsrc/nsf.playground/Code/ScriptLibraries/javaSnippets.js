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
 * Extract the java imports from the Java code
 */
function extractJavaImports(js) {
	var a = [];
	if(js) {
		var rx = /<%@\s*page\s+import\s*=\s*['"]([^'"]*)['"]/g;
		for(var m=rx.exec(js); m; m=rx.exec(js)) {
			var dep = m[1].split(',');
			for(var i=0; i<dep.length; i++) {
				a.push(XSP.trim(dep[i]));
			}
		}
	}
	return a;
}

function docUrl(s) {
	if(XSP.startsWith(s,"com.ibm.commons") || XSP.startsWith(s,"com.ibm.sbt")) {
		var urlRoot = "http://infolib.lotus.com/resources/social_business_toolkit/javadoc/index.html?";
		if(XSP.endsWith(s,".*")) {
			s = s.substring(0,s.length-2)+"/package-summary";
		}
		return urlRoot+s.replace(/\./g,"/")+".html";
	}
	if(XSP.startsWith(s,"java.")) {
		var urlRoot = "http://docs.oracle.com/javase/6/docs/api/index.html?";
		if(XSP.endsWith(s,".*")) {
			s = s.substring(0,s.length-2)+"/package-summary";
		}
		return urlRoot+s.replace(/\./g,"/")+".html";
	}
	
	return null;
}

function updateDocumentation() {
	var a = extractJavaImports(pageGlobal.jspEditor.getValue());
	if(a.length) {
		dojo.empty("javadoclist");
		for(var i=0; i<a.length; i++) {
			var url = docUrl(a[i]);
			if(url) {
				var li = dojo.create("li", {} , "javadoclist");
				var lk = dojo.create("a", {href: url, target: 'blank', innerHTML: a[i]} , li);
			}
		}
		dojo.style("javadoc","display","block");
	} else {
		dojo.style("javadoc","display","none");
	}
}

/**
 * Create a new snippet 
 */
function createSnippet() {
	pageGlobal.id = "";
	pageGlobal.unid = "";
	if(pageGlobal.jspEditor) {
		pageGlobal.jspEditor.setValue("");
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
			if(pageGlobal.jspEditor) pageGlobal.jspEditor.setValue(cleanSnippet(r.jsp));
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
function cleanSnippet(source) {
	if(false) { // How to parse the doc with this?
		var s = source.search(/<body>/i);
		var e = source.search(/<\/body>/i);
		if(s>=0 && e>=s+6) {
			return XSP.trim(source.substring(s+6,e));
		}
	}
	return source;
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
		// This can fail if the iFrame was redirected to a different domain
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
