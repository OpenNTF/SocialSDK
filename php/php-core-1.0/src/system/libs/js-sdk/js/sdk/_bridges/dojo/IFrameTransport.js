/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

/**
 * Social Business Toolkit SDK. 
 * Implementation of a proxy that uses an iFrame.
 */
define(['./declare','dojo/ready','dojo/_base/lang'],function(declare,ready,lang) {

return declare(null, {

	idCounter: 0,
	iframe:	null,
	ready: false,
	requests: {},
	pendings: [],
	
	constructor: function(iframeSrc){
		var self = this;
		// The DOM must be ready to get this initialized
		ready(function() {
			var i = self.iframe = dojo.create("iframe");
			i.src = iframeSrc;
			i.className = "dijitBackgroundIframe";
			dojo.style(i, "opacity", 0.1);
			dojo.body().appendChild(i);

			if(window.addEventListener) {
				window.addEventListener("message", dojo.hitch(self,self.processMessage), false );
			} else {
				window.attachEvent("onmessage", dojo.hitch(self,self.processMessage));
			}
		});
	},
	
	processMessage: function(e) {
		if(e.source==this.iframe.contentWindow) {
			var o = dojo.fromJson(e.data);
			//console.log("Transport message:\n"+e.data);
			if(o.method=="ready") {
				//console.log("Processing ready");
				this.ready = true;
				for(var r in this.pendings) {
					this._xhr(this.pendings[r]);
				}
				this.pendings = [];
			} else if(o.method=="load") {
				//console.log("Handling request with 'load'");
				var req = this.requests[o.id]; this.requests[o.id]=null;
				req.handle(o.data,o.ioArgs);
			} else if(o.method=="error") {
				//console.log("Handling request with 'error'");
				var req = this.requests[o.id]; this.requests[o.id]=null;
				var error = new Error();
				dojo.mixin(error,o.data);
				req.handle(error,o.ioArgs);
			} else {
				//console.log("Unknown iframe message:\n"+dojo.toJson(o));
			}
		}
	},
	
	xhr: function(method,args,hasBody) {
		var id = this.idCounter++;
		this.requests[id] = args;
		var msg = {
			id:				id,
			method:			method,
			args:			args,
			hasBody:		hasBody
		};
		if(this.ready) {
			//console.log("SEND MSG:\n"+dojo.toJson(msg));
			this._xhr(msg);
		} else {
			this.pendings.push(msg);
		}
	},
	
	_xhr: function(msg) {
		this.iframe.contentWindow.postMessage(dojo.toJson(msg),"*");
	}
});

});