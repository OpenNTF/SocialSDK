dojo.declare("ValidationTextarea", [dijit.form.ValidationTextBox,dijit.form.SimpleTextarea], {
	invalidMessage: "This value is required",
	validate: function() {
    	var v = this.inherited(arguments);
    	if(!v) {
    		this.displayMessage(this.getErrorMessage());
    	}
    	return v;
    }
});

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

function loadAPI(id,expand,filter) {
	XSP.showContent(pageGlobal.dynPanel,"api",{api:id,expand:expand,filter:filter});
	updateLabel(id);
	//updateNavSelection();
}

function toggleSection(id) {
	var d = dojo.byId(id);
	if(d) {
		if(d.style.display=="none") {
			dojo.fx.wipeIn({node:d, duration:100, onEnd:refreshFrameHeight}).play();
		} else {
			dojo.fx.wipeOut({node:d, duration:100, onEnd:refreshFrameHeight}).play();
		}
	}
}

function expandSection(id) {
	var d = dojo.byId(id);
	if(d) {
		if(d.style.display=="none") {
			dojo.fx.wipeIn({node:d, duration:100, onEnd:refreshFrameHeight}).play();
		}
	}
}

function collapseSection(id) {
	var d = dojo.byId(id);
	if(d) {
		if(d.style.display!="none") {
			dojo.fx.wipeOut({node:d, duration:100, onEnd:refreshFrameHeight}).play();
		}
	}
}

function executeService(params,details,results,callback) {
	require(['dojo','dojo/_base/array','dojo','dojo','dijit','sbt/Endpoint'], function(lang,array,ioQuery,query,registry,Endpoint) {
		//require(['dojo/_base/lang','dojo/_base/array','dojo/io-query','dojo/query','dijit/registry','sbt/Endpoint'], function(lang,array,ioQuery,query,registry,Endpoint) {
		function paramValue(name) {
			var n = dojo.query("[data-param=\""+name+"\"]",details);
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
		
		var startTs = Date.now();
		var args = {
			serviceUrl : uri,
			handleAs : "text",
			headers: {},
			loginUi: "popup",
	    	load : function(response,ioArgs) {
	    		updatePanel(results,m+" "+ep.baseUrl+this.serviceUrl,200,"",response,ioArgs,startTs);
	    		if(callback) {
	    			callback();
	    		}
	    	},
	    	error : function(error,ioArgs) {
	    		updatePanel(results,m+" "+ep.baseUrl+this.serviceUrl,error.code,"",error.message,ioArgs,startTs);
	    		if(callback) {
	    			callback();
	    		}
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

		dojo.style(dojo.query(".respProgress",results)[0],"display","");
		ep.xhr(m,args,args.postData);
	});
}

function updatePanel(id,url,code,headers,body,ioargs,startTs) {
	var hd = "";
    var headers = ioargs && ioargs.headers;
	if(headers) {
        for(var h in headers) {
            if(headers.hasOwnProperty(h)) {
            	if(hd) hd += "\n";
            	hd += h + ": " + headers[h];
            }
        }				
	}
	updateResponse(id,{url:url,status:code,headers:prettify(hd,ioargs),body:prettify(body,ioargs),startTs:startTs});
	// Should we just pretty print one div instead of the whole document?
	prettyPrint();
}

function updatePanelError(id,error) {
	updateResponse(id,{status:"Not Executed",body:error});
}

function clearResultsPanel(id,code,headers,body) {
	updateResponse(id,{});
}

function updateResponse(id,content) {
	// TEMP for Dojo 1.6
	//require(['dojo/query','sbt/dom'], function(query,dom) {
	require(['dojo','sbt/dom'], function(query,dom) {
		function update(clazz,value) {
			var pnl = dojo.query(clazz+"Panel",id)[0];
			var comp = dojo.query(clazz,id)[0];
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
		dojo.style(dojo.query(".respProgress",id)[0],"display","none");
		if(content.startTs) {
			var endTs = Date.now();
			var sec = (endTs-content.startTs)/1000;
			update(".respTime",sec);
		} else {
			update(".respTime","");
		}
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
			return vkbeautify.xml(s);
		}
		if(s.indexOf("<html")==0) {
			return vkbeautify.xml(s);
		}
		// Check for Json result
		if(s.indexOf("{")==0) {
			return vkbeautify.json(s);
		}
	} catch(e) {} // Return the initial string
	return s;
} 
