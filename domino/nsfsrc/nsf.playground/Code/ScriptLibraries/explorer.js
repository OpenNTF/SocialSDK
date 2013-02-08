/**
 * Update the label in the toolbat 
 */
function updateLabel(id) {
	var tt = dojo.byId("CurrentLabel");
	if(tt) {
		tt.innerHTML = id; 
	}
}

function emptyAPI() {
	updateLabel("");
	//updateNavSelection();
}

function loadAPI(id) {
	XSP.showContent(pageGlobal.dynPanel,"api",{api:id});
	updateLabel(id);
	//updateNavSelection();
}

function toggleSection(id) {
	var d = dojo.byId(id);
	if(d) {
		if(d.style.display=="none") {
			dojo.fx.wipeIn({node:d, duration:100}).play();
		} else {
			dojo.fx.wipeOut({node:d, duration:100}).play();
		}
	}
}

function expandSection(id) {
	var d = dojo.byId(id);
	if(d) {
		if(d.style.display=="none") {
			dojo.fx.wipeIn({node:d, duration:100}).play();
		}
	}
}

function collapseSection(id) {
	var d = dojo.byId(id);
	if(d) {
		if(d.style.display!="none") {
			dojo.fx.wipeOut({node:d, duration:100}).play();
		}
	}
}

function executeService(params,panel) {
	require(['sbt/Endpoint'], function(Endpoint) {
		var ep = Endpoint.find("connections");
		var m = params.method;
		var args = {
				serviceUrl : "/profiles/atom/profile.do",
				handleAs : "text",
				content : { 
					userid: "0EE5A7FA-3434-9A59-4825-7A7000278DAA"
	    		},
	    		load : function(response) {
	    			updatePanel(panel,200,"",response);
	    		},
	    		error : function(error) {
	    			updatePanel(panel,"","",error);
	    		}
		};
		var body = null;
		ep.xhr(m,args,body);
	});
}

function updatePanel(id,code,headers,body) {
	require(['sbt/dom'], function(dom) {
		var p = dojo.byId(id);
		if(!p) return;
		var pre = p.getElementsByTagName("pre");
		dom.setText(pre[0],code);
		dom.setText(pre[1],headers);
		dom.setText(pre[2],body);
	});
}

function clearResultsPanel(id,code,headers,body) {
	updatePanel(id,"","","");
}
