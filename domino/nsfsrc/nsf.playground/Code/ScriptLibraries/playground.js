/**
 * 
 */
function updateLabel(r) {
	var tt = dojo.byId("CurrentLabel");
	if(tt) {
		var label = r ? (r.category+" / "+(r.name||"")) : "[New Snippet]";
		// Use text here!
		tt.innerHTML = label; 
	}
}

/**
 * Create a new snippet 
 */
function createSnippet() {
	playground.id = "";
	playground.unid = "";
	if(playground.htmlEditor) {
		playground.htmlEditor.setValue("");
		selectTab(playground.tabHtml);
	}
	if(playground.jsEditor) {
		playground.jsEditor.setValue("");
	}
	if(playground.cssEditor) {
		playground.cssEditor.setValue("");
	}
	if(playground.javaEditor) {
		playground.javaEditor.setValue("");
	}
	if(playground.xpagesEditor) {
		playground.xpagesEditor.setValue("");
	}
	if(playground.docPanelId) {
		dojo.byId(playground.docPanelId).innerHTML = "";
	}
	
	dojo.byId("preview").src = playground._previewFrame;	
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
			playground.id = id;
			playground.unid = r.unid;
			if(playground.htmlEditor) playground.htmlEditor.setValue(r.html);
			if(playground.jsEditor) playground.jsEditor.setValue(r.js);
			if(playground.cssEditor) playground.cssEditor.setValue(r.css);
			if(r.html.length>5) {
				selectTab(playground.tabHtml);
			} else if(r.js.length>5) {
				selectTab(playground.tabJs);
			} else if(r.css.length>5) {
				selectTab(playground.tabCss);
			} else {
				selectTab(playground.tabHtml);
			}
			if(playground.docPanelId) {
				XSP.showContent(playground.docPanelId,'main',{action:'openDocument',documentId:playground.unid})
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
	var tc = dijit.byId(playground.tabContainer);
	var pn = dijit.byId(tab);
	tc.selectChild(pn);
}

/**
 * Run
 */
function runCode(debug) {
	if(playground._loadingFrame) {
		// This can fail is the iFrame was redirected to a different domain
		// ex: OAuth dance
		try {
			var iDoc = window.frames['preview'].document;
			var b = iDoc.getElementsByTagName("body")[0];
			b.innerHTML = "<span>Loading...</span>";
		} catch(e) {} 
	}

	// Compose the HTML code
	var html = playground.htmlEditor.getValue();
	var js = playground.jsEditor.getValue();
	var css = playground.cssEditor.getValue();
	
	// Get the current environment
	var env = dojo.byId(playground.cbEnv).value;
	
	var options = {
		env: env,
		debug: debug
	}
	
	// And update the frame by executing a post to a servlet
	var form = dojo.byId("PreviewForm");
	form["fm_html"].value = html;
	form["fm_js"].value = js;
	form["fm_css"].value = css;
	form["fm_options"].value = dojo.toJson(options);
	form.submit();
}

/**
 * Show source code 
 */
function showCode() {
	// Compose the HTML code
	var html = playground.htmlEditor.getValue();
	var js = playground.jsEditor.getValue();
	var css = playground.cssEditor.getValue();
	var form = dojo.byId("PreviewForm");
	form["fm_html"].value = html;
	form["fm_js"].value = js;
	form["fm_css"].value = css;
	
	// And update the frame by executing a post to a servlet
	dojo.xhrPost({
		form: form,
		handleAs: "text",
		load: function(data){
			window._codeData = data; 
			//alert(data);
			XSP.openDialog(playground.codeDialog);			
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
	// When a view component is used
	// Browse all the link and find the snippet with the id
	// Why is this not working with a child id?
	//dojo.query("a",playground.viewNavPanel).forEach(function(node, index, nodelist){
	if(dojo.byId(playground.viewNavPanel)) {
		dojo.query("a").forEach(function(node, index, nodelist){
			if(node.href&&node.href.indexOf("#snippet")>=0) {
				if(playground.id && node.href.indexOf(playground.id)>=0) {
					dojo.addClass(node.parentNode,"lotusSelected")
				} else {
					dojo.removeClass(node.parentNode,"lotusSelected")
				}
			}
		});
	}
	// When a tree is created
	if(dojo.byId(playground.snippetsTree)) {
		treeSelectId(playground.snippetsTree,playground.id);
	}
}


//
// Tree helpers
//

function treeCollapseAll(tree) {
	tree = dijit.byId(tree);
	if(tree.collapseAll) { // 1.8 and later
		tree.collapseAll();
	} else {
        function collapse(node) {
        	if(node!=tree.rootNode) {
        		tree._collapseNode(node);
        	}
            dojo.map(node.getChildren(),collapse);
        }
        return collapse(tree.rootNode);
    }
}

function treeExpandAll(tree) { 
	tree = dijit.byId(tree);
	if(tree.expandAll) { // 1.8 and later
		tree.expandAll();
	} else {
        function expand(node) {
        	if(node!=tree.rootNode) {
        		tree._expandNode(node);
        	}
            dojo.map(node.getChildren(),expand);
        }
        return expand(tree.rootNode);
    }
}

function treeExpandId(tree,id) {
	tree = dijit.byId(tree);
	function itemPath(model,items,item){
	    items.push(item.id);
	    if(item.id==id){
	    	return items;
	    }
	    var cc = item.children;
	    for(var i in cc){
	        if(itemPath(model,items,cc[i])) {
	        	return items;
	        }
	    }
	    items.pop();
	    return undefined;
	}
	return itemPath(tree.model,[],tree.model.root);
}

function treeSelectId(tree,id) {
	tree = dijit.byId(tree);
    var path = treeExpandId(tree,id);
    //console.log('find path: ['+path+']')
    if(path){
    	//tree.attr('path',path)
    }
}

/**
 * Cross browser utilities
 */
function fireOnClick(target){
	target = dojo.byId(target);
	if(target) {
		if(dojo.isIE) {
			target.fireEvent('onclick');
		} else {
			var o = document.createEvent('MouseEvents');
			o.initEvent( 'click', true, false );
			target.dispatchEvent(o);
		}
	}
}
