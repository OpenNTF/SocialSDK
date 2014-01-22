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
define(['./declare', './jquery'], function(declare) {

    return declare(null, {
    
    	idCounter: 0,
    	iframe:	null,
    	ready: false,
    	requests: {},
    	pendings: [],
    	
    	constructor: function(iframeSrc){
    		var self = this;
    		jQuery.ready(function() {
    			var i = self.iframe = jQuery("<iframe></iframe>").attr("src", iframeSrc).addClass("dijitBackgroundIframe").css("opacity", 0.1);
    			jQuery("body").append(i);

    			if(window.addEventListener) {
    				window.addEventListener("message", jQuery.proxy(self,self.processMessage), false );
    			} else {
    				window.attachEvent("onmessage", jQuery.proxy(self,self.processMessage));
    			}
    		});
    	},
    	
    	processMessage: function(e) {
    		if(e.source==this.iframe.contentWindow) {
    			var o = jQuery.parseJSON(e.data);
    			if(o.method=="ready") {
    				this.ready = true;
    				for(var r in this.pendings) {
    					this._xhr(this.pendings[r]);
    				}
    				this.pendings = [];
    			} else if(o.method=="load") {
    				var req = this.requests[o.id]; this.requests[o.id]=null;
    				req.handle(o.data,o.ioArgs);
    			} else if(o.method=="error") {
    				var req = this.requests[o.id]; this.requests[o.id]=null;
    				var error = new Error();
    				jQuery.extend(error,o.data);
    				req.handle(error,o.ioArgs);
    			} else {
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
    			this._xhr(msg);
    		} else {
    			this.pendings.push(msg);
    		}	
    	},
    	
    	_xhr: function(msg) {
    		this.iframe.contentWindow.postMessage(JSON.stringify(msg),"*");
    	}
    });

});