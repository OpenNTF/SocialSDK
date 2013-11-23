/**
 * JavaScript library used to handle properties within code pieces %{...}
 */
function readParams() {
	var rx = /\%\{(.*)\}/g;
	var p = [];
	for(var i=0; i<arguments.length; i++) {
		var m = rx.exec(arguments[i]);
		if(m) {
			var o = {}
			while(m!=null) {
				var props = m[1].split('|');
				for(var k=0; k<props.length; k++) {
					var prop = props[k].split('=');
					if(prop.length==1) {
						o['name'] = prop[0]
					} else {
						o[prop[0]] = prop[1]
					}
				}
				m = rx.exec(arguments[i]);
			}
			p.push(o);
		}
	}
}

function showParams() {
	if(pageGlobal.previewStack) {
		selectStack(pageGlobal.previewParams);
	}
}

// Gather the paraneter values from the dynamic panel
function gatherParams() {
	var params = {};
	var e = dojo.byId("params");
	if(e) {
		var inps = e.getElementsByTagName("input");
		for(var i=0; i<inps.length; i++) {
			var n = inps[i].name.substring(2);
			var v = inps[i].value;
			params[n] = v;
		}
	}
	if(dojo.hash()) {
		var obj = dojo.queryToObject(dojo.hash());
		dojo.mixin(params,obj);
	}
	return params;
}

// Create the dynamic panel for properties
function createPropertyPanel(params) {
	require(["dojo/dom-style","dojo/dom-construct"], function(domStyle,dom){
		if(params && params.length) {
			dom.empty("previewParams");
			var html = "<table>";
			html += "<tr><td colspan='2' style='font-weight:bold'>Parameters</td></tr>";
			for(var i=0; i<params.length; i++) {
				var p = params[i];
				html += "<tr><td>";
				html += p.name;
				html += "</td><td>";
				html += "<input type='text' name='__"+p.name+"' id='__"+p.name+"'size='80'/>";
				html += "</td></tr>";
			}
			html += "</table>";
			dojo.byId("previewParams").innerHTML = html;
			for(var i=0; i<params.length; i++) {
				var p = params[i];
				if(p.value) {
					dojo.byId("__"+p.name).value = p.value;
				}
			}
			domStyle.set("previewEmpty","display","none");
			domStyle.set("previewParams","display","block");
		} else {
			domStyle.set("previewEmpty","display","block");
			domStyle.set("previewParams","display","none");
		}
	});	
}
function shouldAutoExec(params) {
	if(params && params.length) {
		for(var i=0; i<params.length; i++) {
			var p = params[i];
			if(!p.value && !p.optional) {
				return false;
			}
		}
	}
	return true;
}
