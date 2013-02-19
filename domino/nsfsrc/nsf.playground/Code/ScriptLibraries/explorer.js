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

function executeService(params,details,results) {
	require(['dojo/_base/lang','dojo/_base/array','dojo/io-query','dojo/query','dijit/registry','sbt/Endpoint'], function(lang,array,ioQuery,query,registry,Endpoint) {
		function paramValue(name) {
			var n = query("[data-param=\""+name+"\"]",details);
			if(n.length>0) {
				return n[0].value;
			}
			return null;
		}
		function validate(){
			// See dijit.form._FormMixin
			var didFocus = false;
			return dojo.every(dojo.map(registry.findWidgets(dojo.byId(details)), function(widget){
				widget._hasBeenBlurred = true;
				var valid = widget.disabled || !widget.validate || widget.validate();
				if(!valid && !didFocus){
					dojo.window.scrollIntoView(widget.containerNode || widget.domNode);
					widget.focus();
					didFocus = true;
				}
				return valid;
			}), function(item){ return item; });
		}
		if(!validate()) {
			updatePanelError(results,"Invalid parameters - Please check the input fields");
			return;
		}
		var item = params.item;
		if(!params['endpoint']) {
			updatePanelError(results,"No endpoint specified in the API description");
			return;
		}
		var ep = Endpoint.find(params['endpoint']);
		if(!ep) {
			updatePanelError(results,"Endpoint {0} specified in the API description is invalid",params['endpoint']);
			return;
		}
		var m = item.http_method.toUpperCase();
		if(!m) {
			updatePanelError(results,"No HTTP method specified in the API description");
			return;
		}
		if(m!="GET"&&m!="POST"&&m!="PUT"&&m!="DELETE") {
			updatePanelError(results,"Invalid HTTP method "+m);
			return;
		}
		
		// Transform the URI with the macros
		var uri = item.uri;
		if(item.uriParameters) {
			var subst = {}
			var pp = item.uriParameters;
			for(var i=0; i<pp.length; i++) {
				var r = paramValue(pp[i].name);
				if(r) {
					subst[pp[i].name] = r;
				}
			}
			uri = lang.replace(uri,subst);
			//alert("URI="+uri)
		}
		
		// Compose the query string
		if(item.queryParameters) {
			var qp = {}
			var pp = item.queryParameters;
			for(var i=0; i<pp.length; i++) {
				var r = paramValue(pp[i].name);
				if(r) {
					qp[pp[i].name] = r;
				}
			}
			var s = ioQuery.objectToQuery(qp)
			if(s) {
				uri += (uri.match(/\?/) ? '&' : '?') + s; 
			}
		}
		var qs = paramValue("query-string");
		if(qs) {
			uri += (uri.match(/\?/) ? '&' : '?') + encodeURI(qs); 
		}
		
		var args = {
			serviceUrl : uri,
			handleAs : "text",
			headers: {},
			loginUi: "popup",
	    	load : function(response,ioArgs) {
	    		updatePanel(results,m+" "+ep.baseUrl+this.serviceUrl,200,"",response,ioArgs);
	    	},
	    	error : function(error,ioArgs) {
	    		updatePanel(results,m+" "+ep.baseUrl+this.serviceUrl,error.code,"",error.message,ioArgs);
	    	}
		};
		
		// Compose the POST/PUT content
		if(m=="PUT" || m=="POST") {
			var type = paramValue("post_content_type");
			if(type) {
				args.headers["Content-Type"]=type;
			}
			var data = paramValue("post_content");
			args.postData = data;
		}

		ep.xhr(m,args,args.postData);
	});
}

function updatePanel(id,url,code,headers,body,ioArgs) {
	updateResponse(id,{url:url,status:code,headers:headers,body:prettify(body,ioArgs)});
}

function updatePanelError(id,error) {
	updateResponse(id,{status:"Not Executed",body:error});
}

function clearResultsPanel(id,code,headers,body) {
	updateResponse(id,{});
}

function updateResponse(id,content) {
	require(['dojo/query','sbt/dom'], function(query,dom) {
		function update(clazz,value) {
			var pnl = query(clazz+"Panel",id)[0];
			var comp = query(clazz,id)[0];
			if(value) {
				dojo.style(pnl,"display","");
				dom.setText(comp,value);
				return comp;
			} else {
				dojo.style(pnl,"display","none");
				dom.setText(comp,"");
				return null;
			}
		}
		update(".respUrl",content.url);
		var stat = update(".respCode",content.status);
		if(stat) {
			dojo.style(stat,"color",content.status!=200?"red":"black");
		}
		update(".respHeaders",content.headers);
		update(".respBody",content.body);
	});
}

function prettify(s,ioArgs) {
	try {
		if(!s) {
			return s;
		}
		s = s.trim();
		// Check for XML result
		if(s.indexOf("<?xml")==0) {
			return prettifyXml(s);
		}
		if(s.indexOf("<html")==0) {
			return prettifyXml(s);
		}
		// Check for Json result
		if(s.indexOf("{")==0) {
			return prettifyJson(s);
		}
	} catch(e) {} // Return the initial string
	return s;
} 

// TEMP
function prettifyXml(xml) {
	var reg = /(>)(<)(\/*)/g;
    var wsexp = / *(.*) +\n/g;
    var contexp = /(<.+>)(.+\n)/g;
    xml = xml.replace(reg, '$1\n$2$3').replace(wsexp, '$1\n').replace(contexp, '$1\n$2');
    var pad = 0;
    var formatted = '';
    var lines = xml.split('\n');
    var indent = 0;
    var lastType = 'other';
    // 4 types of tags - single, closing, opening, other (text, doctype, comment) - 4*4 = 16 transitions 
    var transitions = {
        'single->single'    : 0,
        'single->closing'   : -1,
        'single->opening'   : 0,
        'single->other'     : 0,
        'closing->single'   : 0,
        'closing->closing'  : -1,
        'closing->opening'  : 0,
        'closing->other'    : 0,
        'opening->single'   : 1,
        'opening->closing'  : 0, 
        'opening->opening'  : 1,
        'opening->other'    : 1,
        'other->single'     : 0,
        'other->closing'    : -1,
        'other->opening'    : 0,
        'other->other'      : 0
    };

    for (var i=0; i < lines.length; i++) {
        var ln = lines[i];
        var single = Boolean(ln.match(/<.+\/>/)); // is this line a single tag? ex. <br />
        var closing = Boolean(ln.match(/<\/.+>/)); // is this a closing tag? ex. </a>
        var opening = Boolean(ln.match(/<[^!].*>/)); // is this even a tag (that's not <!something>)
        var type = single ? 'single' : closing ? 'closing' : opening ? 'opening' : 'other';
        var fromTo = lastType + '->' + type;
        lastType = type;
        var padding = '';

        indent += transitions[fromTo];
        for (var j = 0; j < indent; j++) {
            padding += '    ';
        }

        formatted += padding + ln + '\n';
    }

    return formatted;
}

function prettifyHtml(html) {
	return prettifyXml(html);
}

function prettifyJson(json) {
	return json;
}
