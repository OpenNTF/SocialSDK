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
	if(pageGlobal.jsonEditor) {
		pageGlobal.jsonEditor.setValue("");
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
			if(pageGlobal.jsonEditor) pageGlobal.jsonEditor.setValue(r.json);
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
		try {
			var iDoc = window.frames['preview'].document;
			var b = iDoc.getElementsByTagName("body")[0];
			b.innerHTML = "<span>Loading...</span>";
		} catch(e) {} 
	}

	// Compose the HTML code
	var gadget = pageGlobal.gadgetEditor.getValue();
	var html = pageGlobal.htmlEditor.getValue();
	var js = pageGlobal.jsEditor.getValue();
	var css = pageGlobal.cssEditor.getValue();
	var json = pageGlobal.jsonEditor.getValue();
	var properties = pageGlobal.propertiesEditor.getValue();
	
	// Get the current options
	var options = {
		debug: debug
	}

	// Calculate a random gadget id to cheat shindig cache and have a key on the server side
	var gadgetId=(Math.floor(Math.random()*1000000000)).toString(36);
	
	// Send the gadget content by posting the data
	var form = dojo.byId("PreviewForm");
	form["fm_gadgetid"].value = gadgetId;
	form["fm_gadget"].value = gadget;
	form["fm_html"].value = html;
	form["fm_js"].value = js;
	form["fm_css"].value = css;
	//form["fm_json"].value = json; // Consume locally - not served by our server
	form["fm_properties"].value = properties;
	form["fm_options"].value = dojo.toJson(options);
	dojo.xhrPost({
		form: form,
		handleAs: "text",
		load: function(data){
			// https require the certificates on the server... 
			var url = form.action+"/"+gadgetId+"/gadget.xml";
			url = url.replace("https://","http://");
			initGadget(url,json);
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

var globalOSContainer = null;
var globalOSGadget = null;
var globalOSSite = null;

function initOSContainer() {
	globalOSContainer = new osapi.container.Container({});
	globalOSGadget = dojo.byId("osgadget");
	globalOSSite = globalOSContainer.newGadgetSite(globalOSGadget);
	
	//var asurl = "http://connections.swg.usma.ibm.com/connections/resources/web/com.ibm.social.as/gadget/ActivityStreamNotes.xml";
	globalASGadget = dojo.byId("asgadget");
	if(globalASGadget) {
		var asurl = "http://priandqs.swg.usma.ibm.com:81/connections/resources/web/com.ibm.social.as/gadget/ActivityStreamNotes.xml";
		globalASSite = globalOSContainer.newGadgetSite(globalASGadget);
		var viewParams = {}
		var renderParams = {}
		renderParams[osapi.container.RenderParam.HEIGHT] = '100%'; 
		renderParams[osapi.container.RenderParam.WIDTH] = '100%';
		globalASSite.close();
		globalOSContainer.navigateGadget(globalASSite,asurl,viewParams,renderParams);
	}
}

function initGadget(url,dataModel) {
	if(dataModel && dataModel.length>0) {
		renderEmbeddedExperience(url,dataModel);
	} else {
		renderGadget(url);
	}
}
function renderGadget(url) {
	var viewParams = {}
	var renderParams = {}
	renderParams[osapi.container.RenderParam.HEIGHT] = '100%'; 
	renderParams[osapi.container.RenderParam.WIDTH] = '100%';
	
	globalOSSite.close();
	globalOSContainer.navigateGadget(globalOSSite,url,viewParams,renderParams);
}

function renderEmbeddedExperience(url, dataModel) {
	globalOSSite.close();
	globalOSContainer.preloadGadget(url, function(metadata) {
      if(metadata && metadata[url]) {
        //self.gadgetToolbar.setGadgetMetadata(metadata[url]);
        var siteNode = dojo.byId("osgadget");
        oDataModel = JSON.parse(dataModel),
        renderParams = {};
        oDataModel.gadget = url;
        renderParams[osapi.container.ee.RenderParam.GADGET_RENDER_PARAMS] = {};
        renderParams[osapi.container.ee.RenderParam.GADGET_RENDER_PARAMS][osapi.container.RenderParam.HEIGHT] = '100%';
        renderParams[osapi.container.ee.RenderParam.GADGET_RENDER_PARAMS][osapi.container.RenderParam.WIDTH] = '100%';
        renderParams[osapi.container.ee.RenderParam.GADGET_VIEW_PARAMS] = {"gadgetUrl" : url}; 

        globalOSContainer.ee.navigate(siteNode, oDataModel, renderParams, function(site){
          // Set the site so we can clean it up when we switch gadgets
        	globalOSSite = site;
        });
      } else {
        console.error('There was an error preloading the embedded experience.');
      }
    });
}